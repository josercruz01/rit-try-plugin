package com.plugin.tryplugin.core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.pluigin.tryplugin.core.app.TryCommandGenerator;
import com.pluigin.tryplugin.core.models.TryProject;

public class TryCommandGeneratorTest {

	private TryCommandGenerator tryCommandGenerator;

	@Before
	public void setUp() throws Exception {
		tryCommandGenerator = new TryCommandGenerator();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		// establish context
		ArrayList<String> filenames = new ArrayList<String>();
		filenames.add("main.h");
		filenames.add("main.cpp") ;
		TryProject project = new TryProject("instructor","homework",filenames);
		
		// because
		String result = tryCommandGenerator.create(project);
		
		// it should
		assertEquals("It should return the correct try command",result,"try instructor homework main.h main.cpp");
		
	}

}
