package com.iit.nlp.assignment1.pos;

import java.util.HashSet;
import java.util.Set;

public class POSTagSet {

	private Set<POSTag> tags;

	public Set<POSTag> getTags() {
		if (null == tags)
			tags = new HashSet<POSTag>();
		return tags;
	}

	public POSTag addTag(POSTag tag) {
		// System.out.println(tag.getName());
		getTags().add(tag);
		return tag;
	}

	public int getIndex(String name) {
		int index = -1;
		for (POSTag tag : getTags())
			if (name.equalsIgnoreCase(tag.getName())) {
				index = tag.getIndex();
				break;
			}
		return index;
	}

	public POSTag addTag(String tagname) {
		POSTag tagTosearch = null;
		for (POSTag tag : getTags())
			if (tagname.equalsIgnoreCase(tag.getName())) {
				tagTosearch = tag;
				break;
			}
		return null != tagTosearch ? tagTosearch : addTag(new POSTag(tagname));
	}

	public int getSize() {
		return getTags().size();
	}
}
