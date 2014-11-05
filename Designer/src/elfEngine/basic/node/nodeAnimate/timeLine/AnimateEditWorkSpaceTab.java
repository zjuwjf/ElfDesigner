package elfEngine.basic.node.nodeAnimate.timeLine;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.ielfgame.stupidGame.AbstractWorkSpaceTab;
import com.ielfgame.stupidGame.IconCache;
import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.ResJudge;
import com.ielfgame.stupidGame.animation.Animate;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.opengl.BlendMode;

public class AnimateEditWorkSpaceTab extends AbstractWorkSpaceTab {
	public AnimateEditWorkSpaceTab() {
		super("Animate Edit");
	}

	private IconCache mIconCache;
	private Spinner mGlobalOffX, mGlobalOffY, mGlobalFrameTime;
	private Spinner mCurrentOffX, mCurrentOffY, mCurrentDelayTime, mCurrentIndex;
	private CCombo mGlobalBlendMode;
	private Text mCurrentImage;
	protected Text mGlobalName;

	public Composite createTab(CTabFolder parent) {
		final Composite composite = new Composite(parent, SWT.NONE | SWT.BORDER | SWT.SEPARATOR
		// | SWT.V_SCROLL | SWT.H_SCROLL
		);

		final GridLayout gridLayout = new GridLayout();
		composite.setLayout(gridLayout);

		mIconCache = PowerMan.getSingleton(MainDesigner.class).mIconCache;

		createActionBar(composite);
		// createActionTable(composite);

		run();
		return composite;
	}

	// private void createActionTable(Composite parent) {
	//
	// }

	public void onSelect() {
//		final AnimateEditor animateEditor = PowerMan.getSingleton(DataModel.class).getScreenNode().getAnimateEditor();
//		animateEditor.setVisible(true);
//		animateEditor.setSelect(true);
	}

	public void onDeselect() {
//		final AnimateEditor animateEditor = PowerMan.getSingleton(DataModel.class).getScreenNode().getAnimateEditor();
//		animateEditor.setVisible(false);
//		animateEditor.setSelect(false);
	}

	private void createActionBar(final Composite parent) {
		final Group topGroup = new Group(parent, SWT.NONE);
		topGroup.setText("");
		GridLayout gridLayout = new GridLayout(2, false);
		topGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		topGroup.setLayout(gridLayout);

		ToolBar toolBar = new ToolBar(topGroup, SWT.FLAT);

		// play tool item
		final ToolItem playItem = new ToolItem(toolBar, SWT.PUSH);
		playItem.setText(ResJudge.getResourceString("Play"));
		playItem.setImage(mIconCache.stockImages[mIconCache.iconPlay]);

		// pause tool item
		final ToolItem pauseItem = new ToolItem(toolBar, SWT.PUSH);
		pauseItem.setText(ResJudge.getResourceString("Pause"));
		pauseItem.setImage(mIconCache.stockImages[mIconCache.iconPause]);

		// save tool item
		final ToolItem saveItem = new ToolItem(toolBar, SWT.PUSH);
		saveItem.setText(ResJudge.getResourceString("Save"));
		saveItem.setImage(mIconCache.stockImages[mIconCache.iconSave]);

		// clear all
		final ToolItem clearItem = new ToolItem(toolBar, SWT.PUSH);
		clearItem.setText(ResJudge.getResourceString("Clear"));
		clearItem.setImage(mIconCache.stockImages[mIconCache.iconRemoveAll]);

		final Listener toolBarListener = new Listener() {
			private boolean mAanimate = true;

			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.Selection: {
					if (event.widget == playItem) {
						mAanimate = true;
						playItem.setEnabled(!mAanimate);
						pauseItem.setEnabled(mAanimate);
						play();
					} else if (event.widget == pauseItem) {
						mAanimate = false;
						playItem.setEnabled(!mAanimate);
						pauseItem.setEnabled(mAanimate);
						pause();
					} else if (event.widget == saveItem) {
						save();
					} else if (event.widget == clearItem) {
						clear();
					}
				}
					break;
				}
			}

