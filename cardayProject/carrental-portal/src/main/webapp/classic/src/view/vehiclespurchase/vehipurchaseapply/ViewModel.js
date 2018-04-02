Ext.define('Admin.view.vehiclespurchase.vehipurchaseapply.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.vehiPurchaseApplyModel',
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
        vehiPurchaseApplyResults: {
            model: 'Admin.model.vehiclespurchase.vehipurchaseapply.VehiPurchaseApplyModel',
            remoteFilter: true,
            autoLoad: false,
            pageSize: 10,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
        },
        vehiPurchaseRefuseResults: {
            model: 'Admin.model.vehiclespurchase.vehipurchaseapply.VehiPurchaseRefuseModel',
            remoteFilter: true,
            autoLoad: false,
            pageSize: 10,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
        },
        vehiPurchaseAuditedResults: {
            model: 'Admin.model.vehiclespurchase.vehipurchaseapply.VehiPurchaseAuditedModel',
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
        vehiPurchaseInfoResultsAdd: {
            model: 'Admin.model.vehiclespurchase.vehipurchaseapply.VehiPurchaseInfoAddModel',
            remoteFilter: true,
            autoLoad: false,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
        },
        vehiPurchaseTypeAddResults: {
            model: 'Admin.model.vehiclespurchase.vehipurchaseapply.VehiPurchaseTypeAddModel',
            remoteFilter: true,
            autoLoad: false,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
        },
        approvalProcessResults: {
            model: 'Admin.model.vehiclespurchase.vehipurchaseapply.ApprovalProcessResultsModel',
            remoteFilter: true,
            autoLoad: false,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
        },
    }
});