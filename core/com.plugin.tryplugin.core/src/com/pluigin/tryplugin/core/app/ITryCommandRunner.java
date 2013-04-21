package com.pluigin.tryplugin.core.app;

import com.pluigin.tryplugin.core.models.ITryCommandView;
import com.pluigin.tryplugin.core.models.ServerConfig;
import com.pluigin.tryplugin.core.models.TryProject;

public interface ITryCommandRunner {
	void setView(ITryCommandView view);
	void run(ServerConfig config, TryProject project);
}
