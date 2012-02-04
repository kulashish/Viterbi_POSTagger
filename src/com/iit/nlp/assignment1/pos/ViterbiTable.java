package com.iit.nlp.assignment1.pos;

public class ViterbiTable {
	private ViterbiTableColumnEntry[] endingStates;

	public ViterbiTable(POSTagSet tagSet, Observation[] observations) {
		endingStates = new ViterbiTableColumnEntry[tagSet.getSize()];
		int index = 0;
		for (POSTag tag : tagSet.getTags()) {
			// System.out.println(tag.getName());
			endingStates[index++] = new ViterbiTableColumnEntry(tag,
					observations);
		}
	}
	
	public void reinitialize(Observation[] observations) {
		for(ViterbiTableColumnEntry entry:endingStates)
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
				outSequence[i] = new ViterbiTableRowEntry(observations[i],
						i == 0 ? 1 : endingStates.length);
		}

		public void reinitialize(Observation[] observations) {
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
//		private final static float DEFAULT_PROB = 0.000000000000000000000000000000000000000000001f;
		private Observation word;
		private double[] accumulatedProbabilities;
		private double maxProbability = 0d;
		private int stateWithMaxProb;

		public ViterbiTableRowEntry(Observation observation, int size) {
			// System.out.println("Creating row entry for "
			// + observation.getName() + " with size " + size);
			word = observation;
			accumulatedProbabilities = new double[size];
			for (int i = 0; i < size; i++)
				accumulatedProbabilities[i] = 0d;
		}

		public void computeMaxProbability() {
			for (int index = 0; index < accumulatedProbabilities.length; index++)
				if (accumulatedProbabilities[index] > maxProbability) {
					maxProbability = accumulatedProbabilities[index];
					stateWithMaxProb = index;
				}
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

		public double[] getAccumulatedProbabilities() {
			return accumulatedProbabilities;
		}

		public void setAccumulatedProbabilities(
				double[] accumulatedProbabilities) {
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
			// System.out.println(entry.getEndState().getName()+": "+
			// entry.getOutSequence()[0].getMaxProbability());
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
