Ext.define('Admin.view.vehiclemgmt.stationmgmt.StationVehicleAssignedModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.stationVehicleAssignedModel',

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
    	stationVehicleAssignedStore: {
            	type: 'stationVehicleAssigned', 
                remoteFilter: true,
                autoLoad: false,
                pageSize: 5,
                sorters: [{
                    property: 'id',
                    direction: 'DESC'
                }],
                listeners: {
                    beforeload: 'loadStationVehicle'
                }
        }
    }
});
