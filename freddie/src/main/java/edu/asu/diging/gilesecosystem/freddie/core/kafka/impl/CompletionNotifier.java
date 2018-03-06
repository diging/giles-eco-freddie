package edu.asu.diging.gilesecosystem.freddie.core.kafka.impl;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import edu.asu.diging.gilesecosystem.freddie.core.Properties;
import edu.asu.diging.gilesecosystem.freddie.core.kafka.ICompletionNotifier;
import edu.asu.diging.gilesecosystem.requests.ICompletedStorageRequest;
import edu.asu.diging.gilesecosystem.requests.ICompletionNotificationRequest;
import edu.asu.diging.gilesecosystem.requests.IRequestFactory;
import edu.asu.diging.gilesecosystem.requests.RequestStatus;
import edu.asu.diging.gilesecosystem.requests.exceptions.MessageCreationException;
import edu.asu.diging.gilesecosystem.requests.impl.CompletionNotificationRequest;
import edu.asu.diging.gilesecosystem.requests.kafka.IRequestProducer;
import edu.asu.diging.gilesecosystem.util.properties.IPropertiesManager;

@PropertySource(value = "classpath:/config.properties")
@Service
public class CompletionNotifier implements ICompletionNotifier {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public final static String REQUEST_PREFIX = "FREDDIE_";

    @Autowired
    private IRequestProducer requestProducer;  
      
    @Autowired
    private IRequestFactory<ICompletionNotificationRequest, CompletionNotificationRequest> requestFactory;
    
    @Value("${topic_completion_notification}")
    private String topic;
    
    @Autowired
    private IPropertiesManager propertiesManager;

    @PostConstruct
    public void init() {
        requestFactory.config(CompletionNotificationRequest.class);
    }
    
    /* (non-Javadoc)
     * @see edu.asu.diging.gilesecosystem.freddie.core.kafka.impl.ICompletionNotifier#sendNotification(edu.asu.diging.gilesecosystem.freddie.core.model.impl.Document)
     */
    @Override
    public void sendNotification(ICompletedStorageRequest request) {
        logger.debug("Sending notification completion request.");
        
        ICompletionNotificationRequest completionRequest = null;
        try {
            completionRequest = requestFactory.createRequest(REQUEST_PREFIX + request.getDocumentId(), request.getUploadId());
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Could not create request.", e);
            // TODO: for now fail silently, but we'll have to send it to September
        }
        
        completionRequest.setStatus(RequestStatus.COMPLETE);
        completionRequest.setNotifier(propertiesManager.getProperty(Properties.NOTIFIER_ID));
        completionRequest.setDocumentId(request.getDocumentId());
        completionRequest.setFileId(request.getFileId());
        
        try {
            requestProducer.sendRequest(completionRequest, topic);
        } catch (MessageCreationException e) {
            logger.error("Could not send request.", e);
            // TODO: for now fail silently, but we'll have to send it to September
        }
    }
}
