Ext.define('Admin.view.vehiclemgmt.vehiclemaintain.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.vehiclemaintainModel',
    requires: [
        'Ext.data.Store',
        'Ext.data.proxy.Memory',
        'Ext.data.field.Integer',
        'Ext.data.field.String',
        'Ext.data.field.Date',
        'Ext.data.field.Boolean',
        'Ext.data.reader.Json'
    ],
	data: {
        },
    stores: {
        vehicleMaintainResults: {
            model: 'Admin.model.vehiclemgmt.vehiclemaintain.VehicleMaintainModel',
            remoteFilter: true,
            autoLoad: false,
            pageSize: 5,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'onBeforeLoad'
            }
        },
    }
});