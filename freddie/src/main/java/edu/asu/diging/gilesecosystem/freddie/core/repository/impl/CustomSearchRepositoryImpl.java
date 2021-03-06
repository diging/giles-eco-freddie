package edu.asu.diging.gilesecosystem.freddie.core.repository.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.solr.client.solrj.impl.HttpSolrClient.RemoteSolrException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.UncategorizedSolrException;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.stereotype.Component;

import edu.asu.diging.gilesecosystem.freddie.core.Properties;
import edu.asu.diging.gilesecosystem.freddie.core.exception.SearchQueryException;
import edu.asu.diging.gilesecosystem.freddie.core.model.impl.Document;
import edu.asu.diging.gilesecosystem.freddie.core.repository.CustomSearchRepository;
import edu.asu.diging.gilesecosystem.util.properties.IPropertiesManager;

@Component
public class CustomSearchRepositoryImpl implements CustomSearchRepository {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SolrTemplate solrTemplate;
    
    @Autowired
    private IPropertiesManager propertiesManager;
    
    @Override
    public List<Document> searchWithNativeString(String query) throws SearchQueryException {
       
        String solrQuery = buildContentSolrQuery(query);
        
        HighlightQuery simpleQuery = new SimpleHighlightQuery(new SimpleStringCriteria(solrQuery));
        HighlightOptions options = new HighlightOptions();
        options.setNrSnipplets(new Integer(propertiesManager.getProperty(Properties.NR_OF_HIGHLIGHTED_SNIPPETS)));
        simpleQuery.setHighlightOptions(options);
        try {
            HighlightPage<Document> page = solrTemplate.queryForHighlightPage(simpleQuery, Document.class);
            return addSnippets(page);
        } catch (RemoteSolrException | UncategorizedSolrException ex) {
            logger.warn("Could not execute query.");
            throw new SearchQueryException(ex);
        }
    }
    
    public List<Document> searchWithQueryByUser(String query, String username) throws SearchQueryException {
        String solrQuery = buildContentSolrQuery(query);
        
        solrQuery += " AND username_s:" + username;
        
        HighlightQuery simpleQuery = new SimpleHighlightQuery(new SimpleStringCriteria(solrQuery));
        HighlightOptions options = new HighlightOptions();
        options.setNrSnipplets(new Integer(propertiesManager.getProperty(Properties.NR_OF_HIGHLIGHTED_SNIPPETS)));
        simpleQuery.setHighlightOptions(options);
        try {
            HighlightPage<Document> page = solrTemplate.queryForHighlightPage(simpleQuery, Document.class);
            return addSnippets(page);
        } catch (RemoteSolrException | UncategorizedSolrException ex) {
            logger.warn("Could not execute query.");
            throw new SearchQueryException(ex);
        }
    }

    private String buildContentSolrQuery(String query) {
        query = query.toLowerCase();
        
        String quotePattern = "\".*?\"";
        
        List<String> quoted = new ArrayList<>();
        Matcher matcher = Pattern.compile(quotePattern).matcher(query);
        while(matcher.find()) {
            String q = matcher.group();
            if (!q.isEmpty()) {
                quoted.add(q);
            }
        }
        
        String[] notQuoted = query.split(quotePattern);
        List<String> singleTerms = new ArrayList<>();
        
        for (String phrase : notQuoted) {
            singleTerms.addAll(Arrays.asList(phrase.split(" ")));
        }
        StringBuffer sb = new StringBuffer();
        sb.append("content_txt:");
        sb.append("(");
        for (String term : singleTerms) {
            if (!term.startsWith("-")) {
                sb.append(" +");
            }
            sb.append(term);
        }
        for (String qt : quoted) {
            sb.append(" +");
            sb.append(qt);
        }
        sb.append(")");
        return sb.toString();
    }
    
    private List<Document> addSnippets(HighlightPage<Document> page) {
        List<Document> docs = page.getContent();
        for (Document doc : docs) {
            List<Highlight> highlights = page.getHighlights(doc);
            for (Highlight h : highlights) {
                if (doc.getHighlightedSnippets() ==  null) {
                    doc.setHighlightedSnippets(new ArrayList<>());
                }
                doc.getHighlightedSnippets().addAll(h.getSnipplets());
            }
        }
        return docs;
    }
}
