package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.ActorsEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.BirthdateEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.ClubEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.DateEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.DirectorEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.HeightEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.NameEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.NationalityEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.TitleEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.WeightEvaluationRule;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.ActorsFuserUnion;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.BirthdateFuserVoting;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.ClubFuserLongestString;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.DateFuserFavourSource;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.DateFuserVoting;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.DirectorFuserLongestString;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.HeightFuser;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.NameFuserLongestString;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.NationalityFuserLongestString;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.TitleFuserShortestString;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.WeightFuser;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.FusibleMovieFactory;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.Movie;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.MovieXMLFormatter;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.MovieXMLReader;
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
		ds1.setScore(1.0);
		ds2.setScore(2.0);
		ds3.setScore(2.0);

		// Date (e.g. last update) - How recent is each dataset?
		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
		        .appendPattern("yyyy-MM-dd")
		        .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
		        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
		        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
		        .toFormatter(Locale.ENGLISH);
		
		ds1.setDate(LocalDateTime.parse("2023-01-01", formatter));
		ds2.setDate(LocalDateTime.parse("2023-01-01", formatter));
		ds3.setDate(LocalDateTime.parse("2023-01-01", formatter));

		// load correspondences
		logger.info("*\tLoading correspondences\t*");
		CorrespondenceSet<Player, Attribute> correspondences = new CorrespondenceSet<>();
		
		correspondences.loadCorrespondences(new File("data/correspondences/ea_fm_correspondences.csv"),ds1, ds2);
		correspondences.loadCorrespondences(new File("data/correspondences/ea_tm_correspondences.csv"),ds3, ds1);
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
		
		// add attribute fusers
		//strategy.addAttributeFuser(Movie.TITLE, new TitleFuserShortestString(),new TitleEvaluationRule());
		//strategy.addAttributeFuser(Movie.DIRECTOR,new DirectorFuserLongestString(), new DirectorEvaluationRule());
		//strategy.addAttributeFuser(Movie.DATE, new DateFuserFavourSource(),new DateEvaluationRule());
		//strategy.addAttributeFuser(Movie.ACTORS,new ActorsFuserUnion(),new ActorsEvaluationRule());
		
		
		strategy.addAttributeFuser(Player.NAME, new NameFuserLongestString(),new NameEvaluationRule());
		strategy.addAttributeFuser(Player.BIRTHDATE,new BirthdateFuserVoting(), new BirthdateEvaluationRule());
		strategy.addAttributeFuser(Player.NATIONALITY, new NationalityFuserLongestString() ,new NationalityEvaluationRule());
		strategy.addAttributeFuser(Player.CLUB,new ClubFuserLongestString(),new ClubEvaluationRule());
		strategy.addAttributeFuser(Player.WEIGHT, new WeightFuser(), new WeightEvaluationRule());
		strategy.addAttributeFuser(Player.HEIGHT, new HeightFuser(), new HeightEvaluationRule());

		
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
    }
}
