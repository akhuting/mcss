package com.sobey.mcss.service;

import com.sobey.common.dao.QueryResult;
import com.sobey.common.util.DateUtil;
import com.sobey.common.util.MirrorUtil;
import com.sobey.common.util.R;
import com.sobey.mcss.dao.HourStatItemDao;
import com.sobey.mcss.domain.Cp;
import com.sobey.mcss.domain.Hourstatitem;
import com.sobey.mcss.domain.id.HourstatitemPK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Yanggang.
 * Date: 11-1-20
 * Time: 下午3:10
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class HourStatItemService {

    @Autowired
    private HourStatItemDao hourStatItemDao;

    public void saveHourStatItem(String cp, String type, String subType, String item, String... date) {
         Random random = new Random();
        Map<String, Integer> counts = new HashMap<String, Integer>();
        for (int i = 0; i < 24; i++) {
            counts.put("count" + (i + 1),
//                    Math.abs(random.nextInt())%10000000);
//                    Math.abs(random.nextInt())%(80 * 100000));
                    Math.abs(random.nextInt())%(100));
        }
        HourstatitemPK pk = new HourstatitemPK();
        pk.setCp(cp);
        pk.setItem(item);
        if (date.length > 0) {
            pk.setPeriod(date[0]);
        } else {
            pk.setPeriod(DateUtil.getCurrentTime(DateUtil.YY_MM_D));
        }
        pk.setType(type);
        pk.setSubtype(subType);
        if (hourStatItemDao.fetch(pk) != null) {
            throw new RuntimeException(String.format("Class %s Already exists in the database", Hourstatitem.class.getSimpleName()));
        }
        Hourstatitem hourstatitem = MirrorUtil.getObject(Hourstatitem.class, counts);
        hourstatitem.setCp(cp);
        hourstatitem.setType(type);
        hourstatitem.setSubtype(subType);
        hourstatitem.setItem(item);
        if (date.length > 0) {
            hourstatitem.setPeriod(date[0]);
        } else {
            hourstatitem.setPeriod(DateUtil.getCurrentTime(DateUtil.YY_MM));
        }
        hourStatItemDao.save(hourstatitem);
    }

    public void saveHourStatItem(Hourstatitem hourstatitem) {

        hourStatItemDao.save(hourstatitem);
    }

    public void saveHourStatItem(String cp, String type, String subType, String item, String date, Map<String, Integer> map) {
        HourstatitemPK pk = new HourstatitemPK();
        pk.setCp(cp);
        pk.setItem(item);
        pk.setPeriod(date);
        pk.setType(type);
        pk.setSubtype(subType);
        if (hourStatItemDao.fetch(pk) != null) {
            throw new RuntimeException(String.format("Class %s Already exists in the database", Hourstatitem.class.getSimpleName()));
        }
        Hourstatitem hourstatitem = MirrorUtil.getObject(Hourstatitem.class, map);
        hourstatitem.setCp(cp);
        hourstatitem.setType(type);
        hourstatitem.setSubtype(subType);
        hourstatitem.setItem(item);
        hourstatitem.setPeriod(date);
        hourStatItemDao.save(hourstatitem);
    }

    public Hourstatitem getHourstatitemByPk(String cp, String type, String subType, String item, String date) {
        HourstatitemPK pk = new HourstatitemPK();
        pk.setCp(cp);
        pk.setItem(item);
        pk.setPeriod(date);
        pk.setType(type);
        pk.setSubtype(subType);

        return hourStatItemDao.fetch(pk);
    }

    public void updateHourStatItem(String cp, String type, String subType, String item, String date) {
        Map<String, Integer> counts = new HashMap<String, Integer>();
        for (int i = 0; i < 24; i++) {
            counts.put("count" + (i + 1),
                    R.randomInt(R.randomInt(1)));
        }
        Hourstatitem hourstatitem = MirrorUtil.getObject(Hourstatitem.class, counts);
        hourstatitem.setCp(cp);
        hourstatitem.setType(type);
        hourstatitem.setSubtype(subType);
        hourstatitem.setItem(item);
        hourstatitem.setPeriod(date);
        hourStatItemDao.update(hourstatitem);
    }

    public void updateHourStatItem(Hourstatitem hourstatitem) {

        hourStatItemDao.update(hourstatitem);
    }

    public void updateHourStatItem(String cp, String type, String subType, String item, String date, Map<String, Integer> map) {
        StringBuffer updateStr = null;
        for (String key : map.keySet()) {
            if (updateStr != null) {
                updateStr.append(" , ").append(key).append("=").append(map.get(key));
            } else {
                updateStr = new StringBuffer();
                updateStr.append(key).append("=").append(map.get(key));
            }

        }
        if (!hourStatItemDao.execute("update Hourstatitem  set " + updateStr.toString() +
                " where cp = ? and type = ? and subtype = ? and item = ? and period=?", cp, type, subType, item, date)) {
            throw new RuntimeException(String.format("类 %s 更新失败 ", Hourstatitem.class.getSimpleName()));
        }
    }

    public List<Hourstatitem> getHourstatitemList(List<Cp> cps, String type, String subType, String item, String... date) {
        StringBuffer hql = new StringBuffer(" from Hourstatitem where 1=1 ");
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
        if (type != null && type.equals("")) {
            hql.append(" and type = '").append(type).append("'");
        }
        if (subType != null && !subType.equals("")) {
            hql.append(" and subType = '").append(subType).append("'");
        }
        if (item != null && !item.equals("")) {
            hql.append(" and item = '").append(item).append("'");
        }
        if (date.length > 0) {
            hql.append(" and Period = '" + date[0] + "'");
        }
        List<Hourstatitem> list = hourStatItemDao.query(hql.toString());
        return list;
    }

    public List<Hourstatitem> getHourstatitemList(String cp, String type, String subType, String item, String... date) {
        StringBuffer hql = new StringBuffer(" from Hourstatitem where cp = ?");
        if (type != null && !type.equals("")) {
            hql.append(" and type = '").append(type).append("'");
        }
        if (subType != null && !subType.equals("")) {
            hql.append(" and subType = '").append(subType).append("'");
        }
        if (item != null && !item.equals("")) {
            hql.append(" and item = '").append(item).append("'");
        }
        if (date.length > 0) {
            hql.append(" and Period = '" + date[0] + "'");
        }
        List<Hourstatitem> list = hourStatItemDao.query(hql.toString(), cp);
        return list;
    }

    public QueryResult getHourstatitemList(String cp, String type, String subType, String item, int pageNum, int pageSize, String... date) {
        StringBuffer hql = new StringBuffer(" from Hourstatitem where cp = ?");
        if (type != null && type.equals("")) {
            hql.append(" and type = '").append(type).append("'");
        }
        if (subType != null && !subType.equals("")) {
            hql.append(" and subType = '").append(subType).append("'");
        }
        if (item != null && !item.equals("")) {
            hql.append(" and item = '").append(item).append("'");
        }
        if (date.length > 0) {
            hql.append(" and Period = '" + date[0] + "'");
        }
        hourStatItemDao.query(hql.toString(), pageNum, pageSize, cp);
        return hourStatItemDao.query(hql.toString(), pageNum, pageSize, cp);
    }

    public List<Hourstatitem> getHourstatitemListByHql(String hql, String... values) {
        return hourStatItemDao.query(hql, new Object[]{values});
    }

    @SuppressWarnings({"unchecked"})
    public List<Hourstatitem> getHourstatitemListByHql(String hql, int pageNum, int pageSize, String... values) {
        return (List<Hourstatitem>) hourStatItemDao.query(hql, pageNum, pageSize, values).getList();
    }

    public void test() {
        HourstatitemPK pk = new HourstatitemPK();
        pk.setCp("1");
        pk.setItem("1");
        pk.setPeriod("201011");
        pk.setSubtype("li");
        pk.setType("li");
        hourStatItemDao.fetch(pk);
    }
}
