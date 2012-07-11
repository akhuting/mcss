package com.sobey.mcss.service;

import com.sobey.mcss.dao.BroadBandStatDao;
import com.sobey.mcss.domain.Broadbandstat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Yanggang.
 * Date: 11-1-24
 * Time: 上午11:11
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class BroadBandStatService {
    @Autowired
    private BroadBandStatDao broadBandStatDao;

    public List<Broadbandstat> getBroadbandstatList(String cp, String type, String beginTime, String endTime) {
        StringBuffer hql = new StringBuffer(" from Broadbandstat where cp = ?");
        if (type != null && !type.equals("")) {
            hql.append(" and type = '").append(type).append("'");
        }
        if (beginTime != null && !beginTime.equals("")) {
            hql.append(" and dateTime >= '").append(beginTime).append("'");
        }
        if (endTime != null && !endTime.equals("")) {
            hql.append(" and dateTime <='").append(endTime).append("'");
        }
        hql.append(" order by dateTime");
        return broadBandStatDao.query(hql.toString(), cp);
    }


    public List getBroadbandstatListByDay(String cp, String type, String beginTime, String endTime) {
        StringBuffer sql = new StringBuffer("SELECT DATE(a.datetime) AS `date`, a.`datetime` AS datetime ,a.broadband AS broadband,");
        sql.append(" (SELECT SUM(`BroadBand`) FROM `broadbandstat` WHERE DATE(a.datetime)=DATE(datetime)) AS sumbroadband ");
        sql.append(" FROM `broadbandstat` a ");
        sql.append(" WHERE NOT EXISTS(SELECT 1 FROM `broadbandstat`  WHERE DATE(a.datetime)=DATE(datetime) AND a.`BroadBand`<`BroadBand`) ");
        sql.append(" and a.cp = ? and a.type=? and datetime >= ? and datetime <=?");
        sql.append(" ORDER BY date");
        return broadBandStatDao.querySql(sql.toString(), cp, type, beginTime, endTime);
    }

    public void saveBroadbandstat(String cp, String type, String dateTime, long value) {
        Broadbandstat broadbandstat = new Broadbandstat();
        broadbandstat.setBroadband(value);
        broadbandstat.setCp(cp);
        broadbandstat.setDatetime(dateTime);
        broadbandstat.setType(type);
        broadBandStatDao.save(broadbandstat);
    }

    public List getBroadbandstatListBySql(String sql, Object... value) {
        return broadBandStatDao.querySql(sql, value);
    }

    public List getBroadbandstatListByHour(String cp, String type, String begin, String end){
        StringBuffer sql = new StringBuffer("SELECT id,cp,type,datetime,broadband FROM broadbandstat where 1=1");
        sql.append(" and " + cp);
        sql.append(" and type = ?");
        sql.append(" and DateTime >= ?");
        sql.append(" and  DateTime <= ?");
        sql.append(" ORDER BY datetime");
        return broadBandStatDao.querySql(sql.toString(), new Object[]{ type, begin, end});
    }
}
