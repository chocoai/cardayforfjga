Ext.define('Admin.view.systemconfiguration.policeCardConfiguration.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: ['Ext.layout.container.HBox', 
                'Ext.form.field.ComboBox'],
    
    reference: 'searchForm',
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
    header: false,
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    items: [{
        xtype: 'form',
        defaults:{
	        margin: '0 10 0 0'
        },
        layout: 'hbox',
        items: [{
            name: 'patternName',
            labelWidth:65,
            width:250,
            xtype: 'textfield',
            emptyText: '模式名称',
            fieldLabel: '模式名称'
        }, {
            xtype: 'combo',
            labelWidth:65,
            width:200,
            valueField: 'value',
            name: 'approvalRating',
            fieldLabel: '审批级数',
            queryMode: 'local',
            editable: false,
            displayField: 'name',
            value: '-1',
            emptyText: '请选择审批级数',
            store: {
                fields: ['name', 'value'],
                data: [{
                    "name": "所有类型",
                    "value": "-1"
                }, {
                    "name": "一级",
                    "value": "0"
                }, {
                    "name": "二级",
                    "value": "1"
                }, {
                    "name": "三级",
                    "value": "2"
                }, {
                    "name": "四级",
                    "value": "3"
                }, {
                    "name": "五级",
                    "value": "3"
                }]
            }
        }, {
            margin: '0 0 0 10',
            xtype: 'button',
            text: '<i class="fa fa-search"></i>&nbsp;查询',
            listeners: {
                click: 'onSearchClick'
            }
        }]
    },
    {
        xtype:'container',
        flex:1,
        items:[{
            xtype:'button',
            text: "新增警牌审批设置",
            style:'float:right',
            handler:'addPoliceCard'
        }]
    }],
    initComponent: function() {
        this.callParent();
    }
});
