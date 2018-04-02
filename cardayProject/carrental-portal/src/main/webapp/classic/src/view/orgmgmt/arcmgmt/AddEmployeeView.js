Ext.define('Admin.view.orgmgmt.arcmgmt.AddEmployeeView', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.orgmgmt.arcmgmt.EmployeeModel'
    ],
    reference: 'addEmployeeView',
    viewModel: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.EmployeeModel'
    },
    title: '员工列表',
    width : 750,
    id: 'addEmployeeView',
    xtype: 'addEmployeeView',
    bind: {
        store: '{employeeStore}'
    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
    listeners: {
    },
    stateful: true,
    collapsible: false,
    forceFit: false,
    mask: true,
    // collapsed: true,
    multiSelect: false,
    columnLines: true, // 加上表格线
    align: 'center',
    // height: 350,  
    cls: 'shadow',
    margin: '20 0 0 10',
    selModel: {
        injectCheckbox: 0,
        mode: "SIMPLE",     //"SINGLE"/"SIMPLE"/"MULTI"
//        checkOnly: true     //只能通过checkbox选择
    },
    selType: 'checkboxmodel',//可以对表格的数据进行多选
    columns: [{
        header: '姓名',
        sortable: false,
        align: 'center',
        menuDisabled: true,
        dataIndex: 'realname',
//        width: 115,
        flex: 1
    },
//    {
//    	header: '性别',
//        sortable: false,
//        dataIndex: 'sex',
//        renderer: function(value) {
//        	if(value=='male'){
//        		return '男';
//        	}else{
//        		return '女';
//        	}
//        },
//        flex: 1
//    }, 
    {
        header: '手机',
        sortable: false,
        align: 'center',
        menuDisabled: true,
        dataIndex: 'phone',
        flex: 1
//        width: 150,
    }, {
        header: '员工系统角色',
        sortable: false,
        align: 'center',
        menuDisabled: true,
        flex: 1,
        dataIndex: 'userCategory',
        renderer: function(value,metaData) {
            if (value=='0') {
                return '超级管理员';
            } else if(value=='1'){
            	return '租户管理员';
            } else if(value=='2'){
            	return '企业管理员';
            } else if(value=='3'){
            	return '部门管理员';
            } else if(value=='5'){
            	return '司机';
            } else {
            	return '员工';
            }
        }
    }
],
    dockedItems: [
        {
//            id:'ggflowReportpage',
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
                store:  "{employeeStore}"
            },
            displayInfo: true
        }
    ],
    initComponent: function() {
        this.callParent();
    },
});
