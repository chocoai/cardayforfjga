/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.systemmgmt.usermgmt.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    id: 'user_searchfomr_id',
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
    	items:[{
    		xtype:'button',
    		text:'<i class="fa fa-plus"></i>&nbsp;新增用户',
    		//style:'float:right',
    		handler:'onAddClick',
    		margin: '0 20 0 0',
    	},
    	{
        	xtype: "button",
    		text: "<i class='fa fa-upload'></i>&nbsp;批量导入",
    		handler: "openFileUpWindow",
        }]
	},
	{
        xtype: 'form',
        layout: {
            type: 'hbox',
            align: 'stretch'
        },
        //flex:1,
        items: [{
            name:'realname',
            xtype: 'textfield',
            emptyText:'用户姓名',
            width:220
        	},{
        	xtype:'button',
        	text: '<i class="fa fa-search"></i>&nbsp;查询',
        	handler: 'onSearchClick',
        }]
	},

    ],
    
    initComponent: function() {
        this.callParent();
    }
});
