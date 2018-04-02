Ext.define('Admin.store.vehiclemgmt.Vehicles', {
    extend: 'Ext.data.Store',

    alias: 'store.vehicles',

    model: 'Admin.model.vehiclemgmt.vehicleInfomgmt.Vehicle',

    proxy:{
    	type: 'ajax',
        url: 'vehicle/listVhicle',
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
    autoLoad: 'false'
    /*sorters: {
        direction: 'DESC',
        property: 'carNo'
    }*/
});