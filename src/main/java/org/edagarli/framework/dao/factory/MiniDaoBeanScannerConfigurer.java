package org.edagarli.framework.dao.factory;

import org.edagarli.framework.annotation.Dao;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.lang.annotation.Annotation;

/**
 * User: edagarli
 * Email: lizhi@edagarli.com
 * Date: 16/2/20
 * Time: 22:57
 * Desc: 扫描配置文件
 */
public class MiniDaoBeanScannerConfigurer implements BeanDefinitionRegistryPostProcessor {

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

    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }


}
