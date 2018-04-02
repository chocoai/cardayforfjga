Ext.define('Admin.model.vehiclemgmt.vehicleInfomgmt.VehicleGeofence', {
    fields: [],
    extend: 'Ext.data.Model',

    proxy:{
    	type: 'ajax',
        url: 'vehicle/findVehicleAvialiableMarkers',
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
      	direction:'DESC',
        property: 'id'
    }
});