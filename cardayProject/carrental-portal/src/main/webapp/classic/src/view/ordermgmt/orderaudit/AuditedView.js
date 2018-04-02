/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.ordermgmt.orderaudit.AuditedView', {
    extend: 'Ext.panel.Panel',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
    ],
    
    controller: 'orderauditedcontroller',
    viewModel: {
        xclass: 'Admin.view.ordermgmt.orderaudit.OrderViewModel'
    },
    listeners:{
        afterrender: 'onSearchClick',
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
        xclass: 'Admin.view.ordermgmt.orderaudit.SearchFormAudited',
    }, {
        xclass: 'Admin.view.ordermgmt.orderaudit.GridOrderAudited',
        frame: true
    }],
    initComponent: function() {
        this.callParent();
    }
});
