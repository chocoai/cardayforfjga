<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>GPS设备使用查询</title>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<link rel="stylesheet" href="obdDemand.css" />
		
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />    
        <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=134db1b9cf1f1f2b4427210932b34dcb"></script> 
	</head>
	<body>
		<div class="top">
			<div class="return" onclick="#">
				<img src="return.png" alt="" />
			</div>
			<div class="topTitle">GPS设备使用查询</div>
		</div>
		<div class="content">
			<div class="obdSearch"><input type="text" id="obdimei" placeholder="输入您要查询的GPS IMEI或SN编号"/></div>
			<div class="SearchBth">
				<div class="basic">
					<img src="btnbg.png" alt="" />
					<span>基础信息查询</span>
				</div>
				<div class="newest">
					<img src="btnbg.png" alt="" />
					<span>实时数据查询</span>
				</div>
			</div>
			<div class="obdInfo"></div>
			<div class="mapInfo" id="container"></div>
		</div>
		<script src="jquery-1.11.3.min.js"></script>
		<script src="obdDemand.js"></script>
	</body>
</html>