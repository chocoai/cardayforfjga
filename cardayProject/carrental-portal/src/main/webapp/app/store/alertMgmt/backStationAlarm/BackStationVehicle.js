Ext.define('Admin.store.alertMgmt.backStationAlarm.BackStationVehicle', {
    extend: 'Ext.data.Store',

    alias: 'store.backStationVehicle',

    model: 'Admin.model.alertMgmt.backStationAlarm.BackStationVehicle',

    proxy:{
    	type: 'ajax',
//        url: 'app/data/alertMgmt/outMarkerAlarm/OutMarkerVehicle.json',
//        url: 'vehicleAlert/findVehiclebackAlert',
        url: 'vehicleAlert/findVehicleAlertInfo',
         actionMethods: {
                    create : 'POST',
                    read   : 'POST', // by default GET
                    update : 'POST',
                    destroy: 'POST'
        },
         reader: {
             type: 'json',
             rootProperty: 'data.resultList',
             successProperty: 'status',
             totalProperty:'data.totalRows'
         }
    },
    autoLoad: false
});