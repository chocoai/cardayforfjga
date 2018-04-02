Ext.define('Admin.view.orgmgmt.arcmgmt.EmployeeModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.employeeModel',

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
    	employeeStore: {
            	 type: 'departmentPerson',   //app/store/search/Users.js
                 remoteFilter: true,
                 autoLoad: false
        }
    }
});
