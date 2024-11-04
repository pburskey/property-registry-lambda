package com.burskey.property.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.burskey.property.dao.Dynamo;
import com.burskey.property.domain.Property;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;

public class GetByID extends AbstractLambda {


    public GetByID() {
    }

    public GetByID(Dynamo dynamo) {
        super(dynamo);
    }

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {

        LambdaLogger logger = context.getLogger();
        logger.log("Event Details:" + event);

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setIsBase64Encoded(false);
        response.setStatusCode(200);
        try {

            String id = null;
            if (event != null && event.getPathParameters() != null) {
                id = event.getPathParameters().get("id");
            }

            response.setBody("Searching for ID: " + id);
            Property property = this.dynamo.find(id);
            response.setBody(new ObjectMapper().writeValueAsString(property));

        } catch (Exception e) {
            logger.log(e.getMessage());
            response.setBody(e.getMessage());
            response.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }

        return response;

    }


}
