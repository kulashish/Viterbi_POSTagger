package com.iit.nlp.assignment1.pos;

public class ViterbiTable {
	private ViterbiTableColumnEntry[] endingStates;

	public ViterbiTable(POSTagSet tagSet, Observation[] observations) {
		endingStates = new ViterbiTableColumnEntry[tagSet.getSize()];
		for (POSTag tag : tagSet.getTags()) {
			endingStates[tag.getIndex()] = new ViterbiTableColumnEntry(tag,
					observations);
		}
	}

	public void reinitialize(Observation[] observations) {
		for (ViterbiTableColumnEntry entry : endingStates)
			entry.reinitialize(observations);

	}

	public ViterbiTableColumnEntry[] getEndingStates() {
		return endingStates;
	}

	class ViterbiTableColumnEntry {
		private POSTag endState;
		private ViterbiTableRowEntry[] outSequence;

		public ViterbiTableColumnEntry(POSTag tag, Observation[] observations) {
			endState = tag;
			outSequence = new ViterbiTableRowEntry[observations.length];
			for (int i = 0; i < outSequence.length; i++)
				outSequence[i] = new ViterbiTableRowEntry(observations[i]);
		}

		public void reinitialize(Observation[] observations) {
			outSequence = new ViterbiTableRowEntry[observations.length];
			for (int i = 0; i < outSequence.length; i++)
				outSequence[i] = new ViterbiTableRowEntry(observations[i]);
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
		private double maxProbability = 0d;
		private int stateWithMaxProb;

		public ViterbiTableRowEntry(Observation observation) {
			word = observation;
		}

		public void setMaxProbability(double maxProbability) {
			this.maxProbability = maxProbability;
		}

		public void setStateWithMaxProb(int stateWithMaxProb) {
			this.stateWithMaxProb = stateWithMaxProb;
		}

		public int getStateWithMaxProb() {
			return stateWithMaxProb;
		}

		public double getMaxProbability() {
			return maxProbability;
		}

		public Observation getWord() {
			return word;
		}

		public void setWord(Observation word) {
			this.word = word;
		}
	}

	public void init(InitialProbabilityVector initialProbVec) {
		POSTag state = null;
		for (ViterbiTableColumnEntry entry : endingStates) {
			state = entry.getEndState();
			entry.getOutSequence()[0].maxProbability = Math.log(initialProbVec
					.getInitialProbability(state));
			entry.getOutSequence()[0].stateWithMaxProb = state.getIndex();
		}
	}

	public ViterbiTableColumnEntry getColumnEntry(POSTag tag) {
		ViterbiTableColumnEntry entryFound = null;
		for (ViterbiTableColumnEntry entry : endingStates) {
			if (entry.getEndState().equals(tag)) {
				entryFound = entry;
				break;
			}
		}
		return entryFound;
	}

}
