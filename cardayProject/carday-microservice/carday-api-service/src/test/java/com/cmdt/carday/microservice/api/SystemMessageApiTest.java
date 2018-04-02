package com.cmdt.carday.microservice.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cmdt.carday.microservice.model.request.message.MessageCreateDto;
import com.cmdt.carday.microservice.model.request.message.MessagePageDto;

@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@RunWith(SpringRunner.class)
@SpringBootTest
public class SystemMessageApiTest extends BaseApiTest {

	@Test
	public void showMessagesTest() throws Exception{
		MessagePageDto messagePageDto=new MessagePageDto();
		messagePageDto.setCurrentPage(1);
		messagePageDto.setNumPerPage(10);
		messagePageDto.setUserId(149L);
	//	messagePageDto.setType("system");
		runPost("/systemMessage/message/list", messagePageDto);
	}
	
	@Test
	public void addSysMessageTest() throws Exception{
		MessageCreateDto messageCreateDto =new MessageCreateDto();
		messageCreateDto.setContent("testService");
		messageCreateDto.setTitle("test");
		messageCreateDto.setUserId(149L);
		runPost("/systemMessage/message",messageCreateDto);
	}
	

}
