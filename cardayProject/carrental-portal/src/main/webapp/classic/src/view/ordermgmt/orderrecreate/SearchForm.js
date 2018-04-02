/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.ordermgmt.orderrecreate.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    
    id: 'orderrecreate_searchform_id',
    reference: 'searchForm',
    controller: 'orderrecreatecontroller',
    padding: '0 0 10 0',
    itemId: 'searchForm',

    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    margin: '0 0 20 0', //对panel生效

    items: [
	{
		itemId: 'searchParm',
        xtype: 'form',
        layout: 'hbox',
        flex:5,
        items: [
		{
			id: 'orderrecreate_orderNo_id',
			fieldLabel: '订单编号',
			xtype: 'textfield',
			name: 'orderNo',
			labelWidth: 60,
			width:220,
			
		},
		{
			id: 'orderrecreate_orderTime_id',
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
			xtype:'textfield',
			name:'deptId',
			id:'ordermgmt_orderrecreate_deptId',
			hidden:true,
			//value: window.sessionStorage.getItem("organizationId"),
			listeners:{
        		afterrender: function(text){
        			text.setValue(window.sessionStorage.getItem("organizationId"));
        		}
		    },
			},{
			xtype : 'combo',
//			valueField : 'id',
//			name : 'deptId',
			name:'deptName',
			fieldLabel : '所属部门',
			labelWidth: 60,
			itemId : 'deptId',
			id:'ordermgmt_orderrecreate_deptName',
//			queryMode : 'remote',
			editable : false,
//			displayField : 'name',
//			value:'-1',
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
	},
	{
    	xtype:'container',
    	//flex:1,
    	items:[
	        { 
	        	xtype: 'button', 
	        	text: '补录订单',
	        	style:'float:right',
	        	handler:'recreateOrder',
	        	listeners:{
			        afterrender: function(){
		        		var userType = window.sessionStorage.getItem('userType');
		        		if(userType == '2' ||　userType == '6'){
			                this.hidden=true;
	                   }
		        	},
			    },
	        	
	        }]
    },],
    
    initComponent: function() {
        this.callParent();
    }
});
