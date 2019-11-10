package com.qyq.springbootshiro.mapper;

import com.qyq.springbootshiro.pojo.User;

public interface UserMapper {

    public User findUserByName(String name);

}
