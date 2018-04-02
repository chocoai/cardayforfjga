Ext.define('Admin.view.vehiclespurchase.vehipurchaseapply.ApprovalProcessWin', {
	extend: 'Ext.window.Window',
	
    alias: "widget.approvalProcessWin",
	reference: 'approvalProcessWin',

	controller: {
        xclass: 'Admin.view.vehiclespurchase.vehipurchaseapply.ViewController'
    },

	title : '申请单审批流程',
    width : 1048,
    scrollable: true,
	closable: true,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作

    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },

    bodyPadding: 20,

    items: [
        {
            xtype:'gridpanel',
            frame: true,
            id:'approvalProcessWinGrid',

            viewModel: {
                xclass: 'Admin.view.vehiclespurchase.vehipurchaseapply.ViewModel'
            },

            bind:{
                store:'{approvalProcessResults}'
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
                    header: '审批Id',
                    dataIndex: 'id',
                    hidden: true
                },{
                    header: '审批时间',
                    align:'center',
                    sortable: false,
                    menuDisabled: true,
                    dataIndex: 'approvalTime',
                    flex: 1,
                },{
                    header: '审批人',
                    align:'center',
                    sortable: false,
                    menuDisabled: true,
                    dataIndex: 'approvalUser',
                    flex: 1,
                },{
                    header: '审批状态',
                    align:'center',
                    sortable: false,
                    menuDisabled: true,
                    dataIndex: 'approvalStatus',
                    flex: 1,
                    renderer: function(val,metaData) {
                        switch(val){
                            case "0":
                                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('申请中') + '"'; 
                                return "<span style='color:blue;'>申请中</span>";
                            case "1":
                                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('已驳回') + '"'; 
                                return "<span style='color:red;'>已驳回</span>";
                            case "2":
                                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('审核通过') + '"'; 
                                return "<span style='color:green;'>审核通过</span>";
                        }
                    }
                },{
                    header: '审批备注',
                    align:'center',
                    sortable: false,
                    menuDisabled: true,
                    dataIndex: 'approvalComment',
                    flex: 1,
                }],
        }
    ],
});