package org.smart4j.framework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * User: lurou
 * Email: lurou@2dfire.com
 * Date: 16/1/26
 * Time: 10:50
 * Desc:
 */
public class AspectProxy implements Proxy{

    private static final Logger LOGGER = LoggerFactory.getLogger(AspectProxy.class);

    public Object doProxy(ProxyChain proxyChain) throws Throwable {

        Object result = null;

        Class<?> cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();


        return null;
    }

    public void begin(){

    }

    public boolean intercept(Class<?> cls, Method method, Object[] params) throws Throwable{
        return true;
    }

    public void before(Class<?> cls, Method method, Object[] params) throws Throwable {

    }

    public void after(Class<?> cls, Method method, Object[] params) throws Throwable {

    }

    public void error(Class<?> cls, Method method, Object[] params) throws Throwable {

    }

    public void end(){

    }
}
