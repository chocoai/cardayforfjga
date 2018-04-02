Ext.define('Admin.store.orgmgmt.personmgmt.DepartmentPerson', {
    extend: 'Ext.data.Store',

    alias: 'store.departmentPerson',

    model: 'Admin.model.personmgmt.person',

    proxy:{
    	type: 'ajax',
        url: 'user/0/listByOrgId',
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
        direction: 'ASC',
  //  	direction:'DESC',
        property: 'id'
    }
});
