package com.iit.nlp.assignment1.pos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import com.iit.nlp.assignment1.pos.EmissionMatrix.EmissionMatrixColumnEntry;
import com.iit.nlp.assignment1.pos.TransitionMatrix.TransitionMatrixColumnEntry;

public class MPPersistence {

	private static final String TAGS_BEGIN = "TAGS";
	private static final String OBSERVATIONS_BEGIN = "OBSERVATIONS";
	private static final String TPM_BEGIN = "TPM";
	private static final String IPV_BEGIN = "INIT";
	private static final String EM_BEGIN = "EM";
	private static final String BLOCK_END = "END";

	private String perFile;
	String currentType = ""; // current type of data structure while parsing
	Pattern typePattern, endPattern;
	private ModelParameters params;

	public MPPersistence() {

	}

	public MPPersistence(String modelParamFilePath) {
		perFile = modelParamFilePath;
	}

	public static void main(String[] args) {
		MPPersistence per = new MPPersistence(
				"/home/ashish/NLP POS Tagging Assignment/params.dat");
		per.loadParameters();
	}

	// Save Model parameters to a file
	public void saveParameters(ModelParameters params) {
		this.params = params;
		try {
			FileWriter fstream = new FileWriter(perFile);
			BufferedWriter out = new BufferedWriter(fstream);

			saveTagset(out);
			saveObservationset(out);
			saveTransitionMatrix(out);
			saveEmissionMatrix(out);
			saveInitialProbabilityVector(out);

			out.close();
			fstream.close();
		}// try
		catch (Exception fe) {
			System.out.println(fe);
		}
	}

