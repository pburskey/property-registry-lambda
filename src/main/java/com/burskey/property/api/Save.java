package com.burskey.property.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.burskey.property.dao.Dynamo;
import com.burskey.property.domain.Property;
import com.burskey.property.domain.ValidationException;
import org.apache.http.HttpStatus;

public class Save extends AbstractLambda {


    public Save() {
    }

    public Save(Dynamo dynamo) {
        super(dynamo);
    }

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Event Details:" + event.toString());

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setIsBase64Encoded(false);
        response.setStatusCode(200);

        if (event == null || event.getBody() == null || event.getBody().isEmpty()) {
            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
            response.setBody("Missing payload");
        } else {
            try {
                logger.log("Body: " + event.getBody());
                Property property = mapper.readValue(event.getBody(), Property.class);
                if (property != null && property.isValid()) {
                    this.dynamo.save(property);
//                    response.setBody(property.getId());
                }

            } catch (ValidationException e) {
                logger.log(e.getMessage());
                response.setBody(e.getMessage());
                response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
            } catch (Exception e) {
                logger.log(e.getMessage());
                response.setBody(e.getMessage());
                response.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
        }
        return response;
    }

}
