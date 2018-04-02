Ext.define('Admin.view.ordermgmt.orderrecreate.EditOrder', {
	extend: 'Ext.window.Window',
	
    alias: "widget.recreateEditOrderWin",
    controller: 'orderrecreatecontroller',
	title : '订单信息',
	minWidth : 400,
	maxHeight : 600,
	scrollable:'y',
    closeToolText:'',
	scrollable: true,
	closable:true,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	bodyStyle : "padding:20px 20px",
	
	items : [{
		xtype:'form',
		layout: {
            type: 'vbox', 
            //type: 'form', //form，字段验证时，整体都会改变
            align: 'stretch'
        },
        
        fieldDefaults: {
            msgTarget : Ext.supports.Touch ? 'side' : 'qtip',
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
			id: 'recreate_order_organizationName_id',
			fieldLabel: '单位名称',
			xtype: 'displayfield',
		    name: 'organizationName',
		    labelWidth: 90,
	        width: 220,
		},
		{
			xtype: 'displayfield',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
			fieldLabel: '订单编号',
	        name: 'orderNo',
	        labelWidth: 90,
	        width: 220,
		}, 
		{
			xtype: 'displayfield',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
			fieldLabel: '订单日期',
	        name: 'orderTimeF',
	        labelWidth: 90,
	        width: 220,
		},
		{
			xtype: 'displayfield',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
			fieldLabel: '用车人',
			editable: false,
	        name: 'orderUsername1',
	        labelWidth: 90,
	        width: 220,
		}, 
		{
			xtype: 'textfield',
			fieldLabel: 'id',
	        name: 'id',
	        labelWidth: 90,
	        width: 220,
	        hidden: true
		},
		{
			xtype: 'textfield',
			fieldLabel: '用车人id',
	        name: 'orderUserid',
	        labelWidth: 90,
	        width: 220,
	        hidden: true
		},
		{
			xtype: 'textfield',
			fieldLabel: '用车人姓名',
	        name: 'orderUsername',
	        labelWidth: 90,
	        width: 220,
	        hidden: true
		},
		{
			xtype: 'textfield',
			id: 'edit_ordercreate_orderUserOrganizationId_id',
			fieldLabel: '用车人组织Id',
	        name: 'organizationId',
	        labelWidth: 90,
	        width: 220,
	        hidden: true
		},
		{
			xtype: 'displayfield',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
			fieldLabel: '用车电话',
	        name: 'orderUserphone',
	        labelWidth: 90,
	        width: 220,
		},
		{
    		fieldLabel: '用车城市',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        editable: false,
            xtype: 'combo',
            labelWidth: 90,
            width: 220,
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
    		id: 'recreateEditOrderFromPlaceId',
			xtype: 'textfield',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
			fieldLabel: '出发地',
			name: 'fromPlace',
			labelWidth: 90,
	        width: 220,
		},
		{
			id: 'recreateEditOrderToPlaceId',
			xtype: 'textfield',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
			fieldLabel: '目的地',
			name: 'toPlace',
			labelWidth: 90,
	        width: 220,
		},
		{
			id: 'recreate_editOrder_factStTime_id',
			xtype: 'textfield',
			name: 'factStTime',
	        hidden: true
		},
		{
			id: 'recreate_editOrder_factEdTime_id',
			xtype: 'textfield',
			name: 'factEdTime',
	        hidden: true
		},
    	{
    		xtype: 'fieldcontainer',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
			layout: 'hbox',
			labelWidth: 90,
			fieldLabel: '出车时间',
			items: [
			    {
			    	id:'edit_order_start_id',
			    	xtype: 'datefield',
			    	format: 'Y-m-d',
			    	width: 125,
			    	name: 'stDate',
			    	editable: false,
			        allowBlank:false,
			        maxValue:new Date(),
			        value:new Date(),
			        listeners:{
			        	select: 'selectCallTime'
			    	} 
			    },
			    {
					xtype: 'displayfield',
					value: '日',
				},
			    {
			    	id: 'edit_hour_start_id',
			    	xtype: 'combo',
			    	editable: false,
			    	width: 70,
			    	displayField: 'time',
			    	name: 'stHour',
			    	valueField: 'id',
			    	allowBlank: false,
			        listeners:{
			        	select: 'selectHour'
			    	} 
			    },
			    {
			    	xtype: 'displayfield',
					value: '时',
			    },
			    {
			    	id: 'edit_minute_start_id',
			    	xtype: 'numberfield',
			    	width: 70,
			    	minValue: 0,
			    	maxValue: 59,
			    	name: 'stMinute',
			    	allowBlank: false,
			    },
			    {
			    	xtype: 'displayfield',
					value: '分',
			    }
			 ]
    	},
    	{
			id: 'recreate_stMileage_id',
			fieldLabel: '出车前公里数',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			xtype: 'textfield',
			name: 'stMileage',
			allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        labelWidth: 90,
	        width: 220,
		},
    	{
    		xtype: 'fieldcontainer',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
			layout: 'hbox',
			labelWidth: 90,
			fieldLabel: '返回时间',
			items: [
			    {
			    	id:'edit_order_end_id',
			    	xtype: 'datefield',
			    	format: 'Y-m-d',
			    	width: 125,
			    	name: 'edDate',
			    	editable:false,
			        allowBlank:false,
			        maxValue:new Date(),
			        value:new Date(),
			        listeners:{
			        	select: 'selectCallTime'
			    	} 
			    },
			    {
					xtype: 'displayfield',
					value: '日',
				},
			    {
			    	id: 'edit_hour_end_id',
			    	xtype: 'combo',
			    	editable: false,
			    	width: 70,
			    	displayField: 'time',
			    	name: 'edHour',
			    	valueField: 'id',
			    	allowBlank: false,
			        listeners:{
			        	select: 'selectHour'
			    	} 
			    },
			    {
			    	xtype: 'displayfield',
					value: '时',
			    },
			    {
			    	id: 'edit_minute_end_id',
			    	xtype: 'numberfield',
			    	width: 70,
			    	minValue: 0,
			    	maxValue: 59,
			    	name: 'edMinute',
			    	allowBlank: false,
			    },
			    {
			    	xtype: 'displayfield',
					value: '分',
			    }
			 ]
    	},
    	{
			id: 'recreate_edMileage_id',
			fieldLabel: '返回后公里数',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			xtype: 'textfield',
			name: 'edMileage',
			allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        labelWidth: 90,
	        width: 220,
		},
		{
			xtype: 'radiogroup',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
			fieldLabel: '是否往返',
			layout: 'hbox',
	        labelWidth: 90,
	        //width: 220,
	        items: [
			        { 
			        	boxLabel: "是", 
			        	name: "returnType",
			        	inputValue: "0",
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
//		    			Ext.getCmp('recreate_editorder_waitHour_id').clearValue();
//		    			Ext.getCmp('recreate_editorder_waitMinute_id').reset();
		    			Ext.getCmp('recreate_editorder_waitTime_id').setHidden(true);
		    		} else if (newValue.returnType == 0 && oldValue.returnType == 1) {
		    			//选中是，显示 ‘等待时长’字段
		    			Ext.getCmp('recreate_editorder_waitTime_id').setHidden(false);
		    			
		    		}
		    	}
		   }
    	},
    	{
    		id : 'recreate_editorder_waitTime_id',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
    		xtype: 'fieldcontainer',
    		layout: 'hbox',
    		fieldLabel: '等待时长',
    		editable: false,
    		hidden: true,
    		items: [
				{
					id: 'recreate_editorder_waitHour_id',
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
			    	id: 'recreate_editorder_waitMinute_id',
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
			fieldLabel: '公车性质',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        editable: false,
	        xtype: 'combo',
	        name: 'vehicleType',
	        labelWidth: 90,
            width: 220,
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
			fieldLabel: '用车事由',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        editable: false,
			name: 'orderReason',
	        xtype: 'combo',
	        labelWidth: 90,
            width: 220,
            displayField: 'name',//combo显示的值
            //valueField: 'value',//combo可以获取的值
            store: {
            	fields:['value', 'name'],
            	data : [
        		 	{'id': '0', 'name': '送文件'},
		        	{'id': '1', 'name': '特殊警务'},
		        	{'id': '2', 'name': '常规巡逻'}
				]
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
    		editable: false,
    		labelWidth: 90,
	        width: 220,
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
			fieldLabel: '用车备注',
			name: 'comments',
			xtype: 'textarea',
	        maxLength: 40,
	        msgTarget: 'side',
	        labelWidth: 90,
	        width: 220,
	        hidden: true
    	},
    	{
			id: 'edit_ordercreate_vehicle_id',
			itemId: 'itemVehicle',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        editable: false,
			fieldLabel: '车牌号码',
			labelWidth: 90,
	        width: 220,
			name: 'vehicleNumber',
		    xtype: 'combo',
	        displayField: 'vehicleNumber',
		    valueField: 'id',
		    listeners:{
		    	afterrender: 'selectVehicle',
		    	select:'editSelectVehicleDone'
			}
      	},
    	{
      		id: 'edit_ordercreate_vehicle_id_id',
			fieldLabel: '车辆Id',
			name: 'vehicleId',
	        xtype: 'textfield',
	        labelWidth: 90,
	        width: 220,
	        hidden: true
    	},
    	{
			id: 'edit_ordercreate_seatNumber_id',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			fieldLabel: '座位数',
			name: 'seatNumber',
		    xtype: 'textfield',
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        editable: false,
	        labelWidth: 90,
	        width: 220,
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
            labelWidth: 90,
	        width: 220,
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
			fieldLabel: '驾驶类型',
			editable:false,
			name: 'drivingType',
	        xtype: 'combo',
            displayField: 'name',//combo显示的值
            valueField: 'id',//combo可以获取的值
            labelWidth: 90,
	        width: 220,
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
			id: 'edit_ordercreate_driver_id',
			itemId: 'itemDriver',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        editable: false,
			fieldLabel: '司机',
			labelWidth: 90,
	        width: 220,
			name: 'driverName',
		    xtype: 'combo',
		    displayField: 'realname',
		    valueField: 'id',
		    listeners:{
		    	afterrender:'selectDriver',
				select: 'editSelectDriverDone'
			}
      	},
	  	{
      		id: 'edit_ordercreate_driver_id_id',
			fieldLabel: '司机Id',
			name: 'driverId',
	        xtype: 'textfield',
	        labelWidth: 90,
	        width: 220,
	        hidden: true
		},
		{
      		id: 'edit_ordercreate_driver_name_id',
			fieldLabel: '司机姓名',
			name: 'driverName1',
	        xtype: 'textfield',
	        labelWidth: 90,
	        width: 220,
	        hidden: true
		},
		{
      		id: 'edit_ordercreate_driver_phone_id',
			fieldLabel: '司机手机号',
			name: 'driverPhone',
	        xtype: 'textfield',
	        editable: false,
	        labelWidth: 90,
	        width: 220,
	        hidden: false
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
	        xclass: 'Admin.view.ordermgmt.orderrecreate.OrderMap',
	        hidden: true
        }
		]
	}]
});