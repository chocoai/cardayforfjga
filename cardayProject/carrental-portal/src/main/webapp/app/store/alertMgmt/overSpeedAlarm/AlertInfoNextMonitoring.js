Ext.define('Admin.store.alertMgmt.overSpeedAlarm.AlertInfoNextMonitoring', {
    extend: 'Ext.data.Store',
    alias: 'store.alertInfoNextMonitoring',

    model: 'Admin.model.alertMgmt.overSpeedAlarm.OverSpeedVehicle',

    proxy:{
    	type: 'ajax',
//      url: 'app/data/alertMgmt/outMarkerAlarm/OutMarkerVehicle.json',
        url: 'vehicleAlert/findVehicleAlert',
      	actionMethods: {
                create : 'POST',
                read   : 'POST', // by default GET
                update : 'POST',
                destroy: 'POST'
    	},
         reader: {
             type: 'json',
             rootProperty: 'data',
             successProperty: 'status',
         }
    },
    autoLoad: false,
    sorters: {
        direction:'DESC',
        property: 'alertTime'
    }
});