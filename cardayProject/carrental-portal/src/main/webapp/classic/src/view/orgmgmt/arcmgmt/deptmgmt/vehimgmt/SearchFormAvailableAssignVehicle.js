Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.SearchFormAvailableAssignVehicle', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],

    id:'searchFormAvailableAssignVehicle',

    reference: 'searchFormAvailableAssignVehicle',

    header : false,
    fieldDefaults : {
        labelWidth : 60,
    },
    layout: {
        type: 'vbox',
        align: 'stretch',
    },

    items: [
    {
        xtype: 'form',
        defaults:{
            margin: '0 5 0 0'
        },
        fieldDefaults: {
            labelWidth: 60
        },
        layout: 'hbox',
        items : [{
                name : 'vehicleNumber',
                margin : '0 10 10 10',
                xtype : 'textfield',
                width: 180,
                emptyText : '车牌号',
                fieldLabel : '车牌号'
            }, {
                xtype : 'combo',
                margin : '0 10 10 10',
                width: 200,
                valueField : 'vehicleModel',
                name : 'vehicleModel',
                fieldLabel : '车辆型号',
                queryMode : 'local',
                //typeAhead:false,
                editable : false,
                displayField : 'vehicleModel',
                //emptyText : '请选择...',
                value:'全部',
                id:'vehicleModelUnAssignedId',

                store: new Ext.data.Store({
                    proxy: {
                        type: 'ajax',
                        url: 'vehicle/organization/listvehicleModel',
                        actionMethods : 'GET', 
                        reader: { 
                            type: 'json', 
                            totalProperty: 'totalCount', 
                            rootProperty: 'data' 
                        } 
                    },
                    autoLoad : false, 
                    listeners:{    
                        load : function(store, records, options ){  
                            store.insert(0,{"id": "-1","vehicleModel":"全部"}); //只添加一行用这个比较方便
                            if(Ext.getCmp("vehicleModelUnAssignedId") != null){
                                Ext.getCmp("vehicleModelUnAssignedId").setValue(0);
                            }
                        },
                        beforeload: function( store, operation, eOpts ){
                            var selection = Ext.getCmp("deptMgmtTreeList").getSelectionModel().getSelection();
                            if(selection.length == 0){
                                return;
                            }
                            var select = selection[0].getData();
                            var deptId = select.id;
                            var input = {
                                "deptId":deptId,
                                "tFlag":Ext.getCmp('vehicleMgmtTabId').getActiveTab().tFlag,
                            };

                            var json = Ext.encode(input);

                            store.proxy.extraParams = {
                                "json":json
                            }
                        },
                    }
                }),

                listeners:{
                    expand: function( combo, event, eOpts){
                        combo.getStore().load();
                    }
                },
            },{
                xtype: 'combo',
                valueField: 'id',
                itemId: 'fromOrgId',
                name: 'fromOrgId',
                fieldLabel: '车辆来源',
                queryMode: 'local',
                editable: false,
                displayField: 'name',
                emptyText: '请选择车辆来源',
                value: '-1',
                store: Ext.create('Ext.data.Store', {
                    proxy: {
                        type: 'ajax',
                        url: 'vehicle/listVehicleFrom',
                        reader: {
                            type: 'json',
                            rootProperty: 'data',
                            successProperty: 'status'
                        }
                    },
                    autoLoad: true,
                    listeners: {
                        load: function(store, records, options) {
                            var userType = window.sessionStorage.getItem('userType');
                            if (userType == '6') {
                                var organizationId = window.sessionStorage.getItem("organizationId");
                                for (var i in records) {
                                    if (records[i].getData().id != organizationId) {
                                        store.remove(records[i]);
                                    }
                                }

                            }
                            store.insert(0, {
                                "name": "所有来源",
                                "id": "-1"
                            })
                        }
                    }
                })
            }, /*{
                xtype : 'combo',
                margin : '0 10 10 10',
                width: 180,
                valueField : 'value',
                name : 'vehiclePurpose',
                fieldLabel : '车辆用途',
                queryMode : 'local',
                editable : false,
                displayField : 'name',
                emptyText : '请选择车辆用途',
                value:'-1',

                store : {
                    fields : ['name', 'value'],
                    data : [
                    {
                        "name" : "全部",
                        "value" : "-1"
                    },{
                        "name" : "生产用车",
                        "value" : "0"
                    }, {
                        "name" : "营销用车",
                        "value" : "1"
                    }, {
                        "name" : "接待用车",
                        "value" : "2"
                    }, {
                        "name" : "会议用车",
                        "value" : "3"
                    }]
                }
            },*/{
                xtype:'button',
                margin : '0 10 10 10',
                text:'查询',
                handler:'onSearchClickforAvailableAssignVehicle',
              },
              {
                xtype:'button',
                margin : '0 10 10 10',
                text:'重置',
                handler:'onResetClickforAvailableAssignVehicle',
              }
            ],
            },
            {
                xtype: 'container',
                layout: 'hbox',
                margin:'0 0 10 0',
                items: [{
                    xtype: "button",
                    text: "确认分配车辆",
                    handler: "batchAssignedVehicle",
                }]
            },],
    initComponent : function() {
        this.callParent();
    }
});
