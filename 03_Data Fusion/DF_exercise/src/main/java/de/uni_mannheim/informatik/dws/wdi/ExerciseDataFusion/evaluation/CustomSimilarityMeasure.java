package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation;

import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;

	public class CustomSimilarityMeasure extends SimilarityMeasure<Integer> {

		private static final long serialVersionUID = 1L;
		private double diff_max = 1;

		/**
		 * Creates a new instance of the similarity measure
		 * 
		 * @param max_absolute_difference
		 *            the max absolute difference between two values. Higher
		 *            differences lead to a similarity value of 0.0.
		 */
		public CustomSimilarityMeasure(double max_absolute_difference) {
			this.diff_max = max_absolute_difference;
		}

		@Override
		public double calculate(Integer first, Integer second) {
			if (first == null || second == null) {
				return 0;
			} else {
				int diff = Math.abs(first - second);

				if (diff < diff_max) {
					return 1 - diff / diff_max;
				} else {
					return 0;
				}
			}
		}
	}
