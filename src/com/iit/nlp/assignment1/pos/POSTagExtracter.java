package com.iit.nlp.assignment1.pos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class POSTagExtracter {

	private static final String POSTAG_REGEX = "[A-Z0-9]{3}(-[A-Z0-9]{3})*_";
	private static Pattern POSTagPattern;

	static {
		POSTagPattern = Pattern.compile(POSTAG_REGEX);
	}

	public String[] tags(String input) {
		Matcher matcher = POSTagPattern.matcher(input);
		List<String> tags = new ArrayList<String>();
		String tag = null;
		while (matcher.find()) {
			tag = matcher.group();
			tag = tag.substring(0, tag.indexOf('_'));
			tags.add(tag);
		}
		return (String[]) tags.toArray(new String[tags.size()]);
	}

	public String[] words(String input) {
		String[] words = POSTagPattern.split(input);
		for (int i = 0; i < words.length; i++)
			words[i] = words[i].trim();
		return Arrays.copyOfRange(words, 1, words.length);
	}

	public static void main(String... args) {
		POSTagExtracter extracter = new POSTagExtracter();
		String input = "CJS_As AT0_the NN2_roots VVB_begin TO0_to VVI_break PRP_out of AT0_the NN2_grains AT0_the NN1_barley VBZ_is VVN_transferred PRP_to AT0_a AJ0_vast NN1_hall VVN_heated PRP_with AJ0_warm NN1_air CJC_and VVD-VVN_turned PRP_by AJ0_large ";
		String[] matches = extracter.tags(input);
		System.out.println(matches.length);
		for (String match : matches)
			System.out.println(match);
		String[] words = extracter.words(input);
		System.out.println(words.length);
		for (String word : words)
			System.out.println(word);
	}

}
