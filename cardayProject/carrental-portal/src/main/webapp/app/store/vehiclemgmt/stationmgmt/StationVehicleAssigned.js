Ext.define('Admin.store.vehiclemgmt.stationmgmt.StationVehicleAssigned', {
    extend: 'Ext.data.Store',

    alias: 'store.stationVehicleAssigned',

    model: 'Admin.model.vehiclemgmt.stationmgmt.StationVehicle',

    proxy:{
    	type: 'ajax',
    	url: 'station/findStationAssignedVehicles',
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