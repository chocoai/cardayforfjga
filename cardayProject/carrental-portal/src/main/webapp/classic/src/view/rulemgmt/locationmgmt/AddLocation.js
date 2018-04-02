Ext.define('Admin.view.rulemgmt.locationmgmt.AddLocation', {
	extend: 'Ext.window.Window',
	
    alias: "widget.addLocation",
    id: 'addLocation',
    controller: 'locationmgmtcontroller',
	reference: 'addLocation',
	title : '新增用车位置',
	width : 1200,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	frame : true,

	listeners:{
    	afterrender: 'loadAddLocationInformation',
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
							items: [{
								fieldLabel: '<span style="color:red;">*</span>位置名称',
								xtype: 'textfield',
								allowBlank: false, 
								blankText: '位置名称不能为空',
						        name: 'locationName',
						        maxLength:50,
						        labelWidth: 60,
					            width: 220,
							}, 
							{
								fieldLabel: '<span style="color:red;">*</span>城市',
								xtype: 'combo',
					        	displayField: 'value',
								allowBlank: false, 
								blankText: '城市名称不能为空',
					        	name: 'city',
					        	labelWidth: 60,
					            typeAhead:false,
					            editable:false,
							    store : {
									fields : ['id','value' ],
									data : [
									        {'id' : '1','value' : '北京市'},
									        {'id' : '2','value' : '上海市'},
									        {'id' : '3','value' : '深圳市'},
									        {'id' : '4','value' : '武汉市'},
									        {'id' : '5','value' : '广州市'},
									]
								},
								listeners : {
                                    select: 'citySelectLoadOnMap',
                                },
							},
							{
								fieldLabel: '<span style="color:red;">*</span>位置',
								id:'positionLocationId',
								xtype: 'textfield',
								allowBlank: false, 
								blankText: '位置不能为空',
						        name: 'position',
						        maxLength:50,
						        labelWidth: 60,
					            width: 220,
							},
							{
								xtype: 'fieldcontainer',
								layout: 'hbox',
								fieldLabel: '<span style="color:red;">*</span>半径',
								items: [
								{
						        	xtype: 'combo',
						        	displayField: 'type',
									allowBlank: false, 
									blankText: '半径不能为空',
						        	name: 'radius',
						        	labelWidth: 60,
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
							}],
						buttonAlign : 'center',
					    buttons: [{
								text : '确定',
								disabled : true,
				                formBind : true,
								handler: 'addLocationDone',
							},{
								text: '取消',
								handler: function(btn) {
									btn.up('addLocation').close();
								}
							}]
						},{
								flex: 2,
			        	        xclass: 'Admin.view.rulemgmt.locationmgmt.LocationMap',
			                }],
		    }

	   ],
});