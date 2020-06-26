package com.guo.traveldemo.web.service;

import com.guo.traveldemo.util.PageQuery;
import com.guo.traveldemo.web.dto.SearchResultDTO;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import java.io.IOException;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/5/28
 */
public interface ILuceneService {
    /**
     * 启动后将同步Product表,并创建index
     * @throws IOException
     */
    public void synProductCreatIndex() throws IOException;

    public PageQuery<SearchResultDTO> searchProduct(PageQuery<SearchResultDTO> pageQuery) throws IOException, ParseException, InvalidTokenOffsetsException;
}
