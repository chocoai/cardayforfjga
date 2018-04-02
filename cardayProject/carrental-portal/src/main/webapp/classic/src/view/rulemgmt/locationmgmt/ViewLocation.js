Ext.define('Admin.view.rulemgmt.locationmgmt.ViewLocation', {
	extend: 'Ext.window.Window',
	
    alias: "widget.viewLocation",
    controller: 'locationmgmtcontroller',
	reference: 'viewLocation',
	title : '查看位置信息',
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
								    padding: '15px'  //与边界的距离
							},
							
							fieldDefaults: {
					            labelAlign: 'left',
					            labelWidth: 100,
					            height:20,    
					        },
					        
							items: [{
								fieldLabel: '用车位置编号',
						        name: 'id',
							}, {
								fieldLabel: '用车位置名称',
						        name: 'locationName',
							}, 
							{
								fieldLabel: '位置',
						        name: 'position',
							},  
							{
								fieldLabel: '城市',
						        name: 'city',
							},  
							{
								fieldLabel: '范围',
								id: 'radiusLocation',
						        name: 'radius',
							},
							]
						},{
							flex: 2,
		        	        xclass: 'Admin.view.rulemgmt.locationmgmt.LocationMap',
		                }]
		            }],

	buttonAlign : 'center',
	buttons : [{
				text : '关闭',
				handler: function(btn){
					btn.up("viewLocation").close();
				}
			}
	]
});