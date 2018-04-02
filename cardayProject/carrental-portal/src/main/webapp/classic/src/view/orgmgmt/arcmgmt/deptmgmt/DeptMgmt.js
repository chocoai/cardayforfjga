/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.DeptMgmt', {
    extend: 'Ext.panel.Panel',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Border',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
        'Admin.view.orgmgmt.arcmgmt.SearchForm',
        'Admin.view.orgmgmt.arcmgmt.Grid',
        'Admin.view.orgmgmt.arcmgmt.OrgTreeList',
        'Admin.view.orgmgmt.arcmgmt.ArcModel'
    ],
    
    controller: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.DeptMgmtController'
    },
//    viewModel: {
//        xclass: 'Admin.view.orgmgmt.arcmgmt.ArcModel'
//    },
    listeners:{
//    	afterrender: 'loadDepartmentsInfo',
    },
    title:'组织列表',
//    autoScroll: true,
    height: 600,
    layout: {
        type: 'border',
        pack: 'start',
        align: 'stretch'
    },
    defaults: {
        frame: true,
//        collapsible: true,
        margin: '0 0 3 0'
    },
    items: [
	    {
	    	xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.DeptTreeList',
	    	region:'west',
//	    	collapsed: true,
	        width: 250,
	    },{
	        xtype: 'tabpanel',
	        region:'center',
	        id:'orgmgmtDeptmgmtTab',
	        items:[{
	        	title:'组织信息',
	        	xclass:'Admin.view.orgmgmt.arcmgmt.deptmgmt.deptinfo.DeptInfo',
	        },{
                title:'民警管理',
                xtype:'tabpanel',
                id:'EmpMgmtTabId',
                region:'center',
                listeners:{
                    activate: 'onActivateforEmpMgmt',
                },
                items:[
                    {
                        title:'现有民警',
                        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.ShowAssignedEmp',
                    },{
                        title:'可分配民警',
                        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.ShowAvailableAssignEmp',
                    }]
	        },/*{
	        	title:'额度管理',
                xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.creditlimitmgmt.ShowCreditLimitMgmt',
	        },*/{
	        	title:'车辆管理',                
                xtype:'tabpanel',
                id:'vehicleMgmtTabId',
                region:'center',
                listeners:{
                     activate: 'onActivateforVehicleMgmt',
                },
                items:[
                {
                    title:'现有车辆', 
                    tFlag : '0',
                    xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.ShowAssignedVehicle',
                },{
                    title:'可分配车辆',
                    tFlag : '1',
                    xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.ShowAvailableAssignVehicle',
                }]
	        },{
	        	title:'司机管理',
                xtype:'tabpanel',
                id:'driverMgmtTabId',
                region:'center',
                listeners:{
                    activate: 'onActivateforDriverMgmt',
                },
                items:[
                    {
                        title:'现有司机',
                        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.ShowAssignedDriver',
                    },{
                        title:'可分配司机',
                        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.ShowAvailableAssignDriver',
                    }]
	        }]
	    }
    ],
    initComponent: function() {
        this.callParent();
    }
});
