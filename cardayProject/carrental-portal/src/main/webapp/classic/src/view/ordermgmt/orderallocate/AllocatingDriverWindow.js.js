Ext.define('Admin.view.ordermgmt.orderallocate.AllocatingDriverWindow', {
	extend: 'Ext.window.Window',

    alias: "widget.allocatingDriverWindow",
    controller: 'allocatingViewController',
	reference: 'allocatingDriverWindow',
	title : '司机分配',
	width : 650,
	//height : 400,
	closable:true,//右上角x关闭是否显示
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
 
    bodyPadding: 8,
 /*   defaults: {  //对items的组件生效
        frame: true,
    },*/
    margin: '5 5 50 5',
    
    viewModel: {
        xclass: 'Admin.view.ordermgmt.orderallocate.DriverViewModel'
    },
    listeners:{
    	//afterrender: 'onBeforeLoadDriver'
    },
    items : [ 
     {
         xtype: 'gridpanel',
         id: 'AllocatingDriverWindow_grid_id',
         bind: {
          	store : '{driversResults}'
         },
         scrollable: false,
         frame: true,
         selModel: {
     		injectCheckbox: 0,
     		mode: 'SINGLE',
     		checkOnly: true,
     		allowDeselect:false
     	},
         columns: [
			 {
				 header:'选择',
				 xtype:'templatecolumn',
				 tpl:'<input name=driver type=radio value={id}" />'
			 },
	         {
	             header: '序号',
	             sortable: false,
	            menuDisabled: true,
	            align: 'center',
	             dataIndex: 'id',
	             flex: 1
	         }, 
	         {
	         	header: '司机姓名',
	             sortable: false,
	            menuDisabled: true,
	            align: 'center',
	             dataIndex: 'realname',
	             flex: 1
	         }, 
	         {
	             header: '司机电话',
	             sortable: false,
	            menuDisabled: true,
	            align: 'center',
	             dataIndex: 'phone',
	             flex: 1
	         }
         ],
         dockedItems: [
	 	      {
	 	          xtype: 'pagingtoolbar',
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
	 	          bind: {
	 	          	//store: '{employeeStore}'
	 	          },
	 	          displayInfo: true
	 	      }
	 	],
    }],
	
    
	
	buttonAlign : 'center',
	buttons : [
		{
			text : '完成',
			handler: 'driverAlloDone',
		}
	],
    
    initComponent: function() {
        this.callParent();
    }
});