package org.smart4j.framework.bean;

import org.smart4j.framework.util.CastUtil;

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

    
}
