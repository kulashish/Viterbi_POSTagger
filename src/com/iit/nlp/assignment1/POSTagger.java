package com.iit.nlp.assignment1;

import com.iit.nlp.assignment1.corpus.Corpus;

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
	}
}
