Ext.define('Admin.view.vehiclemgmt.authorizedmgmt.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.authorizedmodel',
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

        authorizedVehicleResults: {
            model: 'Admin.model.vehiclemgmt.authorizedmgmt.AuthorizedVehicleResults',
            remoteFilter: true,
            autoLoad: false,
            pageSize: 10,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
           /* listeners: {
                beforeload: 'onBeforeLoad'
            }*/
        },
        authorizedApplyResults: {
            model: 'Admin.model.vehiclemgmt.authorizedmgmt.authorizedApplyResults',
            remoteFilter: true,
            autoLoad: false,
            pageSize: 10,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
           /* listeners: {
                beforeload: 'onBeforeLoad'
            }*/
        },
        
        authorizedApplyRefuseResults: {
            model: 'Admin.model.vehiclemgmt.authorizedmgmt.authorizedApplyRefuseResults',
            remoteFilter: true,
            autoLoad: false,
            pageSize: 10,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
           /* listeners: {
                beforeload: 'onBeforeLoad'
            }*/
        },
        
    }
});