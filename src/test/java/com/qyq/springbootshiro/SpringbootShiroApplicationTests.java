package com.qyq.springbootshiro;

import com.qyq.springbootshiro.mapper.PremissionMapper;
import com.qyq.springbootshiro.mapper.RoleMapper;
import com.qyq.springbootshiro.pojo.Premission;
import com.qyq.springbootshiro.pojo.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootShiroApplicationTests {

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PremissionMapper premissionMapper;

    @Test
    public void contextLoads() {
        List<Role> roles = roleMapper.findARoleById(1);
        for (Role role : roles) {
            System.out.println(role.getRname());
            List<Premission> premissions = premissionMapper.findPremissionById(role.getId());
            for (Premission premission : premissions) {
                System.out.println(premission.getPname());
            }
        }
    }

}
