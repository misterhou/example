package com.fy.example.es.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_hotel")
public class Hotel {

    private Long id;

    private String name;

    private String address;

    private Integer price;

    private Integer score;

    private String brand;

    private String city;

    private String starName;

    private String business;

    private String latitude;

    private String longitude;

    private String pic;
}
