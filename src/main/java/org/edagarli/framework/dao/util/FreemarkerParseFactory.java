package org.edagarli.framework.dao.util;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * User: edagarli
 * Email: lizhi@edagarli.com
 * Date: 16/2/23
 * Time: 11:00
 * Desc:
 */
public class FreemarkerParseFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(FreemarkerParseFactory.class);

    private static final String ENCODE = "UTF-8";

    private static final Configuration _tplConfig = new Configuration();

    private static final Configuration _sqlConfig = new Configuration();

    private static StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();

    // 使用内嵌的(?ms)打开单行和多行模式
    private final static Pattern p = Pattern
            .compile("(?ms)/\\*.*?\\*/|^\\s*//.*?$");


    static {
        _tplConfig.setClassForTemplateLoading(
                new FreemarkerParseFactory().getClass(), "/");
        _tplConfig.setNumberFormat("0.#####################");
        _sqlConfig.setTemplateLoader(stringTemplateLoader);
        _sqlConfig.setNumberFormat("0.#####################");
    }

    public static boolean isExistTemplate(String tplName) {
        try {
            Template mytpl = _tplConfig.getTemplate(tplName, ENCODE);
            if (mytpl == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public static String parseTemplate(String tplName, Map<String, Object> paras) {
        try {
            StringWriter swriter = new StringWriter();
            Template mytpl = _tplConfig.getTemplate(tplName, ENCODE);
            mytpl.process(paras, swriter);
            return getSqlText(swriter.toString());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e.fillInStackTrace());
            LOGGER.error("发送一次的模板key:{ " + tplName + " }");
            throw new RuntimeException("解析SQL模板异常");
        }
    }

    /**
     * 解析ftl
     * @param tplContent
     * @param paras
     * @return
     */
    public static String parseTemplateContent(String tplContent,
                                              Map<String, Object> paras) {
        try {
            StringWriter swriter = new StringWriter();
            if(stringTemplateLoader.findTemplateSource("sql_" + tplContent.hashCode()) ==  null){
                stringTemplateLoader.putTemplate("sql_" + tplContent.hashCode(), tplContent);
            }
            Template mytpl = _sqlConfig.getTemplate("sql_" + tplContent.hashCode(), ENCODE);
            mytpl.process(paras, swriter);
            return null;
        } catch (Exception e){
            LOGGER.error(e.getMessage(),e.fillInStackTrace());
            LOGGER.error("发送一次的模板key:{ "+ tplContent +" }");
            throw new RuntimeException("解析SQL模板异常");
        }
    }

    private static String getSqlText(String sql) {
        // 将注释替换成""
        sql = p.matcher(sql).replaceAll("");
        sql = sql.replaceAll("\\n", " ").replaceAll("\\t", " ")
                .replaceAll("\\s{1,}", " ").trim();
        // 去掉 最后是 where这样的问题
        if (sql.endsWith("where") || sql.endsWith("where ")) {
            sql = sql.substring(0, sql.lastIndexOf("where"));
        }
        // 去掉where and 这样的问题
        int index = 0;
        while ((index = StringUtils.indexOfIgnoreCase(sql, "where and", index)) != -1) {
            sql = sql.substring(0, index + 5)
                    + sql.substring(index + 9, sql.length());
        }
        // 去掉 , where 这样的问题
        index = 0;
        while ((index = StringUtils.indexOfIgnoreCase(sql, ", where", index)) != -1) {
            sql = sql.substring(0, index)
                    + sql.substring(index + 1, sql.length());
        }
        // 去掉 最后是 ,这样的问题
        if (sql.endsWith(",") || sql.endsWith(", ")) {
            sql = sql.substring(0, sql.lastIndexOf(","));
        }
        return sql;
    }
}
