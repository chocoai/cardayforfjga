package com.cmdt.carday.microservice.api;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;

import com.cmdt.carday.microservice.dto.HelloDto;

@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloApiTest extends BaseApiTest {
    
	
    @Test
    public void testGetHello() throws Exception {
    	mockMvc.perform(get("/helloWithPage")
    		.param("limit", "5").param("offset", "5").param("name", "xxx")
	        .accept(MediaType.APPLICATION_JSON))
	        .andExpect(status().isOk())
	        .andDo(MockMvcRestDocumentation.document("helloWithPage", preprocessResponse(prettyPrint())));
    }

    
    @Test
    public void testAddHello() throws Exception {
    	//data preparation
		HelloDto dto = new HelloDto();
		dto.setId(1L);
		dto.setDate("2017-12-09 00:01:01");
		dto.setName("tester");
		
	    mockMvc.perform(post("/hello").contentType(MediaType.APPLICATION_JSON)
	            .content(mapper.writeValueAsString(dto))
	            .accept(MediaType.APPLICATION_JSON))
	            .andExpect(status().is2xxSuccessful())
	            .andDo(MockMvcRestDocumentation.document("hello", preprocessResponse(prettyPrint())));
    }

}
