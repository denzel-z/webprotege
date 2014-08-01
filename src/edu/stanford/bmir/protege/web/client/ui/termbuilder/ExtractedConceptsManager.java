package edu.stanford.bmir.protege.web.client.ui.termbuilder;

import edu.stanford.bmir.protege.web.shared.termbuilder.CompetencyQuestionInfo;
import edu.stanford.bmir.protege.web.shared.termbuilder.Concept;
import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class ExtractedConceptsManager {

    private Set<Concept> extractedConceptSet;
    private Map<Concept, Set<Integer>> conceptToQuestionIDMap;

    public ExtractedConceptsManager() {
        extractedConceptSet = new HashSet<Concept>();
        conceptToQuestionIDMap = new HashMap<Concept, Set<Integer>>();
    }

    public void addExtractedConcepts(Collection<Concept> c) {
        extractedConceptSet.addAll(c);
    }

    public void addExtractedConceptsForSingleQuestion(Collection<Concept> c, int questionID) {
        extractedConceptSet.addAll(c);
        for(Concept concept : c) {
            if(conceptToQuestionIDMap.containsKey(concept)) {
                conceptToQuestionIDMap.get(concept).add(questionID);
            } else {
                Set<Integer> qIDSet = new HashSet<Integer>();
                qIDSet.add(questionID);
                conceptToQuestionIDMap.put(concept, qIDSet);
            }
        }
    }

    public void clearExtractedConcepts() {
        extractedConceptSet.clear();
        conceptToQuestionIDMap.clear();
    }

    public void removeExtractedConceptsForSingleQuestion(int questionID) {
        Iterator<Map.Entry<Concept, Set<Integer>>> iter = conceptToQuestionIDMap.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry<Concept, Set<Integer>> entry = iter.next();
            Set<Integer> qIDSet = entry.getValue();
            if(qIDSet.contains(questionID)) {
                qIDSet.remove(questionID);
            }
            if(qIDSet.isEmpty()) {
                extractedConceptSet.remove(entry.getKey());
                iter.remove();
            }
        }
    }

    public void removeExtractedConceptsForQuestions(Collection<CompetencyQuestionInfo> c) {
        for(CompetencyQuestionInfo info : c) {
            removeExtractedConceptsForSingleQuestion(info.getId());
        }
    }

    /**
     * Return a read-only extracted conceptName set. If want to change the conceptSet, please use
     * other methods like addExtractedConcepts().
     * @return
     */
    public Set<Concept> getExtractedConcepts() {
        return Collections.unmodifiableSet(extractedConceptSet);
    }
}
