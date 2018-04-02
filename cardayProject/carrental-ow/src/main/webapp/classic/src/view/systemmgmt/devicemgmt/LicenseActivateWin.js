Ext.define('Admin.view.systemmgmt.devicemgmt.LicenseActivateWin', {
	extend: 'Ext.window.Window',
	       
    alias: "widget.licenseActivateWin",
    id: 'licenseActivateWin_id',
    controller: 'devicemgmtcontroller',
	reference: 'LicenseActivateWin',
	title : '服务时间选择',
	width : 400,
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
	frame : true,
	items : [{
		xtype:'form',
		//layout : 'form',
		layout: {
            type: 'vbox', 
            //type: 'form', //form，字段验证时，整体都会改变
            align: 'stretch'
        },
		bodyStyle : "background-color:#FFF0F5",
		items: [ 
		{
			id: 'active_deviceId_id',
			fieldLabel: '设备Id',
			xtype: 'displayfield',
		    name: 'id',
		    hidden: true
		},
    	{
    		id: 'active_licenseNo_id',
    		fieldLabel: 'license编号',
    		xtype: 'displayfield',
            name: 'licenseNumber',
            hidden: true
    	},
    	{
 			id: 'license_start_time_id',
 	        xtype: 'datefield',
 	        editable:false,
 	        fieldLabel: '服务开始时间',
 	        width:180,
 	        labelWidth: 90,
 	        name: 'startDate',
 	        //format: 'Y-m-d H:i:s',
 	        format: 'Y-m-d',
 	        minValue: new Date(),
 	        itemId: 'startdt',
	        vtype: 'daterange',
	        endDateField: 'enddt'
 	    },
     	    {
 	    	id: 'license_end_time_id',
 	        xtype: 'datefield',
 	        editable:false,
 	        fieldLabel: '服务结束时间',
 	        width:180,
 	        labelWidth: 90,
 	        name: 'endDate',
 	        //format: 'Y-m-d H:i:s',
 	        format: 'Y-m-d',
 	        itemId: 'enddt',
 	        vtype: 'daterange',
 	        startDateField: 'startdt'
 	    }
 	    ]}
	],

	buttonAlign : 'center',
	buttons : [{
				text : '确定',
				handler: 'licenseActiveDone',
			},{
				text: '取消',
				handler: function(btn){
					btn.up("licenseActivateWin").close();
				}
			} ]
});