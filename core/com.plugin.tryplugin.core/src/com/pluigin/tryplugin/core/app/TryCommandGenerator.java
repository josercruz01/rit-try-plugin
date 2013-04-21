package com.pluigin.tryplugin.core.app;

import java.io.File;

import com.pluigin.tryplugin.core.models.TryProject;

public class TryCommandGenerator implements ITryCommandGenerator {

	@Override
	public String create(TryProject project) {
		String filenames = "";
		for(String filename: project.getFilenames()) {
			File file = new File(filename);
			filenames+=file.getName() + " ";
		}
		return String.format("try %s %s %s", project.getInstructorAccount(),project.getAssignmentCode(),filenames.trim());
	}

}
