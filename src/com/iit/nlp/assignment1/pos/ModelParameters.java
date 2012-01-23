package com.iit.nlp.assignment1.pos;

public class ModelParameters {

	private POSTagSet tagSet;
	private TransitionMatrix transitionMatrix;
	private EmissionMatrix emissionMatrix;

	public ModelParameters() {
		tagSet = new POSTagSet();
		transitionMatrix = new TransitionMatrix();
		emissionMatrix = new EmissionMatrix();
	}

	public void updateParameters(String[] tags, String[] observations) {
		POSTag tag = null;
		POSTag prevTag = null;
		Observation observation = null;
		for (int i = 0; i < tags.length; i++) {
			tag = new POSTag(tags[i]);
			observation = new Observation(observations[i]);
			tagSet.addTag(tag);
			if (null != prevTag && null != tag)
				transitionMatrix.addTransition(prevTag, tag);
			if (null != tag && null != observation)
				emissionMatrix.addEmission(tag, observation);
			prevTag = tag;
		}
	}

	public void updateParameters() {
		transitionMatrix.computeProbabilities();
		emissionMatrix.computeProbabilities();
	}

}
