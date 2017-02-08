package edu.asu.diging.gilesecosystem.freddie.core.service.impl;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.gilesecosystem.freddie.core.model.impl.Document;
import edu.asu.diging.gilesecosystem.freddie.core.repository.DocumentRepository;
import edu.asu.diging.gilesecosystem.requests.FileType;
import edu.asu.diging.gilesecosystem.requests.ICompletedStorageRequest;
import edu.asu.diging.gilesecosystem.util.files.IFileContentUtility;

@Service
public class IndexService {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private DocumentRepository repository;
    
    @Autowired
    private IFileContentUtility fileUtility;

    public void indexDocument(ICompletedStorageRequest request) {
        if (request.getFileType() != FileType.TEXT) {
            // nothing to index
            return;
        }
        
        Document document = new Document();
        document.setDocumentId(request.getDocumentId());
        document.setFileId(request.getFileId());
        document.setStoredFileId(request.getStoredFileId());
        
        try {
            byte[] fileContent = fileUtility.getFileContentFromUrl(new URL(request.getDownloadUrl()));
            document.setContent(new String(fileContent, Charset.forName("utf-8")));
        } catch (IOException e) {
            logger.error("Could not download file.", e);
            // FIXME handle this
            // nothing to index
            return;
        }
        
        repository.save(document);
    }
}
