Ext.define('Admin.model.arcmgmt.vehiclemgmt.TasksInfo', {
    fields: [],
    extend: 'Ext.data.Model',

    fields: [
        {
          type: 'string',
            name: 'orderNo'
        },{
          type:'string',
          name:'status' 
        },{
          type: 'string',
          name: 'driverName' 
        },{
          type: 'string',
          name: 'orderUserphone' 
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
