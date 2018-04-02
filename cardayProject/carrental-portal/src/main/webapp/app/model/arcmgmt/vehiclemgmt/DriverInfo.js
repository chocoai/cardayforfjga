Ext.define('Admin.model.arcmgmt.vehiclemgmt.DriverInfo', {
    fields: [],
    extend: 'Ext.data.Model',

    fields: [
        {
          type: 'string',
            name: 'vehicleNumber'
        },{
          type:'string',
          name:'driverName' 
        },{
          type: 'string',
          name: 'driverPhone' 
        }
    ],

    data: { items: [    
    ]},
    proxy: {
        type: 'memory',
        reader: {
            type: 'json',
            rootProperty: 'items'
        }
    }
});
