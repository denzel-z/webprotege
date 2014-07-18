package edu.stanford.bmir.protege.web.client.ui.termbuilder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author denzel
 */
public class TermBuilderConstant {
    static public final String OWLTHING_NAME = "owl:Thing";

    static public final Set<String> STOP_CONCEPTS = new HashSet<String>(Arrays.asList(
            "kind", "kinds", "different kind", "different kinds", "many kind", "many kinds"
    ));

}
