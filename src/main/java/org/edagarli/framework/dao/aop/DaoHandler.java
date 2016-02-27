package org.edagarli.framework.dao.aop;

import ognl.Ognl;
import ognl.OgnlException;
import org.edagarli.framework.annotation.Arguments;
import org.edagarli.framework.annotation.ResultType;
import org.edagarli.framework.annotation.Sql;
import org.edagarli.framework.dao.def.DaoConstants;
import org.edagarli.framework.dao.pojo.DaoPage;
import org.edagarli.framework.dao.rowMapper.MiniColumnMapRowMapper;
import org.edagarli.framework.dao.rowMapper.MiniColumnOriginalMapRowMapper;
import org.edagarli.framework.dao.util.FreemarkerParseFactory;
import org.edagarli.framework.util.DaoUtil;
import org.edagarli.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: edagarli
 * Email: lizhi@edagarli.com
 * Date: 16/2/20
 * Time: 23:04
 * Desc:dao 拦截器
 */
public class DaoHandler implements InvocationHandler {

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

        Map<String, Object> rs = new HashMap<String, Object>();

        templateSql = installDaoMetaData(pageSetting, method, sqlParamsMap, args);

        String executeSql = parseSqlTemplate(method, templateSql, sqlParamsMap);

        Map<String, Object> sqlMap = installPlaceholderSqlParam(executeSql, sqlParamsMap);

        try {
            returnObj = getReturnDaoResult(dbType, pageSetting, method, executeSql, sqlMap);
        } catch (EmptyResultDataAccessException e) {
            returnObj = null;
        }

