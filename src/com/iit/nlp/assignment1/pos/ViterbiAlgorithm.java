package com.iit.nlp.assignment1.pos;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.iit.nlp.assignment1.Result;
import com.iit.nlp.assignment1.corpus.TaggedDocument;
import com.iit.nlp.assignment1.pos.ViterbiTable.ViterbiTableColumnEntry;
import com.iit.nlp.assignment1.pos.ViterbiTable.ViterbiTableRowEntry;

public class ViterbiAlgorithm {

	private ModelParameters modelParameters;
	private ViterbiTable viterbiTable;
	private Observation[] observations;
	private List<TaggedDocument> testDocuments;
	private Result result;

	public ViterbiAlgorithm(ModelParameters params,
			List<TaggedDocument> documents) {
		result = new Result();
		modelParameters = params;
		testDocuments = documents;
		System.out.println("Testing on " + testDocuments.size() + " documents");
	}

	public Result run() {
		POSTagExtracter extracter = new POSTagExtracter();
		String line = null;
		String[] words = null;
		POSTag[] tags = null;
		POSTag[] originalTags = null;
		try {
			for (TaggedDocument document : testDocuments) {
				System.out.print(".");
				while (null != (line = document.readLine())) {
					System.out.print("-");
					words = extracter.words(line);
					originalTags = modelParameters.getTagSet().getTags(
							extracter.tags(line));
					init(modelParameters.getObservationSet().getObservations(
							words));
					tags = postag();
					updateAccuracy(originalTags, tags, words);
				}
				System.out.println();
				document.closeReader();
			}
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private void updateAccuracy(POSTag[] originalTags, POSTag[] tags,
			String[] words) {
		result.incrementTotalLines();
		result.incrementObservations(tags.length);
		List<Integer> diff = modelParameters.getTagSet().compareTags(
				originalTags, tags);
		if (diff.size() == 0)
			result.incrementTaggedCorrectly();
		result.incrementObservationsTaggedCorrectly(Math.max(
				originalTags.length, tags.length) - diff.size());
		for (int i : diff) {
			result.addError(words[i], originalTags[i], tags[i]);
		}
		// observationsTaggedCorrectly += (Math.max(originalTags.length,
		// tags.length) - diff);
		result.computeAccuracy();
	}

	public void init(Observation[] words) {
		observations = new Observation[words.length + 1];
		observations[0] = new Observation("epsilon");
		for (int i = 1; i < observations.length; i++)
			observations[i] = words[i - 1];

		viterbiTable = new ViterbiTable(modelParameters.getTagSet(),
				observations);

		viterbiTable.init(modelParameters.getInitialProbVec());
	}

	public POSTag[] postag() {
		for (int obsIndex = 1; obsIndex < observations.length; obsIndex++)
			for (ViterbiTableColumnEntry endstate : viterbiTable
					.getEndingStates())
				updateViterbiCell(obsIndex, endstate);
		return getTagSequence();
	}

	private void updateViterbiCell(int obsIndex,
			ViterbiTableColumnEntry endstate) {
		ViterbiTableRowEntry entry = endstate.getOutSequence()[obsIndex];
		Iterator<POSTag> iter = modelParameters.getTagSet().getTags()
				.iterator();
		POSTag tag = null;
		float transitionProb = 0f;
		float emissionProb = 0f;
		double max = 0d;
		double prob = 0d;
		int indexOfMax = -1;
		double lastMaxProb = 0d;

		while (iter.hasNext()) {
			tag = iter.next();
			transitionProb = modelParameters.getTransitionMatrix()
					.getTransitionProbability(tag, endstate.getEndState());
			emissionProb = modelParameters.getEmissionMatrix()
					.getEmissionProbability(tag, observations[obsIndex]);

			lastMaxProb = viterbiTable.getEndingStates()[tag.getIndex()]
					.getOutSequence()[obsIndex - 1].getMaxProbability();
			prob = lastMaxProb + Math.log(transitionProb)
					+ Math.log(emissionProb);
			if (max == 0 || prob > max) {
				max = prob;
				indexOfMax = tag.getIndex();
			}
		}

		entry.setMaxProbability(max);
		entry.setStateWithMaxProb(indexOfMax);
	}

	private POSTag[] getTagSequence() {
		ViterbiTableColumnEntry[] endingStates = viterbiTable.getEndingStates();
		ViterbiTableRowEntry[] viterbiEntries = null;
		double maxProb = 0d;
		POSTag[] postags = new POSTag[observations.length - 1];
		int backPtr = 0;
		double currentProb = 0d;

		for (ViterbiTableColumnEntry endingState : endingStates) {
			viterbiEntries = endingState.getOutSequence();
			currentProb = viterbiEntries[observations.length - 1]
					.getMaxProbability();
			if (maxProb == 0 || currentProb > maxProb) {
				maxProb = currentProb;
				backPtr = viterbiEntries[observations.length - 1]
						.getStateWithMaxProb();
			}
		}

		ViterbiTableColumnEntry columnEntryForTag = null;
		for (int index = observations.length - 2; index >= 0; index--) {
			columnEntryForTag = viterbiTable.getColumnEntry(modelParameters
					.getTagSet().getTag(backPtr));
			postags[index] = columnEntryForTag.getEndState();
			backPtr = columnEntryForTag.getOutSequence()[index]
					.getStateWithMaxProb();
		}
		return postags;
	}
}
