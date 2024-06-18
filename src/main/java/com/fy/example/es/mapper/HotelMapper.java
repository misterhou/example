package com.fy.example.es.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fy.example.es.entity.Hotel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HotelMapper extends BaseMapper<Hotel> {
}
