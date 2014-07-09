package edu.stanford.bmir.protege.web.shared.termbuilder.websearch;

import edu.stanford.bmir.protege.web.shared.termbuilder.ReferenceDocumentInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class MixSearchStrategy implements SearchStrategy {

    private List<List<ReferenceDocumentInfo>> intermediateResultList;
    private List<ReferenceDocumentInfo> result;

    public MixSearchStrategy() {
        intermediateResultList = new ArrayList<List<ReferenceDocumentInfo>>();
        result = new ArrayList<ReferenceDocumentInfo>();
    }

    @Override
    public void addIntermediateResult(List<ReferenceDocumentInfo> docs) {
        intermediateResultList.add(docs);
    }

    @Override
    public void integrate() {
        if(intermediateResultList.size() == 0) {
            return;
        } else if(intermediateResultList.size() == 1) {
            result = intermediateResultList.get(0);
        } else {
            // Use round robin strategy to integrate the results from different lists
            Set<String> resultURLSet = new HashSet<String>();
            // Create an iterator list to emit the docs from different intermediate result list
            List<Iterator<ReferenceDocumentInfo>> iterList = new ArrayList<Iterator<ReferenceDocumentInfo>>();
            for(List<ReferenceDocumentInfo> l : intermediateResultList) {
                if (l.size() > 0) {
                    iterList.add(l.iterator());
                }
            }
            while(!iterList.isEmpty()) {
                List<Integer> removeList = new ArrayList<Integer>();
                // iterate through all iterators
                for(int i = 0; i < iterList.size(); i++) {
                    Iterator<ReferenceDocumentInfo> it = iterList.get(i);
                    ReferenceDocumentInfo info = it.next();
                    if(!resultURLSet.contains(info.getDocURL())) {
                        result.add(info);
                        resultURLSet.add(info.getDocURL());
                    }
                    if(!it.hasNext()) {
                        removeList.add(i);
                    }
                }
                // Remove empty elements (iterators)
                for(int i = removeList.size() - 1; i >= 0; i--) {
                    int key = removeList.get(i);
                    iterList.remove(key);
                }
            }
        }
    }

    @Override
    public List<ReferenceDocumentInfo> getResult() {
        return result;
    }
}
