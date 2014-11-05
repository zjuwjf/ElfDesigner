package com.ielfgame.stupidGame;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.ielfgame.stupidGame.design.ElfClassLoader;
import com.ielfgame.stupidGame.design.Surface.Surface;
import com.ielfgame.stupidGame.design.Surface.SurfaceWindow;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.utils.FileHelper;

public class SurfaceTab extends AbstractWorkSpaceTab {
	public SurfaceTab() { 
		super("Surface"); 
	} 
	
	public Composite createTab(CTabFolder parent) {
		final Composite composite = new Composite(parent, SWT.NONE | SWT.BORDER);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginHeight = gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
		composite.setLayout(gridLayout);
		
		Composite composite2 = new Composite(composite, SWT.NONE);
		composite2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite2.setLayout(layout);
		
		final CCombo surfaceCCombo = new CCombo(composite2, SWT.NONE | SWT.BORDER);
		surfaceCCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		final Button run = new Button(composite2, SWT.CENTER);
		GridData gridData = new GridData(); 
		gridData.widthHint = 50; 
		run.setLayoutData(gridData); 
		
		run.addSelectionListener(new SelectionListener() { 
			public void widgetSelected(SelectionEvent e) {
				try {
					final String surfaceName = surfaceCCombo.getText(); 
					final SurfaceWindow glView = new SurfaceWindow(); 
					final ElfClassLoader ucl = ElfClassLoader.getClassLoader(); 
					
					final Class<?> _class = ucl.loadClass("com.ielfgame.stupidGame.design.hotSwap."+surfaceName);
					final Object surface = _class.newInstance(); 
					glView.setSurface((Surface)surface); 
					glView.open(PowerMan.getSingleton(MainDesigner.class).mShell);
				} catch (Exception e2) { 
					e2.printStackTrace();
				} 
			} 
			
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			} 
		});
		run.setText("run");
		
		composite2 = new Composite(composite, SWT.NONE);
		composite2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		layout = new GridLayout();
		layout.numColumns = 2;
		composite2.setLayout(layout); 
		
		final Text pathText = new Text(composite2, SWT.NONE | SWT.BORDER);
		pathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		pathText.setText("com.ielfgame.stupidGame.design.hotSwap");
		
		final Button refresh = new Button(composite2, SWT.CENTER);
		refresh.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				try {
					final String path = pathText.getText();
					final String dir = FileHelper.getUserDir()+FileHelper.DECOLLATOR+"src"+FileHelper.DECOLLATOR+path.replace(".", FileHelper.DECOLLATOR);
					final String [] list = new File(dir).list(); 
					surfaceCCombo.removeAll();
					for(String sur: list) {
						if(sur.startsWith("Surface") && sur.endsWith(".java")) { 
							surfaceCCombo.add(sur.substring(0, sur.length()-5)); 
						} 
					} 
				} catch (Exception e2) {
					e2.printStackTrace();
				} 
			} 
			public void widgetDefaultSelected(SelectionEvent e) { 
				widgetSelected(e);
			} 
		});
		refresh.setText("refresh");
		gridData = new GridData(); 
		gridData.widthHint = 50; 
		refresh.setLayoutData(gridData); 
		
		try {
			final String path = pathText.getText();
			final String dir = FileHelper.getUserDir()+FileHelper.DECOLLATOR+"src"+FileHelper.DECOLLATOR+path.replace(".", FileHelper.DECOLLATOR);
			final String [] list = new File(dir).list(); 
			surfaceCCombo.clearSelection();
			for(String sur: list) { 
				if(sur.startsWith("Surface") && sur.endsWith(".java")) { 
					surfaceCCombo.add(sur.substring(0, sur.length()-5)); 
				} 
			} 
		} catch (Exception e2) { 
//			e2.printStackTrace(); 
		} 
		
		final StyledText output = new StyledText(composite, SWT.NONE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		output.setEditable(false);
		output.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		output.setWordWrap(true);
		output.setJustify(true);
		output.setBottomMargin(0);
		
		return composite;
	}

	public void update() {

	}
}