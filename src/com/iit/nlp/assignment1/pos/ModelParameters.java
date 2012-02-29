package com.iit.nlp.assignment1.pos;

public class ModelParameters {

	private POSTagSet tagSet;
	private TransitionMatrix transitionMatrix;
	private EmissionMatrix emissionMatrix;
	private InitialProbabilityVector initialProbVec;
	private ObservationSet observationSet;

	public ModelParameters() {
		tagSet = POSTagSet.getPOSTagSet();
		transitionMatrix = new TransitionMatrix();
		emissionMatrix = new EmissionMatrix();
		initialProbVec = new InitialProbabilityVector();
		observationSet = ObservationSet.getObservationSet();
	}

	public ObservationSet getObservationSet() {
		return observationSet;
	}

	public POSTagSet getTagSet() {
		return tagSet;
	}

	public TransitionMatrix getTransitionMatrix() {
		return transitionMatrix;
	}

	public EmissionMatrix getEmissionMatrix() {
		return emissionMatrix;
	}

	public InitialProbabilityVector getInitialProbVec() {
		return initialProbVec;
	}

	public void setTagSet(POSTagSet tagSet) {
		this.tagSet = tagSet;
	}

	public void setTransitionMatrix(TransitionMatrix transitionMatrix) {
		this.transitionMatrix = transitionMatrix;
	}

	public void setEmissionMatrix(EmissionMatrix emissionMatrix) {
		this.emissionMatrix = emissionMatrix;
	}

	public void setInitialProbVec(InitialProbabilityVector initialProbVec) {
		this.initialProbVec = initialProbVec;
	}

	public void setObservationSet(ObservationSet observationSet) {
		this.observationSet = observationSet;
	}

	public void updateParameters(String[] tags, String[] observations) {
		POSTag tag = null;
		POSTag prevTag = null;
		Observation observation = null;
		for (int i = 0; i < tags.length; i++) {
			tag = tagSet.addTag(tags[i]);
			observation = observationSet.addObservation(observations[i]);
			if (null == prevTag)
				initialProbVec.addState(tag);
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
		initialProbVec.computeProbabilities();
	}

}
