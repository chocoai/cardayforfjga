//��ȡ��Ļ���
var pw = parseInt(window.screen.width);
// if(phoneWidth>=720){phoneWidth = 720;}
//�������ű���
var phoneScale = pw/720;
var ua = navigator.userAgent;
if (/Android (\d+\.\d+)/.test(ua)){
	var version = parseFloat(RegExp.$1);
	// andriod 2.3
	if(version>2.3){
		document.write('<meta name="viewport" content="width=720, minimum-scale = '+phoneScale+', maximum-scale = '+phoneScale+', target-densitydpi=device-dpi">');
	// andriod 2.3����
	}else{
		document.write('<meta name="viewport" content="width=720, target-densitydpi=device-dpi">');
	}
	// ����ϵͳ
} else {
	document.write('<meta name="viewport" content="width=720, user-scalable=no, target-densitydpi=device-dpi">');
}