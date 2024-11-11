package com.burskey.property.api;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.tests.EventLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static com.burskey.property.FileResourceLoader.FindFile;
import static org.junit.jupiter.api.Assertions.*;

class SaveTest extends AbstractTest{




    protected Save save = null;


    @BeforeEach
    void setUp() {
        super.setUp();
        save = new Save(dao);
    }

    @Test
    void handleRequest() throws Exception {

        File file = FindFile("classpath:sample_request_events/Save-Empty.json");
        APIGatewayProxyRequestEvent event = EventLoader.loadApiGatewayRestEvent(file.getPath());
//        APIGatewayProxyRequestEvent event = mapper.readValue(data, APIGatewayProxyRequestEvent.class);


        APIGatewayProxyResponseEvent response = this.save.handleRequest(event, context);
        assertNotNull(response);
        assertEquals(response.getStatusCode(), 400);
        assertTrue(response.getBody().contains("Name is missing"));


    }
}