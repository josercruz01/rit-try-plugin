package com.pluigin.tryplugin.core.app;

import java.io.InputStream;
import java.util.ArrayList;

import com.jcraft.jsch.JSchException;

public interface ISSHSession {

	String uploadFiles(String parentFolder, ArrayList<String> filenames);

	InputStream execute(ITryCommandView view, String command);

	void disconnect();

}
