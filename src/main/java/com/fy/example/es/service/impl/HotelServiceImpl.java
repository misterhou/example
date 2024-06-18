package com.fy.example.es.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fy.example.es.entity.Hotel;
import com.fy.example.es.mapper.HotelMapper;
import com.fy.example.es.service.HotelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {

    @Resource
    private HotelMapper hotelMapper;
    @Override
    public List<Hotel> findAll() {
//        Page<Hotel> page = new Page<>(1, 2);
        QueryWrapper<Hotel> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(Hotel::getId);
        return this.hotelMapper.selectList(queryWrapper);
    }
}
