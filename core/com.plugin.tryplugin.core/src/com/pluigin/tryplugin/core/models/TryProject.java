package com.pluigin.tryplugin.core.models;

import java.util.ArrayList;

public class TryProject {
	private String instructorAccount;
	private String assignmentCode;
	private ArrayList<String> filenames;
	
	public TryProject(String instructorAccount,String assignmentCode)
	{
		this.instructorAccount = instructorAccount;
		this.assignmentCode = assignmentCode;
	}
	
	public String getInstructorAccount() {
		return instructorAccount;
	}
	public void setInstructorAccount(String instructorAccount) {
		this.instructorAccount = instructorAccount;
	}
	public String getAssignmentCode() {
		return assignmentCode;
	}
	public void setAssignmentCode(String assignmentCode) {
		this.assignmentCode = assignmentCode;
	}

	public ArrayList<String> getFilenames() {
		return filenames;
	}

	public void setFilenames(ArrayList<String> filenames) {
		this.filenames = filenames;
	}	

}
