Ext.define('Admin.view.vehiclemgmt.stationmgmt.GridStation', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.vehiclemgmt.stationmgmt.StationModel',
    ],
    
    id: 'gridStation',
    reference: 'gridStation',
    title: '站点管理',
    //cls : 'user-grid',  //图片用户样式
    bind:{
    	store:'{stationsResults}'
    },
    viewConfig: {
        loadMask: true,
    },
    
//    viewModel : {
//		type : 'stationmodel'
//	},
	
    stateful: true,
    collapsible: false,
    forceFit: false,
    mask: true,
    columnLines: true, // 加上表格线
    align: 'center',
    selModel: {
        injectCheckbox: 0,
        mode: "SINGLE",     //"SINGLE"/"SIMPLE"/"MULTI"
        checkOnly: true     //只能通过checkbox选择
    },

    columns: [{
            xtype : 'gridcolumn',
            header: '站点ID',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'id',
            flex: 3,
            name: 'id',
            hidden: true,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },{
    		xtype : 'gridcolumn',
            header: '站点名称',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'stationName',
            flex: 3,
            name: 'stationName',
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        }, {
       	 header: '省/直辖市',
         sortable: true,
            menuDisabled: true,
         dataIndex: 'province',
         flex: 2.5,
         name: 'province',
         align: 'center',
         renderer:function (value, metaData){
             metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
             return value;  
         }
        }, {
        	header: 'provinceId',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'provinceId',
            flex: 2.5,
            name: 'provinceId',
            hidden: true,
            align: 'center',
        }, {
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
        }, {
        	header: 'cityId',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'cityId',
            flex: 2.5,
            name: 'cityId',
            align: 'center',
            hidden: true,
        },   {
        	header: '所在区',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'area',
            flex: 2.5,
            name: 'area',
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        }, {
        	header: 'areaId',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'areaId',
            flex: 2.5,
            name: 'areaId',
            align: 'center',
            hidden: true,
        }, {
            header: '位置',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'position',
            flex: 2.5,
            name: 'position',
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '半径',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'radius',
            flex: 2.5,
            name: 'radius',
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        }, 
        {
            header: '分配车辆数',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'assignedVehicleNumber',
            flex: 2.5,
            name: 'assignedVehicleNumber',
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        }, 
        {
            header: '停车位数量',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'carNumber',
            flex: 2.5,
            name: 'carNumber',
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        }, 
        {
            header: '开始运营时间',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'startTime',
            flex: 2.5,
            name: 'startTime',
            hidden: true,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },  
        {
            header: '结束运营时间',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'endTime',
            flex: 2.5,
            name: 'endTime',
            hidden: true,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },  
        {
            header: '经度',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'longitude',
            flex: 2.5,
            name: 'longitude',
            hidden: true,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },  
        {
            header: '纬度',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'latitude',
            flex: 2.5,
            name: 'latitude',
            hidden: true,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },  
        {
            header: '已分配权限',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'resourceNames',
            flex: 2.5,
            name: 'resourceNames',
            hidden: true,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
        	header: '站点ID',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'id',
            flex: 2.5,
            name: 'stationId',
            hidden: true,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
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
                    handler: 'viewStation'
                },
                {
                    xtype: 'button',
                    tooltip:'修改',
                    iconCls: 'x-fa fa-pencil',
                    handler: 'editStation'
                },
                {
                    xtype: 'button',
                    tooltip:'删除',
                    iconCls: 'x-fa fa-close',
                    handler: 'deleteStation'
                },
                {
                    xtype: 'button',
                    tooltip:'分配车辆',
                    iconCls: 'x-fa fa-automobile',
                    handler: 'assignVehicle'
                },/*{
                    getClass: function(v, meta, record) {
                    var marginValue = this.width/2-20;                        
                    this.setMargin('0 -'+marginValue+' 0 '+marginValue);
                }

        }*/
            ],
//            align: 'left',
            align: 'center',
            cls: 'content-column',
            width: 160,
            text: '操作'//,
            //tooltip: 'edit'
        }
    ],
    dockedItems : [{
            id:'stationPage',
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
                store: '{stationsResults}'
            },
            displayInfo: true
        }],
    
    initComponent: function() {
        this.callParent();

    },
});
