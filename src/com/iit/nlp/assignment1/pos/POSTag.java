package com.iit.nlp.assignment1.pos;

public class POSTag implements Comparable<POSTag> {
	private static int tagcount = 0;
	private String name;
	private int index;

	public POSTag(int i, String tag) {
		index = i;
		name = tag;
	}

	public POSTag(String tag) {
		name = tag;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex() {
		index = tagcount++;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		POSTag tag = (POSTag) o;
		return this.name.equalsIgnoreCase(tag.name);
	}

	@Override
	public int compareTo(POSTag o) {
		return this.name.compareToIgnoreCase(o.name);
	}

}
