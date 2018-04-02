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
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/zzsc.css" />
</head>
<body>
    <div class="head">
      <div class="logo">
      	<img src="${pageContext.request.contextPath}/static/images/logo_retrieve_password.png" style="float:left;" />
        <div class="backToLogin"><a href="../login" style="color:#FFF; text-decoration:none;">返回登录</a></div>
      </div>
    </div>
    <div class="form">
    	<div class="inputArea">
        	<div class="forgetTitle">找回密码</div>    
            <form action="code/checkVertificationCode" method="post" id="checkCode">
                <input type="hidden" name="flag" value="zz" id="gh"/>
                <div class="div-phone">
                  <div class="text">手机号</div>
                  <input type="text" id="phone" name="phoneNumber" class="infos" placeholder="请输入手机号" value="${requestScope.phoneNumber}"/>
                </div>
                <div style="clear:both; margin-top:14px;">&nbsp;</div>
                <div class="div-ranks" >
                  <div class="text">验证码</div>
                  <input type="text" id="ranks" name="code" class="infos" placeholder="请输入验证码" style="width:166px !important;"/>
                  <a href="javascript:;" class="send1">发送验证码</a> 
                </div>
                <div style="clear:both; margin-top:20px;">&nbsp;</div>
                <div class="error_msg" id="error_msg"></div>
                <div class="div-button" style="height:46px;">
                  <input type="button" class="nextStep" id="nextStep" value="下一步" />
                </div>
            </form>
    	</div>
    </div>
    <div style="position:relative;color : #4E4E4E; font-size:14px; width:100%;text-align:center; background-color:#DDDDDD; height:30px; padding-top:10px;"> 沪ICP备16002681号-2  &nbsp;&nbsp;&nbsp; Copyright © 2017 Virtue Intelligent Network Ltd, co.</div>
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