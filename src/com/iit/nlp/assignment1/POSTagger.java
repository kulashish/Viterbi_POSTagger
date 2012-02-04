package com.iit.nlp.assignment1;

import java.io.IOException;

import com.iit.nlp.assignment1.corpus.Corpus;
import com.iit.nlp.assignment1.pos.ModelParameterEstimater;
import com.iit.nlp.assignment1.pos.ModelParameters;
import com.iit.nlp.assignment1.pos.ViterbiAlgorithm;

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
		float acc[] = new float[5];
		for (int i = 0; i < 5; i++) {
			System.out.println("Iteration " + (i + 1));
			ModelParameterEstimater parameterEstimater = new ModelParameterEstimater(
					new ModelParameters(), corpus.getTrainingSet(i));
			long time1 = System.currentTimeMillis();
			parameterEstimater.estimate();
			System.out.println(System.currentTimeMillis() - time1);
			ViterbiAlgorithm viterbi = new ViterbiAlgorithm(parameterEstimater
					.getParameters(), corpus.getTestSet(i));
			acc[i] = viterbi.run();
			System.out.println("Accuracy : " + acc[i]);
		}
		System.out.println("Average accuracy : "
				+ (acc[0] + acc[1] + acc[2] + acc[3] + acc[4]) / 5);
	}
}
