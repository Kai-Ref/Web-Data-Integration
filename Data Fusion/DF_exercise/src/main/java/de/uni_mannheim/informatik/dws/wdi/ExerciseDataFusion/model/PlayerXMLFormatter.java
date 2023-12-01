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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

/**
 * {@link XMLFormatter} for {@link Movie}s.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class PlayerXMLFormatter extends XMLFormatter<Player> {

	PositionsXMLFormatter positionFormatter = new PositionsXMLFormatter();

	@Override
	public Element createRootElement(Document doc) {
		return doc.createElement("players");
	}

	@Override
	public Element createElementFromRecord(Player record, Document doc) {
		Element player = doc.createElement("player");

		player.appendChild(createTextElement("id", record.getIdentifier(), doc));
		
		player.appendChild(createPositionsElement(record, doc));

		player.appendChild(createTextElementWithProvenance("name",
				record.getName(),
				record.getMergedAttributeProvenance(Player.NAME), doc));
		player.appendChild(createTextElementWithProvenance("nationality",
				record.getNationality(),
				record.getMergedAttributeProvenance(Player.NATIONALITY), doc));
		if (record.getBirthdate() != null) {
			
		player.appendChild(createTextElementWithProvenance("birthdate", record
				.getBirthdate().toString(), record
				.getMergedAttributeProvenance(Player.BIRTHDATE), doc));
		}
		player.appendChild(createTextElementWithProvenance("club",
				record.getClub(),
				record.getMergedAttributeProvenance(Player.CLUB), doc));
		if (record.getWeight() != 0) {
		    player.appendChild(createTextElementWithProvenance("weight",
		            Integer.toString(record.getWeight()),
		            record.getMergedAttributeProvenance(Player.WEIGHT), doc));
		}
		if (record.getHeight() != 0) {
		    player.appendChild(createTextElementWithProvenance("height",
		            Integer.toString(record.getHeight()),
		            record.getMergedAttributeProvenance(Player.HEIGHT), doc));
		}
		if (record.getJersey_number() != 0) {
		    player.appendChild(createTextElementWithProvenance("jersey_number",
		            Integer.toString(record.getJersey_number()),
		            record.getMergedAttributeProvenance(Player.JERSEY_NUMBER), doc));
		}
		if (record.getLeague() != null) {
		player.appendChild(createTextElementWithProvenance("league",
				record.getLeague(),
				record.getMergedAttributeProvenance(Player.LEAGUE), doc));
		}
		if (record.getCurrent_market_value() != 0) {
		    player.appendChild(createTextElementWithProvenance("current_market_value",
		            Integer.toString(record.getCurrent_market_value()),
		            record.getMergedAttributeProvenance(Player.CURRENT_MARKET_VALUE), doc));
		}
		if (record.getWage() != 0) {
		    player.appendChild(createTextElementWithProvenance("wage",
		            Integer.toString(record.getWage()),
		            record.getMergedAttributeProvenance(Player.WAGE), doc));
		}
		if (record.getPreferred_foot() != null) {
		player.appendChild(createTextElementWithProvenance("preferred_foot",
				record.getPreferred_foot(),
				record.getMergedAttributeProvenance(Player.PREFERRED_FOOT), doc));
		}
		return player;
	}
	
//	protected Element createPositionsElement(Player record, Document doc) {
//		Element positionsRoot = positionFormatter.createRootElement(doc);
//		positionsRoot.setAttribute("provenance",
//				record.getMergedAttributeProvenance(Player.POSITIONS));
//
//		for (String a : record.getPositions()) {
//			positionsRoot.appendChild(positionFormatter
//					.createElementFromRecord(a, doc));
//		}
//
//		return positionsRoot;
//	}
	
	protected Element createPositionsElement(Player record, Document doc) {
	    Element positionsRoot = positionFormatter.createRootElement(doc);
	    positionsRoot.setAttribute("provenance", record.getMergedAttributeProvenance(Player.POSITIONS));

	    for (String position : record.getPositions()) {
	        if (!position.isEmpty()) {
	            Element positionElement = positionFormatter.createElementFromRecord(position, doc);
	            // Append the positionElement directly to the positionsRoot
	            positionsRoot.appendChild(positionElement.getFirstChild());
	        }
	    }

	    return positionsRoot;
	}

	protected Element createTextElementWithProvenance(String name,
			String value, String provenance, Document doc) {
		Element elem = createTextElement(name, value, doc);
		elem.setAttribute("provenance", provenance);
		return elem;
	}

}
