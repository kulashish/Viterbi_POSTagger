package com.iit.nlp.assignment1.corpus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Corpus {

	private String path;
	private File corpusFile;
	private List<TaggedDocument> documents;

	public Corpus() {

	}

	public Corpus(String path) {
		this.path = path;
		corpusFile = new File(path);
		for (File file : corpusFile.listFiles())
			addTaggedDocument(file);
	}

	public List<TaggedDocument> getTrainingSet(int index) {
		int subsetSize = documents.size() / 5;
		List<TaggedDocument> trainingSet = new ArrayList<TaggedDocument>();
		int start = 0;
		int end = 0;
		for (int i = 0; i < 5; i++) {
			if (i == index)
				continue;
			start = i * subsetSize;
			end = i == 4 ? documents.size() : start + subsetSize;
			trainingSet.addAll(documents.subList(start, end));
		}
		return trainingSet;
	}

	public List<TaggedDocument> getTestSet(int index) {
		int subsetSize = documents.size() / 5;
		List<TaggedDocument> testSet = new ArrayList<TaggedDocument>();
		int start = index * subsetSize;
		int end = index == 4 ? documents.size() : start + subsetSize;
		testSet.addAll(documents.subList(start, end));
		return testSet;
	}

	private void addTaggedDocument(File file) {
		if (null == documents)
			documents = new ArrayList<TaggedDocument>();
		documents.add(new TaggedDocument(file));
	}

	public List<TaggedDocument> getDocuments() {
		return documents;
	}

}
