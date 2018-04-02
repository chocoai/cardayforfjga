Ext.define('Admin.view.alertMgmt.outMarkerAlarm.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.outMarkerModel',

    stores: {
    	outMarkerResults: {
 			type: 'outMarkerVehicle',   //app/store/search/Users.js
            remoteFilter: true,
            autoLoad: false,
            pageSize: 10,
            listeners: {
                beforeload: 'onBeforeLoad'
            }
        }
    }
});
