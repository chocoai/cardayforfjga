/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.reportmgmt.reportdriving.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],
    reference: 'searchForm',
    bodyPadding: 1,
    header: false,
    fieldDefaults: {
        // labelAlign: 'right',
        labelWidth: 65,
        margin: '0 20 0 0', //label和上一个输入框的样式，间隔5
        msgTarget: Ext.supports.Touch ? 'side' : 'qtip'
    },
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    margin: '0 0 20 0', //对panel生效
    
    items: [
	{
		xtype: 'form',
        layout: 'hbox',
        flex:1,
        items: [
        {
            fieldLabel: '车牌号',
            emptyText:'请输入车牌号',
            allowBlank: true,
            id: 'add_device_vehicleNumber_id',
			xtype: 'combo',
			name: 'vehNum',
			queryMode: 'remote',
			valueField : 'vehicleNumber',
			editable:true,
			displayField:'vehicleNumber',
			hideTrigger: true,
			minChars: 1,
			typeAhead: true,
            matchFieldWidth: true,
            listConfig : {
                maxHeight : 165,
            },
	        store:new Ext.data.Store({
	        	proxy: {
                    type: 'ajax',
                    url: 'vehicle/listVehicleAutoComplete',
                    actionMethods : 'get', 
                    reader: { 
                        type: 'json', 
                        totalProperty: 'totalRows', 
                        rootProperty: 'data' 
                    } 
                },
                listeners : {
                	'beforeload' : function(store, operation, eOpts) {
		        		var vehicleNumberCombo = Ext.getCmp('add_device_vehicleNumber_id').getValue();
		        		var input = {'vehicleNumber' : vehicleNumberCombo};
		        		var param = {'json': Ext.encode(input)};
		        		Ext.apply(store.proxy.extraParams, param);
		        	},
		        	'load' : function(store, records, options) {
		        		if(records.length==0){
		        			store.insert(0, {
								"vehicleNumber" : "没有匹配的车辆",
								"id" : "-1"
		        			})
		        		}
					}
                },
                autoLoad : false, 

	        }),
        },
        {
        	name:'startDate',
            fieldLabel: '开始日期',
            xtype: 'datefield',
            emptyText: '请选择开始日期',
            format: 'Y-m-d',
            vtype: 'daterange',
            itemId: 'startdt',
            endDateField: 'enddt',
            editable: false,
            value: new Date(new Date().getTime()-24*60*60*1000),
            maxValue : new Date(),
        },
        {
        	name:'endDate',
            fieldLabel: '结束日期',
            emptyText: '请选择结束日期',
            xtype: 'datefield',
            format: 'Y-m-d',
            vtype: 'daterange',
            itemId: 'enddt',
            //startDateField: 'startdt',
            editable: false,
            value: new Date(new Date().getTime()-24*60*60*1000),
            maxValue : new Date(),
        },
        {
            margin: '0 0 0 20',
            xtype:'button',
            text:'<i class="fa fa-search"></i>&nbsp;查询',
            width:100,
            //style:'float:right',
            handler:'onSearchClick',
            tooltip: '建议：未避免长时间等待，搜索的历史记录时间间隔请不要大于3天。'
        }
        ]
	},
	{
    	xtype:'container',
    	//flex:1,
    	items:[
        {
            xtype:'button',
            text:'<i class="fa fa-download"></i>&nbsp;导出Excel',
            width:120,
            style:'float:right',
            handler:'onExcelClick',
            tooltip: '建议：未避免长时间等待，搜索的历史记录时间间隔请不要大于3天。'
        }]
	}
    ],

    initComponent: function() {
        this.callParent();
    }
});
