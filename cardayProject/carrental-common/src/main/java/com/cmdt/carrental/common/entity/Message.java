package com.cmdt.carrental.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id; 				//编号
    private MessageType type;		//消息类型  系统消息，任务消息，行程消息，异常报警
    private String carNo;			//车牌号
    private Date time;				//消息时间
    private Long orgId;				//所属部门
    private String location;		//异常位置
    private Integer isEnd;			//报警是否解除
    private Integer isNew; 			//0:已读,1:未读
    private Long warningId;		    //报警Id号    	
    private String msg;				//消息内容
    private Long orderId;			//订单编号
    private String title;			//消息标题
   
	public Message() {
    }
    public static enum MessageType {
    	SYSTEM("系统消息"),OVERSPEED("超速报警"),VEHICLEBACK("回车报警"),OUTBOUND("越界报警"),VIOLATE("违规报警"),TASK("任务消息"),TRAVEL("行程消息"),MAINTAIN("维保消息");
        private final String info;
        private MessageType(String info) {
            this.info = info;
        }
        public String getInfo() {
            return info;
        }
    }
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(Integer isEnd) {
		this.isEnd = isEnd;
	}

	public Integer getIsNew() {
		return isNew;
	}

	public void setIsNew(Integer isNew) {
		this.isNew = isNew;
	}

	public Long getWarningId() {
		return warningId;
	}

	public void setWarningId(Long warningId) {
		this.warningId = warningId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
