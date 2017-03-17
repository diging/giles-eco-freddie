package edu.asu.diging.gilesecosystem.freddie.core.kafka;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.diging.gilesecosystem.freddie.core.service.ISolrService;
import edu.asu.diging.gilesecosystem.requests.ICompletedStorageRequest;
import edu.asu.diging.gilesecosystem.requests.impl.CompletedStorageRequest;

@PropertySource("classpath:/config.properties")
public class KafkaProcessingListener {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ISolrService indexService;

    @KafkaListener(id="freddie.listener", topics = {"${topic_storage_request_complete}"})
    public void receiveMessage(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        
        ObjectMapper mapper = new ObjectMapper();
        
        ICompletedStorageRequest request = null;
        try {
            request = mapper.readValue(message, CompletedStorageRequest.class);
        } catch (IOException e) {
            logger.error("Could not unmarshall request.", e);
            // FIXME: handle this case
            return;
        }
        
        indexService.indexDocument(request);
    }
}
