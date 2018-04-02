Ext.define('Admin.view.orgmgmt.enterinfomgmt.GridPayHistory', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.orgmgmt.enterinfomgmt.EnterInfoModel'
    ],
    reference: 'gridPayHistory',
    viewModel: {
        xclass: 'Admin.view.orgmgmt.enterinfomgmt.EnterInfoModel'
    },
    title: '充值记录',
    id : 'gridPayHistory',
    bind: {
        store: '{payHistoryStore}'
    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },

    stateful: true,
    forceFit: false,
    mask: true,
    multiSelect: false,
    columnLines: true, // 加上表格线
    align: 'center',
    margin: '10 0 0 0',
    columns: [{
        header: '操作ID',
        dataIndex: 'id',
        align: 'center',
        menuDisabled: true,
        sortable: false,
        flex: 1
    }, {
        header: '操作名称',
        dataIndex: 'operationType',
        align: 'center',
        menuDisabled: true,
        sortable: false,
        flex: 1
    }, 
    {
        header: '金额',
        dataIndex: 'creditValue',
        align: 'center',
        menuDisabled: true,
        sortable: false,
        flex: 1
    }, 
    {
        header: '操作人',
        dataIndex: 'operatorName',
        align: 'center',
        menuDisabled: true,
        sortable: false,
        flex: 1
    }, 
    {
        header: '操作人角色',
        dataIndex: 'roleName',
        align: 'center',
        menuDisabled: true,
        sortable: false,
        flex: 1
    }, 
    {
        header: '操作时间',
        dataIndex: 'operateTime',
        align: 'center',
        menuDisabled: true,
        sortable: true,
        flex: 1.5,        
        renderer:function(v, cellValues, rec){
            if(v != null){
                var date = new Date();
                date.setTime(rec.get('operateTime'));
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
    }
    ],

    dockedItems : [{
        id:'payHistoryPage',
        pageSize:5,
        xtype: 'pagingtoolbar',
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
            store: '{payHistoryStore}'
        },
        displayInfo: true
    }],
    initComponent: function() {
        this.callParent();
    },
});
