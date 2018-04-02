Ext.define('Admin.store.search.Users', {
    extend: 'Ext.data.Store',

    alias: 'store.searchusers',

    model: 'Admin.model.search.User',

    proxy: {
        type: 'api',
        url: '~api/search/users'
    },
   /* proxy:{
    	type: 'ajax',
         url: 'app/data/search/Users.js',
         reader: {
             type: 'json',
             rootProperty: 'users',
             successProperty: 'success'
         }
    },*/

    autoLoad: 'true',

    sorters: {
    	direction: 'ASC',
        property: 'fullname'
//    	direction:'DESC',
//        property: 'identifier'
    }
});
