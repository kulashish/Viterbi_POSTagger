package com.iit.nlp.assignment1.corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.iit.nlp.assignment1.pos.ModelParameters;
import com.iit.nlp.assignment1.pos.POSTagExtracter;

public class TaggedDocument {

	private File filePtr;
	private BufferedReader reader;

	public TaggedDocument(File file) {
		filePtr = file;
	}

	public void parseAndUpdateParameters(ModelParameters parameters)
			throws IOException {
		reader = new BufferedReader(new FileReader(filePtr));
		reader.readLine();

		String line = null;
		POSTagExtracter tagExtracter = new POSTagExtracter();
		String[] tags = null;
		String[] observations = null;
		while (null != (line = reader.readLine())) {
			tags = tagExtracter.tags(line);
			observations = tagExtracter.words(line);
			parameters.updateParameters(tags, observations);
		}
		if (null != reader)
			reader.close();
	}
}
