/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.personmgmt.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'personmgmt',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Border',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
        'Admin.view.orgmgmt.personmgmt.SearchForm',
        'Admin.view.orgmgmt.personmgmt.Grid',
        'Admin.view.orgmgmt.personmgmt.OrgTreeList'
//        'Web.view.module.license_obdmgmt.obdconfig.ViewController',
//        'Web.view.module.license_obdmgmt.obdconfig.ViewModel',
//        //'Web.view.module.license_obdmgmt.obdconfig.GridSummary',
//        'Web.view.module.license_obdmgmt.obdconfig.ConfigModel',
    ],
    
    controller: {
        xclass: 'Admin.view.orgmgmt.personmgmt.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.orgmgmt.personmgmt.PersonModel'
    },
//    listeners:{
//        afterrender: 'onSearchClick',
//    },
    
    autoScroll: true,
    height: 700,
    layout: {
        type: 'border',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 0,
    defaults: {
        frame: true,
        collapsible: true,
        margin: '0 0 3 0'
    },
    items: [
{
	xclass: 'Admin.view.orgmgmt.personmgmt.OrgTreeList',
	region:'west',
    margins: '5 0 0 0',
//    cmargins: '5 5 0 0',
//    width: 175,
},
    {
		region:'north',
        xtype: 'personSearch'
    },
        {
    		id : 'orgPersonGridId',
    		region:'center',
            xtype: 'gridpanel',
            cls: 'user-grid',
            title: '人员列表',
            routeId: 'user',
            bind: {
                store: {type: "personmgmtReport"}
            },
            scrollable: false,
            columns: [{
                header: '姓名',
                sortable: false,
                menuDisabled: true,
                align: 'center',
                dataIndex: 'username',
//                width: 115,
                flex: 1
            },
            {
            	header: '性别',
                sortable: false,
                menuDisabled: true,
                align: 'center',
//                width: 118,
                dataIndex: 'sex',
                renderer: function(value) {
                	if(value=='male'){
                		return '男';
                	}else{
                		return '女';
                	}
                },
                flex: 1
            }, {
                header: '手机号码',
                menuDisabled: true,
                align: 'center',
                sortable: false,
                dataIndex: 'phone_number',
                flex: 1
//                width: 150,
            }, {
                header: '邮箱',
                sortable: false,
                menuDisabled: true,
                align: 'center',
                dataIndex: 'Email',
                flex: 1
//                width: 150,
            }, {
            	xtype : 'actioncolumn',
        		items : [ {
        			xtype : 'button',
        			tooltip : '查看',
        			iconCls : 'x-fa fa-ban',
        			handler: 'querypersonmgmtInfo'
        		}, {
        			xtype : 'button',
        			tooltip : '编辑',
        			iconCls : 'x-fa fa-pencil',
        			handler : 'editpersonInfo'
        		}, {
        			xtype : 'button',
        			tooltip : '删除',
        			iconCls : 'x-fa fa-close',
        			handler : 'delelteFormInfo'
        		} ],

        		cls : 'content-column',
        		width : 160,
                align: 'center',
        		text : '操作',
        		//tooltip : 'edit '
            }
        ],
        dockedItems: [
            {
//                id:'ggflowReportpage',
                xtype: 'pagingtoolbar',
                pageSize: 20,
                dock: 'bottom',
                displayMsg: '第 {0} - {1} 条记录，共 {2} 条记录',
                emptyMsg : '无数据！',
                beforePageText: "第",
                afterPageText: "页，共{0}页",
                nextText: "下一页",
                prevText: "上一页",
                refreshText: "刷新",
                firstText: "第一页",
                lastText: "最后一页",
                bind: {
                    store: {type: "flowReport"}
                },
                displayInfo: true
            }
        ],
        }
    ]
});
