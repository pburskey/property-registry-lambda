package com.burskey.property.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.burskey.property.domain.Property;

import java.io.IOException;

public class SNSEventHandler extends AbstractLambda {


    public void handler(SNSEvent event, Context context) {
        event.getRecords().forEach(record -> {
            this.process(event, record, context);
        });
    }

    private void process(SNSEvent event, SNSEvent.SNSRecord record, Context context) {

        try {
            final Property property = this.mapper.readValue(record.getSNS().getMessage(), Property.class);

            this.dynamo.save(property);
            context.getLogger().log("Saved: " + property.getName() + " id: " + property.getCategory());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
