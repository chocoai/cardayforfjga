Ext.define('Admin.model.systemmgmt.empmgmt.EmpRule', {
    fields: [],
    extend: 'Ext.data.Model',

    proxy:{
    	type: 'ajax',
        url: 'rule/ruleNotBindingList',
        actionMethods: {
                    create : 'POST',
                    read   : 'POST', // by default GET
                    update : 'POST',
                    destroy: 'POST'
        },
        reader: {
             type: 'json',
             rootProperty: 'data',
             successProperty: 'status',
        }
    },
    autoLoad: 'false',

    sorters: {
        direction: 'DESC',
        property: 'id'
    }
});