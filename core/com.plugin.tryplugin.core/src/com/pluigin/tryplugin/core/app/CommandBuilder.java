package com.pluigin.tryplugin.core.app;

public class CommandBuilder implements ICommandBuilder {

	@Override
	public String buildFrom(String... commands) {
		if(commands == null) commands =new String[0];
		
		String result = "";
		boolean isFirstCommand = true;
		for(String cmd : commands){
			if(!isFirstCommand) result+=";";
			result+=cmd;
			isFirstCommand = false;
		}
		
		return result;
	}

}
