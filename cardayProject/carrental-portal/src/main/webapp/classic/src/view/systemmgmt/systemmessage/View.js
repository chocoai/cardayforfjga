/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.systemmgmt.systemmessage.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'systemmessage',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
        'Admin.view.systemmgmt.systemmessage.SearchForm',
    ],
    
    controller: {
        xclass: 'Admin.view.systemmgmt.systemmessage.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.systemmgmt.systemmessage.ViewModel'
    },
    listeners:{
        afterrender: 'afterrenderView',
    },
    
    autoScroll: true,
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,

    items: [{
        xclass: 'Admin.view.systemmgmt.systemmessage.SearchForm',
    }, {
        xclass: 'Admin.view.systemmgmt.systemmessage.GridMessage',
        frame: true
    }],
    initComponent: function() {
        this.callParent();
    }
});
