package com.sobey.common.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.sobey.common.service.HibernateService;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Yanggang.
 * Date: 11-1-10
 * Time: 下午4:28
 * To change this template use File | Settings | File Templates.
 */
public class BaseAction<T> extends ActionSupport implements ModelDriven, Preparable {
    protected Integer id; //实体类的主键ID
    protected T entity; //T类型对象
    protected List<T> list; //页面列表list

    public static final String RELOAD = "reload"; //重定向的返回字符串
    public static final String VIEW = "view"; //查看方法的返回字符串
    public static final String REDIRECT = "redirect"; //重定向，@Result type属性对应的值
    public static final String REDIRECT_ACTION = "redirectAction"; //Action之间重定向，@Result type属性对应的值

    @Autowired
    protected HibernateService hibernateService;

    /**
     * Action的默认执行方法
     */
    @Override
    public String execute() throws Exception {
        doListEntity();
        return SUCCESS;
    }

    /**
     * 进入新增或修改页面
     */
    @Override
    public String input() throws Exception {
        System.out.println("input");
        doInputEntity();
        return INPUT;
    }

    /**
     * 进入查看页面
     */
    public String view() throws Exception {
        doViewEntity();
        return VIEW;
    }

    /**
     * 新增或修改
     */
    public String save() throws Exception {
        doSaveEntity();
        return RELOAD;
    }

    /**
     * 删除
     */
    public String delete() throws Exception {
        doDeleteEntity();
        return RELOAD;
    }

    /**
     * execute回调方法，处理进入主页面的相关逻辑，可在子类中覆盖此方法
     */
    protected void doListEntity() throws Exception {

    }

    /**
     * input回调方法，处理进入新增或修改页面前的相关逻辑，可在子类中覆盖此方法
     */
    protected void doInputEntity() throws Exception {
        System.out.println("doInputEntity");
    }

    /**
     * view回调方法，处理进入查看页面的相关逻辑，可在子类中覆盖此方法
     */
    protected void doViewEntity() throws Exception {
    }

    /**
     * save回调方法，处理删除的相关逻辑，可在子类中覆盖此方法
     */
    protected void doSaveEntity() throws Exception {
        try {
//			hibernateDao.save(entity);
        } catch (Exception e) {
//			logger.error(e.getMessage(), e);
        }
    }

    /**
     * delete回调方法，处理删除相关逻辑，可在子类中覆盖此方法
     */
    protected void doDeleteEntity() throws Exception {
        try {
//			hibernateDao.delete(entityClass,id);
        } catch (Exception e) {
//			logger.error(e.getMessage(), e);
        }
    }

    public void prepareInput() throws Exception {
        System.out.println(this.hibernateService);
//		prepareEntity();
    }

    public void prepareSave() throws Exception {
        prepareEntity();
    }

    public void prepareView() throws Exception {
        prepareEntity();
    }

    protected void prepareEntity() throws Exception {
        if (id != null) {
            entity = (T) hibernateService.fetch(id);
        } else {
            entity = (T) hibernateService.getEntryClass().newInstance();
        }
    }

    /**
     * 取得HttpSession函数
     */
    public static HttpSession getSession() {
        return ServletActionContext.getRequest().getSession();
    }

    /**
     * 取得HttpRequest函数.
     */
    public static HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }

    /**
     * 取得HttpResponse函数
     */
    public static HttpServletResponse getResponse() {
        return ServletActionContext.getResponse();
    }

    /**
     * ModelDriven接口定义的方法，返回实体对象
     */
    public T getModel() {
        return entity;
    }

    /**
     * 页面列表显示的list
     */
    public List getList() {
        return list;
    }

    /**
     * 获取页面传递的id值
     *
     * @param id 主键ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Preparable接口的方法，设置为空方法体是屏蔽它去拦截所有的方法
     */
    public void prepare() throws Exception {
    }

}
