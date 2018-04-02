Ext.define('Admin.view.carnumbermgmt.carnumberapprove.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: ['Ext.layout.container.HBox', 
                'Ext.form.field.ComboBox'],
    
    //id: 'vehicleAuthorizedSearchformId',
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
            name: 'deptName',
            labelWidth:65,
            width:250,
            xtype: 'textfield',
            emptyText: '单位名称',
            fieldLabel: '单位名称'
        }, {
            xtype: 'combo',
            labelWidth:65,
            width:200,
            valueField: 'value',
            name: 'levelType',
            fieldLabel: '单位级别',
            queryMode: 'local',
            editable: false,
            displayField: 'name',
            value: '-1',
            emptyText: '请选择单位级别',
            store: {
                fields: ['name', 'value'],
                data: [{
                    "name": "所有类型",
                    "value": "-1"
                }, {
                    "name": "正厅级",
                    "value": "0"
                }, {
                    "name": "副厅级",
                    "value": "1"
                }, {
                    "name": "处级",
                    "value": "2"
                }, {
                    "name": "副处级",
                    "value": "3"
                }, {
                    "name" : "科级",
                    "value" : "6"
                }, {
                    "name" : "副科级",
                    "value" : "7"
                },{
                    "name": "县级",
                    "value": "4"
                }, {
                    "name": "乡科级",
                    "value": "5"
                }]
            }
        }, {
            margin: '0 0 0 10',
            xtype: 'button',
            text: '<i class="fa fa-search"></i>&nbsp;查询',
/*            id: 'searchVehicle',
            listeners: {
                click: 'onSearchClick'
            }*/
        }]
    },
    {
        xtype:'container',
        flex:1,
        items:[{
            xtype:'button',
            text: "编制录入",
            style:'float:right',
            handler:'addAuthorizedVeh'
        }]
    }],
    initComponent: function() {
        this.callParent();
    }
});
