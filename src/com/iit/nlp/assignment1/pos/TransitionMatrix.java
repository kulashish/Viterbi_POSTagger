package com.iit.nlp.assignment1.pos;

import java.util.ArrayList;
import java.util.List;

public class TransitionMatrix {

	private List<List<Float>> transitionProbMatrix;

	public TransitionMatrix() {

	}

	public void addTransition(POSTag tag1, POSTag tag2) {
		if (null == transitionProbMatrix)
			transitionProbMatrix = new ArrayList<List<Float>>();
		int outerIndex = tag1.getIndex();
		if (outerIndex >= transitionProbMatrix.size())
			transitionProbMatrix.add(outerIndex, null);

		List<Float> innerArr = transitionProbMatrix.get(outerIndex);
		if (null == innerArr)
			innerArr = new ArrayList<Float>();
		int innerIndex = tag2.getIndex();
		if (innerIndex < innerArr.size()) // tag2 is present
			innerArr.set(innerIndex, ((float) innerArr.get(innerIndex)) + 1);
		else
			innerArr.add(innerIndex, 1f);
	}

	public void computeProbabilities() {
		int numTags = transitionProbMatrix.size();
		float[][] transitionProbArr = (float[][]) transitionProbMatrix
				.toArray(new float[numTags][numTags]);
		int sum = 0;
		for (int outer = 0; outer < numTags; outer++) {
			sum = 0;
			for (int inner = 0; inner < numTags; inner++)
				sum += transitionProbArr[outer][inner];
			for (int inner = 0; inner < numTags; inner++)
				transitionProbArr[outer][inner] /= sum;
		}
	}
}
