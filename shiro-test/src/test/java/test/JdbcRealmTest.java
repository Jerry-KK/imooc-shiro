package test;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * @author lethe
 * @date 2021/7/27 22:24
 */
public class JdbcRealmTest {

    DruidDataSource dataSource = new DruidDataSource();

    {
        dataSource.setUrl("jdbc:mysql://localhost:3306/imooc_shiro");
        dataSource.setUsername("root");
        dataSource.setPassword("admin");
    }


    @Test
    public void testAuthentication() {

        String userSql = "select password from test_user where name = ?";
        String roleSql = "select role_name from test_user_role where user_name = ?";

        DefaultSecurityManager securityManager = new DefaultSecurityManager();

        JdbcRealm realm = new JdbcRealm();
        realm.setDataSource(dataSource);
        realm.setPermissionsLookupEnabled(false);       //默认不开启权限

        realm.setAuthenticationQuery(userSql);
        realm.setUserRolesQuery(roleSql);


        securityManager.setRealm(realm);

        SecurityUtils.setSecurityManager(securityManager);

        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("lethe", "123456");
        subject.login(token);
        System.out.println(subject.isAuthenticated());

        subject.checkRole("admin");
        //subject.checkPermission("user:update");
    }
}
