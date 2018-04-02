Ext.define('Admin.view.ordermgmt.orderavoidapprove.ViewOrder', {
	extend: 'Ext.window.Window',
					
    alias: "widget.orderavoidapprovewin",
	reference: 'vieworder',
	title : '免审批订单信息',
    minWidth : 400,
    maxHeight: 600,
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
			fieldLabel: '订单编号',
	        name: 'orderNo',
		},
		{
			fieldLabel: '单位名称',
	        name: 'unitName',
		},
		{
			fieldLabel: '车牌号',
	        name: 'vehicleNumber',
		},
		{
			fieldLabel: '申请日期',
	        name: 'orderTime',
            renderer:function(v, cellValues, rec){
                if(v != null){
                    var date = new Date();
                    date.setTime(v);
                    var month = date.getMonth(date);
                    month = month + 1;
                    var day = date.getDate(date);
                    var hour = date.getHours(date);
                    var minutes = date.getMinutes(date);
                    var seconds = date.getSeconds(date);
                    if(month < 10){
                        month =  '0' + month;
                    }
                    if(day < 10){
                        day =  '0' + day;
                    }
                    return date.getFullYear() + "-" + month + "-" + day ;
                }else{
                    return '';
                }
            }
		},
		{
			fieldLabel: '用车人',
	        name: 'orderUsername',
		}, 
		{
			fieldLabel: '电话',
	        name: 'orderUserphone',
		},
		{
			fieldLabel: '订单状态',
	        name: 'status',
            renderer : function(value,metaData) {
                switch (value) {
                case 0:
                    value='待审核';
                    break;
                case 1:
                    value='待排车';
                    break;
                case 2:
                    value='已排车';
                    break;
                case 3:
                    value='进行中';
                    break;
                case 4:
                    value='待支付';
                    break;
                case 5:
                    value='被驳回';
                    break;
                case 6:
                    value='已取消';
                    break;
                case 11:
                    value='已出车';
                    break;
                case 12:
                    value='已到达出发地';
                    break;
                case 13:
                    value='等待中';
                    break;
                case 15:
                    value='待评价';
                    break;
                case 16:
                    value='已完成';
                    break;
                }
                return value;
            }
		},
		{
			fieldLabel: '事由',
			name: 'orderReason',
		}
		]
	}],

	buttonAlign : 'center',
	buttons : [{
				text : '关闭',
				handler: function(btn){
					btn.up('orderavoidapprovewin').close();
				}
			}
	]
});