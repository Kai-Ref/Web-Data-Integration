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
		player.setLeague(getValueFromChildElement(node, "league"));
		player.setClub(getValueFromChildElement(node, "club"));
		
		try {
			player.setWeight(Float.parseFloat(getValueFromChildElement(node, "weight")));
		} catch (Exception e) {
			player.setWeight(0);
		}
		try {
			player.setHeight(Float.parseFloat(getValueFromChildElement(node, "height")));
		} catch (Exception e) {
			player.setHeight(0);
		}
		
		


		// convert the date string into a DateTime object
		try {
			String birthdate = getValueFromChildElement(node, "birthdate");
			DateTimeFormatter formatter = new DateTimeFormatterBuilder()
			        .appendPattern("yyyy-MM-dd")
			        .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
			        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
			        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
			        .toFormatter(Locale.ENGLISH);
			if (birthdate != null && !birthdate.isEmpty()) {
				
				LocalDateTime dt = LocalDateTime.parse(birthdate, formatter);
				player.setBirthdate(dt);
			}else {
				player.setBirthdate(LocalDateTime.parse("1900-01-01", formatter));
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
