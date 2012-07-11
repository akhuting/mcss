package com.sobey.mcss.dao;

import com.sobey.common.dao.QueryResult;
import com.sobey.common.service.HibernateService;
import com.sobey.mcss.domain.Urldaystatitem;
import com.sobey.mcss.domain.id.UrldaystatitemPK;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11-3-4
 * Time: 下午4:49
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class UrlDayStatItemDao extends HibernateService<Urldaystatitem> {

    @Override
    public void save(Urldaystatitem urldaystatitem) {
        super.save(urldaystatitem);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void update(Urldaystatitem urldaystatitem) {
        super.update(urldaystatitem);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void save(List<Urldaystatitem> urldaystatitems) {
        super.save(urldaystatitems);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public Urldaystatitem fetch(int id) {
        return super.fetch(id);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Urldaystatitem fetch(UrldaystatitemPK pk) {
        return super.getHibernateTemplate().get(getEntryClass(), pk);
    }

    @Override
    public QueryResult query(String hql, int pageNum, int pageSize, Object... values) {
        return super.query(hql, pageNum, pageSize, values);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public List<Urldaystatitem> query(String hql, Object... values) {
        return super.query(hql, values);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public List<Urldaystatitem> query() {
        return super.query();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public List<Urldaystatitem> querySql(String sql, Object... values) {
        return super.querySql(sql, values);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public QueryResult querySql(String sql, int pageNum, int pageSize, Object... values) {
        return super.querySql(sql, pageNum, pageSize, values);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
