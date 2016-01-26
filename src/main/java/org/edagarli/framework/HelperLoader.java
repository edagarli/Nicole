package org.edagarli.framework;

import org.edagarli.framework.helper.*;
import org.edagarli.framework.util.ClassUtil;

/**
 * User: lurou
 * Email: lurou@2dfire.com
 * Date: 16/1/22
 * Time: 21:55
 * Desc:
 */
public class HelperLoader {
    public static void init() {
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                AopHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };
        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName());
        }
    }
}
