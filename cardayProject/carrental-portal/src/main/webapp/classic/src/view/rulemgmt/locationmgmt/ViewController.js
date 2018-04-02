/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.rulemgmt.locationmgmt.ViewController', {
	extend : 'Ext.app.ViewController',
	requires : [
	        'Ext.window.MessageBox',
			'Admin.view.rulemgmt.locationmgmt.View',
			'Admin.view.rulemgmt.locationmgmt.SearchForm',
			'Admin.view.rulemgmt.locationmgmt.AddLocation',
			'Admin.view.rulemgmt.locationmgmt.EditLocation',
			'Admin.view.rulemgmt.locationmgmt.ViewLocation'
			],
	alias : 'controller.locationmgmtcontroller',

	onBeforeLoad : function() {
		console.log('onBeforeLoad');
		var frmValues = this.lookupReference('searchForm').getValues();
		var page=Ext.getCmp('locationPage').store.currentPage;
		var limit=Ext.getCmp('locationPage').pageSize;
		var input = {
				"currentPage" : page,
				"numPerPage" : limit,
				"locationName":frmValues.locationName,
			};
		var pram = Ext.encode(input);
		this.getViewModel().getStore("locationResults").proxy.extraParams = {
			"json" : pram
		}
	},

	//根据用户的输入的名称查询
	searchByLocationName :　function() {
		console.log('searchByLocationName');
		var LocationStore = this.lookupReference('gridLocation').getStore();
		LocationStore.currentPage = 1;
		this.getViewModel().getStore("locationResults").load();
	},

	//打开新增用车位置窗口
	onAddLocationClick : function() {
		win = Ext.widget('addLocation');
		win.show();
		this.renderPositionInfoOnMap();
	},

	renderPositionInfoOnMap: function(position){		
		var map = Ext.getCmp("locationmapdis_bmappanel").bmap;
		//建立一个自动完成的对象
	    	var ac = new BMap.Autocomplete({
	    		 				"input" : "positionLocationId-inputEl",
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
		var map = Ext.getCmp('locationmapdis_bmappanel').bmap;
		var _value = e.item.value;
			myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
			
		map.clearOverlays();    //清除地图上所有覆盖物
		var local = new BMap.LocalSearch(map, { //智能搜索
		  onSearchComplete: function(){

		  	var city;
	  	    if(Ext.getCmp('addLocation')){             	
			     city = Ext.getCmp('addLocation').down('form').getForm().findField('city').getValue();
	         }

	         if(Ext.getCmp('edit_location_id')){             	
			    city = Ext.getCmp('edit_location_id').down('form').getForm().findField('city').getValue();
	         }

	         if(city != null){

				  	if(local.getResults().city == city){             

							var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
							map.centerAndZoom(pp, 15);
							var marker = new BMap.Marker(pp);
							map.addOverlay(marker);    //添加标注

				            var content = "<table>";
				            content = content + "<tr class=\"window-table\"><td>城市：" + local.getResults().getPoi(0).city + "</td></tr>";
				            content = content + "<tr class=\"window-table\"><td>地址：" + local.getResults().getPoi(0).address + "</td></tr>";
				            content = content + "<tr class=\"window-table\"><td>纬度：" + local.getResults().getPoi(0).point.lat + "</td></tr>";
				            content = content + "<tr class=\"window-table\"><td>经度：" + local.getResults().getPoi(0).point.lng + "</td></tr>";
				            content += "</table>";

				             (function () {
				               var infoWindow = new BMap.InfoWindow(content);
				               marker.addEventListener("click", function () {
				                   this.openInfoWindow(infoWindow);
				               });
				             })();

				             
				             if(Ext.getCmp('addLocation')){
				             	Ext.getCmp('addLocation').down('form').getForm().findField('radius').setValue('');
				             }

				              if(Ext.getCmp('edit_location_id')){
				             	Ext.getCmp('edit_location_id').down('form').getForm().findField('radius').setValue('');
				             }
						}else{
			             	 Ext.Msg.alert('消息提示', '此位置不在 ' + city + ' 内！请重新输入！');
			             	 if(Ext.getCmp('addLocation')){             	
							     Ext.getCmp('addLocation').down('form').getForm().findField('positionLocationId').setValue('');
				             	 Ext.getCmp('addLocation').down('form').getForm().findField('radius').setValue('');
					         }

					         if(Ext.getCmp('edit_location_id')){             	
							     Ext.getCmp('edit_location_id').down('form').getForm().findField('positionLocationId').setValue('');
				             	 Ext.getCmp('edit_location_id').down('form').getForm().findField('radius').setValue('');
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

	loadAddLocationInformation: function(){
		var map = this.lookupReference('bmappanelLocation').bmap;
    	
    	this.initMap(map);

    	function myFun(result){
			var cityName = result.name;
			console.log('initView:' + cityName);
			map.setCenter(cityName);
			map.centerAndZoom(cityName,12);
    	}
    	var myCity = new BMap.LocalCity();
      
    	myCity.get(myFun);
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
		var map = Ext.getCmp('locationmapdis_bmappanel').bmap;
		map.centerAndZoom(record.data.value,11);
		if(Ext.getCmp('addLocation')){
		     Ext.getCmp('addLocation').down('form').getForm().findField('positionLocationId').setValue('');
         }

         if(Ext.getCmp('edit_location_id')){
		     Ext.getCmp('edit_location_id').down('form').getForm().findField('positionLocationId').setValue('');
         }
	},

	radiusSelectLoadOnMap: function(combo , record , eOpts){
		console.log('Load Circle On Map');
		var map = Ext.getCmp('locationmapdis_bmappanel').bmap;
		map.clearOverlays();

		var position = Ext.getCmp('positionLocationId').getValue();

		if(position == '' || position == null){
			Ext.Msg.alert('消息提示', '位置不能为空！');
		}else{
			var local = new BMap.LocalSearch(map, { //智能搜索
			  onSearchComplete: function(){
			  	    var pp = local.getResults().getPoi(0).point;
					map.addOverlay(new BMap.Marker(pp));
					if(Ext.getCmp('addLocation')){             	
					     Ext.getCmp('addLocation').down('form').getForm().findField('longitude').setValue(pp.lng);
					     Ext.getCmp('addLocation').down('form').getForm().findField('latitude').setValue(pp.lat);
			         }

			         if(Ext.getCmp('edit_location_id')){             	
					     Ext.getCmp('edit_location_id').down('form').getForm().findField('longitude').setValue(pp.lng);
					     Ext.getCmp('edit_location_id').down('form').getForm().findField('latitude').setValue(pp.lat);
			         }
			        var circle = new BMap.Circle(pp,record.data.value * 1000,{strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});
			        map.addOverlay(circle);
				}
			});
			local.search(position);		
		}
	},

	//完成新增用车位置
	addLocationDone : function(btn) {
		var myMask = new Ext.LoadMask({
					    msg    : '请稍后，正在添加位置信息........',
					    target : this.getView()
					});
        myMask.show();  

		var locationInfo = this.getView().down('form').getForm().getValues();
		var input = {
        		'organizationId': locationInfo.organizationId,
        		'locationName': locationInfo.locationName,
        		'city': locationInfo.city,
        		'position': locationInfo.position,
        		'longitude': locationInfo.longitude,
        		'latitude': locationInfo.latitude,
        		'radius': locationInfo.radius,
			};
		var json = Ext.encode(input);
		if (this.getView().down('form').getForm().isValid()) {
		
		   	 Ext.Ajax.request({
				url : 'rule/create',//?json='+ Ext.encode(input),
		        method : 'POST',
        		params:{json:json},
		        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
		        success : function(response,options) {
		        	var respText = Ext.util.JSON.decode(response.responseText);
		        	var retStatus = respText.status;
		        	var data = respText.data;
					if (retStatus == 'success' && data != "") {
						btn.up('addLocation').close();
						Ext.Msg.alert('提示信息','添加用车位置成功');
						Ext.getCmp("gridLocation").getStore('locationResults').load();
					}else{
						btn.up('addLocation').close();
						Ext.Msg.alert('消息提示','新增用车位置失败！');
						Ext.getCmp("gridLocation").getStore('locationResults').load();
					}
		        }
        		/*,
		        failure : function() {
					btn.up('addLocation').close();
		            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
		        }*/
		    });
	   	 }

	},

	//查看位置信息
	viewLocation : function(grid, rowIndex, colIndex) {
		var rec = grid.getStore().getAt(rowIndex);		
		var win = Ext.widget("viewLocation");
		win.down("form").loadRecord(rec);
		Ext.getCmp('radiusLocation').setValue(rec.data.radius + '公里');
		win.show();

		this.viewLocationMapinfo(rec);
		
	},

	viewLocationMapinfo: function(rec){
        var map = Ext.getCmp('locationmapdis_bmappanel').bmap;
		this.initMap(map);
		var point = new BMap.Point(rec.data.longitude, rec.data.latitude);
    	map.centerAndZoom(point, 15);
		var marker = new BMap.Marker(point);
        var circle = new BMap.Circle(point,rec.data.radius * 1000,{strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});
        map.addOverlay(marker);
        map.addOverlay(circle);

        var content = "<table>";
            content = content + "<tr class=\"window-table\"><td>名称：" + rec.data.locationName + "</td></tr>";
            content = content + "<tr class=\"window-table\"><td>城市：" + rec.data.city + "</td></tr>";
            content = content + "<tr class=\"window-table\"><td>地址：" + rec.data.position + "</td></tr>";
            content = content + "<tr class=\"window-table\"><td>纬度：" + rec.data.longitude + "</td></tr>";
            content = content + "<tr class=\"window-table\"><td>经度：" + rec.data.latitude + "</td></tr>";
            content += "</table>";

         (function () {
           var infoWindow = new BMap.InfoWindow(content);
           marker.addEventListener("click", function () {
               this.openInfoWindow(infoWindow);
           });
         })();
	},

	//打开用车位置信息修改,根据id查询信息
	editLocation : function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var win = Ext.widget("editLocation");
		win.down("form").loadRecord(rec);
		win.show();		
		this.renderPositionInfoOnMap(rec.data.position);
		this.viewLocationMapinfo(rec);
	},

    //完成用车位置信息修改
	editLocationDone: function(btn){
		var myMask = new Ext.LoadMask({
			    msg    : '请稍后，正在修改用车位置........',
			    target : this.getView()
			});
        myMask.show();  
		var locationInfo = this.getView().down('form').getForm().getValues();
		var input = {
				'id': locationInfo.id,
        		'organizationId': locationInfo.organizationId,
        		'locationName': locationInfo.locationName,
        		'city': locationInfo.city,
        		'position': locationInfo.position,
        		'longitude': locationInfo.longitude,
        		'latitude': locationInfo.latitude,
        		'radius': locationInfo.radius,
			};
		var json = Ext.encode(input);
		Ext.Ajax.request({
	   		url: 'rule/update',//?json='+ Ext.encode(input),
	        method : 'POST',
	        params:{json:json},
	        //defaultHeaders : {'Content-type' : 'application/json;utf-8'},
	        success : function(response,options) {
	        	var respText = Ext.util.JSON.decode(response.responseText);
	        	var retStatus = respText.status;var data = respText.data;
				if (retStatus == 'success' && data != "") {
					btn.up('editLocation').close();
					Ext.Msg.alert('提示信息','修改用车位置成功');
					Ext.getCmp("gridLocation").getStore('locationResults').load();
				}else{
					btn.up('editLocation').close();
					Ext.Msg.alert('消息提示','修改用车位置失败！');
					Ext.getCmp("gridLocation").getStore('locationResults').load();
				}	        	
	        },
	        /*failure : function() {
				btn.up('editLocation').close();
	            Ext.Msg.alert('消息提示','服务器繁忙，请稍后再试！');
	        },*/
	        scope:this
		});
   },

   	//删除
	deleteLocation : function(grid, rowIndex, colIndex) {
		Ext.Msg.confirm('消息提示','确定要删除吗！！！',function(btn){
			if (btn == 'yes') {
				var locationID = grid.getStore().getAt(rowIndex).id;
				var url = 'rule/'+locationID+'/delete';
					
				Ext.Ajax.request({
		   		url: url,
		        method : 'POST',
		        defaultHeaders : {'Content-type' : 'application/json;utf-8'},
		        success : function(response,options) {
					var respText = Ext.util.JSON.decode(response.responseText);
					var	retStatus = respText.status;
						if (retStatus == 'success') {
					       Ext.getCmp("gridLocation").getStore('locationResults').load();
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



});
