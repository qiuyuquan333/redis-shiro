package com.qyq.springbootshiro.mapper;

import com.qyq.springbootshiro.pojo.Premission;

import java.util.List;

public interface PremissionMapper {

    public List<Premission> findPremissionById(int id);
}
