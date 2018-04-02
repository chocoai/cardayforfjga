package com.cmdt.carday.microservice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.robwin.markup.builder.MarkupLanguage;
import io.github.robwin.swagger2markup.GroupBy;
import io.github.robwin.swagger2markup.Swagger2MarkupConverter;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import springfox.documentation.staticdocs.SwaggerResultHandler;
import springfox.documentation.swagger2.web.Swagger2Controller;

import java.text.SimpleDateFormat;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@AutoConfigureMockMvc
//@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class BaseApiTest {


    protected String snippetDir = "target/generated-snippets";
    protected String outputDir = "target/asciidoc";

    protected ObjectMapper mapper = new ObjectMapper();

    protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Autowired
    protected MockMvc mockMvc;

    @After
    public void Test() throws Exception {
        // 得到swagger.json,写入outputDir目录中
        mockMvc.perform(get(Swagger2Controller.DEFAULT_URL).accept(MediaType.APPLICATION_JSON))
                .andDo(SwaggerResultHandler.outputDirectory(outputDir).build())
                .andExpect(status().isOk())
                .andReturn();

        // 读取上一步生成的swagger.json转成asciiDoc,写入到outputDir
        // 这个outputDir必须和插件里面<generated></generated>标签配置一致
        Swagger2MarkupConverter.from(outputDir + "/swagger.json")
                .withPathsGroupedBy(GroupBy.TAGS)// 按tag排序
                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)// 格式
                .withExamples(snippetDir)
                .build()
                .intoFolder(outputDir);// 输出
    }

    /**
     *
     * @param apiUrl 测试的API-URL
     * @param t POST请求的参数
     * @param <T>
     * @throws Exception
     */
    public <T> void runPost(String apiUrl, T t) throws Exception {

        String restDocDir = apiUrl.substring(1).replace("/", "-");

        mockMvc.perform(post(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(t)))
                .andExpect(status().is2xxSuccessful())
                .andDo(MockMvcRestDocumentation.document(restDocDir, preprocessResponse(prettyPrint())));
    }

    /**
    *
    * @param apiUrl 测试的API-URL
    * @param params POST请求的参数
    * @param <T>
    * @throws Exception
    */
   public <T> void runPost(String apiUrl, Map<String, String> params) throws Exception {

       String restDocDir = apiUrl.substring(1).replace("/", "-");
       MockHttpServletRequestBuilder requestBuilder = post(apiUrl);

       if (params != null) {
           for (Map.Entry<String, String> entry : params.entrySet()) {
               if (StringUtils.isNotEmpty(entry.getKey())
                       && StringUtils.isNotEmpty(entry.getValue())) {
                   requestBuilder.param(entry.getKey(), entry.getValue());
               }
           }
       }
       mockMvc.perform(requestBuilder
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().is2xxSuccessful())
               .andDo(MockMvcRestDocumentation.document(restDocDir, preprocessResponse(prettyPrint())));
   }
    /**
     *
     * @param apiUrl GET请求的API-URL
     * @param params GET请求的 请求参数(request-param)
     * @throws Exception
     */
    public void runGet(String apiUrl, Map<String, String> params) throws Exception {

        String restDocDir = apiUrl.substring(1).replace("/", "-");

        MockHttpServletRequestBuilder requestBuilder = get(apiUrl);

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (StringUtils.isNotEmpty(entry.getKey())
                        && StringUtils.isNotEmpty(entry.getValue())) {
                    requestBuilder.param(entry.getKey(), entry.getValue());
                }
            }
        }

        mockMvc.perform(
                requestBuilder
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(MockMvcRestDocumentation.document(restDocDir, preprocessResponse(prettyPrint())));
    }

    /**
    *
    * @param apiUrl 测试的API-URL
    * @param t PUT请求的参数
    * @param <T>
    * @throws Exception
    */
   public <T> void runPut(String apiUrl, T t) throws Exception {

       String restDocDir = apiUrl.substring(1).replace("/", "-");

       mockMvc.perform(put(apiUrl)
               .contentType(MediaType.APPLICATION_JSON)
               .content(mapper.writeValueAsString(t)))
               .andExpect(status().is2xxSuccessful())
               .andDo(MockMvcRestDocumentation.document(restDocDir, preprocessResponse(prettyPrint())));
   }

   /**
   *
   * @param apiUrl 测试的API-URL
   * @param params PUT请求的参数
   * @param <T>
   * @throws Exception
   */
  public <T> void runPut(String apiUrl, Map<String, String> params) throws Exception {

      String restDocDir = apiUrl.substring(1).replace("/", "-");
      MockHttpServletRequestBuilder requestBuilder = put(apiUrl);

      if (params != null) {
          for (Map.Entry<String, String> entry : params.entrySet()) {
              if (StringUtils.isNotEmpty(entry.getKey())
                      && StringUtils.isNotEmpty(entry.getValue())) {
                  requestBuilder.param(entry.getKey(), entry.getValue());
              }
          }
      }
      mockMvc.perform(requestBuilder
              .contentType(MediaType.APPLICATION_JSON))
              .andExpect(status().is2xxSuccessful())
              .andDo(MockMvcRestDocumentation.document(restDocDir, preprocessResponse(prettyPrint())));
  }
    
  /**
  *
  * @param apiUrl 测试的API-URL
  * @param t Delete请求的参数
  * @param <T>
  * @throws Exception
  */
  public <T> void runDelete(String apiUrl, T t) throws Exception {

      String restDocDir = apiUrl.substring(1).replace("/", "-");

      mockMvc.perform(delete(apiUrl)
              .contentType(MediaType.APPLICATION_JSON)
              .content(mapper.writeValueAsString(t)))
              .andExpect(status().is2xxSuccessful())
              .andDo(MockMvcRestDocumentation.document(restDocDir, preprocessResponse(prettyPrint())));
  }

    /**
    *
    * @param apiUrl DELETE测试的API-URL
    * @throws Exception
    */
   public <T> void runDelete(String apiUrl,Map<String, String> params) throws Exception {

       String restDocDir = apiUrl.substring(1).replace("/", "-");

       MockHttpServletRequestBuilder requestBuilder = delete(apiUrl);

       if (params != null) {
           for (Map.Entry<String, String> entry : params.entrySet()) {
               if (StringUtils.isNotEmpty(entry.getKey())
                       && StringUtils.isNotEmpty(entry.getValue())) {
                   requestBuilder.param(entry.getKey(), entry.getValue());
               }
           }
       }
       mockMvc.perform(requestBuilder
    		   .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().is2xxSuccessful())
               .andDo(MockMvcRestDocumentation.document(restDocDir, preprocessResponse(prettyPrint())));
   }
}
