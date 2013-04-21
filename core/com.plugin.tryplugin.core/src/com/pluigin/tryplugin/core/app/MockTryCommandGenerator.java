package com.pluigin.tryplugin.core.app;

import com.pluigin.tryplugin.core.models.TryProject;

public class MockTryCommandGenerator implements ITryCommandGenerator {

	@Override
	public String create(TryProject project) {
		return "ls -l";
	}

}
