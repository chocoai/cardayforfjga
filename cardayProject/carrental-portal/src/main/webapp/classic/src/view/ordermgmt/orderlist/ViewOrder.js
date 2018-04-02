Ext.define('Admin.view.ordermgmt.orderlist.ViewOrder', {
	extend: 'Ext.window.Window',
	
    alias: "widget.vieworder",
    controller: 'orderlistcontroller',
	reference: 'vieworder',
	title : '订单详情',
	minWidth : 500,
	maxHeight: 620,
	//height: 800,
	scrollable: true,
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
			html:'<div style="font:18px arial;color:#0D85CA;">订单信息</div>'
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
			fieldLabel: '预约用车时间',
			name: 'planStTimeF',
			renderer : function(value) {
            	if (value==null || value=="") {
            		return value="--";
            	} else {
            		return value;
            	}
            }
		},
		{
			fieldLabel: '预计行程时间',
			name: 'durationTime',
		},
		{
			fieldLabel: '是否往返',
			name: 'returnType',
		},
		{
			id: 'view_order_wait_time_id',
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
			fieldLabel: '涉密级别',
			name: 'secretLevel',
			renderer : function(value) {
				switch(value) {
				case 1:
					value='机密';
					break;
				case 2:
					value='绝密';
					break;
            	case 3:
            		value='免审';
            		break;				
                default:    
                    value='未涉密';
                    break;
				}
				return value;
			}
		},
		{
	    	id: 'create_orderAttach_id',
	    	fieldLabel: "订单附件",
	    	xtype: 'label',  
            name : 'orderAttach',
	    },
		{
			fieldLabel: '用车备注',
			name: 'comments',
			hidden: true
		},
		{
			id: 'viewOrder_audit_id',
			xtype: 'tbtext',
			//text: '<h2>审核信息</h2>',
			html:'<div style="font:18px arial;color:#0D85CA;">审核信息</div>'
		},
		{
			id: 'paperOrderNo_id',
			fieldLabel: '纸质排车单号',
			name: 'paperOrderNo',
		},
		{
			id: 'auditUserName_id',
			fieldLabel: '审核人',
			name: 'auditUserName',
		},
		{
			id: 'auditUserPhone_id',
			fieldLabel: '手机号码',
			name: 'auditUserPhone',
		},
		{
			id: 'auditStatus_id',
			fieldLabel: '审核状态',
			name: 'auditStatus',
		},
		{
			id: 'auditTime_id',
			fieldLabel: '审核时间',
			name: 'auditTime',
		},
		{
			id: 'refuseComments_id',
			fieldLabel: '被驳回原因',
			name: 'refuseComments',
			//hidden: true,
		}
		]
	}],

	buttonAlign : 'center',
	buttons : [{
				text : '关闭',
				handler: function(btn){
					btn.up('vieworder').close();
				}
			}
	]
});