package com.plugin.tryplugin.core.app;

import com.plugin.tryplugin.core.models.ITryCommandView;
import com.plugin.tryplugin.core.models.ServerConfig;

public interface ISSHSessionManager {
	ISSHSession createSession(ITryCommandView view,ServerConfig config) throws Exception;
}
