<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<html>
<%
	String username = request.getParameter("username");
	String password = request.getParameter("password");
	String flag = request.getParameter("flag");

	if (flag != null && flag.equals("logout")) {
		//用户登出系统，删除session中的用户信息
		session.removeAttribute("username");
		session.removeAttribute("password");
	}
	//获取输入的username，password，点击登录之后，信息保留在输入框
	if (username == null) {
		username = "";
	}

	if (password == null) {
		password = "";
	}
	String ctx = request.getContextPath();
%>

<head>
<title>企业用车云服务系统</title>
<link rel="shortcut icon" href="resources/images/icons/cmdt_sc_logo.ico"
	type="image/x-icon">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/static/css/gh_css.css" />
<style>
#vcode {
	height: 30px;
	width: 240;
	font-size: 15pt;
	/* margin-left:15%; */
	border-radius: 5px;
	border: 0;
	padding-left: 8px;
}

#code {
	color: #000000; /*字体颜色白色*/
	background-color: #ffffff;
	font-size: 24pt;
	font-family: "宋体,楷体,微软雅黑";
	padding: 8px 0px 0px 0px;
	/*                     padding:3px 15px 5px 15px;  */
	/*                     margin-left:3%;        */
	/* 					margin-top:-2%; */
	cursor: pointer;
}
</style>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/jquery-3.1.1.min.js"></script>
<script type="text/javascript">
	$(function() { //生成验证码  
		$('#kaptchaImage')
				.click(
						function() {
							$(this).hide().attr(
									'src',
									'code/captcha-image?'
											+ Math.floor(Math.random() * 100))
									.fadeIn();
						});
		$('#appdownload1').click(function() {
			window.location.href = 'resources/apk/Admin.apk';
		});
		
		/* $('#loginbutton').click(function(){
			validateCode();	
		}); */
	});

	window.onbeforeunload = function() {
		//关闭窗口时自动退出  
		if (event.clientX > 360 && event.clientY < 0 || event.altKey) {
			alert(parent.document.location);
		}
	};

	function changeCode() { //刷新
		console.log('reflash...');
		$('#kaptchaImage').hide().attr(
				'src',
				'../code/captcha-image?'
						+ Math.floor(Math.random() * 100)).fadeIn();
		event.cancelBubble = true;
	}

	
	function validateCode() {
		console.log('validateCode...');
		var username = $("#username").val();
		if (username == '') {
			showErrorMsg('请输入用户名');
			return false;
		}
		var password = $("#password").val();
		if (password == '') {
			showErrorMsg('请输入密码');
			return false;
		}
		var yzcode = $("#yzcode").val();
		if (yzcode == '') {
			showErrorMsg('请输入验证码');
			return false;
		}
		console.log('yzcode:' + yzcode);
		// 				var pattern = /^[0-9a-zA-Z]*$/g;
		var pattern = /^([A-Z]|[a-z]|\d){4}$/;
		if (!pattern.test(yzcode)) {
			showErrorMsg('输入的验证码格式错误!');
			return false;
		}
		var sessionCode = '';
		var input_code = document.getElementById('yzcode').value;
		if (input_code.toLowerCase() == code.toLowerCase()) {
			//验证码正确(表单提交)  
		//	return true;
		} else {
			showErrorMsg('输入的验证码有误!');
			changeImg();
			return false;
		}
		var flag;
		$.ajax({
			//url:"/carrental-portal/validateUser?v="+Math.random(),
			url:"./validateUser?v="+Math.random(),
			type:'POST',
			cache : false, 
			async : false,
			data: {username: $("#username").val(),password:$("#password").val()},
			timeout:1000,    //超时时间
			dataType:'json',
			success:function(data){
				if(data.status==true){
					flag=true;
				}else{
					showErrorMsg('输入的密码有误!');
					changeImg();
					flag=false;
				}
			}
	 	});
		if(flag){
			return true;
		}else{
			return false;
		}

		/* $.ajax({
		    url:'/carrental-portal/code/getVolidateCode',
		    type:'GET',
		    async:false,    //或false,是否异步*/
		// 				    data:{
		// 				        name:'yang',age:25
		// 				    },
		// 				    timeout:1000,    //超时时间
		// 				    dataType:'json',    //返回的数据格式：json/xml/html/script/jsonp/text
		/* beforeSend:function(xhr){
		     console.log(xhr);
		     console.log('发送前');
		 },
		 success:function(data,textStatus,jqXHR){
		     console.log(data);
		     console.log(textStatus);
		// 				        console.log(jqXHR);
			if(textStatus == 'success') {
				sessionCode = data;
			}
		 },
		 error:function(xhr,textStatus){
		     console.log('错误');
		     console.log(xhr);
		     console.log(textStatus);
		     return false;
		 } ,
		 complete:function(){
		     console.log('结束');
		 } 
		}) */
		/* console.log('sessionCode::' + sessionCode);
		if(yzcode.toLowerCase() == sessionCode.toLowerCase()) {
			return true; 					
		}else{
			alert('输入的验证码有误');
			return false;
		} */
	}

	function showErrorMsg(msg) {
		$("#error_msg").html(msg);
	}
