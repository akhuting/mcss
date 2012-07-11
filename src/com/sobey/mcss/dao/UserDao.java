package com.sobey.mcss.dao;

import com.sobey.common.dao.QueryResult;
import com.sobey.common.pager.Pager;
import com.sobey.common.service.HibernateService;
import com.sobey.mcss.domain.Userinfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Yanggang.
 * Date: 11-1-10
 * Time: 上午11:49
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class UserDao extends HibernateService<Userinfo> {


    public void save(Userinfo o) {
        super.save(o);
    }

    public void save(List<Userinfo> list) {
        super.save(list);
    }

    public void update(Userinfo o) {
        super.update(o);
    }

    public Userinfo fetch(int id) {
        return super.fetch(id);
    }

    public QueryResult query(String hql, int pageNum, int pageSize, Object... values) {
        return super.query(hql, pageNum, pageSize, values);
    }

    public List<Userinfo> query(String hql, Object... values) {
        return super.query(hql, values);
    }

    public Pager createPage(int pageNum, int pageSize) {
        return super.createPage(pageNum, pageSize);
    }
}
