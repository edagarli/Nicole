package org.edagarli.framework.proxy;

/**
 * User: lurou
 * Email: lurou@2dfire.com
 * Date: 16/1/26
 * Time: 10:37
 * Desc:
 */
public interface Proxy {

    /**
     * 执行链式代理
     * @param proxyChain
     * @return
     * @throws Throwable
     */
    Object doProxy(ProxyChain proxyChain) throws Throwable;

}
