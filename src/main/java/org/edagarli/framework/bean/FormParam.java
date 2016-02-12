package org.edagarli.framework.bean;

/**
 * User: lurou
 * Email: lurou@2dfire.com
 * Date: 16/2/12
 * Time: 20:35
 * Desc:
 */
public class FormParam {

    private String fieldName;
    private Object fieldValue;

    public FormParam(String fieldName, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

}
