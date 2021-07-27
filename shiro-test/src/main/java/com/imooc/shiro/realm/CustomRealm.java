package com.imooc.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.SimpleRole;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author lethe
 * @date 2021/7/27 23:38
 */
public class CustomRealm extends AuthorizingRealm {

    Map<String,String> userMap = new HashMap<>(16);
    Map<String,String> roleMap = new HashMap<>(16);

    {
        userMap.put("lethe","123456");
        roleMap.put("lethe","admin");
        super.setName("customRealm");
    }

    //授权过程
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        String userName = (String) principalCollection.getPrimaryPrincipal();

        Set<String> roles = getRoleNameByUserName(userName);
        Set<String> permissions = getPermissionsNameByUserName(userName);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permissions);
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }

    //认证过程
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        // 1.从主体传过来的认证信息中，获得用户名
        String userName = (String)authenticationToken.getPrincipal();

        // 2.通过用户名到数据库中获取凭证
        String password = getPassWordByUserName(userName);
        if(password == null) {
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo
                (userName,password,getName());

        return authenticationInfo;
    }

    /**
     * 模拟数据库找到凭证
     * @param userName
     * @return
     */
    private String getPassWordByUserName(String userName) {
        return userMap.get(userName);
    }

    private Set<String> getRoleNameByUserName(String userName) {
        Set<String> set = new HashSet<>(16);
        set.add("admin");
        set.add("user");
        return set;
    }

    private Set<String> getPermissionsNameByUserName(String userName) {
        Set<String> set = new HashSet<>(16);
        set.add("user:select");
        set.add("admin:update");
        return set;
    }
}
