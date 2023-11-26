package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import java.io.File;

import org.slf4j.Logger;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.PlayerBlockingKeyByBirthdateDecadeGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.PlayerBlockingKeyByClubGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.PlayerBlockingKeyByLeagueGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.PlayerBlockingKeyByNameGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.PlayerBlockingKeyByYearGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerNameComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerNameComparatorMongeElkan;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerBirthdateComparatorDay;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerBirthdateComparator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerBirthdayComparator1Day;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerAgeComparatorAbsoluteDifference;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerHeightComparatorRelativeDifference;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerLeagueComparatorEqual;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.PlayerWeightComparatorRelativeDifference;
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
	
    public static void main( String[] args ) throws Exception
    {
		// loading data
		HashedDataSet<Player, Attribute> dataEa = new HashedDataSet<>();
		System.out.println("Hallo");
		new PlayerXMLReader().loadFromXML(new File("data/input/eafc_final.xml"), "/players/player", dataEa);
		
		HashedDataSet<Player, Attribute> dataFm = new HashedDataSet<>();
		new PlayerXMLReader().loadFromXML(new File("data/input/fm23_final.xml"), "/players/player", dataFm);
		
		// print records
		System.out.println("*\tContent of dataPlayer\t*");
		int i = 0;
		for (Player player : dataFm.get()) {
		    System.out.println("Record: " + player.toString());
		    i++;
		    System.out.println(i);
		}

		// load the gold standard 		
		logger.info("*\tLoading gold standard\t*");
		MatchingGoldStandard gsTest = new MatchingGoldStandard();
		gsTest.loadFromCSVFile(new File(
				"data/goldstandard/gold_standard_fm_ea.csv"));

		// create a matching rule
		LinearCombinationMatchingRule<Player, Attribute> matchingRule = new LinearCombinationMatchingRule<>(
				0.7);
		matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000, gsTest);
		
		// add comparators
		// Name
		//matchingRule.addComparator(new PlayerNameComparatorJaccard(), .6);
		matchingRule.addComparator(new PlayerNameComparatorMongeElkan(), 0.5);

		
		// Birthdate
		matchingRule.addComparator(new PlayerBirthdateComparatorDay(10), .3);
		
		// Nationality
		
		
		// League
		// matchingRule.addComparator(new PlayerLeagueComparatorEqual(), 1);
		
		
		// Age
		// matchingRule.addComparator(new PlayerAgeComparatorAbsoluteDifference(0.1), 1);		
		
		// Height 
		 matchingRule.addComparator(new PlayerHeightComparatorRelativeDifference(0.3), 0.1);
		
		// Weight 
		 matchingRule.addComparator(new PlayerWeightComparatorRelativeDifference(0.3), 0.1);
		

		// create a blocker (blocking strategy)
		//StandardRecordBlocker<Player, Attribute> blocker = new StandardRecordBlocker<Player, Attribute>(new PlayerBlockingKeyByLeagueGenerator(2));
		// NoBlocker<Player, Attribute> blocker = new NoBlocker<>();
		// Replace with MovieBlockingKeyByTitleGenerator with Blocker specific for Player
		// SortedNeighbourhoodBlocker<Player, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new PlayerBlockingKeyByClubGenerator(4), 160);
		SortedNeighbourhoodBlocker<Player, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new PlayerBlockingKeyByNameGenerator(1), 110);
		blocker.setMeasureBlockSizes(true);
		//Write debug results to file:
		blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);
		
		// Initialize Matching Engine
		MatchingEngine<Player, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		logger.info("*\tRunning identity resolution\t*");
		Processable<Correspondence<Player, Attribute>> correspondences = engine.runIdentityResolution(
				dataEa, dataFm, null, matchingRule,
				blocker);

		// Create a top-1 global matching
//		  correspondences = engine.getTopKInstanceCorrespondences(correspondences, 1, 0.0);

//		 Alternative: Create a maximum-weight, bipartite matching
//		 MaximumBipartiteMatchingAlgorithm<Movie,Attribute> maxWeight = new MaximumBipartiteMatchingAlgorithm<>(correspondences);
//		 maxWeight.run();
//		 correspondences = maxWeight.getResult();

		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/ea_fm_correspondences.csv"), correspondences);		
		
		logger.info("*\tEvaluating result\t*");
		// evaluate your result
		MatchingEvaluator<Player, Attribute> evaluator = new MatchingEvaluator<Player, Attribute>();
		Performance perfTest = evaluator.evaluateMatching(correspondences,
				gsTest);

		// print the evaluation result
		logger.info("Ea <-> Fm");
		logger.info(String.format(
				"Precision: %.4f",perfTest.getPrecision()));
		logger.info(String.format(
				"Recall: %.4f",	perfTest.getRecall()));
		logger.info(String.format(
				"F1: %.4f",perfTest.getF1()));
		
		FusibleDataSet<Player2, Attribute> ea_fusible = new FusibleHashedDataSet<>();
		new PlayerXMLReader2().loadFromXML(new File("data/input/eafc_final.xml"), "/players/player", ea_fusible);
//		ds3.printDataSetDensityReport();
		
		FusibleDataSet<Player2, Attribute> fm23_fusible = new FusibleHashedDataSet<>();
		new PlayerXMLReader2().loadFromXML(new File("data/input/fm23_final.xml"), "/players/player", fm23_fusible);
//		ds2.printDataSetDensityReport();

		
		CorrespondenceSet<Player2, Attribute> correspondences2 = new CorrespondenceSet<>();
		correspondences2.loadCorrespondences(new File("data/output/ea_fm_correspondences.csv"),ea_fusible, fm23_fusible);
		logger.info("*\tLoading datasets 2\t*");
		correspondences2.printGroupSizeDistribution();
    }
}
