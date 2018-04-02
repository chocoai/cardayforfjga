Ext.define('Admin.view.ordermgmt.orderallocate.DriverAllocatedWindow', {
	extend: 'Ext.window.Window',

    alias: "widget.driverAllocatedWindow",
    controller: 'allocatingViewController',
	reference: 'driverAllocatedWindow',
	title : '确认分配信息',
	width : 400,
	//height : 300,
	closable: true,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
//	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
//	frame : true,
//	layout: {
//        type: 'vbox',
//        pack: 'start',
//        align: 'stretch'
//    },
//    listeners:{
//    	afterrender: 'loadVehicleInfo',
//
//    },
//    bodyPadding: 8,
	items : [ 
	{
		
		id:'vehicle_driver_allocated_id',
		xtype:'form',
		layout : 'vbox',
		defaultType: 'displayfield',
		
		bodyStyle: {
			//  background: '#ffc',
			    padding: '15px'  //与边界的距离
		},
		
		fieldDefaults: {
            labelAlign: 'left',
            labelWidth: 100,
            height:20,    
        },
        
        items: [
		  {
			  xtype: 'tbtext',
			  text: '<h3>请确认车辆分配信息</h3>',
		  },
		  {
			  fieldLabel: '车辆Id',
			  name: 'vehicleId',
			  hidden: true,
		  }, 
	      {
			  fieldLabel: '车牌号',
			  name: 'vehicleNumber',
	      }, 
	      {
	    	  fieldLabel: '车辆品牌',
	    	  name: 'vehicleBrand',
	      }, 
	      {
	    	  fieldLabel: '车型',
	    	  name: 'vehicleModel',
	      }, 
	      {
	    	  fieldLabel: '公车性质',
	    	  name: 'vehicleType',
	      },
	      {
	    	  fieldLabel: '座位数',
	    	  name: 'seatNumber',
	      },
	      {
	    	  fieldLabel: '排量',
	    	  name: 'vehicleOutput',
	    	  hidden: true
	      },
	      {
	    	  fieldLabel: '颜色',
	    	  name: 'vehicleColor',
	    	  hidden: true
	      },
	      {
				xtype: 'tbtext',
				text: '<h3>请确认司机分配信息<h3>',
		  },
	      {
	    	  fieldLabel: '司机Id',
	          name: 'driverId',
	          hidden: true
	      },
	      {
	    	  fieldLabel: '司机姓名',
	    	  name: 'realname',
	      },
	      {
	    	  fieldLabel: '司机电话',
	          name: 'phone',
	      },
	      {
	    	  fieldLabel: '所属站点',
	          name: 'stationName',
	      },
	      {
	    	  fieldLabel: '起始里程',
	    	  name: 'stMileage',
	    	  hidden: true
	      },
	      {
	    	  fieldLabel: '实际开始时间',
	    	  name: 'factStTime',
	    	  hidden: true
	      },
	      ]
	}],

	buttonAlign : 'center',
	buttons : [
		{
			text : '确认分配',
			handler: 'vehicleAndDriverAlloDone',
		},
		{
			text : '取消',
			handler: function(btn) {
				btn.up('driverAllocatedWindow').close();
			}
		}
	],
    
    initComponent: function() {
        this.callParent();
    }
});