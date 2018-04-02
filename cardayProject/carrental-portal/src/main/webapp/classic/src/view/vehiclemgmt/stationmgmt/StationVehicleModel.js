Ext.define('Admin.view.vehiclemgmt.stationmgmt.StationVehicleModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.stationVehicleModel',

    requires: [
        'Ext.data.Store',
        'Ext.data.proxy.Memory',
        'Ext.data.field.Integer',
        'Ext.data.field.String',
        'Ext.data.field.Date',
        'Ext.data.field.Boolean',
        'Ext.data.reader.Json'
    ],

    stores: {
    	stationVehicleStore: {
            type:'stationVehicle',
            autoLoad: false,
            pageSize: 5,
            remoteFilter: true,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'loadStationAvialiableVehicle',
                load: 'loadCheckAvialiableVehicle',
            }
        },
    }
});
