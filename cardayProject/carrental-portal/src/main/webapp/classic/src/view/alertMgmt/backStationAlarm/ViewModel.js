Ext.define('Admin.view.alertMgmt.backStationAlarm.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.backStationModel',

    stores: {
    	backStationResults: {
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
