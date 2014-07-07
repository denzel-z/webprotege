package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.stanford.bmir.protege.web.client.dispatch.actions.GenerateConceptsAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GenerateConceptsResult;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.shared.termbuilder.CompetencyQuestionInfo;
import edu.stanford.bmir.protege.web.shared.termbuilder.Concept;
import edu.stanford.bmir.protege.web.shared.termbuilder.ConceptList;
import edu.stanford.bmir.protege.web.shared.termbuilder.questionparsing.NounPhraseParser;
import edu.stanford.bmir.protege.web.shared.termbuilder.questionparsing.QuestionParsingConstant;
import edu.stanford.bmir.protege.web.shared.termbuilder.questionparsing.WordAndPOSTag;
import edu.stanford.bmir.protege.web.shared.termbuilder.wordnet.WordNetSearch;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class GenerateConceptsActionHandler implements 
	ActionHandler<GenerateConceptsAction, GenerateConceptsResult> {
	
	StanfordCoreNLP pipeline;
	WordNetSearch search; //Used to stem and normalize concepts
	
	/**
	 * The constructor for this handler. It will instantiate the
	 * StanfordCoreNLP pipeline and validTags set.
	 */
	public GenerateConceptsActionHandler() {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos");
		pipeline = new StanfordCoreNLP(props);

		System.err.println("[Server] StanfordCoreNLP initialized");
		
		try {
			search = WordNetSearch.getInstance();
			System.err.println("[Server] WordNetSearch Component initialized");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Class<GenerateConceptsAction> getActionClass() {
		return GenerateConceptsAction.class;
	}

	@Override
	public RequestValidator<GenerateConceptsAction> getRequestValidator(
			GenerateConceptsAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
	}

	@Override
	public GenerateConceptsResult execute(GenerateConceptsAction action,
			ExecutionContext executionContext) {
		List<CompetencyQuestionInfo> questions = action.getQuestions();
		
		Map<CompetencyQuestionInfo, ConceptList> map = 
				new HashMap<CompetencyQuestionInfo, ConceptList>();
		for(CompetencyQuestionInfo q: questions) {
			ConceptList conceptList = parseConceptsFromSingleQuestion(q);
			map.put(q, conceptList);
		}
		GenerateConceptsResult result = new GenerateConceptsResult(map);
		return result;
	}
	
	private ConceptList parseConceptsFromSingleQuestion(CompetencyQuestionInfo info) {
		String text = info.getQuestion();
		Annotation document = new Annotation(text);
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
//		ConceptList conceptList = tagBasedConceptParsing(sentences);
        ConceptList conceptList = patternBasedConceptParsing(sentences);
		return conceptList;
	}

    private ConceptList tagBasedConceptParsing(List<CoreMap> sentences) {
        ConceptList conceptList = new ConceptList();
        for(CoreMap sentence: sentences) {
            for(CoreLabel token: sentence.get(TokensAnnotation.class)) {
                String word = token.get(TextAnnotation.class);
                String pos = token.get(PartOfSpeechAnnotation.class);
                if(QuestionParsingConstant.validTags.contains(pos)) {
                    String normalizedWord = search.normalizeConceptNameByString(word);
                    normalizedWord = search.convertWordNetOutputToConceptString(normalizedWord);
                    conceptList.addConcept(normalizedWord);
                }
            }
        }
        return conceptList;
    }

    private ConceptList patternBasedConceptParsing(List<CoreMap> sentences) {
        ConceptList conceptList = new ConceptList();
        for(CoreMap sentence: sentences) {
            List<WordAndPOSTag> sequence = new ArrayList<WordAndPOSTag>();
            for(CoreLabel token: sentence.get(TokensAnnotation.class)) {
                String word = token.get(TextAnnotation.class);
                String pos = token.get(PartOfSpeechAnnotation.class);
                sequence.add(new WordAndPOSTag(word, pos));
            }
//            List<String> conceptStrings = parseConceptFromSequence(sequence);
            ConceptList concepts = parseConceptFromSequence(sequence);
            conceptList.addAll(concepts);
        }
        return conceptList;
    }

    private ConceptList convertStringListToFormalConcepts(List<String> conceptStrings) {
        return null;
    }

    private ConceptList parseConceptFromSequence(List<WordAndPOSTag> sequence) {
        NounPhraseParser parser = new NounPhraseParser(sequence, search);
        return parser.parseToConceptList();
    }

}