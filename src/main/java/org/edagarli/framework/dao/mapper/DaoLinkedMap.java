package org.edagarli.framework.dao.mapper;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * User: edagarli
 * Email: lizhi@edagarli.com
 * Date: 16/2/24
 * Time: 11:21
 * Desc:
 */
public class DaoLinkedMap extends LinkedHashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    private final Locale locale;

    public DaoLinkedMap() {
        this(((Locale) (null)));
    }

    public DaoLinkedMap(int initialCapacity) {
        this(initialCapacity, null);
    }

    public DaoLinkedMap(int initialCapacity, Locale locale) {
        super(initialCapacity);
        this.locale = locale == null ? Locale.getDefault() : locale;
    }

    public DaoLinkedMap(Locale locale) {
        this.locale = locale == null ? Locale.getDefault() : locale;
    }

    public void clear() {
        super.clear();
    }

    public boolean containsKey(Object key) {
        return (key instanceof String)
                && super.containsKey(convertKey((String) key));
    }

    protected String convertKey(String key) {
        return key.toLowerCase(locale);
    }

    public Object get(Object key) {
        if (key instanceof String)
            return super.get(convertKey((String) key));
        else
            return null;
    }

    public Object put(String key, Object value) {
        return super.put(convertKey(key), value);
    }

    public void putAll(Map map) {
        if (map.isEmpty())
            return;
        java.util.Map.Entry entry;
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); put(
                convertKey((String) entry.getKey()), entry.getValue()))
            entry = (java.util.Map.Entry) iterator.next();

    }

    public Object remove(Object key) {
        if (key instanceof String)
            return super.remove(convertKey((String) key));
        else
            return null;
    }

}
