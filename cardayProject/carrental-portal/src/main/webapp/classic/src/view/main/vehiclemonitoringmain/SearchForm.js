Ext.define('Admin.view.main.vehiclemonitoringmain.SearchForm', {
    extend: 'Ext.form.Panel',
    requires: ['Ext.layout.container.VBox'],
    
    reference: 'searchForm',
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
    header: false,
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    bodyPadding:5,
    items: [{
        margin: '0 0 0 0',
        xtype: 'button',
        text: '<i class="fa fa-car"></i>&nbsp;车辆信息',
        width: 100,
        enableToggle: true,
        id:'mainVehMoniViewVehInfoBtn',
        listeners: {
//            click: 'getVehInfo',
        	toggle: 'getVehInfo',
        }
    },{
        margin: '0 0 0 5',
        xtype: 'button',
        text: '<i class="fa fa-dot-circle-o"></i>&nbsp;实时定位',
        width: 100,
        listeners: {
            click: 'locateVeh',
        }
    },{
        margin: '0 0 0 5',
        xtype: 'button',
        text: '<i class="fa fa-map-marker"></i>&nbsp;今日轨迹',
        width: 100,
        listeners: {
            click: 'openTodayWindow'
        }
    },{
        margin: '0 0 0 5',
        xtype: 'button',
        text: '<i class="fa fa-map-marker"></i>&nbsp;历史轨迹',
        width: 100,
        listeners: {
            click: 'openHistoryWindow'
        }
    },{
        margin: '0 0 0 5',
        xtype: 'button',
        text: '<i class="fa fa-line-chart"></i>&nbsp;车辆数据统计',
//        width: 100,
        enableToggle: true,
        id:'mainVehMoniViewVehCountBtn',
        listeners: {
//          click: 'getVehicleCountInfo'
        	toggle: 'getVehicleCountInfo'
        }
    },{
        margin: '0 0 0 5',
        xtype: 'button',
        text: '<i class="fa fa-bar-chart"></i>&nbsp;报警事件统计',
//        width: 100,
        enableToggle: true,
        id:'mainVehMoniViewVehAlarmBtn',
        listeners: {
//            click: 'getAlarmStatistics'
        	toggle: 'getAlarmStatistics'
            // click: 'messagePanelRender'
        }
    },{
        margin: '0 0 0 20',
        xtype: 'panel',
        id: 'messagePanel',
        reference: 'messagePanel',
        viewModel: {
            data: {
                systemMessageOne: '消息数据',
                systemMessageTwo: '',
                systemMessageThree: '',
                systemMessageOneTime: '',
                systemMessageTwoTime: '',
                systemMessageThreeTime: '',
                systemMessageOneContent: '',
                systemMessageTwoContent: '',
                systemMessageThreeContent: '',
            }
        },
        bind: {
            html: '<span id="mainMessage" style="overflow: hidden;height:35px; line-height:35px;">' +
            '<div id="mainMessageContent" style="height:35px; line-height:35px; width:10000px;">' +
                '<div id="message1" style="float:left; margin-right:20px; ">{systemMessageOneContent}{systemMessageOneTime}</div>' +
                '<div id="message2" style="float:left; margin-right:20px; ">{systemMessageTwoContent}{systemMessageTwoTime}</div>' +
                '<div id="message3" style="float:left; margin-right:20px; ">{systemMessageThreeContent}{systemMessageThreeTime}</div>' +
            '</div>' +
            '</span>'
        },
        flex:1
    }],
    initComponent: function() {
        this.callParent();
    }
});
