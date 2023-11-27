package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import java.io.File;

import org.slf4j.Logger;

// Blocking
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.*;
//import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.PlayerBlockingKeyByNameGenerator;

// Comparators
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.*;
//import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerNameComparatorJaccard;
//import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerClubComparatorLowerCaseJaccard;


import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Player2;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.PlayerXMLReader;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.PlayerXMLReader2;
import de.uni_mannheim.informatik.dws.winter.datafusion.CorrespondenceSet;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.SortedNeighbourhoodBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
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

public class IR_using_linear_combination 
{
	
	private static final Logger logger = WinterLogManager.activateLogger("default");
	
    /**
     * @param args
     * @throws Exception
     */
    public static void main( String[] args ) throws Exception
    {
		// loading data
		HashedDataSet<Player, Attribute> dataTm = new HashedDataSet<>();
		new PlayerXMLReader().loadFromXML(new File("data/input/tm_final.xml"), "/players/player", dataTm);
		
		HashedDataSet<Player, Attribute> dataEa = new HashedDataSet<>();
		new PlayerXMLReader().loadFromXML(new File("data/input/eafc_final.xml"), "/players/player", dataEa);
		
		/*
		System.out.println("*\tContent of dataPlayer\t*");
		int i = 0;
		for (Player player : dataEa.get()) {
		    System.out.println("Record: " + player.toString());
		    i++;
		    System.out.println(i);
		}*/

		// load the gold standard 		
		logger.info("*\tLoading gold standard\t*");
		MatchingGoldStandard gsTest = new MatchingGoldStandard();
		gsTest.loadFromCSVFile(new File(
				"data/goldstandard/gold_standard_ea_tm.csv"));

		// create a matching rule
		LinearCombinationMatchingRule<Player, Attribute> matchingRule = new LinearCombinationMatchingRule<>(
				0.7);
		matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000, gsTest);
		
		// add comparators
//		matchingRule.addComparator(new PlayerNameComparatorJaccard(), 0.45);
		matchingRule.addComparator(new PlayerNameComparatorMongeElkan(), 0.5);
//		matchingRule.addComparator(new PlayerClubComparatorLowerCaseJaccard(), 0.1);
//		matchingRule.addComparator(new PlayerNameComparatorEqual(), 0.3);
//		matchingRule.addComparator(new PlayerBirthdateComparator2Years(), 1);
		//matchingRule.addComparator(new PlayerBirthdateComparator10Years());
//		matchingRule.addComparator(new PlayerClubComparatorJaccard());
//		matchingRule.addComparator(new PlayerClubComparatorLevenshtein(), 0.2);
//		matchingRule.addComparator(new PlayerNameComparatorLevenshtein());
//		matchingRule.addComparator(new PlayerBirthdateComparator(3), 0.55);
//		matchingRule.addComparator(new PlayerJerseyNumberComparatorEqual(), 0.5);
		matchingRule.addComparator(new PlayerBirthdateComparatorDay(5), 0.5);
		

		// create a blocker (blocking strategy)
//		StandardRecordBlocker<Player, Attribute> blocker = new StandardRecordBlocker<Player, Attribute>(new PlayerBlockingKeyByNameGenerator());
//		StandardRecordBlocker<Player, Attribute> blocker = new StandardRecordBlocker<Player, Attribute>(new PlayerBlockingKeyByYearGenerator());
//		StandardRecordBlocker<Player, Attribute> blocker = new StandardRecordBlocker<Player, Attribute>(new PlayerBlockingKeyByDecadeGenerator());
		SortedNeighbourhoodBlocker<Player, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new PlayerBlockingKeyByNameGenerator(1), 110);
//		NoBlocker<Player, Attribute> blocker = new NoBlocker<>();
		blocker.setMeasureBlockSizes(true);
		//Write debug results to file:
		blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);
		
		
		
		
		
		// Replace with MovieBlockingKeyByTitleGenerator with Blocker specific for Player
//		SortedNeighbourhoodBlocker<Player, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByTitleGenerator(), 1);
		
		// Initialize Matching Engine
		MatchingEngine<Player, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		logger.info("*\tRunning identity resolution\t*");
		Processable<Correspondence<Player, Attribute>> correspondences = engine.runIdentityResolution(
				dataTm, dataEa, null, matchingRule,
				blocker);

		// Create a top-1 global matching
//		  correspondences = engine.getTopKInstanceCorrespondences(correspondences, 1, 0.0);

//		 Alternative: Create a maximum-weight, bipartite matching
//		 MaximumBipartiteMatchingAlgorithm<Movie,Attribute> maxWeight = new MaximumBipartiteMatchingAlgorithm<>(correspondences);
//		 maxWeight.run();
//		 correspondences = maxWeight.getResult();

		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/ea_tm_correspondences.csv"), correspondences);		
		
		logger.info("*\tEvaluating result\t*");
		// evaluate your result
		MatchingEvaluator<Player, Attribute> evaluator = new MatchingEvaluator<Player, Attribute>();
		Performance perfTest = evaluator.evaluateMatching(correspondences,
				gsTest);

		// print the evaluation result
		logger.info("Tm <-> Ea");
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
		new PlayerXMLReader2().loadFromXML(new File("data/input/tm_final.xml"), "/players/player", ds3);
//		ds3.printDataSetDensityReport();
		
		
		CorrespondenceSet<Player2, Attribute> correspondences2 = new CorrespondenceSet<>();
		correspondences2.loadCorrespondences(new File("data/output/ea_tm_correspondences.csv"),ds3, ds2);
		logger.info("*\tLoading datasets 2\t*");
		correspondences2.printGroupSizeDistribution();

    }
}
