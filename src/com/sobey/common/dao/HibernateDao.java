package com.sobey.common.dao;

import com.sobey.common.pager.Pager;


import java.util.List;

/**
 * Created by Yanggang.
 * Date: 11-1-8
 * Time: 下午10:58
 * To change this template use File | Settings | File Templates.
 */
public interface HibernateDao<T> {
    public void save(T t);

    public void save(List<T> list);

    public void update(T t);

    public T fetch(int id);

    public boolean execute(String hql, Object... values);

    public boolean executeSql(String sql, Object... values);

    public QueryResult query(String hql, int pageNum, int pageSize, Object... values);

    public List<T> query(String hql, Object... values);

    public List<T> query();

    public List<T> querySql(String sql, Object... values);

    public QueryResult querySql(String sql, int pageNum, int pageSize, Object... values);
}
