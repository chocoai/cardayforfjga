Ext.define('Admin.view.vehiclemgmt.vehiclemaintain.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: ['Ext.layout.container.HBox', 
                'Ext.form.field.ComboBox'],
    
    id: 'vehiclemaintainSearchformId',
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
            name: 'vehicleNumber',
            labelWidth:65,
            width:200,
            xtype: 'textfield',
            emptyText: '车牌号',
            fieldLabel: '车牌号'
        }, {
                xtype:'textfield',
                name:'deptId',
                hidden:true,
                value: window.sessionStorage.getItem("organizationId"),               
                listeners:{
                    afterrender: function(text){
                        text.setValue(window.sessionStorage.getItem("organizationId"));
                    }
                },
            },{
                xtype : 'combo',
                name:'deptName',
                labelWidth:65,
                fieldLabel : '所属部门',
                itemId : 'deptId',
                editable : false,
                emptyText : '请选择...',
                value: window.sessionStorage.getItem("organizationName"),
                listeners:{
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
                width:180,
                flex:null,
                vertical: true,
                items: [
                    { boxLabel: '本部门', name: 'includeSelf', inputValue: true,checked: true, 
                        width:70,
                        listeners:{
                            change:'checkIsGroupNull',
                        },
                        style:{
                            marginRight:'10px',
                            marginLeft:'10px',
                        }},
                    { boxLabel: '子部门', name: 'includeChild', inputValue: true,checked: true,
                        listeners:{
                            change:'checkIsGroupNull',
                        },
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
                click: 'onSearchClick'
            }
        }]
    },
    {
        xtype:'container',
        flex:1,
        items:[{
            xtype:'button',
            text: "新增车辆维修信息",
            style:'float:right',
            handler:'addVehicleMaintain'
        }]
    }],
    initComponent: function() {
        this.callParent();
    }
});
