package com.guo.traveldemo.web.pojo;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/5/28
 */
public class Sort {
    /**
     * 字段名
     */
    private String field;
    /**
     * 升序,asc,desc
     */
    private String order;

    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }
    public String getOrder() {
        return order;
    }
    public void setOrder(String order) {
        this.order = order;
    }


}