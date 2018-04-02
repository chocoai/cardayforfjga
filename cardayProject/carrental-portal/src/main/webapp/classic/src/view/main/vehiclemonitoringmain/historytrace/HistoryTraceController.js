/**
 * This class is the template view for the application.
 */

var locationArrayList = new Array();//轨迹坐标集合
var currentPosition = 1;//当前坐标集合标识
var runner = new Ext.util.TaskRunner();//定时器
var task = {};//执行任务
var speed = 2100;
var carMk = undefined;
var infoWindow;
var opts = {
    width : 230, // 信息窗口宽度
    height : 120, // 信息窗口高度
    enableMessage : true
};

function executeTaskForMain(count, pts, map, carMk, content, opts) {
//	console.log('++executeTask++');
//	var traceMap = Ext.getCmp('tripTraceMapPanel').bmap;
    if(count<pts.length){
        console.log(count);
//		map.centerAndZoom(new BMap.Point(pts[count].longitude, pts[count].latitude),map.getZoom()+3);
//		map.removeOverlay(carMk);
//		carMk = new BMap.Marker(pts[i],{icon:myIcon});
//		carMk = new BMap.Marker(pts[count]);
//		map.addOverlay(carMk);
        carMk.setPosition(new BMap.Point(pts[count].longitude, pts[count].latitude));
        var tracetime = pts[count].tracetime;
        var status = pts[count].status;
//		var direction = pts[count].direction;
        var speed = pts[count].speed;
        var address = pts[count].address;
        //调用百度api查找地址
        var myGeo = new BMap.Geocoder();
        var pt = new BMap.Point(parseFloat(pts[count].longitude),parseFloat(pts[count].latitude))
        myGeo.getLocation(pt, function(rs){
            var addComp = rs.addressComponents;
            // document.getElementById("result").innerHTML += index + ". " +adds[index-1].lng + "," + adds[index-1].lat + "："  + "商圈(" + rs.business + ")  结构化数据(" + addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber + ")<br/><br/>";
            address=addComp.province+addComp.city+addComp.district+addComp.street+addComp.streetNumber;//将地址写入数组
            content = '时间：' + retnruEmptyValue(tracetime, '0') + '<br/>'
            + '状态：' + retnruEmptyValue(status, '0') + '<br/>'
            + '经度：' + pts[count].longitude + '<br/>'
            + '纬度：' + pts[count].latitude + '<br/>'
            + '速度：' + retnruEmptyValue(speed, '1') + '<br/>'
            + '地址：' + retnruEmptyValue(address, '0');
	        infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象
	        map.openInfoWindow(infoWindow,new BMap.Point(pts[count].longitude, pts[count].latitude));
        });
        /*content = '时间：' + retnruEmptyValue(tracetime, '0') + '<br/>'
            + '状态：' + retnruEmptyValue(status, '0') + '<br/>'
            + '经度：' + pts[count].longitude + '<br/>'
            + '纬度：' + pts[count].latitude + '<br/>'
            + '速度：' + retnruEmptyValue(speed, '1') + '<br/>'
            + '地址：' + retnruEmptyValue(address, '0');
        infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象
        map.openInfoWindow(infoWindow,new BMap.Point(pts[count].longitude, pts[count].latitude));*/
    }else{
        runner.stop(task);
        currentPosition = 1;
    }
}

function retnruEmptyValue(value, type) {
    if(typeof(value)=="undefined" || value == null) {
        return '';
    }
    if(type == '1') {
        return '约' + value + 'km/h';
    }
    return value;
}
function GetDateStr(AddDayCount) {
    var dd = new Date();
    dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
    var y = dd.getFullYear();
    var m = dd.getMonth()+1;//获取当前月份的日期
    var d = dd.getDate();
    return y+"-"+m+"-"+d;
}

function GetYesterDayTime() {
    var dd = new Date();
    dd.setDate(dd.getDate()-1);
    var y = dd.getFullYear();
    var m = dd.getMonth()+1;
    if(m < 10){
        m = '0' + m;
    }
    var d = dd.getDate();
    if(d < 10){
        d = '0' + d;
    }
    return y+"-"+m+"-"+d;
}

function VehiclePlayAction(map, locationList) {
    console.log('+++VehiclePlayAction+++');
    // 百度地图API功能

    var myP1 = new BMap.Point(locationList[0].longitude,locationList[0].latitude);    //起点
    var myP2 = new BMap.Point(locationList[locationList.length-1].longitude,locationList[locationList.length-1].latitude);    //终点
    var pts = locationList;
    var paths = pts.length;    //获得有几个点
//	var carMk = new BMap.Marker(pts[0],{icon:myIcon});
    var carMk = new BMap.Marker(pts[0]);
    map.addOverlay(carMk);
    var i=0;
    var content;
    var opts = {
        width : 230,     // 信息窗口宽度
        height: 120,     // 信息窗口高度
//			title : "信息窗口" , // 信息窗口标题
        enableMessage:true//设置允许信息窗发送短息
    };
    var infoWindow;
    function resetMkPoint(i){
//		carMk.setPosition(pts[i]);
        map.removeOverlay(carMk);
//		carMk = new BMap.Marker(pts[i],{icon:myIcon});
        carMk = new BMap.Marker(pts[i]);
        map.addOverlay(carMk);
//		carMk.setPosition(new BMap.Point(pts[i].lng, pts[i].lat));

        content = '时间：2016-12-28 14:15:32<br/>状态：行驶中<br/>经度：'
            + pts[i].longitude + '<br/>纬度：' + pts[i].latitude + '<br/>'
            + '方向：向西<br/>速度：约10km/h<br/>地址：武汉市珞瑜路';

        infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象
        map.openInfoWindow(infoWindow,new BMap.Point(pts[i].longitude, pts[i].latitude));



//		if(i < paths){
//			setTimeout(function(){
//				i++;
//				resetMkPoint(i);
//			},1000);
//		}
    }

    resetMkPoint(1);

}

