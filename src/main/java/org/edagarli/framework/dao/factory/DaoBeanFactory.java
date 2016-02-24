package org.edagarli.framework.dao.factory;

import org.edagarli.framework.dao.aop.DaoHandler;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * User: edagarli
 * Email: lizhi@edagarli.com
 * Date: 16/2/24
 * Time: 11:41
 * Desc:
 */
public class DaoBeanFactory<T> implements FactoryBean<T>{

    private Class daoInterface;

    private DaoHandler proxy;

    public T getObject() throws Exception {
        return newInstance();
    }

    public Class<?> getObjectType() {
        return daoInterface;
    }

    public DaoHandler getProxy() {
        return proxy;
    }

    public boolean isSingleton() {
        return true;
    }

    @SuppressWarnings("unchecked")
    private T newInstance() {
        return (T) Proxy.newProxyInstance(daoInterface.getClassLoader(), new Class[] { daoInterface }, proxy);
    }

    public void setProxy(DaoHandler proxy) {
        this.proxy = proxy;
    }

    public void setDaoInterface(Class daoInterface) {
        this.daoInterface = daoInterface;
    }
}
