/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.alertMgmt.overSpeedAlarm.ViewController', {
	extend : 'Ext.app.ViewController',
	alias : 'controller.alarmcontroller',
	requires : [
	        'Ext.window.MessageBox'
			],
	init: function(view) {
    },
    onBeforeLoad:function(){
    	//所属部门
    	var frmValues=this.lookupReference('searchForm').getValues();
    	
    	if(''==frmValues['fromOrgId']||null==frmValues['fromOrgId']){
    		frmValues['fromOrgId']='-1';
    	}
    	if('所有类型'==frmValues['vehicleType'] || ''==frmValues['vehicleType'] ||null==frmValues['vehicleType']){
    		frmValues['vehicleType']='-1';
    	}
    	if(''==frmValues['deptId']||null==frmValues['deptId']){
    		frmValues['deptId']='-1';
    	}

    	if(frmValues['endTime'] != ''){
			frmValues['endTime']=frmValues['endTime'] + ' 23:59:59';
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
        if(this.lookupReference('searchForm').getForm().isValid()){
	    	var page=Ext.getCmp('overSpeedAlarmPage').store.currentPage;
			var limit=Ext.getCmp('overSpeedAlarmPage').pageSize;
			frmValues.currentPage=page;
			frmValues.numPerPage=limit;
			this.getViewModel().getStore("OverSpeedResults").proxy.extraParams = {
	             "json":Ext.encode(frmValues)
	        }
        }else{
        	Ext.Msg.alert('消息提示','请输入有效的筛选条件！');
        }
    },
	onSearchClick:function(){
/*		if(window.sessionStorage.getItem("overSpeedAlarm") == '1'){
			this.lookupReference('searchForm').getForm().findField('startTime').setValue(new Date());
			this.lookupReference('searchForm').getForm().findField('endTime').setValue(new Date());
            window.sessionStorage.setItem("overSpeedAlarm", '0');
		}*/					
		var VehicleStore = this.lookupReference('overSpeedVehicleGrid').getStore();
		VehicleStore.currentPage = 1;
		this.getViewModel().getStore("OverSpeedResults").load();
	},
    //地图添加控件
    initMap: function(map){
        //添加比例尺控件
    	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
    	var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
    	map.addControl(top_left_control);        
		map.addControl(top_left_navigation);
		map.addControl(new BMap.MapTypeControl()); //添加地图类型，如卫星视图
	},
	//加载地图
    loadMapPosition:function(rec,win){
    	var map = Ext.getCmp('abnormal_overSpeedAlarm').bmap;
		this.initMap(map);
		var gpsPoint = new BMap.Point(rec.data.alertLongitude, rec.data.alertLatitude);
	
		//将gps坐标转换为百度坐标
		var convertor = new BMap.Convertor();
        var pointArr = [];
        pointArr.push(gpsPoint);
        convertor.translate(pointArr, 1, 5, function(data){
			if(data.status === 0) {
				var baiduMarker = new BMap.Marker(data.points[0]);
		        map.addOverlay(baiduMarker);
//		        var label = new BMap.Label("转换后的百度坐标（正确）",{offset:new BMap.Size(20,-10)});
//		        marker.setLabel(label); //添加百度label
//		        baiduMarker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的点
    			map.centerAndZoom(data.points[0], 14);
		     //   bm.setCenter(data.points[0]);
    			var content = "<b>超速报警</b><br>";
			 	content = content + "车牌号：" + rec.data.vehicleNumber + "<br>";
			 	content = content + "驾驶人：" + rec.data.driverName + "<br>";
			 	content = content + "手机号：" + rec.data.driverPhone + "<br>";
				content = content + "经度：" + Ext.util.Format.number(rec.data.alertLongitude, '0.000000') + "<br>";
			    content = content + "纬度：" + Ext.util.Format.number(rec.data.alertLatitude, '0.000000') + "<br>";
			    content = content + "报警时间：" + Ext.util.Format.date(rec.data.alertTime,'Y-m-d H:i:s') + "<br>";
			    content = content + "地址：" + rec.data.alertPosition + "<br>";
			    content = content + "速度：" + rec.data.alertSpeed + "km/h<br>";
				var infoWindow = new BMap.InfoWindow(content);  // 创建信息窗口对象 
				baiduMarker.addEventListener("click", function(){  
					this.openInfoWindow(infoWindow,map.getCenter()); //开启信息窗口
				 });  
				baiduMarker.openInfoWindow(infoWindow);
				
				var data = [{"vehicleNumber": rec.data.vehicleNumber,"alertType": "超速","alertTime": 
    				rec.data.alertTime,"alertSpeed":rec.data.alertSpeed,"alertLongitude":Ext.util.Format.number(rec.data.alertLongitude, '0.000000'),"alertLatitude":Ext.util.Format.number(rec.data.alertLatitude, '0.000000'),"alertPosition":rec.data.alertPosition}];
				win.getViewModel().getStore("OverSpeedResults").loadData(data);
      		}
        });
		
	/*	var marker = new BMap.Marker(point);
        map.addOverlay(marker)
        marker.setAnimation(BMAP_ANIMATION_BOUNCE);
    	map.centerAndZoom(point, 14);
    	
    	var myGeo = new BMap.Geocoder();    
		// 根据坐标得到地址描述
		myGeo.getLocation(point, function(result){    
		 	if (result){ 
				var content = "<b>超速报警</b><br>";
			 	content = content + "车牌号：" + rec.data.vehicleNumber + "<br>";
			 	content = content + "驾驶人：" + rec.data.driverName + "<br>";
			 	content = content + "手机号：" + rec.data.driverPhone + "<br>";
				content = content + "经度：" + Ext.util.Format.number(rec.data.alertLongitude, '0.000000') + "<br>";
			    content = content + "纬度：" + Ext.util.Format.number(rec.data.alertLatitude, '0.000000') + "<br>";
			    content = content + "报警时间：" + Ext.util.Format.date(rec.data.alertTime,'Y-m-d H:i:s') + "<br>";
			    content = content + "地址：" + result.address + "<br>";
			    content = content + "速度：" + rec.data.alertSpeed + "km/h<br>";
				var infoWindow = new BMap.InfoWindow(content);  // 创建信息窗口对象 
				marker.addEventListener("click", function(){  
					this.openInfoWindow(infoWindow,map.getCenter()); //开启信息窗口
				 });  
				marker.openInfoWindow(infoWindow);
				
				var data = [{"vehicleNumber": rec.data.vehicleNumber,"alertType": "超速","alertTime": 
    				rec.data.alertTime,"alertSpeed":rec.data.alertSpeed,"alertLongitude":Ext.util.Format.number(rec.data.alertLongitude, '0.000000'),"alertLatitude":Ext.util.Format.number(rec.data.alertLatitude, '0.000000'),"alertPosition":result.address}];
				win.getViewModel().getStore("OverSpeedResults").loadData(data);
		 	}
		 });*/
/*        marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的点
    	var geoc = new BMap.Geocoder(); 
    	var address='';
		geoc.getLocation(point, function(rs){
			var addComp = rs.addressComponents;
			var address=addComp.province + addComp.city + addComp.district +  addComp.street + addComp.streetNumber;
	 		var content = "<b>超速报警</b><br>";
		    content = content + "纬度：" + rec.data.longitude + "<br>";
            content = content + "经度：" + rec.data.latitude + "<br>";
            content = content + "速度：" + rec.data.realSpeed + "km/h<br>";
            content = content + "地址：" + rec.data.overSpeedPosition + "<br>";
			(function () {
	           var infoWindow = new BMap.InfoWindow(content);
	           map.openInfoWindow(infoWindow,point); //开启信息窗口
	           marker.addEventListener("click", function () {
	               this.openInfoWindow(infoWindow);
	           });
	         })();
		});*/
    },
    //打开超速异常报警窗体
    viewVehiclePosition:function(grid, rowIndex, colIndex){
    	var win = Ext.widget("overSpeedWindow");
    	var rec = grid.getStore().getAt(rowIndex);
		win.show();
		this.loadMapPosition(rec,win);
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
    	if('所有类型'==frmValues['vehicleType'] || ''==frmValues['vehicleType'] ||null==frmValues['vehicleType']){
    		frmValues['vehicleType']='-1';
    	}
    	if(''==frmValues['deptId']||null==frmValues['deptId']){
    		frmValues['deptId']='-1';
    	}

    	if(frmValues['endTime'] != ''){
			frmValues['endTime']=frmValues['endTime'] + ' 23:59:59';
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

		if(this.lookupReference('searchForm').getForm().isValid()){
    	    window.location.href = 'vehicleAlert/exportOverspeedAlert?json='  + Ext.encode(frmValues);
        }else{
        	Ext.Msg.alert('消息提示','请输入有效的筛选条件！');
        }
	},
	
	
	openDeptChooseWin: function(combo, event, eOpts){
     	var win = Ext.create("Admin.view.alertMgmt.overSpeedAlarm.DeptChooseWin",{
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
     	var form = Ext.getCmp("overSpeedAlarmSearchForm").getForm();
     	form.findField("deptId").setValue(deptId);
//     	form.findField("organizationId").setDisplayField(deptName);
     	form.findField("deptName").setValue(deptName);
     	btn.up("window").close();
     },
	
     checkIsGroupNull: function(chk, newValue, oldValue, eOpts ){
      	var group = chk.up("checkboxgroup");
      	var value = group.getValue();
      	if(value.includeSelf == null && value.includeChild == null){
//      		chk.setChecked(true);
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
