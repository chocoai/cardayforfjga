$(function(){
	$(".obdInfo").hide();
	$(".mapInfo").hide();
	
	//obd基础信息查询
	$(".basic").click(function(){
		$(".obdInfo").hide();
		$(".mapInfo").hide();
		var obdimei = $("#obdimei").val();
		$(".basic img").attr("src","btnActivebg.png");
		if(obdimei == ''){
			$(".obdInfo").addClass('warntext');
			$(".obdInfo").html('GPS IMEI / SN 号不能为空！');
			$(".obdInfo").show();
			$("#obdimei").focus();
			return false;
		}
		$.ajax({
			contentType: "application/json",
			dataType: "json",
			url:"../services/device/queryDeviceStaticData/"+obdimei,
			success:function(resp){
				var basicdateHTML = "";
				if(resp.status == '000'){
					$(".obdInfo").removeClass('warntext');
					var data = resp.result;
					basicdateHTML += 
								"<p><strong>GPS型号:</strong> <span>"+(data.deviceModel == null ? '' : data.deviceModel)+"</span></p>"
								+"<p><strong>厂家:</strong> <span>"+(data.deviceVendor == null ? '' : data.deviceVendor)+"</span></p>"
								+"<p><strong>采购日期:</strong> <span>"+(data.purchaseTime == null ? '' : data.purchaseTime)+"</span></p>"
								+"<p><strong>固件版本:</strong> <span>"+(data.firmwareVersion == null ? '' : data.firmwareVersion)+"</span></p>"
								+"<p><strong>GPS绑定的车牌号:</strong> <span>"+(data.vehicleNumber == null ? '' : data.vehicleNumber)+"</span></p>"
								+"<p><strong>GPS绑定的License号:</strong> <span>"+(data.licenseNumber == null ? '' : data.licenseNumber)+"</span></p>"
								+"<p><strong>License状态:</strong> <span>"+(data.licenseStatus == null ? '' : data.licenseStatus)+"</span></p>"
//								+"<p><strong>绑定GPS时间:</strong> <span>"+(data.bindingdate == null ? '' : data.bindingdate)+"</span></p>"
								+"<p><strong>有效期开始时间:</strong> <span>"+(data.startTime == null ? '' : data.startTime)+"</span></p>"
								+"<p><strong>有效期结束时间:</strong> <span>"+(data.endTime == null ? '' : data.endTime)+"</span></p>"
								+"<p><strong>当前状态:</strong> <span>"+(data.deviceStatus == 1 ? '正常' : '异常')+"</span></p>";
				}else{
					basicdateHTML = "<p style='text-align: center;color: #B31D2D'><span>"+data.resultMsg+"</span></p>";
				}
				$(".obdInfo").html(basicdateHTML);
				$(".obdInfo").show();
			}
		});
	});
	

	//获取obd最新数据
	$(".newest").click(function(){
		$(".obdInfo").hide();
		var obdimei = $("#obdimei").val();
		$(".newest img").attr("src","btnActivebg.png");
		if(obdimei == ''){
			$(".obdInfo").addClass('warntext');
			$(".obdInfo").html('GPS IMEI / SN 号不能为空！');
			$(".obdInfo").show();
			$("#obdimei").focus();
			return false;
		}
			
		$.ajax({
			contentType: "application/json",
			dataType: "json",
			url:"../services/device/queryDeviceRealtimeData/"+obdimei,
			success:function(resp){
				var newdateHTML = "";
				if(resp.status == '000'){
					$(".obdInfo").removeClass('warntext');
					var data = resp.result;
					newdateHTML += 
//								"<p><strong>OBD读取的车架号:</strong> <span>"+(data.realVin == null ? '' : data.realVin)+"</span></p>"
//								+"<p><strong>OBD绑定的车架号:</strong> <span>"+(data.vehiclevin == null ? '' : data.vehiclevin)+"</span></p>"
//								"<p><strong>OBD绑定的车牌号:</strong> <span>"+(data.vehicleNumber == null ? '' : data.vehicleNumber)+"</span></p>"
//								+"<p><strong>OBD绑定的license号:</strong> <span>"+(data.license == null ? '' : data.license)+"</span></p>"
//								+"<p><strong>绑定OBD时间:</strong> <span>"+(data.bindingdate == null ? '' : data.bindingdate)+"</span></p>"
//								+"<p><strong>有效期开始时间:</strong> <span>"+(data.activestime == null ? '' : data.activestime)+"</span></p>"
//								+"<p><strong>有效期结束时间:</strong> <span>"+(data.activeetime == null ? '' : data.activeetime)+"</span></p>"
								"<p><strong>GPS累计里程:</strong> <span>"+(data.mileage == null ? 0 : data.mileage/1000.0)+"</span>公里</p>"
								//+"<p><strong>油耗:</strong> <span>"+(data.fuel == null ? 0 : data.fuel)+"</span>L</p>"
								//+"<p><strong>速度:</strong> <span>"+(data.speed == null ? 0 : data.speed)+"</span>Km/h</p>"
								//+"<p><strong>转速:</strong> <span>"+(data.rate == null ? 0 : data.rate)+"</span>r/min</p>"
								+"<p><strong>车辆所在经度:</strong> <span>"+(data.longitude == null ? '' : data.longitude)+"</span></p>"
								+"<p><strong>车辆所在纬度:</strong> <span>"+(data.latitude == null ? '' : data.latitude)+"</span></p>"
								+"<p><strong>车辆所在地址:</strong> <span>"+(data.address == null ? '' : data.address)+"</span></p>"
								+"<p><strong>最后上报时间:</strong> <span>"+(data.time == null ? '' : data.time)+"</span></p>";
				}else{
					newdateHTML = "<p style='text-align: center;color: #B31D2D'><span>"+data.messages+"</span></p>";
				}
				$(".obdInfo").html(newdateHTML);
				$(".obdInfo").show();
				
				//show map
                var map = new BMap.Map("container");    
                var point = new BMap.Point(data.longitude,data.latitude);    
                map.centerAndZoom(point,12);
                var marker = new BMap.Marker(point);  // 创建标注  
                map.addOverlay(marker);              // 将标注添加到地图中   
                $(".mapInfo").show();
			}
		});
	});
	
});
