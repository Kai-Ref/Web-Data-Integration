package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import java.io.File;

import org.slf4j.Logger;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.PlayerBlockingKeyByBirthyearGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.PlayerBlockingKeyByNameGenerator;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerNameComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerNameComparatorEqual;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerNameComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerNameComparatorMongeElkan;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerNameReverseComparator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerWeightComparatorRelativeDifference;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerBirthdateComparator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerBirthdateComparator10Years;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerBirthdateComparator2Years;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerBirthdateComparatorDay;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerClubComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerClubComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerClubComparatorLowerCaseJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerLeagueComparatorEqual;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerBirthdateComparator;

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
		HashedDataSet<Player, Attribute> dataTm = new HashedDataSet<>();
		new PlayerXMLReader().loadFromXML(new File("data/input/tm_final.xml"), "/players/player", dataTm);
		
		HashedDataSet<Player, Attribute> dataFm = new HashedDataSet<>();
		new PlayerXMLReader().loadFromXML(new File("data/input/fm23_final.xml"), "/players/player", dataFm);
		
		// load the training set
		MatchingGoldStandard gsTraining = new MatchingGoldStandard();
		gsTraining.loadFromCSVFile(new File("data/goldstandard/gold_standard_fm_tm_train_NEW.csv"));

		// create a matching rule
		String options[] = new String[] { "-S" };
//		String options[] = new String[] {};//{ "-S" };
		String modelType = "SimpleLogistic"; // use a logistic regression
//		String modelType = "NaiveBayesMultinomial";
//		String modelType = "NeuralNetwork";
//		String modelType = "NaiveBayesMultinomial";
//		String modelType = "RandomTree";
		
		WekaMatchingRule<Player, Attribute> matchingRule = new WekaMatchingRule<>(0.7, modelType, options);
		
		matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000, gsTraining);
		
		// add comparators
		matchingRule.addComparator(new PlayerNameComparatorJaccard());
		matchingRule.addComparator(new PlayerNameReverseComparator());
		matchingRule.addComparator(new PlayerNameComparatorLevenshtein());
		matchingRule.addComparator(new PlayerNameComparatorEqual());
		matchingRule.addComparator(new PlayerNameComparatorMongeElkan());
		
		matchingRule.addComparator(new PlayerBirthdateComparator10Years());
		matchingRule.addComparator(new PlayerBirthdateComparator2Years());
		matchingRule.addComparator(new PlayerBirthdateComparatorDay(1));
		matchingRule.addComparator(new PlayerBirthdateComparatorDay(100));
		matchingRule.addComparator(new PlayerBirthdateComparator(3));
		matchingRule.addComparator(new PlayerBirthdateComparatorDay(5));
		
		matchingRule.addComparator(new PlayerClubComparatorLowerCaseJaccard());
		
		matchingRule.addComparator(new PlayerClubComparatorJaccard());
		matchingRule.addComparator(new PlayerClubComparatorLevenshtein());
		
		matchingRule.addComparator(new PlayerLeagueComparatorEqual());
		
		
		// train the matching rule's model
		logger.info("*\tLearning matching rule\t*");
		RuleLearner<Player, Attribute> learner = new RuleLearner<>();
		learner.learnMatchingRule(dataFm, dataTm, null, matchingRule, gsTraining);
		logger.info(String.format("Matching rule is:\n%s", matchingRule.getModelDescription()));
		
		// create a blocker (blocking strategy)
//		StandardRecordBlocker<Player, Attribute> blocker = new StandardRecordBlocker<Player, Attribute>(new PlayerBlockingKeyByNameGenerator(1));

		SortedNeighbourhoodBlocker<Player, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new PlayerBlockingKeyByNameGenerator(1), 110);
		blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);
		
		// Initialize Matching Engine
		MatchingEngine<Player, Attribute> engine = new MatchingEngine<>();
		
		// Execute the matching
		logger.info("*\tRunning identity resolution\t*");
		Processable<Correspondence<Player, Attribute>> correspondences = engine.runIdentityResolution(
				dataFm, dataTm, null, matchingRule,
				blocker);

		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/correspondences_ml_fm_tm.csv"), correspondences);

		// load the gold standard (test set)
		logger.info("*\tLoading gold standard\t*");
		MatchingGoldStandard gsTest = new MatchingGoldStandard();
		gsTest.loadFromCSVFile(new File(
				"data/goldstandard/gold_standard_fm_tm_v3.csv"));
		
		// evaluate your result
		logger.info("*\tEvaluating result\t*");
		MatchingEvaluator<Player, Attribute> evaluator = new MatchingEvaluator<Player, Attribute>();
		Performance perfTest = evaluator.evaluateMatching(correspondences,
				gsTest);
		
		// print the evaluation result
		logger.info("FM <-> TM");
		logger.info(String.format(
				"Precision: %.4f",perfTest.getPrecision()));
		logger.info(String.format(
				"Recall: %.4f",	perfTest.getRecall()));
		logger.info(String.format(
				"F1: %.4f",perfTest.getF1()));
		
		
		FusibleDataSet<Player2, Attribute> fm23_fusible = new FusibleHashedDataSet<>();
		new PlayerXMLReader2().loadFromXML(new File("data/input/fm23_final.xml"), "/players/player", fm23_fusible);
//		ds2.printDataSetDensityReport();

		FusibleDataSet<Player2, Attribute> tm_fusible = new FusibleHashedDataSet<>();
		new PlayerXMLReader2().loadFromXML(new File("data/input/tm_final.xml"), "/players/player", tm_fusible);
//		ds3.printDataSetDensityReport();
		
		
		CorrespondenceSet<Player2, Attribute> correspondences2 = new CorrespondenceSet<>();
		correspondences2.loadCorrespondences(new File("data/output/correspondences_ml_fm_tm.csv"),fm23_fusible, tm_fusible);
		logger.info("*\tLoading datasets 2\t*");
		correspondences2.printGroupSizeDistribution();
		
		// Get and print the number of correspondences
		int numberOfCorrespondences = correspondences.size();
		System.out.println("Number of correspondences: " + numberOfCorrespondences);
    }
}
