/*
 * Copyright (c) 2017 Data and Web Science Group, University of Mannheim, Germany (http://dws.informatik.uni-mannheim.de/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Node;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

/**
 * A {@link XMLMatchableReader} for {@link Movie}s.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class PlayerXMLReader extends XMLMatchableReader<Player, Attribute>  {

	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.model.io.XMLMatchableReader#initialiseDataset(de.uni_mannheim.informatik.wdi.model.DataSet)
	 */
	@Override
	protected void initialiseDataset(DataSet<Player, Attribute> dataset) {
		super.initialiseDataset(dataset);
		
	}
	
	@Override
	public Player createModelFromElement(Node node, String provenanceInfo) {
		String id = getValueFromChildElement(node, "id");

		// create the object with id and provenance information
		Player player = new Player(id, provenanceInfo);

		// fill the attributes
		player.setName(getValueFromChildElement(node, "name"));
		player.setNationality(getValueFromChildElement(node, "nationality"));
		player.setClub(getValueFromChildElement(node, "club"));
		
		// convert the jersey number string into a Integer object
		try {
			String jersey_number_str = getValueFromChildElement(node, "jersey_number");
			if (jersey_number_str != null && !jersey_number_str.isEmpty()) {
				int jersey_number = Integer.parseInt(jersey_number_str);
	            player.setJersey_number(jersey_number);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// convert the current market value string into a Integer object
		try {
			String current_mv_str = getValueFromChildElement(node, "current_mv");
			if (current_mv_str != null && !current_mv_str.isEmpty()) {
				int current_mv = Integer.parseInt(current_mv_str);
	            player.setCurrent_market_value(current_mv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// convert the date string into a DateTime object
		try {
			String birthdate = getValueFromChildElement(node, "birthdate");
			if (birthdate != null && !birthdate.isEmpty()) {
				DateTimeFormatter formatter = new DateTimeFormatterBuilder()
				        .appendPattern("yyyy-MM-dd")
				        .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
				        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
				        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
				        .toFormatter(Locale.ENGLISH);
				LocalDateTime dt = LocalDateTime.parse(birthdate, formatter);
				player.setBirthdate(dt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// load the list of actors
		List<String> positions = getListFromChildElement(node, "positions");
		player.setPositions(positions);

		return player;
	}

}
