package elfEngine.basic.node.nodeAnimate.flash;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.ielfgame.stupidGame.IconCache;
import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.ResJudge;
import com.ielfgame.stupidGame.power.PowerMan;

public class FlashWorkSpaceBar implements ModifyListener{
	/***
	 * Play, Pause, FPS, Length, Auto-Key
	 */
	protected Spinner mFPSSpinner, mLengthSpinner;
	protected Button mAutoKeyButton, mTimeBarAlignTop, mShowAllGrid;
	protected ToolItem mPalyItem, mPauseItem;

	public Composite createComposite(final Composite parent) {
		final Composite line = new Composite(parent, SWT.NONE);
		line.setLayout(new GridLayout(10, false));
		setLayoutData(line, true);
		line.setOrientation(SWT.HORIZONTAL);

		{
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

			playItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					playItem.setEnabled(false);
					pauseItem.setEnabled(true);
				}
			});
			pauseItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					pauseItem.setEnabled(false);
					playItem.setEnabled(true);
				}
			});
			mPalyItem = playItem;
			mPauseItem = pauseItem;
		}

		{
			// final Composite timeComposite = new Composite(line0, SWT.NONE);
			// timeComposite.setLayout(new GridLayout(6, false));
			// setLayoutData(timeComposite, false);
			{
				final Composite fpsComposite = new Composite(line, SWT.BORDER);
				fpsComposite.setLayout(new GridLayout(2, false));
				setLayoutData(fpsComposite, true);

				final Label label = new Label(fpsComposite, SWT.PUSH);
				label.setText("FPS");

				mFPSSpinner = new Spinner(fpsComposite, SWT.BORDER);
				mFPSSpinner.setMaximum(100);
				mFPSSpinner.setMinimum(1);
				mFPSSpinner.addModifyListener(this);
			}
			{
				// length
				final Composite lengthComposite = new Composite(line, SWT.BORDER);
				lengthComposite.setLayout(new GridLayout(2, false));
				setLayoutData(lengthComposite, true);

				final Label label = new Label(lengthComposite, SWT.PUSH);
				label.setText("Length");

				final Spinner spinner = new Spinner(lengthComposite, SWT.BORDER);

				spinner.setMinimum(1);
				spinner.setMaximum(Integer.MAX_VALUE);

				mLengthSpinner = spinner;
				spinner.addModifyListener(this);
			}
			{
				// auto-key
				final Composite autoKeyComposite = new Composite(line, SWT.BORDER);
				autoKeyComposite.setLayout(new GridLayout(2, false));
				setLayoutData(autoKeyComposite, true);

				final Label label = new Label(autoKeyComposite, SWT.PUSH);
				label.setText("Auto-Key");

				final Button button = new Button(autoKeyComposite, SWT.CHECK);
				
				mAutoKeyButton = button;
			}
			{
				//mTimeBarAlignTop
				final Composite timebarComposite = new Composite(line, SWT.BORDER);
				timebarComposite.setLayout(new GridLayout(2, false));
				setLayoutData(timebarComposite, true);

				final Label label = new Label(timebarComposite, SWT.PUSH);
				label.setText("TimeBar-AlignTop");

				final Button button = new Button(timebarComposite, SWT.CHECK);
				
				mTimeBarAlignTop = button;
			}
			{
				//mShowAllGrid
				final Composite showGridComposite = new Composite(line, SWT.BORDER);
				showGridComposite.setLayout(new GridLayout(2, false));
				setLayoutData(showGridComposite, true);

				final Label label = new Label(showGridComposite, SWT.PUSH);
				label.setText("Show-Grid");

				final Button button = new Button(showGridComposite, SWT.CHECK);
				mShowAllGrid = button;
				
				button.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						button.getSelection();
					}
				});
			}
		}

		return line;
	}
	
	public void modifyText(ModifyEvent e) {
	} 

	private final void setLayoutData(final Composite composite, boolean prefer) {
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		if (prefer) {
			data.heightHint = data.widthHint = SWT.DEFAULT;
			data.verticalAlignment = data.horizontalAlignment = 0;
			data.grabExcessVerticalSpace = data.grabExcessHorizontalSpace = false;
		} else {
			data.verticalAlignment = data.horizontalAlignment = SWT.FILL;
			data.grabExcessVerticalSpace = data.grabExcessHorizontalSpace = true;
		}
		composite.setLayoutData(data);
	}
}
