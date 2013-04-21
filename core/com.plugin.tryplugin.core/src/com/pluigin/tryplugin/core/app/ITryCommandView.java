package com.pluigin.tryplugin.core.app;
import java.io.InputStream;

public interface ITryCommandView {

	void onError(Exception e);

	void onCommandExecuted(String result, int exitStatus);
	
	boolean promptYesNoRSAKeyFingerprint(String str);

}
