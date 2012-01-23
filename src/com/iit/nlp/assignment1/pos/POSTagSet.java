package com.iit.nlp.assignment1.pos;

import java.util.HashSet;
import java.util.Set;

public class POSTagSet {

	private Set<POSTag> tags;

	public void addTag(POSTag tag) {
		if (null == tags)
			tags = new HashSet<POSTag>();
		tags.add(tag);
	}

	public int getIndex(String name) {
		int index = 0;
		for (POSTag tag : tags)
			if (name.equalsIgnoreCase(tag.getName())) {
				index = tag.getIndex();
				break;
			}
		return index;
	}
}
