/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.ordermgmt.orderallocate.AllocatedCarView', {
    extend: 'Ext.panel.Panel',
    xtype: 'allocatedCarView',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
        'Admin.view.ordermgmt.orderallocate.AllocatedViewController',
        'Admin.view.ordermgmt.orderallocate.AllocateModel',
    ],
    
    controller: {
        xclass: 'Admin.view.ordermgmt.orderallocate.AllocatedViewController'
    },
    viewModel: {
    	xclass: 'Admin.view.ordermgmt.orderallocate.AllocateModel'
    },
    
    listeners:{
    	afterrender: 'onSearchClick'
    },
    
    autoScroll: true,
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
//    defaults: {
//        frame: true,
//        collapsible: true,
//        margin: '0 0 3 0'
//    },
    items: [{
        xclass: 'Admin.view.ordermgmt.orderallocate.AllocatedSearchForm',
    }, {
        xclass: 'Admin.view.ordermgmt.orderallocate.AllocatedGrid',
        frame: true
    }],
    initComponent: function() {
        this.callParent();
    }
});
