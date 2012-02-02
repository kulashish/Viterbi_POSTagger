package com.iit.nlp.assignment1.corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

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
		System.out.println("Parsing " + filePtr.getName() + " ...");
		reader = new BufferedReader(new FileReader(filePtr));

		String line = null;
		POSTagExtracter tagExtracter = new POSTagExtracter();
		String[] tags = null;
		String[] observations = null;

		while (null != (line = reader.readLine())) {
			// System.out.println(line);
			tags = tagExtracter.tags(line);
			observations = tagExtracter.words(line);
			if (tags.length != observations.length) {
				// System.out.println(line);
				int min = tags.length < observations.length ? tags.length
						: observations.length;
				tags = Arrays.copyOfRange(tags, 0, min);
				observations = Arrays.copyOfRange(observations, 0, min);
			}
			parameters.updateParameters(tags, observations);
		}
		if (null != reader)
			reader.close();
	}
}
