package com.sobey.mcss.service;

import com.sobey.mcss.dao.UserDao;
import com.sobey.mcss.domain.Userinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yanggang.
 * Date: 11-1-10
 * Time: 下午12:00
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class UserService {
    @Autowired
    private UserDao userDao;

    public List<Userinfo> getUsers() {
        return userDao.query(" from Userinfo order by userRank ");
    }

    public void saveUser(Userinfo user) {
        userDao.save(user);
    }

    public void updateUser(Userinfo user) {
        userDao.update(user);
    }

    public Userinfo getUser(String name, String password) {
        List<Userinfo> list = userDao.query(" from Userinfo where username = ? and password = ? ", name, password);
        if (list != null && list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }


    public Userinfo getUser(String name) {
        List<Userinfo> list = userDao.query(" from Userinfo where username = ? ", name);
        if (list != null && list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public void deleteUser(int id) {
        userDao.execute("delete from Userinfo where userid = ?", id);
    }

    public Userinfo getUserByEmail(String name, String email) {
        List<Userinfo> list = userDao.query(" from Userinfo where username = ? and email = ?", name, email);
        if (list != null && list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public List<Userinfo> getUserById(int id) {
        List<Userinfo> list = userDao.query(" from Userinfo where userid = ? ", id);
        return list;
    }

    public void updatePassword(String name, String password, String newPassword) {
        List<Userinfo> list = userDao.query(" from Userinfo where username = ? and password = ?", name, password);
        if (list != null && list.size() == 1) {
            Userinfo userinfo = list.get(0);
            userinfo.setPassword(newPassword);
            userDao.update(userinfo);
        } else {
            throw new RuntimeException("密码不正确，请重试");
        }
    }
}
