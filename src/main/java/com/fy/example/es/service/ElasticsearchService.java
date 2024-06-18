package com.fy.example.es.service;

import com.fy.example.es.vo.PageInfo;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHits;

import java.io.IOException;

public interface ElasticsearchService {

    boolean createIndex() throws IOException;

    boolean deleteIndex() throws Exception;

    boolean existsIndex() throws IOException;

    DocWriteResponse.Result addDocument() throws IOException;

    DocWriteResponse.Result deleteDocument() throws IOException;

    DocWriteResponse.Result updateDocument() throws IOException;

    String getDocument() throws IOException;

    SearchHits searchDocument() throws IOException;

    void bulk() throws IOException;

    PageInfo searchDocumentMatch() throws IOException;

}
