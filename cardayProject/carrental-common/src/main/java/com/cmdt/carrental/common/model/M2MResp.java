package com.cmdt.carrental.common.model;

import java.util.List;

public class M2MResp {
    
	//{"message":"正确","result":[{"REASON":"","OPER_RESULT":"0","MSISDN":"1064826718524"}],"status":"0"}
	
    private String status;
	private String message;
	private List<M2MRespModel> result;
	
	private String token;
	private String appid;
	private String transid;
	private String ebid;
    public String getStatus()
    {
        return status;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }
    public String getMessage()
    {
        return message;
    }
    public void setMessage(String message)
    {
        this.message = message;
    }
    public List<M2MRespModel> getResult()
    {
        return result;
    }
    public void setResult(List<M2MRespModel> result)
    {
        this.result = result;
    }
    public String getToken()
    {
        return token;
    }
    public void setToken(String token)
    {
        this.token = token;
    }
    public String getAppid()
    {
        return appid;
    }
    public void setAppid(String appid)
    {
        this.appid = appid;
    }
    public String getTransid()
    {
        return transid;
    }
    public void setTransid(String transid)
    {
        this.transid = transid;
    }
    public String getEbid()
    {
        return ebid;
    }
    public void setEbid(String ebid)
    {
        this.ebid = ebid;
    }
}
