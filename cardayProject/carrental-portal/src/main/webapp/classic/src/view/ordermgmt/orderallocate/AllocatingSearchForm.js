/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.ordermgmt.orderallocate.AllocatingSearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    id: 'allocatingsearchform_id',
    reference: 'allocatingSearchForm',
    controller: 'allocatingViewController',
    header: false,
    padding: '5 0 0 5',
    
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
        			xtype:'textfield',
        			name:'deptId',
        			id:'ordermgmt_orderallocating_deptId',
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
        			id:'ordermgmt_orderallocating_deptName',
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
                    handler: 'searchAllotingOrderById'
                        
                },
                { 
                    margin: '0 0 0 14',
                    xtype: 'button', 
                    text: '重置 ',
                    handler:'onResetClick'
                },
                ]
        	}],
        	
        	dockedItems: [{
        	    xtype: 'toolbar',
        	    dock: 'bottom',
        	    ui: 'footer',
        	    style : "background-color:#FFFFFF",
        	    defaults: {
        	        //minWidth: 200
        	    },
        	    items: [
        	        { 
        	        	xtype: 'component', 
        	        	flex: 1 
        	        },
        	    ]
        	}],
    
    initComponent: function() {
        this.callParent();
    }
});
