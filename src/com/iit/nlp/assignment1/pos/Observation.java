package com.iit.nlp.assignment1.pos;

public class Observation {
	private static int count = 0;
	private String name;
	private int index;

	public Observation(String observation) {
		name = observation;
		index = count++;
	}

	public Observation(int i, String name) {
		index = i;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
