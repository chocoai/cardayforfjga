Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.ShowVehicleRemoveErrorWindow', {
    extend: 'Ext.window.Window',
    
    alias: "widget.showVehicleRemoveErrorWindow",
    reference: 'showVehicleRemoveErrorWindow',
    id: 'showVehicleRemoveErrorWindow',
    title : '车辆删除失败',
    width : 830,
    height : 600,

    closable:true,//窗口是否可以改变
    resizable : false,// 窗口大小是否可以改变
    draggable : true,// 窗口是否可以拖动
    scrollable: 'y',
    modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作

    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },

    bodyPadding: 20,

    items : [{
            xclass : 'Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.ConfirmVehicleRemoveError',
        }],

    buttonAlign : 'center',
    buttons : [{
                text: '关闭',
                handler: function(btn) {
                    btn.up('showVehicleRemoveErrorWindow').close();
                }
            } ]
});