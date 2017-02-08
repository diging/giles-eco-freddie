package edu.asu.diging.gilesecosystem.freddie.core.repository;

import java.util.List;

import org.springframework.data.solr.repository.SolrCrudRepository;

import edu.asu.diging.gilesecosystem.freddie.core.model.impl.Document;

public interface DocumentRepository extends SolrCrudRepository<Document, String> {

    public List<Document> findByContent(String searchTerm);
}
