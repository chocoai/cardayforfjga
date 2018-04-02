Ext.define('Admin.view.systemmgmt.systemmessage.GridMessage', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.systemmgmt.systemmessage.ViewModel',
    ],
    
    id: 'gridMessage',
    reference: 'gridMessage',
    title: '公告消息管理',
    //cls : 'user-grid',  //图片用户样式
    
    viewConfig: {
        loadMask: true,
    },

    stateful: true,
    collapsible: false,
    forceFit: false,
    mask: true,
    columnLines: true, // 加上表格线
    align: 'center',

    bind : {
		store : '{messageResults}'
	},
    columns: [{
    		xtype : 'gridcolumn',
            header: 'ID',
            dataIndex: 'id',
            flex: 1,
            name: 'id',
            align: 'center',
            sortable: false,
            menuDisabled: true,
        },
        {
            xtype : 'gridcolumn',
            header: '公告时间',
            dataIndex: 'time',
            flex: 2,
            name: 'time',
            align: 'center',
            sortable: false,
            menuDisabled: true,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            xtype : 'gridcolumn',
            header: '公告类型',
            dataIndex: 'type',
            flex: 2,
            name: 'type',
            align: 'center',
            sortable: false,
            menuDisabled: true,
            renderer : function(v, metadata, record, rowIndex, columnIndex, store){  
                switch(v)
                {
                case 'MAINTAIN':
                  return "车辆维保";
                  break;
                case 'OUTBOUND':
                  return "越界报警";
                  break;
                case 'OVERSPEED':
                  return "超速报警";
                  break;
                case 'VEHICLEBACK':
                  return "回车报警";
                  break;
                case 'SYSTEM':
                  return "系统消息";
                  break;
                case 'TASK':
                  return "任务消息";
                  break;
                case 'TRAVEL':
                  return "行程消息";
                  break;
                default:
                  return "其他";
                }
            }
        },
        {
            xtype : 'gridcolumn',
            header: '公告标题',
            dataIndex: 'title',
            flex: 1,
            name: 'title',
            align: 'center',
            sortable: false,
            menuDisabled: true,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            xtype : 'gridcolumn',
            header: '公告内容',
            dataIndex: 'msg',
            flex: 8,
            name: 'msg',
            align: 'center',
            sortable: false,
            menuDisabled: true,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            xtype: 'actioncolumn',
            items: [
                {
                    xtype: 'button',
                    tooltip:'查看',
                    iconCls: 'x-fa fa-eye',
                    handler: 'viewMessage'
                },/*{
                  getClass: function(v, meta, record) {
                    var marginValue = this.width/2-20;                        
                    this.setMargin('0 -'+marginValue+' 0 '+marginValue);
                  }
                }*/
            ],

            cls: 'content-column',
            width: 60,
            text: '操作',
//            align: 'left',
            align: 'center',
        }
    ],
    
    dockedItems: [
                  {
                      xtype: 'pagingtoolbar',
                      id:'messageId',
                      pageSize: 20,
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
                      bind: {
                      	store: '{messageResults}'
                      },
                      displayInfo: true
                  }
             ],
    
    initComponent: function() {
        this.callParent();

    },
});
