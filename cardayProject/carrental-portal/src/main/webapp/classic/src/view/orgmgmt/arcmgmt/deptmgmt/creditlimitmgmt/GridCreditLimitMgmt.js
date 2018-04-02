Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.creditlimitmgmt.GridCreditLimitMgmt', {
	extend : 'Ext.tree.Panel',
	requires : [
		'Ext.grid.Panel', 
		'Ext.toolbar.Paging', 
		'Ext.grid.column.Date',
	],

    id:'gridCreditLimitMgmt',
	reference: 'gridCreditLimitMgmt',
    itemId: "tree",
    width: 600,
    //maxHeight: 480,
    height: 480,
    rootVisible: false,
    useArrows: true,
    scrollable: true,
    store: new Admin.store.arcmgmt.vehiclemgmt.CreditLimitTreeStore(),
    columns: [
        {
            xtype: 'treecolumn',
            text: '部门',
            dataIndex: "text",
            flex: 1,
            sortable: false
        },
        {
            text: "额度",
            dataIndex: "limitedCredit",
            flex: 1,
            sortable: false,
            editor:{
                allowBlank:false
            },
        },
        {
            text: "未分配额度",
            dataIndex: "availableCredit",
            flex: 1,
            sortable: false
        }
    ],

    plugins:{
        ptype: 'cellediting',
        clicksToEdit: 1,
        listeners : {
            beforeedit : 'beforeEditforCreditLimit',
            edit: 'editforCreditLimit',
        }
    },
});