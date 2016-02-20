package org.edagarli.framework.dao.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * User: edagarli
 * Email: lizhi@edagarli.com
 * Date: 16/2/20
 * Time: 23:04
 * Desc:dao 拦截器
 */
public class DaoHandler implements InvocationHandler{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private String UPPER_KEY = "upper";

    private String LOWER_KEY = "lower";

    /**
     * map的关键字类型 三个值
     */
    private String keyType = "origin";

    private boolean formatSql = false;

    private boolean showSql = false;

    private String dbType;

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        return null;
    }
}
