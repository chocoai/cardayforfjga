/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.arcmgmt.DeptMainView', {
    extend: 'Ext.panel.Panel',
   xtype: 'arcmgmt',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Border',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
        'Admin.view.orgmgmt.arcmgmt.deptmgmt.DeptMgmt',
        'Admin.view.orgmgmt.arcmgmt.OrgTreeList',
        'Admin.view.orgmgmt.arcmgmt.ArcModel'
    ],
    
    controller: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.ArcModel'
    },
    listeners:{
//    	afterrender: 'loadDepartmentsInfo',
    },
    id:'depmMainTabView',
//    autoScroll: true,
//    height: 600,
//    bodyPadding: 10,
    defaults: {
//        frame: true,
//        collapsible: true,
//        margin: '0 0 3 0'
    },
    layout:'fit',
    bodyPadding: 12,	//其他页面都是20，然后tab内部为了和tab标题对齐设置了padding为8px，所以这里设12px
    border:false,
//    layout:'fit',
    items: [{
    	xtype:'tabpanel',
    	border:false,
    	items:[
		    {
	//	    	xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.DeptMgmt',
		    	xtype:'panel',
		    	title:'组织管理',
		    	border:false,
		    	layout:'fit',
		    	bodyPadding: '20 8 8 8',	//这里是为了和tab的标题栏对齐
		    	items:[{
		    		xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.DeptMgmt',
		    	}]
		    },/*{
	//	    	xclass: 'Admin.view.orgmgmt.arcmgmt.DeptConfig',
		    	xtype:'panel',
		    	bodyPadding: 8,
		    	border:false,
		    	title:'部门配置'
		    }*/
		]
    }],
    initComponent: function() {
        this.callParent();
    }
});
