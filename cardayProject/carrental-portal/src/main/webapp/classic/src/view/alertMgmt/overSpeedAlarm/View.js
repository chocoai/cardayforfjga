/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.alertMgmt.overSpeedAlarm.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'overSpeedAlarm',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Admin.view.alertMgmt.overSpeedAlarm.SearchForm',
        'Admin.view.alertMgmt.overSpeedAlarm.OverSpeedVehicleGrid'
    ],
    controller: {
        xclass: 'Admin.view.alertMgmt.overSpeedAlarm.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.alertMgmt.overSpeedAlarm.ViewModel'
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
     bodyPadding:20,
     defaults: {
        collapsible: false,
        margin: '0 0 3 0'
     },
    items: [{
        xclass: 'Admin.view.alertMgmt.overSpeedAlarm.SearchForm'
    }, {
        xclass: 'Admin.view.alertMgmt.overSpeedAlarm.OverSpeedVehicleGrid',
        frame: true
    }],
    initComponent: function() {
        this.callParent();
    }
});
