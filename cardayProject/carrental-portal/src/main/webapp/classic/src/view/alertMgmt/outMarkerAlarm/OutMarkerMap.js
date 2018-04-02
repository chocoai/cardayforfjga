Ext.define('Admin.view.alertMgmt.outMarkerAlarm.OutMarkerMap', {
    extend: 'Ext.panel.Panel',
    requires: [
        'Admin.ux.MapPanel'
    ],
    reference: 'outMarkerMap',
    maxWidth: 1030,
    maxHeight: 480,
    plain: true,
    resizable: false,
//    frame:true,
    initComponent: function() {
        var me = this;
        me.items = [{
            xtype: 'mappanel',
            reference: 'abnormal_outMarkerAlarm',
            id: 'abnormal_outMarkerAlarm',
            width: 1030,
            height:350,
            plain: true,
            border: false,
            zoomLevel: 12,
            gmapType: 'map',
            overlays: [],
            markers: [],
            ContextMenus: ['在此添加标注', '放大', '缩小', '放置到最大级', '查看全国'],
            mapControls: ['NavigationControl', 'ScaleControl', 'OverviewMapControl', 'MapTypeControl'],
            listeners: {
          //  	afterrender: 'initTripTraceMap'
            }
        }];
        me.callParent();
    }
});
