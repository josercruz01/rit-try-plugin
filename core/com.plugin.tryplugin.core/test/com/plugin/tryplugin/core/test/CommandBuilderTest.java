package com.plugin.tryplugin.core.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.pluigin.tryplugin.core.app.CommandBuilder;

public class CommandBuilderTest {

	private CommandBuilder commandBuilder;

	@Before
	public void setUp() throws Exception {
		commandBuilder = new CommandBuilder();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBuildSingleCommand() {
		// because
		String result = commandBuilder.buildFrom("cd ..");
		
		assertEquals("Should build the command without semicolon at the end",result,"cd ..");
	}
	
	@Test
	public void testBuildNoCommand() {
		// because
		String result = commandBuilder.buildFrom("");
		
		assertEquals("Should return empty string",result,"");
	}
	
	@Test
	public void testBuildListOfCommands() {
		// because
		String result = commandBuilder.buildFrom("ls","pwd","uname");
		
		assertEquals("Should concat the input commands using semicolon",result,"ls;pwd;uname");
	}
	
	@Test
	public void testBuildNulllist() {
		// because
		String result = commandBuilder.buildFrom();
		
		assertEquals("Should return empty string",result,"");
	}
	
}
