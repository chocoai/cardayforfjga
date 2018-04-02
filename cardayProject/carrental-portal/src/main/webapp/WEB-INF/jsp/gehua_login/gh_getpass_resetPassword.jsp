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
<%-- <div style="width:604px;margin:100px auto 20px auto">
	<div><img width="604px " src="${pageContext.request.contextPath}/static/images/icon_logo.png" alt="CMDT" /></div>
	<div style="color: #ffffff;font-size: 20px;margin: 30px auto 60px auto;">忘记密码<a href="${pageContext.request.contextPath}/login" id="loginTest">登录</a></div>
</div> --%>
<div class="container">
<div class="login_box_content">
	<div class="form">
	<div style="height:18px"><span id="error_msg" style="color:red;font-size:14px;"></span></div>
	<form action="${pageContext.request.contextPath}/password/changePassword" method="post" id="checkPassword" >
	<input type="hidden" name="flag" value="gh" id="ghs"/>
		<input type="hidden"  name="userId" value="${requestScope.userId }"/>
	<div class="div-ranks">
		<input type="hidden" name="phoneNumber" value="${requestScope.phoneNumber }" />
		<label for="ranks" style="margin-right: 55px;">新密码</label><input type="password" name="code" class="infos" placeholder="请输入新密码"  style="padding-left: 10px;"/>
	</div>
	<div class="div-ranks">
		<label for="ranks">确认新密码</label><input type="password"  name="newPassword" class="infos" placeholder="请再次输入新密码" style="padding-left: 10px;" />
	</div>
	<div class="div-button">
		<input type="button" class="nextStep" id="validatePassword" value="确定" />
	</div>
	<div>
	<div ><a href="${pageContext.request.contextPath}/gh_login.jsp id="loginTest">登录</a></div>
</div>
	</form>
	</div> 
	</div>
<div style="position:absolute;bottom:2%;color : #fff; width:100%;text-align:center;">
							沪ICP备12345678号  &nbsp;&nbsp;&nbsp; Copyright@2015 Virtue Intelligent Network Co..Ltd</div>
</div>

</body>
</html>