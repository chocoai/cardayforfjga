var locationArrayList = new Array();// 轨迹坐标集合
var currentPosition = 0;// 当前坐标集合标识
var runner = new Ext.util.TaskRunner();// 定时器
var task = {};// 执行任务
var speed = 2100;
var carMk = undefined;
var infoWindow;
var opts = {
		width : 230, // 信息窗口宽度
		height : 120, // 信息窗口高度
		enableMessage : true
	};
function executeTask(count, pts, map, carMk, content, opts) {
	if (count < pts.length) {
//		console.log(count);
		carMk.setPosition(new BMap.Point(pts[count].longitude,pts[count].latitude));
		var tracetime = pts[count].tracetime;
		var status = pts[count].status;
		var speed = pts[count].speed;
		var address = pts[count].address;
		content = '时间：' + retnruEmptyValue(tracetime, '0') + '<br/>' + '状态：'
				+ retnruEmptyValue(status, '0') + '<br/>' + '经度：'
				+ pts[count].longitude + '<br/>' + '纬度：' + pts[count].latitude
				+ '<br/>' + '速度：' + retnruEmptyValue(speed, '1') + '<br/>'
				+ '地址：' + retnruEmptyValue(address, '0');
		infoWindow = new BMap.InfoWindow(content, opts);
		map.openInfoWindow(infoWindow, new BMap.Point(pts[count].longitude,pts[count].latitude));
	} else {
		runner.stop(task);
		currentPosition = 0;
	}
}

function retnruEmptyValue(value, type) {
	if (typeof(value) == "undefined" || value == null) {
		return '';
	}
	if (type == '1') {
		return '约' + value + 'km/h';
	}
	return value;
}

function GetDateStr(AddDayCount) {
	var dd = new Date();
	dd.setDate(dd.getDate() + AddDayCount);// 获取AddDayCount天后的日期
	var y = dd.getFullYear();
	var m = dd.getMonth() + 1;// 获取当前月份的日期
	var d = dd.getDate();
	return y + "-" + m + "-" + d;
}

