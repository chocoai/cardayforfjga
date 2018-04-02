Ext.define('Admin.store.vehiclemgmt.stationmgmt.StationVehicle', {
    extend: 'Ext.data.Store',

    alias: 'store.stationVehicle',

    model: 'Admin.model.vehiclemgmt.stationmgmt.StationVehicle',

    proxy:{
    	type: 'ajax',
    	url: 'station/findStationAvialiableVehicles',
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
    autoLoad: 'false',
    sorters: {
        direction: 'DESC',
        property: 'vehicleNumber'
    }
});