package com.sobey.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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

    public static String byteToUnit(Double value, String[] unit, String toUnit) {
        //Byte >> GB
        Double toGB = 1024 * 1024 * 1024 * 1d;
        Double toMB = 1024 * 1024 * 1d;
        Double toKB = 1024 * 1d;
        if (toUnit != null) {
            if (toUnit.equals("KB")) {
                return String.format("%.2f", Arith.div(value, toKB, 2));
            }
            if (toUnit.equals("MB")) {
                return String.format("%.2f", Arith.div(value, toMB, 2));
            }
            if (toUnit.equals("GB")) {
                return String.format("%.2f", Arith.div(value, toGB, 2));
            }
            if (toUnit.equals("Byte")) {
                return String.format("%.2f", Arith.div(value, 1, 2));
            }
        }
        if (value < 1024d) {
            unit[0] = "Byte";
            return String.format("%.2f", Arith.div(value, 1, 2));
        } else if (value > 1024 && value < 1048576d) {
            unit[0] = "KB";
            return String.format("%.2f", Arith.div(value, toKB, 2));
        } else if (value > 1048576d && value < 1073741824d) {
            unit[0] = "MB";
            return String.format("%.2f", Arith.div(value, toMB, 2));
        } else if (value > 1073741824d) {
            unit[0] = "GB";
            return String.format("%.2f", Arith.div(value, toGB, 2));
        } else {
            unit[0] = "NaN";
            return "0";
        }
    }

    public static String byteToGB(Double value) {
        //Byte >> GB
        Double toGB = 1024 * 1024 * 1024 * 1d;
        return String.format("%.2f", Arith.div(value, toGB, 2));

    }

    public static String getHost(String url) {
        url = url.replace("http://", "").replace("https://", "");
        try {
            url = url.split("/")[0];
        } catch (Exception ignored) {
        }
        return url;
    }

    public static String getURI(String url) {
        url = url.replace("http://", "").replace("https://", "");
        try {
            String str[] = url.split("/");
            return "/" + str[1];
        } catch (Exception ignored) {
        }
        return "/";
    }

    public static String FormetFileSize(long fileS) {//转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static long getFileSizes(File f) throws Exception{//取得文件大小
        long s=0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s= fis.available();
        } else {
            f.createNewFile();
            System.out.println("文件不存在");
        }
        return s;
    }
}
