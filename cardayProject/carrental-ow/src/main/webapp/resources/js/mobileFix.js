//获取屏幕宽度
var phoneWidth = parseInt(window.screen.width);
if(phoneWidth>720){phoneWidth = 720;}
//定义缩放比例
var phoneScale = phoneWidth/720;
var ua = navigator.userAgent;
if (/Android (\d+\.\d+)/.test(ua)){
	var version = parseFloat(RegExp.$1);
	// andriod 2.3
	if(version>2.3){
		document.write('<meta name="viewport" content="width=720, minimum-scale = '+phoneScale+', maximum-scale = '+phoneScale+', target-densitydpi=device-dpi">');
	// andriod 2.3以上
	}else{
		document.write('<meta name="viewport" content="width=720, target-densitydpi=device-dpi">');
	}
	// 其他系统
} else {
	document.write('<meta name="viewport" content="width=720, user-scalable=no, target-densitydpi=device-dpi">');
}