package com.cmdt.carrental.platform.service.biz.service;

import com.cmdt.carrental.platform.service.api.ow.CaptchaApi;
import com.cmdt.carrental.platform.service.util.ThreadUtils;
import com.google.code.kaptcha.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author: joe
 * @Date: 17-7-10 上午10:48.
 * @Description:
 */
@Service
public class PlatformCaptchaService {

    private static final Logger LOG = LoggerFactory.getLogger(PlatformCaptchaService.class);

    @Autowired
    private Producer captchaProducer = null;

    private Map<String, CaptchaCode> codeMap = new ConcurrentHashMap<>();

    private ScheduledExecutorService captchaRemoveThread =
            ThreadUtils.newDaemonSingleThreadScheduledExecutor("captcha-remove");

    /**
     * 验证码有效期 5分钟
     */
    private static final Long Valid_Period = 5 * 60 * 1000l;

    @PostConstruct
    public void init() {
        captchaRemoveThread.schedule(new Runnable() {
            @Override
            public void run() {
                Long now = System.currentTimeMillis();
                for (Map.Entry<String, CaptchaCode> entry : codeMap.entrySet()) {
                    if (now - entry.getValue().timestamp > Valid_Period) {
                        codeMap.remove(entry.getKey());
                    }
                }
            }
        }, 5, TimeUnit.MINUTES);
    }

    @PreDestroy
    public void close() {
        captchaRemoveThread.shutdownNow();
    }

    public BufferedImage createCaptcha(String uuid) {
        String capText = captchaProducer.createText();

        codeMap.put(uuid, new CaptchaCode(capText));

        return captchaProducer.createImage(capText);
    }

    public boolean isValid(String uuid, String code) {
        CaptchaCode captcha = codeMap.get(uuid);
        if (captcha != null && code.equals(captcha.code)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证码 带生成时间戳
     */
    class CaptchaCode {
        private String code;
        private Long timestamp;

        CaptchaCode(String code) {
            this.code = code;
            timestamp = System.currentTimeMillis();
        }
    }
}
