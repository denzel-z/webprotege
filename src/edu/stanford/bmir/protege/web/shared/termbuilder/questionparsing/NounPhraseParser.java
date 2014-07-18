package edu.stanford.bmir.protege.web.shared.termbuilder.questionparsing;

import edu.stanford.bmir.protege.web.client.ui.termbuilder.TermBuilderConstant;
import edu.stanford.bmir.protege.web.shared.termbuilder.ConceptList;
import edu.stanford.bmir.protege.web.shared.termbuilder.wordnet.WordNetSearch;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Parse the input sequence using the following pattern:
 * // (Adjective | Noun)* (Noun Preposition)? (Adjective | Noun)* Noun
 * (Adjective | Noun)* Noun
 *
 * @author Yuhao Zhang
 */
public class NounPhraseParser {

    private List<WordAndPOSTag> sequence;
    private List<WordAndPOSTag> clist;
    WordNetSearch search;

    public NounPhraseParser(List<WordAndPOSTag> sequence, WordNetSearch search) {
        this.sequence = sequence;
        clist = new ArrayList<WordAndPOSTag>();
        this.search = search;
    }

    public List<String> parse() {
        List<String> result = new ArrayList<String>();
        for(WordAndPOSTag w : sequence) {
            if(w.isNoun()) {
                clist.add(w);
            } else if(w.isAdj()) {
                clist.add(w);
            } else if(isValidNounPhrase()) {
                addResult(result);
                clist.clear();
            } else {
                clist.clear();
            }
        }
        // Remove the stop concepts
        Iterator<String> r = result.iterator();
        while(r.hasNext()) {
            String s = r.next();
            if(TermBuilderConstant.STOP_CONCEPTS.contains(s.toLowerCase())) {
                r.remove();
            }
        }
        return result;
    }

    private boolean isValidNounPhrase() {
        return clist.size() > 0 && clist.get(clist.size() - 1).isNoun();
    }

    public ConceptList parseToConceptList() {
        List<String> result = parse();
        ConceptList list = new ConceptList(result);
        return list;
    }

    private void addResult(List<String> result) {
        StringBuilder sb = new StringBuilder();
        for(WordAndPOSTag w: clist) {
            String word = search.normalizeConceptNameByWordAndPOSTag(w);
            word = search.convertWordNetOutputToConceptString(word);
            sb.append(word);
            sb.append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
        result.add(sb.toString());
    }

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        WordNetSearch search = null;

        try {
            search = WordNetSearch.getInstance();
            System.err.println("[Server] WordNetSearch Component initialized");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String text = "What is the 2014 FIFA World Cups?";

        Annotation document = new Annotation(text);
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for(CoreMap sentence: sentences) {
            List<WordAndPOSTag> sequence = new ArrayList<WordAndPOSTag>();
            for(CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                sequence.add(new WordAndPOSTag(word, pos));
            }
            NounPhraseParser parser = new NounPhraseParser(sequence, search);
            List<String> result = parser.parse();
            for(String s : result) {
                System.out.println(s);
            }
        }
    }

}
