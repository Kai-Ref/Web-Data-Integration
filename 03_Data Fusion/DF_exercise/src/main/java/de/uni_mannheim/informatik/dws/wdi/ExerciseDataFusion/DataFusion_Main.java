package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.BirthdateEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.ClubEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.CurrentMarketValueEvaluationRule;



import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.HeightEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.JerseyNumberEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.LeagueEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.NameEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.NationalityEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.PrefferedFootEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.WageEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.PositionsEvaluationRule;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.WeightEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.BirthdateFuserFavourSources;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.BirthdateFuserVoting;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.ClubFuserLongestString;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.ClubFuserMostRecent;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.CurrentMarketValueMedian;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.HeightFuserAverage;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.HeightFuserMedian;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.JerseyNumberFavourSources;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.JerseyNumberMostRecent;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.LeagueFuserLongestString;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.LeagueMostRecent;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.NameFuserFavourSources;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.NameFuserLongestString;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.NationalityFuserFavourSources;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.NationalityFuserLongestString;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.PositionsFuserIntersection;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.PrefferedFootMostRecent;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.WageMedian;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.WeightFuserAverage;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.WeightFuserMedian;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.PositionsFuserIntersectionKSources;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.PrefferedFootFavourSources;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.Player;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.PlayerXMLFormatter;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.PlayerXMLReader;
import de.uni_mannheim.informatik.dws.winter.datafusion.CorrespondenceSet;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEngine;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEvaluator;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionStrategy;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleDataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleHashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroupFactory;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;

import org.slf4j.Logger;

public class DataFusion_Main 
{
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

	private static final Logger logger = WinterLogManager.activateLogger("trace");
	
