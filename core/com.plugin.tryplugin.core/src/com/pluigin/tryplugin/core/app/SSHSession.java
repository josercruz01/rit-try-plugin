package com.pluigin.tryplugin.core.app;

import com.pluigin.tryplugin.core.app.ISSHSession;

import java.io.InputStream;
import java.util.ArrayList;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
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
	public InputStream execute(ITryCommandView view,String command) {
		Channel channel = null;
		try{
			channel=session.openChannel("exec");
			((ChannelExec)channel).setCommand(command);

			channel.setInputStream(null);
			((ChannelExec)channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();

			channel.connect();

			String result = "";
			byte[] tmp=new byte[1024];
			while(true){
				while(in.available()>0){
					int i=in.read(tmp, 0, 1024);
					if(i<0)break;
					result += new String(tmp, 0, i);
				}
				if(channel.isClosed()){
					view.onCommandExecuted(result,channel.getExitStatus());
					break;
				}
			}
		}catch(Exception e){
			view.onError(e);
		} finally{
			if(channel!=null) channel.disconnect();
		}


		return null;
	}

	@Override
	public void disconnect() {
		session.disconnect();
	}

}
