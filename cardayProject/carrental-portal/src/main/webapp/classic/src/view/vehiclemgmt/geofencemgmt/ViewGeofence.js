Ext.define('Admin.view.vehiclemgmt.geofencemgmt.ViewGeofence', {
	extend: 'Ext.window.Window',
	
    alias: "widget.viewGeofence",
    controller: 'geofencemgmtcontroller',
	reference: 'viewGeofence',
	title : '查看地理围栏信息',
	width : 1200,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
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
							layout : 'vbox',
							defaultType: 'displayfield',
							
							bodyStyle: {
								//  background: '#ffc',
								    padding: '15px'  //与边界的距离
							},
							
							fieldDefaults: {
					            labelAlign: 'left',
					            labelWidth: 100,
					            height:20,    
					        },
					        
							items: [{
								fieldLabel: '地理围栏编号',
						        name: 'id',
							}, {
								fieldLabel: '地理围栏名称',
						        name: 'markerName',
							},{
								fieldLabel: '省/直辖市',
						        name: 'province',
							},{
								fieldLabel: '城市',
						        name: 'city',
							},{
								fieldLabel: '地理围栏地址',
						        name: 'position',
							},
							{
								fieldLabel: '半径',
						        name: 'radius',
						        hidden:true,
						        renderer: function (value, field) {
						        	if(value == null){
						        		return  '--';
						        	}
						        	return  value + '公里';
				        		}
							},
							{
								fieldLabel: '分配车辆数',
						        name: 'assignedVehicleNumber',
							}, 
							]
						},{
							flex: 2,
		        	        xclass: 'Admin.view.vehiclemgmt.geofencemgmt.GeofenceMap',
		                }]
		            }],

	buttonAlign : 'center',
	buttons : [{
				text : '关闭',
				handler: function(btn){
					btn.up("viewGeofence").close();
				}
			}
	]
});