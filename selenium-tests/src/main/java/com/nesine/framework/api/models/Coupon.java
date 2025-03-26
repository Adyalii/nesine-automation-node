package com.nesine.framework.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Coupon {

    private String couponHash;
    private List<Event> events;
    private int eventCount;
    private String totalOdd;

    public String getCouponHash() {
        return couponHash;
    }

    public void setCouponHash(String couponHash) {
        this.couponHash = couponHash;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public int getEventCount() {
        return eventCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public String getTotalOdd() {
        return totalOdd;
    }

    public void setTotalOdd(String totalOdd) {
        this.totalOdd = totalOdd;
    }


    public double calculateTotalOdd() {
        if (events == null) {
            return 0.0;
        }
        return events.stream()
                .map(Event::getOutcomes)
                .flatMap(List::stream)
                .mapToDouble(outcome -> {
                    try {
                        return Double.parseDouble(outcome.getOdd().replace(",", "."));
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                })
                .reduce(1.0, (a, b) -> a * b);
    }
}