package edu.asu.diging.gilesecosystem.freddie.core.model.impl;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.asu.diging.gilesecosystem.freddie.core.model.IDocument;

@SolrDocument(solrCoreName="documents")
public class Document implements IDocument {

    @Id
    @Indexed
    private String id;
    
    @Field("stored_file_id_s")
    @Indexed(searchable=false, stored=true)
    private String storedFileId;
    
    @Indexed(searchable=false, stored=true)
    @Field("document_id_s")
    private String documentId;
    
    @Indexed
    @Field("username_s")
    private String username;
    
    @JsonIgnore
    @Indexed(name="content", type="string")
    @Field("content_txt")
    private String content;

    /* (non-Javadoc)
     * @see edu.asu.diging.gilesecosystem.freddie.core.model.impl.IDocument#getFileId()
     */
    @Override
    public String getFileId() {
        return id;
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.gilesecosystem.freddie.core.model.impl.IDocument#setFileId(java.lang.String)
     */
    @Override
    public void setFileId(String fileId) {
        this.id = fileId;
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.gilesecosystem.freddie.core.model.impl.IDocument#getStoredFileId()
     */
    @Override
    public String getStoredFileId() {
        return storedFileId;
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.gilesecosystem.freddie.core.model.impl.IDocument#setStoredFileId(java.lang.String)
     */
    @Override
    public void setStoredFileId(String storedFileId) {
        this.storedFileId = storedFileId;
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.gilesecosystem.freddie.core.model.impl.IDocument#getDocumentId()
     */
    @Override
    public String getDocumentId() {
        return documentId;
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.gilesecosystem.freddie.core.model.impl.IDocument#setDocumentId(java.lang.String)
     */
    @Override
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.gilesecosystem.freddie.core.model.impl.IDocument#getContent()
     */
    @Override
    public String getContent() {
        return content;
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.gilesecosystem.freddie.core.model.impl.IDocument#setContent(java.lang.String)
     */
    @Override
    public void setContent(String content) {
        this.content = content;
    }
}
