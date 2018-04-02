Ext.define('Admin.view.vehiclespurchase.vehipurchaseapply.GridVehiPurchaseInfo', {
	extend : 'Ext.grid.Panel',
	reference: 'gridVehiPurchaseInfo',
	requires : [
		'Ext.grid.column.Action'
	],

	title : '车辆购置列表',
    bind:{
    	store:'{vehiPurchaseInfoResults}'
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
            header: '申购汽车名称',
            flex: 1,
            align:'center',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'applyVehName',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },{
            header: '规格或型号',
            flex: 1,
            align:'center',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'vehModel',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },{
            header: '金额',
            flex: 1,
            align:'center',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'sumAccount',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },{
            header: '资金来源（万元）',
            align:'center',
            menuDisabled: true,
            flex: 4,

            columns: [{
                header: '公共财政预算',
                align:'center',
                sortable: false,
                menuDisabled: true,
                dataIndex: 'financialBudget',
                renderer:function (value, metaData){  
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                    return value;  
                }
            }, {
                header: '上年结余',
                align:'center',
                sortable: false,
                menuDisabled: true,
                dataIndex: 'previousBalance',
                renderer:function (value, metaData){  
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                    return value;  
                }
            },{
                header: '上级补助',
                align:'center',
                sortable: false,
                menuDisabled: true,
                dataIndex: 'higherGrant',
                renderer:function (value, metaData){  
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                    return value;  
                }
            },{
                header: '其他资金',
                align:'center',
                sortable: false,
                menuDisabled: true,
                dataIndex: 'otherAccount',
                renderer:function (value, metaData){  
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                    return value;  
                }
            },]
        }],
	initComponent : function() {
		this.callParent();
	}
});