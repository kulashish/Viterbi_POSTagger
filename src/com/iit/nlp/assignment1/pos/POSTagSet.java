package com.iit.nlp.assignment1.pos;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class POSTagSet {

	private static POSTagSet tagset = null;
	private SortedSet<POSTag> tags;

	public SortedSet<POSTag> getTags() {
		if (null == tags)
			tags = new TreeSet<POSTag>();
		return tags;
	}

	public void loadTag(POSTag tag) {
		getTags().add(tag);
	}

	public POSTag addTag(POSTag tag) {
		getTags().add(tag);
		tag.setIndex();
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
			System.out.print(tag.getIndex() + " : " + tag.getName() + ",");
		}
		System.out.println();
	}

	public POSTag getTag(int index) {
		Iterator<POSTag> iter = tags.iterator();
		POSTag tag = null;
		while (iter.hasNext()) {
			tag = iter.next();
			if (tag.getIndex() == index)
				break;
		}
		return tag;
	}

	public POSTag[] getTags(String[] strTags) {
		POSTag[] postags = new POSTag[strTags.length];
		for (int index = 0; index < strTags.length; index++)
			postags[index] = addTag(strTags[index]);
		return postags;
	}

	public int compareTags(POSTag[] tagset1, POSTag[] tagset2) {
		int difference = Math.abs(tagset1.length - tagset2.length);
		int minsize = Math.min(tagset1.length, tagset2.length);
		for (int index = 0; index < minsize; index++)
			if (!tagset1[index].equals(tagset2[index]))
				difference++;
		return difference;
	}

	public static POSTagSet getPOSTagSet() {
		if (null == tagset)
			tagset = new POSTagSet();
		return tagset;
	}
}
