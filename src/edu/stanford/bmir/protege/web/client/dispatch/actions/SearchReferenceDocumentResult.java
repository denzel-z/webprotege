package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.termbuilder.ReferenceDocumentInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Yuhao Zhang
 */
public class SearchReferenceDocumentResult implements Result {

    private List<ReferenceDocumentInfo> list;

    protected SearchReferenceDocumentResult() {}

    public SearchReferenceDocumentResult(List<ReferenceDocumentInfo> list) {
        this.list = list;
    }

    public List<ReferenceDocumentInfo> getReferenceDocuments() {
        return Collections.unmodifiableList(list);
    }

    @Override
    public String toString() {
        return list.toString();
    }

}
