package com.burskey.property.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;
import com.burskey.property.domain.Property;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Dynamo {


    protected final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    protected final DynamoDB dynamoDB = new DynamoDB(client);
    protected final ObjectMapper mapper = new ObjectMapper().
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

    private String tableName;

    public Dynamo(String tableName) {
        this.tableName = tableName;
    }

    public List<Property> find(String name, String category) {
        List<Property> aList = new ArrayList<>();
//
//        Map<String, String> expressionAttributesNames = new HashMap<>();
//        expressionAttributesNames.put("#category", "category");
//        expressionAttributesNames.put("#name", "name");
////
////        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
////        expressionAttributeValues.put(":categoryValue", new AttributeValue().withS(category));
////        expressionAttributeValues.put(":name", new AttributeValue().withS(name));
//
//
//        ValueMap valueMap = new ValueMap();
//        valueMap.withString(":category", category);
//        valueMap.withString(":name", name);
//
//
//        QuerySpec querySpec = new QuerySpec()
//                .withKeyConditionExpression("#category = :category and #name = :name")
//                .withValueMap(valueMap)
//                .withNameMap(expressionAttributesNames)
//                .withConsistentRead(true);
//
//        Index index = this.dynamoDB.getTable(tableName).getIndex("categoryAndName");
//        ItemCollection<QueryOutcome>  outcomes= index.query(querySpec);

        ItemCollection<ScanOutcome>  outcomes= this.dynamoDB.getTable(this.tableName).scan();
        if (outcomes != null) {
            outcomes.forEach(outcome -> {
                String candidateName = outcome.get("name").toString();
                String candidateCategory = outcome.get("category").toString();
                if (name.equalsIgnoreCase(candidateName) && category.equalsIgnoreCase(candidateCategory)) {
                    aList.add(this.find(outcome.get("id").toString()));
                }

            });
        }





//        ItemCollection<ScanOutcome>  outcomes= this.dynamoDB.getTable(this.tableName).scan();
//        if (outcomes != null) {
//            outcomes.forEach(outcome -> {
//                String candidateName = outcome.get("name").toString();
//                String candidateCategory = outcome.get("category").toString();
//                if (name.equalsIgnoreCase(candidateName) && category.equalsIgnoreCase(candidateCategory)) {
//                    aList.add(this.find(outcome.get("id").toString()));
//                }
//
//            });
//        }

        return aList;
    }

    public List<Property> findByCategory( String category) {
        List<Property> aList = new ArrayList<>();
        ScanRequest scanRequest = new ScanRequest().withTableName(this.tableName);


        ItemCollection<ScanOutcome>  outcomes= this.dynamoDB.getTable(this.tableName).scan();
        if (outcomes != null) {
            outcomes.forEach(outcome -> {
                String candidateCategory = outcome.get("category").toString();
                if ( category.equalsIgnoreCase(candidateCategory)) {
                    aList.add(this.find(outcome.get("id").toString()));
                }

            });
        }

        return aList;
    }


    public Property save(Property aProperty) {
        if (aProperty != null){
            if (aProperty.getId() == null){
                aProperty.setId(java.util.UUID.randomUUID().toString());

                final Item item = new Item()
                        .withPrimaryKey("id", aProperty.getId())
                        .withString("name", aProperty.getName())
                        .withString("category", aProperty.getCategory())
                        .withString("description", aProperty.getDescription())
                        .withString("value", aProperty.getValue());

                final Table table = this.dynamoDB.getTable(this.tableName);
                table.putItem(item);
            }else{
                UpdateItemRequest updateItemRequest = new UpdateItemRequest();
                updateItemRequest.setTableName(this.tableName);
                updateItemRequest.addKeyEntry("id", new AttributeValue().withS(aProperty.getId()));
                updateItemRequest.addExpressionAttributeValuesEntry(":description", new AttributeValue().withS(aProperty.getDescription()));
                updateItemRequest.addExpressionAttributeValuesEntry(":value", new AttributeValue().withS(aProperty.getValue()));
                updateItemRequest.withUpdateExpression("set #d = :description , #v = :value");
                updateItemRequest.addExpressionAttributeNamesEntry("#v", "value");
                updateItemRequest.addExpressionAttributeNamesEntry("#d","description");

                try{
                    UpdateItemResult result = this.client.updateItem(updateItemRequest);
                } catch (Exception e) {
                    System.err.println("Error updating item: " + e.getMessage());
                }


            }

        }



        return aProperty;
    }



    public Property find(String id) {

        GetItemSpec spec = new GetItemSpec();
        spec.withPrimaryKey("id", id);
        final Table table = this.dynamoDB.getTable(this.tableName);

        Item item = table.getItem(spec);
        Property aProperty = null;
        if (item != null) {
            Map<String, Object> aMap = item.asMap();
            aProperty = mapper.convertValue(aMap, Property.class);
        }


        return aProperty;
    }
}
