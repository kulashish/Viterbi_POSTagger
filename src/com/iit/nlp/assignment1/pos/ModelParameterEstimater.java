package com.iit.nlp.assignment1.pos;

import java.io.IOException;
import java.util.List;

import com.iit.nlp.assignment1.corpus.TaggedDocument;

public class ModelParameterEstimater {

	private ModelParameters parameters;
	private List<TaggedDocument> trainingDocuments;

	public void estimate() throws IOException {
		for (TaggedDocument document : trainingDocuments) {
			document.parseAndUpdateParameters(parameters);
		}
	}
}
