
function getNowDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + date.getHours() + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds();
    return currentdate;
} 


/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.ordermgmt.orderallocate.schedule.ScheduleController', {
    extend: 'Ext.app.ViewController',
    alias : 'controller.scheduleController',
    requires: [
    ],
    
    onAfterRenderRealtimeMap: function() {
    	console.log('++++onAfterRenderRealtimeMap++++');
    	var map = this.lookupReference('realtimeMapPanel').bmap;
    	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
    	var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
    	map.addControl(top_left_control);        
		map.addControl(top_left_navigation);     
    	map.addControl(new BMap.MapTypeControl());
    	map.clearOverlays();
    	var point = new BMap.Point(114.414308, 30.483367);
    	var longitude = 114.414308;
    	var latitude = 30.483367;
    	var geoc = new BMap.Geocoder();
    	var address = '';
    	var tracetime = getNowDate();
    	//逆地址解析
		geoc.getLocation(point, function(rs){
			var addComp = rs.addressComponents;
			address = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
		
			map.centerAndZoom(point, 15);
			var myIcon;
    		if (typeof(speed) == undefined || speed == '0') {
    			myIcon = new BMap.Icon("resources/images/icons/icon_home_parkinglocated.png", new BMap.Size(60,50),{    //小车图片
    				imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
    			});
    		}else{
    			myIcon = new BMap.Icon("resources/images/icons/icon_home_drivinglocated.png", new BMap.Size(60,50),{    //小车图片
    				imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
    			});
    		}
	    	var marker = new BMap.Marker(point,{icon:myIcon});  // 创建标注
	    	map.addOverlay(marker);               // 将标注添加到地图中
	    	var content = '时间：' + tracetime + '<br/>状态：' + status + '<br/>经度：' + longitude + '<br/>纬度：' + latitude + '<br/>'
	    				+ '速度：约' + speed + 'km/h<br/>地址：' + address;
	    	var opts = {
					width : 230,     // 信息窗口宽度
					height: 120,     // 信息窗口高度
					enableMessage:true//设置允许信息窗发送短息
	    	};
			var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
			map.openInfoWindow(infoWindow,point); //开启信息窗口
			marker.addEventListener("click",function(e){
				var opts = {
						width : 230,     // 信息窗口宽度
						height: 120,     // 信息窗口高度
						enableMessage:true//设置允许信息窗发送短息
					   };
				var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
				map.openInfoWindow(infoWindow,point); //开启信息窗口
			}
			);
		});
    },
    loadTimerElement : function() {
    	var win = Ext.getCmp("vehicleScheduleWindow");
    	var vehicleNumber = win.vehiclenumber;
    	var vehicleId = win.vehicleId;
    	var i = 10;
    	var imei = '898602B5091581519192';
    	
    	//定义地图控件
    	var map = this.lookupReference('realtimeMapPanel').bmap;
    	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
    	var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
    	map.addControl(top_left_control);        
		map.addControl(top_left_navigation);     
    	map.addControl(new BMap.MapTypeControl());
    	
    	var resetMkPoint = function() {
    		console.log('i::' + i);
    		if(i > 1){
				i--;
			}else {
				offsetSideMap(imei);
//				loadVehicleCountInfo();
				i = 10;
			}
    		Ext.getCmp("scheduleTopPanel").getViewModel().set('vehicleNum', i);
    	};
    	
    	task = {  
    		    run: resetMkPoint,  
    		    interval: 1000 //1 second  
    		};
    	
    	runner.start(task);
    	
    	
    	//定时任务
    	var offsetSideMap = function(imei) {
    		console.log("10秒更新实时定位");
    		var tracetime = getNowDate();
    		console.log('tracetime:' + tracetime);
    		//ajax调用接口获取实时经纬度
    		var input = {'deviceNumber' : imei};
    		
    		Ext.Ajax.request({
    	   		url: 'vehicle/queryObdLocationByImei?json=' + Ext.encode(input),
    	        method : 'GET',
    	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
    	        success : function(response,options) {
    	        	var respText = Ext.util.JSON.decode(response.responseText);
    				var data = [];
    		    	data[0] = respText.data;
    		    	
    		    	//地图定位
    	    		map.clearOverlays();
    	    		var point = new BMap.Point(data[0].longitude, data[0].latitude);
    	        	map.centerAndZoom(point, 15);
    	        	var speed = data[0].speed;
    	        	var myIcon;
		    		if (typeof(speed) == undefined || speed == '0') {
		    			myIcon = new BMap.Icon("resources/images/icons/icon_home_parkinglocated.png", new BMap.Size(60,50),{    //小车图片
		    				imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
		    			});
		    		}else{
		    			myIcon = new BMap.Icon("resources/images/icons/icon_home_drivinglocated.png", new BMap.Size(60,50),{    //小车图片
		    				imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
		    			});
		    		}

    	        	var marker = new BMap.Marker(point,{icon:myIcon});
//    	        	var marker = new BMap.Marker(point);  // 创建标注
    	        	map.addOverlay(marker);               // 将标注添加到地图中
//    	        	marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
    	        	var opts = {
    						width : 230,     // 信息窗口宽度
    						height: 120,     // 信息窗口高度
//    						title : "信息窗口" , // 信息窗口标题
    						enableMessage:true//设置允许信息窗发送短息
    	        	};
    	        	
    	        	
    	        	var geoc = new BMap.Geocoder();
    	        	var address = '';
    	        	//逆地址解析
    	    		geoc.getLocation(point, function(rs){
    	    			var addComp = rs.addressComponents;
    	    			address = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
    	    			var content = '时间：' + tracetime + '<br/>状态：' + data[0].status + '<br/>经度：' + data[0].longitude + '<br/>纬度：' + data[0].latitude + '<br/>'
        				+ '速度：约' + data[0].speed + 'km/h<br/>地址：' + address;
        	
						var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
						map.openInfoWindow(infoWindow,point); //开启信息窗口
			    		marker.addEventListener("click",function(e){
			    			var opts = {
			    					width : 230,     // 信息窗口宽度
			    					height: 120,     // 信息窗口高度
			//    					title : "信息窗口" , // 信息窗口标题
			    					enableMessage:true//设置允许信息窗发送短息
			    				   };
			    			var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
			    			map.openInfoWindow(infoWindow,point); //开启信息窗口
			    		}
			    		);
    	    		});
    	        }
    	        /*,
    	        failure : function() {
    	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
    	        },*/
    	    });
    	};
    	
    	
    }
});