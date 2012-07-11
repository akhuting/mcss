package com.sobey.common.util;

/**
 * Created by Yanggang.
 * Date: 11-1-18
 * Time: 上午10:39
 * To change this template use File | Settings | File Templates.
 */
public class R {

    /**
     * @param max
     * @return
     */
    public static int randomInt(int max) {
        long _time = System.currentTimeMillis();
        String time = String.valueOf(_time);
        if (time.length() >= max) {
            return Integer.parseInt(String.valueOf((_time << Integer.
                    parseInt(time.substring(time.length() - 1)))).
                    substring(0, max));
        }
        return (int) _time;
    }

    public static double randomDouble(int min, int max) {
        double r = 0;
        while (r < min) {
            r = Math.random() * max;
            if (min == max) {
                return r;
            }
        }
        return r;
    }

}
