Ext.define('Admin.Application', {
    extend: 'Ext.app.Application',
    
    name: 'Admin',

    stores: [
        'NavigationTree'
    ],

    defaultToken : 'dashboard',

    // The name of the initial view to create. This class will gain a "viewport" plugin
    // if it does not extend Ext.Viewport.
    //
    mainView: 'Admin.view.main.Main',
    launch:function(){
        Ext.Ajax.on('requestcomplete', function(conn, response, option) {
       	
        });
        Ext.Ajax.on('requestexception', function(conn, response, option) {
           if(response.status&&(response.status==401||response.status==302)){
               if(response.getResponseHeader('timeout')){
                   Ext.MessageBox.alert('提示','登入超时,系统将自动跳转到登陆页面,请重新登入!', function(){
                          window.location.href="login"; 
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
