Ext.define('Admin.model.enterinfo.PayHistory', {
    fields: [],
    extend: 'Ext.data.Model',

    proxy:{
    	type: 'ajax',
        url: 'organization/showCreditHistory',
        actionMethods: {
                    create : 'POST',
                    read   : 'POST', // by default GET
                    update : 'POST',
                    destroy: 'POST'
        },
        reader: {
             type: 'json',
             rootProperty: 'data.resultList',
             successProperty: 'status',
             totalProperty:'data.totalRows'
        }

    },
    autoLoad: 'false',
});