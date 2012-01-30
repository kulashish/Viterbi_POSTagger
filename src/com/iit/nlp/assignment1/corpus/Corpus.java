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

	private void addTaggedDocument(File file) {
		if (null == documents)
			documents = new ArrayList<TaggedDocument>();
		documents.add(new TaggedDocument(file));
	}

	public List<TaggedDocument> getDocuments() {
		return documents;
	}

}
