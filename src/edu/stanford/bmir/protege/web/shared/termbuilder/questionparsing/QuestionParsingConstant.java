package edu.stanford.bmir.protege.web.shared.termbuilder.questionparsing;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author denzel
 */
public class QuestionParsingConstant {

    static public final Set<String> validTags = new HashSet<String>(Arrays.asList(new String[]{"NN", "NNS", "NNP", "NNPS"}));
    static public final Set<String> nounTags = new HashSet<String>(Arrays.asList(new String[]{"NN", "NNS", "NNP", "NNPS"}));
    static public final Set<String> adjTags = new HashSet<String>(Arrays.asList(new String[]{"JJ", "JJR", "JJS"}));
    static public final Set<String> prepTags = new HashSet<String>(Arrays.asList(new String[]{"IN"}));

}
