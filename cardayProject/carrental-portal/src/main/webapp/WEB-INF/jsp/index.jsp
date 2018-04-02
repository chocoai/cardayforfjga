<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=10, user-scalable=yes">
    <meta charset="UTF-8">
    <meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">

    <script type="text/javascript">
        var Ext = Ext || {}; // Ext namespace won't be defined yet...

        // This function is called by the Microloader after it has performed basic
        // device detection. The results are provided in the "tags" object. You can
        // use these tags here or even add custom tags. These can be used by platform
        // filters in your manifest or by platformConfig expressions in your app.
        //
        Ext.beforeLoad = function (tags) {
            var s = location.search,  // the query string (ex "?foo=1&bar")
                profile;

            // For testing look for "?classic" or "?modern" in the URL to override
            // device detection default.
            //
            if (s.match(/\bclassic\b/)) {
                profile = 'classic';
            }
            else if (s.match(/\bmodern\b/)) {
                profile = 'modern';
            }
            // uncomment this if you have added native build profiles to your app.json
            /*else if (tags.webview) {
                if (tags.ios) {
                    profile = 'ios';
                }
                // add other native platforms here
            }*/
            else {
                //profile = tags.desktop ? 'classic' : 'modern';
                profile = tags.phone ? 'modern' : 'classic';
            }

            Ext.manifest = profile; // this name must match a build profile name

            // This function is called once the manifest is available but before
            // any data is pulled from it.
            //
            //return function (manifest) {
                // peek at / modify the manifest object
            //};
        };
    </script>
    
    <title>福建省公安机关执法执勤用车管理平台</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/dataload.js"></script>
     <!-- The line below must be kept intact for Sencha Cmd to build your application -->
    <script id="microloader" data-app="6b4e395f-e7e5-4f59-94e2-1f036b63c901" type="text/javascript" src="${pageContext.request.contextPath}/bootstrap.js"></script>
	
    <!--加载压缩运算工具-->
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/lz-string.min.js"></script>
    <link rel="shortcut icon" href="resources\images\icons\cmdt_sc_logo.ico" type="image/x-icon">
   
</head>
<body>




</body>
</html>
