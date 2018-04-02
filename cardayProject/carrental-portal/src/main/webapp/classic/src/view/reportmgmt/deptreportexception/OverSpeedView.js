/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.reportmgmt.deptreportexception.OverSpeedView', {
    extend: 'Ext.panel.Panel',

    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
    ],
    
    controller: 'deptreportexceptioncontroller',
    viewModel: {
        xclass: 'Admin.view.reportmgmt.deptreportexception.ViewModel'
    },
    listeners:{
    	afterrender: 'onBeforeLoadOverSpeed'
    },
    
    autoScroll: true,
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [
	    {
	        xclass: 'Admin.view.reportmgmt.deptreportexception.OverSpeedSearchForm',
	    },
	    {
	        xclass: 'Admin.view.reportmgmt.deptreportexception.OverSpeedStatistics',
	        height:500,
	    },
	    {
	        xclass: 'Admin.view.reportmgmt.deptreportexception.GridOverSpeed',
	    }
    ],
    
    initComponent: function() {
        this.callParent();
    }
});
