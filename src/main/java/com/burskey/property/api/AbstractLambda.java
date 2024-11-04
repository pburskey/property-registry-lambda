package com.burskey.property.api;

import com.burskey.property.dao.Dynamo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AbstractLambda {


    protected static final String ENV_PROPERTY_TABLE = "PROPERTY_TABLE";
    protected Dynamo dynamo = null;

    protected final ObjectMapper mapper = new ObjectMapper().
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);


    public AbstractLambda() {
        this.dynamo = new Dynamo(getFromEnvironment(ENV_PROPERTY_TABLE));
    }

    public AbstractLambda(Dynamo dynamo) {
        this.dynamo = dynamo;
    }

    protected String getFromEnvironment(String key) {
        String value = System.getenv(key);
        return value;
    }
}
