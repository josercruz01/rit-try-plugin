package com.plugin.tryplugin.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class TryPluginMainView extends Composite {
	private Text passwordText;
	private Text usernameText;
	private Text text;
	private Text assignmentCodeText;
	private Text instructorAccountText;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TryPluginMainView(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setBounds(10, 94, 64, 64);
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100, -10);
		fd_composite.right = new FormAttachment(0, 751);
		fd_composite.left = new FormAttachment(0, 10);
		composite.setLayoutData(fd_composite);
		composite.setLayout(new TreeColumnLayout());
		
		TreeViewer treeViewer = new TreeViewer(composite, SWT.BORDER);
		Tree localFilesTree = treeViewer.getTree();
		localFilesTree.setHeaderVisible(true);
		localFilesTree.setLinesVisible(true);
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		fd_composite.top = new FormAttachment(0, 212);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.bottom = new FormAttachment(composite, -6);
		fd_lblNewLabel.left = new FormAttachment(0, 10);
		fd_lblNewLabel.right = new FormAttachment(0, 230);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("Files on local machine");
		
		Label lblNewLabel_1 = new Label(this, SWT.NONE);
		FormData fd_lblNewLabel_1 = new FormData();
		fd_lblNewLabel_1.left = new FormAttachment(0, 426);
		lblNewLabel_1.setLayoutData(fd_lblNewLabel_1);
		lblNewLabel_1.setText("Files added");
		
		ListViewer listViewer = new ListViewer(this, SWT.BORDER | SWT.V_SCROLL);
		List addedFilesList = listViewer.getList();
		fd_lblNewLabel_1.bottom = new FormAttachment(addedFilesList, -6);
		FormData fd_addedFilesList = new FormData();
		fd_addedFilesList.bottom = new FormAttachment(composite, -6);
		fd_addedFilesList.left = new FormAttachment(0, 420);
		fd_addedFilesList.right = new FormAttachment(100, -73);
		fd_addedFilesList.top = new FormAttachment(0, 30);
		addedFilesList.setLayoutData(fd_addedFilesList);
		
		Composite composite_1 = new Composite(this, SWT.NONE);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.top = new FormAttachment(addedFilesList, 0, SWT.TOP);
		fd_composite_1.left = new FormAttachment(composite, 0, SWT.LEFT);
		fd_composite_1.right = new FormAttachment(addedFilesList, -6);
		fd_composite_1.bottom = new FormAttachment(lblNewLabel, -4);
		composite_1.setLayoutData(fd_composite_1);
		
		Label lblRemoteHost = new Label(composite_1, SWT.NONE);
		lblRemoteHost.setBounds(10, 14, 102, 14);
		lblRemoteHost.setText("Remote host:");
		
		Label lblUsername = new Label(composite_1, SWT.NONE);
		lblUsername.setText("Username:");
		lblUsername.setBounds(10, 42, 102, 14);
		
		passwordText = new Text(composite_1, SWT.BORDER);
		passwordText.setEchoChar('*');
		passwordText.setBounds(128, 68, 172, 19);
		
		usernameText = new Text(composite_1, SWT.BORDER);
		usernameText.setBounds(128, 40, 172, 19);
		
		text = new Text(composite_1, SWT.BORDER);
		text.setBounds(128, 10, 172, 19);
		
		Label lblPassword = new Label(composite_1, SWT.NONE);
		lblPassword.setText("Password:");
		lblPassword.setBounds(10, 70, 102, 14);
		
		Label lblInstructorAccount = new Label(composite_1, SWT.NONE);
		lblInstructorAccount.setText("Instructor Account:");
		lblInstructorAccount.setBounds(10, 98, 114, 14);
		
		assignmentCodeText = new Text(composite_1, SWT.BORDER);
		assignmentCodeText.setBounds(128, 126, 172, 19);
		
		Label lblAssignmentCode = new Label(composite_1, SWT.NONE);
		lblAssignmentCode.setText("Assignment code:");
		lblAssignmentCode.setBounds(10, 126, 102, 14);
		
		instructorAccountText = new Text(composite_1, SWT.BORDER);
		instructorAccountText.setBounds(128, 97, 172, 19);
		
		Button btnRun = new Button(composite_1, SWT.NONE);
		btnRun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				usernameText.setText("Hello worlds");
			}
		});
		btnRun.setBounds(318, 10, 76, 46);
		btnRun.setText("Run");
		
		Label lblTryProjectConfiguration = new Label(this, SWT.NONE);
		FormData fd_lblTryProjectConfiguration = new FormData();
		fd_lblTryProjectConfiguration.top = new FormAttachment(lblNewLabel_1, 0, SWT.TOP);
		fd_lblTryProjectConfiguration.left = new FormAttachment(0, 10);
		lblTryProjectConfiguration.setLayoutData(fd_lblTryProjectConfiguration);
		lblTryProjectConfiguration.setText("Try project configuration");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
