package com.fy.example.es.vo;

import com.fy.example.es.entity.Hotel;
import lombok.Data;

@Data
public class HotelDoc {

    private Long id;

    private String name;

    private String address;

    private Integer price;

    private Integer score;

    private String brand;

    private String city;

    private String starName;

    private String business;

    private String location;

    private String pic;

    public HotelDoc() {
    }

    public HotelDoc(Hotel hotel) {
        this.id = hotel.getId();
        this.name = hotel.getName();
        this.address = hotel.getAddress();
        this.price = hotel.getPrice();
        this.score = hotel.getScore();
        this.brand = hotel.getBrand();
        this.city = hotel.getCity();
        this.starName = hotel.getStarName();
        this.business = hotel.getBusiness();
        this.location = hotel.getLatitude() + "," + hotel.getLongitude();
        this.pic = hotel.getPic();
    }
}