</script>
</head>

<body leftmargin="0" onLoad="changeImg()">

	<div class="container" style="overflow:scroll; min-width:1450px;">
			<div class="login_box_content">
				<form action="login" method="post" class="login_form" id="loginSubmit"
					onsubmit="return validateCode()">
					<span id="error_msg"
						style="color: red; position: absolute; left: 40px; top: 20px; text-align: center;">&nbsp;</span>
					<p>
						<span
							style="height: 40; width: 40; display: block; float: left; text-align: center; border-radius: 5px; background-color: #ffffff;">
							<img alt="" style="margin: 9px auto;"
							src="${pageContext.request.contextPath}/static/images/icon_user.png">
						</span> <input type="text" placeholder="请输入用户名"
							class="login_user login_input_font" style="padding-left: 10px;"
							name="username" id="username" value="<shiro:principal/>" />
					</p>
					<p>
						<span
							style="height: 40; width: 40; display: block; float: left; text-align: center; border-radius: 5px; background-color: #ffffff;">
							<img alt="" style="margin: 9px auto;"
							src="${pageContext.request.contextPath}/static/images/icon_password.png">
						</span> <input type="password" placeholder="请输入密码"
							class="login_pwd login_input_font" style="padding-left: 10px;"
							name="password" id="password" value="<%=password%>" />
					</p>
					<p  style="overflow: hidden;">
						<input type="text" placeholder="请输入验证码"
							class="login_code login_input_font" style="padding-left: 10px;"
							id="yzcode" maxlength="4" value="" /><span id="code"
							style="height: 40; width: 105; display: block; float: left; text-align: center; border-radius: 5px;margin-left:16px;padding: 0px;"
							title="看不清，换一张"></span>
					</p>
					<div style="clear:both;"></div>
					<p class="remember">
						<input type="checkbox" id="checkuser" name="rememberMe" value="true" />
						<label for="checkuser"><font>记住密码</font></label><a href="${pageContext.request.contextPath}/password/forgetPassword?flag=gh" class="forget">忘记密码？</a>
					</p>
					<p>
						<input type="submit" value="登     录" class="btn btn-primary logon" id="loginbutton"/>
					</p>
				</form>
			</div>
			<div class="login_buttom">
				<div style="text-align: center;">
					<a href="javascript:void(0);" id="appdownload1" style="color: #fff;">企业管理员App下载</a>
				</div>
				<div style="margin-top: 18px;text-align: center;">沪ICP备16002681号-2 &nbsp;&nbsp;&nbsp;
					Copyright © 2017 Virtue Intelligent Network Ltd, co.</div>
			</div>
	</div>
</body>
<script type="text/javascript">
	var code;//声明一个变量用于存储生成的验证码  
	document.getElementById("code").onclick = changeImg;
	function changeImg() {
		var codeStyle = '';
		//alert("换图片");  
		var arrays = new Array('1', '2', '3', '4', '5', '6', '7', '8', '9',
				'0', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
				'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
				'V', 'W', 'X', 'Y', 'Z');
		var catchaColor = new Array('MAROON', 'CRIMSON', 'RED',
				'MEDIUMVIOLETRED', 'PALEVIOLETRED', 'MAGENTA', 'INDIGO',
				'DARKVIOLET', 'BLACK', 'VIOLET', 'SADDLEBROWN', 'ROSYBROWN',
				'GOLD', 'BLUE', 'CYAN', 'DARKGREEN', 'GREEN', 'ORANGE', 'PINK');
		code = '';//重新初始化验证码  
		//alert(arrays.length);  
		//随机从数组中获取四个元素组成验证码  
		for (var i = 0; i < 4; i++) {
			//随机获取一个数组的下标  
			var r = parseInt(Math.random() * arrays.length);
			var n = parseInt(Math.random() * catchaColor.length);
			codeStyle = codeStyle + '<font color=' + catchaColor[n] + '>'
					+ arrays[r] + '</font>';
			code += arrays[r];
		}
		document.getElementById('code').innerHTML = codeStyle;//将验证码写入指定区域  
	}

	//效验验证码(表单被提交时触发)  
	function check() {
		//获取用户输入的验证码  
		var input_code = document.getElementById('vcode').value;
		//alert(input_code+"----"+code);  
		if (input_code.toLowerCase() == code.toLowerCase()) {
			//验证码正确(表单提交)  
			return true;
		}
		alert("请输入正确的验证码!");
		//验证码不正确,表单不允许提交  
		return false;
	}

	var retString = "${requestScope.error }";
	if (retString != "") {
		showErrorMsg(retString);
	}
</script>
</html>