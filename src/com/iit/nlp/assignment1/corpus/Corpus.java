package com.iit.nlp.assignment1.corpus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Corpus {

	private List<TaggedDocument> documents;
	private List<TaggedDocument> testDocuments;

	public Corpus() {

	}

	public Corpus(String trainingPath, String testPath) {
		if (null != trainingPath) {
			File trainingFile = new File(trainingPath);
			for (File file : trainingFile.listFiles())
				addTaggedDocument(file);
		}
		if (null != testPath) {
			File testFile = new File(testPath);
			for (File file : testFile.listFiles())
				addTestDocument(file);
		}
	}

	private void addTestDocument(File file) {
		if (null == testDocuments)
			testDocuments = new ArrayList<TaggedDocument>();
		testDocuments.add(new TaggedDocument(file));
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

	public List<TaggedDocument> getTestDocuments() {
		return testDocuments;
	}

}
