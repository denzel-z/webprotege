package edu.stanford.bmir.protege.web.shared.termbuilder.websearch.bing;

import edu.stanford.bmir.protege.web.shared.termbuilder.ReferenceDocumentInfo;
import edu.stanford.bmir.protege.web.shared.termbuilder.websearch.WebSearchSource;

/**
 * @author Yuhao Zhang
 */
public class BingCategoryDocumentSearcher extends BingDocumentSearcher {

    private final String QUERY_EXPANSION = " categories";

    public BingCategoryDocumentSearcher(String conceptName) {
        super(conceptName);
    }

    @Override
    public void search() throws Exception {
        String url = buildQueryURL();
        con.sendGet(url);

        resultString = con.getResponseString();
        if(preprocessResultString() == false) {
            return;
        }
        BingDocumentSearchResult result = gson.fromJson(resultString, BingDocumentSearchResult.class);

        // parse the result and store it into the list
        BingDocumentSearchResultElement[] elements = result.getElements();
        if(elements != null && elements.length > 0) {
            int id = 0;
            for(BingDocumentSearchResultElement e : result.getElements()) {
                id++;
                ReferenceDocumentInfo info = new ReferenceDocumentInfo();
                info.setId(id);
                info.setDocTitle(e.getTitle());
                info.setDocSnippet(e.getDescription());
                info.setDocURL(e.getUrl());
                info.setDocDisplayedURL(e.getDisplayUrl());
                info.setSource(WebSearchSource.BING_CATEGORY_SEARCH);
                recommendedDocuments.add(info);
            }
        }
    }

    @Override
    public String buildFullQuery() {
        return normalizedConceptName + QUERY_EXPANSION;
    }
}
