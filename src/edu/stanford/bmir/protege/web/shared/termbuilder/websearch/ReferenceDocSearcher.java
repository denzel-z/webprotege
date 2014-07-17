package edu.stanford.bmir.protege.web.shared.termbuilder.websearch;

import edu.stanford.bmir.protege.web.shared.termbuilder.ReferenceDocumentInfo;
import edu.stanford.bmir.protege.web.shared.termbuilder.websearch.bing.BingCategoryDocumentSearcher;
import edu.stanford.bmir.protege.web.shared.termbuilder.websearch.bing.BingDocumentSearcher;
import edu.stanford.bmir.protege.web.shared.termbuilder.websearch.wikipedia.WikipediaPageSearcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class ReferenceDocSearcher {

    public String conceptName;
    public Properties prop;

    private List<ReferenceDocumentInfo> docs;
    private List<SubSearcher> searchers;
    private SearchStrategy strategy;

    private static final int DEFAULT_MAX_NUM_RESULT = 15;

    public static final String SEARCHERS_PROPERTY_KEY = "searchers";
    public static final String STRATEGY_PROPERTY_KEY = "strategy";

    public static final String SEARCHERS_PROPERTY_BING = "bing";
    public static final String SEARCHERS_PROPERTY_BING_CATEGORY = "category";
    public static final String SEARCHERS_PROPERTY_WIKIPEDIA = "wikipedia";

    public static final String STRATEGY_PROPERTY_MIX = "mix";

    public ReferenceDocSearcher(String conceptName, Properties prop) throws InvalidPropertyException {
        if(prop.getProperty(SEARCHERS_PROPERTY_KEY) == null || prop.getProperty(STRATEGY_PROPERTY_KEY) == null) {
            throw new InvalidPropertyException("Properties argument is not valid!");
        };
        this.prop = prop;
        this.conceptName = conceptName;
        searchers = new ArrayList<SubSearcher>();
    }

    public ReferenceDocSearcher(String conceptName) {
        prop = new Properties();
        prop.setProperty(SEARCHERS_PROPERTY_KEY, SEARCHERS_PROPERTY_BING_CATEGORY + "," + SEARCHERS_PROPERTY_BING + "," + SEARCHERS_PROPERTY_WIKIPEDIA);
        prop.setProperty(STRATEGY_PROPERTY_KEY, STRATEGY_PROPERTY_MIX);
        this.conceptName = conceptName;
        searchers = new ArrayList<SubSearcher>();
    }

    public List<ReferenceDocumentInfo> getDocs() {
        return docs;
    }

    public void search() throws Exception {
        loadSearchers();
        loadStrategy();
        // Use a ThreadPool to accelerate the search api calling process
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for(SubSearcher s : searchers) {
            executor.execute((Runnable) s);
        }
        executor.shutdown();
        while(!executor.isTerminated()) {}
        // Add the search results into the integrated list
        for(SubSearcher s : searchers) {
            strategy.addIntermediateResult(s.getRecommendedDocs());
        }
        strategy.integrate();
        docs = strategy.getResult();
    }

    private void loadSearchers() {
        String searchersProp = prop.getProperty(SEARCHERS_PROPERTY_KEY);
        if(searchersProp.contains(SEARCHERS_PROPERTY_BING)) {
            searchers.add(new BingDocumentSearcher(conceptName));
        }
        if(searchersProp.contains(SEARCHERS_PROPERTY_WIKIPEDIA)) {
            searchers.add(new WikipediaPageSearcher(conceptName));
        }
        if(searchersProp.contains(SEARCHERS_PROPERTY_BING_CATEGORY)) {
            searchers.add(new BingCategoryDocumentSearcher(conceptName));
        }
    }

    private void loadStrategy() {
        String strategyProp = prop.getProperty(STRATEGY_PROPERTY_KEY);
        if(strategyProp.contains(STRATEGY_PROPERTY_MIX)) {
            strategy = new MixSearchStrategy(DEFAULT_MAX_NUM_RESULT);
        }
    }

    public static void main(String[] args) throws Exception {
        ReferenceDocSearcher s = new ReferenceDocSearcher("Minibus");
        long begin = System.currentTimeMillis();
        s.search();
        long end = System.currentTimeMillis();
        System.out.println((end - begin));
        int count = 0;
        for(ReferenceDocumentInfo info : s.getDocs()) {
            count++;
            System.out.println("------------------");
            System.out.println(count);
            System.out.println(info.getSource().toString());
            System.out.println(info.getDocTitle());
            System.out.println(info.getDocURL());
        }
    }

}
