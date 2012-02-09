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
	private long observationsTaggedCorrectly = 0l;
	private long totalObservations = 0l;
	private float observationAccuracy = 0f;

	public ViterbiAlgorithm(ModelParameters params,
			List<TaggedDocument> documents) {
		modelParameters = params;
		testDocuments = documents;
		System.out.println("Testing on " + testDocuments.size() + " documents");
	}

	public float getObservationAccuracy() {
		return observationAccuracy;
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
		totalObservations += tags.length;
		int diff = modelParameters.getTagSet().compareTags(originalTags, tags);
		if (diff == 0)
			taggedCorrectly++;
		observationsTaggedCorrectly += (Math.max(originalTags.length,
				tags.length) - diff);
		accuracy = taggedCorrectly * 1.0f / totalLines;
		observationAccuracy = observationsTaggedCorrectly * 1.0f
				/ totalObservations;
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
