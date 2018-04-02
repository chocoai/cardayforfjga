Ext.define('Admin.view.orgmgmt.enterinfomgmt.VehicleAllocation', {
	extend: 'Ext.window.Window',	
  //  alias: "widget.vehicleAllocation",
	title : '分配车辆',
	width : 600,
	height:300,
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
//	bodyStyle : "background-color:#FFF0F5;padding:10px 5px",	
	frame : true,
	items:[{
		xtype:'grid',
		viewModel : {
			type : 'enterInfoModel' // search-ResultsModel.js
		},
		bind : '{carsResults}',
		scrollable : false,
/*		selModel: {
            type: 'checkboxmodel',
            checkOnly: false
        },*/
		//单选
		selModel: {
		//	type: 'spreadsheet',
			type: 'checkboxmodel',
            mode:'SINGLE'
        },
		columns : [{
					xtype : 'gridcolumn',
					width : 120,
					dataIndex : 'carNo',
					text : '车牌号'
				  },{
					xtype : 'gridcolumn',
					width : 120,
					dataIndex : 'carMark',
					text : '车辆品牌'
				  },{
					xtype : 'gridcolumn',
					width : 120,
					dataIndex : 'carStyle',
					text : '车辆型号'
				  },{
					xtype : 'gridcolumn',
					width : 120,
					dataIndex : 'seatsNumber',
					text : '座位数'
				  },{
					xtype : 'gridcolumn',
					sortable: false,
					menuDisabled: true,
					dataIndex : 'carColor',
					text : '颜色'
				  }
		]
	}],

/*	items : [
	{
		fieldLabel: '公司名称',
		xtype: 'textfield',
        name: 'compName'
	},{
		fieldLabel: '公司简称',
		xtype: 'textfield',
        name: 'compAlias'
	},
	{
		fieldLabel: '联系人',
		xtype: 'textfield',
        name: 'linkMan'
	},
	{
		fieldLabel: '联系电话',
		xtype: 'textfield',
        name: 'phone'
	},
	{
		fieldLabel: '联系人邮箱',
		xtype: 'textfield',
        name: 'mail'
	},
	{
		fieldLabel: '服务起始日期',
		xtype: 'datefield',
        name: 'startDate'
	},
	{
		fieldLabel: '服务到期日期',
		xtype: 'datefield',
        name: 'startDate'
	},
	{
		fieldLabel: '公司地址',
		xtype: 'textfield',
        name: 'address'
	},
		{
        xtype:'textarea',
        fieldLabel:'公司介绍'
	},

	{
            xtype      : 'fieldcontainer',
            fieldLabel : '短租业务开通',
            defaultType: 'radiofield',
            defaults: {
                flex: 1
            },
            layout: 'hbox',
            items: [
                {
                    boxLabel  : '是',
                    name      : 'color',
                    inputValue: 'blue',
                    checked:true
                }, {
                    boxLabel  : '否',
                    name      : 'color',
                    inputValue: 'grey'
                }
            ]
        }
       ],*/

	buttonAlign : 'center',
	buttons : [{
				id : 'button1',
				text : '关闭',
				handler: function(btn){
					btn.up("vehicleAllocation").close();
				}
			},{
				id: 'button2',
				text: '分配',
				handler: function(){
					alert('分配成功');
				}
			} ]
});