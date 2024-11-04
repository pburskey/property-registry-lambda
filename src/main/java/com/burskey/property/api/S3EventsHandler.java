package com.burskey.property.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.burskey.property.domain.Property;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class S3EventsHandler extends AbstractLambda{

    private final AmazonSNS sns = AmazonSNSClientBuilder.defaultClient();
    private final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
    private final String snsTopic = System.getenv("FAN_OUT_TOPIC");

    public void handler(S3Event event, Context context){
        event.getRecords().forEach(this::processS3EventRecord);
    }

    private void processS3EventRecord(S3EventNotification.S3EventNotificationRecord aRecord) {
        final List<Property> events = readEventsFromS3(aRecord.getS3().getBucket().getName(), aRecord.getS3().getObject().getKey());
        events.stream().map(
                this::eventToSNSMessage)
                .forEach(message -> this.sns.publish(snsTopic, message));
    }


    private List<Property> readEventsFromS3(String bucket, String key){
        try{
            final S3ObjectInputStream s3is = this.s3.getObject(new GetObjectRequest(bucket, key)).getObjectContent();
            final Property[] events = this.mapper.readValue(s3is, Property[].class);
            s3is.close();
            return Arrays.asList(events);
        }catch(IOException ex){
            throw new RuntimeException(ex);
        }
    }

    private String eventToSNSMessage(Property event){
        try{
            return this.mapper.writeValueAsString(event);
        }catch (JsonProcessingException ex){
            throw new RuntimeException(ex);
        }
    }



}
