package com.sobey.mcss;

import com.sobey.common.util.DateUtil;
import com.sobey.common.util.PropertiesUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-1-29
 * Time: 下午6:00
 * To change this template use File | Settings | File Templates.
 */
public class Scheduler {
    Logger logger = Logger.getLogger(getClass());

    public static void main(String args[]) {
        Scheduler scheduler = new Scheduler();
        scheduler.clearLogFile();
    }

    /**
     * 定时删除日志文件
     */
    public void clearLogFile() {
        logger.info("开始定时清理日志文件....");
        String url = PropertiesUtil.getString("LOGFILE.URL");
        ArrayList<String> delFiles = new ArrayList<String>();
        File file = new File(url);
        if (!file.exists()) {
            return;
        }
        for (File log : file.listFiles()) {
            if (log.isFile()) {
                String[] str = log.getName().split("_");
                if (str.length == 3) {
                    if (validate(str[2])) {
                        delFiles.add(log.getAbsolutePath());
                    }
                } else {
                    delFiles.add(log.getAbsolutePath());
                }
            }
        }
        logger.info("找到" + delFiles.size() + "个过期的日志文件....");
        for (String s : delFiles) {
            try {
                File delFile = new File(s);
                delFile.delete();
                logger.info("成功删除日志文件:" + s);
            } catch (Exception e) {
                logger.warn("删除日志文件:" + s + "失败", e);
            }
        }
    }


    private boolean validate(String date) {

        try {
            date = date.replace(".log", "");
            int year = Integer.parseInt(date.substring(0, 4));
            int month = Integer.parseInt(date.substring(4, 6));
            int day = Integer.parseInt(date.substring(6, 8));

            Calendar nowCalendar = Calendar.getInstance();
            Calendar compareCalendar = Calendar.getInstance();
            compareCalendar.set(year, month - 1, day);

            long add = (nowCalendar.getTime().getTime() - compareCalendar.getTime().getTime()) / (24 * 60 * 60 * 1000);
            return add > 7;
        } catch (Exception e) {
            return true;
        }
    }
}
