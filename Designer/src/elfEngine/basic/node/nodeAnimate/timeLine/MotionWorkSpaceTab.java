package elfEngine.basic.node.nodeAnimate.timeLine;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.ielfgame.stupidGame.AbstractWorkSpaceTab;
import com.ielfgame.stupidGame.IconCache;
import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.ResJudge;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.utils.EnumHelper;

public class MotionWorkSpaceTab extends AbstractWorkSpaceTab implements ModifyListener{

	private ElfMotionNode mBindMotionNode ;
	private TimeCanvasView mTimeCanvasView;
	private Spinner mLoopStartSpinner, mLoopEndSpinner, mProgressSpinner;
	private Spinner mMotionSpeed;
	private CCombo mLoopCombo, mInterCombo;
	private ToolItem mPalyItem, mPauseItem;
	private Button mAutoKeyButton;
	
	public MotionWorkSpaceTab() {
		super("Motion Edit");
	}
	
	public void setMotionNode(ElfMotionNode motionNode) {
		mBindMotionNode = motionNode;
		if(mTimeCanvasView != null) { 
			mTimeCanvasView.loadMotionNode(motionNode); 
			
			if(mBindMotionNode!=null && !mBindMotionNode.getPauseMotion()) {
				mPalyItem.setEnabled(false);
				mPauseItem.setEnabled(true);
			} else {
				mPalyItem.setEnabled(true); 
				mPauseItem.setEnabled(false); 
			} 
		} 
	}
	public ElfMotionNode getMotionNode() {
		return mBindMotionNode;
	}
	
	public void update() {
		if(mBindMotionNode != null) { 
			if(mLoopStartSpinner.getEnabled() == false) {
				mLoopStartSpinner.setEnabled(true);
			}
			if(mLoopEndSpinner.getEnabled() == false) {
				mLoopEndSpinner.setEnabled(true);
			}
			if(mProgressSpinner.getEnabled() == false) {
				mProgressSpinner.setEnabled(true);
			}
			if(mLoopCombo.getEnabled() == false) {
				mLoopCombo.setEnabled(true);
			}
			if(mInterCombo.getEnabled() == false) {
				mInterCombo.setEnabled(true);
			} 
			//times
			if(mBindMotionNode.getLoopStart() != mLoopStartSpinner.getSelection()) {
				mLoopStartSpinner.setSelection( (int)mBindMotionNode.getLoopStart() ); 
			}
			if(mBindMotionNode.getLoopEnd() != mLoopEndSpinner.getSelection()) {
				mLoopEndSpinner.setSelection( (int)mBindMotionNode.getLoopEnd() ); 
			}
			if(mBindMotionNode.getProgress() != mProgressSpinner.getSelection()) {
				mProgressSpinner.setSelection( (int)mBindMotionNode.getProgress() ); 
			} 
			
			if(!mBindMotionNode.getLoopMode().toString().equals( mLoopCombo.getText() )) {
				mLoopCombo.setText(mBindMotionNode.getLoopMode().toString());  
			}
			if(!mBindMotionNode.getInterType().toString().equals( mInterCombo.getText() )) {
				mInterCombo.setText(mBindMotionNode.getInterType().toString()); 
			} 
			mAutoKeyButton.setSelection(mTimeCanvasView.getAutoKey());
			
			final int v = Math.round(mBindMotionNode.getMotionSpeed()*100);
			if(v != mMotionSpeed.getSelection()) {
				mMotionSpeed.setSelection(v);
			} 
		} else { 
			if(mLoopStartSpinner.getEnabled() == true) {
				mLoopStartSpinner.setEnabled(false);
			}
			if(mLoopEndSpinner.getEnabled() == true) {
				mLoopEndSpinner.setEnabled(false);
			}
			if(mProgressSpinner.getEnabled() == true) {
				mProgressSpinner.setEnabled(false);
			}
			if(mLoopCombo.getEnabled() == true) {
				mLoopCombo.setEnabled(false);
			}
			if(mInterCombo.getEnabled() == true) {
				mInterCombo.setEnabled(false);
			} 
		}
	} 
	
