package com.guo.traveldemo.result;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/14
 */
public class MsgResult implements Serializable {
    private int userNotifyId;
    private int id; // notify的id
    private String content;

    private Integer type;

    private Integer target;

    private Integer targetType;

    private Integer sender;

    private String action;

    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUserNotifyId() {
        return userNotifyId;
    }

    public void setUserNotifyId(int userNotifyId) {
        this.userNotifyId = userNotifyId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
