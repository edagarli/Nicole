package org.edagarli.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;

/**
 * User: edagarli
 * Email: lizhi@edagarli.com
 * Date: 16/2/24
 * Time: 10:26
 * Desc:
 */
public class DaoUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DaoUtil.class);

    /**
     * 数据库类型
     */
    public static final String DATABSE_TYPE_MYSQL = "mysql";
    public static final String DATABSE_TYPE_POSTGRE = "postgresql";
    public static final String DATABSE_TYPE_ORACLE = "oracle";
    public static final String DATABSE_TYPE_SQLSERVER = "sqlserver";

    /**
     * 分页SQL
     */
    public static final String MYSQL_SQL = "select * from ( {0}) sel_tab00 limit {1},{2}"; // mysql
    public static final String POSTGRE_SQL = "select * from ( {0}) sel_tab00 limit {2} offset {1}";// postgresql
    public static final String ORACLE_SQL = "select * from (select row_.*,rownum rownum_ from ({0}) row_ where rownum <= {1}) where rownum_>{2}"; // oracle
    public static final String SQLSERVER_SQL = "select * from ( select row_number() over(order by tempColumn) tempRowNumber, * from (select top {1} tempColumn = 0, {0}) t ) tt where tempRowNumber > {2}"; // sqlserver

    public static String createPageSql(String dbType, String sql, int page,
                                       int rows) {
        int beginNum = (page - 1) * rows;
        String[] sqlParam = new String[3];
        sqlParam[0] = sql;
        sqlParam[1] = beginNum + "";
        sqlParam[2] = rows + "";
        String jdbcType = dbType;
        if (jdbcType == null || "".equals(jdbcType)) {
            throw new RuntimeException(
                    "org.edagarli.framework.dao.aop.MiniDaoHandler:(数据库类型:dbType)没有设置,请检查配置文件");
        }
        if (jdbcType.indexOf(DATABSE_TYPE_MYSQL) != -1) {
            sql = MessageFormat.format(MYSQL_SQL, sqlParam);
        } else if (jdbcType.indexOf(DATABSE_TYPE_POSTGRE) != -1) {
            sql = MessageFormat.format(POSTGRE_SQL, sqlParam);
        }  else {
            int beginIndex = (page - 1) * rows;
            int endIndex = beginIndex + rows;
            sqlParam[2] = Integer.toString(beginIndex);
            sqlParam[1] = Integer.toString(endIndex);
            if (jdbcType.indexOf(DATABSE_TYPE_ORACLE) != -1) {
                sql = MessageFormat.format(ORACLE_SQL, sqlParam);
            } else if (jdbcType.indexOf(DATABSE_TYPE_SQLSERVER) != -1) {
                sqlParam[0] = sql.substring(getAfterSelectInsertPoint(sql));
                sql = MessageFormat.format(SQLSERVER_SQL, sqlParam);
            }
        }
        return sql;
    }

    private static int getAfterSelectInsertPoint(String sql) {
        int selectIndex = sql.toLowerCase().indexOf("select");
        int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct");
        return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
    }

    public static String getMethodSqlLogicJar(String sqlurl) {
        StringBuffer sb = new StringBuffer();
        InputStream is = DaoUtil.class.getResourceAsStream(sqlurl);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String s = "";
        try {
            while ((s = br.readLine()) != null)
                sb.append(s + " ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getFirstSmall(String name) {
        name = name.trim();
        if (name.length() >= 2) {
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        } else {
            return name.toLowerCase();
        }

    }

    public static boolean isAbstract(Method method) {
        int mod = method.getModifiers();
        return Modifier.isAbstract(mod);
    }

    public static boolean isWrapClass(Class<?> clz) {
        try {
            return ((Class<?>) clz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }
}
