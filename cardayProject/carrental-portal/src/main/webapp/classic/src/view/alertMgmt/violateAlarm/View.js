/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.alertMgmt.violateAlarm.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'violateAlarm',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Admin.view.alertMgmt.violateAlarm.SearchForm'
    ],
    controller: {
        xclass: 'Admin.view.alertMgmt.violateAlarm.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.alertMgmt.violateAlarm.ViewModel'
    },
    listeners:{
        afterrender: 'onSearchClick'
    },
    
    scrollable: true,
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [{
        xclass: 'Admin.view.alertMgmt.violateAlarm.SearchForm'
    }, {
        xclass: 'Admin.view.alertMgmt.violateAlarm.ViolateVehicleGrid',
        frame: true
    }],
    initComponent: function() {
        this.callParent();
    }
});
