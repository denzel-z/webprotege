package edu.stanford.bmir.protege.web.shared.termbuilder.questionparsing;

/**
 * @author Yuhao Zhang
 */
public class WordAndPOSTag {
    private String word;
    private String POS;

    public WordAndPOSTag(String word, String POS) {
        this.word = word;
        this.POS = POS;
    }

    public String getWord() {
        return word;
    }

    public String getPOS() {
        return POS;
    }

    public boolean isAdj() {
        return QuestionParsingConstant.adjTags.contains(POS);
    }

    public boolean isNoun() {
        return QuestionParsingConstant.nounTags.contains(POS);
    }

    public boolean isPrep() {
        return QuestionParsingConstant.prepTags.contains(POS);
    }
}
