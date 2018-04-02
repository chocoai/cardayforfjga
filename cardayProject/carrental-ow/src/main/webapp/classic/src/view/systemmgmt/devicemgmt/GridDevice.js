Ext.define('Admin.view.systemmgmt.devicemgmt.GridDevice', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.systemmgmt.devicemgmt.DeviceModel'
    ],
    
    id: 'griddeviceid',
    reference: 'griduser',
    title: '设备列表',
    //cls : 'user-grid',  //图片用户样式
    viewConfig: {
        loadMask: true,
    },
    listeners: {
    },

    stateful: true,
    collapsible: false,
    forceFit: false,
    mask: true,
    columnLines: true, // 加上表格线
    align: 'center',
    selModel: {
        injectCheckbox: 0,
        mode: "SINGLE",     //"SINGLE"/"SIMPLE"/"MULTI"
        checkOnly: true     //只能通过checkbox选择
    },

	bind : {
		store : '{devicesResults}'
	},
    columns: [
       {
	      	header: '编号',
	      	dataIndex: 'id',
	      	flex: 3,
	      	name: 'id',
	      	hidden: true,
            align: 'center',
            menuDisabled: true,
            sortable: true,
            renderer:function (value, metaData){  
                value=value==null?"":value;
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	},
        {
        	header: 'SN设备号',
        	dataIndex: 'snNumber',
        	flex: 4,
        	name: 'snNumber',
            align: 'center',
            menuDisabled: true,
            sortable: true,
            renderer:function (value, metaData){  
                value=value==null?"":value;
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
      	},
      	{
        	header: '设备IMEI号',
        	dataIndex: 'imeiNumber',
        	flex: 4,
        	name: 'imeiNumber',
            align: 'center',
            menuDisabled: true,
            sortable: false,
            renderer:function (value, metaData){
                value=value==null?"":value;
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
      	},
      	{
        	header: '设备类型',
    		dataIndex: 'deviceType',
    		flex: 3,
            name: 'deviceType',
            align: 'center',
            menuDisabled: true,
            sortable: false,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	},
    	{
        	header: '设备型号',
    		dataIndex: 'deviceModel',
    		flex: 3,
            name: 'deviceModel',
            align: 'center',
            menuDisabled: true,
            sortable: false,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	},
        {
            header: '设备厂家',
            dataIndex: 'deviceVendor',
            flex: 3,
            name: 'deviceVendor',
            align: 'center',
            menuDisabled: true,
            sortable: false,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '采购时间',
            dataIndex: 'purchaseTime',
            flex: 3,
            name: 'purchaseTime',
            hidden: true,
            menuDisabled: true,
            sortable: false,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '保修到期日',
            dataIndex: 'maintainExpireTime',
            flex: 3,
            name: 'maintainExpireTime',
            hidden: true,
            menuDisabled: true,
            sortable: false,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
    	{
    		header: 'SIM卡号',
    		dataIndex: 'simNumber',
    		flex: 3,
            name: 'simNumber',
            align: 'center',
            menuDisabled: true,
            sortable: false,
            renderer:function (value, metaData){  
                value=value==null?"":value;
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	},
    	{
    		header: '车辆Id',
    		dataIndex: 'vehicleId',
    		flex: 3,
            name: 'vehicleId',
            hidden: true,
            align: 'center',
            menuDisabled: true,
            sortable: false,
            renderer:function (value, metaData){  
                value=value==null?"":value;
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	},
    	{
    		header: '绑定车牌号',
    		dataIndex: 'vehicleNumber',
    		flex: 3,
            name: 'vehicleNumber',
            align: 'center',
            menuDisabled: true,
            sortable: false,
            renderer:function (value, metaData){  
                value=value==null?"":value;
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	},
    	{
    		header: '车辆来源',
    		dataIndex: 'vehicleSource',
    		flex: 3,
            name: 'vehicleSource',
            hidden: true,
            align: 'center',
            menuDisabled: true,
            sortable: false,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	},
    	{
    		header: '车辆Vin号',
    		dataIndex: 'vehicleIdentification',
    		flex: 3,
            name: 'vehicleIdentification',
            hidden: true,
            align: 'center',
            menuDisabled: true,
            sortable: false,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	},
    	{
    		header: '设备状态',
    		dataIndex: 'deviceStatus',
    		flex: 3,
            name: 'deviceStatus',
            align: 'center',
            menuDisabled: true,
            sortable: false,
            renderer : function(value) {
            	switch (value) {
            	case 1:
            		value='正常';
            		break;
            	case 2:
            		value='未配置';
            		break;
            	case 3:
            		value='故障';
            		break;
            	}
            	return value;
			}
    	},
    	{
    		header: 'License编号',
    		dataIndex: 'licenseNumber',
    		flex: 4,
            name: 'licenseNumber',
            align: 'center',
            menuDisabled: true,
            sortable: false,
            renderer:function (value, metaData){  
                value=value==null?"":value;
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	},
        {
            header: '服务开始时间',
            dataIndex: 'startTime',
            flex: 4,
            name: 'startTime',
            align: 'center',
            menuDisabled: true,
            sortable: false,
            renderer:function (value, metaData){  
                value=value==null?"":value;
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        }, 
        {
            header: '服务结束时间',
            dataIndex: 'endTime',
            flex: 4,
            name: 'endTime',
            align: 'center',
            menuDisabled: true,
            sortable: false,
            renderer:function (value, metaData){  
                value=value==null?"":value;
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: 'License状态',
            dataIndex: 'licenseStatus',
            flex: 3.5,
            name: 'licenseStatus',
            align: 'center',
            menuDisabled: true,
            sortable: false,
            renderer : function(value,metaData) {
                value=value==null?"":value;
            	switch (value) {
            	case 'NOT_IN_USE':
            		value='未绑定';
            		break;
            	case 'BINDING_UNACTIVE':
            		value='未激活';
            		break;
            	case 'IN_USE':
            		value='已激活';
            		break;
            	case 'SUSPEND':
            		value='已挂起';
            		break;
            	case 'TERMINATED':
            		value='已停止';
            		break;
            	}
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
            	return value;
			}
        },
    	{
            xtype: 'actioncolumn',
            //align: 'left',
            menuDisabled: true,
            sortable: false,
            align: 'center',
            items: [
                {
                    xtype: 'button',
                    tooltip:'查看',
                    iconCls: 'x-fa fa-eye',
                    handler: 'viewDevice'
                },
                {
                    xtype: 'button',
                    tooltip:'修改',
                    iconCls: 'x-fa fa-pencil',
                    handler: 'editDevice',
                },
/*                {
                    getClass: function(v, meta, record) {
                        var marginValue = this.width/2-40;                        
                                this.setMargin('0 -'+marginValue+' 0 '+marginValue);
                    }
                }*/
            ],

            cls: 'content-column',
            width: 90,
  //        dataIndex: 'bool',
            header: '设备操作',
            //tooltip: 'edit'
        },
        {
            xtype: 'actioncolumn',
            align: 'center',
            //align: 'left',
            menuDisabled: true,
            sortable: false,
            items: [
                {
                    xtype: 'button',
                    tooltip:'绑定',
                    iconCls: 'x-fa fa-check',
                    handler: 'licenseBind'
                },
                {
                    xtype: 'button',
                    tooltip:'激活',
                    iconCls: 'x-fa fa-pencil',
                    handler: 'licenseActive'
                },
                {
                    xtype: 'button',
                    tooltip:'挂起',
                    iconCls: 'x-fa fa-stop',
                    handler: 'licenseSuspend'
                },
                {
                    xtype: 'button',
                    tooltip:'继续',
                    iconCls: 'x-fa fa-play',
                    handler: 'licenseReactive'
                },
                {
                    xtype: 'button',
                    tooltip:'停止',
                    iconCls: 'x-fa fa-pause',
                    handler: 'licenseTerminated'
                },
                {
                    xtype: 'button',
                    tooltip:'解绑',
                    iconCls: 'x-fa fa-close',
                    handler: 'licenseUnbind'
                },/*{
                    getClass: function(v, meta, record) {
                        var marginValue = this.width/2-55;                        
                        this.setMargin('0 -'+marginValue+' 0 '+marginValue);
                    }
                }*/
            ],

            cls: 'content-column',
            width: 200,
  //        dataIndex: 'bool',
            header: 'License操作',
            //tooltip: 'edit'
        }
    ],
    dockedItems: [
                  {
                	  id: 'device_list_id',
                      xtype: 'pagingtoolbar',
                      pageSize: 10,
                      dock: 'bottom',
                      displayMsg: '第 {0} - {1} 条记录，共 {2} 条记录',
                      emptyMsg : '无数据！',
                      beforePageText: "第",
                      afterPageText: "页，共{0}页",
                      nextText: "下一页",
                      prevText: "上一页",
                      refreshText: "刷新",
                      firstText: "第一页",
                      lastText: "最后一页",
                      displayInfo: true
                  }
             ],
    
    initComponent: function() {
        this.callParent();

    },
});
