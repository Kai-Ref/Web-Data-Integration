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

package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.BlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

/**
 * {@link BlockingKeyGenerator} for {@link Movie}s, which generates a blocking
 * key based on the title
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class PlayerBlockingKeyByNameGenerator extends
		RecordBlockingKeyGenerator<Player, Attribute> {

	private static final long serialVersionUID = 1L;
	private int num_first_letters;
	
	public PlayerBlockingKeyByNameGenerator() {
        this(2); // Default value
    }
	
	public PlayerBlockingKeyByNameGenerator(int num_first_letters) {
		this.num_first_letters = num_first_letters;
	}
	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.matching.blocking.generators.BlockingKeyGenerator#generateBlockingKeys(de.uni_mannheim.informatik.wdi.model.Matchable, de.uni_mannheim.informatik.wdi.model.Result, de.uni_mannheim.informatik.wdi.processing.DatasetIterator)
	 */
	@Override
	public void generateBlockingKeys(Player record, Processable<Correspondence<Attribute, Matchable>> correspondences,
			DataIterator<Pair<String, Player>> resultCollector) {

		String[] tokens  = record.getName().split(" ");

		String blockingKeyValue = "";

		for(int i = 0; i <= 2 && i < tokens.length; i++) {
			blockingKeyValue += tokens[i].substring(0, Math.min(num_first_letters,tokens[i].length())).toUpperCase();
		}

		resultCollector.next(new Pair<>(blockingKeyValue, record));
	}

}
