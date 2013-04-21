package com.plugin.tryplugin.core.test;

import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.pluigin.tryplugin.core.app.ISSHSession;
import com.pluigin.tryplugin.core.app.SSHSession;
import com.pluigin.tryplugin.core.models.ITryCommandView;

public class SSHSessionTest {
	ISSHSession session;

	Session mockSession;

	private ITryCommandView mockView;
	@Before
	public void setUp() throws Exception {
		mockSession = mock(Session.class);
		
		
		mockView  = mock(ITryCommandView.class);
		session = new SSHSession(mockSession);
		session.setView(mockView);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDisconnect() {
		// establish context
		
		// when 
		
		session.disconnect();
		
		//then
		verify(mockSession).disconnect();
	}
	
	@Test
	public void testExecute() throws JSchException, IOException{
		// establish context
		ChannelExec mockChannel = mock(ChannelExec.class);
		InputStream mockInputStream = MockInputStream.create("Blah Result OK!!");
		
		when(mockSession.openChannel("exec")).thenReturn(mockChannel);
		when(mockChannel.getInputStream()).thenReturn(mockInputStream);
		
		when(mockChannel.isClosed()).thenReturn(true);
		when(mockChannel.getExitStatus()).thenReturn(200);
		
		// when
		session.execute("pwd");
		
		//then
		verify(mockChannel).setErrStream(System.err);
		verify(mockView).onCommandExecuted("Blah Result OK!!", 200);
		verify(mockChannel).connect();
		verify(mockChannel).disconnect();
	}
	
	public static class MockInputStream {
		public static InputStream create(String text) {
			InputStream inputStream = new ByteArrayInputStream(text.getBytes());
			return inputStream;
		}
	}
		
}
