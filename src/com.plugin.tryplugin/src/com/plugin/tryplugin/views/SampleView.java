/*
 * @author Wander Bravo
 * @author Jose Raymundo Cruz
 * 
 */
package com.plugin.tryplugin.views;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;


public class SampleView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.plugin.tryplugin.views.SampleView";

	private TreeViewer treeViewer;
	private DrillDownAdapter drillDownAdapter;
	
	
	private Action action1;
	private Action action2;
	
	private Action doubleClickAction;
	private Action doubleClickAction2;
	
	private TableViewer table;
	private ArrayList<FileModel> tryElement = new ArrayList<FileModel>();
	
	private Text passwordText;
	private Text usernameText;
	private Text text;
	private Text assignmentCodeText;
	private Text instructorAccountText;
	 
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
		@SuppressWarnings("rawtypes")
		public Object getAdapter(Class key) {
			return null;
		}
	}
	
	class TreeParent extends TreeObject {
		private ArrayList<TreeObject> children;
		public TreeParent(String name) {
			super(name);
			children = new ArrayList<TreeObject>();
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
			return children.toArray(new TreeObject[children.size()]);
		}
		public boolean hasChildren() {
			return children.size()>0;
		}
	}

	class ViewContentProvider implements IStructuredContentProvider, 
										   ITreeContentProvider {
		
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
			File file = (File) obj;
			String name = file.getName();
			if(name.length()>0)
				return name;
			return file.getPath();
		}
		public Image getImage(Object obj) {
			String imageKey = ISharedImages.IMG_OBJ_FILE;
			File tmp = (File)obj;
			
			if(tmp.isDirectory())
				imageKey = ISharedImages.IMG_OBJ_FOLDER;
			   				 
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
	}
	class NameSorter extends ViewerSorter {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
	    
		/*Section 1*/
		createTryProjectConfigurationForm(toolkit,parent);
	    
	    /*Section 2*/
		createFilesAddedView(toolkit,parent);
	    
		/*Section 3*/		
		createBrowseLocalFilesView(toolkit,parent);
	    
	}

	private void createBrowseLocalFilesView(FormToolkit toolkit, Composite parent) {
		Section localFilesSection = toolkit.createSection(parent,Section.TITLE_BAR | Section.DESCRIPTION);
		
	    localFilesSection.setText("Local Files"); 
	    localFilesSection.setDescription("Double click to add to current project"); 
	    
	    Composite composite = toolkit.createComposite(localFilesSection, SWT.WRAP);
		composite.setBounds(10, 94, 64, 64);
		
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100, -10);
		fd_composite.right = new FormAttachment(0, 751);
		fd_composite.left = new FormAttachment(0, 10);
		composite.setLayoutData(fd_composite);
		composite.setLayout(new TreeColumnLayout());
		
		treeViewer = new TreeViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		Tree localFilesTree = treeViewer.getTree();
		localFilesTree.setHeaderVisible(true);
		localFilesTree.setLinesVisible(true);
		
		treeViewer.setAutoExpandLevel(2);
		
		drillDownAdapter = new DrillDownAdapter(treeViewer);
		
		treeViewer.setContentProvider(new ViewContentProvider());
		
		treeViewer.setLabelProvider(new ViewLabelProvider());
		
		treeViewer.setSorter(new NameSorter());
		treeViewer.setInput(File.listRoots());
		
		
		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(treeViewer.getControl(), "com.plugin.tryplugin.viewer");
		
		makeActions();		
		hookContextMenu();
		hookDoubleClickAction();		
		contributeToActionBars();		
		localFilesSection.setClient(composite);						
	}

	private void createFilesAddedView(FormToolkit toolkit, Composite parent) {
	    Section section = toolkit.createSection(parent, Section.TITLE_BAR | Section.DESCRIPTION);
	    section.setText("Files selected"); 
	    section.setDescription("Double click to remove from current project");
	    
	    Composite client = toolkit.createComposite(section, SWT.WRAP);
	    
	    GridLayout layout = new GridLayout();
	    layout.numColumns = 1;
	    layout.marginWidth = 2;
	    layout.marginHeight = 2;
	    
	    client.setLayout(layout);
	    
	    Table t = toolkit.createTable(client, SWT.NULL);
	    
	    GridData gd = new GridData(GridData.FILL_BOTH);
	    gd.heightHint = 20;
	    gd.widthHint = 200;
	    
	    t.setLayoutData(gd);
	    toolkit.paintBordersFor(client);

	    section.setClient(client);
	    
	    table = new TableViewer(t);
	    ArrayContentProvider tmp =new ArrayContentProvider(); 
	    table.setContentProvider(tmp);
	    
	    TableViewerColumn viewerColumn = new TableViewerColumn(table, SWT.LEFT);
	    viewerColumn.getColumn().setWidth(100);
	    viewerColumn.setLabelProvider(new ColumnLabelProvider () {
	      @Override
	      public String getText(Object element) {
	        return element.toString();
	      };	     	      
	        
	      public Image getImage(Object element) {
	        return PlatformUI.getWorkbench().getSharedImages()
	            .getImage(ISharedImages.IMG_OBJ_FILE);
	      };
	      
	    });
	    
		hookContextMenu2();
		hookDoubleClickAction2();
	    
		
	}

	private void createTryProjectConfigurationForm(FormToolkit toolkit, Composite parent) {
		Form form= toolkit.createForm(parent);
	    form.setText("Project Configuration");
	    
	    Label lblRemoteHost = new Label(form.getBody(), SWT.NONE);
		lblRemoteHost.setBounds(10, 14, 102, 25);
		lblRemoteHost.setText("Remote host:");

		text = new Text(form.getBody(), SWT.BORDER);		
		text.setBounds(158, 10, 172, 25);
		
		Label lblPassword = new Label(form.getBody(), SWT.NONE);
		lblPassword.setText("Password:");
		lblPassword.setBounds(10, 70, 102, 25);
		
		passwordText = new Text(form.getBody(), SWT.BORDER);
		passwordText.setEchoChar('*');
		passwordText.setBounds(158, 68, 172, 25);
		
		
		Label lblUsername = new Label(form.getBody(), SWT.NONE);
		lblUsername.setText("Username:");
		lblUsername.setBounds(10, 42, 102, 25);
		
		usernameText = new Text(form.getBody(), SWT.BORDER);
		usernameText.setBounds(158, 40, 172, 25);
		
		Label lblInstructorAccount = new Label(form.getBody(), SWT.NONE);
		lblInstructorAccount.setText("Instructor account:");
		lblInstructorAccount.setBounds(10, 98, 143, 25);
		
		instructorAccountText = new Text(form.getBody(), SWT.BORDER);
		instructorAccountText.setBounds(158, 97, 172, 25);
		
		Label lblAssignmentCode = new Label(form.getBody(), SWT.NONE);
		lblAssignmentCode.setText("Assignment code:");
		lblAssignmentCode.setBounds(10, 126, 143, 25);
		
		assignmentCodeText = new Text(form.getBody(), SWT.BORDER);
		assignmentCodeText.setBounds(158, 126, 172, 25);
		
		Button btnRun = new Button(form.getBody(), SWT.NONE);
		btnRun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				usernameText.setText("Hello worlds");
			}
		});
		btnRun.setBounds(10, 160, 60, 60);
		btnRun.setText("Run");
		
	}

	private void hookContextMenu() {
		
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SampleView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
		treeViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, treeViewer);
	}

	private void hookContextMenu2() {
		MenuManager menuMgr = new MenuManager("#2ndMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SampleView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(table.getControl());
		table.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, table);
	}
	
	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		
		manager.add(action1);
		manager.add(action2);
		manager.add(new Separator());
		
		drillDownAdapter.addNavigationActions(manager);
		
	}

	private void makeActions() {
		
		action1 = new Action() {
			public void run() {
				ISelection selection = treeViewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				File tmp = (File)obj;				
				
				if(!tmp.isDirectory())
					{
						if(!tryElement.contains(new FileModel(obj.toString())))
						{	
							tryElement.add(new FileModel(obj.toString()));						
							table.setInput(tryElement);
						}
					}
			}
		};		
		
		action1.setText("Upload this");
		action1.setToolTipText("Add this file to the list");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		
		action2 = new Action() {
			public void run() {
				
				ISelection selection = table.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				FileModel tmp = (FileModel)obj;				
				
				if(tryElement.contains(tmp))
				{	
					tryElement.remove( tmp);						
					table.setInput(tryElement);
				}
				
			}
		};
		action2.setText("Remove this");
		action2.setToolTipText("Remove this file from the list");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_ERROR_TSK));

		
		doubleClickAction = new Action() {
			public void run() {
				
				ISelection selection = treeViewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				File tmp = (File)obj;				
				
				if(!tmp.isDirectory())
					{
						if(!tryElement.contains(new FileModel(obj.toString())))
						{	
							tryElement.add(new FileModel(obj.toString()));						
							table.setInput(tryElement);
						}
					}
			}
		};
		
		doubleClickAction2 = new Action() {
			public void run() {
				
				ISelection selection = table.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				FileModel tmp = (FileModel)obj;				
				
				if(tryElement.contains(tmp))
				{	
					tryElement.remove( tmp);						
					table.setInput(tryElement);
				}
				
			}
		};
		
	}

	private void hookDoubleClickAction() {
		
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	private void hookDoubleClickAction2() {
		
		table.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction2.run();
			}
		});
	}
	
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}
	public class FileModel
	{		
		@Override
		public boolean equals(Object obj) {
			FileModel tmp2 = (FileModel) obj;

			return this.fullPath.equals(tmp2.fullPath);
		}

		public String fullPath;
		
		public FileModel(String name)
		{
			this.fullPath = name;
		}
		public String toString()
		{
			File tmp = new File(this.fullPath);			
			return tmp.getName();
		}		
	}
}