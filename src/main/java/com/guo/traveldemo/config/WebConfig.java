package com.guo.traveldemo.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/6
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 将static下面的js，css文件加载出来
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //registry.addResourceHandler("/static/").addResourceLocations("classpath:/static/");
        // 以下配置会直接导致浏览器地址栏输入地址即可直接访问该文件夹下的所有文件
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:C://Users/guohongming/Picturesuploads/");
    }
}
