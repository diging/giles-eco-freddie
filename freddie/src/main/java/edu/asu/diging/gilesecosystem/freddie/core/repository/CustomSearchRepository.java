package edu.asu.diging.gilesecosystem.freddie.core.repository;

import java.util.List;

import edu.asu.diging.gilesecosystem.freddie.core.exception.SearchQueryException;
import edu.asu.diging.gilesecosystem.freddie.core.model.impl.Document;

public interface CustomSearchRepository {

    public List<Document> searchWithNativeString(String query) throws SearchQueryException;
    
    public List<Document> searchWithQueryByUser(String query, String username) throws SearchQueryException;
}
