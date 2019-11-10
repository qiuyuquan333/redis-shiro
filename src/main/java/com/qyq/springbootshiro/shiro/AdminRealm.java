package com.qyq.springbootshiro.shiro;

import com.qyq.springbootshiro.mapper.AdminMapper;
import com.qyq.springbootshiro.mapper.PremissionMapper;
import com.qyq.springbootshiro.mapper.RoleMapper;
import com.qyq.springbootshiro.pojo.Admin;
import com.qyq.springbootshiro.pojo.Premission;
import com.qyq.springbootshiro.pojo.Role;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.List;

public class AdminRealm extends AuthorizingRealm {

    @Resource
    private AdminMapper adminMapper;
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
        Admin admin = (Admin) principalCollection.getPrimaryPrincipal();//获取信息
        List<Role> roles = roleMapper.findARoleById(admin.getId());
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
        Admin admin = adminMapper.findAdminByName(uname);
        if(admin == null){
            throw new UnknownAccountException("没有此用户！");
        }
        //获取密码
        String apassword = admin.getApassword();

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(admin,apassword,ByteSource.Util.bytes(uname),this.getName());

        return info;
    }

    protected boolean supports(UsernamePasswordToken token){
        return token.getHost().equals("admin");
    }

}
