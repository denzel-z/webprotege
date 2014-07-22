package edu.stanford.bmir.protege.web.shared.termbuilder.wordnet;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;
import edu.mit.jwi.morph.WordnetStemmer;
import edu.stanford.bmir.protege.web.shared.termbuilder.Concept;
import edu.stanford.bmir.protege.web.shared.termbuilder.ConceptList;
import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo;
import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo.ConceptRelation;
import edu.stanford.bmir.protege.web.shared.termbuilder.TBStringUtils;
import edu.stanford.bmir.protege.web.shared.termbuilder.questionparsing.WordAndPOSTag;
import edu.stanford.nlp.util.StringUtils;


/**
 * This is a singleton class. For this class, you don't need to create an instance
 * for every conceptName you search.
 *
 * @author Yuhao Zhang
 *
 */
public class WordNetSearch {

	private String DICT_PATH = "wordnetdb";
    private String DATA_PATH = "/data/webprotege/";
	private IDictionary dict;
	private WordnetStemmer stemmer;

	private static WordNetSearch instance = null;

    public static final int MAX_SIBLING_SUPERCLASS_NUM = 5;

	protected WordNetSearch() throws IOException {
//		URL url = new URL("file", null, DICT_PATH);
//        URL url = new URL("file", null, System.getProperty("user.dir") + "/" + DICT_PATH);
        URL url = new URL("file", null, DATA_PATH + DICT_PATH);

        System.out.println("############# WordNet file URL: ###########");
        System.out.println(url.toString());
        System.out.println(url.getPath());
        System.out.println(System.getProperty("user.dir"));

        dict = new Dictionary(url);
		dict.open();

		stemmer = new WordnetStemmer(dict);
	}

	public static WordNetSearch getInstance() throws IOException {
		if(instance == null) {
			instance = new WordNetSearch();
		}
		return instance;
	}

	public List<RecommendedConceptInfo> searchConcept(Concept srcConcept) {
		List<RecommendedConceptInfo> recommendedConcept = new ArrayList<RecommendedConceptInfo>();
		List<RecommendedConceptInfo> conceptList; //temp store

		String normalizedConceptName = normalizeConceptNameByConcept(srcConcept);
		String capNormalizedConceptName = convertWordNetOutputToConceptString(normalizedConceptName);
		// get the synset
		IIndexWord idxWord = dict.getIndexWord(normalizedConceptName, POS.NOUN);
        if(idxWord == null || idxWord.getWordIDs().size() == 0) {
            // no related concept in WordNet, simply return empty list.
            return recommendedConcept;
        }
		IWordID wordID = idxWord.getWordIDs().get(0);
		IWord iword = dict.getWord(wordID);
		ISynset synset = iword.getSynset();

		//Get Synonyms
		List<IWord> synonyms = synset.getWords();
		for(Iterator<IWord> i = synonyms.iterator(); i.hasNext();) {
			String syn = i.next().getLemma();
			if(syn != normalizedConceptName && syn!=capNormalizedConceptName) {
				syn = convertWordNetOutputToConceptString(syn);
				RecommendedConceptInfo info = new RecommendedConceptInfo(srcConcept, new Concept(syn),
						RecommendedConceptInfo.ConceptRelation.SYNONYM);
				recommendedConcept.add(info);
			}
		}

		//Get hypernyms
		List<ISynsetID> hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM);
		conceptList = addSynsetsIntoRecommendedConceptsList(srcConcept, hypernyms,
				RecommendedConceptInfo.ConceptRelation.SUPERCLASS_OF);
		recommendedConcept.addAll(conceptList);

		//Get hyponym
		List<ISynsetID> hyponyms = synset.getRelatedSynsets(Pointer.HYPONYM);
		conceptList = addSynsetsIntoRecommendedConceptsList(srcConcept, hyponyms,
				RecommendedConceptInfo.ConceptRelation.SUBCLASS_OF);
		recommendedConcept.addAll(conceptList);

        // Get siblings (concepts with the same super class)
        conceptList = getSiblingConcepts(hypernyms, srcConcept);
        recommendedConcept.addAll(conceptList);

		/**
         *  Don't care about meronym part relation
		List<ISynsetID> meronyms = synset.getRelatedSynsets(Pointer.MERONYM_PART);
		conceptList = addSynsetsIntoRecommendedConceptsList(srcConcept, meronyms,
				RecommendedConceptInfo.ConceptRelation.PART_OF);
		recommendedConcept.addAll(conceptList);
		*/

