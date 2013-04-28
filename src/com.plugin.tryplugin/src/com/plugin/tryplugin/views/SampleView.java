package com.plugin.tryplugin.views;


import java.util.ArrayList;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.*;
	
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.core.runtime.IAdaptable;

import java.awt.Toolkit;
import java.io.File;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.PasswordView;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;


public class SampleView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.plugin.tryplugin.views.SampleView";

	//private TreeViewer viewer;
	private TreeViewer treeViewer;
	private DrillDownAdapter drillDownAdapter;
	
	private ScrolledForm form;
	
	private Action action1;
	private Action action2;
	
	private Action doubleClickAction;
	private Action doubleClickAction2;
	
	private TableViewer table;
	private ArrayList<FileModel> tryElement = new ArrayList<FileModel>();
	
	private String tryStatus="";
	
	
	private Text passwordText;
	private Text usernameText;
	private Text text;
	private Text assignmentCodeText;
	private Text instructorAccountText;
	private Text outputTry;
	
	 
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
	 * The constructor.
	 */		

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
	    
		/*Section 1*/
		Section section2 = toolkit.createSection(parent, Section.DESCRIPTION
		        | Section.TITLE_BAR);
		
	    section2.setText("Files Local Machine"); //$NON-NLS-1$
	    section2.setDescription("These files are going to be included in List above");
	    
	    Composite composite = toolkit.createComposite(section2, SWT.WRAP);
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
		section2.setClient(composite);						
		/*End Section 1*/
	    
	    
	    /*Section 2*/
	    // Lets make a layout for the first section of the screen
	    GridLayout layout = new GridLayout();
	    layout.numColumns = 1;
	    layout.marginWidth = 2;
	    layout.marginHeight = 2;
	    
	    // Creating the Screen
	    Section section = toolkit.createSection(parent, Section.DESCRIPTION
	        | Section.TITLE_BAR);
	    
	    section.setText("Files selected"); 
	    section.setDescription("These files are going to be included in the try-command");
	    // Composite for storing the data
	    
	    Composite client = toolkit.createComposite(section, SWT.WRAP);
	    layout = new GridLayout();
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
	    
	    //tableViewer
	    //Table	    
	 
	    table = new TableViewer(t);
	    ArrayContentProvider tmp =new ArrayContentProvider(); 
	    table.setContentProvider(tmp);
	    
	    TableViewerColumn viewerColumn = new TableViewerColumn(table, SWT.RIGHT);
	    viewerColumn.getColumn().setWidth(400);
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
	    
	    makeActions2();
		hookContextMenu2();
		hookDoubleClickAction2();
		
		  /*
	    viewerColumn.setLabelProvider(new StyledCellLabelProvider() {
	    	@Override
	    	  public void update(ViewerCell cell) {
	    	    StyledString text = new StyledString();
	    	    
	    	    StyleRange myStyledRange = new StyleRange(17, 2, null, Display
	    	      .getCurrent().getSystemColor(SWT.COLOR_YELLOW));
	    	    
	    	    String value = (String)cell.getElement();
	    	    text.append(value, StyledString.DECORATIONS_STYLER);	    	    
	    	    
	    	    cell.setText(text.toString());

	    	    StyleRange[] range = { myStyledRange };
	    	    cell.setStyleRanges(range);
	    	    
	    	    super.update(cell);
	    	  };
	  	      
	    	});
	    */
	    
	    //getSite().setSelectionProvider(table);
	    //table.setInput(tryElement);
	    
	    /*End Section 2*/
	    
		/*Section 3*/		
			
	    form = toolkit.createScrolledForm(parent);
	    form.setText("Configure the Connection to the Server");
	    GridLayout layout3 = new GridLayout();
	    form.getBody().setLayout(layout3);


	    layout3.numColumns = 2;
	    GridData gd3 = new GridData();
	    gd3.horizontalSpan = 2;
	    
	    
	    Label lblRemoteHost = new Label(form.getBody(), SWT.NONE);
		lblRemoteHost.setBounds(10, 14, 102, 25);
		lblRemoteHost.setText("Remote host:");
		
		
		
		Label lblUsername = new Label(form.getBody(), SWT.NONE);
		lblUsername.setText("Username:");
		lblUsername.setBounds(10, 42, 102, 25);
		
		passwordText = new Text(form.getBody(), SWT.BORDER);
		passwordText.setEchoChar('*');
		passwordText.setBounds(158, 68, 172, 25);
		
		usernameText = new Text(form.getBody(), SWT.BORDER);
		usernameText.setBounds(158, 40, 172, 25);
		
		text = new Text(form.getBody(), SWT.BORDER);
		text.setBounds(158, 10, 172, 25);
		
		Label lblPassword = new Label(form.getBody(), SWT.NONE);
		lblPassword.setText("Password:");
		lblPassword.setBounds(10, 70, 102, 25);
		
		Label lblInstructorAccount = new Label(form.getBody(), SWT.NONE);
		lblInstructorAccount.setText("Instructor Account:");
		lblInstructorAccount.setBounds(10, 98, 143, 25);
		
		assignmentCodeText = new Text(form.getBody(), SWT.BORDER);
		assignmentCodeText.setBounds(158, 126, 172, 25);
		
		Label lblAssignmentCode = new Label(form.getBody(), SWT.NONE);
		lblAssignmentCode.setText("Assignment code:");
		lblAssignmentCode.setBounds(10, 126, 143, 25);
		
		instructorAccountText = new Text(form.getBody(), SWT.BORDER);
		instructorAccountText.setBounds(158, 97, 172, 25);
		
		Button btnRun = new Button(form.getBody(), SWT.NONE);
		btnRun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				usernameText.setText("Hello worlds");
			}
		});
		btnRun.setBounds(348, 10, 76, 46);
		btnRun.setText("Run");
		
		Label lblOutput = new Label(form.getBody(), SWT.NONE);
		lblOutput.setText("Output:");
		lblOutput.setBounds(10, 200, 76, 25);
		
		
		// This is the Text field for the OUTPUT.
		outputTry = new Text(form.getBody(), SWT.BORDER);
		outputTry.setBounds(10, 250, 370, 300);
		
		Label lblTryProjectConfiguration = new Label(form.getBody(), SWT.NONE);
		FormData fd_lblTryProjectConfiguration = new FormData();	
		fd_lblTryProjectConfiguration.left = new FormAttachment(0, 10);
		lblTryProjectConfiguration.setLayoutData(fd_lblTryProjectConfiguration);
		lblTryProjectConfiguration.setText("Try project configuration");
		
	    /*End Section 3*/
	    
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
		// Other plug-ins can contribute there actions here
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
				//showMessage("Action 1 executed");
				ISelection selection = treeViewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				File tmp = (File)obj;				
				
				if(!tmp.isDirectory())
					{
						//showMessage("Trying to upload "+obj.toString());
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
						//showMessage("Trying to upload "+obj.toString());
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

	private void makeActions2() {
		
		
		
	
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
	
	
	private void showMessage(String message) {
		MessageDialog.openInformation(
				treeViewer.getControl().getShell(),
			" ",
			message);
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
			return ".. "+tmp.getName();
		}		
	}
}