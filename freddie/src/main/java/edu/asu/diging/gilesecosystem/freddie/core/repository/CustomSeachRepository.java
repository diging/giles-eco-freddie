package edu.asu.diging.gilesecosystem.freddie.core.repository;

import java.util.List;

import edu.asu.diging.gilesecosystem.freddie.core.exception.SearchQueryException;
import edu.asu.diging.gilesecosystem.freddie.core.model.impl.Document;

public interface CustomSeachRepository {

    public List<Document> searchWithNativeString(String query) throws SearchQueryException;
}
