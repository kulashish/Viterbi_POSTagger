package com.iit.nlp.assignment1.pos;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.iit.nlp.assignment1.corpus.TaggedDocument;
import com.iit.nlp.assignment1.pos.ViterbiTable.ViterbiTableColumnEntry;
import com.iit.nlp.assignment1.pos.ViterbiTable.ViterbiTableRowEntry;

public class ViterbiAlgorithm {

	private ModelParameters modelParameters;
	private ViterbiTable viterbiTable;
	private Observation[] observations;
	private List<TaggedDocument> testDocuments;
	private long taggedCorrectly = 0l;
	private long totalLines = 0l;
	private float accuracy = 0f;

	public ViterbiAlgorithm(ModelParameters params,
			List<TaggedDocument> documents) {
		modelParameters = params;
		testDocuments = documents;
		System.out.println("Testing on " + testDocuments.size() + " documents");
	}

	public float getAccuracy() {
		return accuracy;
	}

	public float run() {
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
					updateAccuracy(originalTags, tags);
				}
				System.out.println();
				document.closeReader();
			}
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Correct: " + taggedCorrectly + "--- Total : "
				+ totalLines);
		return accuracy;
	}

	private void updateAccuracy(POSTag[] originalTags, POSTag[] tags) {
		totalLines++;
		// System.out.println("Original: ");
		// for (POSTag tag : originalTags)
		// System.out.print(tag.getName() + ", ");
		// System.out.println("Predicted: ");
		// for (POSTag tag : tags)
		// System.out.print(tag.getName() + ", ");
		if (modelParameters.getTagSet().compareTags(originalTags, tags) == 0)
			taggedCorrectly++;
		accuracy = taggedCorrectly * 1.0f / totalLines;

	}

	public void init(Observation[] words) {
		observations = new Observation[words.length + 1];
		observations[0] = new Observation("epsilon");
		for (int i = 1; i < observations.length; i++) {
			observations[i] = words[i - 1];
			// System.out.println(observations[i].getName());
		}
		if (null == viterbiTable)
			viterbiTable = new ViterbiTable(modelParameters.getTagSet(),
					observations);
		else
			viterbiTable.reinitialize(observations);
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
		double[] probabilities = entry.getAccumulatedProbabilities();
		double[] lastMaxProbs = getLastMaxProbabilities(obsIndex - 1);

		Iterator<POSTag> iter = modelParameters.getTagSet().getTags()
				.iterator();
		POSTag tag = null;
		float transitionProb = 0f;
		float emissionProb = 0f;
		// boolean stop = false;
		while (iter.hasNext()) {
			tag = iter.next();
			transitionProb = modelParameters.getTransitionMatrix()
					.getTransitionProbability(tag, endstate.getEndState());
			emissionProb = modelParameters.getEmissionMatrix()
					.getEmissionProbability(tag, observations[obsIndex]);
			// if (lastMaxProbs[tag.getIndex()] == 0f && !stop)
			// System.out.println("**MAX ZERO!***"
			// + observations[obsIndex].getName() + "**"
			// + endstate.getEndState().getName());
			probabilities[tag.getIndex()] = lastMaxProbs[tag.getIndex()]
					* transitionProb * emissionProb;
			// if (probabilities[tag.getIndex()] == 0f && !stop) {
			// // System.out.println("***PROB ZERO!!**" + transitionProb + ": "
			// // + emissionProb + ": " + lastMaxProbs[tag.getIndex()]);
			// stop = true;
			// }
			// System.out.println(transitionProb+":"+emissionProb+":"+lastMaxProbs[tag.getIndex()]);
			// if (probabilities[tag.getIndex()] > 0)
			// System.out.println("***" + probabilities[tag.getIndex()]
			// + "***");
		}
		entry.setAccumulatedProbabilities(probabilities);
		entry.computeMaxProbability();
		// System.out.println(observations[obsIndex].getName() + ": "
		// + endstate.getEndState().getName() + ": "
		// + entry.getMaxProbability());
	}

	private double[] getLastMaxProbabilities(int index) {
		double[] probs = new double[viterbiTable.getEndingStates().length];
		for (ViterbiTableColumnEntry columnEntry : viterbiTable
				.getEndingStates()) {
			// System.out.println(columnEntry.getEndState().getIndex());
			probs[columnEntry.getEndState().getIndex()] = columnEntry
					.getOutSequence()[index].getMaxProbability();
		}
		return probs;
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
			if (currentProb > maxProb) {
				// postags[observations.length - 1] = endingState.getEndState();
				maxProb = currentProb;
				backPtr = viterbiEntries[observations.length - 1]
						.getStateWithMaxProb();
			}
		}
		// System.out.println(postags[observations.length - 1].getName());
		// System.out.println("Backptr: " + backPtr);

		ViterbiTableColumnEntry columnEntryForTag = null;
		for (int index = observations.length - 2; index >= 0; index--) {
			// System.out.print(modelParameters.getTagSet().getTag(backPtr)
			// .getName()
			// + "---");
			columnEntryForTag = viterbiTable.getColumnEntry(modelParameters
					.getTagSet().getTag(backPtr));
			// System.out.println(columnEntryForTag.getEndState().getName());
			postags[index] = columnEntryForTag.getEndState();
			backPtr = columnEntryForTag.getOutSequence()[index]
					.getStateWithMaxProb();
		}
		return postags;
	}
}
