
/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.alertMgmt.outMarkerAlarm.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			'Admin.view.alertMgmt.outMarkerAlarm.View',
			'Admin.view.alertMgmt.outMarkerAlarm.SearchForm',
			'Admin.view.alertMgmt.backStationAlarm.ViewVehicleWindow'
			],
	alias : 'controller.outMarkerMgmtController',
 	onBeforeLoad:function(){
    	//所属部门
    	var frmValues=this.lookupReference('searchForm').getValues();
    	
    	if(''==frmValues['fromOrgId']||null==frmValues['fromOrgId']){
    		frmValues['fromOrgId']='-1';
    	}
    	if('所有类型'==frmValues['vehicleType'] || ''==frmValues['vehicleType']||null==frmValues['vehicleType']){
    		frmValues['vehicleType']='-1';
    	}
    	if(''==frmValues['deptId']||null==frmValues['deptId']){
    		frmValues['deptId']='-1';
    	}
        if(frmValues['startTime'] == ''){
            var initStartDay = new Date();
            /*查询30天前数据*/
            initStartDay = new Date(initStartDay.getTime() - 29*24*60*60*1000);
    		this.lookupReference('searchForm').getForm().findField('startTime').setValue(initStartDay);
    		frmValues['startTime']=this.lookupReference('searchForm').getValues().startTime + ' 00:00:00';
		}else{
			frmValues['startTime']= frmValues['startTime'] + ' 00:00:00';
		}

    	if(''!=frmValues['endTime']){
			frmValues['endTime']= frmValues['endTime'] + ' 23:59:59';
    	}

    	if(this.lookupReference('searchForm').getForm().isValid()){
	    	var page=Ext.getCmp('outMarkerPage').store.currentPage;
			var limit=Ext.getCmp('outMarkerPage').pageSize;
			frmValues.currentPage=page;
			frmValues.numPerPage=limit;
			this.getViewModel().getStore("outMarkerResults").proxy.extraParams = {
	             "json":Ext.encode(frmValues)
	        }
	    }else{
        	Ext.Msg.alert('消息提示','请输入有效的筛选条件！');
        }
    },
	onSearchClick:function(){
/*		if(window.sessionStorage.getItem("outMarkerMgmt") == '1'){
			this.lookupReference('searchForm').getForm().findField('startTime').setValue(new Date());
			this.lookupReference('searchForm').getForm().findField('endTime').setValue(new Date());
            window.sessionStorage.setItem("outMarkerMgmt", '0');
		}	*/
		var VehicleStore = this.lookupReference('outMarkerVehicleGrid').getStore();
		VehicleStore.currentPage = 1;
		this.getViewModel().getStore("outMarkerResults").load();
	},
	
	//查看marker和轨迹
	viewOutMarker:function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);	
		var win = Ext.widget("markerAndTraceWindow");
		win.vehicleNumber=rec.get('vehicleNumber');
		win.show();
		this.viewGeofenceMapinfo(rec,win);
	},
	initMap: function(map){
        //添加比例尺控件
    	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
    	var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
    	map.addControl(top_left_control);        
		map.addControl(top_left_navigation);
	},
	/*
	 *根据Ip地址展现当前城市
	getPosition:function(map){
		function myFun(result){
			var cityName = result.name;
			map.setCenter(cityName);
			map.centerAndZoom(cityName);
		}
		var myCity = new BMap.LocalCity();
		myCity.get(myFun);
	},*/
	//加载地理围栏
	initAddress:function(map){
		function myFun(result){
			var cityName = result.name;
			console.log('initView:' + cityName);
			map.centerAndZoom(cityName);
			map.setCenter(cityName);
		}
		var myCity = new BMap.LocalCity();
		myCity.get(myFun);
	},
	loadMarks:function(map,markers){
		for(var j=0;j<markers.length;j++){		
			var marker=markers[j];
			if(marker.type=='0'){
	//			行政区Marker(根据城市名称)
				var bdary = new BMap.Boundary();
			    bdary.get(marker.position, function(rs){  //获取行政区域
		            var count = rs.boundaries.length; //行政区域的点有多少个
		        	if (count === 0) {
		                Ext.Msg.alert('消息提示', '未能获取当前输入行政区域,请重新输入！');
		                return ;
		        	}
		            var pointArray = [];
		            for (var i = 0; i < count; i++) {
		                var ply = new BMap.Polygon(rs.boundaries[i], {strokeWeight: 2, strokeColor: "#ff0000"}); //建立多边形覆盖物
		                map.addOverlay(ply);  //添加覆盖物
		                pointArray = pointArray.concat(ply.getPath());//拿到多边形的点数组
		            }  
		            map.setViewport(pointArray);
		            map.addOverlay(pointArray);
				});
	//			自定义marker
			}else if(marker.type=='1'){
				 var points = marker.pattern.replace('!','');
				  points = points.replace('[[','[');
				  points = points.replace(']]',']');
				  var pointList = Ext.decode(points);
				  var BPointArr = new Array();
		          for(var i = 0 ; i < pointList.length; i++){
		              BPointArr.push(new BMap.Point(pointList[i].lng, pointList[i].lat));
		          }
		          var markerType = new BMap.Polygon(BPointArr, {strokeColor:"red", strokeWeight:3, strokeOpacity:0.8}); 
		 		  map.setViewport(BPointArr);
		 		  map.addOverlay(markerType);
			}else{
				
				var point = new BMap.Point(marker.longitude, marker.latitude);
		    	var radius = marker.radius;
	            map.centerAndZoom(point, 15);
	            map.addOverlay(new BMap.Marker(point));
	            var circle = new BMap.Circle(point,radius * 1000,{strokeColor:"red", strokeWeight:2, strokeOpacity:0.5});
	            map.addOverlay(circle);
			}
			
		}
	},
	//加载轨迹地图
	loadTrace:function(map,traceArrays){
		var trace=new Array();//后台已经过滤经纬度为0的点
		for(var i = 0; i < traceArrays.length; i++){
			trace.push(new BMap.Point(traceArrays[i].longitude, traceArrays[i].latitude));
		}
      //标注起点，终点以及行车路线(折线)
		var polyline = new BMap.Polyline(trace, {
	                        strokeColor: "black",
	                        strokeWeight: 5,
	                        strokeOpacity: 1
	                    });
        map.addOverlay(polyline);
	 	var startMarker = new BMap.Marker(trace[0]);  // 创建起点标注
	 	startMarker.setIcon(new BMap.Icon('resources/images/icons/mappin/Map-Marker-Marker-Outside-Chartreuse-icon.png', new BMap.Size(32, 32)));
		var startLabel = new BMap.Label("起点",{offset:new BMap.Size(-25,-15)});
	 	startMarker.setLabel(startLabel);
	 	map.addOverlay(startMarker);               
	 	var endMarker = new BMap.Marker(trace[trace.length-1]);  // 创建终点标注
	 	endMarker.setIcon(new BMap.Icon('resources/images/icons/mappin/Map-Marker-Marker-Outside-Azure-icon.png', new BMap.Size(32, 32)));
	 	var endLabel = new BMap.Label("终点",{offset:new BMap.Size(25,-15)});
	 	endMarker.setLabel(endLabel);
	 	map.setViewport(trace);
	 	map.addOverlay(endMarker);
	},
	//加载grid
	loadMarkGrid:function(rec,win,markers){
		/*var data = [{"vehicleNumber": rec.data.vehicleNumber,"alertType": "越界","outboundMinutes": 
		rec.data.outboundMinutes,"outboundKilos":rec.data.outboundKilos,"driverName":rec.data.driverName,"driverPhone":rec.data.driverPhone,"markerName":markers.markerName}];
		win.getViewModel().getStore("outMarkerResults").loadData(data);*/
		
		var data=new Array(); 
		for(var i=0;i<markers.length;i++){
			data.push({"vehicleNumber": rec.data.vehicleNumber,"alertType": "越界","outboundMinutes": 
				rec.data.outboundMinutes,"outboundKilos":rec.data.outboundKilos,"driverName":rec.data.driverName,"driverPhone":rec.data.driverPhone,"markerName":markers[i].markerName});
		}
		win.getViewModel().getStore("outMarkerResults").loadData(data);
	},
	//根据日期选择重新加载轨迹
	getTraceByTimePeriod:function(input,map,win,rec){
		var myMask = new Ext.LoadMask({
    	    msg    : '正在导入越界轨迹，请稍候...',
    	    target : win
    	});
    	myMask.show();
		var json_input=Ext.encode(input);
		Ext.Ajax.request({
	    	url:'vehicleAlert/findMarkerByVehicleNumber',
	    	params:{'json':json_input},
			method:'POST',
			timeout : 30000,
			scope:this,
			success: function(res){
				myMask.hide();
				var appendData=Ext.JSON.decode(res.responseText);
				if(appendData.status=='success'){
					map.clearOverlays();        //清除地图覆盖物 
					var markers=appendData.data.markers;
					var tracePoints=appendData.data.traceModels;
					if(markers!=null && appendData.data.traceModels.length >= 1){
						//加载grid中的数据
						this.loadMarkGrid(rec,win,markers);
						//加载markers
						this.loadMarks(map,markers);
						//加载行车轨迹
						this.loadTrace(map,tracePoints);
					}
					if(markers!=null && tracePoints.length < 1){
						//加载grid中的数据
						this.loadMarkGrid(rec,win,markers);
						//加载markers
						this.loadMarks(map,markers);
						Ext.Msg.alert('消息提示', '此车辆无历史轨迹！');
					}
					if(markers == null && tracePoints.length >= 1){
						//加载行车轨迹
						this.loadTrace(map,tracePoints);
						Ext.Msg.alert('消息提示', '此车辆绑定的地理围栏已被删除！');					
					}
					if(markers == null && tracePoints.length < 1){
						this.initAddress(map);
						Ext.Msg.alert('提示消息','此车辆无历史轨迹且绑定的地理围栏已被删除！');
					}
				}else{
					this.initAddress(map);
					Ext.Msg.alert('提示消息','调用接口失败！');					
				}
			}
	    	/*,
			failure : function() {
				myMask.hide();
				Ext.Msg.alert('Failure','Call interface error!');
			}*/
		});
	},
	changeTrace:function(field,value){
		var win=field.up('window');
		var rec=win.rec;
		var vehicleNumber=field.up('window').vehicleNumber;
	    var firstOutboundtime=field.up('window').firstOutboundtime   //年与日 时分秒
		var outboundReleasetime=field.up('window').outboundReleasetime;
		var minTime=field.up('window').minTime+" 00:00:00";   //年月日
		var maxTime=field.up('window').maxTime+" 00:00:00"; 
		var startTime;
		var endTime;
		if(Ext.Date.isEqual(new Date(Date.parse(minTime.replace(/-/g,"/"))),new Date(Date.parse(maxTime.replace(/-/g,"/"))))){
			startTime=firstOutboundtime;
			endTime=outboundReleasetime;
		}else{
			if(value>new Date(Date.parse(minTime.replace(/-/g,"/")))&&value<new Date(Date.parse(maxTime.replace(/-/g,"/")))){
				//startTime=Ext.util.Format.date(value, "Y-m-d")+" 00:00:00";
				//endTime=Ext.util.Format.date(value, "Y-m-d")+" 23:59:59";

				var month = value.getMonth() + 1;
				var date = value.getDate();
				if(month < 10){
					month = '0' + month;
				}
				if(date < 10){
					date = '0' + date;
				}
				startTime = value.getFullYear() + '-' + month + '-' + date +" 00:00:00";
				endTime = value.getFullYear() + '-' + month + '-' + date +" 23:59:59";
			}else if(Ext.Date.isEqual(new Date(Date.parse(minTime.replace(/-/g,"/"))),value)){
				startTime=firstOutboundtime;
				endTime=firstOutboundtime.split(' ')[0]+" 23:59:59";
			}else{
				startTime=outboundReleasetime.split(' ')[0]+" 00:00:00";
				endTime=outboundReleasetime;
			}
		}
		var input={
			vehicleNumber:vehicleNumber,
			startTime:startTime,
			endTime:endTime
		};
		var map=Ext.getCmp('abnormal_outMarkerAlarm').bmap;
		this.getTraceByTimePeriod(input,map,win,rec)
		
	},
	viewGeofenceMapinfo: function(rec,win){
		var timeItem=win.down('form').down('[name=startTime]');
		//var firstOutboundtime=Ext.util.Format.date(rec.get('firstOutboundtime'), "Y-m-d H:i:s");//开始时间
		//var outboundReleasetime=Ext.util.Format.date(rec.get('outboundReleasetime')!=null ? rec.get('outboundReleasetime'):new Date(), "Y-m-d H:i:s");
		/* IE浏览器Ext.util.Format.date*/
		var firstOutboundtime=rec.get('firstOutboundtime');//开始时间
		if(rec.get('outboundReleasetime') == ""){
			var today = new Date();
			var month = today.getMonth() + 1;
			var date = today.getDate();
			if(month < 10){
				month = '0' + month;
			}
			if(date < 10){
				date = '0' + date;
			}
			outboundReleasetime = today.getFullYear() + '-' + month + '-' + date;
		}else{
			outboundReleasetime = rec.get('outboundReleasetime');
		}
		//var minTime=Ext.util.Format.date(firstOutboundtime, "Y-m-d");
		//var maxTime=Ext.util.Format.date(outboundReleasetime, "Y-m-d");
		var minTime = firstOutboundtime.split(' ')[0];
		var maxTime = outboundReleasetime.split(' ')[0];
		timeItem.setMinValue(minTime);
		timeItem.setMaxValue(maxTime);
		timeItem.setValue(minTime);
		win.firstOutboundtime=firstOutboundtime;   //时分秒
		win.outboundReleasetime=outboundReleasetime;
		win.minTime=minTime;  //无时分秒
		win.maxTime=maxTime;
		win.rec=rec;
		var map=Ext.getCmp('abnormal_outMarkerAlarm').bmap;
		this.initMap(map);
    	
		var startTime;
		var endTime;
		if(!Ext.Date.isEqual(new Date(minTime),new Date(maxTime))){  //不在同一天
			startTime=firstOutboundtime;
			//endTime=Ext.util.Format.date(firstOutboundtime, "Y-m-d")+" 23:59:59";
			endTime=firstOutboundtime.split(' ')[0] + " 23:59:59";
		}else{                                                       //在同一天
			startTime=firstOutboundtime;
			endTime=outboundReleasetime;
		}
		var input={
			vehicleNumber:rec.get('vehicleNumber'),
			startTime:startTime,
//			Ext.Date.format(new Date(), 'g:i:s A')
//			endTime:Ext.util.Format.date(rec.get('outboundReleasetime')!=null?rec.get('outboundReleasetime'):new Date(), "Y-m-d H:i:s")
			endTime:endTime
		};
		this.getTraceByTimePeriod(input,map,win,rec);
	
	},
	playTrace:function(map,TPointArr,carMk){
		var index=0;
		var run=function resetMkPoint(index){
			var cur_long = TPointArr[index].longitude;
			var cur_lat = TPointArr[index].latitude;
			var cur_time = TPointArr[index].tracetime;
			var cur_speed = TPointArr[index].speed;
			var cur_position=TPointArr[index].address;
			var point=new BMap.Point(TPointArr[index].longitude,TPointArr[index].latitude);
			if(index > 0) {  
        		map.addOverlay(new BMap.Polyline([new BMap.Point(TPointArr[index-1].longitude,TPointArr[index-1].latitude), point], {strokeColor: "red", strokeWeight: 1, strokeOpacity: 1}));  
    		}
    		
    		var myGeo = new BMap.Geocoder();    
		// 根据坐标得到地址描述
			myGeo.getLocation(point, function(result){    
		 		if (result){ 
					var infoWindowx1 = new BMap.InfoWindow("<b>越界报警</b><br>经度："+cur_long+'<br />纬度：'+cur_lat+"<br />时间："+cur_time+"<br />速度："+cur_speed+'km/h'+"<br />地址："+result.address);
					carMk.openInfoWindow(infoWindowx1,map.getCenter());
			 	}
			 });
			map.centerAndZoom(point,map.getZoom()+2);
			carMk.setPosition(point);
			index++;
    		if(index < TPointArr.length) {
    			window.setTimeout(function(){
    					resetMkPoint(index);
    				},1000);
    		}else{
        		map.setCenter(point);  
        		console.dir(point);
    		}
		}
		run(1);
	},
	viewVehicle:function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);	
		var win = Ext.create('Admin.view.vehiclemgmt.vehicleInfomgmt.ViewVehicleInfo');
		var objectModel = new Ext.data.Model();
		var input = {
					"vehicleNumber":rec.get('vehicleNumber')
				};
		var json = Ext.encode(input);
    	Ext.Ajax.request({
       		url:'vehicle/findVehicleInfoByVehicleNumber',//?json='+Ext.encode(input),
            method : 'POST',
	        params:{json:json},
            //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
            success : function(response,options) {
            	var respText = Ext.util.JSON.decode(response.responseText);
    	    	var data = respText.data;
            	var retStatus = respText.status;
    			if (retStatus == 'success') {
    				//var win = Ext.widget("viewVehicleWindow");
    				objectModel.data = data;
    				win.down("form").loadRecord(objectModel);
    				win.show();
    			}
            }
	        /*,
            failure : function() {
                Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
            }*/
    	});
	},
	onExcelClick:function(){
	//所属部门
	var frmValues=this.lookupReference('searchForm').getValues();
    	
	if(''==frmValues['fromOrgId']||null==frmValues['fromOrgId']){
		frmValues['fromOrgId']='-1';
	}
	if('所有类型'==frmValues['vehicleType'] || ''==frmValues['vehicleType']||null==frmValues['vehicleType']){
		frmValues['vehicleType']='-1';
	}
	if(''==frmValues['deptId']||null==frmValues['deptId']){
		frmValues['deptId']='-1';
	}
    if(frmValues['startTime'] == ''){
        var initStartDay = new Date();
        /*查询30天前数据*/
        initStartDay = new Date(initStartDay.getTime() - 30*24*60*60*1000);
		this.lookupReference('searchForm').getForm().findField('startTime').setValue(initStartDay);
		frmValues['startTime']=this.lookupReference('searchForm').getValues().startTime + ' 00:00:00';
	}else{
		frmValues['startTime']= frmValues['startTime'] + ' 00:00:00';
	}

	if(''!=frmValues['endTime']){
		frmValues['endTime']= frmValues['endTime'] + ' 23:59:59';
	}

	if(this.lookupReference('searchForm').getForm().isValid()){
		window.location.href = 'vehicleAlert/exportOutboundAlert?json='  + Ext.encode(frmValues);
    }else{
    	Ext.Msg.alert('消息提示','请输入有效的筛选条件！');
    }
	
	},
	
	openDeptChooseWin: function(combo, event, eOpts){
     	var win = Ext.create("Admin.view.alertMgmt.outMarkerAlarm.DeptChooseWin",{
     		deptId:combo.up("form").getForm().findField("deptId").getValue()
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
     	var form = Ext.getCmp("outMarkerAlarmSearchForm").getForm();
     	form.findField("deptId").setValue(deptId);
//     	form.findField("organizationId").setDisplayField(deptName);
     	form.findField("deptName").setValue(deptName);
     	btn.up("window").close();
    },
    
    checkIsGroupNull: function(chk, newValue, oldValue, eOpts ){
     	var group = chk.up("checkboxgroup");
     	var value = group.getValue();
     	if(value.includeSelf == null && value.includeChild == null){
//     		chk.setChecked(true);
     		Ext.Msg.alert("提示信息", '本部门和子部门请至少选择一个！');
     		//setValue无法选中上次选中的，有bug
     		if(chk.boxLabel == "本部门"){
     			group.items.items[1].setValue(true);
     		}else{
     			group.items.items[0].setValue(true);
     		}
     	}
    }
});