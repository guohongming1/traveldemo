package com.guo.traveldemo.web.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

public class TravelTable {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private Integer straDeId;

    private Byte days;

    private String fee;

    private String address;

    private String people;

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

    public Byte getDays() {
        return days;
    }

    public void setDays(Byte days) {
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