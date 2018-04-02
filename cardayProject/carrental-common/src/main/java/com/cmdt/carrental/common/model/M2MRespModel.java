package com.cmdt.carrental.common.model;

public class M2MRespModel {
	
	//{"REASON":"","OPER_RESULT":"0","MSISDN":"1064826718524"}
	private String MSISDN;
	private String OPER_RESULT;
	private String REASON;
	
    public String getMSISDN()
    {
        return MSISDN;
    }
    public void setMSISDN(String mSISDN)
    {
        MSISDN = mSISDN;
    }
    public String getOPER_RESULT()
    {
        return OPER_RESULT;
    }
    public void setOPER_RESULT(String oPER_RESULT)
    {
        OPER_RESULT = oPER_RESULT;
    }
    public String getREASON()
    {
        return REASON;
    }
    public void setREASON(String rEASON)
    {
        REASON = rEASON;
    }


}
