package com.pluigin.tryplugin.core.app;

import java.util.ArrayList;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.pluigin.tryplugin.core.models.ITryCommandView;

public interface ISSHSession {

	String uploadFiles(ArrayList<String> filenames) throws SftpException, JSchException;

	void execute(String command);

	void disconnect();

	void setView(ITryCommandView view);
}
