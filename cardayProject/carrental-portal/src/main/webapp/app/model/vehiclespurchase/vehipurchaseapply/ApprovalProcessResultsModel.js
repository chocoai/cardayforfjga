Ext.define('Admin.model.vehiclespurchase.vehipurchaseapply.ApprovalProcessResultsModel', {
    fields: [],
    extend: 'Ext.data.Model',

    proxy:{
        type: 'ajax',
        //url: 'vehicle/findVehicleAvialiableMarkers',
        //url: 'app/data/vehiclespurchase/vehipurchaseapply/ApprovalProcessResultsModel.json',
        actionMethods: {
                    create : 'POST',
                    read   : 'POST', // by default GET
                    update : 'POST',
                    destroy: 'POST'
        },
        reader: {
             type: 'json',
             rootProperty: 'data',
             successProperty: 'status',
        }
    },
    autoLoad: 'false',

    sorters: {
        direction:'DESC',
        property: 'id'
    }
});
