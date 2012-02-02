package com.iit.nlp.assignment1;

import java.io.IOException;

import com.iit.nlp.assignment1.corpus.Corpus;
import com.iit.nlp.assignment1.pos.ModelParameterEstimater;
import com.iit.nlp.assignment1.pos.ModelParameters;

public class POSTagger {

	private Corpus corpus;

	public POSTagger(Corpus corpus) {
		this.corpus = corpus;
	}

	public static void main(String... args) {
		if (null == args || args.length != 1) {
			System.out.println("Usage: java POSTagger <CORPUS_PATH>");
			System.exit(1);
		}
		Corpus corpus = new Corpus(args[0]);
		POSTagger postagger = new POSTagger(corpus);
		try {
			postagger.startTagging();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private void startTagging() throws IOException {
		ModelParameterEstimater parameterEstimater = new ModelParameterEstimater(
				new ModelParameters(), corpus.getDocuments());
		long time1 = System.currentTimeMillis();
		parameterEstimater.estimate();
		System.out.println(System.currentTimeMillis() - time1);

	}
}
