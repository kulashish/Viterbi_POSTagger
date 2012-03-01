package com.iit.nlp.assignment1;

import java.util.ArrayList;
import java.util.List;

import com.iit.nlp.assignment1.pos.POSTag;

public class Result {

	private long taggedCorrectly = 0l;
	private long totalLines = 0l;
	private float accuracy = 0f;
	private long observationsTaggedCorrectly = 0l;
	private long totalObservations = 0l;
	private float observationAccuracy = 0f;
	private List<ErrorEntry> errorList;

	public List<ErrorEntry> getErrorList() {
		if (null == errorList)
			errorList = new ArrayList<Result.ErrorEntry>();
		return errorList;
	}

	public float getAccuracy() {
		return accuracy;
	}

	public float getObservationAccuracy() {
		return observationAccuracy;
	}

	public void incrementTotalLines() {
		totalLines++;
	}

	public void incrementObservations(int length) {
		totalObservations += length;
	}

	public void incrementTaggedCorrectly() {
		taggedCorrectly++;
	}

	public void incrementObservationsTaggedCorrectly(int i) {
		observationsTaggedCorrectly += i;

	}

	public void computeAccuracy() {
		accuracy = taggedCorrectly * 1.0f / totalLines;
		observationAccuracy = observationsTaggedCorrectly * 1.0f
				/ totalObservations;
	}

	public void addError(String word, POSTag correctTag, POSTag wrongTag) {
		getErrorList().add(new ErrorEntry(word, correctTag, wrongTag));
	}

	public void print() {
		System.out.println("Accuracy: " + getObservationAccuracy());
		System.out.println("Error List - ");
		for (ErrorEntry entry : errorList) {
			System.out.println(entry.word + ":" + entry.correctTag.getName()
					+ ":" + entry.wrongTag.getName());
		}
	}

	class ErrorEntry {
		public ErrorEntry(String word, POSTag correctTag, POSTag wrongTag) {
			this.word = word;
			this.correctTag = correctTag;
			this.wrongTag = wrongTag;
		}

		String word;
		POSTag correctTag;
		POSTag wrongTag;
	}

}
