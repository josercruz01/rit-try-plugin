package com.pluigin.tryplugin.core.models;

public class ServerConfig {
	private String host;
	private String username;
	private String password;
	
	public ServerConfig(String host, String username, String password) {
		this.host = host;
		this.username = username;
		this.password = password;
	}
	
	public ServerConfig(){}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
