package edu.stanford.bmir.protege.web.shared.termbuilder.websearch;

import edu.stanford.bmir.protege.web.shared.termbuilder.ReferenceDocumentInfo;

import java.util.List;

/**
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public interface SearchStrategy {

    void addIntermediateResult(List<ReferenceDocumentInfo> docs);
    void integrate();
    List<ReferenceDocumentInfo> getResult();

}
