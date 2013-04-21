package com.plugin.tryplugin.core.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.junit.After;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.VarargMatcher;

import com.pluigin.tryplugin.core.app.ICommandBuilder;
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
	
	ISSHSessionManager mockSessionManager;
	ITryCommandGenerator mockTryCommandGenerator;
	ICommandBuilder mockCommandBuilder;
	
	ServerConfig config;
	TryProject project;

	private ITryCommandView mockView;

	@Before
	public void setUp() throws Exception {
		// configuration
		ArrayList<String> filenames = new ArrayList<String>();
		filenames.add("main.cpp");
		project = new TryProject("instructorAccount", "assignmentCode",filenames);
		config = new ServerConfig("host","username","password");
		
		// mock files
		mockSessionManager = mock(ISSHSessionManager.class);
		mockTryCommandGenerator = mock(ITryCommandGenerator.class);
		mockView = mock(ITryCommandView.class);
		mockCommandBuilder = mock(ICommandBuilder.class);
		
		// System under test
		this.commandRunner = new TryCommandRunner(mockSessionManager,mockTryCommandGenerator,mockCommandBuilder);
		this.commandRunner.setView(mockView);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRun() {
		// Establish Context
	
		ISSHSession mockSession = mock(ISSHSession.class);
		try {
			when(mockSessionManager.createSession(config)).thenReturn(mockSession);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		when(mockSession.uploadFiles("~",project.getFilenames())).thenReturn("blahFolderName");
		when(mockCommandBuilder.buildFrom((String)anyVararg())).thenReturn("pwd");
		
		// because
		commandRunner.run(config, project);
		
		
		// then
		verify(mockSession.execute(mockView, "pwd"));
		verify(mockSession).disconnect();
		verify(mockView,never()).onError((Exception)any());
	}
	
	@Test
	public void testRun_whenFailureToConnect() throws Exception {
		// Establish Context
		ISSHSession session = mock(ISSHSession.class);
		when(mockSessionManager.createSession(config)).thenThrow(Exception.class);
		
		// because
		commandRunner.run(config, project);
		
		// then
		verify(mockView).onError((Exception)any());
		verify(session,never()).disconnect();
	}
	
	@Test
	public void testRun_whenFailureToUploadFile() throws Exception {
		// Establish Context
		ISSHSession session = mock(ISSHSession.class);
		when(mockSessionManager.createSession(config)).thenReturn(session);
		
		when(session.uploadFiles("~",project.getFilenames())).thenThrow(Exception.class);
		
		// because
		commandRunner.run(config, project);
		
		// then
		verify(mockView).onError((Exception)any());
		verify(session).disconnect();
	}
	
	@Test
	public void testRun_whenFailureToExecute() throws Exception {
		// Establish Context
		ISSHSession session = mock(ISSHSession.class);
		when(mockSessionManager.createSession(config)).thenReturn(session);
		
		when(session.uploadFiles("~",project.getFilenames())).thenReturn("blah");
		when(mockCommandBuilder.buildFrom((String)anyVararg())).thenReturn("pwd");
		when(session.execute(mockView,"pwd")).thenThrow(Exception.class);
		
		// because
		commandRunner.run(config, project);
		
		// then
		verify(mockView).onError((Exception)any());
		verify(session).disconnect();
	}
}
