package com.cmdt.carrental.platform.service.api.ow;

import com.cmdt.carrental.platform.service.biz.service.PlatformCaptchaService;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.common.WsResponse;
import com.cmdt.carrental.platform.service.util.ThreadUtils;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

import static com.cmdt.carrental.platform.service.common.Constants.API_STATUS_FAILURE;
import static com.cmdt.carrental.platform.service.common.Constants.API_STATUS_PARAM_ILLEGAL;

public class CaptchaApi {

    private static final Logger LOG = LoggerFactory.getLogger(CaptchaApi.class);

    @Autowired
    private PlatformCaptchaService captchaService;

    /**
     * 生成 图片验证码
     * @param uuid 每次请求的唯一标识
     * @return
     * @throws IOException
     */
    @GET
    @Path("/captcha-image")
    @Consumes("image/jpeg")
    @Produces("image/jpeg")
    public Response roleList(@QueryParam("uuid") String uuid) {

        if (StringUtils.isEmpty(uuid)) {
            // UUID 不能为空
            return Response.status(Status.BAD_REQUEST).build();
        }

        PhaseInterceptorChain.getCurrentMessage().getExchange().put(Constants.INTECEPTOR_SKIP_FLAG, Boolean.TRUE);

//        String capText = captchaProducer.createText();

//        BufferedImage bi = captchaProducer.createImage(capText);
        BufferedImage bi = captchaService.createCaptcha(uuid);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();


        ResponseBuilder builder = null;
        try {
            ImageIO.write(bi, "jpg", outputStream);

            // Response 使用BinaryDataProvider进行二进制流传输，
            // BinaryDataProvider 不识别 BufferedImage,
            // 具体能识别的对象，请查看 BinaryDataProvider.isWriteable()
            // 目前能识别 byte[] / InputStream / StreamingOutput / Reader / File
            builder = Response.ok(outputStream.toByteArray(), "image/jpeg");

            CacheControl cacheControl = new CacheControl();
            cacheControl.setNoCache(true);
            cacheControl.setNoStore(true);
            cacheControl.setMustRevalidate(true);
            builder.cacheControl(cacheControl);

            outputStream.flush();
//            codeMap.put(uuid, capText);
        } catch (IOException e) {
            LOG.warn("error while create captcha!", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                LOG.warn("error while booleanclose captcha output stream!", e);
                return Response.status(Status.INTERNAL_SERVER_ERROR).build();
            }
        }

        return builder.build();
    }

    /**
     * 校验 验证码
     * @param uuid 请求验证码时的 标识
     * @param code 验证码
     * @return
     */
    @GET
    @Path("/validate")
    @Produces(MediaType.APPLICATION_JSON)
    public WsResponse validateCode(@QueryParam("uuid")String uuid,
                                   @QueryParam("code") String code) {
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(uuid)) {
            return WsResponse.failureStatus(API_STATUS_PARAM_ILLEGAL, "验证码为空");
        }

        if (captchaService.isValid(uuid, code)) {
            return WsResponse.success();
        } else {
            return WsResponse.failure("验证码错误");
        }
    }
}
