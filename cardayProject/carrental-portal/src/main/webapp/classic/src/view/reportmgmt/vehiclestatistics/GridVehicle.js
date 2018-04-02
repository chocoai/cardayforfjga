Ext.define('Admin.view.reportmgmt.vehiclestatistics.GridVehicle', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.ordermgmt.orderallocate.AllocatingVehicleModel'
    ],
    
    reference: 'gridvehicle',
    title: '车辆数列表',
    width: '100%',
    
    bind: {
        store: '{vehicleList}'
    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },

    scrollable: false,
    stateful: true,
    forceFit: false,
    align: 'left',
    margin: '0 0 0 0',
    frame: true,
    columnLines: true, // 加上表格线
    
    columns: [
    {
        header: '序号',
        sortable: true,
        menuDisabled: true,
        dataIndex: 'id',
        flex: 0.5,
        align: 'center',
        hidden:true,
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '单位',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'deptName',
        flex: 1,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '级别',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'levelType',
        flex: 1,
        align: 'center',
        renderer: function(val,metaData) {
            switch(val){
                case 0:
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('正厅级') + '"'; 
                    return '正厅级';
                case 1:
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('副厅级') + '"'; 
                    return '副厅级';
                case 2:
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('处级') + '"'; 
                    return '处级';
                case 3:
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('副处级') + '"'; 
                    return '副处级';
                case 6:
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('科级') + '"'; 
                    return '科级';
                case 7:
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('副科级') + '"'; 
                    return '副科级';
                case 4:
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('县级') + '"'; 
                    return '县级';
                case 5:
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('乡科级') + '"'; 
                    return '乡科级';
            }
        }
    },
    {
        header: '车辆编制数',
        align:'center',
        menuDisabled: true,
        flex: 2,

            columns: [{
                    header: '行政执法用车(一般)',
                    sortable: false,
                    menuDisabled: true,
                    dataIndex: 'commonNumAuthorized',
                    flex: 1,
                    align: 'center',
                    renderer:function (value, metaData){  
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                        return value;  
                    }
                },
                {
                    header: '行政执法用车(特殊)',
                    sortable: false,
                    menuDisabled: true,
                    dataIndex: 'specialNumAuthorized',
                    flex: 1,
                    align: 'center',
                    renderer:function (value, metaData){  
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                        return value;  
                    }
                },]
        },
    {
        header: '现有数',
        align:'center',
        menuDisabled: true,
        flex: 2,

            columns: [{
                    header: '行政执法用车(一般)',
                    sortable: false,
                    menuDisabled: true,
                    dataIndex: 'commonNumUsed',
                    flex: 1,
                    align: 'center',
                    renderer:function (value, metaData){  
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                        return value;  
                    }
                },
                {
                    header: '行政执法用车(特殊)',
                    sortable: false,
                    menuDisabled: true,
                    dataIndex: 'specialNumUsed',
                    flex: 1,
                    align: 'center',
                    renderer:function (value, metaData){  
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                        return value;  
                    }
                },]
        },
    ],
    dockedItems: [
	  {
	      xtype: 'pagingtoolbar',
	      id: 'GridVehicle_page_vehiclestatistics',
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
	      displayInfo: true
	  }
   ],
   
   initComponent: function() {
        this.callParent();
    },
});
