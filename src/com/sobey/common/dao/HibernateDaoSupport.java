package com.sobey.common.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

/**
 * Created by Yanggang.
 * Date: 11-1-10
 * Time: 下午1:08
 * To change this template use File | Settings | File Templates.
 */
public abstract class HibernateDaoSupport {
    private HibernateTemplate hibernateTemplate;

    public final HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    @Autowired
    public final void setHibernateTemplate(SessionFactory sessionFactory) {
        this.hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    public final SessionFactory getSessionFactory() {
        return (this.hibernateTemplate != null) ? this.hibernateTemplate.getSessionFactory() : null;
    }

    public final void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    protected final Session getSession()
            throws DataAccessResourceFailureException, IllegalStateException {
        return getSession(this.hibernateTemplate.isAllowCreate());
    }

    protected final Session getSession(boolean allowCreate)
            throws DataAccessResourceFailureException, IllegalStateException {
        return (!allowCreate) ?
                SessionFactoryUtils.getSession(getSessionFactory(), false) :
                SessionFactoryUtils.getSession(
                        getSessionFactory(),
                        this.hibernateTemplate.getEntityInterceptor(),
                        this.hibernateTemplate.getJdbcExceptionTranslator());
    }

    protected final void releaseSession(Session session) {
        SessionFactoryUtils.releaseSession(session, getSessionFactory());
    }
}
