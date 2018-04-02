<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>找回密码-密码重置</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/zzsc.css" />
<script src="${pageContext.request.contextPath}/static/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/passwordReset.js"></script>
</head>
<body>
<script>
$(document).ready(function(){
	var s = 3;
	var intervalFlag = setInterval(function(){
		$(".second").html(s+'秒后');
		s--;
		if(s<=0){
			clearInterval(intervalFlag);
			window.location.href='../login';
		}
	},1000)
})
</script>
<div class="head">
      <div class="logo"> <img src="${pageContext.request.contextPath}/static/images/logo_retrieve_password.png" style="float:left;" />
        <div class="backToLogin"><a href="../login" style="color:#FFF; text-decoration:none;">返回登录</a></div>
      </div>
    </div>
    <div class="form">
      <div class="inputArea">
      	<div class="forgetTitle">找回密码</div>
        <div class="forgetBody">密码重置成功！</div>
        <div class="return">
        	<font class="second">3秒后</font>
            <a href="../login">返回登录页</a>
        </div>
      </div>
    </div>

<div style="position:relative;color : #4E4E4E; font-size:14px; width:100%;text-align:center; background-color:#DDDDDD; height:30px; padding-top:10px;"> 沪ICP备16002681号-2  &nbsp;&nbsp;&nbsp; Copyright © 2017 Virtue Intelligent Network Ltd, co.</div>
</body>
</html>