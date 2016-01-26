package org.edagarli.framework.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * User: lurou
 * Email: lurou@2dfire.com
 * Date: 16/1/26
 * Time: 10:41
 * Desc:
 */
public class ProxyManager {

    public static <T> T createProxy(final Class<?> targetClass,final List<Proxy> proxyList){
        return  (T) Enhancer.create(targetClass, new MethodInterceptor() {
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                 return new ProxyChain(targetClass, obj, method, proxy, args, proxyList).doProxyChain();
            }
        });
    }
}
