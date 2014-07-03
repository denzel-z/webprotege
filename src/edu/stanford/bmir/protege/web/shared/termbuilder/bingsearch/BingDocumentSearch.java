package edu.stanford.bmir.protege.web.shared.termbuilder.bingsearch;

import com.google.gson.Gson;
import edu.stanford.bmir.protege.web.shared.termbuilder.Concept;
import edu.stanford.bmir.protege.web.shared.termbuilder.ReferenceDocumentInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author denzel
 */
public class BingDocumentSearch {

    public String conceptName;
    public String normalizedConceptName;
    public ArrayList<ReferenceDocumentInfo> recommendedDocuments;

    private BingSearchConnection con;
    private Gson gson;

    public String resultString;

    /*
	 * Some properties to set
	 */
    private int SEARCH_RESULT_LIMIT = 10;
    private String ROOT_URL = "https://api.datamarket.azure.com/Bing/Search/Web";
    private String VALID_RESULT_PREFIX = "{\"d\":";


    public BingDocumentSearch(String conceptName) {
        this.conceptName = conceptName;
        con = new BingSearchConnection();
        gson = new Gson();
        recommendedDocuments = new ArrayList<ReferenceDocumentInfo>();

        this.normalizedConceptName = normalizeConceptName(conceptName);

    }

    public void searchForDocuments() throws Exception {
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
                recommendedDocuments.add(info);
            }
        }
    }

    public String buildQueryURL() {
        StringBuilder sb = new StringBuilder(ROOT_URL);
        // add full query terms
        sb.append("?Query=%27");
        String escapedQueryString = "";
        // Use URLEncoder to support multiple word search
        try {
            escapedQueryString = URLEncoder.encode(buildFullQuery(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        sb.append(escapedQueryString);
        sb.append("%27");
        // add other query settings
        sb.append("&$top=" + SEARCH_RESULT_LIMIT);
        sb.append("&$format=JSON");
        return sb.toString();
    }

    public String buildFullQuery() {
        return normalizedConceptName + " wikipedia";
    }

    public boolean preprocessResultString() {
        if(!resultString.substring(0,5).equals(VALID_RESULT_PREFIX)) {
            return false;
        }
        resultString = resultString.substring(5, resultString.length() - 1);
        return true;
    }

    private String normalizeConceptName(String conceptName) {
        //TODO: need some better normalization mechanism
        String normalizedConceptName = conceptName.toLowerCase();
        return normalizedConceptName;
    }

    public static void main(String[] args) throws Exception {
        BingDocumentSearch s = new BingDocumentSearch("west movie");
//        s.searchForDocuments();

        System.out.println(s.buildQueryURL());
//        System.out.println(s.recommendedDocuments.size());
//        System.out.println(s.recommendedDocuments.get(0).getDocTitle());
    }

}
