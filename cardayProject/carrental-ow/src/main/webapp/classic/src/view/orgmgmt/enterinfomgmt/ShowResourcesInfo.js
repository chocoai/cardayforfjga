Ext.define('Admin.view.orgmgmt.enterinfomgmt.ShowResourcesInfo', {
	extend: 'Ext.window.Window',
	
    alias: "widget.showResourcesInfo",
	reference: 'showResourcesInfo',

    controller: {
        xclass: 'Admin.view.orgmgmt.enterinfomgmt.ViewController'
    },

   viewModel : {
        type : 'enterInfoModel' 
    },

	title : '查看用车企业资源',
	width : 1150,
	height : 700,
	id: 'showResourcesInfo',
	 closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	scrollable: true,
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 10,
    items: [{
        	xclass: 'Admin.view.orgmgmt.enterinfomgmt.ShowResourcesInfoTab',
        	frame: true
    		}],
    initComponent: function() {
        this.callParent();
    }
});