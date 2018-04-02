/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.systemmgmt.rolemgmt.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'rolemgmt',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
        'Admin.view.systemmgmt.rolemgmt.SearchForm',
    ],
    
    controller: {
        xclass: 'Admin.view.systemmgmt.rolemgmt.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.systemmgmt.rolemgmt.RoleModel'
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
        xclass: 'Admin.view.systemmgmt.rolemgmt.SearchForm',
    }, {
        xclass: 'Admin.view.systemmgmt.rolemgmt.GridRole',
        frame: true
    }],
    initComponent: function() {
        this.callParent();
    }
});
