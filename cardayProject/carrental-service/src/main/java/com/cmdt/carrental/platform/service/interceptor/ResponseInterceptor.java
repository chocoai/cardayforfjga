package com.cmdt.carrental.platform.service.interceptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.platform.service.common.Constants;

public class ResponseInterceptor extends AbstractPhaseInterceptor<Message>{
	
	private static final Logger log = LoggerFactory.getLogger(ResponseInterceptor.class);
	
	 public ResponseInterceptor() { 
	        //使用pre_stream，意思为在流关闭之前 
	        super(Phase.PRE_STREAM); 
	 }
	 
	
	 
	 public void handleMessage(Message message) { 
		 
	        try { 
	        
	        	
	            OutputStream os = message.getContent(OutputStream.class); 
	 
	            CachedStream cs = new CachedStream(); 
	 
	            message.setContent(OutputStream.class, cs); 
	 
	            message.getInterceptorChain().doIntercept(message); 
	            
	            Boolean skip = (Boolean) PhaseInterceptorChain.getCurrentMessage()
	            		.getExchange().get(Constants.INTECEPTOR_SKIP_FLAG);
	            
	 
	            CachedOutputStream csnew = (CachedOutputStream) message.getContent(OutputStream.class); 
	            InputStream in = csnew.getInputStream(); 
	             
	            String dtoJson = IOUtils.toString(in);
	            
	            String retJson = "";
	            if(null==skip){
	            	       //正常处理
	            	       if(JsonUtils.isJson(dtoJson)){
	            	    	   //复杂数据对象会转成正常JSON
	            	    	    if(!dtoJson.contains(Constants.STATUS_STR_LOWERCASE)&&!dtoJson.contains(Constants.MESSAGES_STR_LOWERCASE)&&!dtoJson.contains(Constants.RESULT_STR_LOWERCASE)){
	            	    	     //如果不是Response格式，自动添加
	            	    	     retJson += "{\""+Constants.STATUS_STR_LOWERCASE+"\":\""+Constants.API_STATUS_SUCCESS+"\","+"\""+Constants.MESSAGES_STR_LOWERCASE+"\":[\""+Constants.API_MESSAGE_SUCCESS+"\"],";
		                         retJson +="\""+Constants.RESULT_STR_LOWERCASE+"\":"+dtoJson+"}";
	            	    	    }else{
	            	    	    	retJson = dtoJson;
	            	    	    }
	            	       }else{
	            	    	   //如果是简单类型的返回，例如String
	            	    	   
	            	    	   if(dtoJson!=null&&dtoJson.length()>0){
	            	    		   retJson += "{\""+Constants.STATUS_STR_LOWERCASE+"\":\""+Constants.API_STATUS_SUCCESS+"\","+"\""+Constants.MESSAGES_STR_LOWERCASE+"\":[\""+Constants.API_MESSAGE_SUCCESS+"\"],";
			                       retJson +="\""+Constants.RESULT_STR_LOWERCASE+"\":\""+dtoJson+"\"}";   
	            	    	   }else{
	            	    		   //处理 空返回 void(并未生效，还需要研究)
	            	    		   //retJson += "{\""+Constants.STATUS_STR+"\":\""+Constants.API_STATUS_SUCCESS+"\","+"\""+Constants.MSG_STR+"\":[\""+Constants.API_MESSAGE_SUCCESS+"\"],";
			                       //retJson +="\""+Constants.RESULT_STR+"\":\""+dtoJson+"\"}";   
	            	    	   }
	            	       }    
	            }else{
	            	//异常已经封装，所以不做处理
	            	retJson = dtoJson;
	            }
	           	            
	            //这里对json做处理，处理完后同理，写回流中 
	            IOUtils.copy(new ByteArrayInputStream(retJson.getBytes()), os); 
	             
	            cs.close();
	            os.flush(); 
	 
	            message.setContent(OutputStream.class, os); 
	 
	 
	        } catch (Exception e) { 
	            log.error("Error when split original inputStream. CausedBy : " + "\n" + e); 
	        } 
	    } 
	 
	    private class CachedStream extends CachedOutputStream { 
	 
	        public CachedStream() { 
	 
	            super(); 
	 
	        } 
	 
	        protected void doFlush() throws IOException { 
	            currentStream.flush(); 
	        } 
	 
	        protected void doClose() throws IOException { 
	 
	        } 
	 
	        protected void onWrite() throws IOException { 
	 
	        } 
	 
	    } 

}
