Ext.define('Admin.model.arcmgmt.drivermgmt.AvailableAssignDriver', {
    fields: [],
    extend: 'Ext.data.Model',

    proxy:{
    	type: 'ajax',
        url: 'driver/listUnallocatedDepDriver',
    	//url: 'app/data/arcmgmt/driver.json',
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

    sorters: {
      direction:'DESC',
      property: 'id'
    }
});
