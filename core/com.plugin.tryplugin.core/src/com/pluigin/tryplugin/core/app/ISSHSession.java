package com.pluigin.tryplugin.core.app;

import java.io.InputStream;
import java.util.ArrayList;

public interface ISSHSession {

	String uploadFiles(String parentFolder, ArrayList<String> filenames);

	InputStream execute(String... string);

	void disconnect();

}
