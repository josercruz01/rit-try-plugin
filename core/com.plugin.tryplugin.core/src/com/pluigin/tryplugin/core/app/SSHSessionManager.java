package com.pluigin.tryplugin.core.app;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.pluigin.tryplugin.core.models.ServerConfig;

public class SSHSessionManager implements ISSHSessionManager{

	@Override
	public ISSHSession createSession(ITryCommandView view,ServerConfig config) throws Exception {
		JSch jsch = new JSch();
		Session session = jsch.getSession(config.getUsername(),config.getHost());
		session.setUserInfo(new UserInfoWrapper(view));
		session.setPassword(config.getPassword());
		session.connect(config.getPort());
		return new SSHSession(session);
	}
}
