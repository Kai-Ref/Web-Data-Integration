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
package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers;

import java.util.List;

//import com.github.andrewoma.dexx.collection.ArrayList;
import java.util.List;
import java.util.ArrayList;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.Player;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.list.IntersectionKSources;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

/**
 * {@link AttributeValueFuser} for the actors of {@link Movie}s.
 * 
 * @author Robert Meusel (robert@dwslab.de)
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class PositionsFuserIntersectionKSources extends
		AttributeValueFuser<List<String>, Player, Attribute> {

	/**
	 * 
	 * @param k specifies the number of sources 
	 */
	public PositionsFuserIntersectionKSources(int k) {
		super(new IntersectionKSources<String, Player, Attribute>(k));
	}

	@Override
	public boolean hasValue(Player record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(Player.POSITIONS) && record.getPositions() != null;
	}

	@Override
	public List<String> getValue(Player record, Correspondence<Attribute, Matchable> correspondence) {
		if (record.getPositions() != null) {
	        return record.getPositions();
	    }else { 
	    	List<String> rt = new ArrayList<String>();
	    	return rt; 
	    }
		
	}

	@Override
	public void fuse(RecordGroup<Player, Attribute> group, Player fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<List<String>, Player, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		fusedRecord.setPositions(fused.getValue());
		fusedRecord
				.setAttributeProvenance(Player.POSITIONS, fused.getOriginalIds());
	}

}
