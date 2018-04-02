Ext.define('Admin.view.vehiclemgmt.specialpolice.SearchFormSecret', {
    extend: 'Ext.form.Panel',
    requires: ['Ext.layout.container.HBox', 
                'Ext.form.field.ComboBox'],
    
    reference: 'searchFormSecret',
    id:'searchFormSecret',
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
            name: 'vehicleNumber',
            xtype: 'textfield',
            labelWidth: 45,
            width:190,
            emptyText: '车牌号',
            fieldLabel: '车牌号'
        }, {
            xtype: 'combo',
            width:250,
            labelWidth: 65,
            valueField: 'value',
            name: 'vehicleType',
            fieldLabel: '公车性质',
            queryMode: 'local',
            editable: false,
            displayField: 'name',
            value: '-1',
            emptyText: '请选择公车性质',
            store: {
                fields: ['name', 'value'],
                data: [{
                    "name": "所有类型",
                    "value": "-1"
                }, {
                    "name": "应急机要通信接待用车",
                    "value": "0"
                }, {
                    "name": "行政执法用车",
                    "value": "1"
                },{
                    "name": "行政执法特种专业用车",
                    "value": "2"
                },{
                    "name": "一般执法执勤用车",
                    "value": "3"
                }, {
                    "name": "执法执勤特种专业用车",
                    "value": "4"
                }]
            }
        },{
            xtype:'textfield',
            name:'deptId',
            hidden:true,
            listeners:{
                afterrender: function(text){
                    text.setValue(window.sessionStorage.getItem("organizationId"));
                }
            },
        },{
            xtype: 'combo',
            width:205,
            labelWidth: 65,
            name: 'deptName',
            fieldLabel: '所属部门',
            itemId: 'deptId',
            editable: false,
            emptyText: '请选择...',
            listeners: {
                expand: 'openDeptChooseWin',
                afterrender: function(combo){
                    console.log("name: " + window.sessionStorage.getItem("organizationName"));
                    console.log("Id: " + window.sessionStorage.getItem("organizationId"));
                    combo.setValue(window.sessionStorage.getItem("organizationName"));
                }
            },
        }, {
            xtype:'checkboxgroup',
            hideLabel:true,
            columns: 2,
            width:170,
            flex:null,
            vertical: true,
            items: [
                { boxLabel: '本部门', name: 'selfDept', inputValue: 1,checked: true, 
                    width:70,
                    listeners:{
                        change:'checkIsGroupNull',
                    },
                    xtype:'checkbox',
                    style:{
                        marginRight:'10px',
                        marginLeft:'10px',
                    }},
                { boxLabel: '子部门', name: 'childDept', inputValue: 1,checked: true,
                    listeners:{
                        change:'checkIsGroupNull',
                    },
                    xtype:'checkbox',
                    style:{
                        marginRight:'10px',
                    }},
            ],
            allowBlank : false,
            blankText : '不能为空'
        },{
            margin: '0 0 0 10',
            xtype: 'button',
            text: '<i class="fa fa-search"></i>&nbsp;查询',
            listeners: {
                click: 'onSearchClickforSecret'
            }
        }]
    }],
    initComponent: function() {
        this.callParent();
    }
});
