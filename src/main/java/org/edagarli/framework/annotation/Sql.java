package org.edagarli.framework.annotation;

import java.lang.annotation.*;

/**
 * User: edagarli
 * Email: lizhi@edagarli.com
 * Date: 16/2/23
 * Time: 10:45
 * Desc:
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sql {

    String value();

}
