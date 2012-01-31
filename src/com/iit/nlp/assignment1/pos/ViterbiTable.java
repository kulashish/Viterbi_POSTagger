package com.iit.nlp.assignment1.pos;

public class ViterbiTable {
	private ViterbiTableColumnEntry[] endingStates;

	public ViterbiTableColumnEntry[] getEndingStates() {
		return endingStates;
	}

	public ViterbiTable(POSTagSet tagSet, Observation[] observations) {
		endingStates = new ViterbiTableColumnEntry[tagSet.getSize()];
		int index = 0;
		for (POSTag tag : tagSet.getTags())
			endingStates[index++] = new ViterbiTableColumnEntry(tag,
					observations);
	}

	class ViterbiTableColumnEntry {
		private POSTag endState;
		private ViterbiTableRowEntry[] outSequence;

		public ViterbiTableColumnEntry(POSTag tag, Observation[] observations) {
			endState = tag;
			outSequence = new ViterbiTableRowEntry[observations.length];
			for (int i = 0; i < outSequence.length; i++)
				outSequence[i] = new ViterbiTableRowEntry(observations[i],
						i == 0 ? 1 : endingStates.length);
		}

		public POSTag getEndState() {
			return endState;
		}

		public void setEndState(POSTag endState) {
			this.endState = endState;
		}

		public ViterbiTableRowEntry[] getOutSequence() {
			return outSequence;
		}

		public void setOutSequence(ViterbiTableRowEntry[] outSequence) {
			this.outSequence = outSequence;
		}

	}

	class ViterbiTableRowEntry {
		private Observation word;
		private float[] accumulatedProbabilities;
		private float maxProbability;
		private int stateWithMaxProb;

		public ViterbiTableRowEntry(Observation observation, int size) {
			word = observation;
			accumulatedProbabilities = new float[size];
		}

		public void computeMaxProbability() {
			float max = 0f;
			for (int index = 0; index < accumulatedProbabilities.length; index++)
				if (accumulatedProbabilities[index] > max) {
					max = accumulatedProbabilities[index];
					stateWithMaxProb = index;
				}
		}

		public int getStateWithMaxProb() {
			return stateWithMaxProb;
		}

		public float getMaxProbability() {
			return maxProbability;
		}

		public Observation getWord() {
			return word;
		}

		public void setWord(Observation word) {
			this.word = word;
		}

		public float[] getAccumulatedProbabilities() {
			return accumulatedProbabilities;
		}

		public void setAccumulatedProbabilities(float[] accumulatedProbabilities) {
			this.accumulatedProbabilities = accumulatedProbabilities;
		}

	}

	public void init(InitialProbabilityVector initialProbVec) {
		POSTag state = null;
		for (ViterbiTableColumnEntry entry : endingStates) {
			state = entry.getEndState();
			entry.getOutSequence()[0].accumulatedProbabilities[0] = initialProbVec
					.getInitialProbability(state);
			entry.getOutSequence()[0].computeMaxProbability();
		}
	}

}
