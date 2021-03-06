package com.guo.traveldemo.web.pojo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class TravelTable {
    private Integer id;

    private Integer straDeId;

    private String days;

    private String fee;

    private String address;

    private String people;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    private String advice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStraDeId() {
        return straDeId;
    }

    public void setStraDeId(Integer straDeId) {
        this.straDeId = straDeId;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
}