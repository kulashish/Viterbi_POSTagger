package com.iit.nlp.assignment1.pos;

import java.util.ArrayList;
import java.util.List;

public class TransitionMatrix {

	private List<TransitionMatrixColumnEntry> transitionProbMatrix;

	public TransitionMatrix() {

	}

	public List<TransitionMatrixColumnEntry> getTransitionProbMatrix() {
		if (null == transitionProbMatrix)
			transitionProbMatrix = new ArrayList<TransitionMatrixColumnEntry>();
		return transitionProbMatrix;
	}

	public int getSize() {
		return null != transitionProbMatrix ? transitionProbMatrix.size() : 0;
	}

	public float getTransitionProbability(POSTag tag1, POSTag tag2) {
		TransitionMatrixColumnEntry foundColumnEntry = null;
		for (TransitionMatrixColumnEntry columnEntry : getTransitionProbMatrix())
			if (columnEntry.getPostag().equals(tag1)) {
				foundColumnEntry = columnEntry;
				break;
			}
		TransitionMatrixRowEntry foundRowEntry = null;
		if (null != foundColumnEntry)
			for (TransitionMatrixRowEntry rowEntry : foundColumnEntry
					.getTransitions())
				if (rowEntry.getPostag().equals(tag2)) {
					foundRowEntry = rowEntry;
					break;
				}
		return foundRowEntry != null ? foundRowEntry.getProbability() : 0f;
	}

	public void addTransition(POSTag tag1, POSTag tag2) {
		TransitionMatrixColumnEntry foundColumnEntry = null;
		for (TransitionMatrixColumnEntry columnEntry : getTransitionProbMatrix())
			if (columnEntry.getPostag().equals(tag1)) {
				foundColumnEntry = columnEntry;
				break;
			}

		if (null == foundColumnEntry) {
			foundColumnEntry = new TransitionMatrixColumnEntry(tag1);
			transitionProbMatrix.add(foundColumnEntry);
		}

		foundColumnEntry.addTransition(tag2);
	}

	public void computeProbabilities() {
		for (TransitionMatrixColumnEntry columnEntry : transitionProbMatrix)
			columnEntry.computeProbabilities();
	}

	public void print() {
		for (TransitionMatrixColumnEntry columnEntry : transitionProbMatrix)
			columnEntry.print();
	}

	private class TransitionMatrixColumnEntry {
		private POSTag postag;
		private List<TransitionMatrixRowEntry> transitions;
		private int sum;

		public TransitionMatrixColumnEntry(POSTag tag) {
			postag = tag;
			sum = 0;
		}

		public void print() {
			System.out.println();
			System.out.println("Transitions for Tag : " + postag.getName()
					+ " - ");
			for (TransitionMatrixRowEntry rowEntry : transitions)
				rowEntry.print();
		}

		public void computeProbabilities() {
			for (TransitionMatrixRowEntry rowEntry : transitions)
				rowEntry.computeProbability(sum);
		}

		public POSTag getPostag() {
			return postag;
		}

		public void addTransition(POSTag tag) {
			TransitionMatrixRowEntry foundRowEntry = null;
			for (TransitionMatrixRowEntry rowEntry : getTransitions())
				if (rowEntry.postag.equals(tag)) {
					foundRowEntry = rowEntry;
					break;
				}
			if (null == foundRowEntry) {
				foundRowEntry = new TransitionMatrixRowEntry(tag);
				transitions.add(foundRowEntry);
			} else
				foundRowEntry.update();
			sum++;
		}

		public void setPostag(POSTag postag) {
			this.postag = postag;
		}

		public List<TransitionMatrixRowEntry> getTransitions() {
			if (null == transitions)
				transitions = new ArrayList<TransitionMatrixRowEntry>();
			return transitions;
		}

		public void setTransitions(List<TransitionMatrixRowEntry> transitions) {
			this.transitions = transitions;
		}

	}

	private class TransitionMatrixRowEntry {
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
