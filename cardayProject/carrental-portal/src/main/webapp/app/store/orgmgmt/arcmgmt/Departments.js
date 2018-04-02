Ext.define('Admin.store.orgmgmt.arcmgmt.Departments', {
    extend: 'Ext.data.Store',

    alias: 'store.departments',

    model: 'Admin.model.arcmgmt.arc',

    proxy:{
    	type: 'ajax',
//        url: 'organization/0/listDirectChildrenById',
        url: 'department/listDirectChildrenWithCount',
//      pageSize:5,
        actionMethods: {
            create : 'POST',
            read   : 'POST', // by default GET
            update : 'POST',
            destroy: 'POST'
        },
        reader: {
             type: 'json',
//             rootProperty: 'data',
             rootProperty: 'data.resultList',
             successProperty: 'status',
             totalProperty:'data.totalRows'
        }
    },
    autoLoad: 'false',

    sorters: [{
        property: 'id',
        direction: 'ASC'
    }, {
        property: 'text',
        direction: 'ASC'
    }],
});
