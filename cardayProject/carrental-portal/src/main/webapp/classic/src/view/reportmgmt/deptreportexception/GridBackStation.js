Ext.define('Admin.view.reportmgmt.deptreportexception.GridBackStation', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
    ],
    
    reference: 'gridBackStation',
    title: '回车报警列表',
    width: '100%',
    columnLines: true,
    //height: 290,
    
    bind: {
        store: '{backStationList}'
    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },

    scrollable: false,
    stateful: true,
    forceFit: false,
    align: 'left',
    cls : 'user-grid',  //图片用户样式
    margin: '0 0 0 0',
    frame: true,
    columns: [
    {
        header: '序号',
        sortable: true,
        menuDisabled: true,
        dataIndex: 'id',
        flex: 1,
        align: 'center',        
    },
    {
        header: '车牌号',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'vehicleNumber',
        flex: 1,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '报警时间',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'alertTime',
        flex: 1,
        align: 'center',
        renderer:function(v, cellValues, rec){
            if(v != null){
                var date = new Date();
                date.setTime(rec.get('alertTime'));
                var month = date.getMonth(date);
                month = month + 1;
                var day = date.getDate(date);
                var hour = date.getHours(date);
                var minutes = date.getMinutes(date);
                var seconds = date.getSeconds(date);
                if(month < 10){
                    month =  '0' + month;
                }
                if(day < 10){
                    day =  '0' + day;
                }
                if(hour < 10){
                    hour =  '0' + hour;
                }
                if(minutes < 10){
                    minutes =  '0' + minutes;
                }
                if(seconds < 10){
                    seconds =  '0' + seconds;
                }
                return date.getFullYear() + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + seconds;
            }else{
                return '';
            }
        }
        
    },
    {
        header: '报警类型',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'alertType',
        flex: 1,
        align: 'center',
        renderer:function(v, cellValues, rec){
            if(rec.get('alertType') == 'OVERSPEED'){
                return '超速报警';
            }else if(rec.get('alertType') == 'OUTBOUND'){
                return '越界报警';
            }else{
                return '回车报警';
            }
        }
    },
    {
        header: '城市',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'alertCity',
        flex: 1,
        align: 'center',
    },
    {
        header: '报警位置',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'alertPosition',
        flex: 1,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    }, 
    {
        header: '站点',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'stationNames',
        flex: 1,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '部门',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'orgName',
        flex: 1,
        align: 'center',
    },
    {
        header: '司机姓名',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'driverName',
        flex: 0.5,
        align: 'center',
    },
    {
        header: '司机电话',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'driverMobile',
        flex: 0.9,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    }, 
/*        {
            xtype: 'actioncolumn',
            items: [
                {
                    xtype: 'button',
                    tooltip:'查看',
                    iconCls: 'x-fa fa-eye',
                    handler: 'viewBackStationList'
                },
            ],

            cls: 'content-column',
            align: 'center',
            width: 160,
            text: '操作',
            tooltip: 'edit'
        }*/
    ],
    dockedItems: [
	  {
	      xtype: 'pagingtoolbar',
	      id: 'backStationPageId',
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
	      bind: {
	      	store: '{backStationList}'
	      },
	      displayInfo: true
	  }
   ],
   
   initComponent: function() {
        this.callParent();
    },
});