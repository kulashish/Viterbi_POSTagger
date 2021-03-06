package com.iit.nlp.assignment1.pos;

import java.util.HashMap;
import java.util.Map;

public class EmissionMatrix {
	private final static float DEFAULT_PROB = 0.0000001f;

	private Map<POSTag, EmissionMatrixColumnEntry> emissionProbMatrix;

	public int getSize() {
		return null != emissionProbMatrix ? emissionProbMatrix.size() : 0;
	}

	public Map<POSTag, EmissionMatrixColumnEntry> getEmissionProbMatrix() {
		if (null == emissionProbMatrix)
			emissionProbMatrix = new HashMap<POSTag, EmissionMatrixColumnEntry>();
		return emissionProbMatrix;
	}

	public void loadEmission(POSTag tag, Observation observation, int sum,
			float probability) {
		EmissionMatrixColumnEntry foundColumnEntry = getEmissionProbMatrix()
				.get(tag);

		if (null == foundColumnEntry) {
			foundColumnEntry = new EmissionMatrixColumnEntry(tag);
			emissionProbMatrix.put(tag, foundColumnEntry);
		}
		foundColumnEntry.setSum(sum);
		foundColumnEntry.loadTransition(observation, probability);
	}

	public void addEmission(POSTag tag, Observation word) {
		EmissionMatrixColumnEntry foundColumnEntry = getEmissionProbMatrix()
				.get(tag);

		if (null == foundColumnEntry) {
			foundColumnEntry = new EmissionMatrixColumnEntry(tag);
			emissionProbMatrix.put(tag, foundColumnEntry);
		}

		foundColumnEntry.addTransition(word);
	}

	public void computeProbabilities() {
		for (EmissionMatrixColumnEntry columnEntry : emissionProbMatrix
				.values())
			columnEntry.computeProbabilities();
	}

	class EmissionMatrixColumnEntry {
		private POSTag postag;
		private Map<Observation, EmissionMatrixRowEntry> transitions;
		private int sum = 0;

		public EmissionMatrixColumnEntry(POSTag tag) {
			postag = tag;
			sum = 0;
		}

		public void loadTransition(Observation observation, float probability) {
			EmissionMatrixRowEntry entry = new EmissionMatrixRowEntry(
					observation);
			getTransitions().put(observation, entry);
			entry.setProbability(probability);
		}

		public void computeProbabilities() {
			for (EmissionMatrixRowEntry rowEntry : transitions.values())
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

		public void addTransition(Observation word) {
			EmissionMatrixRowEntry foundRowEntry = getTransitions().get(word);

			if (null == foundRowEntry) {
				foundRowEntry = new EmissionMatrixRowEntry(word);
				transitions.put(word, foundRowEntry);
				sum++;
			}
			foundRowEntry.update();
			sum++;
		}

		public void setPostag(POSTag postag) {
			this.postag = postag;
		}

		public Map<Observation, EmissionMatrixRowEntry> getTransitions() {
			if (null == transitions)
				transitions = new HashMap<Observation, EmissionMatrixRowEntry>();
			return transitions;
		}
	}

	class EmissionMatrixRowEntry {
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

	public float getEmissionProbability(POSTag tag, Observation observation) {
		float val = DEFAULT_PROB;
		EmissionMatrixColumnEntry foundColumnEntry = getEmissionProbMatrix()
				.get(tag);

		EmissionMatrixRowEntry foundRowEntry = null;
		if (null != foundColumnEntry) {
			foundRowEntry = foundColumnEntry.getTransitions().get(observation);
			val = null != foundRowEntry ? foundRowEntry.getProbability()
					: 1.0f / (foundColumnEntry.sum + 1);
		}
		return val;
	}
}
