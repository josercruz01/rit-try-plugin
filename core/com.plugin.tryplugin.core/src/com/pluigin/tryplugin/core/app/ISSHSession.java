package com.pluigin.tryplugin.core.app;

import java.util.ArrayList;

public interface ISSHSession {

	String uploadFiles(String parentFolder, ArrayList<String> filenames);

	void execute(ITryCommandView view, String command);

	void disconnect();

}
