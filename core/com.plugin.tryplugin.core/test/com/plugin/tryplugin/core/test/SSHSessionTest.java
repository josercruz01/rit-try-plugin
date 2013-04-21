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
import com.pluigin.tryplugin.core.app.ITryCommandView;
import com.pluigin.tryplugin.core.app.SSHSession;

public class SSHSessionTest {
	ISSHSession session;

	Session mockSession;
	@Before
	public void setUp() throws Exception {
		mockSession = mock(Session.class);
		
		
		session = new SSHSession(mockSession);
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
		ITryCommandView mockView  = mock(ITryCommandView.class);
		
		when(mockChannel.isClosed()).thenReturn(true);
		when(mockChannel.getExitStatus()).thenReturn(200);
		
		
		// when
		session.execute(mockView,"pwd");
		
		//then
		verify(mockChannel).setErrStream(System.err);
		verify(mockView).onCommandExecuted("Blah Result OK!!", 200);
		verify(mockChannel).connect();
		verify(mockChannel).disconnect();
	}
	
	@Test
	public void testExecute_whenOpenChannelFails() throws JSchException, IOException{
		// establish context
		ChannelExec mockChannel = mock(ChannelExec.class);
		when(mockSession.openChannel("exec")).thenThrow(Exception.class);
		ITryCommandView mockView  = mock(ITryCommandView.class);
		
		// when
		session.execute(mockView,"pwd");
		
		//then
		verify(mockView).onError((Exception)any());
	}
	
	
	public static class MockInputStream {
		public static InputStream create(String text) {
			InputStream inputStream = new ByteArrayInputStream(text.getBytes());
			return inputStream;
		}
	}
		
}
