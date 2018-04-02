Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.ViewDriverInfo', {
	extend: 'Ext.window.Window',
	
    alias: "widget.viewdriver",
    controller: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.DriverMgmtController'
    },
	reference: 'viewdriver',
	title : '查看司机信息',
	width : 400,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	//bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
	frame : true,
	items : [{
		xtype:'form',
		layout : 'vbox',
		defaultType: 'displayfield',
		bodyStyle: {
			//  background: '#ffc',
			    padding: '15px'
		},
		fieldDefaults: {
            labelAlign: 'left',
            labelWidth: 100,
            height:20
        },
        
		items: [
			{
				fieldLabel: '姓名',
			    name: 'realname',
			}, 
			{
				fieldLabel: '用户名',
		        name: 'username'
			},
			 {
	      		fieldLabel: '登陆密码',
	            name: 'password',
	            hidden: true
	        },
	        {
				fieldLabel: '邮箱',
		        name: 'email',
			}, 
			{
				fieldLabel: '手机号码',
		        name: 'phone',
			},
			{
				fieldLabel: "性别",
		        name: 'sex'
		    },
		    {
				fieldLabel: "出生日期",
		        name: 'birthday',
		    },
			{
				fieldLabel: "所属企业",
		        name: 'organizationName'
		    },
		    {
				fieldLabel: "所属部门",
		        name: 'depName',
		        renderer : function(value) {
		        	if (value == null || value == '') {
		        		return value = '暂未分配'
		        	} else {
		        		return value;
		        	}
		        }
		    },
		    {
				fieldLabel: "所属站点",
		        name: 'stationName',
		        renderer : function(value) {
		        	if (value == null || value == '') {
		        		return value = '暂未分配'
		        	} else {
		        		return value;
		        	}
		        }
		    },
		    {
		    	fieldLabel: "准驾类型",
		    	name: 'licenseType'
		    },
		    {
		    	fieldLabel: "驾照号码",
		    	name: 'licenseNumber'
		    },
		    {
		    	fieldLabel: "初次领证时间",
		    	name: 'licenseBegintime'
		    },
		    {
		    	fieldLabel: "驾龄",
		    	name: 'drivingYears'
		    },
		    {
		    	fieldLabel: "驾照到期时间",
		    	name: 'licenseExpiretime'
		    },
		    {
		    	id: 'view_licenseAttach_id',
		    	fieldLabel: "驾照附件",
		    	//name: 'licenseAttach'
		    	xtype: 'label',  
	            name : 'licenseAttachLink',
		    },
			
		]
	}],

	buttonAlign : 'center',
	buttons : [{
				text : '关闭',
				handler: function(btn){
					btn.up("viewdriver").close();
				}
			}]
});