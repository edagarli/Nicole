package org.edagarli.framework.annotation;

import java.lang.annotation.*;

/**
 * User: edagarli
 * Email: lizhi@edagarli.com
 * Date: 16/2/24
 * Time: 10:55
 * Desc:
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ResultType {

    Class<?> value();

}
