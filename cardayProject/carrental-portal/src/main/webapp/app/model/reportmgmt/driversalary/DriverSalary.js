Ext.define('Admin.model.reportmgmt.driversalary.DriverSalary', {
    fields: [],
    extend: 'Ext.data.Model',

    proxy:{
    	type: 'ajax',
        url: 'driver/listDriverSalary',
        //url: 'app/data/arcmgmtinfo/reportdriving.json',
        actionMethods: {
//                    create : 'POST',
                    read   : 'POST', // by default GET
//                    update : 'POST',
//                    destroy: 'POST'
        },
        reader: {
             type: 'json',
             rootProperty: 'data',
             successProperty: 'status',
        }
    },
    autoLoad: 'false',
});