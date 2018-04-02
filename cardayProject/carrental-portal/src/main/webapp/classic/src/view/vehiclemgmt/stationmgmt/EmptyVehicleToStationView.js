Ext.define('Admin.view.vehiclemgmt.stationmgmt.EmptyVehicleToStationView', {
    extend: 'Ext.panel.Panel',
    requires: [
      'Admin.view.vehiclemgmt.stationmgmt.ViewController',
    ],
    reference: 'emptyVehicleToStationView',

    title: '可分配车辆列表',
    width : 700,
    height: 400,
    items: [{
             html:'<div style="font-weight: bold;text-align: center;padding: 100px;font-size: 16px;"><p>暂无可分配的车辆</p><p>请到<button type="button" class="emptyvehbutton" onclick="onEmptyforVehMag()"><span>车辆信息管理</span></button>新增车辆</p></div>',
        },/*{
            xtype: 'button',
            text: '车辆信息管理',
            scale: 'medium'
        },{
             html:'<span>heheheheheh</span>',
        }*/],
    initComponent: function() {
        this.callParent();
    },
});
