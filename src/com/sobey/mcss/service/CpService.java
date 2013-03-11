package com.sobey.mcss.service;

import com.sobey.mcss.dao.CpDao;
import com.sobey.mcss.domain.Cp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Alex
 * Date: 11-6-9
 * Time: 下午3:10
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class CpService {
    @Autowired
    private CpDao dao;

    public void saveCp(List<Cp> cps) {
        dao.save(cps);
    }

    public void saveCp(Cp cps) {
        dao.save(cps);
    }

    public void deleteAll() {
        dao.executeSql("delete from cp", null);
    }

    public List<Cp> getAll() {
        return dao.query(" from Cp");
    }

    public void delete(List<Cp> cps) {
        dao.deleteAll(cps);
    }

    public Cp getCp(String name) {
        List<Cp> list = dao.query(" from Cp where cp = ?", name);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
