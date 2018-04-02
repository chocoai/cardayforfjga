Ext.define('Admin.view.systemconfiguration.oCardConfiguration.ViewOCard', {
	extend: 'Ext.window.Window',	
   requires : ['Ext.layout.container.Table',
				'Ext.button.Button' ],
	reference: 'viewOCard',
    bodyPadding: 10,
	title: '查看O牌审批设置',
	width : 300,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	frame : true,
	items : [{
		xtype:'form',
		margin:'0 20 0 20',
		layout : 'vbox',
		defaultType: 'displayfield',
		fieldDefaults: {
            labelWidth: 100,
        },       
		items: [
			{
				fieldLabel: '模式名称',
			    name: 'patternName',
			}, 
			{
				fieldLabel: '审批级数',
		        name: 'approvalRating',
		        renderer: function (value, field) {
		        	switch(value){
	        			case '0':
	        				return '一级';
	        			case '1':
	        				return '二级';
	        			case '2':
	        				return '三级';
	        			case '3':
	        				return '四级';
	                    case '4':
	                        return '五级';
	    			}
		        }
			},
			 {
	      		fieldLabel: '第一级节点',
	            name: 'adminNameFirst',
	        },
	        {
				fieldLabel: '第二级节点',
		        name: 'adminNameSecond',
			}, 
			{
				fieldLabel: '第三级节点',
		        name: 'adminNameThird',
			},
			{
				fieldLabel: "第四级节点",
		        name: 'adminNameForth'
		    },
		    {
				fieldLabel: "第五级节点",
		        name: 'adminNameFifth',
		    },				
		]
	}],

	buttonAlign : 'center',
	buttons : [{
				text : '关闭',
				handler: function(btn){
					btn.up('window').close();
				}
			}]
});