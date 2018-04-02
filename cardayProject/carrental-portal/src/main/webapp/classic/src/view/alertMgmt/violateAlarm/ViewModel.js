Ext.define('Admin.view.alertMgmt.violateAlarm.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.violateModel',

    stores: {
    	violateResults: {
 			type: 'backStationVehicle',   //app/store/search/Users.js
            remoteFilter: true,
            autoLoad: false,
            pageSize: 10,
            listeners: {
                beforeload: 'onBeforeLoad'
            }
        }
    }
});
