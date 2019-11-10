package com.qyq.springbootshiro.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ShiroConfig {

    //不需要在此处配置权限页面,因为上面的ShiroFilterFactoryBean已经配置过,但是此处必须存在,因为shiro-spring-boot-web-starter或查找此Bean,没有会报错
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition(){
        return new DefaultShiroFilterChainDefinition();
    }

    /**
     * 配置shiroFilter过滤器
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        //权限配置
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();

        shiroFilterFactoryBean.setLoginUrl("/adminlogin");
        shiroFilterFactoryBean.setSuccessUrl("/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");  //配置没有权限跳转的URL

        filterChainDefinitionMap.put("/index","authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 配置securityManager 安全管理器
     * @return
     */
    @Bean
    public SessionsSecurityManager securityManager(){
        DefaultWebSecurityManager webSecurityManager = new DefaultWebSecurityManager();
        //配置认证器
        List<Realm> realms = new ArrayList<Realm>();
        realms.add(userRealm());
        realms.add(adminRealm());
        webSecurityManager.setRealms(realms);
        webSecurityManager.setSessionManager(sessionManager());
        webSecurityManager.setCacheManager(redisCacheManager());
        return webSecurityManager;
    }

    /**
     * 配置自定义认证器
     * @return
     */
    @Bean
    public UserRealm userRealm(){
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return userRealm;
    }
    @Bean
    public AdminRealm adminRealm(){
        AdminRealm adminRealm = new AdminRealm();
        adminRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return adminRealm;
    }

    /**
     * 配置加密方式
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        //盐
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //配置散列算法，，使用MD5加密算法
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //设置散列次数
        hashedCredentialsMatcher.setHashIterations(1);
        return hashedCredentialsMatcher;
    }


    /**
     * Session Manager
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisCacheManager redisCacheManager() {

        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        redisCacheManager.setExpire(5000);
        redisCacheManager.setPrincipalIdFieldName("id");
        return redisCacheManager;
    }

    /**
     * 配置shiro redisManager
//     * 使用的是shiro-redis开源插件
//     *
//     * @return
//     */
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        //redisManager.setHost("127.0.0.1");
        return redisManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        redisSessionDAO.setExpire(2000);
        return redisSessionDAO;
    }


}
