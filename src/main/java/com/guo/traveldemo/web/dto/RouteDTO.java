package com.guo.traveldemo.web.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/21
 */
public class RouteDTO implements Serializable {
    private static final long serialVersionUID = -3964542768431033815L;
    private int days;
    private List<List<String>> list;

    public RouteDTO(String str){
        if(str.length()>2){
            List<List<String>> result = new ArrayList<>();
            String[] day = str.split("\\{");
            this.days = day.length;
            for(int i=1;i<day.length;i++){
                List<String> list = new ArrayList<>();
                String[] item = day[i].split("}");
                for(int j=1;j<item.length;j++){
                    list.add(item[j]);
                }
                result.add(list);
            }
            this.list = result;
        }
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public List<List<String>> getList() {
        return list;
    }

    public void setList(List<List<String>> list) {
        this.list = list;
    }
}
