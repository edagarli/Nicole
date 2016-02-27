package org.edagarli.framework.dao.def;

/**
 * User: edagarli
 * Email: lizhi@edagarli.com
 * Date: 16/2/23
 * Time: 10:41
 * Desc:
 */
public class DaoConstants {

    /**
     * 接口方法定義規則 添加：insert,add,create 添加：update,modify,store 刪除：delete,remove
     * 檢索：以上各单词之外
     */
    public static final String INF_METHOD_ACTIVE = "insert,add,create,update,modify,store,delete,remove";

    public static final String INF_METHOD_BATCH = "batch";

    /**
     * 方法有且只有一个参数 用户未使用@Arguments 标签 模板中引用参数默认为：dto
     */
    public static final String SQL_FTL_DTO = "dto";

}
