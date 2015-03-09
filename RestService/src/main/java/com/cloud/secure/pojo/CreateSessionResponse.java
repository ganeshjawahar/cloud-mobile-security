package com.cloud.secure.pojo;

public class CreateSessionResponse extends GenericResponse {
	String sessionId;
	
	public CreateSessionResponse(String code,String message,String sessionId){
		super(code,message);
		this.sessionId=sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
