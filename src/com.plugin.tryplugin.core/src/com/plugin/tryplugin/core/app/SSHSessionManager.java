package com.plugin.tryplugin.core.app;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.plugin.tryplugin.core.models.ITryCommandView;
import com.plugin.tryplugin.core.models.ServerConfig;
import com.plugin.tryplugin.core.models.UserInfoWrapper;

public class SSHSessionManager implements ISSHSessionManager{

	@Override
	public ISSHSession createSession(ITryCommandView view,ServerConfig config) throws Exception {
		JSch jsch = new JSch();
		Session session = jsch.getSession(config.getUsername(),config.getHost());
		session.setUserInfo(new UserInfoWrapper(view));
		session.setPassword(config.getPassword());
		session.connect(config.getPort());
		SSHSession sshSession =new SSHSession(session);
		sshSession.setView(view);
		return sshSession;
	}
}
