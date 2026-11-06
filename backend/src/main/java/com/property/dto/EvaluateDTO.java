package com.property.dto;

/**
 * 住户对已完成工单的评价
 */
public class EvaluateDTO {

    private Integer rating;

    private String evaluate;

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }
}
