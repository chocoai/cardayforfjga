package com.cmdt.carday.microservice.business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cmdt.carday.microservice.dto.HelloDto;
import com.cmdt.carday.microservice.dto.HelloPutDto;
import com.cmdt.carday.microservice.dto.RespDto;

@Service
public class SampleService {
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public RespDto getHelloListAsSuccess(Object... t){
		List<HelloDto> dtos = new ArrayList<HelloDto>();
		dtos.add(new HelloDto(1L,sdf.format(new Date()),"Sample hello0081"));
		dtos.add(new HelloDto(2L,sdf.format(new Date()),"Sample hello5682"));
		HelloPutDto putDto = new HelloPutDto();
		putDto.setDtos(dtos);
		return new RespDto(putDto);
	}
	
	public RespDto getHelloAsSuccess(Object... t){
		return new RespDto(new HelloDto(999L,sdf.format(new Date()),"let's say hello to - " + t));
	}
	
	
	public RespDto processHelloAsFailure(Object... t){
		return new RespDto("504", "处理失败！");
	}
	
	
	public RespDto processHelloAsSuccess(Object... t){
		return new RespDto(t);
	}
}
