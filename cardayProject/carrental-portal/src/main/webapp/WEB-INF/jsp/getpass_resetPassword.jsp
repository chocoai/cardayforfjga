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
    <div class="head">
      <div class="logo"> <img src="${pageContext.request.contextPath}/static/images/logo_retrieve_password_fjga.png" style="float:left;margin-top: 12px;" />
        <div class="backToLogin"><a href="../login" style="color:#FFF; text-decoration:none;">返回登录</a></div>
      </div>
    </div>
    <div class="form">
      <div class="inputArea">
        	<div class="forgetTitle">找回密码</div>  
      <form action="${pageContext.request.contextPath}/password/changePassword" method="post" id="checkPassword" >
        <input type="hidden" name="flag" value="zz" id="ghs"/>
        <%-- <input type="hidden"  name="userId" value="${requestScope.userId }"/> --%>
        <div class="div-ranks">
          <%-- <input type="hidden" name="phoneNumber" value="${requestScope.phoneNumber }" /> --%>
          <div class="text">新密码</div>
          <input type="password" name="code" class="infos" placeholder="请输入新密码"  style="width:254px; margin-left:42px !important;"/>
        </div>
        <div style="clear:both; margin-top:14px;">&nbsp;</div>
        <div class="div-ranks">
          <div class="text">确认新密码</div>
          <input type="password"  name="newPassword" class="infos" placeholder="请再次输入新密码" style="width:254px" />
        </div>
        <div style="clear:both; margin-top:14px;">&nbsp;</div>
        <div class="error_msg2" id="error_msg">密码由6-20位字符组成，含数字、字母</div>
        <div class="div-button">
          <input type="submit" class="nextStep" id="validatePassword" value="确定" />
        </div>
      </form>
      </div>
    </div>
<!--     <div style="position:relative;color : #4E4E4E; font-size:14px; width:100%;text-align:center; background-color:#DDDDDD; height:30px; padding-top:10px;"> 沪ICP备16002681号-2  &nbsp;&nbsp;&nbsp; Copyright © 2017 Virtue Intelligent Network Ltd, co.</div> -->
</body>
</html>