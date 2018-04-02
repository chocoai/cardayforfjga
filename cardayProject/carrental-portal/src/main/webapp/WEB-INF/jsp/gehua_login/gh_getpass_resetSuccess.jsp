<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>找回密码-密码重置</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/gehua_restPass.css" />
<script src="${pageContext.request.contextPath}/static/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/passwordReset.js"></script>
</head>
<body>
<div class="container">
<div class="login_box_content">
<div style="color: black;margin: 50px">密码重置成功。&nbsp;&nbsp;请重新<a href="${pageContext.request.contextPath}/gh_login.jsp" style="font-size:16px;color:#3399FF;text-decoration: none;">登录</a></div>
<%-- <div style="width:604px;margin:100px auto 20px auto">
	<div><img width="604px " src="${pageContext.request.contextPath}/static/images/icon_logo.png" alt="CMDT" /></div>
	<div style="color: #ffffff;font-size: 30px;margin: 30px auto 60px auto;">忘记密码<a href="${pageContext.request.contextPath}/login" id="loginTest">登录</a></div>
	<div style="color: #ffffff;margin-top: 40px">密码重置成功。&nbsp;&nbsp;请重新<a href="${pageContext.request.contextPath}/login" style="font-size:16px;color:#3399FF;text-decoration: none;">登录</a></div>
</div> --%>
</div>
<div style="position:absolute;bottom:2%;color : #fff; width:100%;text-align:center;">
							沪ICP备12345678号  &nbsp;&nbsp;&nbsp; Copyright@2015 Virtue Intelligent Network Co..Ltd</div>
</div>


</body>
</html>