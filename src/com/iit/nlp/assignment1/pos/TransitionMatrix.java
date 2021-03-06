package com.iit.nlp.assignment1.pos;

import java.util.HashMap;
import java.util.Map;

public class TransitionMatrix {
	private static final float DEFAULT_PROB = 0.0000001f;

	private Map<POSTag, TransitionMatrixColumnEntry> transitionProbMatrix;

	public TransitionMatrix() {

	}

	public Map<POSTag, TransitionMatrixColumnEntry> getTransitionProbMatrix() {
		if (null == transitionProbMatrix)
			transitionProbMatrix = new HashMap<POSTag, TransitionMatrixColumnEntry>();
		return transitionProbMatrix;
	}

	public int getSize() {
		return null != transitionProbMatrix ? transitionProbMatrix.size() : 0;
	}

	public float getTransitionProbability(POSTag tag1, POSTag tag2) {
		float val = DEFAULT_PROB;
		TransitionMatrixColumnEntry foundColumnEntry = getTransitionProbMatrix()
				.get(tag1);
		TransitionMatrixRowEntry foundRowEntry = null;
		if (null != foundColumnEntry) {
			foundRowEntry = foundColumnEntry.getTransitions().get(tag2);
			val = null != foundRowEntry ? foundRowEntry.getProbability()
					: 1.0f / (1 + foundColumnEntry.sum);
		}
		return val;
	}

	public void loadTransition(POSTag tag1, POSTag tag2, int sum,
			float probability) {
		TransitionMatrixColumnEntry foundColumnEntry = getTransitionProbMatrix()
				.get(tag1);

		if (null == foundColumnEntry) {
			foundColumnEntry = new TransitionMatrixColumnEntry(tag1);
			transitionProbMatrix.put(tag1, foundColumnEntry);
		}
		foundColumnEntry.setSum(sum);
		foundColumnEntry.loadTransition(tag2, probability);
	}

	public void addTransition(POSTag tag1, POSTag tag2) {
		TransitionMatrixColumnEntry foundColumnEntry = getTransitionProbMatrix()
				.get(tag1);

		if (null == foundColumnEntry) {
			foundColumnEntry = new TransitionMatrixColumnEntry(tag1);
			transitionProbMatrix.put(tag1, foundColumnEntry);
		}

		foundColumnEntry.addTransition(tag2);
	}

	public void computeProbabilities() {
		for (TransitionMatrixColumnEntry columnEntry : transitionProbMatrix
				.values())
			columnEntry.computeProbabilities();
	}

	public void print() {
		for (TransitionMatrixColumnEntry columnEntry : transitionProbMatrix
				.values())
			columnEntry.print();
	}

	class TransitionMatrixColumnEntry {
		private POSTag postag;
		private Map<POSTag, TransitionMatrixRowEntry> transitions;
		private int sum;

		public TransitionMatrixColumnEntry(POSTag tag) {
			postag = tag;
			sum = 0;
		}

		public void print() {
			System.out.println();
			System.out.println("Transitions for Tag : " + postag.getName()
					+ " - ");
			for (TransitionMatrixRowEntry rowEntry : transitions.values())
				rowEntry.print();
		}

		public void computeProbabilities() {
			for (TransitionMatrixRowEntry rowEntry : transitions.values())
				rowEntry.computeProbability(sum);
		}

		public int getSum() {
			return sum;
		}

		public void setSum(int sum) {
			this.sum = sum;
		}

		public POSTag getPostag() {
			return postag;
		}

		public void addTransition(POSTag tag) {
			TransitionMatrixRowEntry foundRowEntry = getTransitions().get(tag);

			if (null == foundRowEntry) {
				foundRowEntry = new TransitionMatrixRowEntry(tag);
				transitions.put(tag, foundRowEntry);
				sum++;
			}
			foundRowEntry.update();
			sum++;
		}

		public void loadTransition(POSTag tag, float probability) {
			TransitionMatrixRowEntry entry = new TransitionMatrixRowEntry(tag);
			getTransitions().put(tag, entry);
			entry.setProbability(probability);
		}

		public void setPostag(POSTag postag) {
			this.postag = postag;
		}

		public Map<POSTag, TransitionMatrixRowEntry> getTransitions() {
			if (null == transitions)
				transitions = new HashMap<POSTag, TransitionMatrixRowEntry>();
			return transitions;
		}

	}

	class TransitionMatrixRowEntry {
		private POSTag postag;
		private float probability;

		public TransitionMatrixRowEntry() {
			probability = 1f;
		}

		public void print() {
			System.out.print(postag.getName() + " (" + probability + "), ");
		}

		public void computeProbability(int sum) {
			setProbability(probability / sum);
		}

		public void update() {
			probability += 1;
		}

		public TransitionMatrixRowEntry(POSTag tag) {
			this();
			postag = tag;
		}

		public POSTag getPostag() {
			return postag;
		}

		public void setPostag(POSTag postag) {
			this.postag = postag;
		}

		public float getProbability() {
			return probability;
		}

		public void setProbability(float probability) {
			this.probability = probability;
		}

	}
}
