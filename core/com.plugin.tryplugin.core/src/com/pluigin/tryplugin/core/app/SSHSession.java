package com.pluigin.tryplugin.core.app;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.pluigin.tryplugin.core.models.ITryCommandView;

public class SSHSession implements ISSHSession {
	private Session session;
	private ITryCommandView view;

	public SSHSession(Session session){
		this.session = session;
	}

	@Override
	public String uploadFiles(ArrayList<String> filenames) throws SftpException, JSchException {
		Channel channel =null;
		String tmpFolder = "rit-try-plugin_" + TimeService.textTimeStamp();
		try{

			channel = session.openChannel("sftp");
			channel.connect();

			ChannelSftp csftp = (ChannelSftp) channel;
			try{
				csftp.mkdir(tmpFolder);
			}catch(Exception e){}

			for(String sourceFile: filenames){
				File file = new File(sourceFile); 
				String destAbsolutePath =  tmpFolder + "/" + file.getName();
				csftp.put(sourceFile, destAbsolutePath, (int)file.length());
			}

		}finally{
			if(channel!=null)channel.disconnect();
		}
		return tmpFolder;
	}

	@Override
	public void execute(String command) {
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
					getView().onCommandExecuted(result,channel.getExitStatus());
					break;
				}
			}
		}catch(Exception e){
			getView().onError(e);
		} finally{
			if(channel!=null) channel.disconnect();
		}
	}

	@Override
	public void disconnect() {
		session.disconnect();
	}

	public ITryCommandView getView() {
		if(view == null) throw new IllegalArgumentException("View attribute can not be null");
		return view;
	}

	public void setView(ITryCommandView view) {
		this.view = view;
	}
}