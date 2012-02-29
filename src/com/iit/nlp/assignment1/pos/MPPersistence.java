package com.iit.nlp.assignment1.pos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.iit.nlp.assignment1.pos.EmissionMatrix.EmissionMatrixColumnEntry;
import com.iit.nlp.assignment1.pos.EmissionMatrix.EmissionMatrixRowEntry;
import com.iit.nlp.assignment1.pos.InitialProbabilityVector.InitialProbabilityVectorEntry;
import com.iit.nlp.assignment1.pos.TransitionMatrix.TransitionMatrixColumnEntry;
import com.iit.nlp.assignment1.pos.TransitionMatrix.TransitionMatrixRowEntry;

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

	private POSTagSet loadTagset(BufferedReader buf) throws IOException {
		String line = null;
		String[] tokens;
		POSTag tag = null;
		POSTagSet tagset = POSTagSet.getPOSTagSet();
		while (!(line = buf.readLine()).equalsIgnoreCase(BLOCK_END)) {
			if (line.equals(""))
				continue;
			tokens = line.split(":");
			tag = new POSTag(Integer.parseInt(tokens[0]), tokens[1]);
			tagset.loadTag(tag);
		}
		tagset.print();
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
					+ " ");
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
		for (POSTag pt : initVec.getInitialProbList().keySet()) {
			out.write(pt.getName() + "|" + initVec.getInitialProbability(pt)
					+ " ");
		}
		out.write("\n" + BLOCK_END + "\n");
	}

	private void saveEmissionMatrix(BufferedWriter out) throws IOException {
		// Writing emission matrix
		EmissionMatrix emMat = params.getEmissionMatrix();
		out.write("\n" + EM_BEGIN + "\n");
		Set<POSTag> itEM = emMat.getEmissionProbMatrix().keySet();
		for (POSTag posTag : itEM) {
			out.write("\n" + posTag.getName() + ": ");
			EmissionMatrixColumnEntry emCE = emMat.getEmissionProbMatrix().get(
					posTag);
			for (Observation obs : emCE.getTransitions().keySet()) {
				out.write(obs.getName() + "|"
						+ emCE.getTransitions().get(obs).getProbability() + " ");

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
			out.write("\n" + posTag.getName() + ": ");
			TransitionMatrixColumnEntry transCE = transMat
					.getTransitionProbMatrix().get(posTag);
			for (POSTag pT : transCE.getTransitions().keySet()) {
				out.write(pT.getName() + "-"
						+ transCE.getTransitions().get(pT).getProbability()
						+ " ");
			}
		}
		out.write("\n" + BLOCK_END + "\n");

	}

	public ModelParameters loadParamsPersistentObject() {
		ModelParameters params = null;
		try {
			FileInputStream objectFile = new FileInputStream(perFile);
			DataInputStream dis = new DataInputStream(objectFile);
			BufferedReader buf = new BufferedReader(new InputStreamReader(dis));
			String dsType; // TPM,EM,IV

			String line;
			typePattern = Pattern
					.compile("\\s*TPM\\s*|\\s*EM\\s*|\\s*INIT\\s*|\\s*OBSERVATIONS\\s*|\\s*TAGS\\s*");
			endPattern = Pattern.compile("\\s*END\\s*");

			while ((line = buf.readLine()) != null) {
				Matcher m = typePattern.matcher(line);
				if (m.matches()) {
					line = line.replaceAll("\\s", "");
					if (line.equals("TPM"))
						getTPMObject(buf);
					else if (line.equals("EM")) {
						currentType = "EM";
						getEMObject(buf);
					} else if (line.equals("INIT")) {
						currentType = "INIT";
						getInitObject(buf);
					} else if (line.equals("OBSERVATIONS"))
						currentType = "OBSERVATIONS";

				} else { // prob value line
					parseLine(line);
				}

			}

		} catch (Exception fe) {
			System.out.println(fe);
			fe.printStackTrace();
		}

		return params;
	}

	private void getTPMObject(BufferedReader buf) {
		String line = "";
		TransitionMatrix tm = new TransitionMatrix();

		try {
			while ((line = buf.readLine()) != null) {
				Matcher m = endPattern.matcher(line);
				if (m.matches())
					return;
				else {
					String[] tokens = line.split("\\s+");
					tokens[0] = tokens[0].replaceAll(":", "");
					POSTag ptag = new POSTag(tokens[0]);
					TransitionMatrixColumnEntry tmce = tm.new TransitionMatrixColumnEntry(
							ptag);
					tm.getTransitionProbMatrix().put(ptag, tmce);
					for (int i = 1; i < tokens.length; i++) {
						String[] subtok = tokens[i].split("-");
						TransitionMatrixRowEntry tmre = tm.new TransitionMatrixRowEntry();
						POSTag pt = new POSTag(subtok[0]);
						tmre.setPostag(pt);
						tmre.setProbability(Float.parseFloat(subtok[1]));
						tmce.getTransitions().put(pt, tmre);
					}

				}

			}
		} catch (Exception fe) {
			fe.printStackTrace();
		}

	}

	private EmissionMatrix getEMObject(BufferedReader buf) {
		String line = "";
		EmissionMatrix em = new EmissionMatrix();

		try {
			while ((line = buf.readLine()) != null) {
				Matcher m = endPattern.matcher(line);
				if (m.matches())
					return em;
				else {
					String[] tokens = line.split("\\s+");
					POSTag ptag = new POSTag(tokens[0].replace(" ", ""));
					Map<POSTag, EmissionMatrixColumnEntry> emProbMat = em
							.getEmissionProbMatrix();

					EmissionMatrixColumnEntry emCM = em.new EmissionMatrixColumnEntry(
							ptag);
					emProbMat.put(ptag, emCM);

					String[] subtok = tokens[1].split("\\s*");
					for (String wptok : subtok) {
						String[] wp = wptok.split("\\|"); // word|probvalue
						EmissionMatrixRowEntry emRE = em.new EmissionMatrixRowEntry();
						Observation obs = new Observation(wp[0]);
						emRE.setWord(obs);
						emRE.setProbability(Float.parseFloat(wp[1]));
						emCM.getTransitions().put(obs, emRE);

					}

				}

			}// while

		} catch (Exception fe) {
			fe.printStackTrace();
		}

		return em;
	}

	private InitialProbabilityVector getInitObject(BufferedReader buf) {
		String line = "";
		InitialProbabilityVector ipv = new InitialProbabilityVector();

		try {
			while ((line = buf.readLine()) != null) {

				if (!line.matches(".*\\w.*")) // contains atleast one
												// alpha-numeric character
					continue;

				Matcher m = endPattern.matcher(line);
				if (m.matches())
					return ipv;
				else {
					String[] tokens = line.split("\\s+");

					for (String tok : tokens) {
						String[] subtok = tok.split("\\|");
						POSTag ptag = new POSTag(subtok[0].replace(" ", ""));
						InitialProbabilityVectorEntry ipvEntry = ipv.new InitialProbabilityVectorEntry(
								ptag);
						ipvEntry.setProbability(Float.parseFloat(subtok[1]));
						ipv.getInitialProbList().put(ptag, ipvEntry);

					}

				}

			}// while

		} catch (Exception fe) {
			fe.printStackTrace();
		}

		return ipv;
	}

	// Parses each line and load corresponding data structure
	private void parseLine(String currLine) {
		if (currentType.equals("TPM")) {

		}

	}

	// save ModelParameters to a file
	/*
	 * public void saveAsPersistentObject(ModelParameters params) {
	 * System.out.println("Saving Model Parameters to file"); try {
	 * FileOutputStream os = new FileOutputStream(objPath); XMLEncoder encoder =
	 * new XMLEncoder(os); encoder.writeObject(params); encoder.close(); }
	 * catch(FileNotFoundException fe) { System.out.println(fe); }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * }
	 */

	// Loads ModelParameters object from XML file
	/*
	 * private ModelParameters loadPersistentObject() { ModelParameters params=
	 * null; System.out.println("Loading model parameters");
	 * 
	 * try { FileInputStream os = new FileInputStream(objPath); XMLDecoder
	 * decoder = new XMLDecoder(os); params =
	 * (ModelParameters)decoder.readObject(); decoder.close();
	 * 
	 * } catch(FileNotFoundException fe) { System.out.println(fe); }
	 * 
	 * return params; }
	 */

}
