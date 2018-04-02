Ext.define('Admin.view.orgmgmt.enterinfomgmt.ShowCreditLimitInfo', {
	extend: 'Ext.window.Window',
	
    alias: "widget.showCreditLimitInfo",
	reference: 'showCreditLimitInfo',

    controller: {
        xclass: 'Admin.view.orgmgmt.enterinfomgmt.ViewController'
    },

   viewModel : {
        type : 'enterInfoModel' 
    },

	title : '用车额度',
	width : 830,
	height : 600,
	id: 'showCreditLimitInfo',
	 closable:false,	//窗口是否可以改变
	resizable : false,	// 窗口大小是否可以改变
	draggable : true,	// 窗口是否可以拖动
	modal : true,		// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
    scrollable: true,
	listeners:{
    	afterrender: 'onAfterrenderShowCreditLimitInfo',
    },
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 10,
    items: [{
                id:'companyForCreditLimit',
                html: '<div class="companyForCreditLimit">中国移动上海分公司</div>',
                margin: '0 0 15 0'
            },
            {
                frame: true,

                xtype:'form',
                title: '帐号概况',
                layout: {
                    type: 'vbox', 
                    align: 'stretch'
                },

                items:[{
                        margin: '10 0 10 0',
                        xtype:'form',
                        layout: {
                            type: 'hbox', 
                            align: 'stretch'
                        },

                        items:[{
                            margin: '0 0 0 30',
                            xtype: 'displayfield',
                            fieldLabel: '可分配额度（元）',
                            labelWidth: 120,
                            id:'availableAccount',
                            name: 'availableAccount',
                        }, {
                            margin: '0 0 0 20',
                            xtype:'button',
                            text: '立即充值',
                            height: 30,
                            handler:'onRechargeClick'
                        },{
                            margin: '0 0 0 30',
                            xtype: 'displayfield',
                            fieldLabel: '总额度（元）',
                            labelWidth: 100,
                            id:'totalAccount',
                            name: 'totalAccount'
                        }] 
                }]  
            },
            {
               xclass: 'Admin.view.orgmgmt.enterinfomgmt.GridPayHistory',
               frame: true
            }],

    buttonAlign : 'center',
    buttons : [{
        text : '关闭',
        handler: function(){
            this.up('showCreditLimitInfo').close();
            /*刷新用车企业列表*/
            var grid = Ext.getCmp("enterId");
            if (grid){
                  Ext.getCmp('enterinfosearchForm').reset();
                  Ext.getCmp('enterIdPage').store.currentPage = 1;
                  Ext.getCmp('enterIdPage').pageSize = 10;
                  grid.getStore("usersResults").load();
            };
        },
    }],
    initComponent: function() {
        this.callParent();
    }
});