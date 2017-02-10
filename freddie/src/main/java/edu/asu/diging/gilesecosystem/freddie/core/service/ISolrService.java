package edu.asu.diging.gilesecosystem.freddie.core.service;

import java.util.List;

import edu.asu.diging.gilesecosystem.freddie.core.exception.SearchQueryException;
import edu.asu.diging.gilesecosystem.freddie.core.model.IDocument;
import edu.asu.diging.gilesecosystem.requests.ICompletedStorageRequest;

public interface ISolrService {

    public abstract void indexDocument(ICompletedStorageRequest request);

    public abstract List<IDocument> find(String query) throws SearchQueryException;

}