		//Get Topic and domain
		List<ISynsetID> comb = new ArrayList<ISynsetID>();
		List<ISynsetID> topics = synset.getRelatedSynsets(Pointer.TOPIC);
		List<ISynsetID> domains = synset.getRelatedSynsets(Pointer.DOMAIN);
		comb.addAll(topics);
		comb.addAll(domains);
		conceptList = addSynsetsIntoRecommendedConceptsList(srcConcept, comb,
				RecommendedConceptInfo.ConceptRelation.RELATED_TO);
		recommendedConcept.addAll(conceptList);

		return recommendedConcept;
	}

    private List<RecommendedConceptInfo> getSiblingConcepts(List<ISynsetID> hypernyms, Concept srcConcept) {
        int count = 0;
        List<RecommendedConceptInfo> result = new ArrayList<RecommendedConceptInfo>();
        // Used to remove duplicates
        Set<RecommendedConceptInfo> resultSet = new HashSet<RecommendedConceptInfo>();
        for(ISynsetID sid : hypernyms) {
            count++;
            if(count > MAX_SIBLING_SUPERCLASS_NUM) {
                break;
            }
            ISynset synset = dict.getSynset(sid);
            List<ISynsetID> siblings = synset.getRelatedSynsets(Pointer.HYPONYM);
            List<RecommendedConceptInfo> cList = addSynsetsIntoRecommendedConceptsList(srcConcept, siblings, ConceptRelation.SIBLING_OF);
            resultSet.addAll(cList);
        }
        result.addAll(resultSet);
        return result;
    }

    private List<RecommendedConceptInfo> addSynsetsIntoRecommendedConceptsList(Concept srcConcept,
			List<ISynsetID> synsets, ConceptRelation relation) {
		List<IWord> words;
		List<RecommendedConceptInfo> recommendedConcept = new ArrayList<RecommendedConceptInfo>();
		for(ISynsetID sid : synsets) {
			words = dict.getSynset(sid).getWords();
			for(Iterator<IWord> i = words.iterator(); i.hasNext();) {
				String w = i.next().getLemma();
				w = convertWordNetOutputToConceptString(w);
				RecommendedConceptInfo info = new RecommendedConceptInfo(srcConcept, new Concept(w),
						relation);
				recommendedConcept.add(info);
			}
		}
		return recommendedConcept;
	}

	/**
	 * Convert a concatenated WordNet-output format string into a Concept format.
	 * For example, "computer_network" will be converted into "Computer Network".
	 *
	 * @param output
	 * @return
	 */
	public String convertWordNetOutputToConceptString(String output) {
		String[] tokens = output.split("_");
		StringBuffer sb = new StringBuffer();
		int len = tokens.length;
		for(int i=0; i<tokens.length; i++) {
			sb.append(Character.toUpperCase(tokens[i].charAt(0)) + tokens[i].substring(1));
			if(i < len-1) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	/**
	 * Get WordNet supported version of the word normalization.
	 *
	 * @param srcConcept
	 * @return
	 */
	public String normalizeConceptNameByConcept(Concept srcConcept) {
        String srcConceptName = srcConcept.getConceptName();
        // To handle the case where the word is all in upper case
        if(TBStringUtils.isAllUpper(srcConceptName)) {
            return srcConceptName;
        }
		String normalizedConceptName = srcConceptName.toLowerCase();
		normalizedConceptName = stemmer.findStems(normalizedConceptName, POS.NOUN).get(0);
		return normalizedConceptName;
	}

	public String normalizeConceptNameByString(String conceptName) {
        // To handle the case where the word is all in upper case
        if(TBStringUtils.isAllUpper(conceptName)) {
            return conceptName;
        }
		String normalizedConceptName = conceptName.toLowerCase();
        List<String> stemmed = stemmer.findStems(normalizedConceptName, POS.NOUN);
        if(stemmed.size() > 0) {
            normalizedConceptName = stemmed.get(0);
        }
		return normalizedConceptName;
	}

    public String normalizeConceptNameByWordAndPOSTag(WordAndPOSTag w) {
        // To handle the case where the word is all in upper case
        if(TBStringUtils.isAllUpper(w.getWord())) {
            return w.getWord();
        }
        String normalizedConceptName = w.getWord().toLowerCase();
        List<String> stemmed = null;
        if(w.isNoun()) {
            stemmed = stemmer.findStems(normalizedConceptName, POS.NOUN);
        } else if(w.isAdj()) {
            stemmed = stemmer.findStems(normalizedConceptName, POS.ADJECTIVE);
        }
        if(stemmed != null && stemmed.size() > 0) {
            normalizedConceptName = stemmed.get(0);
        }
        return normalizedConceptName;
    }
}