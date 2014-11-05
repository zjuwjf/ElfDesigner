package elfEngine.basic.node.nodeAnimate.flash;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.ielfgame.stupidGame.AbstractWorkSpaceTab;

import elfEngine.basic.node.nodeAnimate.timeLine.TimeData;

public class FlashWorkSpaceTab extends AbstractWorkSpaceTab implements ModifyListener{
	
	public FlashWorkSpaceTab() {
		super("Flash Edit");
	}
	
	private final static void setLayoutData(final Composite composite, boolean prefer) {
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		if(prefer) {
			data.heightHint = data.widthHint = SWT.DEFAULT;	
			data.verticalAlignment = data.horizontalAlignment = 0;
			data.grabExcessVerticalSpace = data.grabExcessHorizontalSpace = false;
		} else {
			data.verticalAlignment = data.horizontalAlignment = SWT.FILL;
			data.grabExcessVerticalSpace = data.grabExcessHorizontalSpace = true;
		} 
		composite.setLayoutData(data);
	}
	
	public Composite createTab(final CTabFolder parent) { 
		final Composite rootPanel = new Composite(parent, SWT.BORDER|SWT.SEPARATOR);
		rootPanel.setLayout(new GridLayout(1, false));
		setLayoutData(rootPanel, false);
		rootPanel.setOrientation(SWT.VERTICAL);
		
		final FlashWorkSpaceBar flashBar = new FlashWorkSpaceBar();
		flashBar.createComposite(rootPanel);
		
//		final FlashCanvasView flashView = new FlashCanvasView(); 
//		final Canvas canvas = flashView.create(rootPanel); 
//		setLayoutData(canvas, false); 
		
//		final FlashGLView flashGLView = new FlashGLView(); 
//		final Composite composite = flashGLView.create(rootPanel); 
//		setLayoutData(composite, false); 
		
		run(); 
		
		return rootPanel;
	}
	
	public void modifyText(ModifyEvent e) {
	} 
	
	public void onSelect(){
		super.onSelect();
	}
	
	public void onDeselect(){ 
		super.onDeselect();
		
		TimeData.setSelectTimeData(null);
	}

	@Override
	public void update() {
		
	} 
	//add delete 
	//move 
} 
