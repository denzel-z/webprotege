package edu.stanford.bmir.protege.web.shared.termbuilder.websearch;

import edu.stanford.bmir.protege.web.shared.termbuilder.ReferenceDocumentInfo;

import java.util.List;

/**
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public interface SubSearcher {

    void search() throws Exception;
    List<ReferenceDocumentInfo> getRecommendedDocs();
}
