package com.sobey.common.util;

import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: AlexYang
 * Date: 11-3-28
 * Time: 上午11:11
 * To change this template use File | Settings | File Templates.
 */
public class PropertiesUtil {
    private static ResourceBundle rb;

    static {
        rb = ResourceBundle.getBundle("config");
    }

    public static String getString(String key) {
        return rb.getString(key);
    }
}
