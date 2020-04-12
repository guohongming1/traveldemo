package com.guo.traveldemo.web.pojo;

public class Route {
    private Integer id;

    private Integer nextdayId;

    private String content;

    private Integer straDeId;

    private Byte headFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNextdayId() {
        return nextdayId;
    }

    public void setNextdayId(Integer nextdayId) {
        this.nextdayId = nextdayId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStraDeId() {
        return straDeId;
    }

    public void setStraDeId(Integer straDeId) {
        this.straDeId = straDeId;
    }

    public Byte getHeadFlag() {
        return headFlag;
    }

    public void setHeadFlag(Byte headFlag) {
        this.headFlag = headFlag;
    }
}