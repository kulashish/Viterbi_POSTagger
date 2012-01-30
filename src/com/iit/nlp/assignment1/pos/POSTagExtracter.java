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
		String input = "ITJ_Yes PUN_, PNP_I VM0_would VVI_like TO0_to VVI_make NP0_ACETPOS_'s NN1_Home NN1-VVB_Care NN1_service CJC_and NN1_Education NN2_Programmes AV0_more AV0_widely AJ0_available CJC_and VVB_enclose AT0_a NN1_donation PRF_of PUN_: <gap desc=\"remainder of form\" resp=OUP_";
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
