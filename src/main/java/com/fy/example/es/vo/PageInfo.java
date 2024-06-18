package com.fy.example.es.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageInfo {

    private int pageNum;

    private int pageSize;

    private int total;

    private int totalPage;

    private List<String> data;
}
