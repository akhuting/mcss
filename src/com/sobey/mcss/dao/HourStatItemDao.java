package com.sobey.mcss.dao;

import com.sobey.common.dao.QueryResult;
import com.sobey.common.pager.Pager;
import com.sobey.common.service.HibernateService;
import com.sobey.mcss.domain.Hourstatitem;
import com.sobey.mcss.domain.id.HourstatitemPK;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Yanggang.
 * Date: 11-1-20
 * Time: 下午3:11
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class HourStatItemDao extends HibernateService<Hourstatitem> {
    @Override
    public SQLQuery createSQLQuery(String sql, Object... values) {
        return super.createSQLQuery(sql, values);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void save(Hourstatitem hourstatitem) {
        super.save(hourstatitem);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void update(Hourstatitem hourstatitem) {
        super.update(hourstatitem);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void save(List<Hourstatitem> hourstatitems) {
        super.save(hourstatitems);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public Hourstatitem fetch(int id) {
        return super.fetch(id);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Hourstatitem fetch(HourstatitemPK pk) {
        return super.getHibernateTemplate().get(getEntryClass(), pk);
    }

    @Override
    public QueryResult query(String hql, int pageNum, int pageSize, Object... values) {
        return super.query(hql, pageNum, pageSize, values);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public List<Hourstatitem> query(String hql, Object... values) {
        return super.query(hql, values);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public List<Hourstatitem> query() {
        return super.query();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public List<Hourstatitem> querySql(String sql, Object... values) {
        return super.querySql(sql, values);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public QueryResult querySql(String sql, int pageNum, int pageSize, Object... values) {
        return super.querySql(sql, pageNum, pageSize, values);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public Pager createPage(int pageNum, int pageSize) {
        return super.createPage(pageNum, pageSize);    //To change body of overridden methods use File | Settings | File Templates.
    }


    @Override
    public Criteria createCriteria(Class clazz, Criterion... criterions) {
        return super.createCriteria(clazz, criterions);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public Criteria createCriteria(Criterion... criterions) {
        return super.createCriteria(criterions);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected Query createQuery(String queryString, Object... values) {
        return super.createQuery(queryString, values);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
