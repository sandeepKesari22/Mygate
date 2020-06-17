package com.gapbe.commons.enums;

public enum PartnerId {
    FLIPKART("1"),
    GROFERS("2");

    String key;

    PartnerId(String key) {
        this.key = key;
    }

    public String getKey(){
        return this.key;
    }
}
