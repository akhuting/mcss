package com.sobey.common.service;

import com.sobey.common.dao.HibernateDao;
import com.sobey.common.dao.HibernateDaoSupport;
import com.sobey.common.dao.QueryResult;
import com.sobey.common.pager.Pager;
import com.sobey.common.util.MirrorUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Yanggang.
 * Date: 11-1-9
 * Time: 上午12:54
 * To change this template use File | Settings | File Templates.
 */
public class HibernateService<T> extends HibernateDaoSupport implements HibernateDao<T> {
    private Class<T> entryClass;


    public HibernateService() {
        entryClass = MirrorUtil.getTypeParam(getClass(), 0);
    }

    public void save(T t) {
        this.getHibernateTemplate().save(t);
    }

    public void update(T t) {
        this.getHibernateTemplate().update(t);
    }


    public void save(List<T> list) {
       for(int i = 0; i < list.size(); i++){
           this.getHibernateTemplate().saveOrUpdate(list.get(i));
           if(i%20 == 0){
               this.getHibernateTemplate().flush();
               this.getHibernateTemplate().clear();
           }
       }
    }
    public void deleteAll(List<T> list){
        this.getHibernateTemplate().deleteAll(list);
    }
    public T fetch(int id) {
        return this.getHibernateTemplate().get(getEntryClass(), id);
    }

    public QueryResult query(String hql, int pageNum, int pageSize, Object... values) {
        Query query = createQuery(hql, values);
        query.setFirstResult((pageNum - 1) * pageSize);
        query.setMaxResults(pageSize);
        List<T> list = query.list();
        Pager pager = createPage(pageNum, pageSize);
        return new QueryResult(list, pager);
    }

    public boolean execute(String hql, Object... values) {
        Query query = createQuery(hql, values);
        int result = query.executeUpdate();
        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean executeSql(String sql, Object... values) {
        Query query = createSQLQuery(sql, values);
        int result = query.executeUpdate();
        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }

    public List<T> query(String hql, Object... values) {
        return createQuery(hql, values).list();
    }

    public List<T> query() {
        return createCriteria(getEntryClass()).list();
    }

    public List<T> querySql(String sql, Object... values) {
        return createSQLQuery(sql, values).list();
    }

    public QueryResult querySql(String sql, int pageNum, int pageSize, Object... values) {
        Query query = createSQLQuery(sql, values);
        query.setFirstResult(pageNum);
        query.setFetchSize(pageSize);
        Pager pager = createPage(pageNum, pageSize);
        List<T> list = query.list();
        return new QueryResult(list, pager);
    }

    public Pager createPage(int pageNum, int pageSize) {
        Pager page = new Pager();
        page.setPageNumber(pageNum);
        page.setPageSize(pageSize);
        return page;
    }

    public Class<T> getEntryClass() {
        return entryClass;
    }

    public void setEntryClass(Class<T> entryClass) {
        this.entryClass = entryClass;
    }

    public Criteria createCriteria(final Class clazz, final Criterion... criterions) {
        Criteria criteria = getSession().createCriteria(clazz);
        for (Criterion c : criterions) {
            criteria.add(c);
        }
        return criteria;
    }

    public Criteria createCriteria(final Criterion... criterions) {
        return createCriteria(getEntryClass(), criterions);
    }

    protected Query createQuery(String queryString, Object... values) {
        Query queryObject = getSession().createQuery(queryString);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                queryObject.setParameter(i, values[i]);
            }
        }
        return queryObject;
    }

    public SQLQuery createSQLQuery(final String sql, final Object... values) {
        SQLQuery query = getSession().createSQLQuery(sql);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return query;
    }
}
