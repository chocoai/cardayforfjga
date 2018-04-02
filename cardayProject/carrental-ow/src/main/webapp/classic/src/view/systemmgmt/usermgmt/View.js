/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.systemmgmt.usermgmt.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'usermgmt',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
        'Admin.view.systemmgmt.usermgmt.SearchForm',
        'Admin.view.systemmgmt.usermgmt.GridUser',
    ],
    
    controller: {
        xclass: 'Admin.view.systemmgmt.usermgmt.ViewController'
    },
    viewModel: {
    	xclass: 'Admin.view.systemmgmt.usermgmt.UserModel'
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
        xclass: 'Admin.view.systemmgmt.usermgmt.SearchForm',
    }, {
        xclass: 'Admin.view.systemmgmt.usermgmt.GridUser',
        frame: true
    }],
    initComponent: function() {
        this.callParent();
    }
});
