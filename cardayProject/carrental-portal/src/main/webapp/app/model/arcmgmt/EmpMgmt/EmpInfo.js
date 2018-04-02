Ext.define('Admin.model.arcmgmt.EmpMgmt.EmpInfo', {
    fields: [],
    extend: 'Ext.data.Model',

    fields: [
        {
          type: 'string',
            name: 'vehicleNumber'
        },{
          type:'string',
          name:'EmpName' 
        },{
          type: 'string',
          name: 'EmpPhone' 
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
