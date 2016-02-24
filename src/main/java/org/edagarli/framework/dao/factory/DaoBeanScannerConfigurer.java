package org.edagarli.framework.dao.factory;

import org.edagarli.framework.annotation.Dao;
import org.edagarli.framework.dao.aop.DaoHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;

/**
 * User: edagarli
 * Email: lizhi@edagarli.com
 * Date: 16/2/20
 * Time: 22:57
 * Desc: 扫描配置文件
 */
public class DaoBeanScannerConfigurer implements BeanDefinitionRegistryPostProcessor {

    private String basePackage;

    /**
     * 默认是Dao,推荐使用Repository
     */
    private Class<? extends Annotation> annotation = Dao.class;

    /**
     * Map key类型
     */
    private String keyType = "origin";
    /**
     * 是否格式化sql
     */
    private boolean formatSql = false;
    /**
     * 是否输出sql
     */
    private boolean showSql = false;
    /**
     * 数据库类型
     */
    private String dbType;

    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        registerRequestProxyHandler(registry);

        DaoClassPathMapperScanner scanner = new DaoClassPathMapperScanner(registry, annotation);

        scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    private void registerRequestProxyHandler(BeanDefinitionRegistry registry) {
        GenericBeanDefinition jdbcDaoProxyDefinition = new GenericBeanDefinition();
        jdbcDaoProxyDefinition.setBeanClass(DaoHandler.class);
        jdbcDaoProxyDefinition.getPropertyValues().add("formatSql", formatSql);
        jdbcDaoProxyDefinition.getPropertyValues().add("keyType", keyType);
        jdbcDaoProxyDefinition.getPropertyValues().add("showSql", showSql);
        jdbcDaoProxyDefinition.getPropertyValues().add("dbType", dbType);
        registry.registerBeanDefinition("daoHandler", jdbcDaoProxyDefinition);
    }

    public void setAnnotation(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public void setFormatSql(boolean formatSql) {
        this.formatSql = formatSql;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public void setShowSql(boolean showSql) {
        this.showSql = showSql;
    }


}
