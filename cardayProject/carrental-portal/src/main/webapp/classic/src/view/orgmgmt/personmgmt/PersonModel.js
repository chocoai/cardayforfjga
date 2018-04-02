Ext.define('Admin.view.orgmgmt.personmgmt.PersonModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.personModel',

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
        personResults: {
            type: 'personmgmtReport'   //app/store/search/Users.js
        }
    }
});
