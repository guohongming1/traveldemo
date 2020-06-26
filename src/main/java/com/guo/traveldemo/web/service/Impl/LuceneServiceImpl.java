package com.guo.traveldemo.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guo.traveldemo.dao.ILuceneDao;
import com.guo.traveldemo.util.PageQuery;
import com.guo.traveldemo.web.dto.SearchResultDTO;
import com.guo.traveldemo.web.pojo.Question;
import com.guo.traveldemo.web.pojo.Strategy;
import com.guo.traveldemo.web.pojo.Topic;
import com.guo.traveldemo.web.service.GroupService;
import com.guo.traveldemo.web.service.ILuceneService;
import com.guo.traveldemo.web.service.QuestionService;
import com.guo.traveldemo.web.service.StrategyService;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/5/28
 */
@Service
public class LuceneServiceImpl implements ILuceneService {

    @Autowired
    private ILuceneDao luceneDao;

    @Autowired
    private StrategyService strategyService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private GroupService groupService;

    @Override
    public void synProductCreatIndex() throws IOException {
        // 获取所有的productList
        List<SearchResultDTO> allProduct = new ArrayList<>();
        QueryWrapper<Strategy> strategyQueryWrapper = new QueryWrapper<>();
        strategyQueryWrapper.eq("del_flag",0);
        strategyQueryWrapper.eq("push_flag",1);
        List<Strategy> strategyList = strategyService.queryStrategy(strategyQueryWrapper);
        if(strategyList != null && strategyList.size()>0){
            strategyList.forEach(item->{
                SearchResultDTO dto = new SearchResultDTO();
                dto.setId(item.getId());
                dto.setType(1);
                dto.setTitle(item.getTitle());
                dto.setAddress(item.getAddress());
                dto.setDate(item.getDate());
                allProduct.add(dto);
            });
        }
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.ne("flag",3);
        List<Question> questionList = questionService.queryList(questionQueryWrapper);
        if(questionList != null && questionList.size()>0){
            questionList.forEach(item->{
                SearchResultDTO dto = new SearchResultDTO();
                dto.setId(item.getId());
                dto.setType(2);
                dto.setTags(item.getTags());
                dto.setTitle(item.getTitle());
                dto.setAddress(item.getAddress());
                dto.setDate(item.getDate());
                allProduct.add(dto);
            });
        }
        QueryWrapper<Topic> topicQuery = new QueryWrapper<>();
        List<Topic> topicList = groupService.queryListTopic(topicQuery);
        topicList.forEach(item->{
            SearchResultDTO dto = new SearchResultDTO();
            dto.setId(item.getId());
            dto.setType(3);
            dto.setTitle(item.getTitle());
            dto.setTags(item.getTags());
            dto.setDate(item.getDate());
            allProduct.add(dto);
        });
        // 再插入productList
        luceneDao.createProductIndex(allProduct);
    }

    @Override
    public PageQuery<SearchResultDTO> searchProduct(PageQuery<SearchResultDTO> pageQuery) throws IOException, ParseException, InvalidTokenOffsetsException {
        return luceneDao.searchProduct(pageQuery);
    }

}
