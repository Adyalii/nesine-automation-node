package com.nesine.framework.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Outcome {

    private String name;
    private String odd;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOdd() {
        return odd;
    }

    public void setOdd(String odd) {
        this.odd = odd;
    }
}