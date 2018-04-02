/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.ordermgmt.orderaudit.SearchFormAudit', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    id: 'orderaudit_searchformaudit_id',
    reference: 'SearchFormAudit',
    controller: 'orderauditcontroller',
    header: false,
    padding: '0 0 10 0',
    
    items: [
        	{
        		itemId: 'searchParm',
                xtype: 'form',
                layout: 'hbox',
                flex:5,
                items: [
        		{
        			fieldLabel: '订单编号',
        			xtype: 'textfield',
        			name: 'orderNo',
        			labelWidth: 60,
        			width:220,
        			
        		},	
        		{
        			margin: '0 0 0 14',
        			fieldLabel: '订单日期',
        			name: 'orderTime',
        			xtype: 'datefield',
        			editable:false,
        			width:220,
        			labelWidth: 60,
        			format: 'Y-m-d',
        			maxValue: new Date()  // limited to the current date or prior
        		},
        		{
        			margin: '0 0 0 14',
        			fieldLabel: '预约用车日期',
        			name: 'planTime',
        			xtype: 'datefield',
        			editable:false,
        			width:250,
        			labelWidth: 90,
        			format: 'Y-m-d',
        		},
        		{
        			xtype:'textfield',
        			name:'deptId',
        			id:'ordermgmt_orderaudit_deptId',
        			hidden:true,
        			//value: window.sessionStorage.getItem("organizationId"),
        			listeners:{
                		afterrender: function(text){
                			text.setValue(window.sessionStorage.getItem("organizationId"));
                		}
        		    },
        			},{
        			xtype : 'combo',
//        			valueField : 'id',
//        			name : 'deptId',
        			name:'deptName',
        			fieldLabel : '所属部门',
        			labelWidth: 60,
        			itemId : 'deptId',
        			id:'ordermgmt_orderaudit_deptName',
//        			queryMode : 'remote',
        			editable : false,
//        			displayField : 'name',
//        			value:'-1',
        			width:220,
        			margin: '0 0 0 14',
        			emptyText : '请选择...',
        			//value: window.sessionStorage.getItem("organizationName"),
        			listeners:{
                		expand: 'openDeptChooseWin',
                		afterrender: function(combo){
                			combo.setValue(window.sessionStorage.getItem("organizationName"));
                		}
        		    },
        		}, {
        			xtype:'checkboxgroup',
        			hideLabel:true,
        	        columns: 2,
        	        width:155,
        	        flex:null,
        	        vertical: true,
        	        items: [
        	            { boxLabel: '本部门', name: 'includeSelf', inputValue: true,checked: true, 
        	            	width:65,
        	            	style:{
        	            		marginLeft:'5px',
        	            	}},
        	            { boxLabel: '子部门', name: 'includeChild', inputValue: true,checked: true,
        	            	style:{
        	            		marginRight:'0',
        	            	}},
        	        ],
        	        allowBlank : false,
            		blankText : '不能为空'
        		},
                { 
                    margin: '0 0 0 0',
                    xtype: 'button', 
                    text: '查询',
                    handler: 'onSearchClick'
                        
                },
                { 
                    margin: '0 0 0 14',
                    xtype: 'button', 
                    text: '重置 ',
                    handler:'onResetClick'
                },
                ]
        	}],
    
    initComponent: function() {
        this.callParent();
    }
});
