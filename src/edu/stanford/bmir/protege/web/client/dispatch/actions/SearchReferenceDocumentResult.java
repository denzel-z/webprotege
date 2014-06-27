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

    private ArrayList<ReferenceDocumentInfo> list;

    protected SearchReferenceDocumentResult() {}

    public SearchReferenceDocumentResult(ArrayList<ReferenceDocumentInfo> list) {
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
