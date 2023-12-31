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
package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleFactory;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

/**
 * A {@link XMLMatchableReader} for {@link Movie}s.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class PlayerXMLReader extends XMLMatchableReader<Player, Attribute> implements
FusibleFactory<Player, Attribute> {

/* (non-Javadoc)
* @see de.uni_mannheim.informatik.wdi.model.io.XMLMatchableReader#initialiseDataset(de.uni_mannheim.informatik.wdi.model.DataSet)
*/
@Override
protected void initialiseDataset(DataSet<Player, Attribute> dataset) {
super.initialiseDataset(dataset);

// the schema is defined in the Movie class and not interpreted from the file, so we have to set the attributes manually
dataset.addAttribute(Player.NAME);
dataset.addAttribute(Player.BIRTHDATE);
dataset.addAttribute(Player.NATIONALITY);
dataset.addAttribute(Player.CLUB);
dataset.addAttribute(Player.WEIGHT);
dataset.addAttribute(Player.HEIGHT);
dataset.addAttribute(Player.JERSEY_NUMBER);
dataset.addAttribute(Player.LEAGUE);
dataset.addAttribute(Player.CURRENT_MARKET_VALUE);
dataset.addAttribute(Player.WAGE);
dataset.addAttribute(Player.PREFERRED_FOOT);
dataset.addAttribute(Player.POSITIONS);







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
	player.setLeague(getValueFromChildElement(node, "league"));
	player.setPreferred_foot(getValueFromChildElement(node, "preferred_foot"));
	
	
	// load the list of positions
	List<String> positions = getListFromChildElement(node, "positions");
	if (positions != null) {
        try {
        	player.setPositions(positions);
        } catch (NumberFormatException e) {
            // Handle the exception as needed (e.g., log it or throw a more specific exception)
            e.printStackTrace();
        }
    }


	// convert the date string into a DateTime object
	String weightString = getValueFromChildElement(node, "weight");
	
    if (weightString != null && !weightString.isEmpty()) {
        try {
            int weight = Integer.parseInt(weightString);
            player.setWeight(weight);
        } catch (NumberFormatException e) {
            // Handle the exception as needed (e.g., log it or throw a more specific exception)
            e.printStackTrace();
        }
    }
    
	String heightString = getValueFromChildElement(node, "height");
	
    if (heightString != null && !heightString.isEmpty()) {
        try {
            int height = Integer.parseInt(heightString);
            player.setHeight(height);
        } catch (NumberFormatException e) {
            // Handle the exception as needed (e.g., log it or throw a more specific exception)
            e.printStackTrace();
        }
    }
    
	String jersey_numberString = getValueFromChildElement(node, "jersey_number");
	
    if (jersey_numberString != null && !jersey_numberString.isEmpty()) {
        try {
        	int jersey_number = Integer.parseInt(jersey_numberString);
            player.setJersey_number(jersey_number);
        } catch (NumberFormatException e) {
            // Handle the exception as needed (e.g., log it or throw a more specific exception)
            e.printStackTrace();
        }
    }
    
	String current_market_valueString = getValueFromChildElement(node, "current_market_value");
	
    if (current_market_valueString != null && !current_market_valueString.isEmpty()) {
        try {
            int current_market_value = Integer.parseInt(current_market_valueString);
            player.setCurrent_market_value(current_market_value);
        } catch (NumberFormatException e) {
            // Handle the exception as needed (e.g., log it or throw a more specific exception)
            e.printStackTrace();
        }
    }
    
	String wageString = getValueFromChildElement(node, "wage");
	
    if (wageString != null && !wageString.isEmpty()) {
        try {
            int wage = Integer.parseInt(wageString);
            player.setWage(wage);
        } catch (NumberFormatException e) {
            // Handle the exception as needed (e.g., log it or throw a more specific exception)
            e.printStackTrace();
        }
    }
    
    
    
    try {
        String date = getValueFromChildElement(node, "birthdate");
        if (date != null && !date.isEmpty()) {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd")
                    .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter(Locale.ENGLISH);
            LocalDateTime dt = LocalDateTime.parse(date, formatter);
            if (dt != null) {
                player.setBirthdate(dt);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
   
	
	return player;
	}
	
	@Override
	public Player createInstanceForFusion(RecordGroup<Player, Attribute> cluster) {
	
	List<String> ids = new LinkedList<>();
	
	for (Player p : cluster.getRecords()) {
		ids.add(p.getIdentifier());
	}
	
	Collections.sort(ids);
	
	String mergedId = StringUtils.join(ids, '+');
	
	return new Player(mergedId, "fused");
	}

}
