Ext.define('Admin.view.ordermgmt.orderlist.BackOrder', {
	extend: 'Ext.window.Window',
	
    alias: "widget.backorder",
    controller: 'orderlistcontroller',
	reference: 'backorder',
	title : '回车登记',
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
            height:32,
        },
        
		items: [
		{
			xtype: 'tbtext',
			//text: '<h2>审核信息</h2>',
			html:'<div style="font:18px arial;color:#0D85CA;">订单信息</div>'
		},
		{
			xtype: 'textfield',
			fieldLabel: 'id',
	        name: 'id',
	        hidden: true
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
			fieldLabel: '单位名称',
	        name: 'orgName',
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
			fieldLabel: '车辆用途',
			name: 'vehicleUsage',
			renderer : function(value) {
            	if (value==undefined && isNaN(value)) {
            		return "";
            	} else {
            		if(value == 0){
            			return '市区';
            		}else if(value == 1){
            			return '省外';
            		}else{
            			return "";
            		}
            	}
            }
		},
		{
			fieldLabel: '用车事由',
	        name: 'orderReason',
		},
		/*{
			fieldLabel: '用车城市',
			name: 'city',
		},*/
		{
			fieldLabel: '出发地',
			name: 'fromPlace',
		},
		{
			fieldLabel: '目的地',
			name: 'toPlace',
		},
		{
			fieldLabel: '随车人数',
			name: 'passengerNum',
		},
		/*{
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
		},*/
		{
			  fieldLabel: '车牌号',
			  name: 'vehicleNumber',
	    }, 
		{
	  	  	fieldLabel: '座位数',
	        name: 'seatNumber',
	    },
		{
	    	fieldLabel: '司机姓名',
	    	name: 'driverName',
	    },
	    {
	    	fieldLabel: '司机电话',
	        name: 'driverPhone',
	    },
		{
			fieldLabel: '起始里程',
			name: 'stMileage',
			renderer : function(value) {
            	if (value==null || value=="") {
            		return value="--";
            	} else {
            		return value + " KM";
            	}
            }
		},
		{
			fieldLabel: '出发时间',
			name: 'factStTimeF',
			renderer : function(value) {
            	if (value==null || value=="") {
            		return value="--";
            	} else {
            		return value.substring(0, 16);
            	}
            }
		},
		{
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
    		xtype: 'fieldcontainer',
    		id:'backorderEdMileageField',
    		layout: 'hbox',
    		fieldLabel: '结束里程',
    		editable: false,
    		items: [
			    {
			    	xtype: 'numberfield',
		    		allowBlank: false,//不允许为空
			        blankText: '不能为空',//提示信息
			        msgTarget: 'side',
			    	width: 100,
			    	minValue: 1,
			    	name: 'edMileage',
			    },
			    {
			    	xtype: 'displayfield',
					value: 'KM',
			    }
    		]
    	},
		{
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
    		xtype: 'fieldcontainer',
			layout: 'hbox',
//			labelWidth: 60,
			fieldLabel: '返回时间',
			height:32,
			items: [
			    {
			    	id: 'backorder_time_id',
			    	xtype: 'datefield',
			    	format: 'Y-m-d',
			    	width: 125,
			    	name: 'date',
			    	//value : new Date(),
			    	//minValue: this.up("form").getForm().findField("planStTimeF").getValue(),
			    	listeners:{
			    		select: 'selectBackOrderTime',
//			    		afterrender: function(field){
//			    			var startTime = field.up("form").getForm().findField("planStTimeF").getValue();
//			    			var startDate = new Date(startTime.replace(/-/,"/")) ;
//			    			field.setMinValue(startDate);
//			    		}
					} 
			    },
			    {
					xtype: 'displayfield',
					value: '日',
				},
			    {
					id: 'backorder_hour_id',
			    	xtype: 'combo',
			    	editable: false,
			    	width: 70,
			    	value: '00',
			    	displayField: 'time',
			    	name: 'hour',
		            valueField: 'id',
			        listeners:{
			        	select: 'selectBackOrderHour'
			    	} 
			    },
			    {
			    	xtype: 'displayfield',
					value: '时',
			    },
			    {
			    	id: 'backorder_minute_id',
			    	afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    		allowBlank: false,//不允许为空
			        blankText: '不能为空',//提示信息
			        msgTarget: 'side',
			    	xtype: 'numberfield',
			    	width: 70,
			    	minValue: 0,
			    	maxValue: 59,
			    	name: 'minute',
			    },
			    {
			    	xtype: 'displayfield',
					value: '分',
			    }
			 ]
    	},
    	
		/*{
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
		},
		{
			fieldLabel: '乘车人数',
			name: 'passengerNum',
		},
		{
			fieldLabel: '用车备注',
			name: 'comments',
			maxWidth: 350,
			height: 30
		},*/
		{
			id: 'viewOrder_audit_id',
			xtype: 'tbtext',
			//text: '<h2>审核信息</h2>',
			html:'<div style="font:18px arial;color:#0D85CA;">审核信息</div>'
		},
		{
			id: 'auditUserName_id',
			fieldLabel: '审核人',
			name: 'auditUserName',
		},
		/*{
			id: 'auditUserPhone_id',
			fieldLabel: '手机号码',
			name: 'auditUserPhone',
		},*/
		{
			id: 'auditStatus_id',
			fieldLabel: '审核状态',
			name: 'auditStatus',
			renderer : function(value) {
            	if (value==null || value=="") {
            		return value="--";
            	} else {
            		if(value == 1){
            			return '审核通过';
            		}else if(value == 5){
            			return '被驳回';
            		}else{
            			return "";
            		}
            	}
            }
		},
		/*{
			id: 'auditTime_id',
			fieldLabel: '审核时间',
			name: 'auditTime',
		},
		{
			id: 'refuseComments_id',
			fieldLabel: '被驳回原因',
			name: 'refuseComments',
			//hidden: true,
		}*/
		]
	}],

	buttonAlign : 'center',
	buttons : [{
				text : '取消',
				handler: function(btn){
					btn.up('window').close();
				}
			},{
				text : '确定',
				handler: 'backOrder'
			}
	]
});