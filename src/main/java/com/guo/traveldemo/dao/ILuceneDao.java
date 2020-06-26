package com.guo.traveldemo.dao;

import com.guo.traveldemo.util.PageQuery;
import com.guo.traveldemo.web.dto.SearchResultDTO;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import java.io.IOException;
import java.util.List;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/5/28
 */
public interface ILuceneDao {
    /**
     * 创建索引
     * @param productList
     * @throws IOException
     */
    public void createProductIndex(List<SearchResultDTO> productList) throws IOException;
    /**
     * 查询索引
     * @param pageQuery
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public PageQuery<SearchResultDTO> searchProduct(PageQuery<SearchResultDTO> pageQuery) throws IOException, ParseException, InvalidTokenOffsetsException;
    /**
     * 添加一个新索引
     * @param product
     * @throws IOException
     */
    public void addProductIndex(SearchResultDTO product) throws IOException;
    /**
     * 通过id删除商品索引
     * @param id
     * @throws IOException
     */
    public void deleteProductIndexById(String id) throws IOException;


}
