package com.plugin.tryplugin.core.app;

import com.plugin.tryplugin.core.models.TryProject;

public interface ITryCommandGenerator {

	String create(TryProject project);

}
