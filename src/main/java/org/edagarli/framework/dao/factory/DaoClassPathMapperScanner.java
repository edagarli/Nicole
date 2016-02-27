package org.edagarli.framework.dao.factory;

import org.edagarli.framework.annotation.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

/**
 * User: edagarli
 * Email: lizhi@edagarli.com
 * Date: 16/2/24
 * Time: 11:43
 * Desc:
 */
public class DaoClassPathMapperScanner extends ClassPathBeanDefinitionScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DaoClassPathMapperScanner.class);
    public DaoClassPathMapperScanner(BeanDefinitionRegistry registry, Class<? extends Annotation> annotation) {
        super(registry, false);
        addIncludeFilter(new AnnotationTypeFilter(annotation));
        if (!Dao.class.equals(annotation)) {
            addIncludeFilter(new AnnotationTypeFilter(Dao.class));
        }
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty()) {
            LOGGER.warn("No Dao interface was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        }
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            definition.getPropertyValues().add("proxy", getRegistry().getBeanDefinition("daoHandler"));
            definition.getPropertyValues().add("daoInterface", definition.getBeanClassName());
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("register dao name is { " + definition.getBeanClassName() + " }");
            }
            definition.setBeanClass(DaoBeanFactory.class);
        }

        return beanDefinitions;
    }

    /**
     * 默认不允许接口的,这里重写,覆盖下,另外默认会Scan @Component 这样所以的被@Component 注解的都会Scan
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

}
