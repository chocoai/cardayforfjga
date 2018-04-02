
 var task = {};//执行任务

/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.ordermgmt.orderallocate.AllocatedViewController', {
    extend: 'Ext.app.ViewController',
    alias : 'controller.allocatedViewController',
    requires: [
    ],
    
    onBeforeload : function() {
    	console.log('+++++AllocatedViewController onBeforeload++++++');
    	//所属部门
    	var frmValues=this.lookupReference('allocatedSearchForm').getValues();

    	if(''==frmValues['deptId']||null==frmValues['deptId']){
    		frmValues['deptId']='-1';
    	}
    	if(this.lookupReference('allocatedSearchForm').getForm().isValid()){
    		var parm = this.lookupReference('allocatedSearchForm').getValues();
        	var orderNo = parm.orderNo;
        	var status = parm.status;
        	if (status == '全部') {
        		status = '';
        	}
        	var page=Ext.getCmp('allocatedGrid').store.currentPage;
    		var limit=Ext.getCmp('allocatedGrid').store.pageSize;
        	
        	var input = {
    			    "currentPage" 	: page,
    				"numPerPage" : limit,
    				'orderNo': 	orderNo,
    				'status': status,
    				'selfDept': parm.includeSelf || false,
    				'childDept': parm.includeChild || false,
    				'organizationId' : parm.deptId,
    			};
        	
        	input = Ext.encode(input);
        	this.getViewModel().getStore("allocatedCarReport").proxy.extraParams = {
    			"json" : input
    		}
    	}else{
        	Ext.Msg.alert('消息提示','请输入有效的筛选条件！');
        	return false;
        }
    	
    },
    
    onSearchClick : function () {
    	console.log('+++++onSearchClick++++++');
    	Ext.getCmp('allocatedGrid').getStore().currentPage = 1;
    	this.getViewModel().getStore("allocatedCarReport").load();
    },
    
    onResetClick : function() {
    	this.getView().getForm().reset();
    },
    
    queryOrderVehicleDriverInfo : function(grid, rowIndex, colIndex) {
    	console.log('+++++queryOrderVehicleDriverInfo++++');
    	var orderInfo = grid.getStore().getAt(rowIndex);
    	var driverName = orderInfo.data.driverName;
    	var driverPhone = orderInfo.data.driverPhone;
    	var driverSource = orderInfo.data.orgName;  //司机所在部门就是司机来源，与当前订单部门一致
    	var stationName = orderInfo.data.stationName;
    	var driverStationName = '';
    	if (stationName != null) {
    		driverStationName = orderInfo.data.stationName;
    	} else {
    		driverStationName = '暂未分配';
    	}
    	
    	var vehicleId = orderInfo.data.vehicleId;
    	var vehicleType = orderInfo.data.vehicleType;
    	switch(vehicleType) {
    		case '0':
    			vehicleType='应急机要通信接待用车';
    			break;
    		case '1':
    			vehicleType='行政执法用车';
    			break;
    		case '2':
    			vehicleType='行政执法特种专业用车';
    			break;
    		case '3':
    			vehicleType='一般执法执勤用车';
    			break;
            case '4':
                vehicleType='执法执勤特种专业用车';
                break;
    	}
    	
    	//var url='vehicle/'+vehicleId+'/update';
    	var url='vehicle/monitor/'+vehicleId+'/update';
    	Ext.Ajax.request({
	   		url: url,
	        method : 'GET',
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	var rec = new Ext.data.Model();
	        	rec.data = respText.data
	        	rec.data.driverName = driverName;
	        	rec.data.driverPhone = driverPhone;
	        	rec.data.vehicleType = vehicleType;
	        	rec.data.driverStationName = driverStationName;
	        	rec.data.driverSource = driverSource;
				if (respText.status == 'success') {	
					var win = Ext.widget("queryOrderVehicleDriverView");
					win.down("form").loadRecord(rec);
					win.show();	
				}
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
    	});
    },
    
    //获取车辆实时位置
    onAfterRenderAllotRealtimeMap : function(panel, options) {
//    	var vehicleNum = this.getView().vehicleNum;
//    	console.log('onAfterRenderAllotRealtimeMap++vehicleNum:' + vehicleNum);
    	var map = this.lookupReference('allotRealtimeMapPanel').bmap;
    	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
    	var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
    	map.addControl(top_left_control);        
		map.addControl(top_left_navigation);     
		map.addControl(new BMap.MapTypeControl({mapTypes:[BMAP_NORMAL_MAP,BMAP_SATELLITE_MAP]}));
//    	map.addControl(new BMap.MapTypeControl());  	
		var imei = Ext.getCmp('allotRealtimeWindow').imei;
		var vehicleNumber = Ext.getCmp('allotRealtimeWindow').vehicleNumber;
		var factStTimeF = Ext.getCmp('allotRealtimeWindow').factStTimeF;
		var currEdTimeF = Ext.Date.format(new Date(), 'Y-m-d H:i:s');
		
		var i = 10;
		var fistCall = true;
		var runner = new Ext.util.TaskRunner();
    	var resetMkPoint = function() {
    		if (fistCall) {//第一次调用定时任务，获取车辆定时信息
    			offsetSideMap(imei);
    			fistCall = false;
    		}
    		
    		if(i > 1){
				i--;
			}else {
				offsetSideMap(imei);
				i = 10;
			}
    		if (Ext.getCmp('allotRealtimeWindow')) {
    			Ext.getCmp('allotRealtimeWindow').setTitle('车辆实时定位——' + vehicleNumber + '——窗口将在' + i + '秒后刷新');
    		}
    	};
    	task = {
    			run: resetMkPoint,  
    		    interval: 1000 //1 second 	
    	};
    	runner.start(task);
    	
		//定时任务
		var offsetSideMap = function(imei) {
    		console.log("10秒更新实时定位");
    		//ajax调用接口获取实时经纬度
    		var tracetime = getNowDate();
    		var input = {'deviceNumber' : imei};
    		var json = Ext.encode(input);
    		Ext.Ajax.request({
    	   		url: 'vehicle/queryObdLocationByImei',
    	        method : 'POST',
    	        timeout : 3000, //超时时间设置，单位毫秒,3秒请求没有返回，则认为请求失败。
    	        params : {json:json},
    	        success : function(response,options) {
    	        	
    	        	// 里程取得
    	        	var mileage = "";
    	        	var getMileageInput = {
    	        			"imei":imei,
    	        			"starttime":factStTimeF,
    	        			"endtime":currEdTimeF
    	        	};
    	    		var mileageJson = Ext.encode(getMileageInput);
    	    		Ext.Ajax.request({
    	    			url : 'vehicle/monitor/findTripPropertyDataByTimeRange',//?json=' + Ext.encode(input),
    	    			timeout : 60000,
    	    			method : 'POST',
    	    			params:{json:mileageJson},
    	    			wait:"正在搜索数据，请稍候...",
    	    			scope : this,
    	    			success : function(resp, opts) {
    	    				var result = Ext.util.JSON.decode(resp.responseText);
//    	    				result = {"data":{"mileage":1.71,"fuel":0.19,"drivetime":1278,"stoptime":18522},"status":"success"};
    	    				if (result.status == 'success') {
    	    					if (result.data != null) {
    	    						mileage = result.data.mileage;
    	    					}
    	    				}
    	    			},
    	    			failure : function(response, opts){
    	    				//数据请求失败，关闭定时任务
    	    	        	Ext.util.TaskManager.stop(task);
//    	    	        	runner.stop(task);
    	    	            Ext.Msg.alert('消息提示', '没有查询到该车辆的实时位置，请稍后再试！',function(){  
    	    	                //关闭后执行 
    	    	            	if (Ext.getCmp('allotRealtimeWindow')) {
    	    	            		Ext.getCmp('allotRealtimeWindow').close();
    	    	            	}
    	    	            });
    	    			}
    	    		});
    	        	
    	        	var respText = Ext.util.JSON.decode(response.responseText);
    				var data = [];
    		    	data[0] = respText.data;
    		    	var speed = data[0].speed;
    		    	var longitude = data[0].longitude;
    		    	var latitude = data[0].latitude;
    		    	
//    		    	var longitude = 114.414308;
//    	            var latitude = 30.483367;
    	            
    		    	map.clearOverlays();
    		    	console.log("*****经纬度信息******"+longitude+"*******"+latitude);
    	    		var point = new BMap.Point(longitude, latitude);
    	        	map.centerAndZoom(point, 15);
    	        	var marker = new BMap.Marker(point);  // 创建标注
    	        	map.addOverlay(marker);               // 将标注添加到地图中
    	        	marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
    	        	//var content = '经度：114.414318<br/>纬度：30.483377';
    	        	var content = '时间：' + tracetime + '<br/>经度：' + longitude + '<br/>纬度：' + latitude;
    	    		marker.addEventListener("click",function(e){
    	    			var opts = {
    	    					width : 250,     // 信息窗口宽度
    	    					height: 80,     // 信息窗口高度
    	    					title : "信息窗口" , // 信息窗口标题
    	    					enableMessage:true//设置允许信息窗发送短息
    	    				   };
    	    			var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
    	    			map.openInfoWindow(infoWindow,point); //开启信息窗口
    	    		});
    	    		
    	    		//获取车辆行驶信息  当前时间、纬度、城市、道路、速度
    	        	var geoc = new BMap.Geocoder();
    	        	var address = '';
    	        	var city = '';
    	        	var street = '';
    	        	//逆地址解析
    	    		geoc.getLocation(point, function(rs){
    	    			var addComp = rs.addressComponents;
    	    			if(addComp.province == '北京市' || addComp.province == '上海市' || addComp.province == '重庆市' || addComp.province == '天津市'){
    	    				address = addComp.city + addComp.district + addComp.street + addComp.streetNumber;
    	    				city = addComp.city;
    	    				street = addComp.street;
    	    			}else{
    	    				address = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
    	    				city = addComp.city;
    	    				street = addComp.street;
    	    			}
    	    			
    	    			var currentTime = Ext.Date.format(new Date(), 'Y-m-d H:i:s');
    	    			// 保留小数点后6位
    	    			longitude = getOmitNum(longitude);
    	    			latitude = getOmitNum(latitude);
    	    			var data = '[{"currentTime":"' + currentTime + '","lng":"' + longitude + '","lat":"' + 
    	    	    	latitude + '","city":"' + city + '","road":"' + 
    	    	    	street + '","speed":"' +speed+ '","mileage":"' +mileage+ '"}]';
    	    	    	
    	    			if (Ext.getCmp("allotRealtimeDataGrid")) {
    	    				Ext.getCmp("allotRealtimeDataGrid").getViewModel().getStore("allotVehicleRealtimeStore").loadData(JSON.parse(data));
    	    			}
    	    		});
    	        },
    	        failure : function() {
    	        	//数据请求失败，关闭定时任务
    	        	Ext.util.TaskManager.stop(task);
//    	        	runner.stop(task);
    	            Ext.Msg.alert('消息提示', '没有查询到该车辆的实时位置，请稍后再试！',function(){  
    	                //关闭后执行 
    	            	if (Ext.getCmp('allotRealtimeWindow')) {
    	            		Ext.getCmp('allotRealtimeWindow').close();
    	            	}
    	            });
    	            
    	        },
    	    });
		};
    },
    
    // 当窗口关闭时,task停止
    closeDialog : function() {
		runner.stop(task);
	},
    
    showAllotVehicleMapWindow : function(grid, rowIndex, colIndex) {
    	//console.log('++++showAllotVehicleMapWindow++++');
    	var orderInfo = grid.getStore().getAt(rowIndex);
    	
    	var vehicleId = orderInfo.data.vehicleId;
    	var url='vehicle/'+vehicleId+'/update';
    	var vehicleNumber = '';
    	Ext.Ajax.request({
	   		url: url,
	        method : 'GET',
	        async: false,//同步请求，ajax请求完毕后，才能继续执行后面的代码
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	vehicleNumber = respText.data.vehicleNumber;
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
    	});	

    	var win = Ext.widget("allotRealtimeWindow", {
			//title: '车辆追踪——'+vehicleNumber+'——窗口将在10秒后刷新',
    		imei : orderInfo.data.deviceNumber,
    		vehicleNumber : vehicleNumber,
			closable: true,
			buttonAlign : 'center',
		});
    	win.show();
    },
    
