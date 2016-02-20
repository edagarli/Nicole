package org.edagarli.framework.annotation;

import java.lang.annotation.*;

/**
 * User: lurou
 * Email: lurou@2dfire.com
 * Date: 16/2/20
 * Time: 20:35
 * Desc:
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Dao {

    String value() default "";

}
