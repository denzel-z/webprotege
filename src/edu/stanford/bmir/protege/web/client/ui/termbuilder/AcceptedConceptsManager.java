package edu.stanford.bmir.protege.web.client.ui.termbuilder;

import edu.stanford.bmir.protege.web.shared.termbuilder.Concept;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Yuhao Zhang
 */
public class AcceptedConceptsManager {

    private Set<Concept> acceptedConceptSet;

    public AcceptedConceptsManager() {
        acceptedConceptSet = new HashSet<Concept>();
    }

    public void addAcceptedConcepts(Collection<Concept> c) {
        acceptedConceptSet.addAll(c);
    }

    public void addAcceptedConceptsFromString(Collection<String> c) {
        for(String s: c) {
            acceptedConceptSet.add(new Concept(s));
        }
    }

    /**
     * Return a readonly accepted conceptName set. If want to change the acceptedConceptSet, please use
     * other methods like addAcceptedConcepts().
     * @return
     */
    public Set<Concept> getAcceptedConcepts() {
        return Collections.unmodifiableSet(acceptedConceptSet);
    }
}
