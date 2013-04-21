package com.pluigin.tryplugin.core.app;

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

public class UserInfoWrapper implements UserInfo,UIKeyboardInteractive{
	private ITryCommandView tryCommandView;
	public UserInfoWrapper(ITryCommandView tryCommandView){
		this.tryCommandView = tryCommandView;
	}
	
	public String getPassword(){ return null; }
	public boolean promptYesNo(String str){ return tryCommandView.promptYesNoRSAKeyFingerprint(str); }
	public String getPassphrase(){ return null; }
	public boolean promptPassphrase(String message){ return false; }
	public boolean promptPassword(String message){ return false; }
	public void showMessage(String message){ }
	public String[] promptKeyboardInteractive(String destination,
			String name,
			String instruction,
			String[] prompt,
			boolean[] echo){
		return null;
	}
}

