Ext.define('Admin.model.rulemgmt.ruleinfomgmt.RuleInfo', {
    fields: [],
    extend: 'Ext.data.Model',

    proxy:{
    	type: 'ajax',
        url: 'rule/ruleList',
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
        property: 'ruleId'
    }
});