	public ModelParameters loadParameters() {
		ModelParameters params = new ModelParameters();
		FileInputStream objectFile = null;
		DataInputStream dis = null;
		BufferedReader buf = null;
		try {
			objectFile = new FileInputStream(perFile);
			dis = new DataInputStream(objectFile);
			buf = new BufferedReader(new InputStreamReader(dis));
			while (!buf.readLine().equalsIgnoreCase(TAGS_BEGIN))
				;
			params.setTagSet(loadTagset(buf));
			while (!buf.readLine().equalsIgnoreCase(OBSERVATIONS_BEGIN))
				;
			params.setObservationSet(loadObservationset(buf));
			while (!buf.readLine().equalsIgnoreCase(TPM_BEGIN))
				;
			params.setTransitionMatrix(loadTransitionMatrix(buf));
			while (!buf.readLine().equalsIgnoreCase(EM_BEGIN))
				;
			params.setEmissionMatrix(loadEmissionMatrix(buf));
			while (!buf.readLine().equalsIgnoreCase(IPV_BEGIN))
				;
			params.setInitialProbVec(loadInitialProbabilityVector(buf));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buf.close();
				dis.close();
				objectFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return params;
	}

	private InitialProbabilityVector loadInitialProbabilityVector(
			BufferedReader buf) throws NumberFormatException, IOException {
		InitialProbabilityVector initProbVec = new InitialProbabilityVector();
		String line = null;
		POSTag tag = null;
		while (!(line = buf.readLine()).equalsIgnoreCase(BLOCK_END)) {
			if (line.equals(""))
				continue;
			int i = line.indexOf('|');
			if (i != -1) {
				tag = POSTagSet.getPOSTagSet().addTag(line.substring(0, i));
				initProbVec.loadState(tag,
						Float.parseFloat(line.substring(i + 1, line.length())));
			} else {
				initProbVec.setSum(Integer.parseInt(line));
			}
		}
//		initProbVec.print();
		return initProbVec;
	}

	private EmissionMatrix loadEmissionMatrix(BufferedReader buf)
			throws NumberFormatException, IOException {
		EmissionMatrix emissionMatrix = new EmissionMatrix();
		String line = null;
		POSTag tag = null;
		Observation observation = null;
		String[] rowtokens = null;
		int sum = 0;
		while (!(line = buf.readLine()).equalsIgnoreCase(BLOCK_END)) {
			if (line.equals(""))
				continue;
			int i = line.indexOf('^');
			tag = POSTagSet.getPOSTagSet().addTag(line.substring(0, i));
			sum = Integer.parseInt(line.substring(i + 1, line.indexOf(',')));
			rowtokens = line.substring(line.indexOf(',') + 1, line.length())
					.split("##");
			for (String rowtoken : rowtokens) {
				i = rowtoken.indexOf('|');
				observation = ObservationSet.getObservationSet()
						.addObservation(rowtoken.substring(0, i));
				emissionMatrix.loadEmission(
						tag,
						observation,
						sum,
						Float.parseFloat(rowtoken.substring(i + 1,
								rowtoken.length())));
			}
		}
		return emissionMatrix;
	}

	private TransitionMatrix loadTransitionMatrix(BufferedReader buf)
			throws NumberFormatException, IOException {
		TransitionMatrix transitionMatrix = new TransitionMatrix();
		String line = null;
		String[] tokens = null;
		POSTag tag1 = null;
		POSTag tag2 = null;
		String[] rowtokens = null;
		int sum = 0;
		while (!(line = buf.readLine()).equalsIgnoreCase(BLOCK_END)) {
			if (line.equals(""))
				continue;
			tokens = line.split(":");
			tag1 = POSTagSet.getPOSTagSet().addTag(tokens[0]);
			sum = Integer.parseInt(tokens[1].substring(0,
					tokens[1].indexOf(',')));
			tokens[1] = tokens[1].substring(tokens[1].indexOf(',') + 1);
			rowtokens = tokens[1].split(" ");
			for (String rowtoken : rowtokens) {
				int i = rowtoken.indexOf('|');
				tag2 = POSTagSet.getPOSTagSet()
						.addTag(rowtoken.substring(0, i));
				transitionMatrix.loadTransition(
						tag1,
						tag2,
						sum,
						Float.parseFloat(rowtoken.substring(i + 1,
								rowtoken.length())));
			}
		}
//		transitionMatrix.print();
		return transitionMatrix;
	}

	private ObservationSet loadObservationset(BufferedReader buf)
			throws NumberFormatException, IOException {
		ObservationSet observationSet = ObservationSet.getObservationSet();
		String line = null;
		Observation observation = null;
		while (!(line = buf.readLine()).equalsIgnoreCase(BLOCK_END)) {
			if (line.equals(""))
				continue;
			int i = line.indexOf('^');
			observation = new Observation(
					Integer.parseInt(line.substring(0, i)), line.substring(
							i + 1, line.length()));
			observationSet.loadObservation(observation);
		}
		return observationSet;
	}

	private POSTagSet loadTagset(BufferedReader buf) throws IOException {
		String line = null;
		String[] tokens = null;
		POSTag tag = null;
		POSTagSet tagset = POSTagSet.getPOSTagSet();
		while (!(line = buf.readLine()).equalsIgnoreCase(BLOCK_END)) {
			if (line.equals(""))
				continue;
			tokens = line.split(":");
			tag = new POSTag(Integer.parseInt(tokens[0]), tokens[1]);
			tagset.loadTag(tag);
		}
//		tagset.print();
		return tagset;
	}

	private void saveObservationset(BufferedWriter out) throws IOException {
		ObservationSet observationSet = params.getObservationSet();
		out.write("\n" + OBSERVATIONS_BEGIN + "\n");
		Iterator<Observation> iter = observationSet.getObservations().values()
				.iterator();
		Observation observation = null;
		while (iter.hasNext()) {
			observation = iter.next();
			out.write(observation.getIndex() + "^" + observation.getName()
					+ "\n");
		}
		out.write("\n" + BLOCK_END + "\n");
	}

	private void saveTagset(BufferedWriter out) throws IOException {
		POSTagSet tagset = params.getTagSet();
		out.write("\n" + TAGS_BEGIN + "\n");
		Set<POSTag> tags = tagset.getTags();
		for (POSTag tag : tags)
			out.write(tag.getIndex() + ":" + tag.getName() + "\n");
		out.write("\n" + BLOCK_END + "\n");
	}

	private void saveInitialProbabilityVector(BufferedWriter out)
			throws IOException {
		// Write initial probability matrix
		InitialProbabilityVector initVec = params.getInitialProbVec();
		out.write("\n" + IPV_BEGIN + "\n");
		out.write(initVec.getSum() + "\n");
		for (POSTag pt : initVec.getInitialProbList().keySet()) {
			out.write(pt.getName() + "|" + initVec.getInitialProbability(pt)
					+ "\n");
		}
		out.write("\n" + BLOCK_END + "\n");
	}

	private void saveEmissionMatrix(BufferedWriter out) throws IOException {
		// Writing emission matrix
		EmissionMatrix emMat = params.getEmissionMatrix();
		out.write("\n" + EM_BEGIN + "\n");
		Set<POSTag> itEM = emMat.getEmissionProbMatrix().keySet();
		for (POSTag posTag : itEM) {
			out.write("\n" + posTag.getName() + "^");
			EmissionMatrixColumnEntry emCE = emMat.getEmissionProbMatrix().get(
					posTag);
			out.write(emCE.getSum() + ",");
			for (Observation obs : emCE.getTransitions().keySet()) {
				out.write(obs.getName() + "|"
						+ emCE.getTransitions().get(obs).getProbability()
						+ "##");
			}
		}
		out.write("\n" + BLOCK_END + "\n");
	}

	private void saveTransitionMatrix(BufferedWriter out) throws IOException {
		// Writing Transition probability matrix information
		TransitionMatrix transMat = params.getTransitionMatrix();
		out.write("\n" + TPM_BEGIN + "\n");
		Set<POSTag> it = transMat.getTransitionProbMatrix().keySet();
		for (POSTag posTag : it) {
			out.write("\n" + posTag.getName() + ":");
			TransitionMatrixColumnEntry transCE = transMat
					.getTransitionProbMatrix().get(posTag);
			out.write(transCE.getSum() + ",");
			for (POSTag pT : transCE.getTransitions().keySet()) {
				out.write(pT.getName() + "|"
						+ transCE.getTransitions().get(pT).getProbability()
						+ " ");
			}
		}
		out.write("\n" + BLOCK_END + "\n");

	}

}
