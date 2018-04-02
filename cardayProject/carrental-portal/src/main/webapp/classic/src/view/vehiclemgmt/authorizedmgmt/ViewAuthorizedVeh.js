Ext.define('Admin.view.vehiclemgmt.authorizedmgmt.ViewAuthorizedVeh', {
	extend: 'Ext.window.Window',
	
    alias: "widget.viewauthorizedveh",
    controller: {
        xclass: 'Admin.view.vehiclemgmt.authorizedmgmt.ViewController'
    },
    id:'viewauthorizedveh',
	reference: 'viewauthorizedveh',
	title : '查看编制车辆信息',
	width : 480,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	frame : true,
	items : [{
		xtype:'form',
		margin:'0 20 0 20',
		layout : 'vbox',
		defaultType: 'displayfield',
		fieldDefaults: {
            labelWidth: 200,
        },       
		items: [
			{
				fieldLabel: '单位名称',
			    name: 'deptName',
			}, 
			{
				fieldLabel: '级别',
		        name: 'levelType'
			},
			 {
	      		fieldLabel: '应急机要通信接待用车编制数',
	            name: 'emergencyVehNum',
	        },
	        {
				fieldLabel: '行政执法用车编制数',
		        name: 'enforcementVehNum',
			}, 
			{
				fieldLabel: '行政执法特种专业用车编制数',
		        name: 'specialVehNum',
			},
			{
				fieldLabel: "一般执法执勤用车编制数",
		        name: 'normalVehNum'
		    },
		    {
				fieldLabel: "执法执勤特种专业用车编制数",
		        name: 'majorVehNum',
		    },			
		]
	}],

	buttonAlign : 'center',
	buttons : [{
				text : '关闭',
				handler: function(btn){
					btn.up("viewauthorizedveh").close();
				}
			}]
});