Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.EmpMgmtModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.EmpMgmtModel',

    requires: [
        'Ext.data.Store',
        'Ext.data.proxy.Memory',
        'Ext.data.field.Integer',
        'Ext.data.field.String',
        'Ext.data.field.Date',
        'Ext.data.field.Boolean',
        'Ext.data.reader.Json'
    ],

    stores: {
        assignedEmpStore: {
            model: 'Admin.model.arcmgmt.EmpMgmt.AssignedEmp',
            autoLoad: false,
            pageSize: 5,
            remoteFilter: true,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'onBeforeLoadforAssignedEmp',
                load: 'loadforAssignedEmp',
            }
        },

        availableAssignEmpStore: {
            model: 'Admin.model.arcmgmt.EmpMgmt.AvailableAssignEmp',
            autoLoad: false,
            pageSize: 5,
            remoteFilter: true,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'onBeforeLoadforAvailableAssignEmp',
                load: 'loadforAvailableAssignEmp',
            }
        },
        EmpInfoStore: {
            model: 'Admin.model.arcmgmt.EmpMgmt.EmpInfo',
            autoLoad: false,
            remoteFilter: true,
            sorters: [{
                property: 'vehicleNumber',
                direction: 'DESC'
            }],
        },

        tasksInfoStore: {
            model: 'Admin.model.arcmgmt.EmpMgmt.TasksInfo',
            autoLoad: false,
            remoteFilter: true,
            sorters: [{
                property: 'orderNo',
                direction: 'DESC'
            }],
        },
    }
});
