package edu.stanford.bmir.protege.web.client.ui.termbuilder.question;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogValidator;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;

public class CreateCompetencyQuestionsDialogController extends WebProtegeOKCancelDialogController<CreateCompetencyQuestionsInfo> {

	private final CreateCompetencyQuestionsForm form;
	
	protected CreateCompetencyQuestionsDialogController(final CreateCompetencyQuestionsHandler handler) {
		super("Create Competency Questions");
		form = new CreateCompetencyQuestionsForm();
		for(WebProtegeDialogValidator validator : form.getDialogValidators()) {
			//TODO: check out validators here
			addDialogValidator(validator);
		}
		setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<CreateCompetencyQuestionsInfo>() {
			@Override
			public void handleHide(CreateCompetencyQuestionsInfo data, WebProtegeDialogCloser closer) {
				handler.handleCreateQuestions(data);
				closer.hide();
			}
		});
	}
	
	@Override
	public Widget getWidget() {
		return form;
	}
	
	@Override
	public Optional<Focusable> getInitialFocusable() {
        return form.getInitialFocusable();
    }
	
	@Override
	public CreateCompetencyQuestionsInfo getData() {
		return new CreateCompetencyQuestionsInfo(form.getQuestionsBrowserText());
	}
	
	public static interface CreateCompetencyQuestionsHandler {
		void handleCreateQuestions(CreateCompetencyQuestionsInfo createCompetencyQuestionsInfo);
	}
	
}