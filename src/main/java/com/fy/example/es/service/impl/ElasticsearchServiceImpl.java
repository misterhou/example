package com.fy.example.es.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.fy.example.es.entity.Hotel;
import com.fy.example.es.mapper.HotelMapper;
import com.fy.example.es.service.ElasticsearchService;
import com.fy.example.es.vo.HotelDoc;
import com.fy.example.es.vo.PageInfo;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    private static final String INDEX = "hotel";

    private static final Long DOC_ID = 300L;

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Resource
    private HotelMapper hotelMapper;

    @Override
    public boolean createIndex() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(INDEX);
        createIndexRequest.source("{\n" +
                "  \"mappings\": {\n" +
                "    \"properties\": {\n" +
                "      \"id\": {\n" +
                "        \"type\": \"keyword\"\n" +
                "      },\n" +
                "      \"name\": {\n" +
                "        \"type\": \"text\",\n" +
                "        \"analyzer\": \"ik_max_word\",\n" +
                "        \"copy_to\": \"all\"\n" +
                "      },\n" +
                "      \"address\": {\n" +
                "        \"type\": \"keyword\",\n" +
                "        \"index\": false\n" +
                "      },\n" +
                "      \"price\": {\n" +
                "        \"type\": \"integer\"\n" +
                "      },\n" +
                "      \"score\": {\n" +
                "        \"type\": \"integer\"\n" +
                "      },\n" +
                "      \"brand\": {\n" +
                "        \"type\": \"keyword\",\n" +
                "        \"copy_to\": \"all\"\n" +
                "      },\n" +
                "      \"city\": {\n" +
                "        \"type\": \"keyword\"\n" +
                "      },\n" +
                "      \"starName\": {\n" +
                "        \"type\": \"keyword\"\n" +
                "      },\n" +
                "      \"business\": {\n" +
                "        \"type\": \"keyword\",\n" +
                "        \"copy_to\": \"all\"\n" +
                "      },\n" +
                "      \"location\": {\n" +
                "        \"type\": \"geo_point\"\n" +
                "      },\n" +
                "      \"pic\": {\n" +
                "        \"type\": \"keyword\",\n" +
                "        \"index\": false\n" +
                "      },\n" +
                "      \"all\": {\n" +
                "        \"type\": \"text\",\n" +
                "        \"analyzer\": \"ik_max_word\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}", XContentType.JSON);
        CreateIndexResponse createIndexResponse = this.restHighLevelClient.indices()
                .create(createIndexRequest, RequestOptions.DEFAULT);
        return createIndexResponse.isAcknowledged();
    }

    @Override
    public boolean deleteIndex() throws Exception {
        AcknowledgedResponse acknowledgedResponse = this.restHighLevelClient.indices()
                .delete(new DeleteIndexRequest(INDEX), RequestOptions.DEFAULT);
        return acknowledgedResponse.isAcknowledged();
    }

    @Override
    public boolean existsIndex() throws IOException {
        return this.restHighLevelClient.indices().exists(new GetIndexRequest(INDEX), RequestOptions.DEFAULT);
    }

    @Override
    public DocWriteResponse.Result addDocument() throws IOException {
        IndexRequest request = new IndexRequest(INDEX);
        HotelDoc hotelDoc = new HotelDoc();
        hotelDoc.setId(DOC_ID);
        hotelDoc.setName("测试数据");
        hotelDoc.setAddress("测试地址");
        hotelDoc.setBrand("测试品牌");
        hotelDoc.setCity("测试城市");
        hotelDoc.setStarName("测试星级");
        hotelDoc.setPrice(100);
        hotelDoc.setScore(100);
        hotelDoc.setLocation("39.92,116.46");
        hotelDoc.setPic("");
        hotelDoc.setBusiness("测试商圈");
        request.id(hotelDoc.getId().toString())
                .source(JSONObject.toJSONString(hotelDoc), XContentType.JSON);
        IndexResponse indexResponse = this.restHighLevelClient.index(request, RequestOptions.DEFAULT);
        return indexResponse.getResult();
    }

    @Override
    public DocWriteResponse.Result deleteDocument() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(INDEX, DOC_ID.toString());
        DeleteResponse deleteResponse = this.restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(deleteResponse);
        return deleteResponse.getResult();
    }

    @Override
    public DocWriteResponse.Result updateDocument() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(INDEX, DOC_ID.toString());
        updateRequest.doc("content", "我的家乡是河北");
        UpdateResponse updateResponse = this.restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse);
        return updateResponse.getResult();
    }

    @Override
    public String getDocument() throws IOException {
        GetRequest request = new GetRequest(INDEX, DOC_ID.toString());
        GetResponse getResponse = this.restHighLevelClient.get(request, RequestOptions.DEFAULT);
        return getResponse.getSourceAsString();
    }

    @Override
    public SearchHits searchDocument() throws IOException {
        SearchRequest searchRequest = new SearchRequest(INDEX);
        searchRequest.source().query(QueryBuilders.matchAllQuery());
        // 排序
        searchRequest.source().sort("id", SortOrder.DESC);
        // 分页
        int pageNo = 1;
        int pageSize = 2;
        searchRequest.source().from((pageNo - 1) * pageSize).size(pageSize);

        SearchResponse searchResponse = this.restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        long total = searchHits.getTotalHits().value;
        System.out.println("总记录数：" + total);
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        return searchHits;
    }

    public PageInfo searchDocumentMatch() throws IOException {
        PageInfo pageInfo = new PageInfo();
        SearchRequest searchRequest = new SearchRequest(INDEX);
//        QueryBuilders.multiMatchQuery("title", "content");
//        QueryBuilders.termQuery("title", "title");
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        boolQueryBuilder.must(QueryBuilders.matchQuery("title", "北京"));
//        boolQueryBuilder.filter(QueryBuilders.termQuery("title", "title"));
//        QueryBuilders.rangeQuery("title", "北京");
        // 排序
        searchRequest.source().sort("id", SortOrder.DESC);
        // 分页
        int pageNo = 1;
        int pageSize = 3;
        pageInfo.setPageSize(pageSize);
        pageInfo.setPageNum(pageNo);
        searchRequest.source().from((pageNo - 1) * pageSize).size(pageSize);
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("all", "7天");
        // 高亮
        String highlightFiled = "name";
        searchRequest.source().highlighter(new HighlightBuilder().field(highlightFiled).requireFieldMatch(false));
        searchRequest.source().query(queryBuilder);
        SearchResponse searchResponse = this.restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        long total = searchHits.getTotalHits().value;
        pageInfo.setTotal((int) total);
        pageInfo.setTotalPage((int) Math.ceil(total * 1.0 / pageSize));
        System.out.println("总记录数：" + total);
        SearchHit[] hits = searchHits.getHits();
        List<String> data = new ArrayList<>();
        for (SearchHit hit : hits) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlightField = highlightFields.get(highlightFiled);
            String name = highlightField.getFragments()[0].string();
            System.out.println(name);
            System.out.println(hit.getSourceAsString());
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            sourceAsMap.put(highlightFiled, name);
            data.add(JSONObject.toJSONString(sourceAsMap));
        }
        pageInfo.setData(data);
        return pageInfo;
    }

    @Override
    public void bulk() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        List<Hotel> hotelList = this.hotelMapper.selectList(null);
        if (hotelList != null) {
            for (Hotel hotel : hotelList) {
                HotelDoc hotelDoc = new HotelDoc(hotel);
                bulkRequest.add(
                        new IndexRequest(INDEX)
                                .id(hotelDoc.getId().toString())
                                .source(JSONObject.toJSONString(hotelDoc), XContentType.JSON));
            }
        }
        BulkResponse bulkResponse = this.restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulkResponse);
    }
}
