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

	ActorXMLFormatter actorFormatter = new ActorXMLFormatter();

	@Override
	public Element createRootElement(Document doc) {
		return doc.createElement("players");
	}

	@Override
	public Element createElementFromRecord(Player record, Document doc) {
		Element player = doc.createElement("player");

		player.appendChild(createTextElement("id", record.getIdentifier(), doc));

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
		if (record.getWeight() != 0.0) {
		    player.appendChild(createTextElementWithProvenance("weight",
		            Double.toString(record.getWeight()),
		            record.getMergedAttributeProvenance(Player.WEIGHT), doc));
		}
		if (record.getHeight() != 0.0) {
		    player.appendChild(createTextElementWithProvenance("height",
		            Double.toString(record.getHeight()),
		            record.getMergedAttributeProvenance(Player.HEIGHT), doc));
		}
		return player;
	}

	protected Element createTextElementWithProvenance(String name,
			String value, String provenance, Document doc) {
		Element elem = createTextElement(name, value, doc);
		elem.setAttribute("provenance", provenance);
		return elem;
	}

}