			private void pause() {
//				final AnimateEditor animateEditor = PowerMan.getSingleton(DataModel.class).getScreenNode().getAnimateEditor();
//				animateEditor.pauseAnimate();
			}

			private void play() {
//				final AnimateEditor animateEditor = PowerMan.getSingleton(DataModel.class).getScreenNode().getAnimateEditor();
//				animateEditor.runAnimate();
			}

			private void save() {
				saveAnimate();
			}

			private void clear() {
//				final AnimateEditor animateEditor = PowerMan.getSingleton(DataModel.class).getScreenNode().getAnimateEditor();
//				animateEditor.clear();
			}
		};
		playItem.addListener(SWT.Selection, toolBarListener);
		pauseItem.addListener(SWT.Selection, toolBarListener);
		playItem.setEnabled(false);
		saveItem.addListener(SWT.Selection, toolBarListener);
		clearItem.addListener(SWT.Selection, toolBarListener);

		Composite comp = new Composite(topGroup, SWT.NONE);
		gridLayout = new GridLayout(2, false);
		gridLayout.marginLeft = 50;
		gridLayout.horizontalSpacing = 10;
		comp.setLayout(gridLayout);
		comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label labelName = new Label(comp, SWT.RIGHT);
		labelName.setText("Animate Name");

		mGlobalName = new Text(comp, SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		mGlobalName.setLayoutData(gridData);
		mGlobalName.setText("text");
		mGlobalName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
//				final AnimateEditor animateEditor = PowerMan.getSingleton(DataModel.class).getScreenNode().getAnimateEditor();
//				String value = mGlobalName.getText();
//				if (value != null) {
//					animateEditor.getAnimate().mName = value;
//				}
			}
		});

		final Composite comp2 = new Composite(parent, SWT.NONE);
		gridLayout = new GridLayout(2, false);
		gridLayout.horizontalSpacing = 10;
		comp2.setLayout(gridLayout);
		comp2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));

		Group global = new Group(comp2, SWT.NONE);
		global.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		global.setLayout(gridLayout);
		global.setText("Global");

		Composite panelLeft = new Composite(global, SWT.NONE);
		panelLeft.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		panelLeft.setLayout(gridLayout);

		Label separator = new Label(global, SWT.SEPARATOR | SWT.VERTICAL);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		separator.setLayoutData(gridData);

		Composite panelRight = new Composite(global, SWT.NONE);
		panelRight.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		panelRight.setLayout(gridLayout);

		// frame time
		Label label = new Label(panelLeft, SWT.RIGHT);
		label.setText(ResJudge.getResourceString("Frame Time"));
		mGlobalFrameTime = new Spinner(panelLeft, SWT.BORDER);
		mGlobalFrameTime.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		mGlobalFrameTime.setSelection(10);
		mGlobalFrameTime.setDigits(2);
		mGlobalFrameTime.setPageIncrement(1);
		mGlobalFrameTime.setMaximum(Integer.MAX_VALUE);
		mGlobalFrameTime.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
//				final AnimateEditor animateEditor = PowerMan.getSingleton(DataModel.class).getScreenNode().getAnimateEditor();
//				int value = mGlobalFrameTime.getSelection();
//				animateEditor.getAnimate().mGlobelFrameTime = value / 100f;
			}
		});

		label = new Label(panelLeft, SWT.RIGHT);
		label.setText(ResJudge.getResourceString("Blend Mode"));

		mGlobalBlendMode = new CCombo(panelLeft, SWT.BORDER);
		mGlobalBlendMode.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		mGlobalBlendMode.setItems(new String[] { BlendMode.ACTIVLE.toString(), BlendMode.BLEND.toString() });
		mGlobalBlendMode.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