Ext.define('Admin.view.main.vehiclemonitoringmain.historytrace.HistoryTraceController', {
    extend: 'Ext.app.ViewController',
    requires: [
    ],

    loadTripTraceWindowInfo : function() {
//    	Ext.getCmp("tripTraceCountGrid").getViewModel().getStore("vehicleRealtimeStore").proxy.url = "app/data/vehiclemgmt/realtime_monitoring/historyTraceData.json";
//    	Ext.getCmp("tripTraceCountGrid").getViewModel().getStore("vehicleRealtimeStore").proxy.url = "vehicle/monitor/findTripPropertyDataByTimeRange";
        var startTime = GetDateStr(-1) + ' 00:00:00';
        var endTime = GetDateStr(-1) + ' 23:59:59';
        var imei = Ext.getCmp('tripTraceWindow').imei;
        var input = {
            "imei":imei,
//    			"starttime":"2017-01-11 14:25:00",
//    			"endtime":"2017-01-11 14:35:00"
            "starttime":startTime,
            "endtime":endTime
        };
        input = Ext.encode(input);
        Ext.getCmp("tripTraceCountGrid").getViewModel().getStore("vehicleRealtimeStore").proxy.extraParams = {
            "json" : input
        }
        Ext.getCmp("tripTraceCountGrid").getViewModel().getStore("vehicleRealtimeStore").load();
    },
    initTripTraceMap : function() {
        var imei = Ext.getCmp('tripTraceWindow').imei;
        console.log('imei:' + imei);
//    	imei = '41042502439';
//    	var startTime = '2017-01-09 00:00:00';
//    	var endTime = '2017-01-09 23:59:59';
        var startTime = GetDateStr(-1) + ' 00:00:00';
        var endTime = GetDateStr(-1) + ' 23:59:59';
        console.log('startTime:' + startTime);
        console.log('endTime:' + endTime);
        this.initView();
        this.loadTripTraceMapView(imei, startTime, endTime);
    },
    initView : function() {
        locationArrayList = new Array();
        speed = 2100;
        task.interval = speed;
//    	var traceMap = Ext.getCmp('tripTraceMapPanel').bmap;
        var mappanel = Ext.getCmp("VehicleMonitoringStaticInfo").down("mappanel");
        var traceMap = mappanel.bmap;
        traceMap.clearOverlays();
        traceMap.reset();
//    	traceMap.centerAndZoom("武汉",15);

        traceMap.centerAndZoom(new BMap.Point(119.314088, 26.08788), 11);

/*        function myFun(result){
            var cityName = result.name;
            console.log('initView:' + cityName);
            traceMap.setCenter(cityName);
            traceMap.centerAndZoom(cityName,12);
        }
        var myCity = new BMap.LocalCity();

        myCity.get(myFun);*/

    },
    loadTripTraceMapView : function(vehname, startTime, endTime) {
//    	var traceMap = Ext.getCmp('tripTraceMapPanel').bmap;
        var mappanel = Ext.getCmp("VehicleMonitoringStaticInfo").down("mappanel");
        var traceMap = mappanel.bmap;

		/*
		 var polyline = new BMap.Polyline([
		 new BMap.Point(116.405, 39.920),
		 new BMap.Point(116.425, 39.900),
		 new BMap.Point(116.399, 39.910),
		 new BMap.Point(116.385243,39.913063),
		 new BMap.Point(116.387112,39.920977),
		 new BMap.Point(116.394226,39.917988),
		 new BMap.Point(116.401772,39.921364),
		 new BMap.Point(116.41248,39.927893)
		 ], {strokeColor:"blue", strokeWeight:10, strokeOpacity:10,icons:[draw_line_direction(8)]});
		 var point = new BMap.Point(116.404, 39.915);

		 traceMap.centerAndZoom(point, 15);
		 traceMap.addOverlay(polyline);

		 function draw_line_direction(weight) {
		 return new BMap.IconSequence(
		 new BMap.Symbol('M0 -5 L-5 -2 L0 -4 L5 -2 Z', {
		 scale: weight/10,
		 strokeWeight: 2,
		 rotation: 0,
		 fillColor: 'white',
		 fillOpacity: 1,
		 strokeColor:'white'
		 }),'100%','5%',false);
		 }
		 */


        //    	var imei = '41042502439';
//    	var startTime = '2017-01-05 15:30:00';
//    	var endTime = '2017-01-05 15:35:00';

        var input = {
            'vehname' : decodeURIComponent(vehname),
            'starttime' : startTime,
            'endtime' : endTime
        };

        var tripTraceMapPanel = Ext.getCmp('vehMoniHistoryTrace');
        var myMask = new Ext.LoadMask({
            msg    : '正在导入历史数据，请稍候...',
            target : tripTraceMapPanel
        });
        myMask.show();
        var json = Ext.encode(input);

        Ext.Ajax.request({
//									url : 'vehicle/monitor/findVehicleHistoryTrack',//?json=' + Ext.encode(input),
//             url : 'vehicle/monitor/findVehicleHistoryTrackByName',//?json=' + Ext.encode(input),
            url : 'vehicle/monitor/findVehicleHistoryTrackWithoutAddress',
            timeout : 60000,
//									method : 'GET',
            method : 'POST',
            params:{json:json},
            wait:"正在搜索轨迹数据，请稍候...",
            scope : this,

            success : function(resp, opts) {
                myMask.hide();
                var location = Ext.util.JSON
                    .decode(resp.responseText);
                traceMap.clearOverlays();
                // console.log('location::' + JSON.stringify(location));
//                 location = {"data":[{"tracetime":"2017-06-04 09:23:01","longitude":"121.55472333373","latitude":"31.202475229795","speed":"18","address":"上海市浦东新区沪南路200号","status":"行驶"},{"tracetime":"2017-06-04 09:23:21","longitude":"121.55498843366","latitude":"31.201930025574","speed":"9","address":"上海市浦东新区前程路442号","status":"行驶"},{"tracetime":"2017-06-04 09:26:44","longitude":"121.55067753063","latitude":"31.199752764403","speed":"18","address":"上海市浦东新区前程路107号","status":"行驶"},{"tracetime":"2017-06-04 09:28:10","longitude":"121.54691552091","latitude":"31.198951068159","speed":"8","address":"上海市浦东新区前程路43号","status":"行驶"},{"tracetime":"2017-06-04 09:30:10","longitude":"121.54748831374","latitude":"31.198766624187","speed":"2","address":"上海市浦东新区前程路66号","status":"行驶"},{"tracetime":"2017-06-04 09:35:03","longitude":"121.54919086113","latitude":"31.199176506767","speed":"1","address":"上海市浦东新区前程路146号","status":"行驶"},{"tracetime":"2017-06-04 09:36:49","longitude":"121.54818667685","latitude":"31.199042786356","speed":"3","address":"上海市浦东新区前程路100号","status":"行驶"},{"tracetime":"2017-06-04 09:38:49","longitude":"121.5476942868","latitude":"31.198601152651","speed":"3","address":"上海市浦东新区前程路88号","status":"行驶"},{"tracetime":"2017-06-04 09:39:19","longitude":"121.54762637976","latitude":"31.198785657723","speed":"1","address":"上海市浦东新区前程路72号","status":"行驶"},{"tracetime":"2017-06-04 09:39:39","longitude":"121.54761444704","latitude":"31.198447120498","speed":"3","address":"上海市浦东新区前程路88号","status":"行驶"},{"tracetime":"2017-06-04 09:40:36","longitude":"121.54816194763","latitude":"31.198311439846","speed":"10","address":"上海市浦东新区前程路88号","status":"行驶"},{"tracetime":"2017-06-04 09:40:48","longitude":"121.54793666999","latitude":"31.198069126343","speed":"5","address":"上海市浦东新区前程路88号","status":"行驶"},{"tracetime":"2017-06-04 11:16:48","longitude":"121.54762912322","latitude":"31.19911753675","speed":"1","address":"上海市浦东新区前程路55","status":"行驶"},{"tracetime":"2017-06-04 11:16:55","longitude":"121.54764286791","latitude":"31.19879487492","speed":"4","address":"上海市浦东新区前程路74号","status":"行驶"},{"tracetime":"2017-06-04 11:20:23","longitude":"121.54723235986","latitude":"31.198920139185","speed":"0","address":"上海市浦东新区前程路58号","status":"停止"},{"tracetime":"2017-06-04 11:22:35","longitude":"121.54911799677","latitude":"31.199276614415","speed":"12","address":"上海市浦东新区前程路144号","status":"行驶"}],"status":"success"};
//                location = {"data": [{ "tracetime": "2017-06-04 09:23:01", "longitude": "121.55472333373", "latitude": "31.202475229795", "speed": "18", "status": "行驶" }, { "tracetime": "2017-06-04 09:23:21", "longitude": "121.55498843366", "latitude": "31.201930025574", "speed": "9", "status": "行驶" }, { "tracetime": "2017-06-04 09:26:44", "longitude": "121.55067753063", "latitude": "31.199752764403", "speed": "18", "status": "行驶" }, { "tracetime": "2017-06-04 09:28:10", "longitude": "121.54691552091", "latitude": "31.198951068159", "speed": "8", "status": "行驶" }, { "tracetime": "2017-06-04 09:30:10", "longitude": "121.54748831374", "latitude": "31.198766624187", "speed": "2", "status": "行驶" }, { "tracetime": "2017-06-04 09:35:03", "longitude": "121.54919086113", "latitude": "31.199176506767", "speed": "1", "status": "行驶" }, { "tracetime": "2017-06-04 09:36:49", "longitude": "121.54818667685", "latitude": "31.199042786356", "speed": "3", "status": "行驶" }, { "tracetime": "2017-06-04 09:38:49", "longitude": "121.5476942868", "latitude": "31.198601152651", "speed": "3", "status": "行驶" }, { "tracetime": "2017-06-04 09:39:19", "longitude": "121.54762637976", "latitude": "31.198785657723", "speed": "1", "status": "行驶" }, { "tracetime": "2017-06-04 09:39:39", "longitude": "121.54761444704", "latitude": "31.198447120498", "speed": "3", "status": "行驶" }, { "tracetime": "2017-06-04 09:40:36", "longitude": "121.54816194763", "latitude": "31.198311439846", "speed": "10", "status": "行驶" }, { "tracetime": "2017-06-04 09:40:48", "longitude": "121.54793666999", "latitude": "31.198069126343", "speed": "5", "status": "行驶" }, { "tracetime": "2017-06-04 11:16:48", "longitude": "121.54762912322", "latitude": "31.19911753675", "speed": "1", "status": "行驶" }, { "tracetime": "2017-06-04 11:16:55", "longitude": "121.54764286791", "latitude": "31.19879487492", "speed": "4", "status": "行驶" }, { "tracetime": "2017-06-04 11:20:23", "longitude": "121.54723235986", "latitude": "31.198920139185", "speed": "0", "status": "停止" }, { "tracetime": "2017-06-04 11:22:35", "longitude": "121.54911799677", "latitude": "31.199276614415", "speed": "12", "status": "行驶" }], "status": "success"};
				/*location = {"data": [{
				 "tracetime": "2017-06-04 09:23:01",
				 "longitude": "121.55472333373",
				 "latitude": "31.202475229795",
				 "speed": "18",
				 "address": "上海市浦东新区沪南路200号",
				 "status": "行驶"
				 },
				 {
				 "tracetime": "2017-06-04 11:22:35",
				 "longitude": "121.54911799677",
				 "latitude": "31.199276614415",
				 "speed": "12",
				 "address": "上海市浦东新区前程路144号",
				 "status": "行驶"
				 }],
				 "status": "success"};*/
                if (location.status == 'success') {
                    if(location.data.length == 0) {
                        Ext.MessageBox.alert("消息提示","未查询到轨迹记录！");
                        //开始按钮禁用
                        Ext.getCmp("vehMoniTracePlayButton").setDisabled(true);
                        locationArrayList = new Array();
                        return;
                    }
                    /*//开始百度批量解析地址程序
                    var index = 0;//地址index
                    var myGeo = new BMap.Geocoder();//声明百度地址查询接口
                    var adds=[];//百度地址查询临时数组
                    for(var i =0;i<location.data.length;i++){//将地址的经纬度push到临时数组中
                        adds.push(new BMap.Point(parseFloat(location.data[i].longitude),parseFloat(location.data[i].latitude)));
                    }
                    //bdGEO();//开始调用百度地址查询接口
                    function bdGEO(){
                        var pt = adds[index];
                        if(index<location.data.length){
                            geocodeSearch(pt);
						}
                        index++;
                        if(index>=location.data.length){
                            setTimeout(function(){
                            	successGo();//全部查询完毕加载地图
							},200)
                        }
                    }
                    function geocodeSearch(pt){
                        if(index < adds.length-1){
                            setTimeout(function(){bdGEO()},100);//两次调用之间间隔，防止出错
                        }
                        myGeo.getLocation(pt, function(rs){
                            var addComp = rs.addressComponents;
                            // document.getElementById("result").innerHTML += index + ". " +adds[index-1].lng + "," + adds[index-1].lat + "："  + "商圈(" + rs.business + ")  结构化数据(" + addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber + ")<br/><br/>";
                            location.data[index-1].address=addComp.province+addComp.city+addComp.district+addComp.street+addComp.streetNumber;//将地址写入数组
                        });
                    }*/

//                    function successGo(){//全部读取完毕后开始加载地图内容
                        var BPointArr = new Array();
                        var pstart, pend, startPoint, endPoint;
                        var numArray = 0;
                        //开始按钮启用
                        Ext.getCmp("vehMoniTracePlayButton").setDisabled(false);
                        locationArrayList = new Array();
                        for (var i = 0; i < location.data.length; i++) {
                            if (location.data[i].longitude != null
                                && location.data[i].latitude != null) {
                                locationArrayList[numArray] = location.data[i];
                                numArray++;
                            }
                        }

                        for (var i = 0; i < locationArrayList.length; i++) {

                            pstart = new BMap.Point(
                                locationArrayList[i].longitude,
                                locationArrayList[i].latitude);
                            BPointArr
                                .push(new BMap.Point(
                                    locationArrayList[i].longitude,
                                    locationArrayList[i].latitude));

//												pend = new BMap.Point(
//														locationArrayList[i + 1].longitude,
//														locationArrayList[i + 1].latitude);
                            // BPointArr
                            // 		.push(new BMap.Point(
                            // 				locationArrayList[i + 1].longitude,
                            // 				locationArrayList[i + 1].latitude));
                        }


                        //设置起点和终点
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

                            if(locationArrayList.length != 1){
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
                        }

                        //traceMap.centerAndZoom(startPoint, 17);

                        function draw_line_direction(weight) {
                            var icons=new BMap.IconSequence(
                                new BMap.Symbol(BMap_Symbol_SHAPE_BACKWARD_CLOSED_ARROW, {
                                    scale: weight/20,
                                    strokeWeight: 1,
                                    rotation: 0,
                                    fillColor: 'white',
                                    fillOpacity: 1,
                                    strokeColor:'white'
                                }),'100%','5%',false);
                            console.log(icons);
                            return icons;
                        }

                        var polyline = new BMap.Polyline(
                            BPointArr, {
//														strokeColor : "red",
                                strokeColor : "blue",
                                strokeWeight : 8,
                                strokeOpacity : 0.8,
                                icons:[draw_line_direction(10)]
                            });
                        traceMap.addOverlay(polyline);

                        traceMap.setViewport(BPointArr); //先设置viewport，再设置中心点
                        traceMap.setCenter(pstart);
                        traceMap.zoomOut();


//											this.offsetTripTraceMap(traceMap); //对地图做偏移，以保证起点显示在正中

//											this.lookupReference('tripTraceMapPanel')
//													.up().up()
//													.setLoading(false);
                        Ext.getCmp('VehicleMonitoringStaticInfo').down("mappanel")
                            .up().up()
                            .setLoading(false);

                        //轨迹回放
//											this.toPlayCarMoveOpea(traceMap, locationArrayList);
                        //end
//                    }
//											var detailLocation = Ext.util.JSON
//													.decode(location.data[0].tracegeometry);


                } else {
//											this.lookupReference('tripTraceMapPanel')
//													.up().up()
//													.setLoading(false);
                    Ext.getCmp('VehicleMonitoringStaticInfo').down("mappanel")
                        .up().up()
                        .setLoading(false);
                    locationArrayList = new Array();
                    //开始按钮禁用
                    Ext.getCmp("vehMoniTracePlayButton").setDisabled(true);
                    Ext.MessageBox.show({
                        title : '异常提示',
                        msg : '未查询到轨迹记录！',
                        icon : Ext.MessageBox.ERROR,
                        buttons : Ext.Msg.OK
                    });
                }

            },

            failure : function(response, opts) {
                traceMap.clearOverlays();
                this.lookupReference('VehicleMonitoringStaticInfo').down("mappanel").up()
                    .up().setLoading(false);
                //开始按钮禁用
                Ext.getCmp("vehMoniTracePlayButton").setDisabled(true);
                locationArrayList = new Array();
                Ext.MessageBox.show({
                    title : '异常提示',
                    msg : '未查询到轨迹记录！',
                    icon : Ext.MessageBox.ERROR,
                    buttons : Ext.Msg.OK
                });
            },

        });
    },
    offsetTripTraceMap: function(map) {
//        var xOffset = -(this.lookupReference('tripTraceMapPanel').getWidth() - this.lookupReference('tripTraceMap').getWidth()) / 2;
//        var yOffset = -(this.lookupReference('tripTraceMapPanel').getHeight() - this.lookupReference('tripTraceMap').getHeight()) / 2;
        var mappanel = Ext.getCmp("VehicleMonitoringStaticInfo").down("mappanel");
        var xOffset = -(mappanel.getWidth() - mappanel.getWidth()) / 2;
        var yOffset = -(mappanel.getHeight() - mappanel.getHeight()) / 2;
        var panOptions = {
            noAnimation: true
        };
        map.panBy(xOffset, yOffset, panOptions);
    },
    toPlayCarMoveOpea : function (map, locationList) {
        console.log('+++toPlayCarMoveOpea+++');
        // 百度地图API功能

        this.playMap(map, locationList);
        var offsetSideMap = function (){
//    		map.clearOverlays();
            var myP1 = new BMap.Point(locationList[0].longitude,locationList[0].latitude);    //起点
            var myP2 = new BMap.Point(locationList[locationList.length-1].longitude,locationList[locationList.length-1].latitude);    //终点
            var myIcon = new BMap.Icon('http://developer.baidu.com/map/jsdemo/img/car.png', new BMap.Size(52,26), {    //小车图片
                //offset: new BMap.Size(0, -5),    //相当于CSS精灵
                imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
            });
            var pts = locationList;
            var paths = pts.length;    //获得有几个点
//    		var carMk = new BMap.Marker(pts[0],{icon:myIcon});
            var carMk = new BMap.Marker(pts[0]);
            map.addOverlay(carMk);
//    		var i=0;
            function resetMkPoint(i){
//    			carMk.setPosition(pts[i]);
                map.removeOverlay(carMk);
//        		carMk = new BMap.Marker(pts[i],{icon:myIcon});
                carMk = new BMap.Marker(pts[i]);
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
//    var runner = new Ext.util.TaskRunner(),
//       task = runner.start({
//            run: offsetSideMap,
//            interval: 25000,
//       });
    },
    playMap : function(map, locationList) {
        var myP1 = new BMap.Point(locationList[0].longitude,locationList[0].latitude);    //起点
        var myP2 = new BMap.Point(locationList[locationList.length-1].longitude,locationList[locationList.length-1].latitude);    //终点
        var myIcon = new BMap.Icon('http://developer.baidu.com/map/jsdemo/img/car.png', new BMap.Size(52,26), {    //小车图片
            //offset: new BMap.Size(0, -5),    //相当于CSS精灵
            imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
        });
        var pts = locationList;
        var paths = pts.length;    //获得有几个点
//		var carMk = new BMap.Marker(pts[0],{icon:myIcon});
        var carMk = new BMap.Marker(pts[0]);
        map.addOverlay(carMk);
        var i=0;
        var content;
        var opts = {
            width : 230,     // 信息窗口宽度
            height: 120,     // 信息窗口高度
            //			title : "信息窗口" , // 信息窗口标题
            enableMessage:true//设置允许信息窗发送短息
        };
        var infoWindow;
        function resetMkPoint(i){
//			carMk.setPosition(pts[i]);
            map.removeOverlay(carMk);
//    		carMk = new BMap.Marker(pts[i],{icon:myIcon});
            carMk = new BMap.Marker(pts[i]);
            map.addOverlay(carMk);
//			carMk.setPosition(new BMap.Point(pts[i].lng, pts[i].lat));

            content = '时间：2016-12-28 14:15:32<br/>状态：行驶中<br/>经度：'
                + pts[i].longitude + '<br/>纬度：' + pts[i].latitude + '<br/>'
                + '方向：向西<br/>速度：约10km/h<br/>地址：武汉市珞瑜路';

            infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象
            map.openInfoWindow(infoWindow,new BMap.Point(pts[i].longitude, pts[i].latitude));



            if(i < paths){
                setTimeout(function(){
                    i++;
                    resetMkPoint(i);
                },1000);
            }
        }
//		setTimeout(function(){
//			console.log('++load++resetMkPoint');
        resetMkPoint(1);
//		},1000)
    },
    playAction : function() {	//开始
//    	console.log('playAction');
//    	var traceMap = Ext.getCmp('tripTraceMapPanel').bmap;
//    	this.playMap(traceMap, locationArrayList);
        if(locationArrayList == null || locationArrayList.length == 0) {
            Ext.MessageBox.alert("消息提示","无对应轨迹，请先查询!");
            return;
        }
        Ext.getCmp('vehMoniTracePlayButton').setDisabled(true);
        Ext.getCmp('vehMoniTracePauseButton').setDisabled(false);
        Ext.getCmp('vehMoniTraceStopButton').setDisabled(false);
        Ext.getCmp('vehMoniTraceBackwardButton').setDisabled(false);
        Ext.getCmp('vehMoniTraceForwardButton').setDisabled(false);
        Ext.getCmp('vehMoniTraceResetButton').setDisabled(false);
        this.executeRunner();
    },
    pauseAction : function() {	//暂停
//    	console.log('pauseAction');
        Ext.getCmp('vehMoniTracePlayButton').setDisabled(false);
        Ext.getCmp('vehMoniTracePauseButton').setDisabled(true);
        Ext.getCmp('vehMoniTraceStopButton').setDisabled(true);
        Ext.getCmp('vehMoniTraceBackwardButton').setDisabled(true);
        Ext.getCmp('vehMoniTraceForwardButton').setDisabled(true);
        Ext.getCmp('vehMoniTraceResetButton').setDisabled(false);
        runner.stop(task);
    },
    stopAction : function() {
//    	console.log('stopAction');
        var mappanel = Ext.getCmp("VehicleMonitoringStaticInfo").down("mappanel");
        var traceMap = mappanel.bmap;
//    	var traceMap = Ext.getCmp('tripTraceMapPanel').bmap;
        Ext.getCmp('vehMoniTracePlayButton').setDisabled(false);
        Ext.getCmp('vehMoniTracePauseButton').setDisabled(true);
        Ext.getCmp('vehMoniTraceStopButton').setDisabled(true);
        Ext.getCmp('vehMoniTraceBackwardButton').setDisabled(true);
        Ext.getCmp('vehMoniTraceForwardButton').setDisabled(true);
        Ext.getCmp('vehMoniTraceResetButton').setDisabled(true);
        traceMap.removeOverlay(carMk);
        carMk = undefined;
        traceMap.closeInfoWindow(infoWindow);
        runner.stop(task);
        currentPosition = 1;
    },
    backwardAction : function() {
//    	console.log('backwardAction');
		/*if(currentPosition>1){
		 currentPosition--;
		 }*/
        if (currentPosition > 0) {
            this.pauseAction();
            currentPosition--;
            if(currentPosition > 0){
                currentPosition--;
            }
            carMk.setPosition(new BMap.Point(locationArrayList[currentPosition].longitude,locationArrayList[currentPosition].latitude));

//			var traceMap = Ext.getCmp('tripTraceMapPanel').bmap;
            var mappanel = Ext.getCmp("VehicleMonitoringStaticInfo").down("mappanel");
            var traceMap = mappanel.bmap;
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
            this.playAction();
        }
    },
    forwardAction : function() {
//    	console.log('forwardAction');
		/*if(speed >100){
		 speed = speed - 500;
		 task.interval = speed;
		 }*/
        if (currentPosition < locationArrayList.length - 1) {
            this.pauseAction();
            currentPosition++;
            carMk.setPosition(new BMap.Point(locationArrayList[currentPosition].longitude,locationArrayList[currentPosition].latitude));

//			var traceMap = Ext.getCmp('tripTraceMapPanel').bmap;
            var mappanel = Ext.getCmp("VehicleMonitoringStaticInfo").down("mappanel");
            var traceMap = mappanel.bmap;
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
            this.playAction();
        }
    },
    refreshAction : function() {
//    	console.log('refreshAction');
        currentPosition = 1;
        speed = 2100;
        task.interval = speed;
//		var traceMap = Ext.getCmp('tripTraceMapPanel').bmap;
        var mappanel = Ext.getCmp("VehicleMonitoringStaticInfo").down("mappanel");
        var traceMap = mappanel.bmap;
        traceMap.removeOverlay(carMk);
        carMk = undefined;
        Ext.getCmp('vehMoniTracePlayButton').setDisabled(true);
        Ext.getCmp('vehMoniTracePauseButton').setDisabled(false);
        Ext.getCmp('vehMoniTraceStopButton').setDisabled(false);
        Ext.getCmp('vehMoniTraceBackwardButton').setDisabled(false);
        Ext.getCmp('vehMoniTraceForwardButton').setDisabled(false);
        runner.stop(task);
        this.executeRunner();
    },
    executeRunner : function() {
        //Initialization
//    	var traceMap = Ext.getCmp('tripTraceMapPanel').bmap;
        var mappanel = Ext.getCmp("VehicleMonitoringStaticInfo").down("mappanel");
        var traceMap = mappanel.bmap;
        var myP1 = new BMap.Point(locationArrayList[0].longitude,locationArrayList[0].latitude);    //起点
        var myP2 = new BMap.Point(locationArrayList[locationArrayList.length-1].longitude,locationArrayList[locationArrayList.length-1].latitude);    //终点
        var pts = locationArrayList;
        var paths = pts.length;    //获得有几个点
//    	var carMk = new BMap.Marker(pts[0],{icon:myIcon});
        if(carMk == undefined) {
            carMk = new BMap.Marker(pts[0]);
            traceMap.addOverlay(carMk);
        }
        var i=0;
        var content;
        var opts = {
            width : 230,     // 信息窗口宽度
            height: 120,     // 信息窗口高度
//    			title : "信息窗口" , // 信息窗口标题
            enableMessage:true//设置允许信息窗发送短息
        };
//    	var infoWindow;

        //create task
        task = { //Ext的定时器，每隔2秒刷新store。
            run : function() {
                executeTaskForMain(currentPosition++, pts, traceMap, carMk, content, opts);
            },
            interval : speed
        };

        runner.start(task);
    },
    closeDialog : function() {
//    	console.log('close window');
        carMk = undefined;
        currentPosition = 1;
        speed = 2100;
        task.interval = speed;
        runner.stop(task);
    },
    onClickSearch : function(btn, e, eOpts ) {
        console.log('+++onClickSearch+++');
        this.stopAction();
//    	this.initView();
        var frmValues = this.lookupReference('historySearchForm').getValues();
        //时间非空判断
        if(frmValues.sDate == '') {
            Ext.MessageBox.alert("消息提示","请选择开始时间!");
            return;
        }
        if(frmValues.sHour == '') {
            Ext.MessageBox.alert("消息提示","请填写完整的开始时间!");
            return;
        }
        if(frmValues.sMinute == '') {
            Ext.MessageBox.alert("消息提示","请填写完整的开始时间!");
            return;
        }
        if(frmValues.sSecond == '') {
            Ext.MessageBox.alert("消息提示","请填写完整的开始时间!");
            return;
        }
        if(frmValues.eDate == '') {
            Ext.MessageBox.alert("消息提示","请选择结束时间!");
            return;
        }
        if(frmValues.eHour == '') {
            Ext.MessageBox.alert("消息提示","请填写完整的结束时间!");
            return;
        }
        if(frmValues.eMinute == '') {
            Ext.MessageBox.alert("消息提示","请填写完整的结束时间!");
            return;
        }
        if(frmValues.eSecond == '') {
            Ext.MessageBox.alert("消息提示","请填写完整的结束时间!");
            return;
        }
        //只允许查询同一天内的历史轨迹
        //if(frmValues.sDate != frmValues.eDate) {
        //	Ext.MessageBox.alert("消息提示","只能查询同一天内的历史轨迹!");
        //	return;
        //}

        var win = btn.up("window");
        if(win.title == "今日轨迹"){
            var sDateStr = getTodayDate();
            var eDateStr = getTodayDate();
            var startTime = sDateStr + ' ' + frmValues.sHour + ":"+ frmValues.sMinute + ":"+frmValues.sSecond;
            var endTime = sDateStr + ' ' + frmValues.eHour + ":"+ frmValues.eMinute + ":"+frmValues.eSecond;
            var time1=new Date(startTime).getTime();
            var time2=new Date(endTime).getTime();
            //endTime必须小于startTime
            var startDate = new Date(startTime.replace(/-/g,"/"));
            var endDate = new Date(endTime.replace(/-/g,"/"));
            if(endDate <= startDate) {
                Ext.MessageBox.alert("消息提示","结束时间必须晚于开始时间!");
                return;
            }
        }else{
            //时间格式 2017-01-05 15:30:00
            var startTime = frmValues.sDate + ' ' + frmValues.sHour + ":"+ frmValues.sMinute + ":"+frmValues.sSecond;
            var endTime = frmValues.eDate + ' ' + frmValues.eHour + ":"+ frmValues.eMinute + ":"+frmValues.eSecond;
            var time1=new Date(startTime).getTime();
            var time2=new Date(endTime).getTime();
            //endTime必须小于startTime
            var startDate = new Date(startTime.replace(/-/g,"/"));
            var endDate = new Date(endTime.replace(/-/g,"/"));
            if(endDate <= startDate) {
                Ext.MessageBox.alert("消息提示","结束时间必须晚于开始时间!");
                return;
            }
        }

		/*if(time1>time2) {
		 Ext.MessageBox.alert("消息提示","开始时间不能大于结束时间!");
		 return;
		 }*/

        console.log('startTime:' + startTime);
        console.log('endTime:' + endTime);

        var myDate = new Date();
        if(myDate < endDate) {
            Ext.MessageBox.alert("消息提示","结束时间不能大于当前时间!");
            return;
        }
		/*var now = myDate.valueOf();
		 var time = new Date(endTime).valueOf();
		 if(now<time){
		 Ext.MessageBox.alert("消息提示","结束时间不能大于当前时间!");
		 return;
		 }*/



        var mainpanel = Ext.getCmp("vehicleMonitoringMain");
        var vehName = mainpanel.vehicleNumber;
        vehName = decodeURIComponent(vehName);

//    	var imei = Ext.getCmp('tripTraceWindow').imei;
//    	console.log('+++onClickSearch+++imei:' + imei);
//    	var imei = '41042502439';
//    	startTime = '2017-01-05 15:30:00';
//    	endTime = '2017-01-05 15:35:00';
        this.loadTripTraceMapView(vehName, startTime, endTime);


        var input = {
            "vehname":vehName,
            "starttime":startTime,
            "endtime":endTime
        };
        input = Ext.encode(input);
        this.loadTraceCountData(vehName, startTime, endTime);
//    	Ext.getCmp("tripTraceCountGrid").getViewModel().getStore("vehicleRealtimeStore").proxy.extraParams = {
//			"json" : input
//		}
//    	Ext.getCmp("tripTraceCountGrid").getViewModel().getStore("vehicleRealtimeStore").load();
    },

    onHistoryTraceClose: function(win, ops){
        this.stopAction();
        locationArrayList = new Array();	//清空轨迹arraylist
        if(win.refreshMap){
            //重新刷新地图，启动地图的计时器
            var panel = Ext.getCmp("vehicleMonitoringMain");
            panel.getController().afterrenderMap(panel);
        }
    },

    loadTraceCountData: function(vehName, startTime, endTime){
        var mainpanel = Ext.getCmp("vehicleMonitoringMain");
        var vehName = mainpanel.vehicleNumber;
        var input = {
            "vehname":decodeURIComponent(vehName),
            "starttime":startTime,
            "endtime":endTime
        };
        var json = Ext.encode(input);
        Ext.Ajax.request({
//			url : 'vehicle/monitor/findVehicleHistoryTrack',//?json=' + Ext.encode(input),
            url : 'vehicle/monitor/findTripPropertyDataByTimeRangeByName',//?json=' + Ext.encode(input),
            timeout : 60000,
//			method : 'GET',
            method : 'POST',
            params:{json:json},
            wait:"正在搜索数据，请稍候...",
            scope : this,
            success : function(resp, opts) {
                var result = Ext.util.JSON.decode(resp.responseText);
                Ext.getCmp("historyTraceVehData").query('displayfield').forEach(function(c){c.setValue(null);});
//				result = {"data":{"mileage":1.71,"fuel":0.19,"drivetime":1278,"stoptime":18522},"status":"success"};
                if (result.status == 'success') {
                    Ext.getCmp("historyTraceVehData").getForm().setValues(result.data);
                }
            },
            failure : function(response, opts){
                Ext.getCmp("historyTraceVehData").query('displayfield').forEach(function(c){c.setValue(null);});
            }
        });
    }
});

function getTodayDate() {
    var dd = new Date();
    var y = dd.getFullYear();
    var m = dd.getMonth()+1;
    if(m < 10){
        m = '0' + m;
    }
    var d = dd.getDate();
    if(d < 10){
        d = '0' + d;
    }
    return y+"-"+m+"-"+d;
}
