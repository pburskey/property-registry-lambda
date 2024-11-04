package com.burskey.property.api;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.tests.EventLoader;
import com.burskey.property.domain.Property;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static com.burskey.property.FileResourceLoader.FindFile;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class GetByIDTest extends AbstractTest{




    protected GetByID getByID = null;


    @BeforeEach
    void setUp() {
        super.setUp();
        getByID = new GetByID(dao);
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