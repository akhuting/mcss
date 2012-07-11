package com.sobey.mcss.service;

import com.sobey.common.dao.QueryResult;
import com.sobey.mcss.dao.UploadStatDao;
import com.sobey.mcss.domain.Uploadstat;
import com.sobey.mcss.domain.id.UploadstatPK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Yanggang.
 * Date: 11-2-14
 * Time: 下午2:39
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class UploadStatService {

    @Autowired
    private UploadStatDao uploadStatDao;

    public void saveUploadStat(String cp, String taskId, String beginTime, String endTime, String ip, String dataFlow) {
        UploadstatPK pk = new UploadstatPK();
        pk.setCp(cp);
        pk.setTaskid(taskId);
        if (uploadStatDao.fetch(pk) != null) {
            throw new RuntimeException(String.format("Class %s Already exists in the database", Uploadstat.class.getSimpleName()));
        }
        Uploadstat uploadstat = new Uploadstat();
        uploadstat.setCp(cp);
        uploadstat.setTaskid(taskId);
        uploadstat.setBegintime(beginTime);
        uploadstat.setEndtime(endTime);
        uploadstat.setClient(ip);
        uploadstat.setDataflow(dataFlow);
        uploadStatDao.save(uploadstat);
    }

    public QueryResult getUploadStats(String cp, String beginTime, String endTime, int pageNum, int pageSize) {
        StringBuffer hql = new StringBuffer();
        hql.append(" from Uploadstat where begintime >=? and endtime <= ? and cp = ? order by begintime");
        return uploadStatDao.query(hql.toString(), pageNum, pageSize, beginTime, endTime, cp);
    }

    public List getUploadStatsBySql(String sql, Object... values) {
        return uploadStatDao.querySql(sql, values);
    }
}
