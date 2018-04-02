Ext.define('Admin.view.ordermgmt.orderrecreate.ViewOrder', {
	extend: 'Ext.window.Window',
					
    alias: "widget.orderrecreatewin",
	reference: 'vieworder',
	title : '补录订单信息',
    minWidth : 400,
//    maxHeight: 600,
	closable: true,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	items : [{
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
        
		items: [
		{
			xtype: 'tbtext',
			//text: '<h2>审核信息</h2>',
			html:'<div style="font:18px arial;color:#0D85CA;margin: 10px 0px 20px 0px">订单信息</div>'
		},
		{
			fieldLabel: '单位名称',
	        name: 'organizationName',
		}, 
		{
			fieldLabel: '订单编号',
	        name: 'orderNo',
		},
		{
			fieldLabel: '纸质排车单号',
	        name: 'paperOrderNo',
		}, 
		{
			fieldLabel: '订单日期',
	        name: 'orderTimeF',
		},
		{
			fieldLabel: '用车人',
	        name: 'orderUsername',
		}, 
		{
			fieldLabel: '用车电话',
	        name: 'orderUserphone',
		},
		{
			fieldLabel: '用车城市',
			name: 'city',
		},
		{
			fieldLabel: '出发地',
			name: 'fromPlace',
		},
		{
			fieldLabel: '目的地',
			name: 'toPlace',
		},
		{
			fieldLabel: '出车时间',
			name: 'factStTimeF',
		},
		{
			fieldLabel: '出车前公里数',
			name: 'stMileage',
			hidden: true
		},
		{
			fieldLabel: '返回时间',
			name: 'factEdTimeF',
		},
		{
			fieldLabel: '返回后公里数',
			name: 'edMileage',
			hidden: true
		},
		{
			fieldLabel: '是否往返',
			name: 'returnType',
			renderer : function(value) {
            	if (value == 0) {
            		return value="是";
            	} else if (value == 1) {
            		return value="否";
            	}
            }
		},
		{
			id: 'recreate_view_order_wait_time_id',
			fieldLabel: '等待时长',
			name: 'waitTime',
		},
		{
			fieldLabel: '公车性质',
			name: 'vehicleType',
			renderer : function(value) {
				switch(value) {
				case '0':
					value='应急机要通信接待用车';
					break;
				case '1':
					value='行政执法用车';
					break;
				case '2':
					value='行政执法特种专业用车';
					break;
				case '3':
					value='一般执法执勤用车';
					break;
				case '4':
					value='执法执勤特种专业用车';
					break;
				}
				return value;
			}
		},
		{
			fieldLabel: '用车事由',
			name: 'orderReason',
			renderer : function(value) {
				switch(value) {
				case '0':
					value='送文件';
					break;
				case '1':
					value='特殊警务';
					break;
				case '2':
					value='常规巡逻';
					break;
				}
				return value;
			}
		},
		{
			fieldLabel: '车辆用途',
			name: 'vehicleUsage',
			renderer : function(value) {
				switch(value) {
				case 0:
					value='市区';
					break;
				case 1:
					value='省外';
					break;
				}
				return value;
			}
		},
		{
			fieldLabel: '随车人数',
			name: 'passengerNum',
		},
		{
			fieldLabel: '驾驶类型',
			name: 'drivingType',
			renderer : function(value) {
				switch(value) {
				case 0:
					value='派遣驾驶员';
					break;
				case 1:
					value='民警自驾';
					break;
				case 2:
					value='临时驾驶员';
					break;
				}
				return value;
			}
		},
		{
			fieldLabel: '用车备注',
			name: 'comments',
			maxWidth: 350,
			height: 30,
			hidden: true
		},
		{
	    	id: 'recreate_orderAttach_id',
	    	fieldLabel: "订单附件",
	    	xtype: 'label',  
            name : 'orderAttach',
	    },
		]
	}],

	buttonAlign : 'center',
	buttons : [{
				text : '关闭',
				handler: function(btn){
					btn.up('orderrecreatewin').close();
				}
			}
	]
});