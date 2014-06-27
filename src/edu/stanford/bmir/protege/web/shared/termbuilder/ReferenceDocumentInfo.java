package edu.stanford.bmir.protege.web.shared.termbuilder;

import java.io.Serializable;

/**
 * @author Yuhao Zhang
 */
public class ReferenceDocumentInfo implements Serializable {

    int id;

    String docTitle;
    String docSnippet;
    String docURL;
    String docDisplayedURL;

    public ReferenceDocumentInfo() {}

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public String getDocSnippet() {
        return docSnippet;
    }

    public void setDocSnippet(String docSnippet) {
        this.docSnippet = docSnippet;
    }

    public String getDocURL() {
        return docURL;
    }

    public void setDocURL(String docURL) {
        this.docURL = docURL;
    }

    public String getDocDisplayedURL() {
        return docDisplayedURL;
    }

    public void setDocDisplayedURL(String docDisplayedURL) {
        this.docDisplayedURL = docDisplayedURL;
    }

    public String getHyperlinkDocument() {
        StringBuffer sb = new StringBuffer();
        sb.append("<p style=\"margin:0;font-size:13px;\"><a href=\"");
        sb.append(docURL);
        sb.append("\" target=\"_blank\">");
        sb.append(docTitle);
        sb.append("</a></p>");
        sb.append("<p style=\"margin:0;color:#545454;font-size:11px;\">");
        sb.append(docSnippet);
        sb.append("</p>");
        return sb.toString();
    }
}
