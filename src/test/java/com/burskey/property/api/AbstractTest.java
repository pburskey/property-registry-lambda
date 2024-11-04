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

public abstract class AbstractTest {



    protected Dynamo dao = Mockito.mock(Dynamo.class);
    protected final ObjectMapper mapper = new ObjectMapper().
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

    protected Context context = null;


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

}