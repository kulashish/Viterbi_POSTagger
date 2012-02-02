package com.iit.nlp.assignment1.pos;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class POSTagSet {

	private SortedSet<POSTag> tags;

	public SortedSet<POSTag> getTags() {
		if (null == tags)
			tags = new TreeSet<POSTag>();
		return tags;
	}

	public POSTag addTag(POSTag tag) {
		// System.out.println(tag.getName());
		getTags().add(tag);
		return tag;
	}

	public int getIndex(String name) {
		POSTag tag = new POSTag(name);
		return getTags().tailSet(tag).first().getIndex();
	}

	public POSTag addTag(String tagname) {
		POSTag tag = new POSTag(tagname);
		POSTag tagTosearch = getTags().contains(tag) ? getTags().tailSet(tag)
				.first() : addTag(tag);
		return tagTosearch;
	}

	public int getSize() {
		return getTags().size();
	}

	public void print() {
		Iterator<POSTag> iter = tags.iterator();
		POSTag tag = null;
		while (iter.hasNext()) {
			tag = iter.next();
			System.out.println(tag.getIndex() + " : " + tag.getName());
		}
	}
}
