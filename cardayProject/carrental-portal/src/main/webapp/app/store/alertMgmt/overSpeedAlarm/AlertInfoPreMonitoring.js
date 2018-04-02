Ext.define('Admin.store.alertMgmt.overSpeedAlarm.AlertInfoPreMonitoring', {
    extend: 'Ext.data.Store',
    alias: 'store.alertInfoPreMonitoring',

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