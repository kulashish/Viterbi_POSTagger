package com.iit.nlp.assignment1.pos;

import java.util.ArrayList;
import java.util.List;

public class InitialProbabilityVector {
	private int sum = 0;
	private List<InitialProbabilityVectorEntry> initialProbList;

	public int getSize() {
		return null != initialProbList ? initialProbList.size() : 0;
	}

	public void addState(POSTag tag) {
		InitialProbabilityVectorEntry foundEntry = null;
		for (InitialProbabilityVectorEntry entry : getInitialProbList())
			if (entry.tag.equals(tag)) {
				foundEntry = entry;
				break;
			}
		if (null == foundEntry) {
			foundEntry = new InitialProbabilityVectorEntry(tag);
			initialProbList.add(foundEntry);
		} else
			foundEntry.update();
		sum++;
	}

	public List<InitialProbabilityVectorEntry> getInitialProbList() {
		if (null == initialProbList)
			initialProbList = new ArrayList<InitialProbabilityVectorEntry>();
		return initialProbList;
	}

	public void computeProbabilities() {
		for (InitialProbabilityVectorEntry entry : initialProbList)
			entry.computeProbability(sum);
	}

	public float getInitialProbability(POSTag tag) {
		float prob = 0f;
		for (InitialProbabilityVectorEntry entry : initialProbList)
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
