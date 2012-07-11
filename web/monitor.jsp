<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 11-6-14
  Time: 下午1:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
    response.sendRedirect(basePath + "flow/flow!monitor.action?d=" + System.currentTimeMillis());
%>