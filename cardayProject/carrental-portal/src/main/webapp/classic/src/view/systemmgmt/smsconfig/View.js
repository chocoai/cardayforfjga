Ext.define('Admin.view.systemmgmt.smsconfig.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'smsConfig',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
    ],
    controller: {
        xclass: 'Admin.view.systemmgmt.smsconfig.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.systemmgmt.smsconfig.ViewModel'
    },
    listeners:{
        afterrender: 'onAfterrender'
    },
    
    scrollable: true,
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [ {
        xclass: 'Admin.view.systemmgmt.smsconfig.smsConfigGrid',
        frame: true
    }],
    initComponent: function() {
        this.callParent();
    }
});
