Ext.define('Admin.store.enterinfomgmt.Users', {
    extend: 'Ext.data.Store',

    alias: 'store.enterinfousers',

    model: 'Admin.model.enterinfo.User',

    proxy:{
    	type: 'ajax',
        url: 'organization/list',
//      pageSize:5,
        reader: {
             type: 'json',
             rootProperty: 'data',
             successProperty: 'status'
//           totalProperty:14
        }
    },
    autoLoad: 'false',

    sorters: {
    	direction:'DESC',
        property: 'id'
    }
});
