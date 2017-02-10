package edu.asu.diging.gilesecosystem.freddie.core.repository.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient.RemoteSolrException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.UncategorizedSolrException;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.stereotype.Component;

import edu.asu.diging.gilesecosystem.freddie.core.exception.SearchQueryException;
import edu.asu.diging.gilesecosystem.freddie.core.model.impl.Document;
import edu.asu.diging.gilesecosystem.freddie.core.repository.CustomSeachRepository;

@Component
public class CustomSearchRepositoryImpl implements CustomSeachRepository {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SolrTemplate solrTemplate;
    
    @Override
    public List<Document> searchWithNativeString(String query) throws SearchQueryException {
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
        HighlightQuery simpleQuery = new SimpleHighlightQuery(new SimpleStringCriteria(sb.toString()));
        try {
            HighlightPage<Document> page = solrTemplate.queryForHighlightPage(simpleQuery, Document.class);
            return page.getContent();
        } catch (RemoteSolrException | UncategorizedSolrException ex) {
            logger.warn("Could not execute query.");
            throw new SearchQueryException(ex);
        }
    }

}
