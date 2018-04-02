Ext.define('Admin.view.ordermgmt.orderallocate.AllocatingDriverListGrid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.ordermgmt.orderallocate.DriverViewModel'
    ],
    reference: 'allocatingDriverListGrid',
//    viewModel: {
//        xclass: 'Admin.view.ordermgmt.orderallocate.AllocatingVehicleModel'
//    },
    title: '司机列表',
//    width : 400,
    height:500,
    xtype: 'allocatingDriverListGrid',
    id : 'allocatingDriverListGrid',
    bind: {
        store: '{driversResults}'
    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...',
        getRowClass : function(record,rowIndex,rowParams,store) {
            if(record.data.drvStatus != 2){
                return 'row-myNoCkeck';          
            } 
        }
    },
    
//    listeners:{
//    	afterrender: 'onBeforeLoadDriver'
//    },
    stateful: true,
    forceFit: false,
    columnLines: true, // 加上表格线
    cls : 'user-grid',  //图片用户样式
    margin: '0 0 0 0',
    frame: false,
    
    selModel: {
        type: 'checkboxmodel',
        checkOnly: true,
        mode: 'SINGLE',
        allowDeselect: true,
        listeners: {
            beforeselect: function(me,record,index,eOpts){
                if(record.data.drvStatus != 2){
                    return false;          
                } 
            }
        }
    },
	//selType: 'checkboxmodel',	//自5.1.0已废弃
    columns: [
//    {
//    	header:'选择',
//    	xtype:'templatecolumn',
//    	tpl:'<input name=vehicle type=radio value={id}" />',
//    	flex: 0.5
//    },
    {
        header: '序号',
        hidden: true,
        sortable: true,
        dataIndex: 'id',
        menuDisabled: true,
        align: 'center',
        flex: 1
        
    },
    {
        header: '司机姓名',
        sortable: false,
        dataIndex: 'realname',
        menuDisabled: true,
        align: 'center',
        flex: 1
        
    },
    {
        header: '司机电话',
        sortable: false,
        dataIndex: 'phone',
        menuDisabled: true,
        align: 'center',
        flex: 1
        
    }, 
    {
        header: '所属站点',
        sortable: false,
        dataIndex: 'stationName',
        menuDisabled: true,
        align: 'center',
        flex: 1,
        //hidden: true
    }, 
    {
        header: '出发次数',
        sortable: true,
        dataIndex: 'tripQuantity',
        menuDisabled: true,
        align: 'center',
        flex: 1,
    }, 
    {
        header: '公里数',
        sortable: true,
        dataIndex: 'tripMileage',
        menuDisabled: true,
        align: 'center',
        flex: 1,
    }, 
    {
        header: '状态',
        sortable: false,
        dataIndex: 'drvStatus',
        menuDisabled: true,
        align: 'center',
        flex: 1,
        renderer: function(val,metaData) {
            switch(val){
                case 0:
                    return '短途出车';
                case 1:
                    return '长途出车';
                case 2:
                    return '在岗';
                case 3:
                    return '值班锁定';
                case 4: 
                    return '补假/休假';
                case 5: 
                    return '计划锁定';
                case 6:
                    return '出场';
                case 7:
                    return '下班';
            }
        }
    }
    ],
    dockedItems: [
	  {
	      xtype: 'pagingtoolbar',
	      id: 'AllocatingDriver_id',
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
//	      bind: {
//	      	store: '{allocatingVehicleReport}'
//	      },
	      displayInfo: true
	  }
   ],
   
   initComponent: function() {
        this.callParent();
    },
});
