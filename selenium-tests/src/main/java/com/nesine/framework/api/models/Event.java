package com.nesine.framework.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {

    private String name;
    private List<Outcome> outcomes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Outcome> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(List<Outcome> outcomes) {
        this.outcomes = outcomes;
    }
}