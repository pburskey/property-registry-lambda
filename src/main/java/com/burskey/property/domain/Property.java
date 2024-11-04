package com.burskey.property.domain;

public class Property {
    public String id;
    public String category;
    public String name;
    public String description;
    public String value;




//
//    public Property(String id, String name, String description, String value, String category) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.value = value;
//        this.category = category;
//    }
//
//    public Property() {
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}
