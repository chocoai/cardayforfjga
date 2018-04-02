/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.systemmgmt.rolemgmt.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    id: 'role_searchform_id',
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
			text: '<i class="fa fa-plus"></i>&nbsp;新增角色',
			//style:'float:right',
			margin: '0 0 0 20',
			handler:'onAddClick'
		},
		{
        	xtype: "button",
    		text: "<i class='fa fa-upload'></i>&nbsp;批量导入",
    		margin: '0 0 0 20',
    		handler: "openFileUpWindow",
        },
		{
			xtype:'button',
			text: '<i class="fa fa-download"></i>&nbsp;批量导出',
			margin: '0 0 0 20',
			handler:'batchExport'
		},
		{
			xtype:'button',
			text: '<i class="fa fa-search"></i>&nbsp;导出权限参照数据',
			margin: '0 0 0 20',
			handler:'exportAuthorizationData'
		}
		]
	},
	{
        xtype: 'form',
        layout: 'hbox',
        //flex:1,  
        items: [
        {
            name:'rolename',
            xtype: 'textfield',
            emptyText:'角色名称',
            width:220,
        },
        {
        	xtype:'button',
        	text: '<i class="fa fa-search"></i>&nbsp;查询',
        	handler: 'searchByRolename'
        }]
	}],
    
    initComponent: function() {
        this.callParent();
    }
});
