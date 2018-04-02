Ext.define('Admin.view.orgmgmt.rentalcompanymgmt.ShowRentalInfo', {
	extend: 'Ext.window.Window',
	
    alias: "widget.showRentalInfoRental",
	reference: 'showRentalInfoRental',

    controller: {
        xclass: 'Admin.view.orgmgmt.rentalcompanymgmt.ViewController'
    },

   viewModel : {
        type : 'rentalCompanyModel' 
    },

	title : '关联租车公司',
	width : 830,
	height : 600,
	id: 'showRentalInfoRental',
	 closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
    scrollable: true,
	listeners:{
    	afterrender: 'onAfterrenderShowRentalInfo',
    },
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 10,
    items: [
   			{
        	xclass: 'Admin.view.orgmgmt.rentalcompanymgmt.GridRentalInfo',
        	frame: true
    		}],
    initComponent: function() {
        this.callParent();
    }
});