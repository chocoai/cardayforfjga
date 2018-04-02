Ext.define('Admin.view.alertMgmt.backStationAlarm.BackStationMap', {
    extend: 'Ext.panel.Panel',
    requires: [
        'Admin.ux.MapPanel'
    ],
    reference: 'backStationMap',
    maxWidth: 1030,
    maxHeight: 480,
  //  frame:true,
    plain: true,
    resizable: false,
    initComponent: function() {
        var me = this;
        me.items = [{
            xtype: 'mappanel',
            id: 'abnormal_backStationAlarm',
            width: 1030,
            height:400,
            plain: true,
            border: false,
            zoomLevel: 12,
            gmapType: 'map',
            overlays: [],
            markers: [],
            ContextMenus: ['在此添加标注', '放大', '缩小', '放置到最大级', '查看全国'],
            mapControls: ['NavigationControl', 'ScaleControl', 'OverviewMapControl', 'MapTypeControl']
        }];
        me.callParent();
    }
});
