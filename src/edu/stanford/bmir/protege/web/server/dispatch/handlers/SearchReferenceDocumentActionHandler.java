package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.SearchReferenceDocumentAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.SearchReferenceDocumentResult;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.shared.termbuilder.bingsearch.BingDocumentSearch;

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
        BingDocumentSearch search = new BingDocumentSearch(action.getConceptName());
        try {
            search.searchForDocuments();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SearchReferenceDocumentResult result = new SearchReferenceDocumentResult(search.recommendedDocuments);
        return result;
    }
}
