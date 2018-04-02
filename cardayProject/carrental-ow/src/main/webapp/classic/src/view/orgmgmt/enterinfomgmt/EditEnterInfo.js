Ext.define('Admin.view.orgmgmt.enterinfomgmt.EditEnterInfo',{
	extend: 'Ext.window.Window',	
    alias: "widget.editEnterInfo",
	title : '编辑用车企业信息',
	requires: [
        'Admin.view.orgmgmt.enterinfomgmt.AdvancedVType'
    ],
	controller: {
        xclass: 'Admin.view.orgmgmt.enterinfomgmt.ViewController'
    },
	
	id:'editEnterInfo',
	width: 400,
    layout: 'fit',
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	frame : true,
	startTime: '',
	endTime:'',

	items:[{
		xtype:'form',
		reference: 'editEnterInfo',
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
			fieldLabel: '<span style="color:red;">*</span>企业ID',
			xtype: 'textfield',
			itemId: 'id',
	        name: 'id',
	        allowBlank:false,
	        hidden:true,
		},{
			fieldLabel: '<span style="color:red;">*</span>企业名称',
			xtype: 'textfield',
			itemId: 'name',
	        name: 'name',
	        allowBlank:false
		},{
			fieldLabel: '&nbsp;&nbsp;企业简称',
			xtype: 'combo',
	        name: 'shortname',
		    displayField: 'name',
            valueField: 'value',
            queryMode : 'local',
            editable : false,
            value:'',
            store : {
                fields : ['name','value'],
                data : [
                		{"name" : "歌华","value":"gh"},
                        {"name":"其它","value":""}
                ]
            }
	        
		},{
			fieldLabel: '<span style="color:red;">*</span>企业地址',
			xtype: 'textfield',
	        name: 'address',
	        allowBlank:false
		},{
			fieldLabel: '<span style="color:red;">*</span>状态',
			xtype: 'textfield',
	        name: 'status',
	        allowBlank:false,
	        hidden:true
		},{
	        fieldLabel:'&nbsp;&nbsp;企业介绍',
	        xtype:'textfield',
	        name:'introduction',
	        maxLength:30
		},{
			fieldLabel: '<span style="color:red;">*</span>联系人',
			xtype: 'textfield',
	        name: 'linkman',
	        allowBlank:false
		},{
			fieldLabel: '<span style="color:red;">*</span>联系人电话',
			xtype: 'textfield',
	        name: 'linkmanPhone',
	        msgTarget: 'side',           
            regex:/[1][3578]\d{9}$/,
            regexText: '你输入的手机号码有误，请输入11位数字的手机号码',
	        allowBlank:false
		},{
			fieldLabel: '&nbsp;&nbsp;联系人邮箱',
			xtype: 'textfield',
	        name: 'linkmanEmail',
	        vtype: 'email'
		},{
			fieldLabel: '<span style="color:red;">*</span>服务开始日期',
			id:'startTimeEdit',
			xtype: 'datefield',
	        name: 'startTime',
	        format:'Y-m-d',
	        editable:false,
	        itemId: 'startdt',
	        vtype: 'daterange',
            endDateField: 'enddt',
	        allowBlank:false
		},{
			fieldLabel: '<span style="color:red;">*</span>服务结束日期',
			id:'endTimeEdit',
			xtype: 'datefield',
	        name: 'endTime',
	        format:'Y-m-d',
	        itemId: 'enddt',
	        editable:false,
	        vtype: 'daterange',
            startDateField: 'startdt', // id of the start date field
	        allowBlank:false,
		},{
			xtype: 'checkboxgroup',
			id:'businessTypeCheckGroupEdit',
	        fieldLabel: '<span style="color:red;">*</span>业务类型',
	        columns: 2,
	        vertical: true,
	        items: [
	            { boxLabel: '自有车',name: 'businessType',inputValue: '自有车'},
	            { boxLabel: '长租车',name: 'businessType' ,inputValue: '长租车'},
	        ],
	        allowBlank:false,
	        blankText:'请至少选择一项'
		}
		],
      	buttonAlign : 'center',
	    buttons : [{
				text : '确定',				
				disabled : true,
                formBind : true,
				handler:'updateEnterInfo'
			},{
				text: '取消',
				handler: function(btn){
					btn.up('window').close();
				}
			}]
	}
	],
});