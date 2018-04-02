Ext.define('Admin.Application', {
    extend: 'Ext.app.Application',
    
    name: 'Admin',

    stores: [
        'NavigationTree',
        'NavigationMainTree',
        'NavigationVehicleMonitoringTree',
    ],

    defaultToken : 'vehicleMonitoringMain',

    // The name of the initial view to create. This class will gain a "viewport" plugin
    // if it does not extend Ext.Viewport.
    //
    mainView: 'Admin.view.main.Main',
    launch:function(){
         Ext.Ajax.on('requestcomplete', function(conn, response, option) {
        	if(response&&response.responseText!=undefined&&response.responseText.length>0){
        		if(response.responseText.indexOf('<title>企业用车云服务系统</title>')>0){
	        		Ext.MessageBox.alert('提示','登入超时,系统将自动跳转到登陆页面,请重新登入！', function(){
	        			var ent = window.sessionStorage.shortname;
	        			if(ent!=undefined&&window.sessionStorage.shortname.length>0){
	                        window.location.href="gh_login.jsp"; 
	                   }else{
	                        window.location.href="login"; 
	                   }
	        		});
        		}
        	}
         });
         Ext.Ajax.on('requestexception', function(conn, response, option) {
            if(response.status&&(response.status==401||response.status==302)){
                if(response.getResponseHeader('timeout')){
                    Ext.MessageBox.alert('提示','登入超时,系统将自动跳转到登陆页面,请重新登入！', function(){
                        var ent = window.sessionStorage.shortname;
                        if(ent!=undefined&&window.sessionStorage.shortname.length>0){
                             window.location.href="gh_login.jsp"; 
                        }else{
                             window.location.href="login"; 
                        }
                    }); 
                }
            }else{
            	if(response.status&&response.status!=-1){
            	   Ext.MessageBox.alert("消息提示","操作失败!错误码："+response.status);
            	}
            }
         });
    },
    onAppUpdate: function () {
        Ext.Msg.confirm('Application Update', 'This application has an update, reload?',
            function (choice) {
                if (choice === 'yes') {
                    window.location.reload();
                }
            }
        );
    }
});