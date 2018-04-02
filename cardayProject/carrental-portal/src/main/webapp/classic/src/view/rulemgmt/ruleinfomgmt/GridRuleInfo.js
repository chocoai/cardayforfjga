Ext.define('Admin.view.rulemgmt.ruleinfomgmt.GridRuleInfo', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.rulemgmt.ruleinfomgmt.ViewModel',
    ],
    
    id: 'gridRuleInfo',
    reference: 'gridRuleInfo',
    title: '用车规则列表',
    //cls : 'user-grid',  //图片用户样式
    bind:{
    	store:'{ruleInfoResults}'
    },
    viewConfig: {
        loadMask: true,
    },
	
    stateful: true,
    collapsible: false,
    forceFit: false,
    mask: true,
    columnLines: true, // 加上表格线
    align: 'center',

    columns: [{
            xtype : 'gridcolumn',
            header: '编号',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'ruleId',
            flex: 2,
            name: 'ruleId',
            hidden: true,
            align: 'center',
        },{
    		xtype : 'gridcolumn',
            header: '名称',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'ruleName',
            flex: 1,
            name: 'ruleName',
            align: 'center',
            renderer : function(v, metadata, record, rowIndex, columnIndex, store){  
                var value = Ext.htmlEncode(v);  
                value = value == null ? '':value;  
                metadata.attr = ' data-qtip="' + value+ '"'  
                    return "<div class='reply-text' data-qtip='"+value+"' >"+value+"</div>";  
            }
        }, 
        {
            header: '员工人数',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'employeeNum',
            flex: 1,
            name: 'employeeNum',
            align: 'center',
            renderer : function(v, metadata, record, rowIndex, columnIndex, store){  
                var value = Ext.htmlEncode(v);  
                value = value == null ? '':value;  
                metadata.attr = ' data-qtip="' + value+ '"'  
                    return "<div class='reply-text' data-qtip='"+value+"' >"+value+"</div>";  
            }
        },
        {
            header: '时间',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'timeList',
            flex: 3,
            name: 'timeList',
            align: 'center',
            renderer : function(v, metadata, record, rowIndex, columnIndex, store){  
                var value = Ext.htmlEncode(v);  
                value = value == null ? '':value;  
                metadata.attr = ' data-qtip="' + value+ '"'  
                    return "<div class='reply-text' data-qtip='"+value+"' >"+value+"</div>";  
            } 
        },
        {
            header: '上车位置',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'getOnList',
            flex: 3,
            name: 'getOnList',
            align: 'center',
            renderer : function(v, metadata, record, rowIndex, columnIndex, store){  
                var value = Ext.htmlEncode(v);  
                value = value == null ? '':value;  
                metadata.attr = ' data-qtip="' + value+ '"'  
                    return "<div class='reply-text' data-qtip='"+value+"' >"+value+"</div>";  
            }
        },
        {
            header: '下车位置',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'getOffList',
            flex: 3,
            name: 'getOffList',
            align: 'center',
            renderer : function(v, metadata, record, rowIndex, columnIndex, store){  
                var value = Ext.htmlEncode(v);  
                value = value == null ? '':value;  
                metadata.attr = ' data-qtip="' + value+ '"'  
                    return "<div class='reply-text' data-qtip='"+value+"' >"+value+"</div>";  
            }
        },
        {
            header: '公车性质',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'vehicleTypeList',
            flex: 3,
            name: 'vehicleTypeList',
            align: 'center',
            renderer : function(v, metadata, record, rowIndex, columnIndex, store){  
                var value = Ext.htmlEncode(v);  
                value = value == null ? '':value;  
                metadata.attr = ' data-qtip="' + value+ '"'  
                    return "<div class='reply-text' data-qtip='"+value+"' >"+value+"</div>";  
            }
        },
        {
            header: '用车额度',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'useLimit',
            flex:1,
            name: 'useLimit',
            align: 'center',
            renderer : function(v, metadata, record, rowIndex, columnIndex, store){  
                var value = Ext.htmlEncode(v);  
                value = value == null ? '':value;  
                metadata.attr = ' data-qtip="' + value+ '"'  
                    return "<div class='reply-text' data-qtip='"+value+"' >"+value+"</div>";  
            }
        }, 
        {
            xtype: 'actioncolumn',
            items: [
                {
                    xtype: 'button',
                    tooltip:'查看',
                    iconCls: 'x-fa fa-eye',
                    handler: 'viewRuleInfo'
                },
                {
                    xtype: 'button',
                    tooltip:'修改',
                    iconCls: 'x-fa fa-pencil',
                    handler: 'editRuleInfo'
                },
                {
                    xtype: 'button',
                    tooltip:'删除',
                    iconCls: 'x-fa fa-close',
                    handler: 'deleteRuleInfo'
                },
                /*{
                    getClass: function(v, meta, record) {
                        var marginValue = this.width/2-20;                        
                        this.setMargin('0 -'+marginValue+' 0 '+marginValue);
                    }
                }*/
            ],

            cls: 'content-column',
            width: 160,
            text: '操作',
//            align: 'left',
            align: 'center',
            //tooltip: 'edit'
        }
    ],
/*    dockedItems : [{
            id:'ruleInfoPage',
            pageSize:10,
            xtype: 'pagingtoolbar',
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
                store: '{ruleInfoResults}'
            },
            displayInfo: true
        }],*/
    
    initComponent: function() {
        this.callParent();

    },
});
