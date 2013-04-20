package com.plugin.tryplugin.core.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.pluigin.tryplugin.core.app.ITryCommandRunner;
import com.pluigin.tryplugin.core.app.TryCommandRunner;
import com.pluigin.tryplugin.core.models.ServerConfig;
import com.pluigin.tryplugin.core.models.TryProject;

public class TryCommandRunnerTest {
	ITryCommandRunner commandRunner;
	
	ServerConfig config;
	TryProject project;

	@Before
	public void setUp() throws Exception {
		this.commandRunner = new TryCommandRunner();
		
		this.config = new ServerConfig("host","username","password");
		this.project = new TryProject("instructorAccount", "assignmentCode");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRun() {
		// when run is invoked
		commandRunner.run(config, project);
		
		mokitoooo
	}

}
