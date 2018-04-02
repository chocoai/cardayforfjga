Ext.define('Admin.store.systemmgmt.usermgmt.Users', {
    extend: 'Ext.data.Store',

    alias: 'store.usermgmtuser',

    //model: 'Admin.model.systemmgmt.usermgmt.User',

    proxy:{
    	type: 'ajax',
        url: 'app/data/systemmgmt/usermgmt/users.json',
    	//url: 'user/admin',
    	method: 'GET',
    	reader: {
             type: 'json',
             rootProperty: 'users',
             successProperty: 'success'
         }
    },

    autoLoad: 'true',

    sorters: {
        direction: 'ASC',
  //  	direction:'DESC',
        property: 'identifier'
    }
});
