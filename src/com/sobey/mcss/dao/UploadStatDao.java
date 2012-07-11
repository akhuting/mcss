package com.sobey.mcss.dao;

import com.sobey.common.dao.QueryResult;
import com.sobey.common.service.HibernateService;
import com.sobey.mcss.domain.Uploadstat;
import com.sobey.mcss.domain.id.UploadstatPK;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Yanggang.
 * Date: 11-2-14
 * Time: 下午2:37
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class UploadStatDao extends HibernateService<Uploadstat> {
    @Override
    public void save(Uploadstat uploadstat) {
        super.save(uploadstat);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void update(Uploadstat uploadstat) {
        super.update(uploadstat);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void save(List<Uploadstat> uploadstats) {
        super.save(uploadstats);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public Uploadstat fetch(int id) {
        return super.fetch(id);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Uploadstat fetch(UploadstatPK pk) {
        return super.getHibernateTemplate().get(getEntryClass(), pk);
    }

    @Override
    public QueryResult query(String hql, int pageNum, int pageSize, Object... values) {
        return super.query(hql, pageNum, pageSize, values);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public List<Uploadstat> query(String hql, Object... values) {
        return super.query(hql, values);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public List<Uploadstat> query() {
        return super.query();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public List<Uploadstat> querySql(String sql, Object... values) {
        return super.querySql(sql, values);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public QueryResult querySql(String sql, int pageNum, int pageSize, Object... values) {
        return super.querySql(sql, pageNum, pageSize, values);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
