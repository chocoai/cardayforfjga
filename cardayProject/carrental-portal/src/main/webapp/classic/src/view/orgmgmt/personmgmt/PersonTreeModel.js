Ext.define('Admin.view.orgmgmt.personmgmt.PersonTreeModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.personTreeModel',

    requires: [
        'Ext.data.Store',
        'Ext.data.proxy.Memory',
        'Ext.data.field.Integer',
        'Ext.data.field.String',
        'Ext.data.field.Date',
        'Ext.data.field.Boolean',
        'Ext.data.reader.Json'
    ],

//    stores: {
//        personResults: {
//            type: 'personTreeStore'   //app/store/search/Users.js
//        }
//    }
});
