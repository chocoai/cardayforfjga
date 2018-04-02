/**
 * This class is the template view for the application.
 */
function GetDateStr(AddDayCount) {

	var dd = new Date();
	dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
	var y = dd.getFullYear();
	var m = dd.getMonth()+1;//获取当前月份的日期
	var d = dd.getDate();
	return y+"-"+m+"-"+d;
} 
Ext.define('Admin.view.alertMgmt.backStationAlarm.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			'Admin.view.alertMgmt.backStationAlarm.View',
			'Admin.view.alertMgmt.backStationAlarm.SearchForm'
			],
	alias : 'controller.backStationMgmtController',
	
	

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
            /*查询1天前数据*/
            initStartDay = new Date(initStartDay.getTime() - 1*24*60*60*1000);
    		this.lookupReference('searchForm').getForm().findField('startTime').setValue(initStartDay);
    		frmValues['startTime']=this.lookupReference('searchForm').getValues().startTime + ' 00:00:00';
		}else{
			frmValues['startTime']= frmValues['startTime'] + ' 00:00:00';
		}

		if(frmValues['endTime'] == ''){
            var initStartDay = new Date();
            /*查询1天前数据*/
            initStartDay = new Date(initStartDay.getTime() - 1*24*60*60*1000);
    		this.lookupReference('searchForm').getForm().findField('endTime').setValue(initStartDay);
    		frmValues['endTime']=this.lookupReference('searchForm').getValues().endTime + ' 23:59:59';
		}else{
			frmValues['endTime']= frmValues['endTime'] + ' 23:59:59';
		}

    	if(this.lookupReference('searchForm').getForm().isValid()){
	    	var page=Ext.getCmp('backStationPage').store.currentPage;
			var limit=Ext.getCmp('backStationPage').pageSize;
			frmValues.currentPage=page;
			frmValues.numPerPage=limit;
			this.getViewModel().getStore("backStationResults").proxy.extraParams = {
	             "json":Ext.encode(frmValues)
	        }
	    }else{
        	Ext.Msg.alert('消息提示','请输入有效的筛选条件！');
        }
    },
	onSearchClick:function(){
		var VehicleStore = this.lookupReference('backStationVehicleGrid').getStore();
		VehicleStore.currentPage = 1;
		this.getViewModel().getStore("backStationResults").load();
	},
	
	//查看marker和轨迹
	viewBackStation:function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);	
		var win = Ext.widget("stationAndTrackWindow");
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
	viewGeofenceMapinfo: function(rec,win){
        var map = Ext.getCmp('abnormal_backStationAlarm').bmap;
		this.initMap(map);
		var input={
			vehicleNumber:rec.get('vehicleNumber'),
			latitude:rec.get('alertLatitude'),
			longitude:rec.get('alertLongitude')
		};
	   var me =this;

	   var json = Ext.encode(input);

	   Ext.Ajax.request({
	    	url:'vehicleAlert/findStationByVehicleNumber',//?json='+Ext.encode(input),
			method:'POST',
	        params:{json:json},
			scope: this,
			success: function(res){
				var appendData=Ext.JSON.decode(res.responseText);
				if(appendData.status=='success'){
					var tData=new Array();
					var stationArrays=appendData.data.stationModels;
					var traceLatitude=appendData.data.latitude;
					var traceLongitude=appendData.data.longitude;
					if(stationArrays.length!=0){
						var disArray=new Array();
						var trace=new BMap.Point(traceLongitude, traceLatitude);
						for(var i=0;i<stationArrays.length;i++){
							var point = new BMap.Point(stationArrays[i].longitude, stationArrays[i].latitude);
					//		alert('从大渡口区到江北区的距离是：'+(map.getDistance(pointA,pointB)).toFixed(2)+' 米。');  //获取两点距离,保留小数点后两位
							var distance=map.getDistance(trace,point)/1000;
							var distance2=Ext.util.Format.number(distance,'0.00');
							disArray.push(point);
							tData.push({"vehicleNumber": rec.data.vehicleNumber,"distance":distance2,"alertType": "回车","alertTime":rec.data.alertTime,"alertSpeed":(rec.data.alertSpeed!=null?rec.data.alertSpeed:0),"alertLongitude":Ext.util.Format.number(rec.data.alertLongitude, '0.000000'),"alertLatitude":Ext.util.Format.number(rec.data.alertLatitude, '0.000000'),"city":rec.data.alertCity,"alertPosition":rec.data.alertPosition,"stationName":stationArrays[i].stationName})
							var marker = new BMap.Marker(point);
	       				    var circle = new BMap.Circle(point,stationArrays[i].radius * 1000,{strokeColor:"blue", strokeWeight:2, strokeOpacity:0.9,fillOpacity:0.5});
	       					map.addOverlay(marker);
	       					map.addOverlay(circle);
	       					var content = "<b>站点详细信息:</b><br>";
							 	content = content + "站点名称：" +stationArrays[i].stationName + "<br>";
							 	content = content + "半径：" + stationArrays[i].radius+'km' + "<br>";
								content = content + "经度：" + Ext.util.Format.number(stationArrays[i].longitude, '0.000000') + "<br>";
							    content = content + "纬度：" + Ext.util.Format.number(stationArrays[i].latitude, '0.000000') + "<br>";
							me.addClickHandler(content,marker);
						}		
						win.getViewModel().getStore("backStationResults").loadData(tData);
						
						
						var myIcon = new BMap.Icon("resources/images/icons/mappin/Map-Marker-alarm-icon.png", new BMap.Size(60,40),{    //小车图片
									imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
			 				});
			 			var marker2 = new BMap.Marker(trace,{icon:myIcon});  // 创建标注
		 				disArray.push(trace);
		 				map.addOverlay(marker2);
		 				map.setViewport(disArray);
						var content2 = "<b>回车报警</b><br>";
					 	content2 = content2 + "车牌号：" + rec.data.vehicleNumber + "<br>";
					 	content2 = content2 + "驾驶人：" + rec.data.driverName + "<br>";
					 	content2 = content2 + "手机号：" + rec.data.driverPhone + "<br>";
						content2 = content2 + "经度：" + Ext.util.Format.number(rec.data.alertLongitude, '0.000000') + "<br>";
					    content2 = content2 + "纬度：" + Ext.util.Format.number(rec.data.alertLatitude, '0.000000') + "<br>";
					    content2 = content2 + "报警时间：" + Ext.util.Format.date(rec.data.alertTime,'Y-m-d H:i:s') + "<br>";
					    content2 = content2 + "地址：" + rec.data.alertPosition + "<br>";
					    content2 = content2 + "速度：" + (rec.data.alertSpeed!=null?rec.data.alertSpeed:0) + "km/h<br>";
						var infoWindow2 = new BMap.InfoWindow(content2);  // 创建信息窗口对象 
						marker2.addEventListener("click", function(){  
							this.openInfoWindow(infoWindow2,trace); //开启信息窗口
						});
						marker2.openInfoWindow(infoWindow2,trace); //开启信息窗口
					}else{
						function myFun(result){
							var cityName = result.name;
							console.log('initView:' + cityName);
							map.setCenter(cityName);
							map.centerAndZoom(cityName,12);
						 }
						var myCity = new BMap.LocalCity();
						myCity.get(myFun);
						Ext.Msg.alert('提示消息','该车辆的站点已被删除');
					}
				}
			}
	       /* ,
			failure : function() {
				Ext.Msg.alert('Failure','Call interface error!');
			}*/
		});		
	},
	
	addClickHandler: function(content,marker){
		var map = Ext.getCmp('abnormal_backStationAlarm').bmap;
		marker.addEventListener("click",function(e){
			var p = marker.getPosition();
			var point = new BMap.Point(p.lng, p.lat);
			var infoWindow = new BMap.InfoWindow(content);  // 创建信息窗口对象 
			marker.openInfoWindow(infoWindow,point); //开启信息窗口
		});
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
        initStartDay = new Date(initStartDay.getTime() - 1*24*60*60*1000);
		this.lookupReference('searchForm').getForm().findField('startTime').setValue(initStartDay);
		frmValues['startTime']=this.lookupReference('searchForm').getValues().startTime + ' 00:00:00';
	}else{
		frmValues['startTime']= frmValues['startTime'] + ' 00:00:00';
	}

	if(frmValues['endTime'] == ''){
        var initStartDay = new Date();
        /*查询1天前数据*/
        initStartDay = new Date(initStartDay.getTime() - 1*24*60*60*1000);
		this.lookupReference('searchForm').getForm().findField('endTime').setValue(initStartDay);
		frmValues['endTime']=this.lookupReference('searchForm').getValues().endTime + ' 23:59:59';
	}else{
		frmValues['endTime']= frmValues['endTime'] + ' 23:59:59';
	}

	if(this.lookupReference('searchForm').getForm().isValid()){
		window.location.href = 'vehicleAlert/exportVehiclebackAlert?json='  + Ext.encode(frmValues);
    }else{
    	Ext.Msg.alert('消息提示','请输入有效的筛选条件！');
    }
	
	},
	
	openDeptChooseWin: function(combo, event, eOpts){
     	var win = Ext.create("Admin.view.alertMgmt.backStationAlarm.DeptChooseWin",{
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
     	var form = Ext.getCmp("backStationAlarmSearchForm").getForm();
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