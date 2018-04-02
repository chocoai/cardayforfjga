Ext.define('Admin.view.systemmgmt.drivermgmt.ViewDriver', {
	extend: 'Ext.window.Window',
	
    alias: "widget.viewdriver",
    controller: 'drivermgmtcontroller',
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
		            //查看那里改为企业
		        	if (value == null || value == '') {
//		        		return value = '暂未分配'
		        		var organizationName = window.sessionStorage.getItem('organizationName');
	                	var userCategory = window.sessionStorage.getItem('userType');	
	                	if (userCategory==2 || userCategory==6) {
	                		value = organizationName;
	                	}
	                	return value;
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
		    {
		    	fieldLabel: "司机状态",
		    	name: 'drvStatus',
		        renderer : function(value) {
	                switch(value){
	                    case 0:
	                        return '短途出车';
	                    case 1:
	                        return '长途出车';
	                    case 2:
	                        return '在岗';
	                    case 3:
	                        return '值班锁定';
	                    case 4: 
	                        return '补假/休假';
	                    case 5:
	                        return '计划锁定';
	                    case 6:
	                        return '出场';
	                    case 7:
	                        return '下班';
	                }
		        }
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