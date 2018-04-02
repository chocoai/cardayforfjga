package com.cmdt.carrental.common.entity;

import java.io.Serializable;

public class Sessions implements Serializable {
	private static final long serialVersionUID = 1218405820832443222L;
	private String id; //Shiro session id
    private String session; //Shiro session
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

		Sessions sessions = (Sessions) o;

        if (id != null ? !id.equals(sessions.id) : sessions.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
