/**
 * 搜索公司名称
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.dialrecordmgmt.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],

    id:'dialrecordsearchForm',
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
        items: [{
            name:'dialPhone',
            xtype: 'textfield',
            fieldLabel: '来电号码',
            emptyText:'请输入来电号码',
            width:220,
            labelWidth:60,
            margin: '0 20 0 0',
            msgTarget: 'side',
            regex: /^\d{0,11}$/,
            regexText: '请输入至多11位数字',
        },{
            name:'dialName',
            xtype: 'textfield',
            fieldLabel: '来电人姓名',
            emptyText:'请输入来电人姓名',
            width:220,
            labelWidth:70,
            margin: '0 20 0 0',
        },{
        	id:'startTime',
            name:'startTime',
            fieldLabel: '开始日期',
            xtype: 'datefield',
            emptyText: '请选择开始日期',
            format: 'Y-m-d',
            vtype: 'daterange',
            itemId: 'startdt',
            endDateField: 'enddt',
           editable: false,
            maxValue : new Date(),
            labelWidth:60,
            margin: '0 20 0 0',
        },{
        	id:"endTime",
            name:'endTime',
            fieldLabel: '结束日期',
            emptyText: '请选择结束日期',
            xtype: 'datefield',
            format: 'Y-m-d',
            vtype: 'daterange',
            itemId: 'enddt',
            editable: false,
            startDateField: 'startdt',
            maxValue : new Date(),
            labelWidth:60,
            margin: '0 20 0 0',
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
