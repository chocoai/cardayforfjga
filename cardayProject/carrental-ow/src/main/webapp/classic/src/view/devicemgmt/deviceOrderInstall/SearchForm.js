/**
 * 搜索公司名称
 * This class is the template view for the application.
 */
Ext.define('Admin.view.devicemgmt.deviceOrderInstall.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],

    id:'deviceOrderInstallSearchForm',
    reference: 'searchForm',

    header: false,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
   margin: '0 0 20 0', //对panel生效
    items: [{
        xtype: 'form',
        layout: 'hbox',
        flex:1,
        items: [{
            name:'orderNumber',
            xtype: 'textfield',
            fieldLabel: '预约单号',
            emptyText:'请输入预约单号',
            labelWidth:60,
            margin: '0 10 0 0',
        },/*{
            margin: '0 10 0 0',
            xtype: 'combo',
            valueField: 'id',
            itemId: 'corporateCustomer',
            name: 'corporateCustomer',
            fieldLabel: '企业客户',
            labelWidth:60,
            queryMode: 'local',
            editable: false,
            displayField: 'corporateCustomer',
            emptyText: '请选择企业客户',
            value: 1001,
            store: Ext.create('Ext.data.Store', {
                proxy: {
                    type: 'ajax',
                    url: 'app/data/devicemgmt/deviceOrderInstall/corporateCustomer.json',
                    reader: {
                        type: 'json',
                        rootProperty: 'data',
                        successProperty: 'status'
                    }
                },
                autoLoad: true,
            })
        },*/{
            width: 150,
            xtype : 'combo',
            margin: '0 10 0 0',
            valueField : 'value',
            name : 'status',
            fieldLabel : '状态',
            labelWidth:40,
            queryMode : 'local',
            editable : false,
            displayField : 'name',
            emptyText : '请选择状态',
            value:'-1',
            store : {
                fields : ['name', 'value'],
                data : [
                {
                    "name" : "全部",
                    "value" : "-1"
                },{
                    "name" : "已完成",
                    "value" : "0"
                }, {
                    "name" : "未完成",
                    "value" : "1"
                }]
            }
        },{
            name:'startTime',
            fieldLabel: '预约开始日期',
            xtype: 'datefield',
            emptyText: '请选择开始日期',
            format: 'Y-m-d',
            vtype: 'daterange',
            itemId: 'startdt',
            endDateField: 'enddt',
            editable: false,
            labelWidth:90,
            margin: '0 10 0 0',
        },{
            name:'endTime',
            fieldLabel: '预约结束日期',
            emptyText: '请选择结束日期',
            xtype: 'datefield',
            format: 'Y-m-d',
            vtype: 'daterange',
            itemId: 'enddt',
            editable: false,
            startDateField: 'startdt',
            labelWidth:90,
            margin: '0 10 0 0',
        },{
        	xtype:'button',
        	text: '<i class="fa fa-search"></i>&nbsp;查询',
        	handler: 'onSearchClick'
        }]
    },{
        margin: '10 0 0 0',
    	xtype:'container',
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
