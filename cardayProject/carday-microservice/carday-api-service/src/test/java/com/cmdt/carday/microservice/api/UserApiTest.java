package com.cmdt.carday.microservice.api;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cmdt.carday.microservice.model.request.user.ResetPasswordDto;
import com.cmdt.carday.microservice.model.request.user.UserLoginDto;
import com.cmdt.carday.microservice.model.request.user.VerificationCodeDto;
import com.cmdt.carday.microservice.model.request.user.VerificationCodeRequestDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

/**
 * @Author: joe
 * @Date: 17-7-13 下午2:53.
 * @Description:
 */
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserApiTest extends BaseApiTest {

    @Test
    public void loginTest() throws Exception {
        UserLoginDto dto = new UserLoginDto();
        dto.setUsername("admin_ete");
        dto.setPassword("123456");

        runPost("/user/login", dto);
    }

    @Test
    public void sendVerificationCodeTest() throws Exception {
        VerificationCodeRequestDto dto = new VerificationCodeRequestDto();
        dto.setPhone("18888888888");
        dto.setUsername("admin_ete");

        runPost("/user/sendVerificationCode", dto);
    }

    @Test
    public void checkVerificationCodeTest() throws Exception {
        VerificationCodeDto dto = new VerificationCodeDto();
        dto.setCode("123456");
        dto.setPhoneNumber("18696141766");

        runPost("/user/checkVerificationCode", dto);
    }

    @Test
    public void resetPasswordTest() throws Exception {
        ResetPasswordDto dto = new ResetPasswordDto();
        dto.setUsername("joe");
        dto.setPassword("xxxxxx");

        runPost("/user/resetPassword", dto);
    }

    @Test
    public void validatePhoneTest() throws Exception {

        runGet("/user/phone-validate", Collections.singletonMap("phone", "18696141766"));
    }

}
