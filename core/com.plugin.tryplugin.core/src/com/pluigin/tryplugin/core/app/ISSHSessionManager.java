package com.pluigin.tryplugin.core.app;

import com.pluigin.tryplugin.core.models.ServerConfig;

public interface ISSHSessionManager {
	ISSHSession createSession(ServerConfig config) throws Exception;
}
