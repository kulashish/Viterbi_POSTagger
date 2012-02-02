package com.iit.nlp.assignment1.pos;

import java.util.HashMap;
import java.util.Map;

public class ObservationSet {

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

}