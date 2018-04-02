/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.window_dialog.ErrorEventController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.errorEventController',
    requires: [
    ],
    
    onAfterRendererrorEventMap : function() {
    	//定义地图控件
    	var map = this.lookupReference('errorEventMapPanel').bmap;
    	var lng = Ext.getCmp('errorEventMapWindow').lng; 
    	var lat = Ext.getCmp('errorEventMapWindow').lat;
    	var alertPosition = Ext.getCmp('errorEventMapWindow').alertPosition;
    	var alertTime = Ext.getCmp('errorEventMapWindow').alertTime;
    	alertTime = formatDate2Var(alertTime);
    	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
    	var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
    	map.addControl(top_left_control);
		map.addControl(top_left_navigation);
    	map.addControl(new BMap.MapTypeControl());
    	var point = new BMap.Point(lng, lat);
    	map.centerAndZoom(point, 15);
    	var marker = new BMap.Marker(point);  // 创建标注
    	map.addOverlay(marker);               // 将标注添加到地图中
//    	marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
    	var content = '报警时间：' + alertTime
    		+ '<br/>经度：' + lng
    		+ '<br/>纬度：' + lat
    		+ '<br/>报警地址：' + alertPosition;
    	var opts = {
				width : 250,     // 信息窗口宽度
				height: 70,     // 信息窗口高度
//				title : "信息窗口" , // 信息窗口标题
				enableMessage:true//设置允许信息窗发送短息
			   };
    	var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
    	map.openInfoWindow(infoWindow,point); //开启信息窗口
    	
    	function formatDate2Var(val) {
    		var time = new Date(val);
        	var y = time.getFullYear();
        	var m = time.getMonth()+1;
        	var d = time.getDate();
        	var h = time.getHours();
        	var mm = time.getMinutes();
        	var s = time.getSeconds();
        	function add0(m){return m<10?'0'+m:m }
        	return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);
    	}
    }


});

