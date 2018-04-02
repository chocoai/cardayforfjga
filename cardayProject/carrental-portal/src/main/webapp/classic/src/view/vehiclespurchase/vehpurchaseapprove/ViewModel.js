Ext.define('Admin.view.vehiclespurchase.vehpurchaseapprove.ViewModel', {
    extend: 'Ext.app.ViewModel',
    //alias: 'viewmodel.vehicleInfoModel',
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
        vehiPurchaseApprovingResults: {
            model: 'Admin.model.vehiclespurchase.vehpurchaseapprove.VehiPurchaseApprovingModel',
            remoteFilter: true,
            autoLoad: false,
            pageSize: 10,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
        },
        vehiPurchaseApprovedResults: {
            model: 'Admin.model.vehiclespurchase.vehpurchaseapprove.VehiPurchaseApprovedModel',
            remoteFilter: true,
            autoLoad: false,
            pageSize: 10,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
        },

        vehiPurchaseTypeResults: {
            model: 'Admin.model.vehiclespurchase.vehipurchaseapply.VehiPurchaseTypeModel',
            remoteFilter: true,
            autoLoad: false,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
        },
        vehiPurchaseInfoResults: {
            model: 'Admin.model.vehiclespurchase.vehipurchaseapply.VehiPurchaseInfoModel',
            remoteFilter: true,
            autoLoad: false,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
        },
    }
});