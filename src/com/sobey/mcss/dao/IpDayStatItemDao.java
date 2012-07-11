package com.sobey.mcss.dao;

import com.sobey.common.dao.QueryResult;
import com.sobey.common.service.HibernateService;
import com.sobey.mcss.domain.Ipdaystatitem;
import com.sobey.mcss.domain.id.IpdaystatitemPK;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11-3-4
 * Time: 上午11:19
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class IpDayStatItemDao extends HibernateService<Ipdaystatitem> {

    @Override
    public void save(Ipdaystatitem ipdaystatitem) {
        super.save(ipdaystatitem);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void update(Ipdaystatitem ipdaystatitem) {
        super.update(ipdaystatitem);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void save(List<Ipdaystatitem> ipdaystatitems) {
        super.save(ipdaystatitems);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public Ipdaystatitem fetch(int id) {
        return super.fetch(id);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Ipdaystatitem fetch(IpdaystatitemPK pk) {
        return super.getHibernateTemplate().get(getEntryClass(), pk);
    }

    @Override
    public QueryResult query(String hql, int pageNum, int pageSize, Object... values) {
        return super.query(hql, pageNum, pageSize, values);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public List<Ipdaystatitem> query(String hql, Object... values) {
        return super.query(hql, values);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public List<Ipdaystatitem> query() {
        return super.query();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public List<Ipdaystatitem> querySql(String sql, Object... values) {
        return super.querySql(sql, values);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public QueryResult querySql(String sql, int pageNum, int pageSize, Object... values) {
        return super.querySql(sql, pageNum, pageSize, values);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
