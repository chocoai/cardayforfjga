Ext.define('Admin.view.vehiclemgmt.stationmgmt.AddStation', {
	extend: 'Ext.window.Window',
	
    alias: "widget.addStation",
    id: 'addStation',
    controller: 'stationmgmtcontroller',
	reference: 'stationView',
	title : '新增站点',
	width : 1200,
	//height : 400,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
	frame : true,

	listeners:{
    	afterrender: 'loadAddStationInformation',
    },
    
	
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
		        items: [ {
		        			margin: '0 -2 0 0',
		        	        flex: 1,
							xtype:'form',
							layout : 'form',		
							bodyStyle : "background-color:#FFF0F5",
							items: [{
								fieldLabel: '站点名称',
								xtype: 'textfield',
								allowBlank: false, 
								blankText: '站点名称不能为空',
						        name: 'stationName',
						        afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
						        maxLength:50,
						        labelWidth: 60,
					            width: 220,
							}, {
								fieldLabel: '省/直辖市',
								xtype: 'combo',
								queryMode: 'local',
								id:'addStationProvinceCmb',
								displayField: 'regionName',
					        	valueField : 'regionId',
								allowBlank: false, 
								blankText: '省/直辖市不能为空',
					        	name: 'province',
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
							       	autoLoad:true,
						     	}),
						     	listeners:{
						     		select: 'provinceSelectLoadOnMap'
						     	}           
							}, {
								
								fieldLabel: '城市',
								xtype: 'combo',
								id:'addStationCityCmb',
								queryMode: 'remote',
								displayField: 'regionName',
					        	valueField : 'regionId',
								allowBlank: false, 
								blankText: '城市名称不能为空',
					        	name: 'city',
					        	afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					        	labelWidth: 60,
					            typeAhead:false,
					            editable:false,
					            listeners:{
					            	expand:function(combo,event){
						        		combo.getStore().load();
						        	},
						        	select: 'citySelectLoadOnMap',
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
							       	autoLoad:true,
							       	listeners:{
							       		'beforeload' : function(store, operation, eOpts) {
							       			var provinceCombo = Ext.getCmp("addStationProvinceCmb");
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
							       	
						     	}),		            
							}, {
								fieldLabel:'所在区',  
								xtype:'combo',  
                                name:'area',
                                afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
                                id:'addStationAreaCmb',  
                                displayField: 'regionName',
					        	valueField : 'regionId',
                                queryMode:'remote',
                                editable:false,
                                allowBlank: false,
                                listeners:{
					            	expand:function(combo,event){
						        		combo.getStore().load();
						        	},
//						        	select:'administrativeDivisionBlur',
						        	select:'areaSelectLoadOnMap',
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
							       	autoLoad:true,
							       	listeners:{
							       		'beforeload' : function(store, operation, eOpts) {
							       			var provinceCombo = Ext.getCmp("addStationCityCmb");
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
							},
							{
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
                                        	Ext.getCmp('addStation').down('form').getForm().findField('radius').setValue('');
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
		    }

	   ],
	
	

	buttonAlign : 'center',
	buttons : [{
				text : '确定',
				handler: 'addStationDone',
			},{
				text: '取消',
				handler: function(btn) {
					btn.up('addStation').close();
				}
			} ]
});