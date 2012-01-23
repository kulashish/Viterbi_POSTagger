package com.iit.nlp.assignment1.pos;

public class POSTag {
	private static int tagcount = 0;
	private String name;
	private int index;

	public POSTag(String tag) {
		name = tag;
		index = tagcount++;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
