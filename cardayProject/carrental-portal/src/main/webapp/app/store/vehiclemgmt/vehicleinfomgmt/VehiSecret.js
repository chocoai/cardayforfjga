Ext.define('Admin.store.vehiclemgmt.VehiSecret', {
    extend: 'Ext.data.Store',

    alias: 'store.vehiSecret',

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