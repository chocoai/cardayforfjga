/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.systemmgmt.empmgmt.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'empmgmt',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
        'Admin.view.systemmgmt.empmgmt.SearchForm',
        'Admin.view.systemmgmt.empmgmt.GridEmp',
        'Admin.view.systemmgmt.empmgmt.AddEmp',
    ],
    
    controller: {
        xclass: 'Admin.view.systemmgmt.empmgmt.ViewController'
    },
    viewModel: {
    	xclass: 'Admin.view.systemmgmt.empmgmt.EmpViewModel'
    },
    
    listeners:{
    	afterrender: 'onSearchClick'
    },

    id:'empmgmt',
    
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
        xclass: 'Admin.view.systemmgmt.empmgmt.SearchForm',
    }, {
        xclass: 'Admin.view.systemmgmt.empmgmt.GridEmp',
        frame: true
    }],
    initComponent: function() {
        this.callParent();
    }
});
