package edu.asu.diging.gilesecosystem.freddie.rest;

public class Error {

    private String message;
    private String reason;
    
    public Error(String message, String reason) {
        this.message = message;
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    
}
