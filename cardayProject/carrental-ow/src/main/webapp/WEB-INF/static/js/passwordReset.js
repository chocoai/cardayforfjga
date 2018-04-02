$(function(){
	var checked=1;
	var i=5;
	var intervalid;
	$('.send1').click(function(){
		if(checked==0){
			return false;
		}
		$('#error_msg').html('');
		var numbers = /^1\d{10}$/;
		var val = $('#phone').val().replace(/\s+/g,""); //获取输入手机号码
		if($('.div-ranks a').attr('class') == 'send1'){
				if(!numbers.test(val) || val.length ==0){
					$('#error_msg').html('<img src="../static/images/icon_error.png" style="margin-top:7px; margin-right:5px; width:14px"/>手机号格式错误');
					return;
				}
		}
		if(numbers.test(val)){
			$.post("../code/checkVertificationCode?v="+Math.random(),
		 	{ 
			 	phoneNumber:val,
			 	code:'000000'
		 	},
		 function(data){
			 if(data.status=='phoneFailure'){
			 	$('#error_msg').html(data.error);
			 	return;
			 }
			var time = 60;
			function timeCountDown(){
				if(time==0){
					clearInterval(timer);
					$('.div-ranks a').addClass('send1').removeClass('send0').html("发送验证码");
					checked = 1;
					return ;
				}
				$('.div-ranks a').html(time+"秒后再次发送");
				time--;
				checked = 0;
				return ;
			}
			$('.div-ranks a').addClass('send0').removeClass('send1');
			
		 	$.post("../code/sendVertificationCode?v="+Math.random(),
			 { 
				 phoneNumber:val
			 },
			 function(data){
				 if(data.status=='success'){
				 	$('#error_msg').html('验证码已发送，请注意查收');
				 }
			 });
			timeCountDown();
			var timer = setInterval(timeCountDown,1000);
		 });
			
		}
	});
	$('#nextStep').click(function(){
		$('#error_msg').html('');
		var phoneVal = $('#phone').val().replace(/\s+/g,"");
		var identificationCode=$('#ranks').val().replace(/\s+/g,"");
		var numbers = /^1\d{10}$/;
		if(phoneVal==''){
		$('#error_msg').html('<img src="../static/images/icon_error.png" style="margin-top:7px; margin-right:5px; width:14px"/>请填写手机号');
			return false;
		}
		if(!numbers.test(phoneVal)){
			$('#error_msg').html('<img src="../static/images/icon_error.png" style="margin-top:7px; margin-right:5px; width:14px"/>手机号格式错误');
			return false;
		}
		if(identificationCode==''){
			$('#error_msg').html('<img src="../static/images/icon_error.png" style="margin-top:7px; margin-right:5px; width:14px"/>请填写验证码');
			return false;
		}
		
		$.post("../code/checkVertificationCode?v="+Math.random(),
			 { 
				 phoneNumber:phoneVal,
				 code:identificationCode
			 },
			 function(data){
				 if(data.status=='success'){
				 	var jumpPage=$('#gh').val();
				 	if(jumpPage=='gh'){
				 		window.location.href='../code/resetPage?flag=gh&phoneNumber='+phoneVal;
				 	}else{
				 		window.location.href='../code/resetPage?flag=zz&phoneNumber='+phoneVal;
				 	}
				 }else if(data.status=='phoneFailure'){
				 	$('#error_msg').html('<img src="../static/images/icon_error.png" style="margin-top:7px; margin-right:5px; width:14px"/>'+data.error);
				 }else{
				 	$('#error_msg').html('<img src="../static/images/icon_error.png" style="margin-top:7px; margin-right:5px; width:14px"/>'+data.error);
				 }
			 });
		
	});
	$('#validatePassword').click(function(){
		$('#error_msg').html('');
		var password1=$('input[name="code"]').val();
		var password2=$('input[name="newPassword"]').val();
		if(password1!=password2){
			$('#error_msg').html('<img src="../static/images/icon_error.png" style="margin-top:7px; margin-right:5px; width:14px"/>两次密码输入不一致');
			return false;
		}
		var password = /^[0-9a-zA-Z]{6,20}$/;
		if(!password.test(password1)){
			$('#error_msg').html('<img src="../static/images/icon_error.png" style="margin-top:7px; margin-right:5px; width:14px"/>密码由6-20位字符组成，含数字、字母');
			return false;
		}
		$('#checkPassword').submit();
		
	});
/*function redirect(){
	$('#jumpPage').html('密码重置已成功，页面将在'+i+'秒钟跳转至登录页面')
	if (i == 0) { 
		window.location.href='./login';
		clearInterval(intervalid); 
	} 
	i--;
}*/
});
