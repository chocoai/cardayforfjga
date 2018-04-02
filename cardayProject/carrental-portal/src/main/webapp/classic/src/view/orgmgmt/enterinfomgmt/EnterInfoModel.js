Ext.define('Admin.view.orgmgmt.enterinfomgmt.EnterInfoModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.enterInfoModel',

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
            status: window.sessionStorage.getItem('userType')

        },
    stores: {
        usersResults: {
            type: 'enterinfousers',   //app/store/search/Users.js
            remoteFilter: true,
            autoLoad: false
        }
/*        carsResults:{
        	type: 'cars',
        	remoteFilter: true,
            autoLoad: true
        }*/
    }
});
