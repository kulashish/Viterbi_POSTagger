package com.iit.nlp.assignment1.pos;

import java.util.ArrayList;
import java.util.List;

public class EmissionMatrix {

	private List<List<Float>> emissionProbMatrix;
	private List<Integer> sumList;

	public void addEmission(POSTag tag, Observation word) {
		if (null == emissionProbMatrix) {
			emissionProbMatrix = new ArrayList<List<Float>>();
			sumList = new ArrayList<Integer>();
		}
		int outerIndex = tag.getIndex();
		if (outerIndex >= emissionProbMatrix.size())
			emissionProbMatrix.add(outerIndex, null);

		List<Float> innerArr = emissionProbMatrix.get(outerIndex);
		if (null == innerArr)
			innerArr = new ArrayList<Float>();
		int innerIndex = word.getIndex();
		if (innerIndex < innerArr.size()) // word is present
			innerArr.set(innerIndex, ((float) innerArr.get(innerIndex)) + 1);
		else
			innerArr.add(innerIndex, 1f);
		sumList.add(outerIndex, sumList.get(outerIndex) + 1);
	}
	
	public void computeProbabilities()	{
//		int numTags = transitionProbMatrix.size();
//		float[][] transitionProbArr = (float[][]) transitionProbMatrix
//				.toArray(new float[numTags][numTags]);
//		int sum = 0;
//		for (int outer = 0; outer < numTags; outer++) {
//			sum = 0;
//			for (int inner = 0; inner < numTags; inner++)
//				sum += transitionProbArr[outer][inner];
//			for (int inner = 0; inner < numTags; inner++)
//				transitionProbArr[outer][inner] /= sum;
//		}
	}
}
