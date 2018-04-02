Ext.define('Admin.model.systemmgmt.smsconfig.SmsConfigModel', {
    fields: [],
    extend: 'Ext.data.Model',

    proxy:{
    	type: 'ajax',
        url: 'sms/config/query',
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