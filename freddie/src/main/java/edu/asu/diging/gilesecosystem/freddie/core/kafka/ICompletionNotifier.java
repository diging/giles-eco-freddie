package edu.asu.diging.gilesecosystem.freddie.core.kafka;

import edu.asu.diging.gilesecosystem.requests.ICompletedStorageRequest;

public interface ICompletionNotifier {

    public abstract void sendNotification(ICompletedStorageRequest request);

}