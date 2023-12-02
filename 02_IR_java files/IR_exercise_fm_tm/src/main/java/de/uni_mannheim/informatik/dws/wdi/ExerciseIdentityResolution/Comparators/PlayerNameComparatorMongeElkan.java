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
package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators;

import de.uni_mannheim.informatik.dws.winter.matching.rules.comparators.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.comparators.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Player;


public class PlayerNameComparatorMongeElkan implements Comparator<Player, Attribute> {
	
	//uses Levenshtein distance as internal distance measure
    private static final long serialVersionUID = 1L;
    private LevenshteinSimilarity sim = new LevenshteinSimilarity();
    
    private ComparatorLogger comparisonLog;

    @Override
    public double compare(
            Player record1,
            Player record2,
            Correspondence<Attribute, Matchable> schemaCorrespondences) {
        
        String s1 = record1.getName();
        String s2 = record2.getName();
        
        double similarity = calculateMongeElkanSimilarity(s1, s2);
        
        if(this.comparisonLog != null){
            this.comparisonLog.setComparatorName(getClass().getName());
        
            this.comparisonLog.setRecord1Value(s1);
            this.comparisonLog.setRecord2Value(s2);
        
            this.comparisonLog.setSimilarity(Double.toString(similarity));
        }
        
        return similarity;
    }

    // Function to calculate Monge-Elkan similarity between two strings
    private double calculateMongeElkanSimilarity(String str1, String str2) {
        String[] tokens1 = str1.split("\\s+"); // Split the strings into tokens
        String[] tokens2 = str2.split("\\s+");

        double similaritySum = 0.0;

        for (String token1 : tokens1) {
            double maxSimilarity = 0.0;

            for (String token2 : tokens2) {
                double tokenSimilarity = sim.calculate(token1, token2);
                if (tokenSimilarity > maxSimilarity) {
                    maxSimilarity = tokenSimilarity;
                }
            }

            similaritySum += maxSimilarity;
        }

        // Calculate the average similarity
        return similaritySum / tokens1.length;
    }

    @Override
    public ComparatorLogger getComparisonLog() {
        return this.comparisonLog;
    }

    @Override
    public void setComparisonLog(ComparatorLogger comparatorLog) {
        this.comparisonLog = comparatorLog;
    }
}