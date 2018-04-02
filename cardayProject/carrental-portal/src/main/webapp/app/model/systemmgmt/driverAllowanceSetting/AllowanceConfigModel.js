Ext.define('Admin.model.systemmgmt.driverAllowanceSetting.AllowanceConfigModel', {
    fields: [],
    extend: 'Ext.data.Model',

    proxy:{
    	type: 'ajax',
        url: 'allowance/list',
        actionMethods: {
                    create : 'GET',
                    read   : 'GET', // by default GET
                    update : 'GET',
                    destroy: 'GET'
        },
        reader: {
             type: 'json',
             rootProperty: 'data',
             successProperty: 'status',
        }
    },
    autoLoad: 'false',
});