Ext.define('Admin.view.vehiclemgmt.authorizedmgmt.ViewAuthorizedVehApply', {
	extend: 'Ext.window.Window',
	
    alias: "widget.viewAuthorizedVehApply",
    controller: {
        xclass: 'Admin.view.vehiclemgmt.authorizedmgmt.ViewController'
    },
    id:'viewAuthorizedVehApply',
	reference: 'viewAuthorizedVehApply',
	title : '查看编制调整申请',
	width : 720,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	frame : true,
	items : [{
		xtype:'form',
		layout: {
	        type: 'table',
	        columns: 3
    	},
		defaultType:'displayfield',
		fieldDefaults: {
		     margin: '0 10 0 30'
			
		},  
		items: [
			{
				fieldLabel: '单位名称',
			    name: 'deptName',
			}, 
			{
				fieldLabel: '单据编号',
		        name: 'docCode'
			},
			
			{
				fieldLabel: '新增警力数',
		        name: 'policeAdd'
			},
			 
	        {
				fieldLabel: '应急机要通信接待用车编制数量',
		        name: 'emergencyVehAuthNum',
			}, 
			{
				fieldLabel: '应急机要通信接待用车实有数量',
		        name: 'emergencyVehAddNum',
			}, 
			{
				fieldLabel: '应急机要通信接待用车申请数量',
		        name: 'emergencyVehRealNum',
			},
			
			{
				fieldLabel: "行政执法用车编制数量",
		        name: 'enforcementVehAuthNum'
		    },
		    {
				fieldLabel: "行政执法用车实有数量",
		        name: 'enforcementVehRealNum',
		    },	
		    {
				fieldLabel: "行政执法用车申请数量",
		        name: 'enforcementVehAddNum',
		    },
		    {
		    	fieldLabel: '行政执法特种专业用车编制数量',
                dataIndex: 'specialVehAuthNum',
               
              },
              {
            	  fieldLabel: '行政执法特种专业用车实有数量',
                  dataIndex: 'specialVehRealNum',
                
               },
               {
            	   fieldLabel: '行政执法特种专业用车申请数量',
                   dataIndex: 'specialVehAddNum',
                  
                },
            
           {
                	fieldLabel: '一般执法执勤用车编制数量',
                dataIndex: 'normalVehAuthNum',
              
             },
             {
            	 fieldLabel: '一般执法执勤用车实有数量',
                 dataIndex: 'normalVehRealNum',
                 
              },
              {
            	  fieldLabel: '一般执法执勤用车申请数量',
                  dataIndex: 'normalVehAddNum',
                 
               },
        {
            	   fieldLabel: '执法执勤特种专业用车编制数量',
          dataIndex: 'majorVehAuthNum',
         
      },
      {
    	  fieldLabel: '执法执勤特种专业用车实有数量',
          dataIndex: 'majorVehRealNum',
         
      },
      {
    	  fieldLabel: '执法执勤特种专业用车申请数量',
          dataIndex: 'majorVehAddNum',
        
      },
      {
    	  fieldLabel: '原因',
          dataIndex: 'cause',
      },
      {
    	  fieldLabel: '状态',
          dataIndex: 'status',
          renderer: function(val,metaData) {
              switch(val){
                  case "0":
                   //   metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('申请中') + '"'; 
                      return "<span style='color:blue;'>申请中</span>";
                  case "1":
                    //  metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('已驳回') + '"'; 
                      return "<span style='color:red;'>已驳回</span>";
                  case "2":
                   //   metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('审核通过') + '"'; 
                      return "<span style='color:green;'>审核通过</span>";
              }
          }
      },
      {
			xtype: 'displayfield'
		},
      /*{
			id: 'viewAthorizedAttachFile',
			xtype: 'tbtext',
			html:'<div style="font:18px arial;color:#0D85CA;">审核信息</div>'
		},*/
		]
	}],

	buttonAlign : 'center',
	buttons : [{
				text : '关闭',
				handler: function(btn){
					btn.up("viewAuthorizedVehApply").close();
				}
			}]
});