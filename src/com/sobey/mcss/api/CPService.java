package com.sobey.mcss.api;

import com.sobey.common.util.PasswordEncoder;
import com.sobey.mcss.domain.Cp;
import com.sobey.mcss.domain.UserCp;
import com.sobey.mcss.domain.Userinfo;
import com.sobey.mcss.service.CpService;
import com.sobey.mcss.service.UserCpService;
import com.sobey.mcss.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Yanggang
 * Date: 13-3-5
 * Time: 上午9:52
 * To change this template use File | Settings | File Templates.
 */
@Path("/cp")
@Component
public class CPService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserCpService userCpService;

    @Autowired
    private CpService cpService;

    @POST
    @Produces("text/plain")
    public synchronized String createCP(@FormParam("name") String name, @FormParam("alias") String alias, @FormParam("domain") String domain) {
        if(name == null || alias == null || domain == null){
            throw new NullPointerException();
        }
        Cp cp = cpService.getCp(domain);
        Cp cpname = cpService.getCp(name);
        Userinfo userinfo = userService.getUser(alias);
        int userId = 0;
        if (userinfo == null) {
            userinfo = new Userinfo();
            userinfo.setCellphone("");
            userinfo.setEmail("");
            userinfo.setPassword(PasswordEncoder.encode(alias, alias));
            userinfo.setUsername(alias);
            userinfo.setUserStatus(0);
            userinfo.setUserTruename(alias);
            userinfo.setUserRank(1);
            userinfo.setUserRange("");
            userinfo.setStatus(1);

            userService.saveUser(userinfo);
        }

        if (cp == null) {
            cp = new Cp();
            cp.setCp(domain);
            cp.setPid(0);
            cpService.saveCp(cp);
        }
        if (cpname == null) {
            cpname = new Cp();
            cpname.setPid(cp.getId());
            cpname.setCp(name);
            cpService.saveCp(cpname);
        }

        for (UserCp c : userCpService.getUserCp(userinfo.getUserid())) {
            if (c.getCpId() == cpname.getId()) {
                return "新增成功!";
            }
        }

        List cps = new ArrayList();
        cps.add(cpname.getId());
        userCpService.saveUsercp(cps, userinfo.getUserid());
        return "新增成功!";
    }


    @DELETE
    @Produces("text/plain")
    public String delCp(@QueryParam("alias") String alias) {
        Userinfo userinfo = userService.getUser(alias);
        if(userinfo == null){
            throw new NullPointerException();
        }
        userinfo.setStatus(0);
        userService.updateUser(userinfo);
        return "删除成功!";
    }
}
