Ext.define('Admin.view.ordermgmt.orderallocate.AllocatingVehicleListGrid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.ordermgmt.orderallocate.AllocatingVehicleModel',
        'Ext.grid.plugin.CellEditing'
    ],
    reference: 'allocatingVehicleListGrid',
//    viewModel: {
//        xclass: 'Admin.view.ordermgmt.orderallocate.AllocatingVehicleModel'
//    },
    title: '车辆列表',
    height:500,
    xtype: 'allocatingVehicleListGrid',
    id : 'allocatingVehicleListGrid',
    bind: {
        store: '{allocatingVehicleReport}'
    },

    viewConfig: {
        loadMask: true,
        loadingText: '加载中...',
        getRowClass : function(record,rowIndex,rowParams,store) {
        	var seatNumber = record.data.seatNumber;
        	var passengerNum = window.sessionStorage.getItem('passengerNum');
//        	console.log("****seatNumber******"+seatNumber);
//        	console.log("****passengerNum******"+passengerNum);
        	if(record.data.vehStatus != 5 && record.data.vehStatus != 6 && record.data.vehStatus != 7){
                return 'row-myNoCkeck';          
            }else if ((seatNumber-1) < passengerNum) {
                return 'row-mylevel';
            }   
        }
    },
     
//    listeners: {
//    	afterrender: 'loadAllocatingVehicleList',
//    },
    stateful: true,
    forceFit: false,
    columnLines: true, // 加上表格线
//  collapsible: true,
    //cls : 'user-grid',  //图片用户样式
    margin: '0 0 0 0',
    frame: false,

	selModel: {
        type: 'checkboxmodel',
        checkOnly: true,
        mode: 'SINGLE',
        allowDeselect: true,
        listeners: {
            beforeselect: function(me,record,index,eOpts){
                if(record.data.vehStatus != 5 && record.data.vehStatus != 6 && record.data.vehStatus != 7){
                    return false;          
                } 
            }
        }
    },
    plugins: {ptype: 'cellediting', clicksToEdit: 1},
    columns: [
    {
        header: '序号',
        hidden:true,
        sortable: true,
        dataIndex: 'id',
        menuDisabled: true,
        align: 'center',
        flex: 1
    },
    {
        header: '车牌号',
        sortable: false,
        dataIndex: 'vehicleNumber',
        menuDisabled: true,
        align: 'center',
        flex: 1
        
    },
    {
        header: '车辆品牌',
        sortable: false,
        dataIndex: 'vehicleBrand',
        menuDisabled: true,
        align: 'center',
        flex: 1
        
    }, 
    {
        header: '车型',
        sortable: false,
        dataIndex: 'vehicleModel',
        menuDisabled: true,
        align: 'center',
        flex: 1
    }, 
    {
        header: '公车性质',
        sortable: false,
        dataIndex: 'vehicleType',
        menuDisabled: true,
        align: 'center',
        flex: 1,
        renderer : function(value) {
        	switch (value) {
        	case '0':
        		value='应急机要通信接待用车';
        		break;
        	case '1':
        		value='行政执法用车';
        		break;
        	case '2':
        		value='行政执法特种专业用车';
        		break;
        	case '3':
        		value='一般执法执勤用车';
        		break;
            case '4':
                value='执法执勤特种专业用车';
                break;
        	}
        	return value;
        }
    },
    {
        header: '座位数',
        sortable: false,
        align: 'center',
        dataIndex: 'seatNumber',
        menuDisabled: true,
        flex: 1,
    }, 
    {
        header: '站点',
        sortable: false,
        dataIndex: 'stationName',
        menuDisabled: true,
        align: 'center',
        flex: 1,
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
        //hidden: true
    },
    {
        header: '车辆来源',
        hidden:true,
        sortable: false,
        align: 'center',
        //dataIndex: 'entName',
        dataIndex: 'vehicleFromName',
        menuDisabled: true,
        flex: 1,
    }, 
    {
        header: '站点距出发地距离(千米)',
        align: 'center',
        sortable: false,
        //dataIndex: 'vehicleSource',
        menuDisabled: true,
        flex: 1,
        hidden: true
    }, 
    {
        header: '排量',
        sortable: false,
        align: 'center',
        dataIndex: 'vehicleOutput',
        menuDisabled: true,
        flex: 1,
        hidden: true
    }, 
    {
        header: '颜色',
        sortable: false,
        align: 'center',
        dataIndex: 'vehicleColor',
        menuDisabled: true,
        flex: 1,
        hidden: true
    }, 
    {
        header: '车辆状态',
        sortable: true,
        align: 'center',
        dataIndex: 'vehStatus',
        menuDisabled: true,
        flex: 1,
        renderer: function(val,metaData) {
            switch(val){
                case 0:
                    return '已出车';
                case 1:
                    return '待维修';
                case 2:
                    return '维修';
                case 3:
                    return '保养';
                case 4:
                    return '年检';
                case 5: 
                    return '备勤';
                case 6: 
                    return '机动';
                case 7: 
                    return '专用';
                case 8: 
                    return '不调度';
                case 9: 
                    return '计划锁定';
                case 10:
                    return '封存';
                case 11:
                    return '报废';
            }
        }
    }, 
    {
        header: '起始里程',
        sortable: false,
        align: 'center',
        dataIndex: 'stMileage',
        menuDisabled: true,
        flex: 1,
        field: {
            type: 'numberfield'
        }
    }, 
    {
    	header: '开始时间',
    	sortable: false,
    	align: 'center',
    	dataIndex: 'factStTime',
    	menuDisabled: true,
    	flex: 1.5,
    	field: {
            type: 'textfield'
        }
    }, 
    
    {
    	xtype : 'actioncolumn',
		items : [
		{
			xtype : 'button',
			tooltip : '实时定位',
			iconCls : 'x-fa fa-camera',
			handler : 'showAllotVehicleMapWindow',
			hidden: true
		},
		{
			xtype : 'button',
			tooltip : '查看车辆详细信息',
			iconCls : 'x-fa fa-camera',
			handler: 'viewVehicleInfo',
		},
		{
			xtype : 'button',
			tooltip : '查看任务',
			iconCls : 'x-fa fa-calculator',
			handler : 'toVehicleSchedule'
		}],

		cls : 'content-column',
		width : 120,
        align: 'center',
		// dataIndex: 'bool',
		text : '操作',
		//tooltip : 'edit '
    }
    ],
    
    dockedItems: [
	  {
	      xtype: 'pagingtoolbar',
	      id: 'AllocatingVehicle_id',
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
