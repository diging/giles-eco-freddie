package edu.asu.diging.gilesecosystem.freddie.core.service;

import java.util.List;

import edu.asu.diging.gilesecosystem.freddie.core.exception.SearchQueryException;
import edu.asu.diging.gilesecosystem.freddie.core.model.IDocument;
import edu.asu.diging.gilesecosystem.requests.ICompletedStorageRequest;

public interface ISolrService {

    public abstract void indexDocument(ICompletedStorageRequest request);

    /**
     * This method searches solr for the given terms in documents owned by the given
     * username. This method will search for documents containing all words in the query
     * or if the query contains quoted phases for documents that contain the exact
     * quoted phases.
     * 
     * @param query query a query such as 'unicorns awesome' or '"unicorns are awesome"'
     * @return list of found documents
     * @throws SearchQueryException if the search query contains characters SOLR can't handle.
     */
    public abstract List<IDocument> find(String query) throws SearchQueryException;

    /**
     * This method searches solr for the given terms in documents owned by the given
     * username. This method will search for documents containing all words in the query
     * or if the query contains quoted phases for documents that contain the exact
     * quoted phases.
     * 
     * @param query a query such as 'unicorns awesome' or '"unicorns are awesome"'
     * @param username The username indexed along with the documents.
     * @return list of found documents
     * @throws SearchQueryException if the search query contains characters SOLR can't handle.
     */
    public List<IDocument> find(String query, String username) throws SearchQueryException;
}