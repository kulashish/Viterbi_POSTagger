package com.iit.nlp.assignment1.pos;

import java.util.Iterator;

import com.iit.nlp.assignment1.pos.ViterbiTable.ViterbiTableColumnEntry;
import com.iit.nlp.assignment1.pos.ViterbiTable.ViterbiTableRowEntry;

public class ViterbiAlgorithm {

	private ModelParameters modelParameters;
	private ViterbiTable viterbiTable;
	private Observation[] observations;

	public ViterbiAlgorithm(ModelParameters params) {
		modelParameters = params;
	}

	public void init(Observation[] words) {
		observations = new Observation[words.length + 1];
		observations[0] = new Observation("epsilon");
		for (int i = 1; i < observations.length; i++)
			observations[i] = words[i - 1];
		viterbiTable = new ViterbiTable(modelParameters.getTagSet(), words);
		viterbiTable.init(modelParameters.getInitialProbVec());
	}

	public void run() {
		for (int obsIndex = 1; obsIndex < observations.length; obsIndex++)
			for (ViterbiTableColumnEntry endstate : viterbiTable
					.getEndingStates())
				updateViterbiCell(obsIndex, endstate);
	}

	private void updateViterbiCell(int obsIndex,
			ViterbiTableColumnEntry endstate) {
		ViterbiTableRowEntry entry = endstate.getOutSequence()[obsIndex];
		float[] probabilities = entry.getAccumulatedProbabilities();
		float[] lastMaxProbs = getLastMaxProbabilities(obsIndex - 1);
		// endstate.getOutSequence()[obsIndex - 1]
		// .getMaxProbability();
		Iterator<POSTag> iter = modelParameters.getTagSet().getTags()
				.iterator();
		POSTag tag = null;
		float transitionProb = 0f;
		float emissionProb = 0f;
		while (iter.hasNext()) {
			tag = iter.next();
			transitionProb = modelParameters.getTransitionMatrix()
					.getTransitionProbability(tag, endstate.getEndState());
			emissionProb = modelParameters.getEmissionMatrix()
					.getEmissionProbability(tag, observations[obsIndex]);
			probabilities[tag.getIndex()] = lastMaxProbs[tag.getIndex()]
					* transitionProb * emissionProb;
		}
		entry.computeMaxProbability();
	}

	private float[] getLastMaxProbabilities(int index) {
		float[] probs = new float[viterbiTable.getEndingStates().length];
		for (ViterbiTableColumnEntry columnEntry : viterbiTable
				.getEndingStates())
			probs[columnEntry.getEndState().getIndex()] = columnEntry
					.getOutSequence()[index].getMaxProbability();
		return probs;
	}
}
