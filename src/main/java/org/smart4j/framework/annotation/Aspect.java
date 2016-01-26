package org.smart4j.framework.annotation;

import java.lang.annotation.*;

/**
 * User: lurou
 * Email: lurou@2dfire.com
 * Date: 16/1/26
 * Time: 10:18
 * Desc:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    /**
     * 注解
     */
    Class <? extends Annotation> value();

}
