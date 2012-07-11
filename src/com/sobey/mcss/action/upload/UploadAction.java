package com.sobey.mcss.action.upload;

import com.opensymphony.xwork2.ActionSupport;
import com.sobey.common.dao.QueryResult;
import com.sobey.common.util.DateUtil;
import com.sobey.common.util.StringUtil;
import com.sobey.mcss.domain.Uploadstat;
import com.sobey.mcss.service.UploadStatService;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yanggang.
 * Date: 11-2-22
 * Time: 上午11:03
 * To change this template use File | Settings | File Templates.
 */
@Controller
@Results(@Result(name = "uploadStat", location = "uploadStat.jsp"))
public class UploadAction extends ActionSupport {

    private String beginTime;
    private String endTime;
    private Map result = new HashMap();
    private int pageNum = 1;
    private String cp;

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    @Autowired
    private UploadStatService uploadStatService;

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public Map getResult() {
        return result;
    }

    public String uploadStat() {
        if (StringUtil.checkEmpty(beginTime)) {
            beginTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        if (StringUtil.checkEmpty(endTime)) {
            endTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        boolean find = false;
        StringBuffer charts = new StringBuffer();
        StringBuffer data = new StringBuffer();
        StringBuffer avg = new StringBuffer();
        charts.append("<chart palette='2' caption='上传加速数据分析统计' showValues='0' divLineDecimalPrecision='1' limitsDecimalPrecision='1' YAxisName='流量(GB)' XAxisName='日期'  formatNumberScale='0'>");
        data.append("<dataset seriesName='流量'>");
        avg.append("<dataset seriesName='均值带宽' renderAs='Line'>");
        int count = 0;
        List countList = uploadStatService.getUploadStatsBySql(" select count(*) from uploadstat where begintime >=? and endtime <= ? and cp = ? order by begintime", beginTime, endTime, cp);
        if (countList != null && countList.size() > 0) {
            count = Integer.parseInt(countList.get(0).toString());
        }
//        if (count == 0) {
//            return "uploadStat";
//        }
        QueryResult queryResult = uploadStatService.getUploadStats(cp, beginTime, endTime, pageNum, 20);
        queryResult.setPager(queryResult.getPager().setRecordCount(count));
        if (queryResult != null) {
            result.put("result", queryResult);
            List<Uploadstat> list = (List<Uploadstat>) queryResult.getList();
            charts.append("<categories>");
            for (Uploadstat uploadstat : list) {
                if (!find) {
                    find = true;
                }
                charts.append("<category label='").append(DateUtil.getSpecificTime(beginTime, DateUtil.DAY_OF_MONTH)).append("'/>");
                data.append("<set value='").append(uploadstat.getDataflow()).append("'/>");
                avg.append("<set value='").append("100").append("'/>");
            }
            if (!find) {
                charts.append("<category label='").append(DateUtil.getSpecificTime(beginTime, DateUtil.DAY_OF_MONTH)).append("'/>");
                data.append("<set value='").append("0").append("'/>");
                avg.append("<set value='").append("0").append("'/>");
            }
            charts.append("</categories>");
            data.append("</dataset>");
            avg.append("</dataset>");
            charts.append(data);
            charts.append(avg);
            charts.append("</chart>");
            result.put("xml", charts.toString());
        }
        return "uploadStat";
    }
}
