package com.sobey.mcss.service;

import com.sobey.common.util.DateUtil;
import com.sobey.common.util.MirrorUtil;
import com.sobey.common.util.R;
import com.sobey.mcss.dao.IpDayStatItemDao;
import com.sobey.mcss.domain.Ipdaystatitem;
import com.sobey.mcss.domain.id.IpdaystatitemPK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11-3-4
 * Time: 上午11:26
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class IpDayStatItemService {

    @Autowired
    private IpDayStatItemDao ipDayStatItemDao;


    public void saveIpDayStatItem(String cp, String type, String subType, String item, String... date) {
        Map<String, Integer> counts = new HashMap<String, Integer>();
        for (int i = 0; i < 31; i++) {
            counts.put("count" + (i + 1),
                    R.randomInt(3));
        }
        IpdaystatitemPK pk = new IpdaystatitemPK();
        pk.setCp(cp);
        pk.setItem(item);
        if (date.length > 0) {
            pk.setPeriod(date[0]);
        } else {
            pk.setPeriod(DateUtil.getCurrentTime(DateUtil.YY_MM));
        }
        pk.setType(type);
        pk.setSubtype(subType);
        if (ipDayStatItemDao.fetch(pk) != null) {
            throw new RuntimeException(String.format("Class %s Already exists in the database", Ipdaystatitem.class.getSimpleName()));
        }
        Ipdaystatitem daystatitem = MirrorUtil.getObject(Ipdaystatitem.class, counts);
        daystatitem.setCp(cp);
        daystatitem.setType(type);
        daystatitem.setSubtype(subType);
        daystatitem.setItem(item);
        if (date.length > 0) {
            daystatitem.setPeriod(date[0]);
        } else {
            daystatitem.setPeriod(DateUtil.getCurrentTime(DateUtil.YY_MM));
        }
        ipDayStatItemDao.save(daystatitem);
    }

    public List<Ipdaystatitem> getIpdaystatitemList(String cp, String type, String subType, String item, String... date) {
        StringBuffer hql = new StringBuffer(" from Ipdaystatitem where cp = ? and type = ? and subType = ? ");
        if (item != null) {
            hql.append(" and item = '").append(item).append("' ");
        }
        if (date.length > 0) {
            hql.append(" and Period = " + date[0]);
        }
        return ipDayStatItemDao.query(hql.toString(), cp, type, subType);
    }

    public List<Ipdaystatitem> getIpdaystatitemListByHql(String hql, int pageNum, int pageSize, String... values) {
        return (List<Ipdaystatitem>) ipDayStatItemDao.query(hql, pageNum, pageSize, values).getList();
    }

    public List getIpdaystatitemListBySql(String sql, int pageNum, int pageSize, Object... values) {
        return ipDayStatItemDao.querySql(sql, pageNum, pageSize, values).getList();
    }

    public List getIpdaystatitemListBySql(String sql, Object... values) {
        return ipDayStatItemDao.querySql(sql, values);
    }
}
