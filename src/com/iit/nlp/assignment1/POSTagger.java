package com.iit.nlp.assignment1;

import java.io.IOException;

import com.iit.nlp.assignment1.corpus.Corpus;
import com.iit.nlp.assignment1.pos.MPPersistence;
import com.iit.nlp.assignment1.pos.ModelParameterEstimater;
import com.iit.nlp.assignment1.pos.ModelParameters;
import com.iit.nlp.assignment1.pos.ViterbiAlgorithm;

public class POSTagger {

	private Corpus corpus;

	public POSTagger(Corpus corpus) {
		this.corpus = corpus;
	}

	public static void main(String... args) {
		if (null == args || args.length < 2) {
			System.out
					.println("Usage: java POSTagger <TRAINING_CORPUS_PATH> <TEST_CORPUS_PATH");
			System.exit(1);
		}
		if (args[0].equalsIgnoreCase("-train")) {
			String trainingCorpusPath = args[1];
			String modelParamFilePath = args[2];
			Corpus corpus = new Corpus(trainingCorpusPath, null);
			POSTagger postagger = new POSTagger(corpus);
			ModelParameters params = null;
			MPPersistence persistence = new MPPersistence(modelParamFilePath);
			try {
				params = postagger.train();
				persistence.saveParameters(params);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (args[0].equalsIgnoreCase("-test")) {
			String modelParamFilePath = args[1];
			String testCorpusPath = args[2];
			MPPersistence persistence = new MPPersistence(modelParamFilePath);
			ModelParameters params = persistence.loadParameters();
			Corpus corpus = new Corpus(null, testCorpusPath);
			POSTagger tagger = new POSTagger(corpus);
			tagger.test(params);
		}
		// Corpus corpus = new Corpus(args[0], args[1]);
		// POSTagger postagger = new POSTagger(corpus);
		// try {
		// postagger.startTagging();
		// } catch (IOException e) {
		//
		// e.printStackTrace();
		// }
	}

	private ModelParameters train() throws IOException {
		ModelParameterEstimater parameterEstimater = new ModelParameterEstimater(
				new ModelParameters(), corpus.getDocuments());
		parameterEstimater.estimate();
		return parameterEstimater.getParameters();
	}

	private void test(ModelParameters params) {
		ViterbiAlgorithm viterbi = new ViterbiAlgorithm(params,
				corpus.getTestDocuments());
		Result result = viterbi.run();
		result.print();
	}

	private void startTagging() throws IOException {
		// float acc[] = new float[5];
		float lineaccuracy = 0f;
		// for (int i = 0; i < 5; i++) {
		// System.out.println("Iteration " + (i + 1));
		// ModelParameterEstimater parameterEstimater = new
		// ModelParameterEstimater(
		// new ModelParameters(), corpus.getTrainingSet(i));
		// long time1 = System.currentTimeMillis();
		// parameterEstimater.estimate();
		// System.out.println(System.currentTimeMillis() - time1);
		// ViterbiAlgorithm viterbi = new ViterbiAlgorithm(
		// parameterEstimater.getParameters(), corpus.getTestSet(i));
		// acc[i] = viterbi.run();
		// System.out.println("Accuracy : " + acc[i]);
		// System.out.println("Observations accuracy : "
		// + viterbi.getObservationAccuracy());
		// }
		//
		// ModelParameterEstimater parameterEstimater = new
		// ModelParameterEstimater(
		// new ModelParameters(), corpus.getDocuments());
		// parameterEstimater.estimate();
		// ViterbiAlgorithm viterbi = new ViterbiAlgorithm(
		// parameterEstimater.getParameters(), corpus.getTestDocuments());
		// lineaccuracy = viterbi.run();
		// System.out.println("Line Accuracy : " + lineaccuracy);
		// System.out.println("Word accuracy : "
		// + viterbi.getObservationAccuracy());
	}
}
