<%@ page language="java" import="java.util.*" pageEncoding="UTf-8"%>
<%@ page import="java.net.URLDecoder" %>
<% response.setContentType("application/vnd.ms-excel;charset=GBK"); %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>
   <%=request.getParameter("tab")%>
</body>
</html>