	public static void setLayoutData(final Composite composite, boolean prefer) {
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
	
	public Composite createTab(CTabFolder parent) { 
		final Composite rootPanel = new Composite(parent, SWT.BORDER|SWT.SEPARATOR);
		rootPanel.setLayout(new GridLayout(1, false));
		setLayoutData(rootPanel, false);
		rootPanel.setOrientation(SWT.VERTICAL);
		
		final Composite line = new Composite(rootPanel, SWT.BORDER|SWT.SEPARATOR);
		line.setLayout(new GridLayout(9, false));
		setLayoutData(line, true);
		line.setOrientation(SWT.VERTICAL);
		
//		final Group line0 = new Group(line, SWT.NONE); 
//		line0.setLayout(new GridLayout(3, false)); 
//		setLayoutData(line0, true); 
//		line0.setOrientation(SWT.HORIZONTAL); 
		{
//			final Composite toolComposite = new Composite(line0, SWT.NONE); 
//			toolComposite.setLayout(new GridLayout(1, true));
//			setLayoutData(toolComposite, true);
			
			ToolBar toolBar = new ToolBar(line, SWT.FLAT);
			toolBar.setLayout(new GridLayout(2, true));
			setLayoutData(toolBar, true);
			
			final IconCache iconCache = PowerMan.getSingleton(MainDesigner.class).mIconCache;
			// play tool item
			final ToolItem playItem = new ToolItem(toolBar, SWT.PUSH);
			playItem.setText(ResJudge.getResourceString("Play"));
			playItem.setImage(iconCache.stockImages[iconCache.iconPlay]);
			
			// pause tool item
			final ToolItem pauseItem = new ToolItem(toolBar, SWT.PUSH);
			pauseItem.setText(ResJudge.getResourceString("Pause"));
			pauseItem.setImage(iconCache.stockImages[iconCache.iconPause]);
			
			playItem.setEnabled(false);
			pauseItem.setEnabled(true);
			
			playItem.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					playItem.setEnabled(false);
					pauseItem.setEnabled(true);
					if(mBindMotionNode != null) {
						mBindMotionNode.setProgress(mBindMotionNode.getLoopStart());
						mBindMotionNode.setPauseMotion(false);
					}
				}
			});
			pauseItem.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					pauseItem.setEnabled(false);
					playItem.setEnabled(true);
					if(mBindMotionNode != null) {
						mBindMotionNode.setPauseMotion(true);
					}
				}
			});
			mPalyItem = playItem;
			mPauseItem = pauseItem;
		}
		
		{
//			final Composite timeComposite = new Composite(line0, SWT.NONE);
//			timeComposite.setLayout(new GridLayout(6, false));
//			setLayoutData(timeComposite, false);
			
			{
				//mShowAllGrid
				final Composite showGridComposite = new Composite(line, SWT.BORDER);
				showGridComposite.setLayout(new GridLayout(2, false));
				setLayoutData(showGridComposite, true);

				final Label label = new Label(showGridComposite, SWT.PUSH);
				label.setText("Auto-Key");

				final Button button = new Button(showGridComposite, SWT.CHECK);
				mAutoKeyButton = button;
				
				button.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						mTimeCanvasView.setAutoKey(button.getSelection());
					}
				});
			}
			
			{
				final Composite showComposite = new Composite(line, SWT.BORDER); 
				showComposite.setLayout(new GridLayout(2, false));
				setLayoutData(showComposite, true);
				
				final Label label = new Label(showComposite, SWT.PUSH); 
				label.setText("Speed"); 
				
				mMotionSpeed = new Spinner(showComposite, SWT.BORDER);
				mMotionSpeed.setMaximum(Integer.MAX_VALUE); 
				mMotionSpeed.setMinimum(0); 
				mMotionSpeed.setDigits(2); 
				mMotionSpeed.addModifyListener(this); 
			}
			//life, period, progress, loop mode, interpolate
			{
				//life
				final Composite lifeComposite = new Composite(line, SWT.BORDER);
				lifeComposite.setLayout(new GridLayout(2, false));
				setLayoutData(lifeComposite, true);
				
				final Label label = new Label(lifeComposite, SWT.PUSH);
				label.setText("Start");
				
				final Spinner spinner = new Spinner(lifeComposite, SWT.BORDER);
				
				spinner.setMinimum(-1); 
				spinner.setMaximum(Integer.MAX_VALUE); 
				
				mLoopStartSpinner = spinner;
				spinner.addModifyListener(this);
			}
			{
				//period
				final Composite periodComposite = new Composite(line, SWT.BORDER);
				periodComposite.setLayout(new GridLayout(2, false));
				setLayoutData(periodComposite, true);
				
				final Label label = new Label(periodComposite, SWT.PUSH);
				label.setText("End");
				
				final Spinner spinner = new Spinner(periodComposite, SWT.BORDER);
				
				spinner.setMinimum(0); 
				spinner.setMaximum(Integer.MAX_VALUE); 
				
				mLoopEndSpinner = spinner;
				spinner.addModifyListener(this);
			}
			{
				//progress
				final Composite progressComposite = new Composite(line, SWT.BORDER);
				progressComposite.setLayout(new GridLayout(2, false));
				setLayoutData(progressComposite, true);
				
				final Label label = new Label(progressComposite, SWT.PUSH);
				label.setText("Progress");
				
				final Spinner spinner = new Spinner(progressComposite, SWT.BORDER);
				
				spinner.setMinimum(0); 
				spinner.setMaximum(Integer.MAX_VALUE); 
				
				mProgressSpinner = spinner;
				spinner.addModifyListener(this);
			}
			{
				//loop mode
				final Composite loopComposite = new Composite(line, SWT.BORDER);
				loopComposite.setLayout(new GridLayout(2, false));
				setLayoutData(loopComposite, true);
				
				final Label label = new Label(loopComposite, SWT.PUSH);
				label.setText("LoopMode");
				
				final CCombo combo = new CCombo(loopComposite, SWT.BORDER);
				combo.setText("Loop");
				combo.setItems(new String[]{"Loop","Reloop","Stay", "Endless"});
				
				mLoopCombo = combo;
				combo.addModifyListener(this);
			}
			{
				//interpolate
				final Composite interComposite = new Composite(line, SWT.BORDER);
				interComposite.setLayout(new GridLayout(2, false));
				setLayoutData(interComposite, true);
				
				final Label label = new Label(interComposite, SWT.PUSH);
				label.setText("InterType");
				
				final CCombo combo = new CCombo(interComposite, SWT.BORDER);
				combo.setText("Linear");
				combo.setItems(EnumHelper.toStringNoT(InterType.class));
				
				mInterCombo = combo;
				combo.addModifyListener(this);
			} 
		} 
		
		mTimeCanvasView = new TimeCanvasView(); 
		final Canvas canvas = mTimeCanvasView.create(rootPanel); 
		setLayoutData(canvas, false); 
		
		run();
		
		return rootPanel;
	}
	
	public void modifyText(ModifyEvent e) {
		if(mBindMotionNode != null) {
			if(e.widget == mLoopStartSpinner) {
				mBindMotionNode.setLoopStart( mLoopStartSpinner.getSelection() ); 
			} else if(e.widget == mLoopEndSpinner) { 
				mBindMotionNode.setLoopEnd( mLoopEndSpinner.getSelection() ); 
			} else if(e.widget == mProgressSpinner) {
				mBindMotionNode.setProgress( mProgressSpinner.getSelection() ); 
			} else if(e.widget == mLoopCombo) {
				mBindMotionNode.setLoopMode(EnumHelper.fromString(LoopMode.class, mLoopCombo.getText()));
			}  else if(e.widget == mInterCombo) {
				mBindMotionNode.setInterType(EnumHelper.fromString(InterType.class, mInterCombo.getText()));
			} else if(e.widget == mMotionSpeed) {
				mBindMotionNode.setMotionSpeed(mMotionSpeed.getSelection()/100f);
			}
		}
	} 
	
	public void onSelect(){
		super.onSelect();
	}
	
	public void onDeselect(){ 
		super.onDeselect();
		
		TimeData.setSelectTimeData(null);
	} 
	
	//add delete 
	//move 
} 
