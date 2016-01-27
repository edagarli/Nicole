package org.edagarli.framework.bean;

import org.edagarli.framework.util.CastUtil;
import org.edagarli.framework.util.CollectionUtil;

import java.util.Map;

/**
 * User: lurou
 * Email: lurou@2dfire.com
 * Date: 16/1/23
 * Time: 10:35
 * Desc:
 */
public class Param {
    private Map<String,Object> paramMap;

    public Param(Map<String,Object> paramMap){
        this.paramMap = paramMap;
    }

    /**
     * 根据参数名获取long型参数值
     */
    public long getLong(String name){
        return CastUtil.castLong(paramMap.get(name));
    }

    /**
     * 获取所有字断信息
     */
    public Map<String,Object> getMap(){
        return paramMap;
    }

    /**
     * 验证参数是否为空
     */
    public boolean isEmpty(){
        return CollectionUtil.isEmpty(paramMap);
    }
}