//				final String text = mGlobalBlendMode.getText();
//				final AnimateEditor animateEditor = PowerMan.getSingleton(DataModel.class).getScreenNode().getAnimateEditor();
//				if (text.equalsIgnoreCase(BlendMode.ACTIVLE.toString())) {
//					animateEditor.getAnimate().mBlendMode = BlendMode.ACTIVLE;
//				} else {
//					animateEditor.getAnimate().mBlendMode = BlendMode.BLEND;
//				} 
			}
		});

		// off x
		label = new Label(panelRight, SWT.RIGHT);
		label.setText(ResJudge.getResourceString("Off X"));
		mGlobalOffX = new Spinner(panelRight, SWT.BORDER);
		mGlobalOffX.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		mGlobalOffX.setSelection(0);
		mGlobalOffX.setMinimum(Integer.MIN_VALUE);
		mGlobalOffX.setMaximum(Integer.MAX_VALUE);
		mGlobalOffX.setPageIncrement(1);
		mGlobalOffX.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
//				final AnimateEditor animateEditor = PowerMan.getSingleton(DataModel.class).getScreenNode().getAnimateEditor();
//				int value = mGlobalOffX.getSelection();
//				animateEditor.getAnimate().mGlobelOffX = value;
			}
		});

		// off y
		label = new Label(panelRight, SWT.RIGHT);
		label.setText(ResJudge.getResourceString("Off Y"));
		mGlobalOffY = new Spinner(panelRight, SWT.BORDER);
		mGlobalOffY.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		mGlobalOffY.setSelection(0);
		mGlobalOffY.setMinimum(Integer.MIN_VALUE);
		mGlobalOffY.setMaximum(Integer.MAX_VALUE);
		mGlobalOffY.setPageIncrement(1);
		mGlobalOffY.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
//				final AnimateEditor animateEditor = PowerMan.getSingleton(DataModel.class).getScreenNode().getAnimateEditor();
//				int value = mGlobalOffY.getSelection();
//				animateEditor.getAnimate().mGlobelOffY = value;
			}
		});

		Group current = new Group(comp2, SWT.NONE);
		current.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		current.setLayout(gridLayout);
		current.setText("Current");

		panelLeft = new Composite(current, SWT.NONE);
		panelLeft.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		panelLeft.setLayout(gridLayout);

		separator = new Label(current, SWT.SEPARATOR | SWT.VERTICAL);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		separator.setLayoutData(gridData);

		panelRight = new Composite(current, SWT.NONE);
		panelRight.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		panelRight.setLayout(gridLayout);

		// index id
		label = new Label(panelLeft, SWT.RIGHT);
		label.setText(ResJudge.getResourceString("index"));
		mCurrentIndex = new Spinner(panelLeft, SWT.BORDER);
		mCurrentIndex.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		mCurrentIndex.setSelection(0);
		mCurrentIndex.setPageIncrement(1);
		// mCurrentIndex.

		// delay time
		label = new Label(panelLeft, SWT.RIGHT);
		label.setText(ResJudge.getResourceString("Delay Time"));
		mCurrentDelayTime = new Spinner(panelLeft, SWT.BORDER);
		mCurrentDelayTime.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		mCurrentDelayTime.setSelection(10);
		mCurrentDelayTime.setDigits(2);
		mCurrentDelayTime.setMinimum(Integer.MIN_VALUE);
		mCurrentDelayTime.setMaximum(Integer.MAX_VALUE);
		mCurrentDelayTime.setPageIncrement(1);
		mCurrentDelayTime.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
//				final AnimateEditor animateEditor = PowerMan.getSingleton(DataModel.class).getScreenNode().getAnimateEditor();
//				int value = mCurrentDelayTime.getSelection();
//				final AnimateFrame frame = animateEditor.getCurrentFrame();
//				if (frame != null) {
//					frame.mFrameTimeRectify = value / 100f;
//				}
			}
		});

		// frame time
		label = new Label(panelLeft, SWT.RIGHT);
		label.setText(ResJudge.getResourceString("Picture"));
		mCurrentImage = new Text(panelLeft, SWT.BORDER);
		mCurrentImage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		mCurrentImage.setText("??");
		// mCurrentImage.

		// off x
		label = new Label(panelRight, SWT.RIGHT);
		label.setText(ResJudge.getResourceString("Off X"));
		mCurrentOffX = new Spinner(panelRight, SWT.BORDER);
		mCurrentOffX.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		mCurrentOffX.setSelection(0);
		mCurrentOffX.setMinimum(Integer.MIN_VALUE);
		mCurrentOffX.setMaximum(Integer.MAX_VALUE);
		mCurrentOffX.setPageIncrement(1);
		mCurrentOffX.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
