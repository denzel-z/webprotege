package edu.stanford.bmir.protege.web.client.ui.termbuilder;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.regexp.shared.SplitResult;
import com.google.gwt.user.client.ui.Focusable;

import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogForm;
import edu.stanford.bmir.protege.web.client.ui.library.text.ExpandingTextBox;
import edu.stanford.bmir.protege.web.client.ui.library.text.ExpandingTextBoxMode;

/**
 * The WebProtegeDialogForm that used to create a dialog for creating 
 * competency questions.
 * 
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class CreateCompetencyQuestionsForm extends WebProtegeDialogForm {
	
	protected ExpandingTextBox questionBrowserTextField;
	
	private static ExpandingTextBoxMode mode = ExpandingTextBoxMode.MULTI_LINE;
	
	public CreateCompetencyQuestionsForm() {
		questionBrowserTextField = new ExpandingTextBox();
		questionBrowserTextField.setWidth("450px");
		questionBrowserTextField.setMode(mode);
		questionBrowserTextField.setAnchorVisible(false);
		questionBrowserTextField.setPlaceholder("Enter one competency question per line (press CTRL+ENTER to accept and close dialog)");
		addWidget("Question", questionBrowserTextField);
	}
	
	public List<String> getQuestionsBrowserText() {
		String enteredText = questionBrowserTextField.getText().trim();
		List<String> questions = new ArrayList<String>();
		RegExp regExp = RegExp.compile("\n");
		SplitResult split = regExp.split(enteredText);
		for(int i = 0; i < split.length(); i++) {
			questions.add(split.get(i));
		}
		return questions;
	}
	
	public Optional<Focusable> getInitialFocusable() {
        return Optional.<Focusable>of(questionBrowserTextField);
    }
}