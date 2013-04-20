package com.plugin.tryplugin.views;


import java.util.ArrayList;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
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

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

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
	private Action action1;
	//private Action action2;
	private Action doubleClickAction;
	private TableViewer table;
	
	
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
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		
					
		
		
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
	    //ScrolledForm form = toolkit.createScrolledForm(parent);
	    //form.setText(" ScrolledForm Text");
		
		
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
	    gd.widthHint = 100;
	    
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
	            .getImage(ISharedImages.IMG_OBJ_ELEMENT);
	      };
	    });
	    table.setInput(getViewSite());
	    
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

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		//manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		//manager.add(action2);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		
		manager.add(action1);
		//manager.add(action2);
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
					showMessage("Trying to upload this file: "+obj.toString());
			}
		};
		//showMessage("Action 1 executed");
		
		//ISelection selection = viewer.getSelection();
		//Object obj = ((IStructuredSelection)selection).getFirstElement();
		//path1 = ""+ obj.toString();
		
		action1.setText("Upload this");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		
		/*
		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		*/
		
		doubleClickAction = new Action() {
			public void run() {
				
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				File tmp = (File)obj;				
				
				if(!tmp.isDirectory())
					showMessage("Trying to upload "+obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
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