        if (showSql) {
            //TODO 待实现
            LOGGER.info("Dao-SQL:\n\n");
        }
        return returnObj;
    }

    private String getCountSql(String sql) {
        return "select count(0) from (" + sql + ") tmp_count";
    }

    public String getDbType() {
        return dbType;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public String getKeyType() {
        return keyType;
    }

    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    private static boolean checkActiveKey(String methodName) {
        String keys[] = DaoConstants.INF_METHOD_ACTIVE.split(",");
        for (String s : keys) {
            if (methodName.startsWith(s))
                return true;
        }
        return false;
    }

    private static boolean checkBatchKey(String methodName) {
        String keys[] = DaoConstants.INF_METHOD_BATCH.split(",");
        for (String s : keys) {
            if (methodName.startsWith(s))
                return true;
        }
        return false;
    }


    /**
     * @param pageSetting
     * @param method
     * @param sqlParamsMap
     * @param args
     * @return
     * @throws Exception
     */
    private String installDaoMetaData(DaoPage pageSetting, Method method, Map<String, Object> sqlParamsMap, Object[] args) throws Exception {
        String templateSql = null;
        boolean argumentsFlag = method.isAnnotationPresent(Arguments.class);
        if (argumentsFlag) {
            Arguments arguments = method.getAnnotation(Arguments.class);
            LOGGER.debug("@Arguments---------" + Arrays.toString(arguments.value()));
            if (arguments.value().length > args.length) {
                throw new Exception("[注释标签]参数数目，不能大于[方法参数]参数数目");
            }
            int argsNum = 0;
            for (String v : arguments.value()) {
                if (v.equalsIgnoreCase("page")) {
                    pageSetting.setPage(Integer.parseInt(args[argsNum].toString()));
                }
                if (v.equalsIgnoreCase("rows")) {
                    pageSetting.setPage(Integer.parseInt(args[argsNum].toString()));
                }
                sqlParamsMap.put(v, args[argsNum]);
                argsNum++;
            }
        } else {
            if (args != null && args.length > 1) {
                throw new Exception("方法参数数目>=2，方法必须使用注释标签@Arguments");
            } else if (args != null && args.length == 1) {
                sqlParamsMap.put(DaoConstants.SQL_FTL_DTO, args[0]);
            }
        }

        if (method.isAnnotationPresent(Sql.class)) {
            Sql sql = method.getAnnotation(Sql.class);
            // 如果用户采用自定义标签SQL，则SQL文件无效
            if (StringUtil.isNotEmpty(sql.value())) {
                templateSql = sql.value();
            }
            LOGGER.debug("@Sql------------------------------------------" + sql);
        }
        return templateSql;
    }

    private String parseSqlTemplate(Method method, String templateSql, Map<String, Object> sqlParamsMap) {
        String executeSql = null;
        if (StringUtil.isNotEmpty(templateSql)) {
            executeSql = FreemarkerParseFactory.parseTemplateContent(templateSql, sqlParamsMap);
        } else {
            String sqlTempletPath = method.getDeclaringClass().getName().replace(".", "/").replace("/dao/", "/sql/") + "_" + method.getName() + ".sql";
            if (!FreemarkerParseFactory.isExistTemplate(sqlTempletPath)) {
                sqlTempletPath = method.getDeclaringClass().getName().replace(".", "/") + "_" + method.getName() + ".sql";
            }
            LOGGER.debug("Dao-SQL-Path:" + sqlTempletPath);
            executeSql = FreemarkerParseFactory.parseTemplate(sqlTempletPath, sqlParamsMap);
        }
        return executeSql;
    }

    private Map<String, Object> installPlaceholderSqlParam(String executeSql, Map sqlParamsMap) throws OgnlException {
        Map<String, Object> map = new HashMap<String, Object>();
        String regEx = ":[ tnx0Bfr]*[0-9a-z.A-Z]+"; // 表示以：开头，[0-9或者.或者A-Z大小都写]的任意字符，超过一个
        Pattern pat = Pattern.compile(regEx);
        Matcher m = pat.matcher(executeSql);
        while (m.find()) {
            LOGGER.debug(" Match [" + m.group() + "] at positions " + m.start() + "-" + (m.end() - 1));
            String ognl_key = m.group().replace(":", "").trim();
            map.put(ognl_key, Ognl.getValue(ognl_key, sqlParamsMap));
        }
        return map;
    }

    private Object getReturnDaoResult(String dbType, DaoPage pageSetting, Method method, String executeSql, Map<String, Object> paramMap) {
        String methodName = method.getName();
        if (checkActiveKey(methodName)) {
            if (paramMap != null) {
                return namedParameterJdbcTemplate.update(executeSql, paramMap);
            } else {
                return jdbcTemplate.update(executeSql);
            }
        } else if (checkBatchKey(methodName)) {
            return batchUpdate(executeSql);
        } else {
            Class<?> returnType = method.getReturnType();
            if(returnType.isPrimitive()){
                Number number = jdbcTemplate.queryForObject(executeSql, BigDecimal.class);
                if ("int".equals(returnType.getCanonicalName())) {
                    return number.intValue();
                } else if ("long".equals(returnType.getCanonicalName())) {
                    return number.longValue();
                } else if ("double".equals(returnType.getCanonicalName())) {
                    return number.doubleValue();
                }
            } else if(returnType.isAssignableFrom(List.class) || returnType.isAssignableFrom(DaoPage.class)){
                int page = pageSetting.getPage();
                int rows = pageSetting.getRows();
                if (page != 0 && rows != 0) {
                    if(returnType.isAssignableFrom(DaoPage.class)){
                        if(paramMap != null){
                            pageSetting.setTotal(namedParameterJdbcTemplate.queryForObject(getCountSql(executeSql), paramMap, Integer.class));
                        } else{
                            pageSetting.setTotal(jdbcTemplate.queryForObject(getCountSql(executeSql), Integer.class));
                        }
                    }
                    executeSql = DaoUtil.createPageSql(dbType, executeSql, page, rows);
                }
                RowMapper resultType = getListRealType(method);
                List list;
                if (paramMap != null) {
                    list = namedParameterJdbcTemplate.query(executeSql, paramMap, resultType);
                } else {
                    list = jdbcTemplate.query(executeSql, resultType);
                }

                if (returnType.isAssignableFrom(DaoPage.class)) {
                    pageSetting.setResults(list);
                    return pageSetting;
                } else {
                    return list;
                }
            } else if (returnType.isAssignableFrom(Map.class)) {
                // Map类型
                if (paramMap != null) {
                    return namedParameterJdbcTemplate.queryForObject(executeSql, paramMap, getColumnMapRowMapper());
                } else {
                    return jdbcTemplate.queryForObject(executeSql, getColumnMapRowMapper());
                }
            } else if (returnType.isAssignableFrom(String.class)) {
                if (paramMap != null) {
                    return namedParameterJdbcTemplate.queryForObject(executeSql, paramMap, String.class);
                } else {
                    return jdbcTemplate.queryForObject(executeSql, String.class);
                }
            } else if (DaoUtil.isWrapClass(returnType)) {
                if (paramMap != null) {
                    return namedParameterJdbcTemplate.queryForObject(executeSql, paramMap, returnType);
                } else {
                    return jdbcTemplate.queryForObject(executeSql, returnType);
                }
            } else {
                // 对象类型
                RowMapper<?> rm = ParameterizedBeanPropertyRowMapper.newInstance(returnType);
                if (paramMap != null) {
                    return namedParameterJdbcTemplate.queryForObject(executeSql, paramMap, rm);
                } else {
                    return jdbcTemplate.queryForObject(executeSql, rm);
                }
            }
        }

        return null;
    }

    private void addResulArray(int[] result, int index, int[] arr) {
        int length = arr.length;
        for (int i = 0; i < length; i++) {
            result[index - length + i] = arr[i];
        }
    }

    private int[] batchUpdate(String executeSql) {
        String[] sqls = executeSql.split(";");
        if (sqls.length < 100) {
            return jdbcTemplate.batchUpdate(sqls);
        }
        int[] result = new int[sqls.length];
        List<String> sqlList = new ArrayList<String>();
        for (int i = 0; i < sqls.length; i++) {
            sqlList.add(sqls[i]);
            if (i % 100 == 0) {
                addResulArray(result, i + 1, jdbcTemplate.batchUpdate(sqlList.toArray(new String[0])));
                sqlList.clear();
            }
        }
        addResulArray(result, sqls.length, jdbcTemplate.batchUpdate(sqlList.toArray(new String[0])));
        return result;
    }

    private RowMapper<?> getListRealType(Method method) {
        ResultType resultType = method.getAnnotation(ResultType.class);
        if (resultType != null) {
            if (resultType.value().equals(Map.class)) {
                return getColumnMapRowMapper();
            }
            return ParameterizedBeanPropertyRowMapper.newInstance(resultType.value());
        }
        String genericReturnType = method.getGenericReturnType().toString();
        String realType = genericReturnType.replace("java.util.List", "").replace("<", "").replace(">", "");
        if (realType.contains("java.util.Map")) {
            return getColumnMapRowMapper();
        } else if (realType.length() > 0) {
            try {
                return ParameterizedBeanPropertyRowMapper.newInstance(Class.forName(realType));
            } catch (ClassNotFoundException e) {
                LOGGER.error(e.getMessage(), e.fillInStackTrace());
                throw new RuntimeException("dao get class error ,class name is:" + realType);
            }
        }
        return getColumnMapRowMapper();
    }


    private RowMapper<Map<String, Object>> getColumnMapRowMapper() {
        if (getKeyType().equalsIgnoreCase(LOWER_KEY)) {
            return new MiniColumnMapRowMapper();
        } else if (getKeyType().equalsIgnoreCase(UPPER_KEY)) {
            return new ColumnMapRowMapper();
        } else {
            return new MiniColumnOriginalMapRowMapper();
        }
    }

}
