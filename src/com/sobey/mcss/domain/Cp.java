package com.sobey.mcss.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Alex
 * Date: 11-6-9
 * Time: 上午11:56
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Cp {
    private int id;

    @javax.persistence.Column(name = "id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @javax.persistence.Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "identity")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String cp;

    @javax.persistence.Column(name = "cp", nullable = true, insertable = true, updatable = true, length = 500, precision = 0)
    @javax.persistence.Basic
    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }


    private int pid;

    @javax.persistence.Column(name = "pid", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    @javax.persistence.Basic
    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    private List<Userinfo> users;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "cps",
            targetEntity = Userinfo.class
    )
    public List<Userinfo> getUsers() {
        return users;
    }

    public void setUsers(List<Userinfo> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cp cp1 = (Cp) o;

        if (id != cp1.id) return false;
        if (cp != null ? !cp.equals(cp1.cp) : cp1.cp != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (cp != null ? cp.hashCode() : 0);
        return result;
    }
}
