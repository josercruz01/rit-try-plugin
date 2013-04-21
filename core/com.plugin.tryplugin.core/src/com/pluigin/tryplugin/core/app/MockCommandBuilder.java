package com.pluigin.tryplugin.core.app;

public class MockCommandBuilder implements ICommandBuilder {

	@Override
	public String buildFrom(String... commands) {
		return "pwd";
	}

}
