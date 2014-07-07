package edu.stanford.bmir.protege.web.shared.termbuilder;

/**
 * The TermBuilder specific String utilities.
 *
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class TBStringUtils {

    static public boolean isAllUpper(String s) {
        for(char c : s.toCharArray()) {
            if(!Character.isUpperCase(c)) {
                return false;
            }
        }
        return true;
    }
}
