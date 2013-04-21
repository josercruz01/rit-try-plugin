package com.pluigin.tryplugin.core.app;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.pluigin.tryplugin.core.models.ServerConfig;

public class SSHSessionManager implements ISSHSessionManager{

	@Override
	public ISSHSession createSession(ServerConfig config) throws Exception {
		JSch jsch = new JSch();
		Session session = jsch.getSession(config.getUsername() , config.getUsername());
		session.setPassword(config.getPassword());
		session.connect();
		return new SSHSession(session);
	}

}
