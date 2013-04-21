package com.pluigin.tryplugin.core.app;

import java.io.InputStream;

public interface ITryCommandView {

	void onCommandExecuted(InputStream inputStream);

	void onError(Exception e);

}
