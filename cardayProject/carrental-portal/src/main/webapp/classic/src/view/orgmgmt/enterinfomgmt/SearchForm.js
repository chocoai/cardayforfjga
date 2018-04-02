/**
 * 搜索公司名称
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.enterinfomgmt.SearchForm', {
    extend: 'Ext.panel.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    xtype:'SearchCompanyName',
 //   bodyPadding: 8,
    header: false,
   /* fieldDefaults: {
        // labelAlign: 'right',
        labelWidth: 40,
        margin: '0 20 0 0', //label和上一个输入框的样式，间隔5
        msgTarget: Ext.supports.Touch ? 'side' : 'qtip'
    },*/
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
   margin: '0 0 20 0', //对panel生效
    items: [{
        xtype: 'form',
        reference: 'searchEnterInfo',
        layout: 'hbox',
        flex:1,
       /* fieldDefaults: {
       		 msgTarget: 'qtip'
    	},*/
        items: [{
            name:'name',
            xtype: 'textfield',
            emptyText:'公司名称',
            width:220
//            maxLength:10,
//            allowBlank:false
        },{
        	xtype:'button',
//        	disabled: true,
//        	formBind: true,
        	text: '<i class="fa fa-search"></i>&nbsp;查询',
        	handler: 'onSearchClick'
        }]
    },{
    	xtype:'container',
//   	border: 5,
		style: {
//			    borderColor: 'red',
//			    borderStyle: 'solid',
			    float:'right'
		},
    	items:[{
    		xtype:'button',
    		text:'<i class="fa fa-plus"></i>&nbsp;新增',
    		handler:'onAddClick',
    		bind: {
                hidden: '{status === "0"}'
            }
    	}/*,{
    		xtype:'button',
    		text:'批量导入',
    		margin: '0 0 0 20',
    	//	margin: '0 0 0 620',
    		handler:function(btn){
    			Ext.Msg.alert('消息','导入成功');
    		}
    	}*/]
    	
    }],
    initComponent: function() {
        this.callParent();
    }
});
