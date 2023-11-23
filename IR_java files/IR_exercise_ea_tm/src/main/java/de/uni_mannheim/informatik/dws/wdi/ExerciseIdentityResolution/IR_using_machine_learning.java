package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import java.io.File;

import org.slf4j.Logger;

//Blocking
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.*;
//import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.PlayerBlockingKeyByNameGenerator;

//Comparators
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.*;
//import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerNameComparatorJaccard;
//import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerClubComparatorLowerCaseJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.PlayerXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.SortedNeighbourhoodBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.WekaMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
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
		HashedDataSet<Player, Attribute> dataTm = new HashedDataSet<>();
		new PlayerXMLReader().loadFromXML(new File("data/input/tm_final.xml"), "/players/player", dataTm);
		
		HashedDataSet<Player, Attribute> dataEa = new HashedDataSet<>();
		new PlayerXMLReader().loadFromXML(new File("data/input/eafc_final.xml"), "/players/player", dataEa);
		
		
		
		// TODO: WE STILL NEED THE TRAIN AND TEST SET TO APPLY MACHINE LEARNING ALGORITHMS
		
		
		// load the training set
		//MatchingGoldStandard gsTraining = new MatchingGoldStandard();
		//gsTraining.loadFromCSVFile(new File("data/goldstandard/gs_academy_awards_2_actors_training.csv"));

		// create a matching rule
		//String options[] = new String[] { "-S" };
		//String modelType = "SimpleLogistic"; // use a logistic regression
		//WekaMatchingRule<Movie, Attribute> matchingRule = new WekaMatchingRule<>(0.7, modelType, options);
		//matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000, gsTraining);
		
		// add comparators
		//matchingRule.addComparator(new PlayerNameComparatorEqual());
		//matchingRule.addComparator(new PlayerBirthdateComparator2Years());
		//matchingRule.addComparator(new PlayerBirthdateComparator10Years());
		//matchingRule.addComparator(new PlayerClubComparatorJaccard());
		//matchingRule.addComparator(new PlayerClubComparatorLevenshtein());
		//matchingRule.addComparator(new PlayerClubComparatorLowerCaseJaccard());
		//matchingRule.addComparator(new PlayerNameComparatorLevenshtein());
		//matchingRule.addComparator(new MovieTitleComparatorJaccard());
		
		
		// train the matching rule's model
		logger.info("*\tLearning matching rule\t*");
		//RuleLearner<Movie, Attribute> learner = new RuleLearner<>();
		//learner.learnMatchingRule(dataAcademyAwards, dataActors, null, matchingRule, gsTraining);
		//logger.info(String.format("Matching rule is:\n%s", matchingRule.getModelDescription()));
		
		// create a blocker (blocking strategy)
		//StandardRecordBlocker<Movie, Attribute> blocker = new StandardRecordBlocker<Movie, Attribute>(new MovieBlockingKeyByTitleGenerator());
//		SortedNeighbourhoodBlocker<Movie, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByDecadeGenerator(), 1);
		//blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);
		
		// Initialize Matching Engine
		//MatchingEngine<Movie, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		logger.info("*\tRunning identity resolution\t*");
		//Processable<Correspondence<Movie, Attribute>> correspondences = engine.runIdentityResolution(
		//		dataAcademyAwards, dataActors, null, matchingRule,
		//		blocker);

		// write the correspondences to the output file
		//new CSVCorrespondenceFormatter().writeCSV(new File("data/output/academy_awards_2_actors_correspondences.csv"), correspondences);

		// load the gold standard (test set)
		logger.info("*\tLoading gold standard\t*");
		//MatchingGoldStandard gsTest = new MatchingGoldStandard();
		//gsTest.loadFromCSVFile(new File(
		//		"data/goldstandard/gs_academy_awards_2_actors_test.csv"));
		
		// evaluate your result
		logger.info("*\tEvaluating result\t*");
		//MatchingEvaluator<Movie, Attribute> evaluator = new MatchingEvaluator<Movie, Attribute>();
		//Performance perfTest = evaluator.evaluateMatching(correspondences,
		//		gsTest);
		
		// print the evaluation result
		logger.info("Academy Awards <-> Actors");
		//logger.info(String.format(
		//		"Precision: %.4f",perfTest.getPrecision()));
		//logger.info(String.format(
		//		"Recall: %.4f",	perfTest.getRecall()));
		//logger.info(String.format(
		//		"F1: %.4f",perfTest.getF1()));
    }
}