package com.iit.nlp.assignment1.pos;

import java.io.IOException;
import java.util.List;

import com.iit.nlp.assignment1.corpus.TaggedDocument;

public class ModelParameterEstimater {

	private ModelParameters parameters;
	private List<TaggedDocument> trainingDocuments;

	public ModelParameterEstimater() {

	}

	public ModelParameterEstimater(ModelParameters params,
			List<TaggedDocument> docs) {
		parameters = params;
		trainingDocuments = docs;
	}

	public void estimate() throws IOException {
		for (TaggedDocument document : trainingDocuments)
			document.parseAndUpdateParameters(parameters);
		parameters.updateParameters();
		System.out.println("Number of tags : "
				+ parameters.getTagSet().getSize());
//		parameters.getTagSet().print();
		System.out.println("Transition Matrix has "
				+ parameters.getTransitionMatrix().getSize());
//		parameters.getTransitionMatrix().print();
		System.out.println("Emission Matrix has "
				+ parameters.getEmissionMatrix().getSize());
		System.out.println("Initial Probability vector has "
				+ parameters.getInitialProbVec().getSize());
	}
}
