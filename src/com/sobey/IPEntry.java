package com.sobey;

import java.io.UnsupportedEncodingException;

/**
 * Created by Yanggang.
 * Date: 11-1-21
 * Time: 下午3:44
 * To change this template use File | Settings | File Templates.
 */
public class IPEntry {
    public String beginIp;
    public String endIp;
    public String country;
    public String area;

    /**
     * 构造函数
     */


    public IPEntry() {
        beginIp = endIp = country = area = "";
    }

    public String toString() {
        return this.area + "  " + this.country + "IP范围:" + this.beginIp + "-" + this.endIp;
    }
}