	public static void main( String[] args ) throws Exception
    {
		// Load the Data into FusibleDataSet
		
		//FusibleDataSet<Movie, Attribute> ds1 = new FusibleHashedDataSet<>();
		//new MovieXMLReader().loadFromXML(new File("data/input/academy_awards.xml"), "/movies/movie", ds1);
		//ds1.printDataSetDensityReport();

		//FusibleDataSet<Movie, Attribute> ds2 = new FusibleHashedDataSet<>();
		//new MovieXMLReader().loadFromXML(new File("data/input/actors.xml"), "/movies/movie", ds2);
		//ds2.printDataSetDensityReport();

		//FusibleDataSet<Movie, Attribute> ds3 = new FusibleHashedDataSet<>();
		//new MovieXMLReader().loadFromXML(new File("data/input/golden_globes.xml"), "/movies/movie", ds3);
		//ds3.printDataSetDensityReport();
		
		logger.info("*\tLoading datasets\t*");
		FusibleDataSet<Player, Attribute> ds1 = new FusibleHashedDataSet<>();
		new PlayerXMLReader().loadFromXML(new File("data/input/eafc_final.xml"), "/players/player", ds1);
		ds1.printDataSetDensityReport();

		FusibleDataSet<Player, Attribute> ds2 = new FusibleHashedDataSet<>();
		new PlayerXMLReader().loadFromXML(new File("data/input/fm23_final.xml"), "/players/player", ds2);
		ds2.printDataSetDensityReport();

		FusibleDataSet<Player, Attribute> ds3 = new FusibleHashedDataSet<>();
		new PlayerXMLReader().loadFromXML(new File("data/input/tm_final.xml"), "/players/player", ds3);
		ds3.printDataSetDensityReport();
		logger.info("*\tLoading datasets\t*");
		
	

		// Maintain Provenance
		// Scores (e.g. from rating) - how trustworthy is each dataset?
		ds1.setScore(1.5);
		ds2.setScore(1.0);
		ds3.setScore(2.0);

		// Date (e.g. last update) - How recent is each dataset?
		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
		        .appendPattern("yyyy-MM-dd")
		        .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
		        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
		        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
		        .toFormatter(Locale.ENGLISH);
		
		ds1.setDate(LocalDateTime.parse("2023-09-22", formatter));
		ds2.setDate(LocalDateTime.parse("2022-11-07", formatter));
		ds3.setDate(LocalDateTime.parse("2023-09-01", formatter));

		// load correspondences
		logger.info("*\tLoading correspondences\t*");
		CorrespondenceSet<Player, Attribute> correspondences = new CorrespondenceSet<>();
		//correspondences.loadCorrespondences(new File("data/correspondences/academy_awards_2_actors_correspondences.csv"),ds1, ds2);
//		correspondences.loadCorrespondences(new File("data/correspondences/fm_tm_correspondences.csv"),ds3, ds2);
//		correspondences.loadCorrespondences(new File("data/correspondences/ea_fm_correspondences.csv"),ds1, ds2);
		//correspondences.loadCorrespondences(new File("data/correspondences/ea_tm_ml_correspondences.csv"),ds3, ds1);
		
		
		
		//correspondences.loadCorrespondences(new File("data/correspondences/ea_fm_correspondences.csv"),ds1, ds2);
		//correspondences.loadCorrespondences(new File("data/correspondences/ea_tm_correspondences.csv"),ds3, ds1);
		//correspondences.loadCorrespondences(new File("data/correspondences/fm_tm_correspondences.csv"),ds3, ds2);

		correspondences.loadCorrespondences(new File("data/correspondences/ea_fm_ml_correspondences.csv"),ds1, ds2);
		correspondences.loadCorrespondences(new File("data/correspondences/ea_tm_ml_correspondences.csv"),ds3, ds1);
		//correspondences.loadCorrespondences(new File("data/correspondences/fm_tm_correspondences.csv"),ds3, ds2);


		//correspondences.loadCorrespondences(new File("data/correspondences/Correspondences_very_good_ML_ea_2_fm.csv"),ds1, ds2);
		//correspondences.loadCorrespondences(new File("data/correspondences/correspondences_very_good_ml_tm_ea.csv"),ds3, ds1);
		//correspondences.loadCorrespondences(new File("data/correspondences/correspondences_very_good_ml_fm_tm.csv"),ds2, ds3);
		
		//correspondences.loadCorrespondences(new File("data/correspondences/ea_fm_correspondences.csv"),ds1, ds2);
		//correspondences.loadCorrespondences(new File("data/correspondences/ea_tm_correspondences.csv"),ds3, ds1);
		correspondences.loadCorrespondences(new File("data/correspondences/fm_tm_correspondences.csv"),ds3, ds2);

		// write group size distribution
		correspondences.printGroupSizeDistribution();
		
		
		
		// load the gold standard
		logger.info("*\tEvaluating results\t*");
		DataSet<Player, Attribute> gs = new FusibleHashedDataSet<>();
		// CHANGE WITH NEW GS
		new PlayerXMLReader().loadFromXML(new File("data/goldstandard/goldstandard.xml"), "/players/player", gs);

		for(Player p : gs.get()) {
			logger.info(String.format("gs: %s", p.getIdentifier()));
		}

		// define the fusion strategy
		DataFusionStrategy<Player, Attribute> strategy = new DataFusionStrategy<>(new PlayerXMLReader());
		// write debug results to file
		strategy.activateDebugReport("data/output/debugResultsDatafusion.csv", -1, gs);
		
		strategy.addAttributeFuser(Player.NAME, new NameFuserFavourSources(),new NameEvaluationRule()); //Monge Elkan
		strategy.addAttributeFuser(Player.BIRTHDATE,new BirthdateFuserVoting(), new BirthdateEvaluationRule());
		strategy.addAttributeFuser(Player.NATIONALITY, new NationalityFuserFavourSources() ,new NationalityEvaluationRule());
		
		//Different possibilities to fuse by CLUB
		strategy.addAttributeFuser(Player.CLUB,new ClubFuserMostRecent(),new ClubEvaluationRule());
//		strategy.addAttributeFuser(Player.CLUB,new ClubFuserMostRecent(),new ClubEvaluationRule());
		strategy.addAttributeFuser(Player.WEIGHT, new WeightFuserAverage(), new WeightEvaluationRule());
		strategy.addAttributeFuser(Player.HEIGHT, new HeightFuserMedian(), new HeightEvaluationRule());
		strategy.addAttributeFuser(Player.JERSEY_NUMBER, new JerseyNumberMostRecent(), new JerseyNumberEvaluationRule());
		strategy.addAttributeFuser(Player.LEAGUE, new LeagueMostRecent(), new LeagueEvaluationRule());
		strategy.addAttributeFuser(Player.CURRENT_MARKET_VALUE, new CurrentMarketValueMedian(), new CurrentMarketValueEvaluationRule ());
		strategy.addAttributeFuser(Player.WAGE, new WageMedian(), new WageEvaluationRule());
		strategy.addAttributeFuser(Player.PREFERRED_FOOT, new PrefferedFootFavourSources(), new PrefferedFootEvaluationRule());
		strategy.addAttributeFuser(Player.POSITIONS, new PositionsFuserIntersectionKSources(2), new PositionsEvaluationRule());
		


		// create the fusion engine
		DataFusionEngine<Player, Attribute> engine = new DataFusionEngine<>(strategy);

		// print consistency report
		engine.printClusterConsistencyReport(correspondences, null);
		
		// print record groups sorted by consistency
		engine.writeRecordGroupsByConsistency(new File("data/output/recordGroupConsistencies.csv"), correspondences, null);

		// run the fusion
		logger.info("*\tRunning data fusion\t*");
		FusibleDataSet<Player, Attribute> fusedDataSet = engine.run(correspondences, null);

		// write the result
		new PlayerXMLFormatter().writeXML(new File("data/output/fused.xml"), fusedDataSet);

		// evaluate
		DataFusionEvaluator<Player, Attribute> evaluator = new DataFusionEvaluator<>(strategy, new RecordGroupFactory<Player, Attribute>());

		double accuracy = evaluator.evaluate(fusedDataSet, gs, null);

		logger.info(String.format("*\tAccuracy: %.2f", accuracy));
		fusedDataSet.printDataSetDensityReport();
		
		
		
    }
}
