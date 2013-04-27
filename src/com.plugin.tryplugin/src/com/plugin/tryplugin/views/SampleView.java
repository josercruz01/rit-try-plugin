package com.plugin.tryplugin.views;


import java.util.ArrayList;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
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

import com.plugin.tryplugin.core.models.ITryCommandView;

/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class SampleView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.plugin.tryplugin.views.SampleView";

	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	
	private ScrolledForm form;
	
	private Action action1;
	private Action action2;
	private Action doubleClickAction;
	private Action doubleClickAction2;
	
	private TableViewer table;
	private ArrayList<String> tryElement = new ArrayList<String>();
	
	private String tryStatus="";
	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
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
			/*
			if (parent.equals(getViewSite())) {
				if (invisibleRoot==null) initialize();
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
			*/
		}
		public Object getParent(Object child) {
			return ((File) child).getParentFile();
			/*
			if (child instanceof TreeObject) {
				return ((TreeObject)child).getParent();
			}
			return null;
			*/
		}
		public Object [] getChildren(Object parent) {
			File file = (File)parent;
			return file.listFiles();
			/*
			if (parent instanceof TreeParent) {
				return ((TreeParent)parent).getChildren();
			}
			return new Object[0];
			*/
		}
		public boolean hasChildren(Object parent) {
			File file = (File) parent;
			if(file.isDirectory())
				return true;
			return false;
		}
