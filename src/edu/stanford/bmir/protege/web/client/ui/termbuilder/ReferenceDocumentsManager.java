package edu.stanford.bmir.protege.web.client.ui.termbuilder;

import edu.stanford.bmir.protege.web.shared.termbuilder.ReferenceDocumentInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Yuhao Zhang
 */
public class ReferenceDocumentsManager {

    private List<ReferenceDocumentInfo> referenceDocumentInfoList;

    public ReferenceDocumentsManager() {
        referenceDocumentInfoList = new ArrayList<ReferenceDocumentInfo>();
    }

    public void addRecommendedDocuments(Collection<ReferenceDocumentInfo> c) {
        referenceDocumentInfoList.clear();
        referenceDocumentInfoList.addAll(c);
    }

    public List<ReferenceDocumentInfo> getRecommendedDocuments() {
        return Collections.unmodifiableList(referenceDocumentInfoList);
    }

}
