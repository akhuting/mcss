package com.sobey.mcss.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11-3-9
 * Time: 下午2:18
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.Table(name = "userinfo")
@Entity
public class Userinfo {
    private int userid;

    @javax.persistence.Column(name = "USERID", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    private String username;

    @javax.persistence.Column(name = "USERNAME", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Basic
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String userTruename;

    @javax.persistence.Column(name = "USER_TRUENAME", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    @Basic
    public String getUserTruename() {
        return userTruename;
    }

    public void setUserTruename(String userTruename) {
        this.userTruename = userTruename;
    }

    private String password;

    @javax.persistence.Column(name = "PASSWORD", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Basic
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private int userCn = 1;

    @javax.persistence.Column(name = "USER_CN", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getUserCn() {
        return userCn;
    }

    public void setUserCn(int userCn) {
        this.userCn = userCn;
    }

    private int userStatus;

    @javax.persistence.Column(name = "USER_STATUS", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getUserStatus() {
        return userStatus;
    }


    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    private int status;

    @javax.persistence.Column(name = "STATUS", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private String email;

    @javax.persistence.Column(name = "EMAIL", nullable = true, insertable = true, updatable = true, length = 64, precision = 0)
    @Basic
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String phone;

    @javax.persistence.Column(name = "PHONE", nullable = true, insertable = true, updatable = true, length = 16, precision = 0)
    @Basic
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String cellphone;

    @javax.persistence.Column(name = "CELLPHONE", nullable = true, insertable = true, updatable = true, length = 16, precision = 0)
    @Basic
    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    private String userComment;

    @javax.persistence.Column(name = "USER_COMMENT", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    @Basic
    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    private String lastLogin;

    @javax.persistence.Column(name = "LAST_LOGIN", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }


    private List<Cp> cps;

    @ManyToMany(targetEntity = Cp.class,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST}
    )
    @JoinTable(name = "user_cp",
            joinColumns = {@JoinColumn(name = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "cpId")}
    )
    public List<Cp> getCps() {
        return cps;
    }

    public void setCps(List<Cp> cps) {
        this.cps = cps;
    }

    private String userCp;

    @javax.persistence.Column(name = "USER_CP", nullable = true, insertable = true, updatable = true, length = 1000, precision = 0)
    @Basic
    public String getUserCp() {
        return userCp;
    }

    public void setUserCp(String userCp) {
        this.userCp = userCp;
    }

    private int userRank;

    @javax.persistence.Column(name = "USER_RANK", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getUserRank() {
        return userRank;
    }

    public void setUserRank(int userRank) {
        this.userRank = userRank;
    }

    private String userRange;

    @javax.persistence.Column(name = "USER_RANGE", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public String getUserRange() {
        return userRange;
    }

    public void setUserRange(String userRange) {
        this.userRange = userRange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Userinfo that = (Userinfo) o;

        if (userRank != that.userRank) return false;
        if (userStatus != that.userStatus) return false;
        if (userid != that.userid) return false;
        if (cellphone != null ? !cellphone.equals(that.cellphone) : that.cellphone != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (lastLogin != null ? !lastLogin.equals(that.lastLogin) : that.lastLogin != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (userComment != null ? !userComment.equals(that.userComment) : that.userComment != null) return false;
        if (userCp != null ? !userCp.equals(that.userCp) : that.userCp != null) return false;
        if (userRange != null ? !userRange.equals(that.userRange) : that.userRange != null) return false;
        if (userTruename != null ? !userTruename.equals(that.userTruename) : that.userTruename != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userid;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (userTruename != null ? userTruename.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + userStatus;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (cellphone != null ? cellphone.hashCode() : 0);
        result = 31 * result + (userComment != null ? userComment.hashCode() : 0);
        result = 31 * result + (lastLogin != null ? lastLogin.hashCode() : 0);
        result = 31 * result + (userCp != null ? userCp.hashCode() : 0);
        result = 31 * result + userRank;
        result = 31 * result + (userRange != null ? userRange.hashCode() : 0);
        return result;
    }
}
