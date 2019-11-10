package com.qyq.springbootshiro.mapper;

import com.qyq.springbootshiro.pojo.Admin;

public interface AdminMapper {

    public Admin findAdminByName(String name);
}
