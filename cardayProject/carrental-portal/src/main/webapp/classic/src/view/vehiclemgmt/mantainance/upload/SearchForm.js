/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.mantainance.upload.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    reference: 'searchForm',
    //bodyPadding: 8,
    header: false,
    
//    fieldDefaults: {
//        // labelAlign: 'right',
//        labelWidth: 40,
//        margin: '0 20 0 0', //label和上一个输入框的样式，间隔5
//        msgTarget: Ext.supports.Touch ? 'side' : 'qtip'
//    },
    
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    margin: '0 0 20 0', //对panel生效
    
    items: [
	{
    	xtype:'container',
    	flex:1,
//    	margin:'10 0 0 0',
    	items:[{
    		xtype:'button',
    		text:'<i class="fa fa-download"></i>&nbsp;下载导入文件模板',
    		width:180,
    		style:'float:right',
    		handler:function() {
    			window.location.href = './vehicle/down';
    		}
    	},{
    		xtype:'field',
    		style:'float:right',
    		width:5,
    	},{
    		xtype:'button',
    		text:'<i class="fa  fa-upload"></i>&nbsp;导入',
    		width:120,
    		style:'float:right',
//    		handler:'onAddClick'
    	},{
    		xtype:'field',
    		style:'float:right',
    		width:5,
    	},{
    		xtype:'button',
    		text:'<i class="fa  fa-plus"></i>&nbsp;新增',
    		width:120,
    		style:'float:right',
    		handler:'onAddClick',
    	}]
	}
	
    ],
    
    initComponent: function() {
        this.callParent();
    }
});