//    onAfterRenderAllotRealtimeMap : function(panel, options) {
////    	var vehicleNum = this.getView().vehicleNum;
////    	console.log('onAfterRenderAllotRealtimeMap++vehicleNum:' + vehicleNum);
//    	var map = this.lookupReference('allotRealtimeMapPanel').bmap;
//    	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
//    	var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
//    	map.addControl(top_left_control);        
//		map.addControl(top_left_navigation);     
//		map.addControl(new BMap.MapTypeControl({mapTypes:[BMAP_NORMAL_MAP,BMAP_SATELLITE_MAP]}));
////    	map.addControl(new BMap.MapTypeControl());
//    	//定时任务
//    	var offsetSideMap = function() {
//    		console.log("30秒更新实时定位");
//    		//地图定位
//    		map.clearOverlays();
//        	var point = new BMap.Point(114.414308, 30.483367);
//        	map.centerAndZoom(point, 15);
//        	var marker = new BMap.Marker(point);  // 创建标注
//        	map.addOverlay(marker);               // 将标注添加到地图中
//        	marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
//        	var content = '经度：114.414308<br/>纬度：30.48336755';
//    		marker.addEventListener("click",function(e){
//    			var opts = {
//    					width : 250,     // 信息窗口宽度
//    					height: 80,     // 信息窗口高度
//    					title : "信息窗口" , // 信息窗口标题
//    					enableMessage:true//设置允许信息窗发送短息
//    				   };
//    			var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
//    			map.openInfoWindow(infoWindow,point); //开启信息窗口
//    		}
//    		);
//    		
//    	};
//    	//定时器 30秒轮询
//        var runner = new Ext.util.TaskRunner(),
//            task = runner.start({
//                 run: offsetSideMap,
//                 interval: 30000,
//            });
//    },
    
    showAllocatedTraceWindow: function(grid, rowIndex, colIndex) {
    	//console.log('++++showAllocatedTraceWindow++++');
    	var orderInfo = grid.getStore().getAt(rowIndex);
    	var factStTimeF = orderInfo.data.factStTimeF;
    	var factEdTimeF = orderInfo.data.factEdTimeF;
    	var factMileage = orderInfo.data.factMileage;

    	var vehicleId = orderInfo.data.vehicleId;
    	var url='vehicle/'+vehicleId+'/update';
    	var vehicleNumber = '';
    	Ext.Ajax.request({
	   		url: url,
	        method : 'GET',
	        async: false,//同步请求，ajax请求完毕后，才能继续执行后面的代码
	        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	vehicleNumber = respText.data.vehicleNumber;
	        }
	        /*,
	        failure : function() {
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
    	});	
    	
    	var win = Ext.widget("allocatedTraceWindow", {
			title: '订单车辆轨迹——'+vehicleNumber,
			closable: true,
			buttonAlign : 'center',
			imei : orderInfo.data.deviceNumber,
			factStTimeF : factStTimeF,
			factEdTimeF : factEdTimeF,
			factMileage : factMileage,
		});
    	win.show();
    	
//    	var store = Ext.create('Ext.data.Store', {
//    	    fields: ['startTime','endTime', 'mileage', 'speed'],
//    	    data: [
//    	        { 
//    	          'startTime': orderInfo.data.factEdTimeF,
//    	          'endTime': orderInfo.data.factStTimeF,
//    	          'mileage': 100,
//    	          'speed': 100,
//    	        }]
//    	});	
//    	Ext.getCmp("allocatedTraceCountGrid").setStore(store);
    },
    loadTraceInfo : function() {
    	console.log('++++loadTraceInfo++++');
    	//Ext.getCmp("allocatedTraceCountGrid").getViewModel().getStore("allocatedTraceStore").proxy.url = "classic/src/view/ordermgmt/orderallocate/allocatedOrderHistory.json";
    	//Ext.getCmp("allocatedTraceCountGrid").getViewModel().getStore("allocatedTraceStore").load();
    	var startTime = Ext.getCmp('allocatedTraceWindow').factStTimeF;
    	var endTime = Ext.getCmp('allocatedTraceWindow').factEdTimeF;
    	var mileage = Ext.getCmp('allocatedTraceWindow').factMileage;
    	var speed;

    	startTime = Ext.Date.parseDate(startTime, 'Y-m-d H:i:s');
    	endTime = Ext.Date.parseDate(endTime, 'Y-m-d H:i:s');
    	if (startTime != null && endTime != null) {
    		// 运营总时间
    		var elapsedTime = endTime.getTime() - startTime.getTime();
    		var elapsedHour = elapsedTime/(1000*60*60);
    		// 计算平均速度(运营总里程/运营总时间)
    		speed = mileage/elapsedHour;
    		speed = Ext.util.Format.number(speed, '0.##');
    		// 时间转换
    		startTime = Ext.util.Format.date(startTime,'Y-m-d H:i:s');
    		endTime = Ext.util.Format.date(endTime,'Y-m-d H:i:s');
    	}
    	startTime = (startTime == null) ? "--" : startTime;
    	endTime = (endTime == null) ? "--" : endTime;
    	mileage = (mileage == null) ? "--" : mileage;
    	speed = (speed == null) ? "--" : speed;
    	
    	var data = '[{"startTime":"' + startTime + '","endTime":"' + endTime 
    				+ '","mileage":"' + mileage  + '","speed":"' + speed + '"}]';
		Ext.getCmp("allocatedTraceCountGrid").getViewModel().getStore("allocatedTraceStore").loadData(JSON.parse(data));
    	
    	//Ext.getCmp("allocatedEventGrid").getViewModel().getStore("tripTraceErrorStore").proxy.url = "classic/src/view/ordermgmt/orderallocate/triptraceErrorData.json";
    	//Ext.getCmp("allocatedEventGrid").getViewModel().getStore("tripTraceErrorStore").load();
    },
    
    loadTripTraceMapView : function() {
//    	var traceMap = this.lookupReference('tripTraceMapPanel').bmap;
    	//var traceMap = Ext.getCmp('allocatedTraceMapPanel').bmap;
    	var traceMap = this.lookupReference('allocatedTraceMapPanel').bmap;
    	
//    	var imei = '41042502439';
//    	var startTime = '2017-01-05 15:30:00';
//    	var endTime = '2017-01-05 15:35:00';
    	var imei = '';
    	var startTime = '';
    	var endTime = '';
    	imei = Ext.getCmp('allocatedTraceWindow').imei;
    	startTime = Ext.getCmp('allocatedTraceWindow').factStTimeF;
    	endTime = Ext.getCmp('allocatedTraceWindow').factEdTimeF;
    	var input = {
    			'imei' : imei,
    			'starttime' : startTime,
    			'endtime' : endTime
    	};
    	
//    	var tripTraceMapPanel = Ext.getCmp('tripTraceWindow');
//    	var myMask = new Ext.LoadMask({
//    	    msg    : '正在导入历史数据，请稍候...',
//    	    target : tripTraceMapPanel
//    	});
//    	myMask.show();
    	var json = Ext.encode(input);
    	Ext.Ajax.request({
									//      	     url: 'web/ui/findTripTraceDataByTimeRange?jsonString={imei:' + param + ',starttime:\"'+ beginDate + '\",endtime:\"'+ endDate +'\"}',
//									url : 'app/data/vehiclemgmt/realtime_monitoring/triptraceMapData.json',
//									url : 'app/data/vehiclemgmt/realtime_monitoring/playTripTraceData.json',
									//url : 'vehicle/monitor/findTripTrace',//?json=' + Ext.encode(input),
									url : 'vehicle/monitor/findVehicleHistoryTrack',
									timeout : 30000,
//									method : 'GET',
									method : 'POST',
									params:{json:json},
									wait:"正在搜索历史数据，请稍候...",
									scope : this,

									success : function(resp, opts) {
										//myMask.hide();
										var location = Ext.util.JSON
												.decode(resp.responseText);
										traceMap.clearOverlays();
										console.log('location::' + JSON.stringify(location));
										if (location.status == 'success') {
//											var detailLocation = Ext.util.JSON
//													.decode(location.data[0].tracegeometry);
											var BPointArr = new Array();
											var pstart, pend, startPoint, endPoint;
											//找起点

//											var locationArrayList = new Array();
											var numArray = 0;

//											for (var i = 0; i < detailLocation.length; i++) {
//												if (detailLocation[i].lng != null
//														&& detailLocation[i].lat != null) {
//													locationArrayList[numArray] = detailLocation[i];
//													numArray++;
//												}
//											}
											
											if(location.data.length == 0) {
												Ext.MessageBox.alert("消息提示","未查询到历史记录！");
												return;
											}
											
											for (var i = 0; i < location.data.length; i++) {
												if (location.data[i].longitude != null
														&& location.data[i].latitude != null) {
													locationArrayList[numArray] = location.data[i];
													numArray++;
												}
											}
											
//											console.log('pts:' + JSON.stringify(locationArrayList));

											for (var i = 0; i < locationArrayList.length - 1; i++) {

												//                   console.log("Hi I am i::::" + i);

												pstart = new BMap.Point(
														locationArrayList[i].longitude,
														locationArrayList[i].latitude);
												BPointArr
														.push(new BMap.Point(
																locationArrayList[i].longitude,
																locationArrayList[i].latitude));

												pend = new BMap.Point(
														locationArrayList[i + 1].longitude,
														locationArrayList[i + 1].latitude);
												BPointArr
														.push(new BMap.Point(
																locationArrayList[i + 1].longitude,
																locationArrayList[i + 1].latitude));

												var mstart = new BMap.Marker(
														pstart);
												var mend = new BMap.Marker(pend);
											}

											if (locationArrayList.length > 0) {
												startPoint = new BMap.Point(
														locationArrayList[0].longitude,
														locationArrayList[0].latitude);
												endPoint = new BMap.Point(
														locationArrayList[locationArrayList.length - 1].longitude,
														locationArrayList[locationArrayList.length - 1].latitude);

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
//														strokeColor : "red",
														strokeColor : "blue",
														strokeWeight : 5,
														strokeOpacity : 0.8
													});
											traceMap.addOverlay(polyline);
											traceMap.setViewport(BPointArr); //先设置viewport，再设置中心点
											traceMap.setCenter(pstart);
											traceMap.zoomOut();
//											this.offsetTripTraceMap(traceMap); //对地图做偏移，以保证起点显示在正中 	 

//											this.lookupReference('tripTraceMapPanel')
//													.up().up()
//													.setLoading(false);
											
											Ext.getCmp('allocatedTraceMapPanel')
											.up().up()
											.setLoading(false);
											
											//轨迹回放
//											this.toPlayCarMoveOpea(traceMap, locationArrayList);
											//end
											
										} else {
//											this.lookupReference('tripTraceMapPanel')
//													.up().up()
//													.setLoading(false);
											Ext.getCmp('allocatedTraceMapPanel')
											.up().up()
											.setLoading(false);
											Ext.MessageBox.show({
												title : '异常提示',
												msg : '未查询到历史记录！',
												icon : Ext.MessageBox.ERROR,
												buttons : Ext.Msg.OK
											});
										}

									},

									failure : function(response, opts) {
										this.lookupReference('allocatedTraceMapPanel').up()
												.up().setLoading(false);
										Ext.MessageBox.show({
											title : '异常提示',
											msg : '未查询到历史记录！',
											icon : Ext.MessageBox.ERROR,
											buttons : Ext.Msg.OK
										});
									},

								});
    },
    
    
    //查看订单信息
    viweOrderInfo : function(grid, rowIndex, colIndex) {

    	var orderInfo = grid.getStore().getAt(rowIndex);
		var orderNo = orderInfo.data.orderNo;
		
		var auditUserName;
		var auditUserPhone;
		var auditStatus;
		var auditTime;
		
		//待审核、补录的订单没有审核信息
		if ((orderInfo.data.status!=0)&&(orderInfo.data.planStTimeF!=null)) {
			//alert('有审核信息的订单')
			var input1 = {
 	 				'id': 	orderInfo.data.id,
 	 			};
 			var json = Ext.encode(input1);
			//订单审核信息
 			Ext.Ajax.request({
 				url : 'order/auditHistory',//?json='+ Ext.encode(input1),
 		        method : 'POST',
	            params:{json:json},
 		        async: false,
 		        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
 		        success : function(response,options) {
 					var respText = Ext.util.JSON.decode(response.responseText);
					//只显示第一条审核信息（后台已经做排序，第一条就是最新的）
 					auditUserName = respText.data[0].auditUserName;
					auditUserPhone = respText.data[0].auditUserPhone;
					auditStatus = respText.data[0].status;
					if (auditStatus == 1) {
						auditStatus = '审核通过';
					} else if (auditStatus == 5) {
						auditStatus = '被驳回';
					}
					auditTime = respText.data[0].auditTimeF;
 		        },
// 		        failure : function() {
// 		            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
// 		        },
 		        scope:this
 			});
		}
		

 			Ext.Ajax.request({
// 				url : 'order',//?json='+ Ext.encode(input),
 				url: "order/"+orderInfo.data.id+'/queryBusiOrderByOrderNo',
 		        method : 'GET',
//	            params:{json:json},
 		        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
 		        success : function(response,options) {
 					var respText = Ext.util.JSON.decode(response.responseText);
 					var rec = new Ext.data.Model();
 					rec.data = respText.data;
 					
 					var durationTime = respText.data.durationTime;
 					var durationHour = parseInt(durationTime/60);
					var durationMinute = durationTime % 60;
					
					var waitTime = respText.data.waitTime;
 					var waitHour = parseInt(waitTime/60);
					var waitMinute = waitTime % 60;
					
 					rec.data.durationTime = durationHour+'小时' + durationMinute + '分钟';
 					rec.data.waitTime = waitHour+'小时' + waitMinute + '分钟';
 					
 					//rec.data.durationTime = respText.data.resultList[0].durationTime+'小时';
 					rec.data.auditUserName = auditUserName;
 					rec.data.auditUserPhone = auditUserPhone;
 					rec.data.auditStatus = auditStatus;
 					rec.data.auditTime = auditTime;
 					
 					if (rec.data.returnType == 0) {
 						rec.data.returnType = '是';
 					} else if (rec.data.returnType == 1) {
 						rec.data.returnType = '否';
 					}
 					
 					var win = Ext.widget("vieworder");
 					if (respText.status == 'success') {						
 						win.down("form").loadRecord(rec);
 						win.show();
 						
 						//往返，显示等待时长， 不往返，不显示等待时长
 						if (rec.data.returnType == '是') {
 	 						Ext.getCmp('view_order_wait_time_id').setHidden(false);
 	 					} else if (rec.data.returnType == '否') {
 	 						Ext.getCmp('view_order_wait_time_id').setHidden(true);
 	 					}
 						
 						//被驳回的订单，需要显示驳回原因
 						if (rec.data.status=='5') {
 							Ext.getCmp('refuseComments_id').setHidden(false);
 						} else {
 							Ext.getCmp('refuseComments_id').setHidden(true);
 						}
 						
 						if ((orderInfo.data.status==0)||(orderInfo.data.planStTimeF==null)) {
 							Ext.getCmp('viewOrder_audit_id').setHidden(true);
 							Ext.getCmp('auditUserName_id').setHidden(true);
 							Ext.getCmp('auditUserPhone_id').setHidden(true);
 							Ext.getCmp('auditStatus_id').setHidden(true);
 							Ext.getCmp('auditTime_id').setHidden(true);
 						}
 					}
 		        },
// 		        failure : function() {
// 		            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
// 		        },
 		        scope:this
 			});
    },
	/******多级部门筛选*******/
	openDeptChooseWin: function(combo, event, eOpts){
     	var win = Ext.create("Admin.view.ordermgmt.orderallocate.DeptChooseWinForAllocated",{
     	});
     	win.down("treepanel").getStore().load();
     	win.show();
     },
     
     chooseDept: function(btn, e, eOpts){
     	var tree = btn.up("window").down("treepanel");
     	var selection = tree.getSelectionModel().getSelection();
     	if(selection.length == 0){
     		Ext.Msg.alert('提示', '请选择部门！');
     		return;
     	}
     	var select = selection[0].getData();
 		var deptId = select.id;
 		var deptName = select.text;
     	var form = Ext.getCmp("allocatedsearchform_id").getForm();
     	form.findField("deptId").setValue(deptId);
//     	form.findField("organizationId").setDisplayField(deptName);
     	form.findField("deptName").setValue(deptName);
     	btn.up("window").close();
     },
//    loadTripTraceMapView : function() {
//    	console.log('++++loadTripTraceMapView++++');
//    	var traceMap = this.lookupReference('allocatedTraceMapPanel').bmap;
//    	var imei = '41042502439';
//    	var startTime = '2017-01-05 15:30:00';
//    	var endTime = '2017-01-05 15:35:00';
//    	
//    	var input = {
//    			'imei' : imei,
//    			'starttime' : startTime,
//    			'endtime' : endTime
//    	};
//    	Ext.Ajax.request({
//									//      	     url: 'web/ui/findTripTraceDataByTimeRange?jsonString={imei:' + param + ',starttime:\"'+ beginDate + '\",endtime:\"'+ endDate +'\"}',
////									url : 'app/data/vehiclemgmt/realtime_monitoring/triptraceMapData.json',
//									//url : 'app/data/vehiclemgmt/realtime_monitoring/playTripTraceData.json',
//    								//url: 'classic/src/view/ordermgmt/orderallocate/playTripTraceData.json',
//    								url : 'vehicle/monitor/findTripTrace?json=' + Ext.encode(input),
//									timeout : 30000,
//									method : 'POST',
//									wait:"正在搜索历史数据，请稍候...",
//									scope : this,
//
//									success : function(resp, opts) {
//										var location = Ext.util.JSON
//												.decode(resp.responseText);
//										traceMap.clearOverlays();
//										if (location.success) {
////											var detailLocation = Ext.util.JSON
////													.decode(location.data[0].tracegeometry);
//											var BPointArr = new Array();
//											var pstart, pend, startPoint, endPoint;
//											//找起点
//
//											var locationArrayList = new Array();
//											var numArray = 0;
//
//											for (var i = 0; i < detailLocation.length; i++) {
//												if (detailLocation[i].longitude != null
//														&& detailLocation[i].latitude != null) {
//													locationArrayList[numArray] = detailLocation[i];
//													numArray++;
//												}
//											}
//											
////											console.log('pts:' + JSON.stringify(locationArrayList));
//
//											for (var i = 0; i < locationArrayList.length - 1; i++) {
//
//												//                   console.log("Hi I am i::::" + i);
//
//												pstart = new BMap.Point(
//														locationArrayList[i].longitude,
//														locationArrayList[i].latitude);
//												BPointArr
//														.push(new BMap.Point(
//																locationArrayList[i].longitude,
//																locationArrayList[i].latitude));
//
//												pend = new BMap.Point(
//														locationArrayList[i + 1].longitude,
//														locationArrayList[i + 1].latitude);
//												BPointArr
//														.push(new BMap.Point(
//																locationArrayList[i + 1].longitude,
//																locationArrayList[i + 1].latitude));
//
//												var mstart = new BMap.Marker(
//														pstart);
//												var mend = new BMap.Marker(pend);
//											}
//
//											if (locationArrayList.length > 0) {
//												startPoint = new BMap.Point(
//														locationArrayList[0].longitude,
//														locationArrayList[0].latitude);
//												endPoint = new BMap.Point(
//														locationArrayList[locationArrayList.length - 1].longitude,
//														locationArrayList[locationArrayList.length - 1].latitude);
//
//												var startMarker = new BMap.Marker(
//														startPoint); // 创建起点标注
//												startMarker
//														.setIcon(new BMap.Icon(
//																'resources/images/icons/mappin/Map-Marker-Marker-Outside-Chartreuse-icon.png',
//																new BMap.Size(
//																		32, 32)));
//												var startLabel = new BMap.Label(
//														"跟踪起点",
//														{
//															offset : new BMap.Size(
//																	25, -15)
//														});
//												startMarker
//														.setLabel(startLabel);
//												traceMap
//														.addOverlay(startMarker); // 将标注添加到地图中
//
//												var endMarker = new BMap.Marker(
//														endPoint); // 创建终点标注
//												endMarker
//														.setIcon(new BMap.Icon(
//																'resources/images/icons/mappin/Map-Marker-Marker-Outside-Azure-icon.png',
//																new BMap.Size(
//																		32, 32)));
//												var endLabel = new BMap.Label(
//														"跟踪终点",
//														{
//															offset : new BMap.Size(
//																	25, -15)
//														});
//												endMarker.setLabel(endLabel);
//												traceMap.addOverlay(endMarker); // 将标注添加到地图中
//
//											}
//
//											var polyline = new BMap.Polyline(
//													BPointArr, {
//														strokeColor : "red",
//														strokeWeight : 5,
//														strokeOpacity : 0.8
//													});
//											traceMap.addOverlay(polyline);
//											traceMap.setViewport(BPointArr); //先设置viewport，再设置中心点
//											traceMap.setCenter(pstart);
//											 traceMap.zoomOut();
//											//this.offsetTripTraceMap(traceMap); //对地图做偏移，以保证起点显示在正中 	 
//
//											this.lookupReference('allocatedTraceMapPanel')
//													.up().up()
//													.setLoading(false);
//											
//										} else {
//											this.lookupReference('allocatedTraceMapPanel')
//													.up().up()
//													.setLoading(false);
//											Ext.MessageBox.show({
//												title : '异常提示',
//												msg : '请选择合适的OBD编号！',
//												icon : Ext.MessageBox.ERROR,
//												buttons : Ext.Msg.OK
//											});
//										}
//
//									},
//
//									failure : function(response, opts) {
//										this.lookupReference('allocatedTraceMapPanel').up()
//												.up().setLoading(false);
//										Ext.MessageBox.show({
//											title : '异常提示',
//											msg : '请选择合适的OBD编号！',
//											icon : Ext.MessageBox.ERROR,
//											buttons : Ext.Msg.OK
//										});
//									},
//
//								}); 
//    }
});

function getOmitNum(num) {
	var afterNum = "";
	if (null != num && "" != num) {
		afterNum = num.toFixed(7);
		afterNum = afterNum.substring(0,afterNum.lastIndexOf('.')+7);
	}
	return afterNum;
}
