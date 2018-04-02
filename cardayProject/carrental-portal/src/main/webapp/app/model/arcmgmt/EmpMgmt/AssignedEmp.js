Ext.define('Admin.model.arcmgmt.EmpMgmt.AssignedEmp', {
    fields: [],
    extend: 'Ext.data.Model',

    proxy:{
    	type: 'ajax',
    	url: 'employee/listByDeptId',
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
