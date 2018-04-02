/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.carnumbermgmt.carnumberapply.SearchFormApply', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    reference: 'SearchFormAudited',
    header: false,
    margin: '0 0 10 0', //对panel生效
    
    items: [
        	{
        		itemId: 'searchParm',
                xtype: 'form',
                layout: 'hbox',
                flex:5,
                items: [
//        		{
//        			fieldLabel: '订单编号',
//        			xtype: 'textfield',
//        			name: 'orderNo',
//        			labelWidth: 60,
//        			width:200,
//        		},
//        		{
//        			margin: '0 0 0 10', //上，右，下，左
//        			fieldLabel: '订单状态',
//        			name: 'status',
//        			xtype: 'combo',
//        			labelWidth: 60,
//        			width:158,
//        			editable:false,
//        			displayField:'value',
//        			valueField: 'id',
//        			value: '全部',
//        			store:{
//        			    fields: ['id', 'value'],
//        			    data : [
//        			        {"id":"-1", "value":"全部"},
//        			        {"id":"1", "value":"待排车"},
//        			        {"id":"2", "value":"已排车"},
//        			        {"id":"3", "value":"进行中"},
//							{"id":"11", "value":"已出车"},
//							{"id":"12", "value":"已到达"},
//        			        {"id":"13", "value":"等待中"},
//        			        {"id":"4", "value":"待支付"},
//        			        {"id":"5", "value":"被驳回"},
//        			        {"id":"15", "value":"待评价"},
//        			]}
//        		},
//        		
//        		{
//        			margin: '0 0 0 10',
//        			fieldLabel: '订单日期',
//        			name: 'orderTime',
//        			xtype: 'datefield',
//        			editable:false,
//        			width:186,
//        			labelWidth: 60,
//        			format: 'Y-m-d',
//        			maxValue: new Date()  // limited to the current date or prior
//        		},
//        		{
//        			margin: '0 0 0 10',
//        			fieldLabel: '预约用车日期',
//        			name: 'planTime',
//        			xtype: 'datefield',
//        			editable:false,
//        			width:216,
//        			labelWidth: 90,
//        			format: 'Y-m-d',
//        		},
//        		{
//        			xtype:'textfield',
//        			name:'deptId',
//        			hidden:true,
//        			//value: window.sessionStorage.getItem("organizationId"),
//        			listeners:{
//                		afterrender: function(text){
//                			text.setValue(window.sessionStorage.getItem("organizationId"));
//                		}
//        		    },
//        			},{
//        			xtype : 'combo',
////        			valueField : 'id',
////        			name : 'deptId',
//        			name:'deptName',
//        			fieldLabel : '所属部门',
//        			labelWidth: 60,
//        			itemId : 'deptId',
////        			queryMode : 'remote',
//        			editable : false,
////        			displayField : 'name',
////        			value:'-1',
//        			width:210,
//        			margin: '0 0 0 10',
//        			emptyText : '请选择...',
//        			//value: window.sessionStorage.getItem("organizationName"),
//        			listeners:{
//                		expand: 'openDeptChooseWin',
//                		afterrender: function(combo){
//                			combo.setValue(window.sessionStorage.getItem("organizationName"));
//                		}
//        		    },
//        		}, {
//        			xtype:'checkboxgroup',
//        			hideLabel:true,
//        	        columns: 2,
//        	        width:155,
//        	        flex:null,
//        	        vertical: true,
//        	        items: [
//        	            { boxLabel: '本部门', name: 'includeSelf', inputValue: true,checked: true, 
//        	            	width:65,
//        	            	style:{
//        	            		marginLeft:'5px',
//        	            	}},
//        	            { boxLabel: '子部门', name: 'includeChild', inputValue: true,checked: true,
//        	            	style:{
//        	            		marginRight:'0',
//        	            	}},
//        	        ],
//        	        allowBlank : false,
//            		blankText : '不能为空'
//        		},
//                { 
//                    margin: '0 0 0 0',
//                    xtype: 'button', 
//                    text: '查询',
//                    handler: 'onSearchClick'
//                        
//                },
                { 
                    margin: '0 0 0 0',
                    xtype: 'button', 
                    text: '新增',
                    handler:'addApply'
                },
                ]
        	}],
    
    initComponent: function() {
        this.callParent();
    }
});
