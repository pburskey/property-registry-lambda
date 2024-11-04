package com.burskey.property.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.tests.EventLoader;
import com.burskey.property.dao.Dynamo;
import com.burskey.property.domain.Property;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;

import static com.burskey.property.FileResourceLoader.FindFile;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

class GetByIDTest {



    Dynamo dao = Mockito.mock(Dynamo.class);
    GetByID getByID = new GetByID(dao);
    protected final ObjectMapper mapper = new ObjectMapper().
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

    Context context = null;


    @BeforeEach
    void setUp() {
        LambdaLogger loggerMock = Mockito.mock(LambdaLogger.class);
        this.context = Mockito.mock(Context.class);
        when(context.getLogger()).thenReturn(loggerMock);

        doAnswer(call -> {
            System.out.println((String)call.getArgument(0));//print to the console
            return null;
        }).when(loggerMock).log(anyString());

    }

    @Test
    void handleRequest() throws Exception {

        File file = FindFile("classpath:sample_request_events/GetByID.json");
        APIGatewayProxyRequestEvent event = EventLoader.loadApiGatewayRestEvent(file.getPath());
//        APIGatewayProxyRequestEvent event = mapper.readValue(data, APIGatewayProxyRequestEvent.class);
        when(this.dao.find(anyString())).thenReturn(new Property());

        APIGatewayProxyResponseEvent response = this.getByID.handleRequest(event, context);
        assertNotNull(response);

    }
}