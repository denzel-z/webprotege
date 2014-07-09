package edu.stanford.bmir.protege.web.shared.termbuilder.websearch.wikipedia;

import com.google.gson.Gson;
import edu.stanford.bmir.protege.web.shared.termbuilder.HttpConnection;
import edu.stanford.bmir.protege.web.shared.termbuilder.ReferenceDocumentInfo;
import edu.stanford.bmir.protege.web.shared.termbuilder.websearch.SubSearcher;
import edu.stanford.bmir.protege.web.shared.termbuilder.websearch.WebSearchSource;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class WikipediaPageSearcher implements SubSearcher, Runnable {

    public String conceptName;
    public String normalizedConceptName;
    public ArrayList<ReferenceDocumentInfo> recommendedDocuments;

    private HttpConnection con;
    private Gson gson;

    public String resultString;

    /*
	 * Some properties to set
	 */
    private int SEARCH_RESULT_LIMIT = 10;
    private String ROOT_URL = "http://en.wikipedia.org/w/api.php?action=opensearch";
    private String DOC_SNIPPET_PREFFIX = "This is a wikipedia page of the concept: ";
    private String PAGE_DISPLAYED_URL_PREFFIX = "en.wikipedia.org/wiki/";
    private String HTTP_PREFFIX = "http://";
    private String DOC_TITLE_SUFFIX = " - Wikipedia, the free encyclopedia";

    public WikipediaPageSearcher(String conceptName) {
        this.conceptName = conceptName;
        con = new HttpConnection();
        gson = new Gson();
        recommendedDocuments = new ArrayList<ReferenceDocumentInfo>();

        normalizedConceptName = normalizeConceptName(conceptName);
    }

    @Override
    public void search() throws Exception {
        String url = buildQueryURL();
        con.sendGet(url);

        resultString = con.getResponseString();
        if(preprocessResultString() == false) {
            return;
        }

        WikipediaPageSearchResult result = new WikipediaPageSearchResult();
        result.setTitles(gson.fromJson(resultString, String[].class));
        String[] titles = result.getTitles();
        if(titles != null && titles.length > 0) {
            int id = 0;
            for(String title : titles) {
                ReferenceDocumentInfo info = new ReferenceDocumentInfo();
                info.setId(id);
                info.setDocTitle(title + DOC_TITLE_SUFFIX);
                info.setDocSnippet(generateDocSnippet(title));
                info.setDocDisplayedURL(generateDocDisplayedURL(title));
                info.setDocURL(HTTP_PREFFIX + info.getDocDisplayedURL());
                info.setSource(WebSearchSource.WIKIPEDIA_SEARCH);
                recommendedDocuments.add(info);
            }
        }
    }

    @Override
    public List<ReferenceDocumentInfo> getRecommendedDocs() {
        return recommendedDocuments;
    }

    private boolean preprocessResultString() {
        int first = resultString.lastIndexOf("[");
        int second = resultString.indexOf("]");
        if(first == -1 || second == -1) {
            return false;
        }
        resultString = resultString.substring(first, second + 1);
        return true;
    }

    private String generateDocDisplayedURL(String title) throws UnsupportedEncodingException {
        String url = title.replace(" ", "_");
        url = URLEncoder.encode(url, "UTF-8");
        return PAGE_DISPLAYED_URL_PREFFIX + url;
    }

    private String generateDocSnippet(String title) {
        return DOC_SNIPPET_PREFFIX + title;
    }

    private String buildQueryURL() throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder(ROOT_URL);
        // add query terms
        sb.append("&search=");
        String escapedQueryString = "";
        // Use URLEncoder to support multiple word search
        escapedQueryString = URLEncoder.encode(buildFullQuery(), "UTF-8");
        sb.append(escapedQueryString);
        sb.append("&limit=" + SEARCH_RESULT_LIMIT);
        sb.append("&namespace=0");
        sb.append("&format=json");
        return sb.toString();
    }

    private String buildFullQuery() {
        return normalizedConceptName;
    }

    private String normalizeConceptName(String conceptName) {
        //TODO: need some better normalization mechanism
        String normalizedConceptName = conceptName.toLowerCase();
        return normalizedConceptName;
    }

    public static void main(String[] args) throws Exception {
        WikipediaPageSearcher searcher = new WikipediaPageSearcher("hello kitty");
        searcher.search();
        for(ReferenceDocumentInfo info : searcher.recommendedDocuments) {
            System.out.println("TITLE: " + info.getDocTitle());
            System.out.println("URL: " + info.getDocURL());
            System.out.println("DISPLAYED URL: " + info.getDocDisplayedURL());
            System.out.println("-------------------------------");
        }
    }

    @Override
    public void run() {
        try {
            search();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
