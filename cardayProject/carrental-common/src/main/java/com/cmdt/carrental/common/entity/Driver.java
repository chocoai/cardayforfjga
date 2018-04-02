package com.cmdt.carrental.common.entity;

import java.io.Serializable;
import java.sql.Date;

public class Driver implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private Long id; // 编号
    
    private String sex;// 司机性别 0:男 1:女
    
    private Date birthday;// 司机生日 1983-12-16
    
    private Integer age;//司机年龄
    
    private String licenseType;// 驾照类型 C1 or C2
    
    private String licenseNumber;// 驾照号码
    
    private Date licenseBegintime;// 初次领证时间 2015-09-12
    
    private Date licenseExpiretime;// 驾照到期时间 2011-09-12
    
    private Integer drivingYears;// 驾龄
    
    private String licenseAttach;// 附件保存路径
    
    private Long depId;// 所属部门
    
    private String depName;// 所属部门名称
    
    private Long stationId;// 所属站点
    
    private String stationName;// 所属站点名称
    
    private Long vid;
    
    private Long rentNum;// 被租赁的次数
    
    private Double creditRating;// 信用级别
    
    private Integer drvStatus;// 司机状态
    
    private Integer tripQuantity; 
    
    private Long tripMileage;

    /** 驾驶员基本工资 */
    private Integer salary;
    
    public Driver()
    {
    }
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getSex()
    {
        return sex;
    }
    
    public void setSex(String sex)
    {
        this.sex = sex;
    }
    
    public Date getBirthday()
    {
        return birthday;
    }
    
    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }
    
    public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getLicenseType()
    {
        return licenseType;
    }
    
    public void setLicenseType(String licenseType)
    {
        this.licenseType = licenseType;
    }
    
    public String getLicenseNumber()
    {
        return licenseNumber;
    }
    
    public void setLicenseNumber(String licenseNumber)
    {
        this.licenseNumber = licenseNumber;
    }
    
    public Date getLicenseBegintime()
    {
        return licenseBegintime;
    }
    
    public void setLicenseBegintime(Date licenseBegintime)
    {
        this.licenseBegintime = licenseBegintime;
    }
    
    public Date getLicenseExpiretime()
    {
        return licenseExpiretime;
    }
    
    public void setLicenseExpiretime(Date licenseExpiretime)
    {
        this.licenseExpiretime = licenseExpiretime;
    }
    
    public Integer getDrivingYears()
    {
        return drivingYears;
    }
    
    public void setDrivingYears(Integer drivingYears)
    {
        this.drivingYears = drivingYears;
    }
    
    public String getLicenseAttach()
    {
        return licenseAttach;
    }
    
    public void setLicenseAttach(String licenseAttach)
    {
        this.licenseAttach = licenseAttach;
    }
    
    public Long getDepId()
    {
        return depId;
    }
    
    public void setDepId(Long depId)
    {
        this.depId = depId;
    }
    
    public String getDepName()
    {
        return depName;
    }
    
    public void setDepName(String depName)
    {
        this.depName = depName;
    }
    
    public Long getStationId()
    {
        return stationId;
    }
    
    public void setStationId(Long stationId)
    {
        this.stationId = stationId;
    }
    
    public String getStationName()
    {
        return stationName;
    }
    
    public void setStationName(String stationName)
    {
        this.stationName = stationName;
    }
    
    public Long getVid()
    {
        return vid;
    }
    
    public void setVid(Long vid)
    {
        this.vid = vid;
    }
    
    public Long getRentNum()
    {
        return rentNum;
    }
    
    public void setRentNum(Long rentNum)
    {
        this.rentNum = rentNum;
    }
    
    public Double getCreditRating()
    {
        return creditRating;
    }
    
    public void setCreditRating(Double creditRating)
    {
        this.creditRating = creditRating;
    }

    public Integer getDrvStatus()
    {
        return drvStatus;
    }

    public void setDrvStatus(Integer drvStatus)
    {
        this.drvStatus = drvStatus;
    }

	public Integer getTripQuantity() {
		return tripQuantity;
	}

	public void setTripQuantity(Integer tripQuantity) {
		this.tripQuantity = tripQuantity;
	}

	public Long getTripMileage() {
		return tripMileage;
	}

	public void setTripMileage(Long tripMileage) {
		this.tripMileage = tripMileage;
	}

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }
}
