Ext.define('Admin.store.alertMgmt.overSpeedAlarm.OverSpeedVehicle', {
    extend: 'Ext.data.Store',

    alias: 'store.overSpeedVehicle',

    model: 'Admin.model.alertMgmt.overSpeedAlarm.OverSpeedVehicle',

    proxy:{
    	type: 'ajax',
     //   url: 'app/data/alertMgmt/overSpeedAlarm/OverSpeedVehicle.json',
//         url: 'vehicleAlert/findOverspeedAlert',
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
    
/*    sorters: {
        direction: 'DESC',
        property: 'id'
    }*/
});