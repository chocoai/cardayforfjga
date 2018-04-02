Ext.define('Admin.view.systemmgmt.empmgmt.EmpViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.empviewmodel',
    
    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {
    	empsResults: {
            model: 'Admin.view.systemmgmt.empmgmt.EmpDataModel',
            pageSize: 20,
            autoLoad: false,
            //remoteSort: false,
            remoteFilter: true,
            /*sorters: [{
                property: 'id',
                direction: 'DESC'
            }],*/
            listeners: {
                beforeload: 'onBeforeLoad'
            }
        },

        empRuleStore: {
            model: 'Admin.model.systemmgmt.empmgmt.EmpRule',
            autoLoad: false,
            pageSize: 5,
            remoteFilter: true,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'loadEmpAvialiableRule'
            }
        },

        empRuleAssignedStore: {
                model: 'Admin.model.systemmgmt.empmgmt.EmpRuleAssigned',
                remoteFilter: true,
                autoLoad: false,
                pageSize: 5,
                sorters: [{
                    property: 'id',
                    direction: 'DESC'
                }],
                listeners: {
                    beforeload: 'loadEmpRule'
                }
        }
    }
});
