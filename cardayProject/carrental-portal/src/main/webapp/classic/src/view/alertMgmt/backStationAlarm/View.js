/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.alertMgmt.backStationAlarm.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'backStationMgmt',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Admin.view.alertMgmt.backStationAlarm.SearchForm'
    ],
    controller: {
        xclass: 'Admin.view.alertMgmt.backStationAlarm.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.alertMgmt.backStationAlarm.ViewModel'
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
        xclass: 'Admin.view.alertMgmt.backStationAlarm.SearchForm'
    }, {
        xclass: 'Admin.view.alertMgmt.backStationAlarm.BackStationVehicleGrid',
        frame: true
    }],
    initComponent: function() {
        this.callParent();
    }
});
