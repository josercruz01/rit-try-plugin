package com.plugin.tryplugin.core.app;

import com.plugin.tryplugin.core.models.ITryCommandView;
import com.plugin.tryplugin.core.models.ServerConfig;
import com.plugin.tryplugin.core.models.TryProject;

public interface ITryCommandRunner {
	void setView(ITryCommandView view);
	void run(ServerConfig config, TryProject project);
}
