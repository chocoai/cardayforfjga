Ext.define('Admin.view.vehiclemgmt.stationmgmt.EditStation', {
	extend: 'Ext.window.Window',
	
    alias: "widget.editStation",
    id: 'edit_station_id',
    controller: 'stationmgmtcontroller',
	reference: 'editStation',
	title : '修改站点',
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
							xtype:'form',
							layout : 'form',		
							bodyStyle : "background-color:#FFF0F5",
							items: [{
								fieldLabel: '站点ID',
								xtype: 'textfield',
								allowBlank: false, 
								blankText: '站点名称不能为空',
						        name: 'id',
						        labelWidth: 60,
					            width: 220,
					            hidden:true,
							},{
								fieldLabel: '站点名称',
								xtype: 'textfield',
								allowBlank: false, 
								blankText: '站点名称不能为空',
						        name: 'stationName',
						        afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
						        labelWidth: 60,
					            width: 220,
							}, {
								fieldLabel: '省/直辖市',
								xtype: 'combo',
					        	displayField: 'regionName',
					        	valueField : 'regionId',
					        	id: 'editStationProvinceCmb',
								allowBlank: false, 
								blankText: '省/直辖市不能为空',
					        	name: 'provinceId',
					        	afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
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
								id:'editStationCityCmb',
								xtype: 'combo',
								queryMode: 'remote',
					        	displayField: 'regionName',
					        	valueField : 'regionId',
								allowBlank: false, 
								blankText: '城市名称不能为空',
					        	name: 'cityId',
					        	afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					        	labelWidth: 60,
					            typeAhead:false,
					            editable:false,
					            listeners:{
						        	select: 'citySelectLoadOnMap',
						        	change: 'getAreaBycity',
					            },
							}, {
								fieldLabel: '所在区',
								xtype: 'combo',
								id:'editStationAreaCmb',
								queryMode: 'remote',
					        	displayField: 'regionName',
					        	valueField : 'regionId',
								allowBlank: false, 
								blankText: '区不能为空',
					        	name: 'areaId',
					        	afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					        	labelWidth: 60,
					            typeAhead:false,
					            editable:false,
					            listeners:{
						        	select: 'areaSelectLoadOnMap',
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
								fieldLabel: '位置',
								id:'positionStationId',
								xtype: 'textfield',
								allowBlank: false, 
								blankText: '位置不能为空',
						        name: 'position',
						        afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
						        maxLength:50,
						        labelWidth: 60,
					            width: 220,
								listeners : {
                                        blur: function(){
                                        	Ext.getCmp('edit_station_id').down('form').getForm().findField('radius').setValue('');
                                        },
                                    },
							},
							{
                                xtype: 'fieldcontainer',
								layout: 'hbox',
								fieldLabel: '半径',
								afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
								items: [
								{
									//fieldLabel: '半径',
						        	xtype: 'combo',
						        	displayField: 'type',
									allowBlank: false, 
									blankText: '半径不能为空',
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
								fieldLabel: '分配车辆数',
								xtype: 'textfield',
								allowBlank: true, 
						        name: 'assignedVehicleNumber',
						        maxLength:50,
						        labelWidth: 60,
					            width: 220,
					            hidden:true
							},
							{
								fieldLabel: '停车位数量',
								xtype: 'textfield',
								allowBlank: true, 
						        name: 'carNumber',
						        maxLength:50,
						        labelWidth: 60,
					            width: 220,
							},
//							{
//								fieldLabel: '开始运营时间',
//								xtype: 'timefield',
//						        name: 'startTime',
//						        format: 'H:i',
//						        value: '09:00',
///*						        minValue: '6:00 AM',
//						        maxValue: '10:00 PM',*/
//						        increment: 10,
//						        anchor: '100%',
//					            width: 220,
//					            editable:false,
//							},
//							{
//								fieldLabel: '结束运营时间',
//								xtype: 'timefield',
//						        name: 'endTime',
//						        format: 'H:i',
//						        value: '18:00',
///*						        minValue: '6:00 AM',
//						        maxValue: '10:00 PM',*/
//						        increment: 10,
//						        anchor: '100%',
//					            width: 220,
//					            editable:false,
//							},
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
							}]
					    },{
							flex: 2,
		        	        xclass: 'Admin.view.vehiclemgmt.stationmgmt.StationMap',
		                }]
		            }],

	buttonAlign : 'center',
	buttons : [{
				text : '确定',
				handler: 'editStationDone',
			},{
				text: '取消',
				handler: function(btn) {
					btn.up('editStation').close();
			}
	}]
});