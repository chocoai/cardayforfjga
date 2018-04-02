Ext.define('Admin.model.systemmgmt.empmgmt.EmpRuleAssigned', {
    fields: [],
    extend: 'Ext.data.Model',

    proxy:{
    	type: 'ajax',
        url: 'rule/ruleBindingList',
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