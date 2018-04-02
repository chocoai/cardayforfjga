Ext.define('Admin.view.orgmgmt.dialrecordmgmt.AddDialRecord',{
	extend: 'Ext.window.Window',	
    alias: "widget.addDialRecord",
	title : '新增来电客户记录',
	requires: [
        'Admin.view.orgmgmt.dialrecordmgmt.AdvancedVType'
    ],
	controller: {
        xclass: 'Admin.view.orgmgmt.dialrecordmgmt.ViewController'
    },
	
	id: 'addDialRecord',
	width: 480,
    layout: 'fit',
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	frame : true,

	items:[{
		xtype:'form',
		reference: 'addDialRecord',
		layout: {
            type: 'vbox', 
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        fieldDefaults: {
            msgTarget: 'side'
        },	
		items:[{
			fieldLabel: '<span style="color:red;">*</span>来电号码',
			xtype: 'textfield',
			itemId: 'dialPhone',
	        name: 'dialPhone',
	        //maskRe: /[\d\-]/,
	        msgTarget: 'side',           
            regex:/[1][3578]\d{9}$/,
            regexText: '你输入的手机号码有误，请输入11位数字的手机号码 ',
	        allowBlank:false,
	        listeners:{
	        	change: 'onChangeForDialPhone'
		    },
		},{
			fieldLabel: '<span style="color:red;">*</span>来电人姓名',
			xtype: 'textfield',
	        name: 'dialName',
	        //readOnly: true,
	        allowBlank:false
		},{
			fieldLabel: '<span style="color:red;">*</span>来电人单位',
			xtype: 'textfield',
	        name: 'dialOrganization',
	        //readOnly: true,
	        allowBlank:false
		},{
			xtype:'fieldcontainer',
			layout: {
	            type: 'hbox', 
	           // align: 'stretch',
	        },
	        items:[
		        {
		        	id: 'add_call_time_id',
		        	fieldLabel: '<span style="color:red;">*</span>来电时间',
					xtype: 'datefield',
			        name: 'dialDate',
			        format:'Y-m-d',
			        editable:false,
			        //itemId: 'startdt',
			        //vtype: 'daterange',
			        allowBlank:false,
			        maxValue:new Date(),
			        value:new Date(),
			        listeners:{
			        	select: 'selectCallTime'
			    		} 
			    },{
			    	xtype: 'displayfield',
			    	value: '日',
			    },
			    {
			    	id: 'call_hour_id',
			    	xtype: 'combo',
			    	editable: false,
			    	width: 70,
			    	value: '00',
			    	displayField: 'time',
			    	name: 'hour',
			    	displayField: 'time',
			    	valueField: 'id',
			    	listeners:{
				        	select: 'selectHourTime'
				    },
			    },
			   {
			    	xtype: 'displayfield',
			    	value: '时',
			    },
			    {
			    	id: 'call_minute_id',
			    	afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			    	allowBlank: false,//不允许为空
			    	blankText: '不能为空',//提示信息
			    	msgTarget: 'side',
			    	xtype: 'numberfield',
			    	width: 70,
			    	//value: 0,
			    	minValue: 0,
			    	name: 'minute',
			    },  
			    {
			    	xtype: 'displayfield',
			    	value: '分',
			    }]

		},{
			fieldLabel: '<span style="color:red;">*</span>来电类型',
			xtype: 'combo',
	        name: 'dialType',
		    displayField: 'name',
            valueField: 'name',
            queryMode : 'local',
            editable : false,
            value:'报障类',
            store : {
                fields : ['name'],
                data : [{
                            "name" : "报障类",
                        }, {
                            "name" : "投诉类",
                        }, {
                            "name" : "其他",
                        }]
            }
		},{
			fieldLabel: '<span style="color:red;">*</span>来电内容',
	        xtype:'textfield',
	        name:'dialContent',
	        allowBlank:false
		},{
			fieldLabel: '&nbsp;&nbsp;行程订单号',
			xtype: 'textfield',
	        name: 'orderNo',
	        listeners:{
		    	blur: 'onBlurFororderNo'
		    },
		},{
			fieldLabel: '&nbsp;&nbsp;车牌号',
			xtype: 'textfield',
	        name: 'vehicleNumber',
	        //readOnly: true,
		},{
			fieldLabel: '&nbsp;&nbsp;终端设备号',
			xtype: 'textfield',
	        name: 'deviceNo',
	        //readOnly: true,
		},{
			fieldLabel: '<span style="color:red;">*</span>处理结果',
			xtype: 'combo',
	        name: 'dealResult',
	        displayField: 'name',
            valueField: 'name',
            queryMode : 'local',
            editable : false,
            value:'未核实',
            store : {
                fields : ['name'],
                data : [{
                            "name" : "未核实",
                        }, {
                            "name" : "已核实",
                        }, {
                            "name" : "已处理",
                        }, {
                            "name" : "已回访",
                        }]
            }
		}
		],
      	buttonAlign : 'center',
	    buttons : [{
				text : '确定',
				disabled : true,
                formBind : true,
				handler:'addDialRecord'
			},{
				text: '取消',
				handler: function(btn){
					btn.up('window').close();
				}
			}]
	}
	],
});