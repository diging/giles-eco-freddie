package edu.asu.diging.gilesecosystem.freddie.core.model;

public interface IDocument {

    public abstract String getFileId();

    public abstract void setFileId(String fileId);

    public abstract String getStoredFileId();

    public abstract void setStoredFileId(String storedFileId);

    public abstract String getDocumentId();

    public abstract void setDocumentId(String documentId);

    public abstract String getContent();

    public abstract void setContent(String content);

}