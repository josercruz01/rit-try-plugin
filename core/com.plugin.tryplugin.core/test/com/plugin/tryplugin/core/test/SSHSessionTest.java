package com.plugin.tryplugin.core.test;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jcraft.jsch.Session;
import com.pluigin.tryplugin.core.app.ISSHSession;
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
		// Establish context
		
		// when 
		
		session.disconnect();
		
		//then
		verify(mockSession).disconnect();
	}
	
	@Test
	public void testExecute(){
		// Establish context
	}

}