/*
 * We will set up a dummy model to initialize tree heararchy.
 * In a real code, you will connect to a real model and
 * expose its hierarchy.
 */
		/*
		private void initialize() {
			TreeObject to1 = new TreeObject("Leaf 1");
			TreeObject to2 = new TreeObject("Leaf 2");
			TreeObject to3 = new TreeObject("Leaf 3");
			TreeParent p1 = new TreeParent("Parent 1");
			p1.addChild(to1);
			p1.addChild(to2);
			p1.addChild(to3);
			
			TreeObject to4 = new TreeObject("Leaf 4");
			TreeParent p2 = new TreeParent("Parent 2");
			p2.addChild(to4);
			
			TreeParent root = new TreeParent("Root");
			root.addChild(p1);
			root.addChild(p2);
			
			invisibleRoot = new TreeParent("");
			invisibleRoot.addChild(root);
		}
		*/
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

	/**
	 * The constructor.
	 */
	public SampleView() {
		ITryCommandView view = new ITryCommandView() {
			
			@Override
			public boolean promptYesNoRSAKeyFingerprint(String str) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCommandExecuted(String result, int exitStatus) {
				// TODO Auto-generated method stub
				
			}
		};
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		
					
		
		
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
	    //ScrolledForm form = toolkit.createScrolledForm(parent);
	    //form.setText(" ScrolledForm Text");
		
		/*Section 1*/
		Section section2 = toolkit.createSection(parent, Section.DESCRIPTION
		        | Section.TITLE_BAR);
	    section2.setText("Files Local Machine"); //$NON-NLS-1$
	    section2.setDescription("These files are going to be included in List above");
	    
	    Composite browseLocal = toolkit.createComposite(section2, SWT.WRAP);
	    
    	viewer = new TreeViewer(browseLocal, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
	    
    	GridLayout layout2 = new GridLayout();
	    layout2.numColumns = 1;
	    layout2.marginWidth = 2;
	    layout2.marginHeight = 2;
	    
	    //viewer.
	    browseLocal.setLayout(layout2);
	    
		drillDownAdapter = new DrillDownAdapter(viewer);
		
		viewer.setContentProvider(new ViewContentProvider());
		
		viewer.setLabelProvider(new ViewLabelProvider());
		
		viewer.setSorter(new NameSorter());
		//viewer.setInput(getViewSite());
		
		viewer.setInput(File.listRoots());
		
		
		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.plugin.tryplugin.viewer");
		
		makeActions();
		
		hookContextMenu();
		

		
		hookDoubleClickAction();
		
		contributeToActionBars();
		section2.setClient(browseLocal);
		
		/*Section 1*/
	    
	    
	    /*Section 2*/
	    // Lets make a layout for the first section of the screen
	    GridLayout layout = new GridLayout();
	    layout.numColumns = 1;
	    layout.marginWidth = 2;
	    layout.marginHeight = 2;
	    
	    // Creating the Screen
	    Section section = toolkit.createSection(parent, Section.DESCRIPTION
	        | Section.TITLE_BAR);
	    section.setText("Files selected"); //$NON-NLS-1$
	    section.setDescription("These files are going to be included in the try-command");
	    // Composite for storing the data
	    Composite client = toolkit.createComposite(section, SWT.WRAP);
	    layout = new GridLayout();
	    layout.numColumns = 2;
	    layout.marginWidth = 2;
	    layout.marginHeight = 2;
	    
	    client.setLayout(layout);
	    Table t = toolkit.createTable(client, SWT.NULL);
	    
	    GridData gd = new GridData(GridData.FILL_BOTH);
	    gd.heightHint = 20;
	    gd.widthHint = 200;
	    
	    t.setLayoutData(gd);
	    toolkit.paintBordersFor(client);
	    
	    Button b = toolkit.createButton(client, "Run Try Command", SWT.PUSH); //$NON-NLS-1$
	    
	    gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
	    b.setLayoutData(gd);
	    section.setClient(client);
	    
	    //tableViewer
	    //Table
	    
	    
	    table = new TableViewer(t);
	    ArrayContentProvider tmp =new ArrayContentProvider(); 
	    table.setContentProvider(tmp);
	    
	    TableViewerColumn viewerColumn = new TableViewerColumn(table, SWT.NONE);
	    viewerColumn.getColumn().setWidth(100);
	    viewerColumn.setLabelProvider(new ColumnLabelProvider() {
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
		
	    getSite().setSelectionProvider(table);
	    table.setInput(tryElement);
	    
	    /*Section 2*/
	    
	    
	    
		/*Section 3*/
	    toolkit = new FormToolkit(parent.getDisplay());
	    form = toolkit.createScrolledForm(parent);
	    form.setText("Configure the Connection to the Server");
	    GridLayout layout3 = new GridLayout();
	    form.getBody().setLayout(layout3);
	    
	    
	    layout3.numColumns = 2;
	    GridData gd3 = new GridData();
	    gd3.horizontalSpan = 2;
	    
	    
	    Label labelServer = new Label(form.getBody(), SWT.NULL);
	    labelServer.setText("Server Name:");
	    
	    Text text = new Text(form.getBody(), SWT.BORDER);
	    text.setTextLimit(20);
	    text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    
	    Label labelUser = new Label(form.getBody(), SWT.NULL);
	    labelUser.setText("User Name:");
	    
	    Text textUser = new Text(form.getBody(), SWT.BORDER);
	    textUser.setTextLimit(10);
	    textUser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    
	    Label labelPassword = new Label(form.getBody(), SWT.NULL);
	    labelPassword.setText("Password:");	        
	     
	    Text textPassword = new Text(form.getBody(), SWT.BORDER);
	    textPassword.setTextLimit(10);
	    textPassword.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    
	    Label labelInstructor = new Label(form.getBody(), SWT.NULL);
	    labelInstructor.setText("Instructor account:");
	    
	    Text textInstructr = new Text(form.getBody(), SWT.BORDER);
	    textInstructr.setTextLimit(15);
	    textInstructr.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    
	    Label labelAssignment = new Label(form.getBody(), SWT.NULL);
	    labelAssignment.setText("Assignment code:");
	    
	    Text textAssignment = new Text(form.getBody(), SWT.BORDER);
	    textAssignment.setTextLimit(10);
	    textAssignment.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    
	    Button button = new Button(form.getBody(), SWT.CHECK);
	    
	    button.setText("Save the files in the server");
	    gd3 = new GridData();
	    gd3.horizontalSpan = 2;
	    button.setLayoutData(gd3);
	    
	    Label SaveFile = new Label(form.getBody(), SWT.NULL);
	    SaveFile.setText("Do you want to see the output File: ");
	    
	    Hyperlink link = toolkit.createHyperlink(form.getBody(), 
	  	      "Click here.", SWT.WRAP);
	  	    link.addHyperlinkListener(new HyperlinkAdapter() {
	  	     public void linkActivated(HyperlinkEvent e) {
	  	      System.out.println("Link activated!");
	  	     }
	  	    });
	  	  link.setLayoutData(gd3);
	  	  
	  	  Label OutputTry = new Label(form.getBody(), SWT.NULL);
		    SaveFile.setText("try "+textInstructr.getText()+" "+textAssignment.getText()+ tryStatus);
		    //try instructor-account assignment-code file-list
	    /*
		Section section3 = toolkit.createSection(parent, Section.DESCRIPTION
		        | Section.TITLE_BAR);
		section3.setText("Connection Setup"); //$NON-NLS-1$
		section3.setDescription("Setting up the connection");
	    
	    Composite browseLocal2 = toolkit.createComposite(section3, SWT.WRAP);
	    
    	//viewer = new TreeViewer(browseLocal2, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
	    
    	GridLayout layout3 = new GridLayout();
    	layout3.numColumns = 1;
    	layout3.marginWidth = 2;
    	layout3.marginHeight = 2;
	    
	    //viewer.
	    browseLocal2.setLayout(layout3);
	    
	    form = toolkit.createScrolledForm(browseLocal2);  	    
	    
	    form.setText("Hello, Eclipse Forms");
	    
	    //TableWrapLayout layoutWrap = new TableWrapLayout();
	    form.getBody().setLayout(layout3);
	    
	    
	    
	    //link.setText("This is an example of a form that is much longer and will need to wrap.");
	    layout.numColumns = 2;
	    TableWrapData td = new TableWrapData();
	    td.colspan = 2;
	    //link.setLayoutData(td);
	    Label label = toolkit.createLabel(form.getBody(), "Text field label:");
	    Text text = toolkit.createText(form.getBody(), "");
	    td = new TableWrapData(TableWrapData.FILL_GRAB);
	    text.setLayoutData(td);
	    Button button = toolkit.createButton(form.getBody(), "A checkbox in a form", SWT.CHECK);
	    td = new TableWrapData();
	    td.colspan = 2;
	    button.setLayoutData(td);
	    
	    */
	    /*Section 3*/
	    
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SampleView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
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
		String path1="";
		action1 = new Action() {
			public void run() {
				//showMessage("Action 1 executed");
				
				
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				File tmp = (File)obj;								
				if(!tmp.isDirectory())
				{	
					//showMessage("Trying to upload this file: "+obj.toString());
					if(!tryElement.contains(obj.toString()))
					{	
						tryElement.add(obj.toString());						
						table.setInput(tryElement);
					}
				}
			}
		};
		//showMessage("Action 1 executed");
		
		//ISelection selection = viewer.getSelection();
		//Object obj = ((IStructuredSelection)selection).getFirstElement();
		//path1 = ""+ obj.toString();
		
		action1.setText("Upload this");
		action1.setToolTipText("Add this file to the list");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		
		action2 = new Action() {
			public void run() {
				//showMessage("Action 2 executed");
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				File tmp = (File)obj;								
				if(!tmp.isDirectory())
				{	
					//showMessage("Trying to upload this file: "+obj.toString());
					if(tryElement.contains(obj.toString()))
					{	
						tryElement.remove(obj.toString());						
						table.setInput(tryElement);
					}
				}
			}
		};
		action2.setText("Remove this");
		action2.setToolTipText("Remove this file from the list");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_ERROR_TSK));

		
		doubleClickAction = new Action() {
			public void run() {
				
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				File tmp = (File)obj;				
				
				if(!tmp.isDirectory())
					{
						//showMessage("Trying to upload "+obj.toString());
						if(!tryElement.contains(obj.toString()))
						{	
							tryElement.add(obj.toString());						
							table.setInput(tryElement);
						}
					}
			}
		};
		
		doubleClickAction2 = new Action() {
			public void run() {
				
				ISelection selection = table.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				String tmp = (String)obj;				
								
				//showMessage("Trying to upload "+obj.toString());
				if(tryElement.contains(tmp.toString()))
				{	
					tryElement.remove(tmp.toString());						
					table.setInput(tryElement);
				}
				
			}
		};
		
	}

	private void makeActions2() {
		
		
		
	
	}
	private void hookDoubleClickAction() {
		
		viewer.addDoubleClickListener(new IDoubleClickListener() {
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
			viewer.getControl().getShell(),
			" ",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}