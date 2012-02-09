package com.iit.nlp.assignment1.pos;

import java.util.HashMap;
import java.util.Map;

public class ObservationSet {

	private static ObservationSet observationSet = null;
	private Map<String, Observation> observations;

	public Map<String, Observation> getObservations() {
		if (null == observations)
			observations = new HashMap<String, Observation>();
		return observations;
	}

	public Observation addObservation(String word) {
		Observation observation = getObservations().get(word);
		if (null == observation) {
			observation = new Observation(word);
			getObservations().put(word, observation);
		}
		return observation;
	}

	public Observation[] getObservations(String[] words) {
		Observation[] observations = new Observation[words.length];
		for (int index = 0; index < words.length; index++)
			observations[index] = addObservation(words[index]);
		return observations;
	}

	public static ObservationSet getObservationSet() {
		if (null == observationSet)
			observationSet = new ObservationSet();
		return observationSet;
	}

}
