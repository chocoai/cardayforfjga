<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>找回密码-确认账号</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/gh_zz.css" />
</head>
<body>
<div class="container">
<%-- <div style="width:604px;margin:100px auto 20px auto">
	<div style="color: #ffffff;font-size: 20px;margin: 30px auto 60px auto;">忘记密码<a href="${pageContext.request.contextPath}/login" id="loginTest">登录</a></div>
</div> --%>
<div class="login_box_content">
	<div class="form">
	<div style="height:18px"><span id="error_msg" style="color:red;font-size:14px;"></span></div>
	<form action="code/checkVertificationCode" method="post" id="checkCode">
		<input type="hidden" name="flag" value="gh" id="gh"/>
	<div class="div-phone">
		<label for="phone">手机号</label><input type="text" id="phone" name="phoneNumber" class="infos" placeholder="请输入手机号" value="${requestScope.phoneNumber}" style="padding-left: 10px;"/>
		<a href="javascript:;" class="send1" style="margin-left:20px;">发送验证码</a>
	</div>
	<div class="div-ranks" >
		<label for="ranks">验证码</label><input type="text" id="ranks" name="code" class="infos" placeholder="请输入验证码"  style="padding-left: 10px;"/>
	</div>
	<div class="div-button" style="height:60px;">
		<input type="button" class="nextStep" id="nextStep" value="下一步" />
	</div>
	<div >
	<div ><a href="${pageContext.request.contextPath}/gh_login.jsp" id="loginTest">登录</a></div>
</div>
	</form>
	</div>
</div>
<div style="position:absolute;bottom:2%;color : #fff; width:100%;text-align:center;">
							沪ICP备12345678号  &nbsp;&nbsp;&nbsp; Copyright@2015 Virtue Intelligent Network Co..Ltd</div>
</div>

							
							
<script src="${pageContext.request.contextPath}/static/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/passwordReset.js"></script>
<!-- <script>
var sends = {
	checked:1,
	send:function(){
		if(sends.checked==0){
			return false;
		}
			var numbers = /^1\d{10}$/;
			var val = $('#phone').val().replace(/\s+/g,""); //获取输入手机号码
			if($('.div-phone').find('span').length == 0 && $('.div-phone a').attr('class') == 'send1'){
				if(!numbers.test(val) || val.length ==0){
					$('.div-phone').append('<span class="error">手机格式错误</span>');
					return false;
				}
			}
			if(numbers.test(val)){
				var time = 59;
				$('.div-phone span').remove();
				function timeCountDown(){
					if(time<0){
						clearInterval(timer);
						$('.div-phone a').addClass('send1').removeClass('send0').html("发送验证码");
						sends.checked = 1;
						return true;
					}
					$('.div-phone a').html(time+"S后再次发送");
					time--;
					sends.checked = 0;
					return false;
				}
				$('.div-phone a').addClass('send0').removeClass('send1');
				 $.post("code/sendVertificationCode",
				 { 
					 phoneNumber:val
				 },
				 function(data,status){
					 	//alert(data.test);
				 });
				timeCountDown();
				var timer = setInterval(timeCountDown,1000);
			}
	}
}
</script> -->
</body>
</html>