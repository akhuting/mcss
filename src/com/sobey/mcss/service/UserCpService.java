package com.sobey.mcss.service;

import com.sobey.mcss.dao.UserCpDao;
import com.sobey.mcss.domain.UserCp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Alex
 * Date: 11-6-9
 * Time: 下午5:26
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class UserCpService {

    @Autowired
    private UserCpDao userCpDao;

    public void saveUsercp(List cp, int userId) {
        List<UserCp> list = new ArrayList<UserCp>();
        for (int i = 0; i < cp.size(); i++) {
            UserCp userCp = new UserCp();
            userCp.setUserId(userId);
            userCp.setCpId(Integer.parseInt(cp.get(i).toString()));
            list.add(userCp);
        }
        userCpDao.save(list);
    }
    public void deleteAll(int id){
        userCpDao.execute(" delete from UserCp where userId = ?" , id);
    }
    public List<UserCp> getUserCp(int userId){
        return userCpDao.query(" from UserCp where userId = ?",userId);
    }
}
