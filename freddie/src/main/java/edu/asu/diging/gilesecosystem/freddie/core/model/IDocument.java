package edu.asu.diging.gilesecosystem.freddie.core.model;

import java.util.List;

public interface IDocument {

    public abstract String getFileId();

    public abstract void setFileId(String fileId);

    public abstract String getStoredFileId();

    public abstract void setStoredFileId(String storedFileId);

    public abstract String getDocumentId();

    public abstract void setDocumentId(String documentId);

    public abstract String getContent();

    public abstract void setContent(String content);

    public abstract void setUsername(String username);

    public abstract String getUsername();

    void setHighlightedSnippets(List<String> highlightedSnippets);

    List<String> getHighlightedSnippets();

}