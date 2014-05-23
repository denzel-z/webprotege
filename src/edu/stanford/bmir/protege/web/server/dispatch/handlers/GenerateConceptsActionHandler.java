package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import edu.stanford.bmir.protege.web.client.dispatch.actions.GenerateConceptsAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GenerateConceptsResult;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.termbuilder.CompetencyQuestionInfo;
import edu.stanford.bmir.protege.web.shared.termbuilder.ConceptList;
import edu.stanford.bmir.protege.web.shared.termbuilder.ConceptSet;
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
	Set<String> validTags;
	WordNetSearch search; //Used to stem and normalize concepts
	
	/**
	 * The constructor for this handler. It will instantiate the
	 * StanfordCoreNLP pipeline and validTags set.
	 */
	public GenerateConceptsActionHandler() {
		System.err.println("[Server] Instantiate StanfordCoreNLP ...");
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos");
		pipeline = new StanfordCoreNLP(props);
		validTags = new HashSet<String>();
		validTags.add("NN");
		validTags.add("NNS");
		validTags.add("NNP");
		validTags.add("NNPS");
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

	/**
	 * For now just use NullValidator to validate every user.
	 * TODO: Add better validation mechanism.
	 */
	@Override
	public RequestValidator<GenerateConceptsAction> getRequestValidator(
			GenerateConceptsAction action, RequestContext requestContext) {
		System.err.println("[Server] Request Validator processed!");
		return new NullValidator<GenerateConceptsAction, GenerateConceptsResult>();
	}

	@Override
	public GenerateConceptsResult execute(GenerateConceptsAction action,
			ExecutionContext executionContext) {
		System.err.println("[Server] Execute Competency Questions...");
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
//		System.err.println("Enter parseConceptsFromSingleQuestion");
//		ConceptSet conceptSet = new ConceptSet();
		ConceptList conceptList = new ConceptList();
		String text = info.getQuestion();
//		System.err.println("Parsing question: " + text);
		Annotation document = new Annotation(text);
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
//		System.err.println("Sentences constructed");
		for(CoreMap sentence: sentences) {
			for(CoreLabel token: sentence.get(TokensAnnotation.class)) {
				String word = token.get(TextAnnotation.class);
				String pos = token.get(PartOfSpeechAnnotation.class);
				if(validTags.contains(pos)) {
					String normalizedWord = search.normalizeConceptNameByString(word);
					normalizedWord = search.convertWordNetOutputToConceptString(normalizedWord);
//					conceptSet.addConcept(normalizedWord);
					conceptList.addConcept(normalizedWord);
				}
			}
		}
//		return new ConceptList(conceptSet);
		return conceptList;
	}
}