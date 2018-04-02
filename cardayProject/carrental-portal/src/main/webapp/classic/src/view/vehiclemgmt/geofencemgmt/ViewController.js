/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.geofencemgmt.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			'Admin.view.vehiclemgmt.geofencemgmt.View',
			'Admin.view.vehiclemgmt.geofencemgmt.SearchForm',
			'Admin.view.vehiclemgmt.geofencemgmt.AddGeofence',
			'Admin.view.vehiclemgmt.geofencemgmt.EditGeofence',
			'Admin.view.vehiclemgmt.geofencemgmt.ViewGeofence'
			],
	alias : 'controller.geofencemgmtcontroller',

	onBeforeLoad : function() {
		console.log('onBeforeLoad');
		var frmValues = this.lookupReference('searchForm').getValues();
		var page=Ext.getCmp('geofencePage').store.currentPage;
		var limit=Ext.getCmp('geofencePage').pageSize;
		var input = {
				"currentPage" : page,
				"numPerPage" : limit,
				"geofenceName":frmValues.geofenceName,
			};
		var pram = Ext.encode(input);
		this.getViewModel().getStore("geofenceResults").proxy.extraParams = {
			"json" : pram
		}
	},

	//根据用户的输入的地理围栏名称查询
	searchByGeofenceName :　function() {
		console.log('searchByGeofenceName');
		var VehicleStore = this.lookupReference('gridGeofence').getStore();
		VehicleStore.currentPage = 1;
		this.getViewModel().getStore("geofenceResults").load();
	},

	//打开新增地理围栏窗口
	onAddGeofenceClick : function() {
		win = Ext.widget('addGeofence');
		win.show();
		this.renderPositionInfoOnMap();
	},

	loadAddGeofenceInformation: function(){
		var map = this.lookupReference('bmappanelGeofence').bmap;
    	
    	this.initMap(map);

        function myFun(result){
			var cityName = result.name;
			console.log('initView:' + cityName);
			map.setCenter(cityName);
			map.centerAndZoom(cityName,12);
    	}
    	var myCity = new BMap.LocalCity();
      
    	myCity.get(myFun);
    	
/*    	var point = new BMap.Point(114.29821255382512, 30.589583523896508);
    	map.centerAndZoom(point, 13);*/
	},

	initMap: function(map){
        //添加比例尺控件
    	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
    	var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
    	map.addControl(top_left_control);        
		map.addControl(top_left_navigation);
	},

	citySelectLoadOnMap: function(combo , record , eOpts){
		console.log('Load Posistion On Map');
		var map = Ext.getCmp('geofencemapdis_bmappanel').bmap;
		map.centerAndZoom(record.data.regionName,11);
		if(Ext.getCmp('addGeofence')){             	
		     Ext.getCmp('addGeofence').down('form').getForm().findField('addressGeofenceId').setValue('');
		     Ext.getCmp('addGeofence').down('form').getForm().findField('addGeofenceTypeId').setValue('');
		     Ext.getCmp('addGeofence').down('form').getForm().findField('radius').clearValue();
//		     Ext.getCmp('addGeofence').down('form').getForm().findField('address').setValue('');
         }
		if(Ext.getCmp('edit_geofence_id')){
			Ext.getCmp('edit_geofence_id').down('form').getForm().findField('address').setValue('');
			Ext.getCmp('edit_geofence_id').down('form').getForm().findField('position').setValue('');
			Ext.getCmp('edit_geofence_id').down('form').getForm().findField('radius').clearValue();
		}
	},

	renderPositionInfoOnMap: function(position){		
		var map = Ext.getCmp("geofencemapdis_bmappanel").bmap;
		//建立一个自动完成的对象
	    	var ac = new BMap.Autocomplete({
	    		 				"input" : "addressGeofenceId-inputEl",
	    		 				"location" : map
							});
	    if(position != ""  && position != null){
	    	ac.setInputValue(position);
	    }

		ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
			var str = "";
			var _value = e.fromitem.value;
			var value = "";
			if (e.fromitem.index > -1) {
				value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
			}
			str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

			value = "";
			if (e.toitem.index > -1) {
				_value = e.toitem.value;
				value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
			}    
			str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
		});

		var myValue;
		ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
		var map = Ext.getCmp('geofencemapdis_bmappanel').bmap;
		var _value = e.item.value;
			myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
			
		map.clearOverlays();    //清除地图上所有覆盖物
		var local = new BMap.LocalSearch(map, { //智能搜索
		  onSearchComplete: function(){

		  	var city;
	  	    if(Ext.getCmp('addGeofence')){             	
			     city = Ext.getCmp('addGeofence').down('form').getForm().findField('city').getRawValue();
	         }

	         if(Ext.getCmp('edit_geofence_id')){             	
			    city = Ext.getCmp('edit_geofence_id').down('form').getForm().findField('cityId').getRawValue();
	         }

	         if(city != null){
	        	    console.log('local.getResults().city:' + local.getResults().city);
	        	    console.log('city:' + city);
				  	if(local.getResults().city == city){
						var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
						map.centerAndZoom(pp, 15);
			/*			var marker = new BMap.Marker(pp);
						map.addOverlay(marker);    //添加标注*/
						    map.enableScrollWheelZoom();  
					    	var overlaycomplete = function(e){
					            map.addOverlay(e.overlay);
					            
					            if($("a[drawingtype$='polygon']").hasClass("BMapLib_polygon_hover")){
					            	$("a[drawingtype$='polygon']").css("display","none");
					            }
					        };
					        var styleOptions = {
					            strokeColor:"red",    //边线颜色。
					            strokeWeight: 3,       //边线的宽度，以像素为单位。
					            strokeOpacity: 0.8,	   //边线透明度，取值范围0 - 1。
					            fillOpacity: 0.6,      //填充的透明度，取值范围0 - 1。
					            strokeStyle: 'solid' //边线的样式，solid或dashed。
					        }
					        
					        //如果是自主绘制，则实例化鼠标绘制工具 yh
					        var flag = false;
					        if(position == null){
					        	var type = Ext.getCmp("addGeofenceTypeId");
					        	if(type != null && type.value == '自主绘制'){
					        		flag = true;
					        	}
					        }else{
					        	var type = Ext.getCmp("eidtGeofenceTypeId");
					        	if(type != null && type.value == '自主绘制'){
					        		flag = true;
					        	}
					        }
					        
					        if(flag){
					        	//实例化鼠标绘制工具
					        	var drawingManager = new BMapLib.DrawingManager(map, {
					        		isOpen: false, //是否开启绘制模式
					        		enableDrawingTool: true, //是否显示工具栏
					        		drawingToolOptions: {
					        			anchor: BMAP_ANCHOR_TOP_RIGHT, //位置
					        			offset: new BMap.Size(5, 5), //偏离值
					        		},
					        		polygonOptions: styleOptions, //多边形的样式
					        	});  
					        	//添加鼠标绘制工具监听事件，用于获取绘制结果
					        	drawingManager.addEventListener('overlaycomplete', overlaycomplete);
					        	
					        	$('.BMapLib_Drawing .BMapLib_hander').css("display","none");
					        	$('.BMapLib_Drawing .BMapLib_marker').css("display","none");
					        	$('.BMapLib_Drawing .BMapLib_circle').css("display","none");
					        	$('.BMapLib_Drawing .BMapLib_polyline').css("display","none");
					        	$('.BMapLib_Drawing .BMapLib_rectangle').css("display","none");
					        }
					        
					        $('.BMap_noprint').css("margin-right","50px");
						}else{
		             	 Ext.Msg.alert('消息提示', '此位置不在 ' + city + ' 内！请重新输入！');
		             	 if(Ext.getCmp('addGeofence')){             	
						     Ext.getCmp('addGeofence').down('form').getForm().findField('addressGeofenceId').setValue('');
				         }

				         if(Ext.getCmp('edit_geofence_id')){             	
						     Ext.getCmp('edit_geofence_id').down('form').getForm().findField('addressGeofenceId').setValue('');
				         }
		             }
		        }else{
		        	Ext.Msg.alert('消息提示', '请选择城市！');
		        }
		    }
		});
		local.search(myValue);
		});
	},

	//完成新增地理围栏
	addGeofenceDone : function(btn) {
		var myMask = new Ext.LoadMask({
					    msg    : '请稍后，正在添加地理围栏........',
					    target : this.getView()
					});
        myMask.show();  

		var geofenceInfo = this.getView().down('form').getForm().getValues();
		var type,position;
		if(geofenceInfo.type == '行政区划分'){
            type = '0';
            position = Ext.getCmp('administrativeDivisionId').getRawValue();
            Ext.getCmp('addressGeofenceId').allowBlank = true;
            Ext.getCmp('administrativeDivisionId').allowBlank = false;
            Ext.getCmp('radiusGeofenceId').down("combo").allowBlank = true;
		}else if(geofenceInfo.type == '自主绘制'){
			type = '1';
            position = geofenceInfo.position[0];
            Ext.getCmp('addressGeofenceId').allowBlank = false;
            Ext.getCmp('administrativeDivisionId').allowBlank = true;
            Ext.getCmp('radiusGeofenceId').down("combo").allowBlank = true;
		}else{
			type = '2';
            position = geofenceInfo.position[0];
            Ext.getCmp('addressGeofenceId').allowBlank = false;
            Ext.getCmp('radiusGeofenceId').down("combo").allowBlank = false;
            Ext.getCmp('administrativeDivisionId').allowBlank = true;
		}
		console.log('position:' + position);
		var map = this.lookupReference('bmappanelGeofence').bmap;

		var patternList = "";
		var count = 0;

		if(map.getOverlays().length > 0){
    		
    		for(var i = 0; i < map.getOverlays().length; i++){
    			if(map.getOverlays()[i] instanceof BMap.Circle){
    				longitude = parseFloat(geofenceInfo.longitude);
					latitude = parseFloat(geofenceInfo.latitude);
    			}else if(map.getOverlays()[i] instanceof BMap.Polygon){
    				var pattern = '[[';
    				if(map.getOverlays()[i].getPath().length > 0){
    					for(var j = 0; j < map.getOverlays()[i].getPath().length; j++){
    						   pattern = pattern + '{"lng":' + map.getOverlays()[i].getPath()[j].lng + ',"lat":' +map.getOverlays()[i].getPath()[j].lat + '},';
    					}

    					 var longitude = map.getOverlays()[i].getPath()[0].lng;
    					 var latitude = map.getOverlays()[i].getPath()[0].lat;
    					 pattern = pattern + '{"lng":' + longitude + ',"lat":' + latitude + '}]]';
    				}
    				patternList = patternList + pattern + '!';
    			}else{
    				count++;
    			}
    		}
    	}

    	if(count == map.getOverlays().length){    			
				Ext.Msg.alert('消息提示','请绘制地理围栏！');
				myMask.hide();
    	}else{
    		var city =  Ext.getCmp('addGeofenceCityCmb').getRawValue();
    		var input = {
        		'markerName': geofenceInfo.markerName,
        		'position': position,
        		'city':city,
        		'cityId': geofenceInfo.city,
        		'type': type,
        		'pattern': patternList,
        		'longitude': longitude,
        		'latitude' : latitude,
        		'radius':geofenceInfo.radius
			};

		var json = Ext.encode(input);

		if (this.getView().down('form').getForm().isValid()) {
		
		   	 Ext.Ajax.request({
				url : './geofence/create',
		        method : 'POST',
		        params:{json:json},
		        timeout: 60000,
		        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
		        success : function(response) {
		        	var respText = Ext.util.JSON.decode(response.responseText);
		        	var retStatus = respText.status;
					if (retStatus == 'success') {
						btn.up('addGeofence').close();
						Ext.Msg.alert('提示信息','添加地理围栏成功');
						Ext.getCmp("gridGeofence").getStore('geofenceResults').load();
					}else{
						btn.up('addGeofence').close();
						Ext.Msg.alert('消息提示','新增地理围栏失败！');
						Ext.getCmp("gridGeofence").getStore('geofenceResults').load();
					}
		        }
//		        ,
//		        failure : function() {
//					btn.up('addGeofence').close();
//		            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//		        }
		    });
	   	 }else{
	   	 	Ext.Msg.alert('消息提示','地理围栏信息有误，请重新输入！');
	   	 	myMask.hide();
	   	 }

       }

	},

	//查看地理围栏信息
	viewGeofence : function(grid, rowIndex, colIndex) {
		var rec = grid.getStore().getAt(rowIndex);		
		var win = Ext.widget("viewGeofence");
		//console.dir(win.down("form"));
		win.down("form").loadRecord(rec);
		win.show();
		if(rec.data.type == '2'){
			//圆形显示半径
			win.down("form").getForm().findField("radius").show();
		}

		this.viewGeofenceMapinfo(rec);
		
	},

	viewGeofenceMapinfo: function(rec){
        var map = Ext.getCmp('geofencemapdis_bmappanel').bmap;
		this.initMap(map);
		if(rec.data.type == '0'){
	       	var bdary = new BMap.Boundary();
	       	var position = rec.data.city + rec.data.position;
			bdary.get(position, function(rs){       //获取行政区域
				map.clearOverlays();        //清除地图覆盖物       
				var count = rs.boundaries.length; //行政区域的点有多少个
				if (count === 0) {
					Ext.Msg.alert('消息提示', '未能获取当前输入行政区域,请重新输入！');
					return ;
				}
	          	var pointArray = [];
				for (var i = 0; i < count; i++) {
					var ply = new BMap.Polygon(rs.boundaries[i], {strokeWeight: 2, strokeColor: "#ff0000"}); //建立多边形覆盖物
					map.addOverlay(ply);  //添加覆盖物
					pointArray = pointArray.concat(ply.getPath());
				}    
				map.setViewport(pointArray);    //调整视野                
			}); 
		}else if(rec.data.type == '1'){
			var points = rec.data.pattern.replace('!','');
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
	    }else if(rec.data.type == '2'){
	    	var point = new BMap.Point(rec.data.longitude, rec.data.latitude);
	    	var radius = rec.data.radius;
            map.centerAndZoom(point, 15);
            map.addOverlay(new BMap.Marker(point));
            var circle = new BMap.Circle(point,radius * 1000,{strokeColor:"red", strokeWeight:2, strokeOpacity:0.5});
            map.addOverlay(circle);
	    }
	},

	//打开地理围栏信息修改,根据id查询地理围栏信息
	editGeofence : function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget("editGeofence");
		if(rec.data.type == '0'){			
             Ext.getCmp('addressGeofenceId').hide();
             Ext.getCmp('administrativeDivisionId').show();
		}else if(rec.data.type == '1'){
             Ext.getCmp('addressGeofenceId').show();
             Ext.getCmp('administrativeDivisionId').hide();
		}else{
			Ext.getCmp('addressGeofenceId').show();
			Ext.getCmp('radiusGeofenceId').show();
            Ext.getCmp('administrativeDivisionId').hide();
		}
		win.down("form").loadRecord(rec);
		win.down("form").form.findField('address').setValue(rec.data.position);
		win.show();
        this.renderPositionInfoOnMap(rec.data.position);
		this.viewGeofenceMapinfo(rec);
	},

    //完成地理围栏信息修改
	editGeofenceDone: function(btn){
		var myMask = new Ext.LoadMask({
			    msg    : '请稍后，正在修改地理围栏........',
			    target : this.getView()
			});
        myMask.show();  
		var geofenceInfo = this.getView().down('form').getForm().getValues();		
		var map = this.lookupReference('bmappanelGeofence').bmap;

		if(geofenceInfo.type == '0'){
            position = Ext.getCmp('administrativeDivisionId').getRawValue();
		}else{
            position = geofenceInfo.position;
		}

		var patternList = "";
		var count = 0;


		if(map.getOverlays().length > 0){
    		
    		for(var i = 0; i < map.getOverlays().length; i++){
    			if(map.getOverlays()[i] instanceof BMap.Circle){
    				longitude = parseFloat(geofenceInfo.longitude);
					latitude = parseFloat(geofenceInfo.latitude);
    			}else if(map.getOverlays()[i] instanceof BMap.Polygon){
    				var pattern = '[[';
    				if(map.getOverlays()[i].getPath().length > 0){
    					for(var j = 0; j < map.getOverlays()[i].getPath().length; j++){
    						   pattern = pattern + '{"lng":' + map.getOverlays()[i].getPath()[j].lng + ',"lat":' +map.getOverlays()[i].getPath()[j].lat + '},';
    					}

    					 var longitude = map.getOverlays()[i].getPath()[0].lng;
    					 var latitude = map.getOverlays()[i].getPath()[0].lat;
    					 pattern = pattern + '{"lng":' + longitude + ',"lat":' + latitude + '}]]';
    				}
    				patternList = patternList + pattern + '!';
    			}else{
    				count++;
    			}
    		}
    	}

    	if(count == map.getOverlays().length){    			
				Ext.Msg.alert('消息提示','请绘制地理围栏！');
	   	 	    myMask.hide();
    	}else{
    		var city =  Ext.getCmp('editGeofenceCityCmb').getRawValue();
    		var cityId =  Ext.getCmp('editGeofenceCityCmb').getValue();
    		var pos;
    		var input = {
    			'id': this.getView().down('form').getForm().findField('id').getValue(),
        		'markerName': geofenceInfo.markerName,
        		'position': position,
        		'city': city,
        		'cityId': cityId,
        		'type': geofenceInfo.type,
        		'pattern': patternList,
        		'longitude': longitude,
        		'latitude' : latitude,
        		'radius':geofenceInfo.radius
			};
    	
    	console.log('position:' + position);
    	console.log('city:' + city);
//    	console.log('city:' + geofenceInfo.city);	
		var json = Ext.encode(input);

//		if (this.getView().down('form').getForm().isValid()) {
		
		   	 Ext.Ajax.request({
		        url : './geofence/update',
		        method : 'POST',
		        params:{json:json},
		        timeout: 60000,
		        success : function(response,options) {
		        	var respText = Ext.util.JSON.decode(response.responseText);
		        	var retStatus = respText.status;
					if (retStatus == 'success') {
						btn.up('editGeofence').close();
						Ext.Msg.alert('提示信息','修改地理围栏成功');
						Ext.getCmp("gridGeofence").getStore('geofenceResults').load();
					}else{
						btn.up('editGeofence').close();
						Ext.Msg.alert('消息提示','修改地理围栏失败！');
						Ext.getCmp("gridGeofence").getStore('geofenceResults').load();
					}
		        }
//		        ,
//		        failure : function() {
//					btn.up('editGeofence').close();
//		            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//		        }
		    });
//	   	 }else{
//	   	 	Ext.Msg.alert('消息提示','地理围栏信息有误，请重新你输入！');
//	   	 	myMask.hide();
//	   	 }

       }
   },

   	//删除
	deleteGeofence : function(grid, rowIndex, colIndex) {
		Ext.Msg.confirm('消息提示','确定要删除吗！！！',function(btn){
			if (btn == 'yes') {
				var genfenceID = grid.getStore().getAt(rowIndex).id;
				var url = 'geofence/'+genfenceID+'/delete';
					
				Ext.Ajax.request({
		   		url: url,
		        method : 'POST',
		        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
		        success : function(response,options) {
					var respText = Ext.util.JSON.decode(response.responseText);
					var	retStatus = respText.status;
						if (retStatus == 'success') {
							//删除成功后，刷新页面
							Ext.getCmp("gridGeofence").getStore('geofenceResults').load();
						}
			        },
//			        failure : function() {
//			            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
//			        },
			        scope:this
				});		
			}
		});
	},

	//打开给地理围栏分配车辆窗口，显示当前地理围栏可分配车辆
	assignGeofenceVehicle:function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
    	var win = Ext.widget("assignGeofenceVehicle", {
			title: '车辆管理',
			closable: false,
			buttonAlign : 'center',
			markerId: rec.data.id,
			markerName: rec.data.markerName,
			buttons : [{
				text : '返回',
				handler: 'toBackGeofenceView'
			}]
		});
		win.show();
	},

	 loadGeofenceVehicle: function() {
 		var markerId = Ext.getCmp("assignGeofenceVehicle").markerId;
		var page=Ext.getCmp('assignGeofenceVehiclePage').store.currentPage;
		var limit=Ext.getCmp('assignGeofenceVehiclePage').pageSize;
		var input = {
				"currentPage" : page,
				"numPerPage" : limit,
				"markerId":markerId,
			};
		var pram = Ext.encode(input);
		Ext.getCmp("assignedGeofenceVehicleGrid").getViewModel().getStore("geofenceVehicleAssignedStore").proxy.extraParams = {
			"json" : pram
		};

     },

     onAfterLoadGeofenceVehicle: function(){
      	var avialiableVehicleStore = Ext.getCmp('assignGeofenceVehiclePage').store;
		avialiableVehicleStore.currentPage = 1;
		Ext.getCmp("assignedGeofenceVehicleGrid").getViewModel().getStore("geofenceVehicleAssignedStore").load();
     },

    //返回地理围栏管理页面
	toBackGeofenceView: function() {
     	console.log('to back geofence view');
     	var VehicleStore = Ext.getCmp('geofencemgmt').getViewModel().getStore('geofenceResults');
		VehicleStore.currentPage = 1;
		Ext.getCmp('geofencemgmt').getViewModel().getStore("geofenceResults").load();
     	this.view.close();
     },

     //打开可分配车辆窗口
     showAvailiableVehicleView: function(){
     	var markerId = Ext.getCmp("assignGeofenceVehicle").markerId;
     	var markerName = Ext.getCmp("assignGeofenceVehicle").markerName;

	    var input = {
    				"currentPage" : 1,
					"numPerPage" : 10,
	    	        'markerId' : markerId};
	    var json = Ext.encode(input);
     	Ext.Ajax.request({
 			url : 'geofence/findMarkerAvialiableVehicles',//?json=' + Ext.encode(input),
 			method : 'POST',
	        params:{json:json},
 			//defaultHeaders : {'Content-type' : 'application/json;utf-8'},
 			success : function(response, options) {
 				var respText = Ext.util.JSON.decode(response.responseText);
 				var totalRows = respText.data.totalRows;
 				if(totalRows == '0'){
 					var win = Ext.widget("emptyGeofenceAvialiableVehicles", {
			 			title: '车辆管理',
			 			buttonAlign : 'center',
			 			closable: false,
			 			markerId : markerId,
			 			markerName: markerName,
			 			buttons : [{
			 				text : '返回',
			 				handler: 'toAssignVehicleView'
			 			}]
			 		});
			     	Ext.getCmp("assignGeofenceVehicle").close();
			 		win.show();
 				}else{
			     	console.log('+++showAvailiableVehicleView+++markerId: ' + markerId);
			     	var win = Ext.widget("addVehicleToGeofence", {
			 			title: '车辆管理',
			 			buttonAlign : 'center',
			 			closable: false,
			 			markerId : markerId,
			 			markerName: markerName,
			 			buttons : [{
			 				text : '返回',
			 				handler: 'toAssignVehicleView'
			 			}]
			 		});
			     	Ext.getCmp("assignGeofenceVehicle").close();
			 		win.show();
 				}
 		    	
 			},
// 			failure : function() {
// 				Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
// 			},
 			scope : this
 		});
     },

      toAssignVehicleView: function() {
     	if(Ext.getCmp("addVehicleToGeofence")){
     		var markerId = Ext.getCmp("addVehicleToGeofence").markerId;
     		var markerName = Ext.getCmp("addVehicleToGeofence").markerName;
	 		/*清空存储的选中车辆list*/
	 		Ext.getCmp('addVehicleToGeofence').AllSelectedRecords.length = 0;
	        Ext.getCmp('addVehicleToGeofenceView').getSelectionModel().clearSelections();
     	}
     	if(Ext.getCmp("emptyGeofenceAvialiableVehicles")){
     		var markerId = Ext.getCmp("emptyGeofenceAvialiableVehicles").markerId;
     		var markerName = Ext.getCmp("emptyGeofenceAvialiableVehicles").markerName;
     	}
     	
     	console.log('markerId id : ' + markerId);
     	var win = Ext.widget("assignGeofenceVehicle", {
 			title: '车辆管理',
 			closable: false,
 			buttonAlign : 'center',
 			markerId: markerId,
 			markerName: markerName,
 			buttons : [{
 				text : '返回',
 				handler: 'toBackGeofenceView'
 			}]
 		});
     	this.view.close();
 		win.show();
     },

       loadGeofenceAvialiableVehicle: function() {
  		var markerId = Ext.getCmp("addVehicleToGeofence").markerId;
		var page=Ext.getCmp('addVehicleToGeofencePage').store.currentPage;
		var limit=Ext.getCmp('addVehicleToGeofencePage').pageSize;
		var input = {
				"currentPage" : page,
				"numPerPage" : limit,
				"markerId":markerId,
			};
		var pram = Ext.encode(input);
		Ext.getCmp("addVehicleToGeofenceView").getViewModel().getStore("geofenceVehicleStore").proxy.extraParams = {
			"json" : pram
		};

      },

      onAfterGeofenceAvialiableVehicle: function(){
      	var avialiableVehicleStore = Ext.getCmp('addVehicleToGeofencePage').store;
		avialiableVehicleStore.currentPage = 1;
		Ext.getCmp("addVehicleToGeofenceView").getViewModel().getStore("geofenceVehicleStore").load();
      },

     confirmAddVehicle: function() {
     	var markerId = Ext.getCmp("addVehicleToGeofence").markerId;
     	console.log('to confirm add vehicle to geofence');
     	var gridPanel = Ext.getCmp('addVehicleToGeofenceView');
 		var record = Ext.getCmp('addVehicleToGeofence').AllSelectedRecords;
 		console.log('length:' + record.length);
 		if(gridPanel.getViewModel().getStore('geofenceVehicleStore').totalCount == 0) {
 			Ext.Msg.alert('消息提示', '无车辆可选择');
 			return;
 		}
 		if(record.length == 0) {
 			Ext.Msg.alert('消息提示', '请选择车辆');
 			return;
 		}
 		var vehicleIds = '';
 		for (var i=0; i<record.length; i++) {
 			vehicleIds += record[i].data.id + ',';
 		}
 		console.log('markerId:' + markerId);
 		vehicleIds = vehicleIds.substr(0,vehicleIds.length-1);
 		console.log('vehicleIds:' + vehicleIds);
 		
 		var input = {
 			'markerId' : markerId,
 			'vehicleIds' : vehicleIds
 		};
 		var json = Ext.encode(input);
 		Ext.Ajax.request({
 			url : 'geofence/assignVehicles',//?json= + Ext.encode(input),
 			method : 'POST',
	        params:{json:json},
 			//defaultHeaders : {'Content-type' : 'application/json;utf-8'},
 			success : function(response, options) {
 				var respText = Ext.util.JSON.decode(response.responseText);
 				var status = respText.status;
 				var input = {
 			 			'markerId' : Ext.getCmp("addVehicleToGeofence").markerId
 			 		};
 				var win = Ext.widget("assignGeofenceVehicle", {
 					title: '车辆管理',
 					closable: false,
 					buttonAlign : 'center',
 					markerId: Ext.getCmp("addVehicleToGeofence").markerId,
 					markerName: Ext.getCmp("addVehicleToGeofence").markerName,
 					buttons : [{
 						text : '返回',
 						handler: 'toBackGeofenceView'
 					}]
 				});
 		    	if(status == 'success') {
 		    		Ext.Msg.alert('消息提示', '添加成功', function(text) {
 		    			/*清空存储的选中车辆list*/
 						Ext.getCmp('addVehicleToGeofence').AllSelectedRecords.length = 0;
	        			Ext.getCmp('addVehicleToGeofenceView').getSelectionModel().clearSelections();
                       	Ext.getCmp("addVehicleToGeofence").close();
 		    		    win.show();
                    });
 		    	}
 			},
// 			failure : function() {
// 				Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
// 			},
 			scope : this
 		});
     },

     	//移除已分配车辆
	unassignVehicle: function(grid, rowIndex, colIndex) {
     	var rec = grid.getStore().getAt(rowIndex);
     	var markerId = Ext.getCmp("assignGeofenceVehicle").markerId;
     	var markerName = Ext.getCmp("assignGeofenceVehicle").markerName;
     	var msg = '是否确认将车辆 ' + rec.data.vehicleNumber + ' 从地理围栏 ' +  markerName + ' 移除 ?';
     	console.log('+++unassignGeofenceVehicle+++stationId: ' + markerId);
     	Ext.Msg.confirm('消息提示', msg, function(btn){
     		if (btn == 'yes') {
     			console.log('markerId:' + markerId + '; id' + rec.data.id);
     			//调用部门移除接口 start
     			var input = {'markerId' : markerId,
     					     'vehicleId': rec.data.id};
     			var json = Ext.encode(input);
             	Ext.Ajax.request({
         			url : 'geofence/unassignVehicles',//?json=' + Ext.encode(input),
         			method : 'POST',
	        		params:{json:json},
         			//defaultHeaders : {'Content-type' : 'application/json;utf-8'},
         			success : function(response, options) {
         				var respText = Ext.util.JSON.decode(response.responseText);
         				var status = respText.status;
         		    	console.log('length:' + status);
         		    	if(status == 'success') {
         		    		Ext.Msg.alert('消息提示', '移除成功');
					      	var avialiableVehicleStore = Ext.getCmp('assignGeofenceVehiclePage').store;
							avialiableVehicleStore.currentPage = 1;
							Ext.getCmp("assignedGeofenceVehicleGrid").getViewModel().getStore("geofenceVehicleAssignedStore").load();
         		    	}
         			},
//         			failure : function() {
//         				Ext.Msg.alert('消息提示', '服务器繁忙，请稍后再试！');
//         			},
         			scope : this
         		});
     			//调用已分配车辆移除接口 end
     		}
     	});
     },

    typeSelectLoadMarker: function(combo , record , eOpts){
		console.log('Type select to load marker On Map');
		if(record.data.id == '0'){	//行政区域
             Ext.getCmp('addressGeofenceId').hide();
             Ext.getCmp('administrativeDivisionId').show();
             Ext.getCmp('radiusGeofenceId').hide();
             Ext.getCmp('radiusGeofenceId').down("combo").clearValue();
             if(Ext.getCmp('addGeofence')){             	
    		     Ext.getCmp('administrativeDivisionId').setValue('');
    		     Ext.getCmp('addressGeofenceId').setValue('');
             }
		}else if(record.data.id == '1'){	//多边形
             Ext.getCmp('addressGeofenceId').show();
             Ext.getCmp('administrativeDivisionId').hide();
             Ext.getCmp('radiusGeofenceId').hide();
             Ext.getCmp('addressGeofenceId').setValue('');
             Ext.getCmp('radiusGeofenceId').down("combo").clearValue();
		}else{	//圆形
			 Ext.getCmp('addressGeofenceId').show();
			 Ext.getCmp('radiusGeofenceId').show();
             Ext.getCmp('administrativeDivisionId').hide();
             Ext.getCmp('addressGeofenceId').setValue('');
             Ext.getCmp('radiusGeofenceId').down("combo").clearValue();
		}
	},

	administrativeDivisionBlur: function(combo , record , eOpts) {
		console.log('me.getValue():' + record.data.regionName);
//        var administrativeDivision = record.data.regionName;
		var city;
		if(Ext.getCmp('addGeofence')) {
			city = Ext.getCmp('addGeofenceCityCmb').getRawValue();
		}
		if(Ext.getCmp('edit_geofence_id')) {
			city = Ext.getCmp('editGeofenceCityCmb').getRawValue();
		}
        var administrativeDivision = city + record.data.regionName;
        console.log('administrativeDivision:' + administrativeDivision);
		var map = Ext.getCmp('geofencemapdis_bmappanel').bmap;
       	var bdary = new BMap.Boundary();
		bdary.get(administrativeDivision, function(rs){       //获取行政区域
			map.clearOverlays();        //清除地图覆盖物       
			var count = rs.boundaries.length; //行政区域的点有多少个
			if (count === 0) {
				Ext.Msg.alert('消息提示', '未能获取当前输入行政区域,请重新输入！');
				return ;
			}
          	var pointArray = [];
			for (var i = 0; i < count; i++) {
				var ply = new BMap.Polygon(rs.boundaries[i], {strokeWeight: 2, strokeColor: "#ff0000"}); //建立多边形覆盖物
				map.addOverlay(ply);  //添加覆盖物
				pointArray = pointArray.concat(ply.getPath());
			}    
			map.setViewport(pointArray);    //调整视野                
		}); 
	},

	checkVehSelect: function (me, record, index, opts) {
        Ext.getCmp('addVehicleToGeofence').AllSelectedRecords.push(record);
    },

    checkVehdeSelect: function (me, record, index, opts) {
	    Ext.getCmp('addVehicleToGeofence').AllSelectedRecords = Ext.Array.filter(Ext.getCmp('addVehicleToGeofence').AllSelectedRecords, function (item) {
	        return item.get("id") != record.get("id");
	    });
	},
	loadCheckAvialiableVehicle: function (me, records, success, opts) {
        if (!success || !records || records.length == 0)
            return;

        //根据全局的选择，初始化选中的列
        var selModel = Ext.getCmp('addVehicleToGeofenceView').getSelectionModel();
        Ext.Array.each(Ext.getCmp('addVehicleToGeofence').AllSelectedRecords, function () {
            for (var i = 0; i < records.length; i++) {
                var record = records[i];
                if (record.get("id") == this.get("id")) {
                    selModel.select(record, true, true);    //选中record，并且保持现有的选择，不触发选中事件
                }
            }
        });
    },
    provinceSelectLoadOnMap : function(combo , record , eOpts){
		console.log('Load Posistion On Map');
		var map = Ext.getCmp('geofencemapdis_bmappanel').bmap;
		map.centerAndZoom(record.data.regionName,11);
		if(Ext.getCmp('edit_geofence_id')){             	
		     Ext.getCmp('edit_geofence_id').down('form').getForm().findField('city').setValue('');
		     Ext.getCmp('edit_geofence_id').down('form').getForm().findField('address').setValue('');
		     Ext.getCmp('edit_geofence_id').down('form').getForm().findField('position').setValue('');
		     Ext.getCmp('edit_geofence_id').down('form').getForm().findField('radius').clearValue();
		}

         if(Ext.getCmp('addGeofence')){             	
		     Ext.getCmp('addGeofence').down('form').getForm().findField('city').setValue('');
		     Ext.getCmp('addGeofence').down('form').getForm().findField('radius').clearValue();
		     Ext.getCmp('addGeofence').down('form').getForm().findField('position').setValue('');
         }
		
	},
	loadProvinceComStore : function(){
		console.log('+++loadProvinceComStore++++');
		var url= 'area/queryAreaInfo';
		Ext.Ajax.request({
		method:'GET',
       	url:url,
       	defaultHeaders : {'Content-type' : 'application/json;utf-8'},
			success: function(res){
				var appendData=Ext.util.JSON.decode(res.responseText);
				if(appendData.status=='success'){
				    Ext.getCmp("editGeofenceProvinceCmb").setStore(appendData);
				}else{
					Ext.Msg.alert("提示信息", appendData.error);
				}
			 }
       	/*,
			failure : function() {
				Ext.Msg.alert('Failure','Call interface error!');
			}*/
       });
		console.log('+++loadProvinceComStore++++end');
	},
	getCityByprovince: function(obj,newValue,oldValue){
		  var input={
				  parentId:newValue
				};
		  var json=Ext.encode(input);
		  Ext.Ajax.request({
			method:'GET',
      	url:'area/queryAreaInfo',
      	params:{json:json},
      	defaultHeaders : {'Content-type' : 'application/json;utf-8'},
			success: function(res){
				var appendData=Ext.util.JSON.decode(res.responseText);
				if(appendData.status=='success'){
				    Ext.getCmp("editGeofenceCityCmb").setStore(appendData);
				    console.log('newValue:' + newValue);
				    console.log('oldValue:' + oldValue);
				    if(oldValue!=null) {
				    	Ext.getCmp('editGeofenceCityCmb').setValue(appendData.data[0].regionId); 
				    }
//				    if(appendData.data.length>0){
//				    	 Ext.getCmp('editGeofenceCityCmb').setValue(appendData.data[0].regionId); 
//				    }
				   
				}else{
					Ext.Msg.alert("提示信息", appendData.error);
				}
			 }
      	/*,
			failure : function() {
				Ext.Msg.alert('Failure','Call interface error!');
			}*/
      });
	},
	getpositionAddressStore : function(obj,newValue,oldValue) {
		 console.log('getpositionAddressStore:' + newValue);
		  var input={
				  parentId:newValue
				};
		  var json=Ext.encode(input);
		  Ext.Ajax.request({
			method:'GET',
    	url:'area/queryAreaInfo/',
    	params:{json:json},
    	defaultHeaders : {'Content-type' : 'application/json;utf-8'},
			success: function(res){
				var appendData=Ext.util.JSON.decode(res.responseText);
				if(appendData.status=='success'){
				    Ext.getCmp("administrativeDivisionId").setStore(appendData);
				    /*if(appendData.data.length>0){
				    	 Ext.getCmp('editGeofenceCityCmb').setValue(appendData.data[0].regionId); 
				    }*/
				   
				}else{
					Ext.Msg.alert("提示信息", appendData.error);
				}
			 }
    	/*,
			failure : function() {
				Ext.Msg.alert('Failure','Call interface error!');
			}*/
    });
	},
	
	//选择圆形围栏半径后，生成围栏  yh
	 radiusSelectLoadOnMap: function(combo , record , eOpts){
	        console.log('Load Circle On Geofence Map');
	        var map = Ext.getCmp('geofencemapdis_bmappanel').bmap;
	        map.clearOverlays();

	        var position = Ext.getCmp('addressGeofenceId').getValue();

	        if(position == '' || position == null){
	            Ext.Msg.alert('消息提示', '位置不能为空！');
	        }else{
	            var local = new BMap.LocalSearch(map, { //智能搜索
	                onSearchComplete: function(){
	                    if(local.getResults().getPoi(0) == undefined){
	                        Ext.Msg.alert('消息提示','无法此解析地址，请重新输入');
	                        if(Ext.getCmp('addGeofence')){
	                            Ext.getCmp('addGeofence').down('form').getForm().findField('addressGeofenceId').setValue('');
	                            Ext.getCmp('addGeofence').down('form').getForm().findField('radius').setValue('');
	                        }

	                        if(Ext.getCmp('edit_geofence_id')){
	                            Ext.getCmp('edit_geofence_id').down('form').getForm().findField('addressGeofenceId').setValue('');
	                            Ext.getCmp('edit_geofence_id').down('form').getForm().findField('radius').setValue('');
	                        }
	                    }else{
	                        var pp = local.getResults().getPoi(0).point;
	                        map.centerAndZoom(pp, 15);
	                        map.addOverlay(new BMap.Marker(pp));
	                        if(Ext.getCmp('addGeofence')){
	                            Ext.getCmp('addGeofence').down('form').getForm().findField('longitude').setValue(pp.lng);
	                            Ext.getCmp('addGeofence').down('form').getForm().findField('latitude').setValue(pp.lat);
	                        }

	                        if(Ext.getCmp('edit_geofence_id')){
	                            Ext.getCmp('edit_geofence_id').down('form').getForm().findField('longitude').setValue(pp.lng);
	                            Ext.getCmp('edit_geofence_id').down('form').getForm().findField('latitude').setValue(pp.lat);
	                        }
	                        var circle = new BMap.Circle(pp,record.data.value * 1000,{strokeColor:"red", strokeWeight:2, strokeOpacity:0.5});
	                        map.addOverlay(circle);
	                    }
	                }
	            });
	            local.search(position);
	        }
	    },
});

	function onEmptyforVehGeofenceMag(){
		console.log(' geofence click');
		var main = Ext.getCmp('main').getController();
		main.redirectTo('vehicleInfoMgmt');
		Ext.getCmp("emptyGeofenceAvialiableVehicles").close();
	}
