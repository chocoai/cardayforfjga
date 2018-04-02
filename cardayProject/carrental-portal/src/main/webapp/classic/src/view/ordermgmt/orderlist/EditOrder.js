Ext.define('Admin.view.ordermgmt.orderlist.EditOrder', {
	extend: 'Ext.window.Window',
	
    alias: "widget.editorder",
    controller: 'orderlistcontroller',
	reference: 'editorder',
	title : '订单信息',
	width : 460,
	closable:true,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	bodyStyle : "padding:30px 20px",

	items : [{
		xtype:'form',
		layout: {
            type: 'vbox', 
            //type: 'form', //form，字段验证时，整体都会改变
            align: 'stretch'
        },	
        fieldDefaults: {
            msgTarget : Ext.supports.Touch ? 'side' : 'qtip'
        },
        
        dockedItems : [{
			xtype : 'toolbar',
			dock : 'bottom',
			ui : 'footer',
			style : "background-color:#FFFFFF",
			layout: {pack: 'center'},//button居中
			items : [
			{
				width: 120,
				text : '确认并重新提交',
				handler: 'editOrderDone',
				disabled: true,
		        formBind: true
			}]
		}],
		
		items: [
		{
			id: 'add_order_organizationName_id',
			fieldLabel: '单位名称',
			xtype: 'displayfield',
		    name: 'organizationName',
		},
		{
			xtype: 'displayfield',
			fieldLabel: '订单编号',
	        name: 'orderNo',
	        height:32,
		}, 
		{
			xtype: 'displayfield',
			fieldLabel: '订单日期',
	        name: 'orderTimeF',
	        height:32,
		},
		{
			xtype: 'displayfield',
			fieldLabel: '用车人',
	        name: 'orderUsername1',
	        height:32,
		}, 
		{
			xtype: 'textfield',
			fieldLabel: 'id',
	        name: 'id',
	        hidden: true
		},
		{
			xtype: 'textfield',
			fieldLabel: '用车人id',
	        name: 'orderUserid',
	        hidden: true
		},
		{
			xtype: 'textfield',
			fieldLabel: '用车人姓名',
	        name: 'orderUsername',
	        hidden: true
		},
		{
			xtype: 'textfield',
			fieldLabel: '用车人组织Id',
	        name: 'organizationId',
	        hidden: true
		},
		{
			xtype: 'displayfield',
			fieldLabel: '用车人电话',
	        name: 'orderUserphone',
	        height:32,
		},
		{
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
	        editable:false,
    		fieldLabel: '用车城市',
            xtype: 'combo',
            name: 'city',
            displayField: 'name',
            store: {
            	fields:['id', 'name'],
            	data : [
				        {'id': '0', 'name': '福州市'},
				        {'id': '1', 'name': '厦门市'},
				        {'id': '2', 'name': '莆田市'},
				        {'id': '3', 'name': '三明市'},
				        {'id': '4', 'name': '泉州市'},
				        {'id': '5', 'name': '漳州市'},
				        {'id': '6', 'name': '南平市'},
				        {'id': '7', 'name': '龙岩市'},
				        {'id': '8', 'name': '宁德市'},

				]
            }
    	}, 
		{
    		id:'editOrderFromPlaceId',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
    		xtype: 'textfield',
			fieldLabel: '出发地',
			name: 'fromPlace',
		},
		{
    		id:'editOrderFromPlace_lng_id',
    		fieldLabel: '出发地经度',
            xtype: 'textfield',
            name: 'fromLng',
            hidden: true,
    	},
    	{
    		id:'editOrderFromPlace_lat_id',
    		fieldLabel: '出发地纬度',
            xtype: 'textfield',
            name: 'fromLat',
            hidden: true,
    	},
		{
			id:'editOrderToPlaceId',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
			xtype: 'textfield',
			fieldLabel: '目的地',
			name: 'toPlace',
		},
		{
    		id:'editOrderToPlace_lng_id',
    		fieldLabel: '目的地经度',
            xtype: 'textfield',
            name: 'toLng',
            hidden: true,
    	},
    	{
    		id:'editOrderToPlace_lat_id',
    		fieldLabel: '目的地纬度',
            xtype: 'textfield',
            name: 'toLat',
            hidden: true,
    	},
    	{
    		id:'editOrder_planTime_id',
    		fieldLabel: '预约用车时间',
            xtype: 'textfield',
            name: 'planTime',
            hidden: true,
    	},
    	{
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
			xtype: 'fieldcontainer',
			layout: 'hbox',
			fieldLabel: '预约用车时间',
			height:32,
			items: [
			    {
			    	id: 'edit_order_planTime_id',
			    	xtype: 'datefield',
			    	format: 'Y-m-d',
			    	width: 125,
			    	name: 'date',
			    	minValue: new Date(),
			    	listeners:{
			    		select: 'selectPlanTime'
					} 
			    },
			    {
					xtype: 'displayfield',
					value: '日',
				},
			    {
					id: 'edit_plan_hour_id',
			    	xtype: 'combo',
			    	width: 70,
			    	displayField: 'time',
			    	//valueField: 'id',
			    	editable: false,
			    	name: 'hour',
			    	displayField: 'time',
		            valueField: 'id',
			        listeners:{
			        	select: 'selectHour'
			    	} 
			    },
			    {
			    	xtype: 'displayfield',
					value: '时',
			    },
			    {
			    	id: 'edit_plan_minute_id',
			    	afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    		allowBlank: false,//不允许为空
			        blankText: '不能为空',//提示信息
			        msgTarget: 'side',
			    	xtype: 'numberfield',
			    	minValue: 0,
			    	maxValue: 59,
			    	width: 70,
			    	name: 'minute',
			    },
			    {
			    	xtype: 'displayfield',
					value: '分',
			    }
			 ]
    	},
//    	{
//    		xtype: 'fieldcontainer',
//    		layout: 'hbox',
//    		labelWidth: 90,
//    		fieldLabel: '预计行程时间',
//    		items: [
//				{
//					xtype: 'combo',
//					width: 75,
//					displayField: 'value',
//					valueField: 'value',
//					name:'durationTime',
//					store: {
//						fields:['name', 'value'],
//				    	data : [
//				    	        {name: '0', value: '0.5'},
//						        {name: '1', value: '1'},
//						        {name: '2', value: '1.5'},
//						        {name: '3', value: '2'},
//						        {name: '4', value: '2.5'},
//						        {name: '5', value: '3'},
//						        {name: '6', value: '3.5'},
//						        {name: '7', value: '4'},
//						        {name: '8', value: '4.5'},
//						        {name: '9', value: '5'},
//						        {name: '10', value: '5.5'},
//						        {name: '11', value: '6'},
//						        {name: '12', value: '6.5'},
//						        {name: '13', value: '7'},
//						        {name: '14', value: '7.5'},
//						        {name: '15', value: '8'},
//						]
//					}
//				},
//		    	{
//			    	xtype: 'displayfield',
//					value: '小时',
//			    }
//    		]
//    	},
    	{
    		id:'editorder_durationTime_id',
    		fieldLabel: '预计行车时间',
            xtype: 'textfield',
            name: 'durationTime',
            hidden: true,
    	},
    	{
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
    		xtype: 'fieldcontainer',
    		layout: 'hbox',
    		fieldLabel: '预计行程时间',
    		height:32,
    		items: [
				{
					xtype: 'combo',
					width: 75,
					displayField: 'value',
					valueField: 'value',
					name:'durationHour',
					store: {
						fields:['name', 'value'],
				    	data : [
				    	        {name: '0', value: '0'},
						        {name: '1', value: '1'},
						        {name: '2', value: '2'},
						        {name: '3', value: '3'},
						        {name: '4', value: '4'},
						        {name: '5', value: '5'},
						        {name: '6', value: '6'},
						        {name: '7', value: '7'},
						]
					}
				},
		    	{
			    	xtype: 'displayfield',
					value: '小时',
			    },
			    {
			    	xtype: 'numberfield',
			    	afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    		allowBlank: false,//不允许为空
			        blankText: '不能为空',//提示信息
			        msgTarget: 'side',
			    	width: 70,
			    	minValue: 0,
			    	maxValue: 59,
			    	name: 'durationMinute',
			    },
			    {
			    	xtype: 'displayfield',
					value: '分',
			    }
    		]
    	},
		{
			xtype: 'radiogroup',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
	        msgTarget: 'side',
			fieldLabel: '是否往返',
			layout: 'hbox',
	        items: [
			        { 
			        	boxLabel: "是", 
			        	name: "returnType",
			        	inputValue: "0",
			        	style:{
			        		marginRight:'20px',
			        	}
			        },
			        { 
			        	boxLabel: "否", 
			        	name: "returnType",
			        	inputValue: "1",
			        	//checked: true
			        }
			    ],
		    listeners : {
		    	change : function (obj, newValue , oldValue) {
		    		if (newValue.returnType == 1 && oldValue.returnType == 0) {
		    			//选中否，隐藏 ‘等待时长’字段
//		    			Ext.getCmp('editorder_waitHour_id').clearValue();
//		    			Ext.getCmp('editorder_waitMinute_id').reset();
		    			Ext.getCmp('editorder_waitTime_id').setHidden(true);
		    		} else if (newValue.returnType == 0 && oldValue.returnType == 1) {
		    			//选中是，显示 ‘等待时长’字段
		    			Ext.getCmp('editorder_waitTime_id').setHidden(false);
		    			
		    		}
		    	}
		   }
    	},
    	{
    		id:'editorder_waitTime_id1',
    		fieldLabel: '预计行车时间',
            xtype: 'textfield',
            name: 'waitTime',
            hidden: true,
    	},
    	{
    		id : 'editorder_waitTime_id',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
    		xtype: 'fieldcontainer',
    		layout: 'hbox',
    		fieldLabel: '等待时长',
    		height:32,
    		editable: false,
    		hidden: true,
    		items: [
				{
					id: 'editorder_waitHour_id',
					xtype: 'combo',
					width: 75,
					displayField: 'value',
					valueField: 'value',
					name:'waitHour',
					store: {
						fields:['name', 'value'],
				    	data : [
				    	        {name: '0', value: '0'},
						        {name: '1', value: '1'},
						        {name: '2', value: '2'},
						        {name: '3', value: '3'},
						        {name: '4', value: '4'},
						        {name: '5', value: '5'},
						        {name: '6', value: '6'},
						        {name: '7', value: '7'},
						]
					}
				},
		    	{
			    	xtype: 'displayfield',
					value: '小时',
			    },
			    {
			    	id: 'editorder_waitMinute_id',
			    	afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    		allowBlank: false,//不允许为空
			        blankText: '不能为空',//提示信息
			        msgTarget: 'side',
			    	xtype: 'numberfield',
			    	width: 70,
			    	minValue: 0,
			    	maxValue: 59,
			    	name: 'waitMinute',
			    },
			    {
			    	xtype: 'displayfield',
					value: '分',
			    }
    		]
    	},
    	{
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
	        editable:false,
			fieldLabel: '公车性质',
	        xtype: 'combo',
	        name: 'vehicleType',
            displayField: 'name',
            valueField: 'value',//combo可以获取的值
            store: {
            	fields:['value', 'name'],
            	data : [
				        {'value': '0', 'name': '应急机要通信接待用车'},
				        {'value': '1', 'name': '行政执法用车'},
				        {'value': '2', 'name': '行政执法特种专业用车'},
				        {'value': '3', 'name': '一般执法执勤用车'},
				        {'value': '4', 'name': '执法执勤特种专业用车'},
				]
            }
		},
		{
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
			fieldLabel: '车辆用途',
			editable:false,
			name: 'vehicleUsage',
	        xtype: 'combo',
            displayField: 'name',//combo显示的值
            valueField: 'id',//combo可以获取的值
            store: {
            	fields:['id', 'name'],
            	data : [
				        {'id': '0', 'name': '市区'},
				        {'id': '1', 'name': '省外'},

				]
            }
		},
		{
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
	        editable:false,
			fieldLabel: '用车事由',
			name: 'orderReason',
	        xtype: 'combo',
            displayField: 'name',//combo显示的值
            valueField: 'value',//combo可以获取的值
            store: {
            	fields:['value', 'name'],
            	data : [
				        {'value': '0', 'name': '送文件'},
				        {'value': '1', 'name': '特殊警务'},
				        {'value': '2', 'name': '常规巡逻'}

				]
            }
		},
		{
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
			fieldLabel: '涉密级别',
			editable:false,
			name: 'secretLevel',
	        xtype: 'combo',
            displayField: 'name',//combo显示的值
            valueField: 'id',//combo可以获取的值
		    listeners:{
				afterrender:function(me,eOpts){
					var secretLevelStore;
					if(window.sessionStorage.getItem("hasSpecialServicePerm") == "true"){
						secretLevelStore = {
					    		data: [{'id': '0', 'name': '不涉密'},{'id': '1', 'name': '机密'},{'id': '2', 'name': '绝密'}]
							};
					}else{
						secretLevelStore = {
					    		data: [{'id': '0', 'name': '不涉密'}]
							};
					}
					me.setStore(secretLevelStore);
				},
			}
		},
		{
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
    		xtype: 'fieldcontainer',
    		layout: 'hbox',
    		fieldLabel: '随车人数',
    		height:32,
    		editable: false,
    		items: [
			    {
			    	xtype: 'numberfield',
			    	afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    		allowBlank: false,//不允许为空
			        blankText: '不能为空',//提示信息
			        msgTarget: 'side',
			    	width: 100,
			    	minValue: 1,
			    	name: 'passengerNum',
			    },
			    {
			    	xtype: 'displayfield',
					value: '人',
			    }
    		]
    	},
    	{
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
			fieldLabel: '驾驶类型',
			editable:false,
			name: 'drivingType',
	        xtype: 'combo',
            displayField: 'name',//combo显示的值
            valueField: 'id',//combo可以获取的值
            store: {
            	fields:['id', 'name'],
            	data : [
				        {'id': '0', 'name': '派遣驾驶员'},
				        {'id': '1', 'name': '民警自驾'},
				        {'id': '2', 'name': '临时驾驶员'}

				]
            }
		},
		{
    		fieldLabel: '附件',
			//labelWidth: 120,
			anchor: '100%',
		    xtype: 'filefield',
		    name: 'orderAttach',
		    emptyText:'请选择jpg格式图片',
		    regex:/\.jpg$/,
		    regexText :'文件格式不正确',
		    buttonText: '',
            buttonConfig: {
                iconCls: 'fa-file'
            },
            hidden: false,
            listeners: {
            	change: function(me,value,eOpts){
            		console.log('filefield change' + me.isValid());
            		if(!me.isValid()){
            			Ext.Msg.alert('消息提示','文件格式不正确，请重新选择！');
            			me.setRawValue('');		                			
            		}
            	},
			},
		},
    	{
			fieldLabel: '用车备注',
			name: 'comments',
	        xtype: 'textarea',
	        maxLength: 40,
	        msgTarget: 'side',
	        hidden: true
    	},
    	{
			flex: 2,
	        xclass: 'Admin.view.ordermgmt.orderlist.OrderMap',
	        hidden: true
        }
		]
	}]
});