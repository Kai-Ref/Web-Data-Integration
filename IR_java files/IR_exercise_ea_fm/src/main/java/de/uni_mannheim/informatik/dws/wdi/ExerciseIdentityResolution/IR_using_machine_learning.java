package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import java.io.File;

import org.slf4j.Logger;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerNameComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerNameComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerNameComparatorMongeElkan;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerNameComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerNameComparatorEqual;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerBirthdateComparator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerBirthdateComparatorDay;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerClubComparatorLowerCaseJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerClubComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerClubComparatorLevenshtein;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.PlayerBlockingKeyByNameGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Player2;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.PlayerXMLReader;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.PlayerXMLReader2;
import de.uni_mannheim.informatik.dws.winter.datafusion.CorrespondenceSet;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.SortedNeighbourhoodBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.WekaMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusibleDataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleHashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;



public class IR_using_machine_learning {
	
	/*
	 * Logging Options:
	 * 		default: 	level INFO	- console
	 * 		trace:		level TRACE     - console
	 * 		infoFile:	level INFO	- console/file
	 * 		traceFile:	level TRACE	- console/file
	 *  
	 * To set the log level to trace and write the log to winter.log and console, 
	 * activate the "traceFile" logger as follows:
	 *     private static final Logger logger = WinterLogManager.activateLogger("traceFile");
	 *
	 */

	private static final Logger logger = WinterLogManager.activateLogger("default");
	
    public static void main( String[] args ) throws Exception
    {
		// loading data
		logger.info("*\tLoading datasets\t*");
		HashedDataSet<Player, Attribute> dataEA= new HashedDataSet<>();
		new PlayerXMLReader().loadFromXML(new File("data/input/eafc_final.xml"), "/players/player", dataEA);
		HashedDataSet<Player, Attribute> dataFM = new HashedDataSet<>();
		new PlayerXMLReader().loadFromXML(new File("data/input/fm23_final.xml"), "/players/player", dataFM);
		
		// load the training set
		MatchingGoldStandard gsTraining = new MatchingGoldStandard();
		gsTraining.loadFromCSVFile(new File("data/goldstandard/gold_standard_fm_ea_train.csv"));
		
		
		// create a matching rule
		String options[] = new String[] {};
//		String options[] = new String[] { "-S" };
//		String modelType = "SimpleLogistic"; // use a logistic regression
//		String modelType = "NaiveBayesMultinomial";
//		String modelType = "NeuralNetwork";
//		String modelType = "RandomForest";
		String modelType = "RandomTree";
		
		
		WekaMatchingRule<Player, Attribute> matchingRule = new WekaMatchingRule<>(0.9, modelType, options);
		matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000, gsTraining);
		
		// add comparators
		matchingRule.addComparator(new PlayerNameComparatorJaccard());
		matchingRule.addComparator(new PlayerClubComparatorLowerCaseJaccard());
		matchingRule.addComparator(new PlayerNameComparatorEqual());
//		matchingRule.addComparator(new PlayerBirthdateComparator2Years());
//		matchingRule.addComparator(new PlayerBirthdateComparator10Years());
		matchingRule.addComparator(new PlayerClubComparatorJaccard());
		matchingRule.addComparator(new PlayerClubComparatorLevenshtein());
		matchingRule.addComparator(new PlayerNameComparatorLevenshtein());
		matchingRule.addComparator(new PlayerBirthdateComparator(3));
		matchingRule.addComparator(new PlayerBirthdateComparatorDay(1));
		matchingRule.addComparator(new PlayerBirthdateComparatorDay(100));
		matchingRule.addComparator(new PlayerBirthdateComparatorDay(5));
		matchingRule.addComparator(new PlayerNameComparatorMongeElkan());
		
		
		
		// train the matching rule's model
		logger.info("*\tLearning matching rule\t*");
		RuleLearner<Player, Attribute> learner = new RuleLearner<>();
		learner.learnMatchingRule(dataEA, dataFM, null, matchingRule, gsTraining);
		logger.info(String.format("Matching rule is:\n%s", matchingRule.getModelDescription()));
		
		// create a blocker (blocking strategy)
//		SortedNeighbourhoodBlocker<Player, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new PlayerBlockingKeyByNameGenerator(1), 110);
		
		StandardRecordBlocker<Player, Attribute> blocker = new StandardRecordBlocker<Player, Attribute>(new PlayerBlockingKeyByNameGenerator(1));
//		SortedNeighbourhoodBlocker<Movie, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByDecadeGenerator(), 1);
		blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);
		
		// Initialize Matching Engine
		MatchingEngine<Player, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		logger.info("*\tRunning identity resolution\t*");
		Processable<Correspondence<Player, Attribute>> correspondences = engine.runIdentityResolution(
				dataEA, dataFM, null, matchingRule,
				blocker);

		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/ML_ea_2_fm.csv"), correspondences);

		// load the gold standard (test set)
		logger.info("*\tLoading gold standard\t*");
		MatchingGoldStandard gsTest = new MatchingGoldStandard();
		gsTest.loadFromCSVFile(new File(
				"data/goldstandard/gold_standard_fm_ea_test.csv"));
		
		// evaluate your result
		logger.info("*\tEvaluating result\t*");
		MatchingEvaluator<Player, Attribute> evaluator = new MatchingEvaluator<Player, Attribute>();
		Performance perfTest = evaluator.evaluateMatching(correspondences,
				gsTest);
		
		// print the evaluation result
		logger.info("Academy Awards <-> Actors");
		logger.info(String.format(
				"Precision: %.4f",perfTest.getPrecision()));
		logger.info(String.format(
				"Recall: %.4f",	perfTest.getRecall()));
		logger.info(String.format(
				"F1: %.4f",perfTest.getF1()));
		
		
		FusibleDataSet<Player2, Attribute> ds2 = new FusibleHashedDataSet<>();
		new PlayerXMLReader2().loadFromXML(new File("data/input/eafc_final.xml"), "/players/player", ds2);
//		ds2.printDataSetDensityReport();

		FusibleDataSet<Player2, Attribute> ds3 = new FusibleHashedDataSet<>();
		new PlayerXMLReader2().loadFromXML(new File("data/input/fm23_final.xml"), "/players/player", ds3);
//		ds3.printDataSetDensityReport();
		
		
		CorrespondenceSet<Player2, Attribute> correspondences2 = new CorrespondenceSet<>();
		correspondences2.loadCorrespondences(new File("data/output/ML_ea_2_fm.csv"),ds2, ds3);
		logger.info("*\tLoading datasets 2\t*");
		correspondences2.printGroupSizeDistribution();
		int n1 = correspondences.size();
//		int n2 = correspondences2.size();
		logger.info("*\\tCorrespondences:"+ n1);
    }
}
