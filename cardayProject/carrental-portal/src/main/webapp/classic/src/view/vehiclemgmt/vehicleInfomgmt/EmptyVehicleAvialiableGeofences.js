Ext.define('Admin.view.vehiclemgmt.vehicleInfomgmt.EmptyVehicleAvialiableGeofences', {
    extend: 'Ext.panel.Panel',
    requires: [
      'Admin.view.vehiclemgmt.vehicleInfomgmt.ViewController',
    ],
    reference: 'emptyVehicleAvialiableGeofences',

    title: '可分配地理围栏列表',
    width : 700,
    height: 400,
    items: [{
             html:'<div style="font-weight: bold;text-align: center;padding: 100px;font-size: 16px;"><p>暂无可分配的地理围栏</p><p>请到<button type="button" class="emptyvehbutton" onclick="onEmptyforGeofenceMsg()"><span>地理围栏管理</span></button>新增地理围栏</p></div>',
        },],
    initComponent: function() {
        this.callParent();
    },
});
