package com.plugin.tryplugin.core.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.junit.After;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.VarargMatcher;

import com.pluigin.tryplugin.core.app.ISSHSession;
import com.pluigin.tryplugin.core.app.ISSHSessionManager;
import com.pluigin.tryplugin.core.app.ITryCommandGenerator;
import com.pluigin.tryplugin.core.app.ITryCommandRunner;
import com.pluigin.tryplugin.core.app.ITryCommandView;
import com.pluigin.tryplugin.core.app.TryCommandRunner;
import com.pluigin.tryplugin.core.models.ServerConfig;
import com.pluigin.tryplugin.core.models.TryProject;
import static org.mockito.Mockito.*;

public class TryCommandRunnerTest {
	ITryCommandRunner commandRunner;
	
	ISSHSessionManager sessionManager;
	ITryCommandGenerator tryCommandGenerator;
	
	ServerConfig config;
	TryProject project;

	private ITryCommandView view;

	@Before
	public void setUp() throws Exception {
		// configuration
		ArrayList<String> filenames = new ArrayList<String>();
		filenames.add("main.cpp");
		this.project = new TryProject("instructorAccount", "assignmentCode",filenames);
		this.config = new ServerConfig("host","username","password");
		
		// mock files
		this.sessionManager = mock(ISSHSessionManager.class);
		this.tryCommandGenerator = mock(ITryCommandGenerator.class);
		this.view = mock(ITryCommandView.class);
		
		// System under test
		this.commandRunner = new TryCommandRunner(sessionManager,tryCommandGenerator);
		this.commandRunner.setView(view);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRun() {
		// Establish Context
		InputStream inputStream = MockInputStream.create();
		ISSHSession session = mock(ISSHSession.class);
		try {
			when(sessionManager.createSession(config)).thenReturn(session);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		when(session.uploadFiles("~",project.getFilenames())).thenReturn("blahFolderName");
		
		when(session.execute((String)anyVararg())).thenReturn(inputStream);
		
		// because
		commandRunner.run(config, project);
		
		
		// then
		verify(view).onCommandExecuted(inputStream);
		verify(session).disconnect();
		verify(view,never()).onError((Exception)any());
	}
	
	@Test
	public void testRun_whenFailureToConnect() throws Exception {
		// Establish Context
		ISSHSession session = mock(ISSHSession.class);
		when(sessionManager.createSession(config)).thenThrow(Exception.class);
		
		// because
		commandRunner.run(config, project);
		
		// then
		verify(view).onError((Exception)any());
		verify(session,never()).disconnect();
	}
	
	@Test
	public void testRun_whenFailureToUploadFile() throws Exception {
		// Establish Context
		ISSHSession session = mock(ISSHSession.class);
		when(sessionManager.createSession(config)).thenReturn(session);
		
		when(session.uploadFiles("~",project.getFilenames())).thenThrow(Exception.class);
		
		// because
		commandRunner.run(config, project);
		
		// then
		verify(view).onError((Exception)any());
		verify(session).disconnect();
	}
	
	@Test
	public void testRun_whenFailureToExecute() throws Exception {
		// Establish Context
		ISSHSession session = mock(ISSHSession.class);
		when(sessionManager.createSession(config)).thenReturn(session);
		
		when(session.uploadFiles("~",project.getFilenames())).thenReturn("blah");
		when(session.execute((String)anyVararg())).thenThrow(Exception.class);;
		
		// because
		commandRunner.run(config, project);
		
		// then
		verify(view).onError((Exception)any());
		verify(session).disconnect();
	}
	
	public static class MockInputStream {
		public static InputStream create() {
			
		InputStream inputStream = new InputStream() {
			@Override
			public int read() throws IOException {
				return 0;
			}
		};
		return inputStream;
		}
		
	}
	
}