//				final AnimateEditor animateEditor = PowerMan.getSingleton(DataModel.class).getScreenNode().getAnimateEditor();
//				int value = mCurrentOffX.getSelection();
//				final AnimateFrame frame = animateEditor.getCurrentFrame();
//				if (frame != null) {
//					frame.mOffX = value;
//				}
			}
		});

		// off y
		label = new Label(panelRight, SWT.RIGHT);
		label.setText(ResJudge.getResourceString("Off Y"));
		mCurrentOffY = new Spinner(panelRight, SWT.BORDER);
		mCurrentOffY.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		mCurrentOffY.setSelection(0);
		mCurrentOffY.setMinimum(Integer.MIN_VALUE);
		mCurrentOffY.setMaximum(Integer.MAX_VALUE);
		mCurrentOffY.setPageIncrement(1);
		mCurrentOffY.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
//				final AnimateEditor animateEditor = PowerMan.getSingleton(DataModel.class).getScreenNode().getAnimateEditor();
//				int value = mCurrentOffY.getSelection();
//				final AnimateFrame frame = animateEditor.getCurrentFrame();
//				if (frame != null) {
//					frame.mOffY = value;
//				}
			}
		});
	}

//	private AnimateFrame mCurrentFrame;

	public void update() {
//		final AnimateEditor animateEditor = PowerMan.getSingleton(DataModel.class).getScreenNode().getAnimateEditor();
//		if (isSelected()) {
//			final AnimateFrame frame = animateEditor.getCurrentFrame();
//			if (frame != null && frame != mCurrentFrame) {
//				int delay = (int) (frame.mFrameTimeRectify * 100);
//				mCurrentDelayTime.setSelection(delay);
//				final int index = animateEditor.getAnimate().mFrames.indexOf(frame);
//				mCurrentIndex.setSelection(index);
//				mCurrentImage.setText(frame.mResid);
//				mCurrentOffX.setSelection(frame.mOffX);
//				mCurrentOffY.setSelection(frame.mOffY);
//			}
//			mCurrentFrame = frame;
//		}
	}

	public void openOldAnimate(Animate animate) {
//		final AnimateEditor animateEditor = PowerMan.getSingleton(DataModel.class).getScreenNode().getAnimateEditor();
//		animateEditor.setVisible(true);
//		animateEditor.clear();
//		animateEditor.createOldAnimate(animate);
//		mGlobalName.setText(animate.mName);
//		mGlobalOffX.setSelection(animate.mGlobelOffX);
//		mGlobalOffY.setSelection(animate.mGlobelOffY);
//		mGlobalFrameTime.setSelection((int) (animate.mGlobelFrameTime * 100));
//		mGlobalBlendMode.setText(animate.mBlendMode.toString());
	}

	public void saveAnimate() {
//		//
//		final AnimateEditor animateEditor = PowerMan.getSingleton(DataModel.class).getScreenNode().getAnimateEditor();
//		final String saveName = mGlobalName.getText();
//		animateEditor.getAnimate().mName = saveName;
//
//		if (PowerMan.getSingleton(AnimationWorkSpaceTab.class).hasExited(saveName)) {
//			final MakeSurePanel makeSurePanel = new MakeSurePanel(PowerMan.getSingleton(MainDesigner.class).mShell, saveName + " already existed, Are you sure ?");
//			if (makeSurePanel.open()) {
//				PowerMan.getSingleton(AnimationWorkSpaceTab.class).save(new Animate(animateEditor.getAnimate()));
//			} else {
//				// nothing
//			}
//		} else {
//			PowerMan.getSingleton(AnimationWorkSpaceTab.class).save(new Animate(animateEditor.getAnimate()));
//		}
	}
}
