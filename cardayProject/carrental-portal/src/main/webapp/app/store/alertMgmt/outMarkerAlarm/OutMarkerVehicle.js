Ext.define('Admin.store.alertMgmt.outMarkerAlarm.OutMarkerVehicle', {
    extend: 'Ext.data.Store',

    alias: 'store.outMarkerVehicle',

    model: 'Admin.model.alertMgmt.outMarkerAlarm.OutMarkerVehicle',

    proxy:{
    	type: 'ajax',
//        url: 'app/data/alertMgmt/outMarkerAlarm/OutMarkerVehicle.json',
//        url: 'vehicleAlert/findOutboundAlert',
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