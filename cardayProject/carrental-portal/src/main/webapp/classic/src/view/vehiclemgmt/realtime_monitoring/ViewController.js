function queryPanoramaByLocation(lng, lat) {
	var win = Ext.widget("panoramaMapWindow", {
		title: '街道全景图',
		closable: true,
		buttonAlign : 'center',
		lng : lng,
		lat : lat,
	});
	win.show();
}

function getAddressByPoint (point) {
	var address = '';
	var geoc = new BMap.Geocoder();
	geoc.getLocation(point, function(rs){
		var addComp = rs.addressComponents;
	    address = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
	});
	return address;
}

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
var runner = new Ext.util.TaskRunner();
var task = {};

Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.ViewController', {
    extend: 'Ext.app.ViewController',
    requires: [
    ],
    alias : 'controller.realtimeMonitoringcontroller',
    
    
    
    loadVehicleRealtimeReport: function() {
//    	console.log('+++loadVehicleRealtimeReport+++');
//    	this.getViewModel().getStore("vehicleRealtimeReport").proxy.url = "app/data/vehiclemgmt/realtime_monitoring/vehicleData.json";
//    	this.getViewModel().getStore("vehicleRealtimeReport").load();
    	var input = {
				"vehicleType":"-1",
				"fromOrgId":"-1",
				"deptId":"-1"
		};
		var json = Ext.encode(input);
    	Ext.Ajax.request({
//			url : 'app/data/vehiclemgmt/realtime_monitoring/vehicleData.json',
//			url : 'vehicle/queryObdLocationList?json={"vehicleType":"-1","fromOrgId":"-1","deptId":"-1"}',
			url : 'vehicle/queryObdLocationList',
			method : 'POST',
//			defaultHeaders : {
//				'Content-type' : 'application/json;utf-8'
//			},
			params:{json:json},
			success : function(response, options) {

				var respText = Ext.util.JSON.decode(response.responseText);
		    	var data = respText.data;
		    	
//		    	console.log('vehiclenumber:' + data[0].vehiclenumber);
		    	
		    	this.getViewModel().getStore("vehicleRealtimeReport").loadData(data);
		    	//加载地图
		    	this.onAfterRenderPanelTrackingMap(data);
			},
//			failure : function() {
//				Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
//			},
			scope : this
		});
    },
    onAfterRenderPanelTrackingMap : function(data) {
//    	console.log('+++onAfterRenderPanelTrackingMap+++');
    	var map = this.lookupReference('bmappanel').bmap;
    	map.clearOverlays();
    	map.reset();
    	var points = new Array();
    	//添加比例尺控件
    	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
    	var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
    	map.addControl(top_left_control);        
		map.addControl(top_left_navigation);     
    	map.enableAutoResize();
//    	var point = new BMap.Point(114.3162001, 30.58108413);
    	var point;
//    	map.centerAndZoom(point, 6);
//    	map.panBy(280, 200);
//    	this.offsetMap(map); //对地图做偏移，以保证起点显示在正中 
    	var opts = {
				width : 250,     // 信息窗口宽度
				height: 120,     // 信息窗口高度
//				title : "信息窗口" , // 信息窗口标题
				enableMessage:true//设置允许信息窗发送短息
			   };
    	var queryPanoramaByLocation = function (){
//    		console.log('+++++++queryPanoramaByLocation+++++++');
    		var panorama = new BMap.Panorama(map); 
    		panorama.setPov({heading: -40, pitch: 6});
    		panorama.setPosition(new BMap.Point(114.29821255382512, 30.589583523896508));
    	};
    	// 编写自定义函数,创建标注
    	function addMarker(point, status, speed, tracetime, vehiclenumber, imei){
    		var myIcon;
    		/*if (typeof(speed) == undefined || speed == '0') {
    			myIcon = new BMap.Icon("resources/images/icons/icon_home_parkinglocated.png", new BMap.Size(60,50),{    //小车图片
    				imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
    			});
    		}else{
    			myIcon = new BMap.Icon("resources/images/icons/icon_home_drivinglocated.png", new BMap.Size(60,50),{    //小车图片
    				imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
    			});
    		}*/
    		
            if (status.indexOf('行驶') == 0) {
                myIcon = new BMap.Icon("resources/images/icons/icon_bussiness_driving.png", new BMap.Size(80, 80), { //小车图片
                    imageOffset: new BMap.Size(25, 0) //图片的偏移量。为了是图片底部中心对准坐标点。
                });

            } else if (status.indexOf('停止') == 0) {
                myIcon = new BMap.Icon("resources/images/icons/icon_bussiness_parking.png", new BMap.Size(80, 80), { //小车图片
                    imageOffset: new BMap.Size(25, 0) //图片的偏移量。为了是图片底部中心对准坐标点。
                });
            } else if (status.indexOf('离线') == 0) {
                myIcon = new BMap.Icon("resources/images/icons/icon_bussiness_offline.png", new BMap.Size(80, 80), { //小车图片
                    imageOffset: new BMap.Size(25, 0) //图片的偏移量。为了是图片底部中心对准坐标点。
                });
            }

    	  var marker = new BMap.Marker(point,{icon:myIcon});
    	  map.addOverlay(marker);
    	  
    	  	var address = '';
    		var geoc = new BMap.Geocoder();
    		geoc.getLocation(point, function(rs){
    			var addComp = rs.addressComponents;
    			if(addComp.province == '北京市') {
    				address = addComp.city + addComp.district + addComp.street + addComp.streetNumber;
    			}else{
    				address = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
    			}
    		    var content = '时间：' + tracetime
    		    + '<br/>状态：' + status
    		    + '<br/>经度：' + point.lng 
    		    + '<br/>纬度：' + point.lat
    		    + '<br/>速度：约' + speed + 'km/h'
    		    + '<br/>地址：' + address
    		    + '<br/>街景：<a href="javascript:void(0);" onclick="queryPanoramaByLocation(' + point.lng + ',' + point.lat + ')"><font color="red">全景地图展开</font></a>';
    		    addClickHandler(content,marker, imei, point.lng, point.lat, vehiclenumber, speed,
    		    				status, tracetime);
    		}); 
    	}
    	// 随机向地图添加25个标注
//    	var bounds = map.getBounds();
//    	var sw = bounds.getSouthWest();
//    	var ne = bounds.getNorthEast();
    	for(var i=0;i<data.length;i++) {
    		var point = new BMap.Point(data[i].longitude,data[i].latitude);
    		addMarker(point, data[i].status, data[i].speed, data[i].tracetime,
    					data[i].vehiclenumber, data[i].imei);
    		points.push(point);
    	}
    	
    	function addClickHandler(content, marker,
    							 imei, longitude, latitude, vehiclenumber, speed, status, tracetime){
    		//绑定点击事件
    		marker.addEventListener("click",function(e){
    			//加载消息窗口
//    			openInfo(content,e)
//    			showRealtimeDataWindow();
    			openRealtimeView(imei, longitude, latitude, vehiclenumber, speed, status, tracetime);
    			}
    		);
    	}
    	function openInfo(content,e){
    		var p = e.target;
    		var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
    		var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
    		map.openInfoWindow(infoWindow,point); //开启信息窗口
    	}
    	function openRealtimeView(imei, longitude, latitude, vehiclenumber, speed, status, tracetime) {
    		var win = Ext.widget("realtimeMapWindow", {
//    			title: '车辆实时定位——' + rec.data.vehicle_num + '——窗口将在10秒后刷新',
    			closable: true,
    			buttonAlign : 'center',
    			imei : imei,
    			longitude : longitude,
    			latitude : latitude,
    			vehiclenumber : vehiclenumber,
    			speed : speed,
    			status : status,
    			tracetime : tracetime,
    			listeners:{
    				close:function() {
    					runner.stop(task);
    				}
    			}
    		});
    		win.show();
    	}
//    	console.log('length:' + points.length);
//    	map.addControl(new BMap.MapTypeControl());
    	if(points.length != 0){
//    		console.log('getCenter():' + JSON.stringify(map.getCenter()));
//    		console.log('getViewport:' + JSON.stringify(map.getViewport(points)));
//    		map.setViewport(points);
    		if(points.length == 1) {
    			map.centerAndZoom(points[0], 13);
    		}else {
    			map.setViewport(points);
    		}
    	}
//    	console.log('vehicleMonitoringListGrid..width:' + Ext.getCmp('vehicleMonitoringListGrid').getWidth());
//    	console.log('leftGridWidth:' + leftGridWidth);
//    	map.setZoom(12);
//    	map.panBy(leftGridWidth, 0);
//    	map.addControl(new BMap.MapTypeControl({mapTypes:[BMAP_NORMAL_MAP,BMAP_SATELLITE_MAP]}));
    },
    
    offsetMap: function(map) {
        var xOffset = -(this.lookupReference('bmappanel').getWidth() - this.lookupReference('vehicleTrackingMap').getWidth())/2 ;
        var yOffset = -(this.lookupReference('bmappanel').getHeight() - this.lookupReference('vehicleTrackingMap').getHeight())/2 ;
//        console.log('bmappanel.width:' + this.lookupReference('bmappanel').getWidth());
//        console.log('vehicleTrackingMap.width:' + this.lookupReference('vehicleTrackingMap').getWidth());
//        console.log('bmappanel.Height:' + this.lookupReference('bmappanel').getHeight());
//        console.log('vehicleTrackingMap.Height:' + this.lookupReference('vehicleTrackingMap').getHeight());
        var panOptions = {
            noAnimation: true
        };
//        console.log('xofset:' + xOffset);
//        console.log('yOffset:' + yOffset);
//        yOffset =-319;
        map.panBy(xOffset, yOffset, panOptions);
    },
    showRealtimeDataWindow: function(grid, rowIndex, colIndex){
    	var rec = grid.getStore().getAt(rowIndex);
    	if (rec.data.realname == null || rec.data.realname == "") {
    		rec.data.realname = "--";
    	}
    	if (rec.data.phone == null || rec.data.phone == "") {
    		rec.data.phone = "--";
    	}
//    	console.log('selected vehicle imei:' + rec.data.imei);
//    	var win = Ext.widget("realtimeDataWindow", {
    	var win = Ext.widget("realtimeMapWindow", {
//			title: '车辆实时定位——' + rec.data.vehicle_num + '——窗口将在10秒后刷新',
			closable: true,
			buttonAlign : 'center',
			imei : rec.data.imei,
			longitude : rec.data.longitude,
			latitude : rec.data.latitude,
			vehiclenumber : rec.data.vehiclenumber,
			speed : rec.data.speed,
			status : rec.data.status,
			tracetime : rec.data.tracetime,
			realname : rec.data.realname,
			phone : rec.data.phone,
			listeners:{
				close:function() {
					runner.stop(task);
				}
			}
		});
		win.show();
    },
    loadTimerElement : function() {
//    	console.log('title::' + Ext.getCmp('realtimeMapWindow').title);
    	var win = Ext.getCmp("realtimeMapWindow");
    	var vehicleNumber = win.vehiclenumber;
//    	console.log('+++loadTimerElement+++imei:' + win.imei);
    	var imei = win.imei;
    	var i = 10;
//    	resetMkPoint(i);
    	
    	//定义地图控件
    	var map = this.lookupReference('realtimeMapPanel').bmap;
    	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
    	var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
    	map.addControl(top_left_control);        
		map.addControl(top_left_navigation);     
    	map.addControl(new BMap.MapTypeControl());
    	
    	
//    	function resetMkPoint(i){
//			if(i > 1){
//				i--;
//			}else {
//				this.loadRealtimeMapPanel(map);
//				i = 10;
//			}
//			win.setTitle('车辆实时定位——' + vehicleNumber + '——窗口将在' + i + '秒后刷新');
//			setTimeout(function(){
//				resetMkPoint(i);
//			},1000);
//		}
    	
    	var resetMkPoint = function() {
//    		console.log('i::' + i);
    		if(i > 1){
				i--;
			}else {
				offsetSideMap(imei);
//				loadVehicleCountInfo();
				i = 10;
			}
    		win.setTitle('车辆实时定位——' + vehicleNumber + '——窗口将在' + i + '秒后刷新');
    	};
    	
    	task = {  
    		    run: resetMkPoint,  
    		    interval: 1000 //1 second  
    		};
    	
    	runner.start(task);
    	
//      task = runner.start({
//            run: resetMkPoint,
//            interval: 1000,
//      });
    	
    	
    	//定时任务
    	var offsetSideMap = function(imei) {
//    		console.log("10秒更新实时定位");
    		var tracetime = getNowDate();
    		//ajax调用接口获取实时经纬度
//    		console.log('+++offsetSideMap+++imei:' + imei);
    		var input = {'deviceNumber' : imei};
//    		console.log("input:" +  Ext.encode(input));
    		var json = Ext.encode(input);
    		Ext.Ajax.request({
    	   		url: 'vehicle/queryObdLocationByImei',
    	        method : 'POST',
    	        params : {json:json},
    	        success : function(response,options) {
    	        	var respText = Ext.util.JSON.decode(response.responseText);
    				var data = [];
    		    	data[0] = respText.data;
//    		    	console.log('lng:' + data[0].longitude + ';lat' + data[0].latitude);
    		    	if (data[0].realname==null || data[0].realname == '') {
    		    		data[0].realname = '--';
    		    	}
    		    	if (data[0].phone==null || data[0].phone == '') {
    		    		data[0].phone = '--';
    		    	}
    		    	//地图定位
    	    		map.clearOverlays();
//    	        	var point = new BMap.Point(114.414308, 30.483367);
//    	    		var point = new BMap.Point(116.582568, 40.042344);
    	    		var point = new BMap.Point(data[0].longitude, data[0].latitude);

                    function draw_line_direction(weight) {
                        var icons=new BMap.IconSequence(
                          new BMap.Symbol('M0 -5 L-5 -2 L0 -4 L5 -2 Z', {
                            scale: weight/10,
                            strokeWeight: 2,
                            rotation: 0,
                            fillColor: 'white',
                            fillOpacity: 1,
                            strokeColor:'white'
                          }),'100%','5%',false);
                        return icons;
                      }
    	    		
    	        	var mappanel = Ext.getCmp("realtimeMapWindow").down("mappanel");
    	        	if(mappanel.trackLine == null){
    	        		var polyline = new BMap.Polyline([point], {strokeColor:"green", strokeWeight:4, strokeOpacity:0.5,icons:[draw_line_direction(8)]});  //创建多边形
    	        		polyline.disableMassClear();	//禁止轨迹线在map.clearOverlays方法中被清除
    	            	map.addOverlay(polyline);   //增加轨迹线
    	            	mappanel.trackLine = polyline;
    	        	}else{
    	        		var polyline = mappanel.trackLine;
    	        		var path = polyline.getPath();
    	        		path.push(point);
    	        		polyline.setPath(path);
    	        	}
    	    		
//    	        	map.centerAndZoom(point, 15);
    	        	var speed = data[0].speed;
    	        	var myIcon;
		    		if (typeof(speed) == undefined || speed == '0') {
		    			myIcon = new BMap.Icon("resources/images/icons/icon_home_parkinglocated.png", new BMap.Size(48,98),{    //小车图片
		    				imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
		    			});
		    		}else{
		    			myIcon = new BMap.Icon("resources/images/icons/icon_home_drivinglocated.png", new BMap.Size(48,98),{    //小车图片
		    				imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
		    			});
		    		}

    	        	var marker = new BMap.Marker(point,{icon:myIcon});
//    	        	var marker = new BMap.Marker(point);  // 创建标注
    	        	map.addOverlay(marker);               // 将标注添加到地图中
//    	        	marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
    	        	var opts = {
    						width : 230,     // 信息窗口宽度
    						height: 170,     // 信息窗口高度
//    						title : "信息窗口" , // 信息窗口标题
    						enableMessage:true//设置允许信息窗发送短息
    	        	};
    	        	
    	        	
    	        	var geoc = new BMap.Geocoder();
    	        	var address = '';
    	        	//逆地址解析
    	    		geoc.getLocation(point, function(rs){
    	    			var addComp = rs.addressComponents;
    	    			if(addComp.province == '北京市'){
    	    				address = addComp.city + addComp.district + addComp.street + addComp.streetNumber;
    	    			}else{
    	    				address = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
    	    			}
    	    			var content = '时间：' + data[0].tracetime + '<br/>状态：' + data[0].status + '<br/>经度：' + data[0].longitude + '<br/>纬度：' + data[0].latitude + '<br/>方向：' + getDirection(data[0].bearing, data[0].longitude, data[0].latitude) + '<br/>'
        				+ '速度：约' + data[0].speed + 'km/h<br/>地址：' + address+ '<br/>司机姓名：' + data[0].realname + '<br/>司机手机号：' + data[0].phone;
    	    			
						var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
//						map.openInfoWindow(infoWindow,point); //开启信息窗口
			    		marker.addEventListener("click",function(e){
			    			var opts = {
			    					width : 230,     // 信息窗口宽度
			    					height: 160,     // 信息窗口高度
			//    					title : "信息窗口" , // 信息窗口标题
			    					enableMessage:true//设置允许信息窗发送短息
			    				   };
			    			var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
			    			map.openInfoWindow(infoWindow,point); //开启信息窗口
			    		}
			    		);
    	    		});
    	        }
//    	        ,
//    	        failure : function() {
//    	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//    	        },
    	    });
    		
//    		//表数据加载
//        	Ext.getCmp("realtimeDataGrid").getViewModel().getStore("tripTraceErrorStore").proxy.url = "app/data/vehiclemgmt/realtime_monitoring/triptraceErrorData.json";
//        	Ext.getCmp("realtimeDataGrid").getViewModel().getStore("tripTraceErrorStore").load();
    		
    		
    	
    	};
    	
    	var loadVehicleCountInfo = function() {
//        	console.log('+++++loadVehicleCountInfo++++');
        	var objectModel = new Ext.data.Model();
        	Ext.Ajax.request({
    	   		url: 'app/data/vehiclemgmt/realtime_monitoring/vehicleCountData.json',
    	        method : 'GET',
    	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
    	        success : function(response,options) {
//    	        	console.log('response.responseText:' + response.responseText);
    	        	var respText = Ext.util.JSON.decode(response.responseText);
    		    	var data = respText.data;
    	        	var retStatus = respText.status;
//    	        	console.log('status:' + retStatus);
    				if (retStatus == 'success') {
    					var win = Ext.getCmp("vehicleCountInfo");
//    					console.log('carNum:' + JSON.stringify(data));
    					objectModel.data = data;
//    					console.log('data:' + objectModel.data);
    					win.down("form").loadRecord(objectModel);
    				}
    	        }
//    	        ,
//    	        failure : function() {
//    	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//    	        },
    	    });
        };
    	
    },
    
    onAfterRenderRealtimeMap : function(panel, options) {
    	console.log('++++onAfterRenderRealtimeMap++++');
    	var imei = this.getView().imei;
    	var longitude;
    	var latitude;
    	var realname;
    	var phone;
    	var speed;
    	var status;
    	var tracetime;
    	var bearing;
    	console.log('imei:' + imei);
    	var input = {'deviceNumber' : imei};
    	var json = Ext.encode(input);
    	Ext.Ajax.request({
    		url: 'vehicle/queryObdLocationByImei',
    		method : 'POST',
    		async:false,
    		params : {json:json},
    		success : function(response,options) {
    			var respText = Ext.util.JSON.decode(response.responseText);
    		    var data = [];
    		    data[0] = respText.data;
    		    longitude = data[0].longitude;
    		    latitude = data[0].latitude;
    		    speed = data[0].speed;
    		    status = data[0].status;
    		    tracetime = data[0].tracetime;
    		    realname = data[0].realname;
    		    phone = data[0].phone;
    		    bearing = data[0].bearing;
    	    	if (realname == null ||realname == "") {
    	    		realname = "--";
    	    	} 
    	    	if (phone == null || phone == "") {
    	    		phone = "--";
    	    	}
    		}
//    		,
//    		failure : function() {
//    		    Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//    		},
    	});
    	
    	
//    	tracetime = getNowDate();
//    	console.log('onAfterRenderPanelTrackingMap++vehicleNum:' + vehiclenumber);
//    	console.log('longitude:' + longitude);
//    	console.log('latitude:' + latitude);
//    	console.log('++++onAfterRenderRealtimeMap+++tracetime+++' + tracetime);
    	var map = this.lookupReference('realtimeMapPanel').bmap;
    	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
    	var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
    	map.addControl(top_left_control);        
		map.addControl(top_left_navigation);     
//		map.addControl(new BMap.MapTypeControl({mapTypes:[BMAP_NORMAL_MAP,BMAP_SATELLITE_MAP]}));
    	map.addControl(new BMap.MapTypeControl());
    	this.loadRealtimeMapPanel(map, longitude, latitude, speed, status, tracetime, realname, phone, bearing);
    	
//    	//定时任务
//    	var offsetSideMap = function() {
//    		console.log("首次加载地图");
//    		//地图定位
//    		map.clearOverlays();
//        	var point = new BMap.Point(114.414308, 30.483367);
//        	map.centerAndZoom(point, 15);
//        	var marker = new BMap.Marker(point);  // 创建标注
//        	map.addOverlay(marker);               // 将标注添加到地图中
//        	marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
//        	var content = '时间：2016-12-28 14:15:32<br/>状态：行驶中<br/>经度：114.414308<br/>纬度：30.483367<br/>'
//        				+ '方向：向西<br/>速度：约10km/h<br/>地址：武汉市珞瑜路';
//        	var opts = {
//					width : 230,     // 信息窗口宽度
//					height: 120,     // 信息窗口高度
////					title : "信息窗口" , // 信息窗口标题
//					enableMessage:true//设置允许信息窗发送短息
//        	};
//			var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
//			map.openInfoWindow(infoWindow,point); //开启信息窗口
//    		marker.addEventListener("click",function(e){
//    			var opts = {
//    					width : 230,     // 信息窗口宽度
//    					height: 120,     // 信息窗口高度
////    					title : "信息窗口" , // 信息窗口标题
//    					enableMessage:true//设置允许信息窗发送短息
//    				   };
//    			var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
//    			map.openInfoWindow(infoWindow,point); //开启信息窗口
//    		}
//    		);
//    		
////        	//表数据加载
////        	console.log('search for vehicle num for' + vehicleNum);
////        	Ext.getCmp("realtimeDataGrid").getViewModel().getStore("tripTraceErrorStore").proxy.url = "app/data/vehiclemgmt/realtime_monitoring/triptraceErrorData.json";
////        	Ext.getCmp("realtimeDataGrid").getViewModel().getStore("tripTraceErrorStore").load();
//    	};
//    	//定时器 30秒轮询
//        var runner = new Ext.util.TaskRunner(),
//            task = runner.start({
//                 run: offsetSideMap,
//                 interval: 30000,
//            });
    },
    loadRealtimeMapPanel : function(map, longitude, latitude, speed, status, tracetime, realname, phone, bearing) {
//    	console.log("加载地图控件");
		//地图定位
		map.clearOverlays();
//    	var point = new BMap.Point(114.414308, 30.483367);
//    	var point = new BMap.Point(116.582568, 40.042344);
    	var point = new BMap.Point(longitude, latitude);
/*
        function draw_line_direction(weight) {
            var icons=new BMap.IconSequence(
              new BMap.Symbol('M0 -5 L-5 -2 L0 -4 L5 -2 Z', {
                scale: weight/10,
                strokeWeight: 2,
                rotation: 0,
                fillColor: 'white',
                fillOpacity: 1,
                strokeColor:'white'
              }),'100%','5%',false);
            return icons;
          }*/
    	//116.403406,39.919805
    	
    	var mappanel = this.lookupReference('realtimeMapPanel');
    	if(mappanel.trackLine == null){
//    		var polyline = new BMap.Polyline([point], {strokeColor:"green", strokeWeight:4, strokeOpacity:0.5,icons:[draw_line_direction(8)]});  //创建多边形
    		var polyline = new BMap.Polyline([point], {strokeColor:"green", strokeWeight:4, strokeOpacity:0.5});  //创建多边形
    		polyline.disableMassClear();	//禁止轨迹线在map.clearOverlays方法中被清除
        	map.addOverlay(polyline);   //增加轨迹线
        	mappanel.trackLine = polyline;
    	}else{
    		var polyline = mappanel.trackLine;
    		var path = polyline.getPath();
    		path.push(point);
    		polyline.setPath(path);
    	}
    	
//    	var geoc = Ext.getCmp('realtimeMapPanel').geoc;
    	var geoc = new BMap.Geocoder();
//    	var point = new BMap.Point(114.3162001, 30.58108413);
    	var address = '';
    	//逆地址解析
		geoc.getLocation(point, function(rs){
			var addComp = rs.addressComponents;
			if(addComp.province == '北京市'){
				address = addComp.city + addComp.district + addComp.street + addComp.streetNumber;
			}else{
				address = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
			}
		
			map.centerAndZoom(point, 15);
			var myIcon;
    		if (typeof(speed) == undefined || speed == '0') {
    			myIcon = new BMap.Icon("resources/images/icons/icon_home_parkinglocated.png", new BMap.Size(48,98),{    //小车图片
    				imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
    			});
    		}else{
    			myIcon = new BMap.Icon("resources/images/icons/icon_home_drivinglocated.png", new BMap.Size(48,98),{    //小车图片
    				imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
    			});
    		}
	    	var marker = new BMap.Marker(point,{icon:myIcon});  // 创建标注
	    	map.addOverlay(marker);               // 将标注添加到地图中
//	    	marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
	    	var content = '时间：' + tracetime + '<br/>状态：' + status + '<br/>经度：' + longitude + '<br/>纬度：' + latitude + '<br/>方向：' + getDirection(bearing, longitude, latitude) + '<br/>'
	    				+ '速度：约' + speed + 'km/h<br/>地址：' + address + '<br/>司机姓名：' + realname + '<br/>司机手机号：' + phone;
	    	var opts = {
					width : 230,     // 信息窗口宽度
					height: 160,     // 信息窗口高度
//					title : "信息窗口" , // 信息窗口标题
					enableMessage:true//设置允许信息窗发送短息
	    	};
			var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
//			map.openInfoWindow(infoWindow,point); //开启信息窗口
			marker.addEventListener("click",function(e){
				var opts = {
						width : 230,     // 信息窗口宽度
						height: 120,     // 信息窗口高度
//						title : "信息窗口" , // 信息窗口标题
						enableMessage:true//设置允许信息窗发送短息
					   };
				var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
				map.openInfoWindow(infoWindow,point); //开启信息窗口
			}
			);
		});
		
		function GetDateStr(AddDayCount) {
			var dd = new Date();
			dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
			var y = dd.getFullYear();
			var m = dd.getMonth()+1;//获取当前月份的日期
			var d = dd.getDate();
			return y+"-"+m+"-"+d;
		}
		
//		console.log('前7天:' + GetDateStr(-7));
//		
//		console.log('今天:' + GetDateStr(0));
		
		var startTime = GetDateStr(-7) + ' 00:00:00';
		var endTime = GetDateStr(0) + ' 23:59:59';
		
    	//表数据加载
//    	Ext.getCmp("realtimeDataGrid").getViewModel().getStore("tripTraceErrorStore").proxy.url = "app/data/vehiclemgmt/realtime_monitoring/triptraceErrorData.json";
		var vehicleNumber = Ext.getCmp('realtimeMapWindow').vehiclenumber;
		console.log('vehicleNumber:' + vehicleNumber);
		console.log('startTime:' + startTime);
		console.log('endTime:' + endTime);
		var input = {
	    		'vehicleNumber' : vehicleNumber,	
	    		'startTime' : startTime,	
	    		'endTime' : endTime
	    	};
	    	
	    input = Ext.encode(input);
	    console.log('load tripTraceErrorStore');
	    Ext.getCmp('realtimeDataGrid').getViewModel().getStore("tripTraceErrorStore").proxy.extraParams = {
				"json" : input
		};
    	Ext.getCmp("realtimeDataGrid").getViewModel().getStore("tripTraceErrorStore").load();
    },
    showTripTraceWindow : function(grid, rowIndex, colIndex) {
//    	console.log('+++showTripTraceWindow+++');
    	var rec = grid.getStore().getAt(rowIndex);
//    	console.log('imei:' + rec.data.imei);
    	var win = Ext.widget("tripTraceWindow", {
			title: '车辆历史轨迹',
			closable: true,
			buttonAlign : 'center',
			imei : rec.data.imei
		});
		win.show();
    },
    loadTripTraceMapView: function() {
    	var traceMap = this.lookupReference('tripTraceMapPanel').bmap;
    	Ext.Ajax.request({
									//      	     url: 'web/ui/findTripTraceDataByTimeRange?jsonString={imei:' + param + ',starttime:\"'+ beginDate + '\",endtime:\"'+ endDate +'\"}',
//									url : 'app/data/vehiclemgmt/realtime_monitoring/triptraceMapData.json',
									url : 'app/data/vehiclemgmt/realtime_monitoring/playTripTraceData.json',
									timeout : 30000,
									method : 'GET',
									scope : this,

									success : function(resp, opts) {
										var location = Ext.util.JSON
												.decode(resp.responseText);
										traceMap.clearOverlays();
										if (location.success) {
											var detailLocation = Ext.util.JSON
													.decode(location.data[0].tracegeometry);
											var BPointArr = new Array();
											var pstart, pend, startPoint, endPoint;
											//找起点

											var locationArrayList = new Array();
											var numArray = 0;

											for (var i = 0; i < detailLocation.length; i++) {
												if (detailLocation[i].lng != null
														&& detailLocation[i].lat != null) {
													locationArrayList[numArray] = detailLocation[i];
													numArray++;
												}
											}
											
//											console.log('pts:' + JSON.stringify(locationArrayList));

											for (var i = 0; i < locationArrayList.length - 1; i++) {

												//                   console.log("Hi I am i::::" + i);

												pstart = new BMap.Point(
														locationArrayList[i].lng,
														locationArrayList[i].lat);
												BPointArr
														.push(new BMap.Point(
																locationArrayList[i].lng,
																locationArrayList[i].lat));

												pend = new BMap.Point(
														locationArrayList[i + 1].lng,
														locationArrayList[i + 1].lat);
												BPointArr
														.push(new BMap.Point(
																locationArrayList[i + 1].lng,
																locationArrayList[i + 1].lat));

												var mstart = new BMap.Marker(
														pstart);
												var mend = new BMap.Marker(pend);
											}

											if (locationArrayList.length > 0) {
												startPoint = new BMap.Point(
														locationArrayList[0].lng,
														locationArrayList[0].lat);
												endPoint = new BMap.Point(
														locationArrayList[locationArrayList.length - 1].lng,
														locationArrayList[locationArrayList.length - 1].lat);

												var startMarker = new BMap.Marker(
														startPoint); // 创建起点标注
												startMarker
														.setIcon(new BMap.Icon(
																'resources/images/icons/mappin/Map-Marker-Marker-Outside-Chartreuse-icon.png',
																new BMap.Size(
																		32, 32)));
												var startLabel = new BMap.Label(
														"跟踪起点",
														{
															offset : new BMap.Size(
																	25, -15)
														});
												startMarker
														.setLabel(startLabel);
												traceMap
														.addOverlay(startMarker); // 将标注添加到地图中

												var endMarker = new BMap.Marker(
														endPoint); // 创建终点标注
												endMarker
														.setIcon(new BMap.Icon(
																'resources/images/icons/mappin/Map-Marker-Marker-Outside-Azure-icon.png',
																new BMap.Size(
																		32, 32)));
												var endLabel = new BMap.Label(
														"跟踪终点",
														{
															offset : new BMap.Size(
																	25, -15)
														});
												endMarker.setLabel(endLabel);
												traceMap.addOverlay(endMarker); // 将标注添加到地图中

											}

											var polyline = new BMap.Polyline(
													BPointArr, {
														strokeColor : "red",
														strokeWeight : 5,
														strokeOpacity : 0.8
													});
											traceMap.addOverlay(polyline);
											traceMap.setViewport(BPointArr); //先设置viewport，再设置中心点
											traceMap.setCenter(pstart);
											 traceMap.zoomOut();
											this.offsetTripTraceMap(traceMap); //对地图做偏移，以保证起点显示在正中 	 

											this.lookupReference('tripTraceMapPanel')
													.up().up()
													.setLoading(false);
											
											//轨迹回放
											this.toPlayCarMoveOpea(traceMap, locationArrayList);
											//end
											
										} else {
											this.lookupReference('tripTraceMapPanel')
													.up().up()
													.setLoading(false);
											Ext.MessageBox.show({
												title : '异常提示',
												msg : '请选择合适的OBD编号！',
												icon : Ext.MessageBox.ERROR,
												buttons : Ext.Msg.OK
											});
										}

									},

									failure : function(response, opts) {
										this.lookupReference('tripTraceMapPanel').up()
												.up().setLoading(false);
										Ext.MessageBox.show({
											title : '异常提示',
											msg : '请选择合适的OBD编号！',
											icon : Ext.MessageBox.ERROR,
											buttons : Ext.Msg.OK
										});
									},

								}); 
    },
    toPlayCarMoveOpea : function (map, locationList) {
    	// 百度地图API功能
    	var offsetSideMap = function (){
//    		map.clearOverlays();
    		var myP1 = new BMap.Point(locationList[0].lng,locationList[0].lat);    //起点
    		var myP2 = new BMap.Point(locationList[locationList.length-1].lng,locationList[locationList.length-1].lat);    //终点
    		var myIcon = new BMap.Icon('http://developer.baidu.com/map/jsdemo/img/car.png', new BMap.Size(52,26), {    //小车图片
    			//offset: new BMap.Size(0, -5),    //相当于CSS精灵
    			imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
    		});
    		var pts = locationList;
    		var paths = pts.length;    //获得有几个点
    		var carMk = new BMap.Marker(pts[0],{icon:myIcon});
    		map.addOverlay(carMk);
    		i=0;
    		function resetMkPoint(i){
//    			carMk.setPosition(pts[i]);
    			map.removeOverlay(carMk);
        		carMk = new BMap.Marker(pts[i],{icon:myIcon});
        		map.addOverlay(carMk);
//    			carMk.setPosition(new BMap.Point(pts[i].lng, pts[i].lat));
    			if(i < paths){
    				setTimeout(function(){
    					i++;
    					resetMkPoint(i);
    				},100);
    			}
    		}
    		setTimeout(function(){
    			resetMkPoint(1);
    		},100)

    	}
    var runner = new Ext.util.TaskRunner(),
       task = runner.start({
            run: offsetSideMap,
            interval: 25000,
       });
    },
    loadTripTraceMoveMapView: function() {
    	// 百度地图API功能
    	var map = this.lookupReference('tripTraceMapPanel').bmap;
//    	map.centerAndZoom(new BMap.Point(116.404, 39.915), 15);
    	map.centerAndZoom(new BMap.Point(114.277049,30.583319), 15);


    	var offsetSideMap = function (){
    		map.clearOverlays();
    		var myP1 = new BMap.Point(114.277049,30.583319);    //起点
    		var myP2 = new BMap.Point(114.323761,30.559439);    //终点
    		var myIcon = new BMap.Icon('http://developer.baidu.com/map/jsdemo/img/car.png', new BMap.Size(52,26), {    //小车图片
    			//offset: new BMap.Size(0, -5),    //相当于CSS精灵
    			imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
    		});
    		var driving2 = new BMap.DrivingRoute(map, {renderOptions:{map: map, autoViewport: true}});    //驾车实例
    		driving2.search(myP1, myP2);    //显示一条公交线路
    		var driving = new BMap.DrivingRoute(map);    //驾车实例
    		driving.search(myP1, myP2);
    		driving.setSearchCompleteCallback(function(){
    			var pts = driving.getResults().getPlan(0).getRoute(0).getPath();    //通过驾车实例，获得一系列点的数组
    			var paths = pts.length;    //获得有几个点

    			var carMk = new BMap.Marker(pts[0],{icon:myIcon});
    			map.addOverlay(carMk);
    			i=0;
    			function resetMkPoint(i){
    				carMk.setPosition(pts[i]);
    				if(i < paths){
    					setTimeout(function(){
    						i++;
    						resetMkPoint(i);
    					},100);
    				}
    			}
    			setTimeout(function(){
    				resetMkPoint(5);
    			},100)

    		});
    	}

//    	setTimeout(function(){
//    		run();
//    	},2500);
    	
    	var runner = new Ext.util.TaskRunner(),
        task = runner.start({
             run: offsetSideMap,
             interval: 25000,
        });
    },
    offsetTripTraceMap: function(map) {
        var xOffset = -(this.lookupReference('tripTraceMapPanel').getWidth() - this.lookupReference('tripTraceMap').getWidth()) / 2;
        var yOffset = -(this.lookupReference('tripTraceMapPanel').getHeight() - this.lookupReference('tripTraceMap').getHeight()) / 2;
        var panOptions = {
            noAnimation: true
        };
        map.panBy(xOffset, yOffset, panOptions);
    },
    loadTrackRealtimeReport: function(vehicleNum) {
//    	console.log('search for vehicle num for' + vehicleNum);
    	Ext.getCmp("realtimeDataGrid").getViewModel().getStore("vehicleRealtimeStore").proxy.url = "app/data/vehiclemgmt/realtime_monitoring/triptraceRealtimeData.json";
    	Ext.getCmp("realtimeDataGrid").getViewModel().getStore("vehicleRealtimeStore").load();
    },
    loadTripTraceWindowInfo : function() {
//    	console.log('++++++loadTripTraceWindowInfo+++++');
    	Ext.getCmp("tripTraceCountGrid").getViewModel().getStore("vehicleRealtimeStore").proxy.url = "app/data/vehiclemgmt/realtime_monitoring/historyTraceData.json";
    	Ext.getCmp("tripTraceCountGrid").getViewModel().getStore("vehicleRealtimeStore").load();
//    	console.log('object::' + Ext.getCmp("tripTraceErrorGrid"));
//    	Ext.getCmp("tripTraceErrorEventGrid").getViewModel().getStore("tripTraceErrorStore").proxy.url = "app/data/vehiclemgmt/realtime_monitoring/triptraceErrorData.json";
//    	Ext.getCmp("tripTraceErrorEventGrid").getViewModel().getStore("tripTraceErrorStore").load();
    },
    onSearchClick : function() {
    	var parm = this.lookupReference('searchForm').getValues();
    	var vehicleNumber = parm.vehicleNumber;
    	vehicleNumber = vehicleNumber.replace(/(^\s*)|(\s*$)/g, "");//删除左右两端的空格
//    	console.log('vehicleNumber:' + vehicleNumber);
    	var regEx = new RegExp(/^(([^\^\.<>%&',;=?$"':#@!~\]\[{}\\/`\|])*)$/);
        if(vehicleNumber != ''){
        	if(!regEx.test(vehicleNumber)) {
        		Ext.Msg.alert('消息提示', '车牌号格式错误！');
        		return;     
        	}
    	}     
    	var vehicleType = parm.vehicleType;
    	if(vehicleType == '') {
    		vehicleType = '-1';
    	}
//    	console.log('vehicleType:' + vehicleType);
    	var vehicleFromName = parm.vehicleFromName;
    	if(vehicleFromName == '') {
    		vehicleFromName = '-1';
    	}
//    	console.log('vehicleFromName:' + vehicleFromName);
    	var arrangedOrgName = parm.arrangedOrgName;
    	if(arrangedOrgName == '') {
    		arrangedOrgName = '-1';
    	}
//    	console.log('arrangedOrgName:' + arrangedOrgName);
    	var city = parm.city;
//    	console.log('city:' + city);
    	var input = {
    		'vehicleNumber' : vehicleNumber,	
    		'vehicleType' : vehicleType,	
    		'fromOrgId' : vehicleFromName,	
    		'deptId' : arrangedOrgName,	
    		'city' : city
    	};
//    	console.log('input:' + input);
    	
//    	input = Ext.encode(input);
//    	Ext.getCmp('vehicleMonitoringListGrid').getStore("vehicleRealtimeReport").proxy.extraParams = {
//			"json" : input
//		};
//    	Ext.getCmp('vehicleMonitoringListGrid').getStore("vehicleRealtimeReport").load();
    	var json = Ext.encode(input);
    	Ext.Ajax.request({
//			url : 'app/data/vehiclemgmt/realtime_monitoring/vehicleData.json',
//			url : 'vehicle/queryObdLocationList?json=' + Ext.encode(input),
			url : 'vehicle/queryObdLocationList',
			method : 'POST',
			params:{json:json},
			success : function(response, options) {
				var respText = Ext.util.JSON.decode(response.responseText);
		    	var data = respText.data;
		    	Ext.getCmp('vehicleMonitoringListGrid').getStore("vehicleRealtimeReport").loadData(data);
		    	//加载地图
//		    	console.log('data.length::' + data.length);
		    	 if (data != null && data.length > 0) {
		    	 	this.onAfterRenderPanelTrackingMap(data);
		    	 }else{
	                function myFun(result) {
	                    var cityName = result.name;
	                    console.log('初始化地图--城市名称:' + cityName);
	                    map.setCenter(cityName);
	                    map.centerAndZoom(cityName, 12);
	                }
	                var myCity = new BMap.LocalCity();
	                myCity.get(myFun);
		    	 
		    	 }
			},
//			failure : function() {
//				Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
//			},
			scope : this
		});
    },
    loadPanoramaMapInfo : function() {
    	var map = this.lookupReference('panoramaMapPanel').panorama;
    	var lng = Ext.getCmp('panoramaMapWindow').lng;
    	var lat = Ext.getCmp('panoramaMapWindow').lat;
//    	console.log('++++loadPanoramaMapInfo+++');
//    	console.log('lng:' + lng);
//    	console.log('lat:' + lat);
    	map.setPov({heading: -40, pitch: 6});
//    	map.setPosition(new BMap.Point(120.320032, 31.589666));
    	map.setPosition(new BMap.Point(lng, lat)); //根据经纬度坐标展示全景图	
    },
    loadVehicleCountInfo : function() {
//    	console.log('+++++loadVehicleCountInfo++++');
    	var imei = Ext.getCmp('realtimeMapWindow').imei;
    	var vehicleNumber = Ext.getCmp('realtimeMapWindow').vehiclenumber;
//    	console.log('++++loadVehicleCountInfo++++imei:' + imei);
    	var input = {
    			'vehicleNumber' : vehicleNumber,
    			'deviceNumber' : imei
    	};
    	var objectModel = new Ext.data.Model();
        var json = Ext.encode(input);
    	Ext.Ajax.request({
//	   		url: 'app/data/vehiclemgmt/realtime_monitoring/vehicleCountData.json',
	   		url: 'usage/report/getVehicleAllMileageAndFuleconsList',//?json=' + Ext.encode(input),
	        method : 'POST',
            params:{json:json},
	        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
//	        	console.log('response.responseText:' + response.responseText);
	        	var respText = Ext.util.JSON.decode(response.responseText);
		    	var data = respText.data;
	        	var retStatus = respText.status;
//	        	console.log('status:' + retStatus);
				if (retStatus == 'success') {
					var win = Ext.getCmp("vehicleCountInfo");
//					console.log('carNum:' + JSON.stringify(data));
					objectModel.data = data;
//					console.log('data:' + objectModel.data);
					win.down("form").loadRecord(objectModel);
				}
	        }
//            ,
//	        failure : function() {
//	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//	        },
	    });
    },
    showVehicle: function(grid, rowIndex, colIndex) {
    	var rec = grid.getStore().getAt(rowIndex);
    	var win = Ext.create('Admin.view.vehiclemgmt.vehicleInfomgmt.ViewVehicleInfo');
    	var objectModel = new Ext.data.Model();
    	var input = {
				"vehicleNumber":rec.data.vehiclenumber
			};
    	var json = Ext.encode(input);
    	Ext.Ajax.request({
       		url: 'vehicle/findVehicleInfoByVehicleNumber',
            method : 'POST',
            params:{json:json},
          //  defaultHeaders : {'Content-type' : 'application/json;utf-8'},
            success : function(response,options) {
            	var respText = Ext.util.JSON.decode(response.responseText);
    	    	var data = respText.data;
            	var retStatus = respText.status;
    			if (retStatus == 'success') {
    				objectModel.data = data;
    				win.down("form").loadRecord(objectModel);
    				win.show();
    			}
            }
//            ,
//            failure : function() {
//                Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//            },
        });
    },
    showErrorEventMapWindow : function(grid, rowIndex, colIndex) {
    	var rec = grid.getStore().getAt(rowIndex);
    	var win = Ext.widget("errorEventMapWindow", {
			title: '报警地理位置',
			closable: true,
			buttonAlign : 'center',
			lng : rec.data.alertLongitude,
			lat : rec.data.alertLatitude,
			alertPosition : rec.data.alertPosition,
			alertTime : rec.data.alertTime
		});
		win.show();
    }

});

function getDirection(bearing, longitude, latitude){
	if(bearing == null || bearing == undefined){
		return "--";
	}
	if(longitude == 0 && latitude == 0){
		return "--";
	}
	var bearing = parseInt(bearing);
	var direction = "--";
	switch(bearing){
	case 0:
		direction = "正北";
		break;
	case 90:
		direction = "正东";
		break;
	case 180:
		direction = "正南";
		break;
	case 270:
		direction = "正西";
		break;
	case 360:
		direction = "正北";
		break;
	default:
		if(bearing > 0 && bearing < 90){
			direction = "东北";
		}else if(bearing > 90 && bearing < 180){
			direction = "东南";
		}else if(bearing > 180 && bearing < 270){
			direction = "西南";
		}else if(bearing > 270 && bearing < 360){
			direction = "西北";
		}
	}
	return direction;
}
