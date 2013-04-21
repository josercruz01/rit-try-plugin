package com.pluigin.tryplugin.core.app;

import java.io.InputStream;
import java.util.ArrayList;

import com.jcraft.jsch.Session;

public class SSHSession implements ISSHSession {
	private Session session;

	public SSHSession(Session session){
		this.session = session;
		
	}

	@Override
	public String uploadFiles(String parentFolder, ArrayList<String> filenames) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream execute(String... string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disconnect() {
		session.disconnect();
	}

}
