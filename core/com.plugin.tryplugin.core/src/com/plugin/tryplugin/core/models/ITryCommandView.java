package com.plugin.tryplugin.core.models;

public interface ITryCommandView {

	void onError(Exception e);

	void onCommandExecuted(String result, int exitStatus);
	
	boolean promptYesNoRSAKeyFingerprint(String str);

}
