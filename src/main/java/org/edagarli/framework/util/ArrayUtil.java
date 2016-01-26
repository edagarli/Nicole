package org.edagarli.framework.util;

import org.apache.commons.lang3.ArrayUtils;

/**
 * User: lurou
 * Email: lurou@2dfire.com
 * Date: 16/1/22
 * Time: 17:54
 * Desc:
 */
public class ArrayUtil {

    /**
     * 判断数组是否非空
     */
    public static boolean isNotEmpty(Object[] array) {
        return !ArrayUtils.isEmpty(array);
    }

    /**
     * 判断数组是否为空
     */
    public static boolean isEmpty(Object[] array) {
        return ArrayUtils.isEmpty(array);
    }
}
