Ext.define('Admin.view.systemmgmt.driverAllowanceSetting.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'allowanceConfig',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
    ],
    controller: {
        xclass: 'Admin.view.systemmgmt.driverAllowanceSetting.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.systemmgmt.driverAllowanceSetting.ViewModel'
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
        xclass: 'Admin.view.systemmgmt.driverAllowanceSetting.AllowanceConfigGrid',
        frame: true
    }],
    initComponent: function() {
        this.callParent();
    }
});
