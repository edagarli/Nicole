package org.edagarli.framework.datasource;

import java.util.Map;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;
/**
 * User: edagarli
 * Email: lizhi@edagarli.com
 * Date: 16/2/20
 * Time: 22:02
 * Desc: 动态数据源类
 */
public class DynamicDataSource extends AbstractRoutingDataSource{

    /*
	 * 该方法必须要重写 方法是为了根据数据库标示符取得当前的数据库
	 */
    @Override
    protected Object determineCurrentLookupKey() {
        DataSourceType dataSourceType = DataSourceContextHolder
                .getDataSourceType();
        return dataSourceType;
    }

    @Override
    public void setDataSourceLookup(DataSourceLookup dataSourceLookup) {
        super.setDataSourceLookup(dataSourceLookup);
    }

    @Override
    public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
    }

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
    }

}
