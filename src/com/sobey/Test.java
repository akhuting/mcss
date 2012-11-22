package com.sobey;

import com.sobey.common.util.*;
import com.sobey.mcss.domain.Daystatitem;
import com.sobey.mcss.service.*;
import net.sf.json.JSONArray;
import org.dom4j.DocumentException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;


/**
 * Created by Yanggang.
 * Date: 11-1-8
 * Time: 下午11:53
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String args[]) throws DocumentException {
        HttpUtil.sendPost("http://localhost:8080/FlowService", "{cp:'113.142.30.222',begin:'2012-10-1' , end : '2012-10-7'}");

    }
}
