package edu.stanford.bmir.protege.web.client.ui.termbuilder;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is used to store the compatency data parsed from the
 * user input in the CreateCompetencyQuestionsDialog.
 * 
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class CreateCompetencyQuestionsInfo {
	
	private Set<String> browserTexts;
	
	public CreateCompetencyQuestionsInfo(List<String> browserTexts) {
		this.browserTexts = new LinkedHashSet<String>(browserTexts);
	}
	
	public Set<String> getBrowserTexts() {
		return new LinkedHashSet<String>(browserTexts);
	}
	
	@Override
	public int hashCode() {
		return "CreateCompetencyQuestionsInfo".hashCode() + browserTexts.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
            return true;
        }
        if(!(obj instanceof CreateCompetencyQuestionsInfo)) {
            return false;
        }
        CreateCompetencyQuestionsInfo other = (CreateCompetencyQuestionsInfo) obj;
        return this.browserTexts.equals(other.browserTexts);
	}
	
}
