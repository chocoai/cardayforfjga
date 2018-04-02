Ext.define('Admin.view.vehiclemgmt.geofencemgmt.EditGeofence', {
	extend: 'Ext.window.Window',
	
    alias: "widget.editGeofence",
    id: 'edit_geofence_id',
    controller: 'geofencemgmtcontroller',
	reference: 'editGeofence',
	title : '修改地理围栏',
	width : 1200,
	//height : 550,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
	//layout : 'form',
	labelAlign : 'center',
	lableWidth : 80,
	frame : true,
	items : [{
		    	xtype: 'container',
		        flex: 1,
		        layout: {
		            type: 'hbox',
		            pack: 'start',
		            align: 'stretch'
		        },
		        margin: '0 0 3 0',
		        defaults: {
		            flex: 1,
		            frame: true,
		        },
		        items: [{
		        			margin: '0 -2 0 0',
		        	        flex: 1,
							xtype:'form',
							layout : 'form',		
							bodyStyle : "background-color:#FFF0F5",
							items: [
								{
									xtype: 'displayfield',
									fieldLabel: '地理围栏编号',
							        name: 'id',
							        labelWidth: 60,
							        width: 220,
								},{
								fieldLabel: '地理围栏名称',
								xtype: 'textfield',
								allowBlank: false, 
								blankText: '地理围栏不能为空',
						        name: 'markerName',
						        maxLength:50,
						        labelWidth: 60,
					            width: 220,
							}, {
								fieldLabel: '省/直辖市',
								xtype: 'combo',
					        	displayField: 'regionName',
					        	valueField : 'regionId',
					        	id: 'editGeofenceProvinceCmb',
								allowBlank: false, 
								blankText: '省/直辖市不能为空',
					        	name: 'provinceId',
					        	labelWidth: 60,
					            typeAhead:false,
					            editable:false,
					            store:Ext.create('Ext.data.Store', {
							     	proxy: {
							         	type: 'ajax',
							         	url: 'area/queryAreaInfo',
							         	actionMethods : 'get', 
							     		reader: {
								        	type: 'json',
								         	rootProperty: 'data',
								         	successProperty: 'status'
							     		}
							     	},
							       	autoLoad:false,
						     	}),
						     	listeners : {
						     		afterrender:'loadProvinceComStore',
						     		select: 'provinceSelectLoadOnMap',
						     		change: 'getCityByprovince',
                                },
							}, {
								
								fieldLabel: '城市',
								xtype: 'combo',
								queryMode: 'remote',
					        	displayField: 'regionName',
					        	valueField : 'regionId',
								allowBlank: false, 
								blankText: '城市名称不能为空',
								id: 'editGeofenceCityCmb',
					        	name: 'cityId',
					        	labelWidth: 60,
					            typeAhead:false,
					            editable:false,
					            listeners:{
					            	/*afterrender : 'loadCityAfterrender',*/
					            	change: 'getpositionAddressStore',
						        	select: 'citySelectLoadOnMap',
					            },
					            /*store:Ext.create('Ext.data.Store', {
							     	proxy: {
							         	type: 'ajax',
							         	url: 'area/queryAreaInfo',
							         	actionMethods : 'get', 
							     		reader: {
								        	type: 'json',
								         	rootProperty: 'data',
								         	successProperty: 'status'
							     		}
							     	},
							       	autoLoad:false,
						     	})*/
							}, {
								fieldLabel: '地理围栏地址',
								id:'addressGeofenceId',
								xtype: 'textfield',
								//allowBlank: false, 
								blankText: '地理围栏地址不能为空',
						        name: 'position',
						        maxLength:50,
						        labelWidth: 60,
					            width: 220,
					            hidden: true,
					            listeners : {
                                    blur: function(){
                                    	Ext.getCmp('edit_geofence_id').down('form').getForm().findField('radius').setValue('');
                                    },
                                },
							},
							/*{
								fieldLabel: '行政区域',
								id:'administrativeDivisionId',
								xtype: 'textfield',
								//allowBlank: false, 
								blankText: '行政区域不能为空',
						        name: 'address',
						        maxLength:50,
						        labelWidth: 60,
					            width: 220,
					            hidden: true,
					            listeners : {
                                    blur: 'administrativeDivisionBlur',
                                },
							}*/
							
							//geofence radius yh
							{
								xtype: 'fieldcontainer',
								id:'radiusGeofenceId',
								hidden:true,
								layout: 'hbox',
								fieldLabel: '半径',
								//afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
								items: [
								{
									//fieldLabel: '半径',
						        	xtype: 'combo',
						        	displayField: 'type',
						        	name: 'radius',
						        	labelWidth: 60,
						            //width: 10,
						            typeAhead:false,
						            editable:false,
								    store : {
										fields : ['type','value' ],
										data : [
										        {'type' : '0.5','value' : '0.5'},
										        {'type' : '1.0','value' : '1.0'},
										        {'type' : '1.5','value' : '1.5'},
										        {'type' : '2.0','value' : '2.0'},
										        {'type' : '2.5','value' : '2.5'},
										        {'type' : '3.0','value' : '3.0'}
										]
									},
									listeners : {
/*	                                        render : function(obj) {
	                                            var font = document.createElement("font");
	                                            font.setAttribute("color","black");
	                                            var redStar = document.createTextNode('公里');
	                                            font.appendChild(redStar);
	                                            obj.el.dom.appendChild(font);
	                                        },*/
	                                        select: 'radiusSelectLoadOnMap',
	                                    },
								}, 
								{
									xtype: 'displayfield',
							    	value: '公里',
								}
								]
					        },
					        {
								fieldLabel: '经度',
								xtype: 'textfield',
								allowBlank: true, 
						        name: 'longitude',
						        maxLength:50,
						        labelWidth: 60,
					            width: 220,
					            hidden: true,
							},
							{
								fieldLabel: '纬度',
								xtype: 'textfield',
								allowBlank: true, 
						        name: 'latitude',
						        maxLength:50,
						        labelWidth: 60,
					            width: 220,
					            hidden: true,
							},
							
							{
								xtype:'combo',  
                                name:'address',  
                                id:'administrativeDivisionId',  
                                fieldLabel:'行政区域',  
                                emptyText:'请选择行政区域',  
                                allowBlank:false,  
                                queryMode: 'remote',
					        	displayField: 'regionName',
					        	valueField : 'regionId',
					        	editable:false,
                                listeners : {
                                    select: 'administrativeDivisionBlur',
    					            expand:function(combo,event){
    						        	combo.getStore().load();
    						        } 
                                },
                                store:Ext.create('Ext.data.Store', {
							     	proxy: {
							         	type: 'ajax',
							         	url: 'area/queryAreaInfo',
							         	actionMethods : 'get', 
							     		reader: {
								        	type: 'json',
								         	rootProperty: 'data',
								         	successProperty: 'status'
							     		}
							     	},
							       	autoLoad:false,
							       	listeners:{
							       		load:function(store,records,options){
							       			store.insert(0,{"value":"所有省份","id":"0"})
							       		},
							       		'beforeload' : function(store, operation, eOpts) {
							       			var provinceCombo = Ext.getCmp("editGeofenceCityCmb");
					                    	var provinceId = 0;
					                    	if(provinceCombo != null){
					                    	 var province = provinceCombo.getValue();
					                        	if(province != null && province != 0){
					                        	 provinceId = province;
					                        	}
					                    	}
							        		var input = {'parentId' : provinceId};
							        		console.log('provinceId:' + provinceId);
							        		var param = {'json': Ext.encode(input)};
							        		Ext.apply(store.proxy.extraParams, param);
							        	},
							       		
							       	},
						     	})          
							}, {
								fieldLabel: '类型',
								xtype: 'textfield',
								allowBlank: false, 
								id:'eidtGeofenceTypeId',
						        name: 'type',
						        maxLength:50,
						        labelWidth: 60,
					            width: 220,
					            hidden: true
							},{
								fieldLabel: '城市',
								xtype: 'textfield',
								allowBlank: false, 
						        name: 'city',
						        maxLength:50,
						        labelWidth: 60,
					            width: 220,
					            hidden: true
							},]
					    },{
							flex: 2,
		        	        title: '<div style="text-align: right; color: whitesmoke;font-size: 11px;">提示：绘制地理围栏时双击鼠标结束</div>',
		        	        xclass: 'Admin.view.vehiclemgmt.geofencemgmt.GeofenceMap',
		                }]
		            }],

	buttonAlign : 'center',
	buttons : [{
				text : '确定',
				handler: 'editGeofenceDone',
			},{
				text: '取消',
				handler: function(btn) {
					btn.up('editGeofence').close();
			}
	}]
});