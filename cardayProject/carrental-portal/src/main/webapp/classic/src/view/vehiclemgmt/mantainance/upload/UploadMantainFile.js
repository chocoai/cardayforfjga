Ext.define('Admin.view.vehiclemgmt.mantainance.upload.UploadMantainFile', {
	extend : 'Ext.window.Window',
	alias: "widget.uploadMantainFile",
	requires : ['Ext.form.Panel'],
//	reference : 'vehicleAllocation',
//	bodyPadding : 10,
 //   frame: true,
	controller: 'mantainanceUploadController',
	constrain : true,
	closable : true,
	resizable : false,
	ghost : false,
    modal: true,
    title:'批量导入',
    fileUpload:true,  
	resizable : false,// 窗口大小是否可以改变
	layout : 'fit',
	items : [{ 
			xtype : 'form',
			fieldDefaults: {
       			// msgTarget: Ext.supports.Touch ? 'side' : 'qtip'
       			 msgTarget: 'side'
    		},
			width: 400,
			bodyPadding: 10,
			frame: true,
			items : [{
				 xtype: 'filefield',
				 name: 'file',
				 fieldLabel: '文件上传',
				 labelStyle : 'color:red;',
				 labelWidth: 80,
				 allowBlank: false,
				 anchor: '100%',
				 emptyText:'请选择csv/xls格式的文件',
				 buttonText: '请选择...',
//				 regex:/\.csv$/,
				 regex:/\.(csv|xls)$/,
                 regexText :'文件格式不正确'
			},{  
	        	xtype: 'label',  
	            name : 'templateLink',
	            html: "<a href='./maintenance/loadTemplate'>模板下载</a>"  
       		}],
       		dockedItems: [{
				xtype: 'toolbar',
				dock: 'bottom',
				items: ['->', {
					xtype: 'button',
					text: '<i class="fa fa-download"></i>&nbsp;导入',
			//		glyph: 0xf0c7,
					formBind: true,
					handler: 'uploadCSV'
				}, {
					xtype: 'button',
					text: '<i class="fa fa-close"></i>&nbsp;关闭',
				//	glyph: 0xf057,
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