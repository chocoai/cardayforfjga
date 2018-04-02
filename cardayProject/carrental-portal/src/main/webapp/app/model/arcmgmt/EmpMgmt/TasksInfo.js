Ext.define('Admin.model.arcmgmt.EmpMgmt.TasksInfo', {
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
          name: 'EmpName'
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
