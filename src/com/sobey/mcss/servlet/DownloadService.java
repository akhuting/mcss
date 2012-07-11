package com.sobey.mcss.servlet;

import com.sobey.common.util.PropertiesUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11-5-23
 * Time: 上午10:51
 * To change this template use File | Settings | File Templates.
 */
public class DownloadService extends HttpServlet {

    public HttpServletResponse download(String path, HttpServletResponse response) {
        try {
            //  path是指欲下载的文件的路径。
            File file = new File(path);
            //  取得文件名。
            String filename = file.getName();
            //  取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(" . ") + 1).toUpperCase();

            //  以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            //  清空response
            response.reset();
            //  设置response的Header
            response.addHeader(" Content-Disposition ", " attachment;filename= " + new String(filename.getBytes()));
            response.addHeader(" Content-Length ", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType(" application/octet-stream ");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uir = req.getRequestURI();
        int index = uir.indexOf("log/") + 3;
        String path = PropertiesUtil.getString("LOGFILE.URL") + uir.substring(index, uir.length());
        download(path, resp);
    }
}
