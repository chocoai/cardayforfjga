Ext.define('Admin.view.carnumbermgmt.specificcarapply.ViewApply', {
	extend: 'Ext.window.Window',
	
    alias: "widget.viewapply",
	reference: 'viewapply',
	title : '闽"O"号牌申请信息查看',
    minWidth : 300,
    maxHeight: 600,
    scrollable: true,
	closable: true,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	items : [{
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
			fieldLabel: '申请单位',
	        name: 'applyOrganization',
		}, 
		{
			fieldLabel: '品牌型号',
	        name: 'vehicleModel',
		},
		{
			fieldLabel: '公车性质',
	        name: 'vehicleType',
	        renderer: function (value, field) {
	        	switch(value){
	    			case "0":
	    				return '应急机要通信接待用车';
	    			case "1":
	    				return '行政执法用车';
	    			case "2":
	    				return '行政执法特种专业用车';
	    			case "3":
	    				return '一般执法执勤用车';
	    			case "4":
	    				return '执法执勤特种专业用车';
    			}
	        }
		},
		{
			fieldLabel: '车架号',
	        name: 'vin',
		},
		{
			fieldLabel: '发动机号',
			name: 'engineNumber',
		},
		{
			fieldLabel: '联系人',
			name: 'contactPerson',
		},
		{
			fieldLabel: '电话',
			name: 'phone',
		},
		{
			fieldLabel: '申请理由',
			name: 'applyReason',
		},
		{
			fieldLabel: '申请时间',
			name: 'applyTime',
		},
		{
			fieldLabel: '状态',
			name: 'status',
	        renderer: function (value, field) {
	        	switch(value){
	    			case '0':
	    				return '申请中';
	    			case '1':
	    				return '已驳回';
	    			case '2':
	    				return '审核通过';
    			}
	        }
		},
		]
	}],

	buttonAlign : 'center',
	buttons : [{
				text : '关闭',
				handler: function(btn){
					btn.up('viewapply').close();
				}
			}
	]
});