package com.guo.traveldemo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/18
 */
public class ScenicApiUtil {

    /**
     * 根据景点名称获取地址
     * @param scenicName
     * @param region
     * @return
     * @throws Exception
     */
    public static String getScenicInfoByURL(String scenicName,String region) throws Exception{
        String url = APIContants.CITY_BYNAME_URL+"query=" +scenicName+"&tag=旅游景点"+"&output=json"+"&region="+region+"&ak="+APIContants.BAIDU_KEY;
        URL u = new URL(url);
        InputStream in = u.openStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte buf[] = new byte[1024];
            int read = 0;
            while ((read = in .read(buf)) > 0) {
                out.write(buf, 0, read);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if ( in != null) {
                in .close();
            }
        }
        byte b[] = out.toByteArray();
        JSONObject jsonObject =  JSON.parseObject(new String(b, "utf-8"));
        JSONArray detail = jsonObject.getJSONArray("results");
        if(detail.size()>0){
            JSONObject jo = detail.getJSONObject(0);
            String str =jo.getString("province")+ jo.getString("city")+jo.getString("area");
            return str;
        }
        return null;
    }

}
