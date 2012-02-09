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
			if (matcher.end() == input.length()
					|| input.charAt(matcher.end()) == ' ')
				continue;
			tag = tag.substring(0, tag.indexOf('_'));
			tags.add(tag);
		}
		return (String[]) tags.toArray(new String[tags.size()]);
	}

	public String[] words(String input) {
		String[] words = POSTagPattern.split(input);
		List<String> wordList = new ArrayList<String>();
		for (int i = 1; i < words.length; i++) {
			if (words[i].charAt(0) == ' ')
				continue;
			if (words[i].indexOf('<') != -1)
				words[i] = words[i].substring(0, words[i].indexOf('<'));
			wordList.add(words[i].trim());
		}
		return (String[]) wordList.toArray(new String[wordList.size()]);
		// Arrays.copyOfRange(words, 1, words.length);
	}

	public static void main(String... args) {
		POSTagExtracter extracter = new POSTagExtracter();
		String input = "TO0_To VVI_win AT0_a NN1_prize VVB_send AVP_in DPS_your NN2_captions PRP_as well as DT0_any NN2_photographs PNP_you VVB_think VM0_could VVI_feature PRP-AVP_in DT0_this NN1_section PRP_to PUN_: OUP_ <hi rend=it NN2_Dogs AV0_Today </hi_ <gap desc=address resp=OUP_";
		String[] matches = extracter.tags(input);
		System.out.println(matches.length);
		for (String match : matches)
			System.out.print(match + ", ");

		String[] words = extracter.words(input);
		System.out.println(words.length);
		for (String word : words)
			System.out.print(word + ", ");
		System.out.println();
	}

}
