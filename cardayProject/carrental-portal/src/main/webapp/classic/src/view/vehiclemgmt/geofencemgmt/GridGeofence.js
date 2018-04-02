Ext.define('Admin.view.vehiclemgmt.geofencemgmt.GridGeofence', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.vehiclemgmt.geofencemgmt.ViewModel',
    ],
    
    id: 'gridGeofence',
    reference: 'gridGeofence',
    title: '地理围栏管理',
    //cls : 'user-grid',  //图片用户样式
    bind:{
    	store:'{geofenceResults}'
    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...',
        emptyText: '无记录！',
        deferEmptyText: false
    },
	
    stateful: true,
    collapsible: false,
    forceFit: false,
    mask: true,
    columnLines: true, // 加上表格线
    
    columns: [{
            xtype : 'gridcolumn',
            header: '地理围栏编号',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'id',
            flex: 3,
            name: 'id',
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },{
    		xtype : 'gridcolumn',
            header: '地理围栏名称',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'markerName',
            flex: 3,
            name: 'markerName',
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
            header: '绘制类型',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'type',
            //flex: 2.5,
            name: 'type',
            align: 'center',
            renderer:function(val, cellValues, rec){
                if(val == '0'){
                    return '行政区划分';
                }else if(val == '1'){
                    return '自主绘制';
                }else if(val == '2'){
                	return '圆形';
                }
            },
            width: 100,
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
        }, {
        	header: 'provinceId',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'provinceId',
            flex: 2.5,
            name: 'provinceId',
            align: 'center',
            hidden: true,
        }, {
        	header: 'cityId',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'cityId',
            flex: 2.5,
            name: 'cityId',
            align: 'center',
            hidden: true,
        }, {
        	header: 'address',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'position',
            flex: 2.5,
            name: 'address',
            align: 'center',
            hidden: true,
        }, {
            header: '多边形覆盖点',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'pattern',
            flex: 2.5,
            name: 'pattern',
            align: 'center',
            hidden:true,
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
            align: 'center',
            hidden: true,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        }, 
        {
            xtype: 'actioncolumn',
            cls: 'content-column',
            width: 180,
            text: '操作',
            //tooltip: 'edit'
//            align: 'left',
            align: 'center',
            items: [
                {
                    xtype: 'button',
                    tooltip:'查看',
                    iconCls: 'x-fa fa-eye',
                    handler: 'viewGeofence'
                },
                {
                    xtype: 'button',
                    tooltip:'修改',
                    iconCls: 'x-fa fa-pencil',
                    handler: 'editGeofence'
                },
                {
                    xtype: 'button',
                    tooltip:'删除',
                    iconCls: 'x-fa fa-close',
                    handler: 'deleteGeofence'
                },
                {
                    xtype: 'button',
                    tooltip:'分配车辆',
                    iconCls: 'x-fa fa-automobile',
                    handler: 'assignGeofenceVehicle'
                },
                /*{
                    getClass: function(v, meta, record) {
                        var marginValue = this.width/2-20;                        
                        this.setMargin('0 -'+marginValue+' 0 '+marginValue);
                    }
                }*/
            ],

            
        }
    ],
    dockedItems : [{
            id:'geofencePage',
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
                store: '{geofenceResults}'
            },
            displayInfo: true
        }],
    
    initComponent: function() {
        this.callParent();

    },
});
