package com.qyq.springbootshiro.shiro;

import com.qyq.springbootshiro.mapper.PremissionMapper;
import com.qyq.springbootshiro.mapper.RoleMapper;
import com.qyq.springbootshiro.mapper.UserMapper;
import com.qyq.springbootshiro.pojo.Premission;
import com.qyq.springbootshiro.pojo.Role;
import com.qyq.springbootshiro.pojo.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.List;

public class UserRealm extends AuthorizingRealm {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PremissionMapper premissionMapper;

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        if(principalCollection.fromRealm(this.getName()).isEmpty()){
            System.out.println(principalCollection.fromRealm(this.getName()));
            return null;
        }
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principalCollection.getPrimaryPrincipal();//获取信息
        List<Role> roles = roleMapper.findURoleById(user.getId());
        for (Role role : roles) {
            authorizationInfo.addRole(role.getRname());  //添加角色
            List<Premission> premissions = premissionMapper.findPremissionById(role.getId());
            for (Premission premission : premissions) {
                authorizationInfo.addStringPermission(premission.getPname());  //添加权限
            }
        }
        return authorizationInfo;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //强转成UsernamePasswordToken对象
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        if(!supports(token)){
            return null;
        }
        //获取用户名
        String uname = token.getUsername();
        //连接数据库查询
        User user = userMapper.findUserByName(uname);
        if(user == null){
            throw new UnknownAccountException("没有此用户！");
        }
        //获取密码
        String upassword = user.getUpassword();

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user,upassword,ByteSource.Util.bytes(uname),this.getName());

        return info;
    }


    protected boolean supports(UsernamePasswordToken token){
        return token.getHost().equals("user");
    }

}
