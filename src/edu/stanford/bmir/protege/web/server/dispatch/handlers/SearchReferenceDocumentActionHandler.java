package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.SearchReferenceDocumentAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.SearchReferenceDocumentResult;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.shared.termbuilder.websearch.ReferenceDocSearcher;
import edu.stanford.bmir.protege.web.shared.termbuilder.websearch.bing.BingDocumentSearcher;

/**
 * @author Yuhao Zhang
 */
public class SearchReferenceDocumentActionHandler implements
        ActionHandler<SearchReferenceDocumentAction, SearchReferenceDocumentResult> {

    @Override
    public Class<SearchReferenceDocumentAction> getActionClass() {
        return SearchReferenceDocumentAction.class;
    }

    @Override
    public RequestValidator<SearchReferenceDocumentAction> getRequestValidator(SearchReferenceDocumentAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    @Override
    public SearchReferenceDocumentResult execute(SearchReferenceDocumentAction action, ExecutionContext executionContext) {
        ReferenceDocSearcher s = new ReferenceDocSearcher(action.getConceptName());
        try {
            s.search();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SearchReferenceDocumentResult result = new SearchReferenceDocumentResult(s.getDocs());
        return result;
    }
}
