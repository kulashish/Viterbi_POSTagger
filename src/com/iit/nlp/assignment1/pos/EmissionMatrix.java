package com.iit.nlp.assignment1.pos;

import java.util.ArrayList;
import java.util.List;

public class EmissionMatrix {

	private List<EmissionMatrixColumnEntry> emissionProbMatrix;

	public int getSize() {
		return null != emissionProbMatrix ? emissionProbMatrix.size() : 0;
	}

	public List<EmissionMatrixColumnEntry> getEmissionProbMatrix() {
		if (null == emissionProbMatrix)
			emissionProbMatrix = new ArrayList<EmissionMatrixColumnEntry>();
		return emissionProbMatrix;
	}

	public void addEmission(POSTag tag, Observation word) {
		EmissionMatrixColumnEntry foundColumnEntry = null;
		for (EmissionMatrixColumnEntry columnEntry : getEmissionProbMatrix())
			if (columnEntry.getPostag().equals(tag)) {
				foundColumnEntry = columnEntry;
				break;
			}

		if (null == foundColumnEntry) {
			foundColumnEntry = new EmissionMatrixColumnEntry(tag);
			emissionProbMatrix.add(foundColumnEntry);
		}

		foundColumnEntry.addTransition(word);
	}

	public void computeProbabilities() {
		for (EmissionMatrixColumnEntry columnEntry : emissionProbMatrix)
			columnEntry.computeProbabilities();
	}

	private class EmissionMatrixColumnEntry {
		private POSTag postag;
		private List<EmissionMatrixRowEntry> transitions;
		private int sum = 0;

		public EmissionMatrixColumnEntry(POSTag tag) {
			postag = tag;
			sum = 0;
		}

		public void computeProbabilities() {
			for (EmissionMatrixRowEntry rowEntry : transitions)
				rowEntry.computeProbability(sum);
		}

		public POSTag getPostag() {
			return postag;
		}

		public void addTransition(Observation word) {
			EmissionMatrixRowEntry foundRowEntry = null;
			for (EmissionMatrixRowEntry rowEntry : getTransitions())
				if (rowEntry.word.equals(word)) {
					foundRowEntry = rowEntry;
					break;
				}
			if (null == foundRowEntry) {
				foundRowEntry = new EmissionMatrixRowEntry(word);
				transitions.add(foundRowEntry);
			} else
				foundRowEntry.update();
			sum++;
		}

		public void setPostag(POSTag postag) {
			this.postag = postag;
		}

		public List<EmissionMatrixRowEntry> getTransitions() {
			if (null == transitions)
				transitions = new ArrayList<EmissionMatrixRowEntry>();
			return transitions;
		}

		public void setTransitions(List<EmissionMatrixRowEntry> transitions) {
			this.transitions = transitions;
		}

	}

	private class EmissionMatrixRowEntry {
		private Observation word;
		private float probability;

		public EmissionMatrixRowEntry() {
			probability = 1f;
		}

		public void computeProbability(int sum) {
			setProbability(probability / sum);
		}

		public void update() {
			probability += 1;
		}

		public EmissionMatrixRowEntry(Observation word) {
			this();
			this.word = word;
		}

		public Observation getWord() {
			return word;
		}

		public void setWord(Observation word) {
			this.word = word;
		}

		public float getProbability() {
			return probability;
		}

		public void setProbability(float probability) {
			this.probability = probability;
		}

	}
}
