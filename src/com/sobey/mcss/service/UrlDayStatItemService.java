package com.sobey.mcss.service;

import com.sobey.common.util.DateUtil;
import com.sobey.common.util.MirrorUtil;
import com.sobey.common.util.R;
import com.sobey.mcss.dao.UrlDayStatItemDao;
import com.sobey.mcss.domain.Urldaystatitem;
import com.sobey.mcss.domain.id.UrldaystatitemPK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11-3-7
 * Time: 上午10:24
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class UrlDayStatItemService {

    @Autowired
    private UrlDayStatItemDao urlDayStatItemDao;

    public void saveUrlDayStatItem(String cp, String type, String subType, String item, String... date) {
        Map<String, Integer> counts = new HashMap<String, Integer>();
        for (int i = 0; i < 31; i++) {
            counts.put("count" + (i + 1),
                    R.randomInt(3));
        }
        UrldaystatitemPK pk = new UrldaystatitemPK();
        pk.setCp(cp);
        pk.setItem(item);
        if (date.length > 0) {
            pk.setPeriod(date[0]);
        } else {
            pk.setPeriod(DateUtil.getCurrentTime(DateUtil.YY_MM));
        }
        pk.setType(type);
        pk.setSubtype(subType);
        if (urlDayStatItemDao.fetch(pk) != null) {
            throw new RuntimeException(String.format("Class %s Already exists in the database", Urldaystatitem.class.getSimpleName()));
        }
        Urldaystatitem daystatitem = MirrorUtil.getObject(Urldaystatitem.class, counts);
        daystatitem.setCp(cp);
        daystatitem.setType(type);
        daystatitem.setSubtype(subType);
        daystatitem.setItem(item);
        if (date.length > 0) {
            daystatitem.setPeriod(date[0]);
        } else {
            daystatitem.setPeriod(DateUtil.getCurrentTime(DateUtil.YY_MM));
        }
        urlDayStatItemDao.save(daystatitem);
    }

    public List<Urldaystatitem> getUrldaystatitemList(String cp, String type, String subType, String item, String... date) {
        StringBuffer hql = new StringBuffer(" from Ipdaystatitem where cp = ? and type = ? and subType = ? ");
        if (item != null) {
            hql.append(" and item = '").append(item).append("' ");
        }
        if (date.length > 0) {
            hql.append(" and Period = " + date[0]);
        }
        return urlDayStatItemDao.query(hql.toString(), cp, type, subType);
    }

    public List<Urldaystatitem> getUrldaystatitemListByHql(String hql, int pageNum, int pageSize, String... values) {
        return (List<Urldaystatitem>) urlDayStatItemDao.query(hql, pageNum, pageSize, new Object[]{values}).getList();
    }

    public List getUrldaystatitemListBySql(String sql, int pageNum, int pageSize, Object... values) {
        return urlDayStatItemDao.querySql(sql, pageNum, pageSize, values).getList();
    }

    public List getUrldaystatitemListBySql(String sql, Object... values) {
        return urlDayStatItemDao.querySql(sql, values);
    }
}
