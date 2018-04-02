Ext.define('Admin.view.vehiclespurchase.vehipurchaseapply.GridVehiPurchaseTypeAdd', {
	extend : 'Ext.grid.Panel',
	reference: 'gridVehiPurchaseTypeAdd',
	requires : [
		'Ext.grid.column.Action'
	],

	title : '申购公车性质',
    bind:{
    	store:'{vehiPurchaseTypeAddResults}'
    },

	viewConfig: {
        loadMask: true,
       	loadingText: '加载中...',
       	emptyText: '无记录！',
        deferEmptyText: false
    },
	stateful: true,
	multiSelect: false,
	forceFit: false,
    mask: true,
   	columnLines: true,  //表格线
	columns:[
		  {
            header: '申请Id',
            dataIndex: 'id',
            hidden: true
    	  },{
            header: '应急机要通信接待用车',
            align:'center',
            menuDisabled: true,
            flex: 2,

            columns: [{
                header: '编制数',
                align:'center',
                sortable: false,
                menuDisabled: true,
                dataIndex: 'emergencyOriginalVehNum',
                renderer:function (value, metaData){  
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                    return value;  
                }
            }, {
                header: '更新数(辆)',
                align:'center',
                sortable: false,
                menuDisabled: true,
                dataIndex: 'emergencyChangeVehNum',
                renderer:function (value, metaData){  
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                    return value;  
                }
            },]
        },{
            header: '行政执法及其他用车',
            align:'center',
            menuDisabled: true,
            flex: 4,

            columns: [{
                header: '行政执法用车',
                align:'center',
                menuDisabled: true,

                columns: [{
                    header: '编制数',
                    align:'center',
                    sortable: false,
                    menuDisabled: true,
                    dataIndex: 'administrationOriginalVehNum',
                    renderer:function (value, metaData){  
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                        return value;  
                    }
                }, {
                    header: '更新数(辆)',
                    align:'center',
                    sortable: false,
                    menuDisabled: true,
                    dataIndex: 'administrationChangeVehNum',
                    renderer:function (value, metaData){  
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                        return value;  
                    }
                },]
            }, {
                header: '特种专业用车',
                align:'center',
                menuDisabled: true,

                columns: [{
                    header: '编制数',
                    align:'center',
                    sortable: false,
                    menuDisabled: true,
                    dataIndex: 'administrationSpecOriginalVehNum',
                    renderer:function (value, metaData){  
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                        return value;  
                    }
                }, {
                    header: '更新数(辆)',
                    align:'center',
                    sortable: false,
                    menuDisabled: true,
                    dataIndex: 'administrationSpecChangeVehNum',
                    renderer:function (value, metaData){  
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                        return value;  
                    }
                },]
            },]
        },{
            header: '执法执勤用车',
            align:'center',
            menuDisabled: true,
            flex: 4,

            columns: [{
                header: '一般执法执勤用车',
                align:'center',
                menuDisabled: true,

                columns: [{
                    header: '编制数',
                    align:'center',
                    sortable: false,
                    menuDisabled: true,
                    dataIndex: 'enforcementOriginalVehNum',
                    renderer:function (value, metaData){  
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                        return value;  
                    }
                }, {
                    header: '更新数(辆)',
                    align:'center',
                    sortable: false,
                    menuDisabled: true,
                    dataIndex: 'enforcementChangeVehNum',
                    renderer:function (value, metaData){  
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                        return value;  
                    }
                },]
            }, {
                header: '特种专业用车',
                align:'center',
                menuDisabled: true,

                columns: [{
                    header: '编制数',
                    align:'center',
                    sortable: false,
                    menuDisabled: true,
                    dataIndex: 'enforcementSpecOriginalVehNum',
                    renderer:function (value, metaData){  
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                        return value;  
                    }
                }, {
                    header: '更新数(辆)',
                    align:'center',
                    sortable: false,
                    menuDisabled: true,
                    dataIndex: 'enforcementSpecChangeVehNum',
                    renderer:function (value, metaData){  
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                        return value;  
                    }
                },]
            },]
        }],
	initComponent : function() {
		this.callParent();
	}
});