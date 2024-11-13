package com.burskey.property.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.burskey.property.dao.Dynamo;
import com.burskey.property.domain.Property;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;

public class CompositeCrudHandler extends AbstractLambda {


    public CompositeCrudHandler() {
    }

    public CompositeCrudHandler(Dynamo dynamo) {
        super(dynamo);
    }

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {

        LambdaLogger logger = context.getLogger();
        logger.log("Event Details:" + event);

        if (event.getPath().endsWith("/save")){
            Save save = new Save(this.dynamo);
            return save.handleRequest(event, context);
        } else if (event.getPath().endsWith("/find")){
            GetByNameCategory getByNameCategory = new GetByNameCategory(this.dynamo);
            return getByNameCategory.handleRequest(event, context);
        } else {
            GetByID getByID = new GetByID(this.dynamo);
            return getByID.handleRequest(event, context);
        }

//
//        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
//        response.setIsBase64Encoded(false);
//        response.setStatusCode(200);
//        response.setBody("cool");
//
//
//        return response;

    }


}
