package org.edagarli.framework.dao.aop;

import org.edagarli.framework.dao.pojo.DaoPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * User: edagarli
 * Email: lizhi@edagarli.com
 * Date: 16/2/20
 * Time: 23:04
 * Desc:dao 拦截器
 */
public class DaoHandler implements InvocationHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(DaoHandler.class);

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
// 返回结果
        Object returnObj = null;
        // SQL模板
        String templateSql = null;
        // SQL模板参数
        Map<String, Object> sqlParamsMap = new HashMap<String, Object>();
        // 分页参数
        DaoPage pageSetting = new DaoPage();

        // Step.0 判断是否是Hiber实体维护方法，如果是执行Hibernate方式实体维护
        Map<String, Object> rs = new HashMap<String, Object>();

        //// TODO: 16/2/20

        if (showSql) {
            //TODO 待实现
            LOGGER.info("Dao-SQL:\n\n");
        }
        return returnObj;
    }


}
