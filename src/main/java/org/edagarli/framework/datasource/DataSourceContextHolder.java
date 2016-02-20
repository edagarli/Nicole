package org.edagarli.framework.datasource;

/**
 * User: edagarli
 * Email: lizhi@edagarli.com
 * Date: 16/2/20
 * Time: 21:57
 * Desc:获得和设置上下文环境的类，主要负责改变上下文数据源的名称
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<DataSourceType> contextHolder = new ThreadLocal<DataSourceType>();


    public static void clearDataSourceType() {
        contextHolder.remove();
    }

    public static DataSourceType getDataSourceType() {
        return (DataSourceType) contextHolder.get();
    }

    public static void setDataSourceType(DataSourceType dataSourceType) {
        contextHolder.set(dataSourceType);
    }
}
