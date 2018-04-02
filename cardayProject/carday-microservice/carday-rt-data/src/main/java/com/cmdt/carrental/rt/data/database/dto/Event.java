package com.cmdt.carrental.rt.data.database.dto;

public class Event
{
    private Integer eventId;
    
    private Boolean eventFlag;
    
    private String eventData;
    
    public Integer getEventId()
    {
        return eventId;
    }
    
    public void setEventId(Integer eventId)
    {
        this.eventId = eventId;
    }
    
    public Boolean getEventFlag()
    {
        return eventFlag;
    }
    
    public void setEventFlag(Boolean eventFlag)
    {
        this.eventFlag = eventFlag;
    }
    
    public String getEventData()
    {
        return eventData;
    }
    
    public void setEventData(String eventData)
    {
        this.eventData = eventData;
    }
}
