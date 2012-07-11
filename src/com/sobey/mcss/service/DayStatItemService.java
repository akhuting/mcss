package com.sobey.mcss.service;

import com.sobey.common.dao.QueryResult;
import com.sobey.common.util.DateUtil;
import com.sobey.common.util.MirrorUtil;
import com.sobey.common.util.R;
import com.sobey.mcss.dao.DayStatItemDao;
import com.sobey.mcss.domain.Cp;
import com.sobey.mcss.domain.Daystatitem;
import com.sobey.mcss.domain.id.DaystatitemPK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Yanggang.
 * Date: 11-1-17
 * Time: 下午3:17
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class DayStatItemService {

    @Autowired
    private DayStatItemDao dayStatItemDao;

    public void saveDayStatItem(String cp, String type, String subType, String item, String... date) {
        Map<String, Integer> counts = new HashMap<String, Integer>();
         Random random = new Random();
        for (int i = 0; i < 31; i++) {
            counts.put("count" + (i + 1),
                    Math.abs(random.nextInt())%100);
        }
        DaystatitemPK pk = new DaystatitemPK();
        pk.setCp(cp);
        pk.setItem(item);
        if (date.length > 0) {
            pk.setPeriod(date[0]);
        } else {
            pk.setPeriod(DateUtil.getCurrentTime(DateUtil.YY_MM));
        }
        pk.setType(type);
        pk.setSubtype(subType);
        if (dayStatItemDao.fetch(pk) != null) {
            throw new RuntimeException(String.format("Class %s Already exists in the database", Daystatitem.class.getSimpleName()));
        }
        Daystatitem daystatitem = MirrorUtil.getObject(Daystatitem.class, counts);
        daystatitem.setCp(cp);
        daystatitem.setType(type);
        daystatitem.setSubtype(subType);
        daystatitem.setItem(item);
        if (date.length > 0) {
            daystatitem.setPeriod(date[0]);
        } else {
            daystatitem.setPeriod(DateUtil.getCurrentTime(DateUtil.YY_MM));
        }
        dayStatItemDao.save(daystatitem);
    }

    public void saveDayStatItem(String cp, String type, String subType, String item, String date, Map<String, Integer> map) {
        DaystatitemPK pk = new DaystatitemPK();
        pk.setCp(cp);
        pk.setItem(item);
        pk.setType(type);
        pk.setSubtype(subType);
        pk.setPeriod(date);
        if (dayStatItemDao.fetch(pk) != null) {
            throw new RuntimeException(String.format("Class %s Already exists in the database", Daystatitem.class.getSimpleName()));
        }
        Daystatitem daystatitem = MirrorUtil.getObject(Daystatitem.class, map);
        daystatitem.setCp(cp);
        daystatitem.setType(type);
        daystatitem.setSubtype(subType);
        daystatitem.setItem(item);
        daystatitem.setPeriod(date);
        dayStatItemDao.save(daystatitem);
    }

    public void saveDayStatItem(Daystatitem daystatitem) {
        dayStatItemDao.save(daystatitem);
    }

    public void updateDayStatItem(String cp, String type, String subType, String item, String date) {
        Map<String, Integer> counts = new HashMap<String, Integer>();
        for (int i = 0; i < 31; i++) {
            counts.put("count" + (i + 1),
                    R.randomInt(R.randomInt(1)));
        }
        Daystatitem daystatitem = MirrorUtil.getObject(Daystatitem.class, counts);
        daystatitem.setCp(cp);
        daystatitem.setType(type);
        daystatitem.setSubtype(subType);
        daystatitem.setItem(item);
        daystatitem.setPeriod(date);
        dayStatItemDao.update(daystatitem);
    }

    public void updateDayStatItem(Daystatitem daystatitem) {
        dayStatItemDao.update(daystatitem);
    }

    public Daystatitem getDaystatitemByPk(String cp, String type, String subType, String item, String date) {
        DaystatitemPK pk = new DaystatitemPK();
        pk.setCp(cp);
        pk.setItem(item);
        pk.setType(type);
        pk.setSubtype(subType);
        pk.setPeriod(date);
        return dayStatItemDao.fetch(pk);
    }

    public void updateDayStatItem(String cp, String type, String subType, String item, String date, Map<String, Integer> map) {
        StringBuffer updateStr = null;
        for (String key : map.keySet()) {
            if (updateStr != null) {
                updateStr.append(" , ").append(key).append("=").append(map.get(key));
            } else {
                updateStr = new StringBuffer();
                updateStr.append(key).append("=").append(map.get(key));
            }
        }
        if (!dayStatItemDao.execute("update Daystatitem  set " + updateStr.toString() +
                " where cp = ? and type = ? and subtype = ? and item = ? and period=?", cp, type, subType, item, date)) {
            throw new RuntimeException(String.format("类 %s 更新失败 ", Daystatitem.class.getSimpleName()));
        }
    }

    public List<Daystatitem> getDaystatitemList(String cp, String type, String subType, String item, String... date) {
        StringBuffer hql = new StringBuffer(" from Daystatitem where cp = ? and type = ?  ");
        if (subType != null) {
            hql.append(" and subType = '").append(subType).append("' ");
        }
        if (item != null) {
            hql.append(" and item = '").append(item).append("' ");
        }
        if (date.length > 0) {
            hql.append(" and Period = " + date[0]);
        }
        List<Daystatitem> list = dayStatItemDao.query(hql.toString(), cp, type);
        return list;
    }

    public List<Daystatitem> getDaystatitemList(List<Cp> cps, String type, String subType, String item, String... date) {
        StringBuffer hql = new StringBuffer(" from Daystatitem where type = ?  ");
        if (cps != null && cps.size() > 0) {
            hql.append(" and (");
            for (int i = 0; i < cps.size(); i++) {
                if (i != 0) {
                    hql.append(" or cp = '").append(cps.get(i).getCp()).append("'");
                } else {
                    hql.append(" cp = '").append(cps.get(i).getCp()).append("'");
                }

            }
            hql.append(")");
        }
        if (subType != null) {
            hql.append(" and subType = '").append(subType).append("' ");
        }
        if (item != null) {
            hql.append(" and item = '").append(item).append("' ");
        }
        if (date.length > 0) {
            hql.append(" and Period = " + date[0]);
        }
        List<Daystatitem> list = dayStatItemDao.query(hql.toString(), type);
        return list;
    }

    public QueryResult getDaystatitemList(String cp, String type, String subType, String item, int pageNum, int pageSize, String... date) {
        StringBuffer hql = new StringBuffer(" from Daystatitem where cp = ? and type = ? and subType = ? ");
        if (item != null) {
            hql.append(" and item = '").append(item).append("' ");
        }
        if (date.length > 0) {
            hql.append(" and Period = " + date[0]);
        }
        return dayStatItemDao.query(hql.toString(), pageNum, pageSize, cp, type, subType);
    }

    public List<Daystatitem> getDaystatitemListByHql(String hql, String... values) {
        return dayStatItemDao.query(hql, new Object[]{values});
    }

    @SuppressWarnings({"unchecked"})
    public List<Daystatitem> getDaystatitemListByHql(String hql, int pageNum, int pageSize, String... values) {
        return (List<Daystatitem>) dayStatItemDao.query(hql, pageNum, pageSize, values).getList();
    }

    public List getDaystatitemListBySql(String sql, Object... values) {
        return dayStatItemDao.querySql(sql, values);
    }
}
