Ext.define('Admin.view.orgmgmt.rentalcompanymgmt.ShowAuditInfo', {
	extend: 'Ext.window.Window',
	
    alias: "widget.showAuditInfoRental",
	reference: 'showAuditInfoRental',

    controller: {
        xclass: 'Admin.view.orgmgmt.rentalcompanymgmt.ViewController'
    },

   viewModel : {
        type : 'rentalCompanyModel' 
    },

	title : '审核信息',
	width : 830,
	height : 600,
	id: 'showAuditInfoRental',
	 closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
    scrollable: true,
    
	listeners:{
    	afterrender: 'onAfterrenderShowAudit',
    },
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [
   			{
        	xclass: 'Admin.view.orgmgmt.rentalcompanymgmt.GridAuditInfo',
        	frame: true
    		}],
    initComponent: function() {
        this.callParent();
    }
});