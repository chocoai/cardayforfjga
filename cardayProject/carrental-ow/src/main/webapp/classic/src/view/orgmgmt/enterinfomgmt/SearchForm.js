/**
 * 搜索公司名称
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.enterinfomgmt.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],

    id:'enterinfosearchForm',

    reference: 'searchForm',

    header: false,
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
   margin: '0 0 20 0', //对panel生效
    items: [{
        xtype: 'form',
        layout: 'hbox',
        flex:1,
        fieldDefaults: {
            // labelAlign: 'right',
            labelWidth: 65,
            margin: '0 20 0 0', //label和上一个输入框的样式，间隔5
        },
        items: [{
            name:'name',
            xtype: 'textfield',
            emptyText:'企业名称',
            width:220
        },{
            xtype: 'combo',
            valueField: 'value',
            name:'status',
            fieldLabel: '服务状态',
            queryMode: 'local',
            editable: false,
            displayField: 'name',
            value: '-1',
            emptyText:'所有状态',
            store: {
                fields: ['name', 'value'],
                data: [{
                    "name": "所有状态",
                    "value": "-1"
                }, {
                    "name": "待审核",
                    "value": "0"
                }, {
                    "name": "审核未通过",
                    "value": "1"
                },{
                    "name": "待服务开通",
                    "value": "2"
                }, {
                    "name": "服务中",
                    "value": "3"
                }, {
                    "name": "服务到期",
                    "value": "4"
                },{
                    "name": "服务暂停",
                    "value": "5"
                }]
            }
        },{
        	xtype:'button',
        	text: '<i class="fa fa-search"></i>&nbsp;查询',
        	handler: 'onSearchClick'
        }]
    },{
    	xtype:'container',
		style: {
			float:'right'
		},
    	items:[{
    		xtype:'button',
    		text:'新增',
    		handler:'onAddClick',
    	}]
    	
    }],
    initComponent: function() {
        this.callParent();
    }
});
