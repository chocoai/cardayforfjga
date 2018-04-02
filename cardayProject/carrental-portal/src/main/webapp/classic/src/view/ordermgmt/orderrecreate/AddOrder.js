Ext.define('Admin.view.ordermgmt.orderrecreate.AddOrder', {
	extend: 'Ext.window.Window',
	       
    alias: "widget.addorderrecreatewin",
    controller: 'orderrecreatecontroller',
	title : '补录订单',
	minWidth : 400,
	maxHeight : 600,
	scrollable:'y',
    closeToolText:'',
	closable:true,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	frame : true,
	bodyStyle : "padding:30px 20px",
	items : [{
		xtype:'form',
		//layout : 'form',
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
				width: 100,
				text : '提交',
				handler: 'onSubmit',
				disabled: true,
		        formBind: true
			}]
		}],
		
		items: [
		{
			id: 'recreate_add_order_organizationName_id',
			fieldLabel: '单位名称',
			xtype: 'displayfield',
		    name: 'organizationName',
		},
        {
			id: 'ordercreate_orderUser_id',
			fieldLabel: '用车人',
			name: 'orderUser',
		    xtype: 'combo',
		    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        editable: false,
		    displayField: 'realname',
		    valueField: 'id',
		    listeners:{
				afterrender:'selectOrderUser',
				select: 'SelectOrderUserDone'
			}
      	},
      	{ 
      		id: 'ordercreate_orderUserId_id',
        	fieldLabel: '用车人Id',
			xtype: 'textfield',
			name: 'orderUserid',
			hidden: true
        },
      	{ 
      		id: 'ordercreate_orderUserName_id',
        	fieldLabel: '用车人姓名',
			xtype: 'textfield',
			name: 'orderUsername',
			hidden: true
        },
        { 
      		id: 'ordercreate_orderUserphone_id',
      		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
      		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
      		fieldLabel: '用车人电话',
			xtype: 'textfield',
			name: 'orderUserphone',
			editable: false,
        },
        { 
      		id: 'ordercreate_orderUserOrganizationId_id',
        	fieldLabel: '用车人组织Id',
			xtype: 'textfield',
			name: 'organizationId',
			hidden: true
        },
        {
    		fieldLabel: '用车城市',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
            xtype: 'combo',
            name: 'city',
            value: '福州市',
            displayField: 'name',
            allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        editable: false,
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
    		id: 'recreateAddOrderFromPlaceId',
			fieldLabel: '出发地',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			xtype: 'textfield',
			name: 'fromPlace',
			allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
		},
		{
			id: 'recreateAddOrderToPlaceId',
			fieldLabel: '目的地',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			xtype: 'textfield',
			name: 'toPlace',
			allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
		},
		{
			id: 'recreate_stMileage_id',
			fieldLabel: '出车前公里数',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			xtype: 'textfield',
			name: 'stMileage',
			allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
		},
		{
			id: 'recreate_addOrder_factStTime_id',
			xtype: 'textfield',
			name: 'factStTime',
	        hidden: true
		},
		{
			id: 'recreate_addOrder_factEdTime_id',
			xtype: 'textfield',
			name: 'factEdTime',
	        hidden: true
		},
    	{
    		xtype: 'fieldcontainer',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			layout: 'hbox',
			fieldLabel: '出车时间',
			name: 'planStTime',
			allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
			items: [
			    {
			    	id:'add_order_start_id',
			    	xtype: 'datefield',
			    	format: 'Y-m-d',
			    	width: 125,
			    	name: 'stdate',
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
			    	id: 'add_hour_start_id',
			    	xtype: 'combo',
			    	editable: false,
			    	width: 70,
			    	value: '00',
			    	displayField: 'time',
			    	name: 'sthour',
			    	displayField: 'time',
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
			    	id: 'add_minute_start_id',
			    	xtype: 'numberfield',
			    	width: 70,
			    	minValue: 0,
			    	maxValue: 59,
			    	name: 'stminute',
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
		},
		{
    		xtype: 'fieldcontainer',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			layout: 'hbox',
			fieldLabel: '返回时间',
			name: 'planEdTime',
			allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
			items: [
			    {			    	
			    	id:'add_order_end_id',
			    	xtype: 'datefield',
			    	format: 'Y-m-d',
			    	width: 125,
			    	name: 'eddate',
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
			    	id: 'add_hour_end_id',
			    	xtype: 'combo',
			    	editable: false,
			    	width: 70,
			    	value: '00',
			    	displayField: 'time',
			    	name: 'edhour',
			    	displayField: 'time',
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
			    	id: 'add_minute_end_id',
			    	xtype: 'numberfield',
			    	width: 70,
			    	minValue: 0,
			    	maxValue: 59,
			    	name: 'edminute',
			    	allowBlank: false,
			    },
			    {
			    	xtype: 'displayfield',
					value: '分',
			    }
			 ]
		},
	  	{ 
	    	xtype: "radiogroup",
	    	afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
			fieldLabel: "是否往返",
			//name: 'returnType',
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
		        	checked: true
		        }
		    ],
		    listeners : {
		    	change : function (obj, newValue , oldValue) {
		    		if (newValue.returnType == 1 && oldValue.returnType == 0) {
		    			//选中否，隐藏 ‘等待时长’字段
		    			Ext.getCmp('recreate_addorder_waitTime_id').setHidden(true);
		    		} else if (newValue.returnType == 0 && oldValue.returnType == 1) {
		    			//选中是，显示 ‘等待时长’字段
		    			Ext.getCmp('recreate_addorder_waitTime_id').setHidden(false);
		    		}
		    	}
		   }
		},
		{
    		id : 'recreate_addorder_waitTime_id',
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
	        xtype: 'combo',
	        name: 'vehicleType',
            displayField: 'name',
            valueField: 'id',//combo可以获取的值
            allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        editable: false,
            store: {
            	fields:['id', 'name'],
            	data : [
				        {'id': '0', 'name': '应急机要通信接待用车'},
				        {'id': '1', 'name': '行政执法用车'},
				        {'id': '2', 'name': '行政执法特种专业用车'},
				        {'id': '3', 'name': '一般执法执勤用车'},
				        {'id': '4', 'name': '执法执勤特种专业用车'},
				]
            }
		},
		{
			fieldLabel: '用车事由',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			name: 'orderReason',
	        xtype: 'combo',
            displayField: 'name',//combo显示的值
            allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        editable: false,
            store: {
            	fields:['id', 'name'],
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
			id: 'ordercreate_vehicle_id',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			fieldLabel: '车牌号码',
			name: 'vehicleId',
		    xtype: 'combo',
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        editable: false,
		    displayField: 'vehicleNumber',
		    valueField: 'id',
		    listeners:{
		    	expand:'selectVehicle',
		    	select: 'selectVehicleDone'
			}
      	},
      	{
			id: 'ordercreate_seatNumber_id',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			fieldLabel: '座位数',
			name: 'seatNumber',
		    xtype: 'textfield',
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        editable: false,
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
			id: 'ordercreate_driver_id',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			fieldLabel: '司机',
			name: 'driver',
		    xtype: 'combo',
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        editable: false,
		    displayField: 'realname',
		    valueField: 'id',
		    listeners:{
//				afterrender:'selectDriver',
				select: 'selectDriverDone',
		    	expand: 'selectDriver'
			}
      	},
      	{
			id: 'ordercreate_driverPhone_id',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			fieldLabel: '司机手机号',
			name: 'driverPhone',
		    xtype: 'textfield',
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        editable: false,
      	},
      	{
      		id: 'ordercreate_driverId_id',
			fieldLabel: '司机Id',
		    xtype: 'textfield',
		    name: 'driverId',
		    hidden: true
		},
		{
			id: 'ordercreate_driverName_id',
			fieldLabel: '司机姓名',
		    xtype: 'textfield',
		    name: 'driverName',
		    hidden: true
		},
		{
			name: 'comments',
			fieldLabel: '用车备注',
		    xtype: 'textarea',
	        maxLength: 40,
	        msgTarget: 'side',
	        hidden: true
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