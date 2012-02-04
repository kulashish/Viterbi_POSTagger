package com.iit.nlp.assignment1.pos;

import java.util.HashMap;
import java.util.Map;

public class InitialProbabilityVector {
	private static final float DEFAULT_PROB = 0.0000001f;
	private int sum = 0;
	private Map<POSTag, InitialProbabilityVectorEntry> initialProbList;

	public int getSize() {
		return null != initialProbList ? initialProbList.size() : 0;
	}

	public void print() {
		for (InitialProbabilityVectorEntry entry : initialProbList.values()) {
			System.out.print(entry.getTag().getName() + ": ");
			System.out.println(entry.getProbability());
		}
	}

	public void addState(POSTag tag) {
		InitialProbabilityVectorEntry foundEntry = getInitialProbList()
				.get(tag);

		if (null == foundEntry) {
			foundEntry = new InitialProbabilityVectorEntry(tag);
			initialProbList.put(tag, foundEntry);
		} else
			foundEntry.update();
		sum++;
	}

	public Map<POSTag, InitialProbabilityVectorEntry> getInitialProbList() {
		if (null == initialProbList)
			initialProbList = new HashMap<POSTag, InitialProbabilityVectorEntry>();
		return initialProbList;
	}

	public void computeProbabilities() {
		for (InitialProbabilityVectorEntry entry : initialProbList.values())
			entry.computeProbability(sum);
	}

	public float getInitialProbability(POSTag tag) {
		float prob = DEFAULT_PROB;
		for (InitialProbabilityVectorEntry entry : initialProbList.values())
			if (entry.getTag().equals(tag)) {
				prob = entry.getProbability();
				break;
			}
		return prob;
	}

	private class InitialProbabilityVectorEntry {
		private POSTag tag;
		private float probability;

		public InitialProbabilityVectorEntry(POSTag tag2) {
			this.tag = tag2;
			probability = 1f;
		}

		public void computeProbability(int sum) {
			setProbability(probability / sum);
		}

		public void update() {
			probability += 1;
		}

		public POSTag getTag() {
			return tag;
		}

		public void setTag(POSTag tag) {
			this.tag = tag;
		}

		public float getProbability() {
			return probability;
		}

		public void setProbability(float probability) {
			this.probability = probability;
		}

	}

}
