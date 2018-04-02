Ext.define('Admin.view.vehiclemgmt.vehicleInfomgmt.FileUpLoad', {
	extend : 'Ext.window.Window',
	requires : ['Ext.form.Panel'],
	constrain : true,
	closable : true,
	resizable : false,
	ghost : false,
    modal: true,
    title:'批量导入',
 //   fileUpload:true,  
	resizable : false,// 窗口大小是否可以改变
	controller : {
		xclass : 'Admin.view.vehiclemgmt.vehicleInfomgmt.ViewController'
	},
	viewModel : {
		xclass : 'Admin.view.vehiclemgmt.vehicleInfomgmt.ViewModel'
	},
	layout : 'fit',
	items : [{ 
			xtype : 'form',
			fieldDefaults: {
       			// msgTarget: Ext.supports.Touch ? 'side' : 'qtip'
       			 msgTarget: 'side'
    		},
			width: 400,
			bodyPadding: 10,
			items : [{
				 xtype: 'filefield',
				 name: 'file',  //与后台中的MultipartFile multiFile = multipartRequest.getFile("file")保持一致
				 fieldLabel: '文件上传',
				 labelStyle : 'color:red;',
				 labelWidth: 80,
				 allowBlank: false,
				 anchor: '100%',
				 emptyText:'请选择csv、xls、xlsx格式的文件',
				 buttonText: '请选择...',
				 regex:/(\.csv$)|(\.xls$)|(\.xlsx$)/,
                 regexText :'文件格式不正确'
			},{  
	        	xtype: 'label',  
	            name : 'templateLink',
	            html: "<a href='resources/template/vehicle/template.xls'>模板下载</a>"  
       		}],
       		dockedItems: [{
				xtype: 'toolbar',
				dock: 'bottom',
				items: ['->', {
					xtype: 'button',
					text: '导入',
					formBind: true,
					handler: 'uploadCSV'
				}, {
					xtype: 'button',
					text: '关闭',
					handler : function() {
						this.up('window').close();
					}
				}]
       		}]
		}],
   		initComponent: function() {
			this.callParent();
		}
});