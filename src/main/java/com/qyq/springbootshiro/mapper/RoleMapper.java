package com.qyq.springbootshiro.mapper;

import com.qyq.springbootshiro.pojo.Role;

import java.util.List;

public interface RoleMapper {

    public List<Role> findURoleById(int id);

    public List<Role> findARoleById(int id);
}