function GetYesterDayTime() {
	var dd = new Date();
	dd.setDate(dd.getDate() - 1);
	var y = dd.getFullYear();
	var m = dd.getMonth() + 1;
	if (m < 10) {
		m = '0' + m;
	}
	var d = dd.getDate();
	if (d < 10) {
		d = '0' + d;
	}
	return y + "-" + m + "-" + d;
}
Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.history_data.HistoryViewController',{
	extend : 'Ext.app.ViewController',
	alias : 'controller.historyViewController',
	requires : [],
	//统计车辆总里程，总油耗等
	loadTripTraceWindowInfo : function() {
		var startTime = GetDateStr(-1) + ' 00:00:00';
		var endTime = GetDateStr(-1) + ' 23:59:59';
		var imei = Ext.getCmp('tripTraceWindow').imei;
		var input = {
			"imei" : imei,
			"starttime" : startTime,
			"endtime" : endTime
		};
		input = Ext.encode(input);
		Ext.getCmp("tripTraceCountGrid").getViewModel().getStore("vehicleRealtimeStore").proxy.extraParams = {"json" : input}
		Ext.getCmp("tripTraceCountGrid").getViewModel().getStore("vehicleRealtimeStore").load();
	},
	//地图渲染后执行该方法
	initTripTraceMap : function() {
		var imei = Ext.getCmp('tripTraceWindow').imei;
		console.log('imei:' + imei);
		var startTime = GetDateStr(-1) + ' 00:00:00';
		var endTime = GetDateStr(-1) + ' 23:59:59';
		this.initView();
		this.loadTripTraceMapView(imei, startTime, endTime);
	},
	initView : function() {
		locationArrayList = new Array();
		speed = 2100;
		task.interval = speed;
		var traceMap = Ext.getCmp('tripTraceMapPanel').bmap;
		traceMap.clearOverlays();
		traceMap.reset();
		function myFun(result) {
			var cityName = result.name;
			console.log('initView:' + cityName);
			traceMap.setCenter(cityName);
			traceMap.centerAndZoom(cityName, 12);
		}
		var myCity = new BMap.LocalCity();
		myCity.get(myFun);
	},
	
	loadTripTraceMapView : function(imei, startTime, endTime) {
		var traceMap = Ext.getCmp('tripTraceMapPanel').bmap;
		var input = {
			'imei' : imei,
			'starttime' : startTime,
			'endtime' : endTime
		};

		var tripTraceMapPanel = Ext.getCmp('tripTraceWindow');
		var myMask = new Ext.LoadMask({
					msg : '正在导入历史数据，请稍候...',
					target : tripTraceMapPanel
				});
		myMask.show();
		var json = Ext.encode(input);
		Ext.Ajax.request({
			url : 'vehicle/monitor/findVehicleHistoryTrack',
			timeout : 60000,
			method : 'POST',
			params : {json : json},
			scope : this,
			success : function(resp, opts) {
				myMask.hide();
				var location = Ext.util.JSON.decode(resp.responseText);
				traceMap.clearOverlays();
				console.log('location::'+ JSON.stringify(location));
				if (location.status == 'success') {
					var BPointArr = new Array();
					var pstart, pend, startPoint, endPoint;
					var numArray = 0;
					if (location.data.length == 0) {
						Ext.MessageBox.alert("消息提示","未查询到历史记录！");
						return;
					}
					//过滤为null的经纬度
					for (var i = 0; i < location.data.length; i++) {
						if (location.data[i].longitude != null&& location.data[i].latitude != null) {
							locationArrayList[numArray] = location.data[i];
							BPointArr.push(new BMap.Point(locationArrayList[numArray].longitude, locationArrayList[numArray].latitude));
							numArray++;
						}
					}
					if (locationArrayList.length > 0) {
						startPoint = new BMap.Point(locationArrayList[0].longitude,locationArrayList[0].latitude);
						endPoint = new BMap.Point(locationArrayList[locationArrayList.length- 1].longitude,locationArrayList[locationArrayList.length- 1].latitude);
						var startMarker = new BMap.Marker(startPoint); // 创建起点标注
						startMarker.setIcon(new BMap.Icon('resources/images/icons/mappin/Map-Marker-Marker-Outside-Chartreuse-icon.png',new BMap.Size(32, 32)));
						var startLabel = new BMap.Label("跟踪起点",{offset : new BMap.Size(-25,-15)});
						startMarker.setLabel(startLabel);
						traceMap.addOverlay(startMarker); // 将标注添加到地图中
						var endMarker = new BMap.Marker(endPoint); // 创建终点标注
						endMarker.setIcon(new BMap.Icon('resources/images/icons/mappin/Map-Marker-Marker-Outside-Azure-icon.png',new BMap.Size(32, 32)));
						var endLabel = new BMap.Label("跟踪终点", {offset : new BMap.Size(25,-15)});
						endMarker.setLabel(endLabel);
						traceMap.addOverlay(endMarker); // 将标注添加到地图中
					}
					var polyline = new BMap.Polyline(BPointArr,{strokeColor : "blue",strokeWeight : 5,strokeOpacity : 0.8});
					traceMap.addOverlay(polyline);
					traceMap.setViewport(BPointArr); // 先设置viewport，再设置中心点
//					traceMap.setCenter(pstart);
//					traceMap.zoomOut();
//					Ext.getCmp('tripTraceMapPanel').up().up().setLoading(false);
				} else {
//					Ext.getCmp('tripTraceMapPanel').up().up().setLoading(false);
					Ext.MessageBox.show({
						title : '异常提示',
						msg : '未查询到历史记录！',
						icon : Ext.MessageBox.ERROR,
						buttons : Ext.Msg.OK
					});
								
				}
			},
			failure : function(response, opts) {
//				this.lookupReference('tripTraceMapPanel').up().up().setLoading(false);
				Ext.MessageBox.show({
					title : '异常提示',
					msg : '未查询到历史记录！',
					icon : Ext.MessageBox.ERROR,
					buttons : Ext.Msg.OK
				});
			}
		});
	},
	playAction : function() { // 开始
		if (locationArrayList.length == 0) {
			Ext.MessageBox.alert("消息提示", "无历史轨迹，请先查询!");
			return;
		}
		Ext.getCmp('historyTracePlayButton').setDisabled(true);
		Ext.getCmp('historyTracePauseButton').setDisabled(false);
		Ext.getCmp('historyTraceStopButton').setDisabled(false);
		Ext.getCmp('historyTraceBackwardButton').setDisabled(false);
		Ext.getCmp('historyTraceForwardButton').setDisabled(false);
		Ext.getCmp('historyTraceResetButton').setDisabled(false);
		this.executeRunner();
	},
	pauseAction : function() { // 暂停
		Ext.getCmp('historyTracePlayButton').setDisabled(false);
		Ext.getCmp('historyTracePauseButton').setDisabled(true);
		Ext.getCmp('historyTraceStopButton').setDisabled(true);
		Ext.getCmp('historyTraceBackwardButton').setDisabled(false);
		Ext.getCmp('historyTraceForwardButton').setDisabled(false);
		Ext.getCmp('historyTraceResetButton').setDisabled(false);
		runner.stop(task);
	},
	stopAction : function() {
		var traceMap = Ext.getCmp('tripTraceMapPanel').bmap;
		Ext.getCmp('historyTracePlayButton').setDisabled(false);
		Ext.getCmp('historyTracePauseButton').setDisabled(true);
		Ext.getCmp('historyTraceStopButton').setDisabled(true);
		Ext.getCmp('historyTraceBackwardButton').setDisabled(true);
		Ext.getCmp('historyTraceForwardButton').setDisabled(true);
		Ext.getCmp('historyTraceResetButton').setDisabled(true);
		traceMap.removeOverlay(carMk);
		carMk = undefined;
		traceMap.closeInfoWindow(infoWindow);
		runner.stop(task);
		currentPosition = 0;
	},
	backwardAction : function() {
		if (currentPosition > 0) {
			currentPosition--;
			carMk.setPosition(new BMap.Point(locationArrayList[currentPosition].longitude,locationArrayList[currentPosition].latitude));
			
			var traceMap = Ext.getCmp('tripTraceMapPanel').bmap;
			var tracetime = locationArrayList[currentPosition].tracetime;
			var status = locationArrayList[currentPosition].status;
			var speed = locationArrayList[currentPosition].speed;
			var address = locationArrayList[currentPosition].address;
			content = '时间：' + retnruEmptyValue(tracetime, '0') + '<br/>' + '状态：'
					+ retnruEmptyValue(status, '0') + '<br/>' + '经度：'
					+ locationArrayList[currentPosition].longitude + '<br/>' + '纬度：' + locationArrayList[currentPosition].latitude
					+ '<br/>' + '速度：' + retnruEmptyValue(speed, '1') + '<br/>'
					+ '地址：' + retnruEmptyValue(address, '0');
			infoWindow = new BMap.InfoWindow(content, opts);
			traceMap.openInfoWindow(infoWindow, new BMap.Point(locationArrayList[currentPosition].longitude,locationArrayList[currentPosition].latitude));
		}
	},
	forwardAction : function() {
		/*if (speed > 100) {
			speed = speed - 500;
			task.interval = speed;
		}*/
		if (currentPosition < locationArrayList.length) {
			currentPosition++;
			carMk.setPosition(new BMap.Point(locationArrayList[currentPosition].longitude,locationArrayList[currentPosition].latitude));
			
			var traceMap = Ext.getCmp('tripTraceMapPanel').bmap;
			var tracetime = locationArrayList[currentPosition].tracetime;
			var status = locationArrayList[currentPosition].status;
			var speed = locationArrayList[currentPosition].speed;
			var address = locationArrayList[currentPosition].address;
			content = '时间：' + retnruEmptyValue(tracetime, '0') + '<br/>' + '状态：'
					+ retnruEmptyValue(status, '0') + '<br/>' + '经度：'
					+ locationArrayList[currentPosition].longitude + '<br/>' + '纬度：' + locationArrayList[currentPosition].latitude
					+ '<br/>' + '速度：' + retnruEmptyValue(speed, '1') + '<br/>'
					+ '地址：' + retnruEmptyValue(address, '0');
			infoWindow = new BMap.InfoWindow(content, opts);
			traceMap.openInfoWindow(infoWindow, new BMap.Point(locationArrayList[currentPosition].longitude,locationArrayList[currentPosition].latitude));
		}
	},
	refreshAction : function() {
		currentPosition = 0;
		speed = 2100;
		task.interval = speed;
		var traceMap = Ext.getCmp('tripTraceMapPanel').bmap;
		traceMap.removeOverlay(carMk);
		carMk = undefined;
		Ext.getCmp('historyTracePlayButton').setDisabled(true);
		Ext.getCmp('historyTracePauseButton').setDisabled(false);
		Ext.getCmp('historyTraceStopButton').setDisabled(false);
		Ext.getCmp('historyTraceBackwardButton').setDisabled(false);
		Ext.getCmp('historyTraceForwardButton').setDisabled(false);
		runner.stop(task);
		this.executeRunner();
	},
	executeRunner : function() {
		var traceMap = Ext.getCmp('tripTraceMapPanel').bmap;
		var myP1 = new BMap.Point(locationArrayList[0].longitude,locationArrayList[0].latitude); // 起点
		var myP2 = new BMap.Point(locationArrayList[locationArrayList.length - 1].longitude,locationArrayList[locationArrayList.length - 1].latitude); // 终点
		var pts = locationArrayList;
		var paths = pts.length; // 获得有几个点
		if (carMk == undefined) {
			carMk = new BMap.Marker(myP1);
			traceMap.addOverlay(carMk);
		}
		var i = 0;
		var content;
		
		task = {
			run : function() {
				executeTask(currentPosition++, pts, traceMap,carMk, content, opts);
			},
			interval : speed
		};
		runner.start(task);
				
	},
	closeDialog : function() {
		carMk = undefined;
		currentPosition = 0;
		speed = 2100;
		task.interval = speed;
		runner.stop(task);
	},
	onClickSearch : function() {
		console.log('+++onClickSearch+++');
		this.stopAction();
		this.initView();
		var frmValues = this.lookupReference('historySearchForm').getValues();
		if (frmValues.sDate == '') {
			Ext.MessageBox.alert("消息提示", "请选择开始时间!");
			return;
		}
		if (frmValues.sHour == '') {
			Ext.MessageBox.alert("消息提示", "请填写完整的开始时间!");
			return;
		}
		if (frmValues.sMinute == '') {
			Ext.MessageBox.alert("消息提示", "请填写完整的开始时间!");
			return;
		}
		if (frmValues.sSecond == '') {
			Ext.MessageBox.alert("消息提示", "请填写完整的开始时间!");
			return;
		}
		if (frmValues.eDate == '') {
			Ext.MessageBox.alert("消息提示", "请选择结束时间!");
			return;
		}
		if (frmValues.eHour == '') {
			Ext.MessageBox.alert("消息提示", "请填写完整的结束时间!");
			return;
		}
		if (frmValues.eMinute == '') {
			Ext.MessageBox.alert("消息提示", "请填写完整的结束时间!");
			return;
		}
		if (frmValues.eSecond == '') {
			Ext.MessageBox.alert("消息提示", "请填写完整的结束时间!");
			return;
		}
		var startTime = frmValues.sDate + ' ' + frmValues.sHour+ ":" + frmValues.sMinute + ":"+ frmValues.sSecond;
		var endTime = frmValues.eDate + ' ' + frmValues.eHour+ ":" + frmValues.eMinute + ":"+ frmValues.eSecond;
		var startDate = new Date(startTime.replace(/-/g, "/"));
		var endDate = new Date(endTime.replace(/-/g, "/"));
		if (endDate <= startDate) {
			Ext.MessageBox.alert("消息提示", "结束时间必须晚于开始时间!");
			return;
		}
		var myDate = new Date();
		if (myDate < endDate) {
			Ext.MessageBox.alert("消息提示", "结束时间不能大于当前时间!");
			return;
		}
		var imei = Ext.getCmp('tripTraceWindow').imei;
		console.log('+++onClickSearch+++imei:' + imei);
		this.loadTripTraceMapView(imei, startTime, endTime);
		var input = {
			"imei" : imei,
			"starttime" : startTime,
			"endtime" : endTime
		};
		input = Ext.encode(input);
		Ext.getCmp("tripTraceCountGrid").getViewModel().getStore("vehicleRealtimeStore").proxy.extraParams = {
			"json" : input
		}
		Ext.getCmp("tripTraceCountGrid").getViewModel().getStore("vehicleRealtimeStore").load();
	}
});
