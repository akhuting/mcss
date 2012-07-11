package com.sobey.common.util;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Yanggang.
 * Date: 11-1-18
 * Time: 上午11:34
 * To change this template use File | Settings | File Templates.
 */
public class StringUtil {
    public static void print(Object print) {
        System.out.print(print);
    }

    public static void println(Object print) {
        System.out.println(print);
    }

    public static boolean checkEmpty(String str) {
        if (str == null || str.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public static Map.Entry[] sortMapByValue(Map map) {
        Set set = map.entrySet();
        Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
        Arrays.sort(entries, new Comparator() {
            public int compare(Object o1, Object o2) {
                Long value1 = null;
                Long value2 = null;
                try {
                    value1 = Long.parseLong(((Map.Entry) o1).getValue().toString());
                } catch (Exception e) {
                    value1 = Long.valueOf(0);
                }
                try {
                    value2 = Long.parseLong(((Map.Entry) o2).getValue().toString());
                } catch (Exception e) {
                    value2 = Long.valueOf(0);
                }
                return value2.compareTo(value1);
            }
        });
        return entries;
    }

    public static Map.Entry[] sortMapByValueDouble(Map map) {
        Set set = map.entrySet();
        Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
        Arrays.sort(entries, new Comparator() {
            public int compare(Object o1, Object o2) {
                Double value1 = null;
                Double value2 = null;
                try {
                    value1 = Double.parseDouble(((Map.Entry) o1).getValue().toString());
                } catch (Exception e) {
                    value1 = Double.valueOf(0);
                }
                try {
                    value2 = Double.parseDouble(((Map.Entry) o2).getValue().toString());
                } catch (Exception e) {
                    value2 = Double.valueOf(0);
                }
                return value2.compareTo(value1);
            }
        });
        return entries;
    }

    public static Double div(Double v1, Double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static int divide(Double v1, Double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).intValue();
    }
}
