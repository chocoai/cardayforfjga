package com.cmdt.carrental.common.integration;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cmdt.carrental.common.constants.Constants;
import com.cmdt.carrental.common.model.M2MResp;
import com.cmdt.carrental.common.util.HttpClientUtil;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.RespResult;

@Component
public class M2MService {
	
	private static final Logger LOG = LoggerFactory.getLogger(M2MService.class);
	
	@Value("${service.m2mServiceUrl}")
	private String m2mServiceUrl;
	
	
	public M2MResp sendM2M(String msisdn,int flag){
		try {
//			Map<String,String> params = new HashMap<String,String>();
//			params.put("msisdn",msisdn);
//			params.put("state", flag == 0?"1":"0");
//			params.put("homecity", Constants.M2M_CITYCODE);
//			params.put("appid",Constants.M2M_APPID);
//			params.put("ebid",Constants.M2M_EBID);
//			params.put("transid",transId);
//			params.put("token",SHA256(Constants.M2M_APPID + Constants.M2M_PASSWORD + transId));
			
			String transId = generateTransId(Constants.M2M_APPID);
            String tokenId = SHA256(Constants.M2M_APPID + Constants.M2M_PASSWORD + transId);
            String state = flag == 0?"1":"0";
			String url_param = "?transid="+transId+"&appid="+Constants.M2M_APPID+"&ebid="+Constants.M2M_EBID+"&token="+tokenId+"&msisdn="+msisdn+"&homecity="+Constants.M2M_CITYCODE+"&state="+state;
			//invoke m2m service
			String responseStr = null;
//			RespResult result = HttpClientUtil.sendPostForM2M("http://218.207.217.31:14002/v2/boss/memberreset".concat(url_param), null, Constants.M2M_CONTENT_TYPE);
			RespResult result = HttpClientUtil.sendPostForM2M(m2mServiceUrl.concat(url_param), null, Constants.M2M_CONTENT_TYPE);
			if(result != null){
			    responseStr = result.getBeanStr();
			}
			return JsonUtils.json2Object(responseStr, M2MResp.class);
		} catch (Exception e) {
			LOG.error("Call m2m service error, cause by :\n",e);
			return null;
		}
	}
	
    public static void main(String[] args)
    {
//        String transId = generateTransId("5911323401");
//        System.out.println(transId);
//        System.out.println(SHA256("5911323401".concat("D2Dapi_591").concat(transId)));
//        sendM2M("1064826718524",1);
    }
	
	
	private static final SimpleDateFormat DATEFORMAT_ALL = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final SimpleDateFormat DATEFORMAT_DAY = new SimpleDateFormat("ddHHmmss");
	private static String generateTransId(String appid){
	    return appid.concat(DATEFORMAT_ALL.format(new Date())).concat(DATEFORMAT_DAY.format(new Date()));
	}
	
//    public String EncoderByMd5(String plainText)
//    {
//        try
//        {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            try
//            {
//                md.update(plainText.getBytes("UTF8"));
//            }
//            catch (UnsupportedEncodingException e)
//            {
//                e.printStackTrace();
//            }
//            byte b[] = md.digest();
//            int i;
//            StringBuffer buf = new StringBuffer(200);
//            for (int offset = 0; offset < b.length; offset++)
//            {
//                i = b[offset] & 0xff;
//                if (i < 16)
//                    buf.append("0");
//                buf.append(Integer.toHexString(i));
//            }
//            return buf.toString();
//        }
//        catch (NoSuchAlgorithmException e)
//        {
//            LOG.error("Md5加密异常", e);
//            return null;
//        }
//    }
	
    public static String SHA256(final String strText)
    {
        return SHA(strText, "SHA-256");
    }
	
    /**
     * 字符串 SHA 加密
     * 
     * @param strSourceText
     * @return
     */
    private static String SHA(final String strText, final String strType)
    {
        // 返回值
        String strResult = null;
        
        // 是否是有效字符串
        if (strText != null && strText.length() > 0)
        {
            try
            {
                // SHA 加密开始
                // 创建加密对象 并傳入加密類型
                MessageDigest messageDigest = MessageDigest.getInstance(strType);
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                // 得到 byte 類型结果
                byte byteBuffer[] = messageDigest.digest();
                
                // 將 byte 轉換爲 string
                StringBuffer strHexString = new StringBuffer();
                // 遍歷 byte buffer
                for (int i = 0; i < byteBuffer.length; i++)
                {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1)
                    {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回結果
                strResult = strHexString.toString();
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
        }
        
        return strResult;
    }
	
}
