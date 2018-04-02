Ext.define('Admin.store.alertMgmt.overSpeedAlarm.AlarmInfo', {
    extend: 'Ext.data.Store',
    alias: 'store.alarmInfo',

    model: 'Admin.model.alertMgmt.overSpeedAlarm.OverSpeedVehicle',

    proxy:{
    	type: 'ajax',
//      url: 'app/data/alertMgmt/outMarkerAlarm/OutMarkerVehicle.json',
        url: 'vehicleAlert/findVehicleAlertByPage',
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
    /*sorters: {
        direction: 'DESC',
        property: 'carNo'
    }*/
});