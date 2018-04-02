Ext.define('Admin.view.vehiclemgmt.vehicleInfomgmt.AddVehicle', {
    extend: 'Ext.window.Window',
	requires: [
		'Ext.form.Panel'
	],    
	reference: 'addVehicle',
    bodyPadding: 10,
    constrain: true,
    closable: true,
    resizable: false,
    modal: true,
    resizable: false,// 窗口大小是否可以改变
	title: '添加车辆信息',
	items: [{
			 xtype:'form',
			 width: 610,
			 minWidth: 610,
			 minHeight: 300,
   			 layout: 'column',
		     defaults: {
		        layout: 'form',
		        xtype: 'container',
		        defaultType: 'textfield',
		        style: 'width: 50%'
		     },

		     items: [{
		        items: [
		            { fieldLabel: '车牌号',name: 'vehicleNumber' },
		            { fieldLabel: '车辆品牌',name: 'vehicleBrand' },
		            { fieldLabel: '车架号',name: 'vehicleIdentification' },
		            { fieldLabel: '车辆颜色',name: 'vehicleColor'},
		            { fieldLabel: '排量',name: 'vehicleOutput'},
		            { 
		            	xtype: 'combo',
				        valueField: 'value',
				        fieldLabel: '车辆所属部门',
				        queryMode: 'local',
				        editable:false,
				        displayField:'name',
				        name: 'currentuseOrgId',
				        width:180,
				        value:'',
				        store:{
				            fields: ['name', 'value'],
				            data : [
				                {"name":"首汽", "value":"首汽"},
				                {"name":"中国移动", "value":"中国移动"}
				        ]}
	      			},{
				        xtype: 'combo',
				        valueField: 'value',
				        fieldLabel: '准驾类型',
				        queryMode: 'local',
				        editable:false,
				        displayField:'name',
				        name: 'licenseType',
				        width:180,
				        value:'',
				        store:{
				            fields: ['name', 'value'],
				            data : [
				                {"name":"A1", "value":"A1"},
				                {"name":"A2", "value":"A2"},
				                {"name":"A3", "value":"A3"},
				                {"name":"B1", "value":"B1"},
				                {"name":"B2", "value":"B2"},
				                {"name":"C1", "value":"C1"},
				                {"name":"C2", "value":"C2"}
				        	]
				        }
	      		}]
		     },{
		        items: [{
				        xtype: 'combo',
				        valueField: 'value',
				        name: 'vehicleType',
				        fieldLabel: '公车性质',
				        queryMode: 'local',
				        editable:false,
				        displayField:'name',
				        value:'',
				        store:{
				            fields: ['name', 'value'],
				            data : [
				                {"name":"所有类型", "value":""},
				                {"name":"应急机要通信接待用车", "value":"应急机要通信接待用车"},
				                {"name":"行政执法用车", "value":"行政执法用车"},
				                {"name":"行政执法特种专业用车", "value":"行政执法特种专业用车"},
				                {"name":"一般执法执勤用车", "value":"一般执法执勤用车"},
				                {"name":"执法执勤特种专业用车", "value":"执法执勤特种专业用车"}
				        ]}
			      	   },
		            { fieldLabel: '车辆型号',name: 'vehicleModel' },
		            {
           				xtype: 'fieldcontainer',
			            fieldLabel: '理论油耗',
			            combineErrors: false,
            			layout: 'hbox',
			            defaults: {
			                hideLabel: true,
			                margin: '0 5 0 0'
			            },
			            items: [{
			               name : 'theoreticalFuelCon',
			               xtype: 'numberfield',
			               minValue: 0,
			               width:120
			               //allowBlank: false
			           }, {
			               xtype: 'displayfield',
			               value: 'L/百公里'
			           }]
			        },
		            { fieldLabel: '车辆座位数',name: 'seatNumber' },
		            {
				        xtype: 'combo',
				        valueField: 'value',
				        fieldLabel: '燃油号',
				        queryMode: 'local',
				        editable:false,
				        displayField:'name',
				        name: 'vehicleFuel',
				        width:180,
				        value:'',
				        store:{
				            fields: ['name', 'value'],
				            data : [
				                {"name":"90(京89)", "value":"90(京89"},
				                {"name":"93(京92)", "value":"93(京92)"},
				                {"name":"97(京95)", "value":"97(京95)"}
				        	]
				        }
	      		},
		            { fieldLabel: '车辆所属城市',name: 'city' },
		            { fieldLabel: '购买时间',xtype:'datefield',name: 'vehicleBuyTime', format:'Y-m-d'}
		        	
		        ]
		     }]
	}],
	buttons: [{
		text: '添加',
		handler: function(){
			this.up('window').close();
		}
	},{
		text: '关闭',
		handler: function(){
			this.up('window').close();
		}
	}]
});