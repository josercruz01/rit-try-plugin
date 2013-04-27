package com.plugin.tryplugin.core.models;

public class ServerConfig {
	private String host;
	private String username;
	private String password;
	private int port;
	
	public ServerConfig(String host, String username, String password) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.setPort(3000);
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

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
