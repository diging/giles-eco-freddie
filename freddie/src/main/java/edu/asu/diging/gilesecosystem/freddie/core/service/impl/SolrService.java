package edu.asu.diging.gilesecosystem.freddie.core.service.impl;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.gilesecosystem.freddie.core.exception.SearchQueryException;
import edu.asu.diging.gilesecosystem.freddie.core.kafka.ICompletionNotifier;
import edu.asu.diging.gilesecosystem.freddie.core.model.IDocument;
import edu.asu.diging.gilesecosystem.freddie.core.model.impl.Document;
import edu.asu.diging.gilesecosystem.freddie.core.repository.CustomSearchRepository;
import edu.asu.diging.gilesecosystem.freddie.core.repository.DocumentRepository;
import edu.asu.diging.gilesecosystem.freddie.core.service.ISolrService;
import edu.asu.diging.gilesecosystem.requests.FileType;
import edu.asu.diging.gilesecosystem.requests.ICompletedStorageRequest;
import edu.asu.diging.gilesecosystem.util.files.IFileContentUtility;

@Service
public class SolrService implements ISolrService {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private CustomSearchRepository repository;
    
    @Autowired
    private DocumentRepository crudRepo;
    
    @Autowired
    private IFileContentUtility fileUtility;
    
    @Autowired
    private ICompletionNotifier notifier;

    /* (non-Javadoc)
     * @see edu.asu.diging.gilesecosystem.freddie.core.service.impl.IIndexService#indexDocument(edu.asu.diging.gilesecosystem.requests.ICompletedStorageRequest)
     */
    @Override
    public void indexDocument(ICompletedStorageRequest request) {
        if (request.getFileType() != FileType.TEXT) {
            // nothing to index
            return;
        }
        
        Document document = new Document();
        document.setDocumentId(request.getDocumentId());
        document.setFileId(request.getFileId());
        document.setStoredFileId(request.getStoredFileId());
        document.setUsername(request.getUsername());
        
        try {
            byte[] fileContent = fileUtility.getFileContentFromUrl(new URL(request.getDownloadUrl()));
            document.setContent(new String(fileContent, Charset.forName("utf-8")));
        } catch (IOException e) {
            logger.error("Could not download file.", e);
            // FIXME handle this
            // nothing to index
            return;
        }
        
        IDocument savedDoc = crudRepo.save(document);
        if (savedDoc != null) {
            notifier.sendNotification(request);
        }
    }
    
    /* (non-Javadoc)
     * @see edu.asu.diging.gilesecosystem.freddie.core.service.impl.IIndexService#find(java.lang.String)
     */
    @Override
    public List<IDocument> find(String query) throws SearchQueryException {
        List<Document> docs = repository.searchWithNativeString(query);
        return docs.stream().map(d -> (IDocument) d).collect(Collectors.toList());
    }
    
    public List<IDocument> find(String query, String username) throws SearchQueryException {
        List<Document> docs = repository.searchWithQueryByUser(query, username);
        return docs.stream().map(d -> (IDocument) d).collect(Collectors.toList());
    }
}
