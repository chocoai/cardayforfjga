Ext.define('Admin.view.rulemgmt.locationmgmt.GridLocation', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.rulemgmt.locationmgmt.ViewModel',
    ],
    
    id: 'gridLocation',
    reference: 'gridLocation',
    title: '用车位置管理',
    //cls : 'user-grid',  //图片用户样式
    bind:{
    	store:'{locationResults}'
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
            header: '用车位置编号',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'id',
            flex: 2,
            name: 'id',
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },{
    		xtype : 'gridcolumn',
            header: '用车位置名称',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'locationName',
            flex: 3.5,
            name: 'locationName',
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        }, 
        {
            header: '城市',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'city',
            flex: 2.5,
            name: 'city',
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '位置',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'position',
            flex: 3.5,
            name: 'position',
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '范围',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'radius',
            flex: 2.5,
            name: 'radius',
            align: 'center',
            renderer:function(v, cellValues, rec){
                v=rec.get('radius')+'公里';
                cellValues.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(v) + '"';  
                return v;
            }
        },
        {
            header: '组织机构ID',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'organizationId',
            flex: 2.5,
            name: 'organizationId',
            hidden: true,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        }, 
        {
            xtype: 'actioncolumn',
            items: [
                {
                    xtype: 'button',
                    tooltip:'查看',
                    iconCls: 'x-fa fa-eye',
                    handler: 'viewLocation'
                },
                {
                    xtype: 'button',
                    tooltip:'修改',
                    iconCls: 'x-fa fa-pencil',
                    handler: 'editLocation'
                },
                {
                    xtype: 'button',
                    tooltip:'删除',
                    iconCls: 'x-fa fa-close',
                    handler: 'deleteLocation'
                },
                /*{
                    getClass: function(v, meta, record) {
                        var marginValue = this.width/2-20;                        
                        this.setMargin('0 -'+marginValue+' 0 '+marginValue);
                    }
                }*/
            ],

            cls: 'content-column',
//            align: 'left',
            align: 'center',
            width: 160,
            text: '操作',
            //tooltip: 'edit'
        }
    ],
    dockedItems : [{
            id:'locationPage',
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
                store: '{locationResults}'
            },
            displayInfo: true
        }],
    
    initComponent: function() {
        this.callParent();

    },
});
