package com.burskey.property.api;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.tests.EventLoader;
import com.burskey.property.domain.Property;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.burskey.property.FileResourceLoader.FindFile;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class GetByNameCategoryTest extends AbstractTest{


    protected GetByNameCategory getByNameCategory = null;

    @BeforeEach
    void setUp() {
        super.setUp();
        this.getByNameCategory = new GetByNameCategory(this.dao);
    }

    @Test
    void handleRequest_a() {

        File file = FindFile("classpath:sample_request_events/GetByNameCategory.json");
        APIGatewayProxyRequestEvent event = EventLoader.loadApiGatewayRestEvent(file.getPath());
//        APIGatewayProxyRequestEvent event = mapper.readValue(data, APIGatewayProxyRequestEvent.class);
        List<Property> list = new ArrayList<>();
        list.add(new Property());
        when(this.dao.find(anyString(),anyString())).thenReturn(list);

        APIGatewayProxyResponseEvent response = this.getByNameCategory.handleRequest(event, context);
        assertNotNull(response);

    }

    @Test
    void handleRequest_b() {

        File file = FindFile("classpath:sample_request_events/GetByNameCategory2.json");
        APIGatewayProxyRequestEvent event = EventLoader.loadApiGatewayRestEvent(file.getPath());
//        APIGatewayProxyRequestEvent event = mapper.readValue(data, APIGatewayProxyRequestEvent.class);
        List<Property> list = new ArrayList<>();
        list.add(new Property());
        when(this.dao.findByCategory(anyString())).thenReturn(list);

        APIGatewayProxyResponseEvent response = this.getByNameCategory.handleRequest(event, context);
        assertNotNull(response);

    }

}