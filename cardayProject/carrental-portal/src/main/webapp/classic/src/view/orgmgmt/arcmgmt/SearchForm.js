/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.arcmgmt.SearchForm', {
	extend : 'Ext.form.Panel',
	requires : [ 'Ext.layout.container.HBox',
	        'Ext.form.field.Date',
	        'Ext.form.FieldContainer',
	        'Ext.form.field.ComboBox',
	        'Ext.form.FieldSet',
	],
	reference : 'searchForm',
//	bodyPadding : 10,
	header : false,
	layout: {
        type: 'hbox',
        align: 'stretch'
    },
//    border:true,
    margin: '0 0 20 0',
    items: [
//            {
//        xtype: 'form',
//        layout: 'hbox',
//        items: [{
//            //id: 'imei',
//            name:'department_id',
//            xtype: 'textfield',
//            emptyText:'部门编号',
//    //        labelWidth: 60,
//            width:220
//     //       fieldLabel: 'Obd NO',
//        },{
//        	xtype:'button',
//        text: '<i class="fa fa-search"></i>&nbsp;查询',
//        //用iconCls会出现图标偏移
//     //   width: 60,
//    //    height: 30,
//        handler: 'searchClick'
//        }]
//    },
    {
    	xtype:'container',
    	flex:1,
    	items:[{
    		xtype:'button',
    		text:'新增部门',
    		//style:'float:right',
    		handler:'addDepartmentClick',
    		listeners:{
		        afterrender: function(){
	        		var userType = window.sessionStorage.getItem('userType');
	        		if(userType != '2' && userType != '6'){
		                this.hidden=true;
                   }
	        	},
		    },
    	}]
	}],
	initComponent : function() {
		this.callParent();
	}
});
