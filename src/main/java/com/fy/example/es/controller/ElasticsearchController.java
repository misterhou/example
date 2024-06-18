package com.fy.example.es.controller;

import com.fy.example.es.service.ElasticsearchService;
import com.fy.example.es.vo.PageInfo;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHits;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/es")
public class ElasticsearchController {

    @Resource
    private ElasticsearchService elasticsearchService;

    @GetMapping("/index/exists")
    public String existsIndex() {
        boolean result = false;
        try {
            result = this.elasticsearchService.existsIndex();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result ? "存在" : "不存在";
    }

    @GetMapping("/index/create")
    public String createIndex() {
        boolean result = false;
        try {
            result = this.elasticsearchService.createIndex();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result ? "创建成功" : "创建失败";
    }

    @GetMapping("/index/delete")
    public String deleteIndex() {
        boolean result = false;
        try {
            result = this.elasticsearchService.deleteIndex();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result ? "删除成功" : "删除失败";
    }

    @GetMapping("/document/search")
    public SearchHits searchDocument() {
        SearchHits searchHits = null;
        try {
            searchHits = this.elasticsearchService.searchDocument();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchHits;
    }

    @GetMapping("/document/add")
    public DocWriteResponse.Result addDocument() {
        DocWriteResponse.Result result = null;
        try {
            result = this.elasticsearchService.addDocument();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/document/delete")
    public DocWriteResponse.Result deleteDocument() {
        DocWriteResponse.Result result = null;
        try {
            result = this.elasticsearchService.deleteDocument();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/document/update")
    public DocWriteResponse.Result updateDocument() {
        DocWriteResponse.Result result = null;
        try {
            result = this.elasticsearchService.updateDocument();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/document/get")
    public String getDocument() {
        String document = null;
        try {
            document = this.elasticsearchService.getDocument();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    @RequestMapping("/document/bulk")
    public String test() {
        boolean result = false;
        try {
            this.elasticsearchService.bulk();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result ? "批量导入成功" : "批量导入失败";
    }

    @GetMapping("/document/search-match")
    public PageInfo searchDocumentMatch() {
        PageInfo pageInfo = null;
        try {
            pageInfo = this.elasticsearchService.searchDocumentMatch();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pageInfo;
    }
}
