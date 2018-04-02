Ext.define('Admin.view.alertMgmt.alarmConfig.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'alarmConfig',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
    ],
    controller: {
        xclass: 'Admin.view.alertMgmt.alarmConfig.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.alertMgmt.alarmConfig.ViewModel'
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
        xclass: 'Admin.view.alertMgmt.alarmConfig.AlarmConfigGrid',
        frame: true
    }],
    initComponent: function() {
        this.callParent();
    }
});
