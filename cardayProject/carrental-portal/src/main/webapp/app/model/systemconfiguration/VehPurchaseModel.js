Ext.define('Admin.model.systemconfiguration.VehPurchaseModel', {
    fields: [],
    extend: 'Ext.data.Model',

    proxy:{
        type: 'ajax',
        //url: 'vehicle/findVehicleAvialiableMarkers',
        url: 'app/data/systemconfiguration/vehPurchaseResults.json',
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
