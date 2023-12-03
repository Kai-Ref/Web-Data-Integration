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
import java.util.List;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

/**
 * {@link XMLFormatter} for {@link Actor}s.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class PositionsXMLFormatter extends XMLFormatter<List<String>> {

    @Override
    public Element createRootElement(Document doc) {
        return doc.createElement("positions");
    }

    @Override
    public Element createElementFromRecord(List<String> positions, Document doc) {
        Element positionsElement = createRootElement(doc);

        for (String position : positions) {
            if (!position.isEmpty()) {
                Element positionElement = createTextElement("position", position, doc);
                positionsElement.appendChild(positionElement);
            }
        }

        return positionsElement;
    }

    // Overload for handling a single position string
    public Element createElementFromRecord(String position, Document doc) {
        Element positionsElement = createRootElement(doc);

        if (!position.isEmpty()) {
            Element positionElement = createTextElement("position", position, doc);
            positionsElement.appendChild(positionElement);
        }

        return positionsElement;
    }
}

