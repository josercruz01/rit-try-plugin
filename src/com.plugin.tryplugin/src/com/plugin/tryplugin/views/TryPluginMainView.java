package com.plugin.tryplugin.views;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.action.Action;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

import com.plugin.tryplugin.views.SampleView.FileModel;
import com.plugin.tryplugin.views.SampleView.TreeObject;
import com.plugin.tryplugin.views.SampleView.TreeParent;

@SuppressWarnings(value = { "all" })
public class TryPluginMainView extends ViewPart {
	
	public static final String ID = "com.plugin.tryplugin.views.TryPluginMainView";
	/*
	private TreeViewer treeViewer;
	private DrillDownAdapter drillDownAdapter;
	
	private ScrolledForm form;
	
	private Action action1;
	private Action action2;
	
	private Action doubleClickAction;
	private Action doubleClickAction2;
	
	private TableViewer table;
	private ArrayList<FileModel> tryElement = new ArrayList<FileModel>();
	
	*/
	
	private Text passwordText;
	private Text usernameText;
	private Text text;
	private Text assignmentCodeText;
	private Text instructorAccountText;
	private TreeViewer treeViewer;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TryPluginMainView(Composite parent, int style) {		
		
		

	}
	class TreeObject implements IAdaptable {
		private String name;
		private TreeParent parent;
		
		
		public TreeObject(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setParent(TreeParent parent) {
			this.parent = parent;
		}
		public TreeParent getParent() {
			return parent;
		}
		public String toString() {
			return getName();
		}
		public Object getAdapter(Class key) {
			return null;
		}
	}
	
	class TreeParent extends TreeObject {
		private ArrayList children;
		public TreeParent(String name) {
			super(name);
			children = new ArrayList();
		}
		public void addChild(TreeObject child) {
			children.add(child);
			child.setParent(this);
		}
		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}
		public TreeObject [] getChildren() {
			return (TreeObject [])children.toArray(new TreeObject[children.size()]);
		}
		public boolean hasChildren() {
			return children.size()>0;
		}
	}

	class ViewContentProvider implements IStructuredContentProvider, 
										   ITreeContentProvider {
		private TreeParent invisibleRoot;
		
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			return (File[]) parent;			
		}
		public Object getParent(Object child) {
			return ((File) child).getParentFile();			
		}
		public Object [] getChildren(Object parent) {
			File file = (File)parent;
			return file.listFiles();			
		}
		public boolean hasChildren(Object parent) {
			File file = (File) parent;
			if(file.isDirectory())
				return true;
			return false;
		}
	}
	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			//return obj.toString();
			File file = (File) obj;
			String name = file.getName();
			if(name.length()>0)
				return name;
			return file.getPath();
		}
		public Image getImage(Object obj) {
			String imageKey = ISharedImages.IMG_OBJ_FILE;//IMG_OBJ_FILE;
			File tmp = (File)obj;
			//if (obj instanceof TreeParent)
			
			if(tmp.isDirectory())
				imageKey = ISharedImages.IMG_OBJ_FOLDER;//IMG_OBJ_FOLDER;
			   				 
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
	}
	class NameSorter extends ViewerSorter {
	}
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		
		Composite composite = toolkit.createComposite(parent, SWT.NONE);
	    
		//composite.setBounds(10, 94, 64, 64);
		
		
		composite.setBounds(10, 94, 64, 64);
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100, -10);
		fd_composite.right = new FormAttachment(0, 751);
		fd_composite.left = new FormAttachment(0, 10);
		composite.setLayoutData(fd_composite);
		composite.setLayout(new TreeColumnLayout());
		
		treeViewer = new TreeViewer(composite, SWT.BORDER);
		Tree localFilesTree = treeViewer.getTree();
		localFilesTree.setHeaderVisible(true);
		localFilesTree.setLinesVisible(true);
		
		
		
		Label lblNewLabel = toolkit.createLabel(parent, "Files on local machine");// new Label(this, SWT.NONE);
		fd_composite.top = new FormAttachment(0, 212);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.bottom = new FormAttachment(composite, -6);
		fd_lblNewLabel.left = new FormAttachment(0, 10);
		fd_lblNewLabel.right = new FormAttachment(0, 230);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		//lblNewLabel.setText("Files on local machine");
		
		Label lblNewLabel_1 = toolkit.createLabel(parent, "");//new Label(this, SWT.NONE);
		FormData fd_lblNewLabel_1 = new FormData();
		fd_lblNewLabel_1.left = new FormAttachment(0, 426);
		lblNewLabel_1.setLayoutData(fd_lblNewLabel_1);
		lblNewLabel_1.setText("Files added");
		
		
		ListViewer listViewer = new ListViewer(parent, SWT.BORDER | SWT.V_SCROLL);
		List addedFilesList = listViewer.getList();
		fd_lblNewLabel_1.bottom = new FormAttachment(addedFilesList, -6);
		FormData fd_addedFilesList = new FormData();
		fd_addedFilesList.bottom = new FormAttachment(composite, -6);
		fd_addedFilesList.left = new FormAttachment(0, 420);
		fd_addedFilesList.right = new FormAttachment(100, -73);
		fd_addedFilesList.top = new FormAttachment(0, 30);
		addedFilesList.setLayoutData(fd_addedFilesList);
		
		
		
		Composite composite_1 = toolkit.createComposite(parent, SWT.NONE);//new Composite(this, SWT.NONE);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.top = new FormAttachment(addedFilesList, 0, SWT.TOP);
		fd_composite_1.left = new FormAttachment(composite, 0, SWT.LEFT);
		fd_composite_1.right = new FormAttachment(addedFilesList, -6);
		fd_composite_1.bottom = new FormAttachment(lblNewLabel, -4);
		composite_1.setLayoutData(fd_composite_1);
		
		
		
		Label lblRemoteHost = toolkit.createLabel(parent, "");//new Label(composite_1, SWT.NONE);
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
		
		Label lblTryProjectConfiguration = toolkit.createLabel(parent, "");//new Label(this, SWT.NONE);
		FormData fd_lblTryProjectConfiguration = new FormData();
		fd_lblTryProjectConfiguration.top = new FormAttachment(lblNewLabel_1, 0, SWT.TOP);
		fd_lblTryProjectConfiguration.left = new FormAttachment(0, 10);
		lblTryProjectConfiguration.setLayoutData(fd_lblTryProjectConfiguration);
		lblTryProjectConfiguration.setText("Try project configuration");
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		treeViewer.getControl().setFocus();
	}
}
