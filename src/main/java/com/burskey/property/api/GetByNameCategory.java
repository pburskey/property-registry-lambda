package com.burskey.property.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.burskey.property.dao.Dynamo;
import com.burskey.property.domain.Property;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;

import java.util.List;

public class GetByNameCategory extends AbstractLambda {


    public GetByNameCategory() {
        super();
    }

    public GetByNameCategory(Dynamo dynamo) {
        super(dynamo);
    }

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {

        LambdaLogger logger = context.getLogger();
//        logger.log("Event Details:" + event);

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setIsBase64Encoded(false);
        response.setStatusCode(200);
        try {

            String name = null;
            String category = null;
            if (event != null && event.getQueryStringParameters() != null) {
                name = event.getQueryStringParameters().get("name");
                category = event.getQueryStringParameters().get("category");

                if (category == null || category.isEmpty()) {
                    response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                    response.setBody("Missing category");
                } else {

                    List<Property> aList = null;
                    if (name != null && category != null) {
                        aList = dynamo.find(name, category);
                    } else if (category != null) {
                        aList = dynamo.findByCategory(category);
                    }

                    if (aList != null && aList.size() > 0) {
                        response.setBody(new ObjectMapper().writeValueAsString(aList));
                    }
                }
            }

        } catch (Exception e) {
            logger.log(e.getMessage());
            response.setBody(e.getMessage());
            response.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }

        return response;

    }

}
