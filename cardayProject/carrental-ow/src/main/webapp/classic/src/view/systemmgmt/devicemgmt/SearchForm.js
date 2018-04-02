/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.systemmgmt.devicemgmt.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    reference: 'searchForm',
    id: 'devicemgmtsearchForm',
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
    margin: '0 0 0 0', //对panel生效
    
    items: [
	{
        xtype: 'form',
        layout: {
            type: 'hbox',
            align: 'stretch'
        },
        margin: '0 0 20 0', //对panel生效
        items: [
        {
            name:'imeiNumber',
            xtype: 'textfield',
            fieldLabel:'设备IMEI号',
            labelWidth: 70,
            width:200
        },
        {
        	margin: '0 0 0 30', //上，右，下，左
            name:'vehicleNumber',
            xtype: 'textfield',
            fieldLabel:'车牌号',
            labelWidth: 50,
            width:200
        },
        {
        	margin: '0 0 0 30', //上，右，下，左
        	name:'deviceVendor',
            xtype: 'textfield',
            fieldLabel:'设备厂家',
            labelWidth: 60,
            width:200
        },
        {
        	margin: '0 0 0 30',
            name:'deviceStatus',
            xtype: 'combo',
            fieldLabel:'设备状态',
            labelWidth: 60,
            width:200,
            editable:false,
            editable:false,
			displayField:'value',
			valueField: 'id',
			value: '全部',
			store:{
			    fields: ['id', 'value'],
			    data : [
			        {"id":"0", "value":"全部"},
			        {"id":"1", "value":"正常"},
			        {"id":"2", "value":"未配置"},
			        {"id":"3", "value":"故障"},
			]}
        },
        {
        	margin: '0 0 0 30',
        	xtype:'button',
        	text: '<i class="fa fa-search"></i>&nbsp;查询',
        	handler: 'onSearchClick',
        }]
	},
	{
    	xtype:'container',
    	flex:1,
    	items:[
        {
            xtype: "button",
            style:'float:right',
            text: "<i class='fa fa-upload'></i>&nbsp;批量导入",
            handler: "openFileUpWindow",
        },
        {
            xtype:'field',
            style:'float:right',
            width:5,
        },{
            xtype:'button',
            text:'<i class="fa fa-plus"></i>&nbsp;新增',
            style:'float:right',
            handler:'onAddClick',
            margin: '0 20 0 0',
        },]
	}
    ],
    
    initComponent: function() {
        this.callParent();
    }
});
