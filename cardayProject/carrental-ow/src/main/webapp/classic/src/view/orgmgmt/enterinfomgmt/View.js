Ext.define('Admin.view.enterinfomgmt.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'Enterinfoxx',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date',
        'Admin.view.orgmgmt.enterinfomgmt.SearchForm',
        'Admin.view.orgmgmt.enterinfomgmt.Grid'
    ],
    controller: {
        xclass: 'Admin.view.orgmgmt.enterinfomgmt.ViewController'
    },
    viewModel : {
		type : 'enterInfoModel' 
	},
	listeners:{
    	afterrender: 'onSearchClick'
    },
    
//	autoScroll: true,
//  height: 700,
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
 /*   defaults: {  //对items中的组件生效
        frame: true,
    },*/
    items: [{
	    	xclass: 'Admin.view.orgmgmt.enterinfomgmt.SearchForm'
    		},
   			{
        	xclass: 'Admin.view.orgmgmt.enterinfomgmt.Grid',
        	frame: true
    		}],
    initComponent: function() {
        this.callParent();
    }
});
