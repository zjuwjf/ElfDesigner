package com.ielfgame.stupidGame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import com.ielfgame.stupidGame.Actions.Type;
import com.ielfgame.stupidGame.animation.Animate;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.modifier.INodeModifier;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.ElfNode.IIterateChilds;
import elfEngine.basic.node.particle.ParticleNode;

public class ModifiersEditWorkSpaceTab extends AbstractWorkSpaceTab implements ActionDataConfig {

	private Group mTabControlPanel;
	private Tree mActionTree;
	private IconCache mIconCache;

	private ElfNode mCurrentNode;
	private ActionData mCurrentActions;

	public ModifiersEditWorkSpaceTab() {
		super("Modifiers Edit");
	}

	private final void createActionBar(final Composite parent) {
		mTabControlPanel = new Group(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginHeight = gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
		mTabControlPanel.setLayout(gridLayout);
		mTabControlPanel.setLayoutData(new GridData(GridData.FILL_VERTICAL | GridData.VERTICAL_ALIGN_BEGINNING));
		mTabControlPanel.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.character == 'b') {
					asStartValue();
				} else if (e.character == 'e') {
					asEndValue();
				}  else if (e.character == 'a') {
					setSelectAll();
				}  else if (e.character == 'n') {
					setSelectNone();
				} 
			}
		});
		
		ToolBar toolBar0 = new ToolBar(mTabControlPanel, SWT.FLAT);
		// play tool item
		final ToolItem playItem = new ToolItem(toolBar0, SWT.PUSH);
		playItem.setToolTipText("Play");
		playItem.setText("  Play  ");
		playItem.setImage(mIconCache.stockImages[mIconCache.iconPlay]);

		// pause tool item
		final ToolItem pauseItem = new ToolItem(toolBar0, SWT.PUSH);
		pauseItem.setToolTipText("Pause");
		pauseItem.setText("  Pause ");
		pauseItem.setImage(mIconCache.stockImages[mIconCache.iconPause]);

		Label separator = new Label(mTabControlPanel, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 1;
		separator.setLayoutData(gridData);
		
		ToolBar toolBar1 = new ToolBar(mTabControlPanel, SWT.FLAT);
		final ToolItem addItem = new ToolItem(toolBar1, SWT.PUSH);
		addItem.setToolTipText("Add a new modifier");
		addItem.setText("  Add  ");
		addItem.setImage(mIconCache.stockImages[mIconCache.iconAddAction]);

		final ToolItem deleteItem = new ToolItem(toolBar1, SWT.PUSH);
		deleteItem.setToolTipText("Delete modifiers");
		deleteItem.setText("Delete");
		deleteItem.setImage(mIconCache.stockImages[mIconCache.iconRemoveAction]);

		separator = new Label(mTabControlPanel, SWT.SEPARATOR | SWT.HORIZONTAL);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 1;
		separator.setLayoutData(gridData);

		ToolBar toolBar2 = new ToolBar(mTabControlPanel, SWT.FLAT);
		final ToolItem asStartValueItem = new ToolItem(toolBar2, SWT.PUSH);
		asStartValueItem.setToolTipText("As start value");
		asStartValueItem.setText("Value1");
		asStartValueItem.setImage(mIconCache.stockImages[mIconCache.iconValue1]);

		final ToolItem asEndValueItem = new ToolItem(toolBar2, SWT.PUSH);
		asEndValueItem.setToolTipText("As End Value");
		asEndValueItem.setText("Value2");
		asEndValueItem.setImage(mIconCache.stockImages[mIconCache.iconValue2]);
		
		separator = new Label(mTabControlPanel, SWT.SEPARATOR | SWT.HORIZONTAL);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 1;
		separator.setLayoutData(gridData);

		ToolBar toolBar3 = new ToolBar(mTabControlPanel, SWT.FLAT);
		final ToolItem selectAllItem = new ToolItem(toolBar3, SWT.PUSH);
		selectAllItem.setToolTipText("All selected");
		selectAllItem.setText("   All   ");
		selectAllItem.setImage(mIconCache.stockImages[mIconCache.iconSelectAll]);

		final ToolItem deselectAllItem = new ToolItem(toolBar3, SWT.PUSH);
		deselectAllItem.setToolTipText("None selected");
		deselectAllItem.setText("  None ");
		deselectAllItem.setImage(mIconCache.stockImages[mIconCache.iconDeselectAll]);

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
					} else if (event.widget == addItem) {
						addANewTableItem();
					} else if (event.widget == deleteItem) {
						deleteTreeItems(true);
					} else if (event.widget == asStartValueItem) {
						asStartValue();
					} else if (event.widget == asEndValueItem) {
						asEndValue();
					} else if(event.widget == selectAllItem){
						setSelectAll();
					} else if(event.widget == deselectAllItem){
						setSelectNone();
					} 
				}
					break;
				}
			}
		};

		playItem.addListener(SWT.Selection, toolBarListener);
		pauseItem.addListener(SWT.Selection, toolBarListener);
		addItem.addListener(SWT.Selection, toolBarListener);
		deleteItem.addListener(SWT.Selection, toolBarListener);
		playItem.setEnabled(false);
		asStartValueItem.addListener(SWT.Selection, toolBarListener);
		asEndValueItem.addListener(SWT.Selection, toolBarListener);
		selectAllItem.addListener(SWT.Selection, toolBarListener);
		deselectAllItem.addListener(SWT.Selection, toolBarListener);
	}

	void addANewTableItem() {
		if(mCurrentNode == null){
			return ;
		}
		
		final TreeItem focusItem = getFocusControlItem();
		final ActionData data = new ActionData(false);
		final TreeItem item;
		
		if (focusItem == null) {
			item = new TreeItem(mActionTree, SWT.NONE);
			data.mFather = mCurrentActions;
			data.addToFather(Integer.MAX_VALUE);
			
		} else if (getContext(focusItem, INDEX_TYPE).equalsIgnoreCase(Actions.Type.Sequence.toString())
				|| getContext(focusItem, INDEX_TYPE).equalsIgnoreCase(Actions.Type.Parallel.toString())) {
			item = new TreeItem(focusItem, SWT.NONE, 0);
			focusItem.setExpanded(true);
			data.mFather = (ActionData)focusItem.getData(ACTION_DATA_KEY);
			data.addToFather(0);
		} else {
			TreeItem grandfather = focusItem.getParentItem();
			if (grandfather == null) {
				int index = mActionTree.indexOf(focusItem);
				item = new TreeItem(mActionTree, SWT.NONE, index + 1);
				
				data.mFather = mCurrentActions;
				data.addToFather(index + 1);
			} else {
				int index = grandfather.indexOf(focusItem);
				item = new TreeItem(grandfather, SWT.NONE, index + 1);
				focusItem.setExpanded(true);
				
				data.mFather = (ActionData)grandfather.getData(ACTION_DATA_KEY);;
				data.addToFather(index + 1);
			}
		}
		
		initItem(item, data, false);
		
		mActionTree.pack();
		mActionTree.layout();
	}

	private final static String CONTROL_TO_FIND_ITEM_KEY = "find_item_key";
	private final static String ACTION_DATA_KEY = ActionDataConfig.ACTIONDATA_KEY;

	private final Composite createActionTable(Composite parent) {
		mActionTree = new Tree(parent, 
				SWT.NONE | SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE);
		final GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		mActionTree.setLayoutData(data);

		mActionTree.setHeaderVisible(true);
		for (int i = 0; i < sTableLabels.length; i++) {
			final TreeColumn colum = new TreeColumn(mActionTree, SWT.NONE);
			colum.setText(sTableLabels[i]);
			final int width = 130;
			colum.setWidth(width);
		}
		
		mActionTree.addListener(SWT.CHECK, new Listener() {
			public void handleEvent(Event event) {
				updateTreeChecks();
			}
		}); 
		
		return mActionTree;
	}
	
	
	
	void initItem(final TreeItem item, final ActionData data, boolean deep){
		TreeEditor editor;
		
		data.mItem = item;
		item.setData(ACTION_DATA_KEY, data);
		item.setChecked(data.mChecked);

		editor = new TreeEditor(mActionTree);
		editor.grabHorizontal = true;
		final Text nameText = new Text(mActionTree, SWT.NONE | SWT.BORDER);
		nameText.setData(CONTROL_TO_FIND_ITEM_KEY, item);
		editor.setEditor(nameText, item, (INDEX_NAME));
		item.setData(sTableLabels[INDEX_NAME], nameText);
		item.setData(sEditorKeys[INDEX_NAME], editor);
		nameText.setText(data.mStrings[INDEX_NAME]);

		editor = new TreeEditor(mActionTree);
		editor.grabHorizontal = true;
		final CCombo typeCombo = new CCombo(mActionTree, SWT.NONE | SWT.BORDER);
		typeCombo.setData(CONTROL_TO_FIND_ITEM_KEY, item);
		typeCombo.setItems(Type.toStrings());
		editor.setEditor(typeCombo, item, (INDEX_TYPE));
		item.setData(sTableLabels[INDEX_TYPE], typeCombo);
		item.setData(sEditorKeys[INDEX_TYPE], editor);
		typeCombo.setText(data.mStrings[INDEX_TYPE]);

		editor = new TreeEditor(mActionTree);
		editor.grabHorizontal = true;
		final Text begValue = new Text(mActionTree, SWT.NONE | SWT.BORDER);
		begValue.setData(CONTROL_TO_FIND_ITEM_KEY, item);
		begValue.setData(CONTROL_TO_FIND_ITEM_KEY, item);
		editor.setEditor(begValue, item, (INDEX_START_VALUE));
		item.setData(sTableLabels[INDEX_START_VALUE], begValue);
		item.setData(sEditorKeys[INDEX_START_VALUE], editor);
		setTextDropSource(begValue);
		begValue.setText(data.mStrings[INDEX_START_VALUE]);

		editor = new TreeEditor(mActionTree);
		editor.grabHorizontal = true;
		final Text endValue = new Text(mActionTree, SWT.NONE | SWT.BORDER);
		endValue.setData(CONTROL_TO_FIND_ITEM_KEY, item);
		editor.setEditor(endValue, item, (INDEX_END_VALUE));
		item.setData(sTableLabels[INDEX_END_VALUE], endValue);
		item.setData(sEditorKeys[INDEX_END_VALUE], editor);
		setTextDropSource(endValue);
		endValue.setText(data.mStrings[INDEX_END_VALUE]);

		editor = new TreeEditor(mActionTree);
		editor.grabHorizontal = true;
		final CCombo modeCombo = new CCombo(mActionTree, SWT.NONE | SWT.BORDER);
		modeCombo.setData(CONTROL_TO_FIND_ITEM_KEY, item);
		modeCombo.setItems(Actions.Mode.toStrings());
		editor.setEditor(modeCombo, item, (INDEX_MODE));
		item.setData(sTableLabels[INDEX_MODE], modeCombo);
		item.setData(sEditorKeys[INDEX_MODE], editor);
		modeCombo.setText(data.mStrings[INDEX_MODE]);

//		editor = new TreeEditor(mActionTree);
//		editor.grabHorizontal = true;
//		final Spinner begTime = new Spinner(mActionTree, SWT.BORDER);
//		begTime.setData(CONTROL_TO_FIND_ITEM_KEY, item);
//		addModifierTextListener(begTime);
//		begTime.setDigits(2);
//		begTime.setPageIncrement(1);
//		begTime.setMaximum(Integer.MAX_VALUE);
//		editor.setEditor(begTime, item, (INDEX_START_TIME));
//		item.setData(sTableLabels[INDEX_START_TIME], begTime);
//		item.setData(sEditorKeys[INDEX_START_TIME], editor);
//		try{
//			begTime.setSelection(Math.round( Float.valueOf( data.mStrings[INDEX_START_TIME] )*100 ));
//		} catch (Exception e) {
//		}
		
		editor = new TreeEditor(mActionTree);
		editor.grabHorizontal = true;
		final Spinner endTime = new Spinner(mActionTree, SWT.BORDER);
		endTime.setData(CONTROL_TO_FIND_ITEM_KEY, item);
		endTime.setSelection(1000);
		endTime.setMinimum(-100);
		endTime.setMaximum(Integer.MAX_VALUE);
		endTime.setDigits(2);
		endTime.setPageIncrement(1);
		editor.setEditor(endTime, item, (INDEX_END_TIME));
		item.setData(sTableLabels[INDEX_END_TIME], endTime);
		item.setData(sEditorKeys[INDEX_END_TIME], editor);
		try{
			endTime.setSelection(Math.round( Float.valueOf( data.mStrings[INDEX_END_TIME] )*100 ));
		} catch (Exception e) {
		}
		

		editor = new TreeEditor(mActionTree);
		editor.grabHorizontal = true;
		final Spinner periodTime = new Spinner(mActionTree, SWT.BORDER);
		periodTime.setData(CONTROL_TO_FIND_ITEM_KEY, item);
		periodTime.setSelection(1000);
		periodTime.setDigits(2);
		periodTime.setPageIncrement(1);
		periodTime.setMaximum(Integer.MAX_VALUE);
		editor.setEditor(periodTime, item, (INDEX_PERIOD));
		item.setData(sTableLabels[INDEX_PERIOD], periodTime);
		item.setData(sEditorKeys[INDEX_PERIOD], editor);
		try{
			periodTime.setSelection(Math.round( Float.valueOf( data.mStrings[INDEX_PERIOD] )*100 ));
		} catch (Exception e) {
		}
		

		editor = new TreeEditor(mActionTree);
		editor.grabHorizontal = true;
		final CCombo interCombo = new CCombo(mActionTree, SWT.NONE | SWT.BORDER);
		interCombo.setData(CONTROL_TO_FIND_ITEM_KEY, item);
		interCombo.setItems(Actions.Inter.toStrings());
		editor.setEditor(interCombo, item, (INDEX_INTERPOLOR));
		item.setData(sTableLabels[INDEX_INTERPOLOR], interCombo);
		item.setData(sEditorKeys[INDEX_INTERPOLOR], editor);
		interCombo.setText(data.mStrings[INDEX_INTERPOLOR]);
		
		addModifierTextListener(nameText);
		addModifierTextListener(typeCombo);
		addModifierTextListener(begValue);
		addModifierTextListener(endValue);
		addModifierTextListener(modeCombo);
		addModifierTextListener(endTime);
		addModifierTextListener(periodTime);
		addModifierTextListener(interCombo);
		
		item.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				for (int i = 0; i < sTableLabels.length; i++) {
					final ControlEditor controlEditor = (ControlEditor) item.getData(sEditorKeys[i]);
					controlEditor.dispose();
					final Control control = (Control) item.getData(sTableLabels[i]);
					control.dispose();
				}
			}
		}); 
		
		if(deep){
			for(ActionData data2 : data.mChildsList){
				final TreeItem item2 = new TreeItem(item, SWT.NONE);
				initItem(item2, data2, deep);
			}
		}
		item.setExpanded(true); 
	}

	

	public void update() {
		final ElfNode node = PowerMan.getSingleton(DataModel.class).getSelectNode();
		
		if (node != mCurrentNode) {		
			
			updateTreeChecks();
			
			mCurrentNode = node;
			
			if (mCurrentNode != null) {
				ActionData actions = (ActionData) mCurrentNode.getData(ACTION_DATA_KEY);
				if (actions == null) {
					actions = new ActionData(true);
					mCurrentNode.setData(ACTION_DATA_KEY, actions);
				}
				mCurrentActions = actions;
			} else {
				mCurrentActions = null;
				return ;
			}
						
			setSelectAll();
			deleteTreeItems(false);
						
			for(ActionData newData : mCurrentActions.mChildsList){
				final TreeItem newItem = new TreeItem(mActionTree, SWT.NONE);
				initItem(newItem, newData, true);
			}
			
			mActionTree.pack();
			mActionTree.layout();
		}
	}
	
	

	private final String[] getItemTexts(final TreeItem item) {
		final ActionData data = (ActionData) item.getData(ACTION_DATA_KEY);
		
		final String[] ret = data.mStrings;
		for (int i = 0; i < ret.length; i++) { 
			switch (i) {
			case INDEX_NAME:
				final Text name = ((Text) item.getData(sTableLabels[i]));
				if(name!=null){
					ret[i] = name.getText();
				}
				break;
			case INDEX_TYPE:
				final CCombo type = ((CCombo) item.getData(sTableLabels[i]));
				if(type != null){
					ret[i] = type.getText();
				}
				break;
			case INDEX_START_VALUE:
				final Text startValue = ((Text) item.getData(sTableLabels[i]));
				if(startValue != null){
					ret[i] = startValue.getText();
				}
				break;
			case INDEX_END_VALUE:
				final Text endValue = ((Text) item.getData(sTableLabels[i]));
				if(endValue != null){
					ret[i] = endValue.getText();
				}
				break;
			case INDEX_MODE:
				final CCombo mode = ((CCombo) item.getData(sTableLabels[i]));
				if(mode != null){
					ret[i] = mode.getText();
				}
				break;
//			case INDEX_START_TIME:
//				final Spinner startTime = ((Spinner) item.getData(sTableLabels[i]));
//				if(startTime != null){
//					ret[i] = startTime.getText();
//				} 
//				break;
			case INDEX_END_TIME:
				final Spinner endTime = ((Spinner) item.getData(sTableLabels[i]));
				if(endTime != null){
					ret[i] = endTime.getText();
				} 
				break;
			case INDEX_PERIOD:
				final Spinner period = ((Spinner) item.getData(sTableLabels[i]));
				if(period != null){
					ret[i] = period.getText();
				} 
				break;
			case INDEX_INTERPOLOR:
				final CCombo inter = ((CCombo) item.getData(sTableLabels[i]));
				if(inter != null){
					ret[i] = inter.getText();
				} 
				break;
			}
		}
		return ret;
	}

	final IIterateChilds mIIterateChilds = new IIterateChilds() {
		public boolean iterate(ElfNode node) { 
			final ActionData actions = (ActionData) node.getData(ACTION_DATA_KEY); 
			if (actions != null) { 
				for (ActionData action : actions.mChildsList) {
					if (action.mChecked) { 
						final Point error = new Point(0, 0);
						final INodeModifier modifier = action.createModifier(error);
						if (modifier != null) {
							node.setUseModifier(true);
							node.addModifier(modifier);
						} else if (node == mCurrentNode) {
							final TreeItem item = getNextIndex(action.mItem, error); 
							setContextColor(item, error.x, ColorFactory.RED);
						}
					}
				}
			} 
			return false;
		}
	};
	
	private final void play() {
		if (mCurrentNode != null) {
			mCurrentNode.clearModifierDeep();
			
			updateTreeChecks();
			
			mCurrentNode.iterateChildsDeep(mIIterateChilds);
			
			mIIterateChilds.iterate(mCurrentNode);
		}
	}

	private final void pause() {
		if (mCurrentNode != null) {			
			mCurrentNode.clearModifierDeep();
		}
	}

	private final void asStartValue() {
		final TreeItem item = getFocusControlItem();
		if(item != null){
			final String text = getContext(item, INDEX_TYPE);
			final Type type = Type.fromString(text);
			if(type != null){
				switch (type) {
				case Alpha:
					setContext(item, INDEX_START_VALUE, ""+mCurrentNode.getAlpha());break;
				case Move:
					setContext(item, INDEX_START_VALUE, ""+mCurrentNode.getPosition().x+","+mCurrentNode.getPosition().y);break;
				case Rotate:
					setContext(item, INDEX_START_VALUE, ""+mCurrentNode.getRotate());break;
				case Scale:
					setContext(item, INDEX_START_VALUE, ""+mCurrentNode.getScale().x+","+mCurrentNode.getScale().y);break;
				case EmitterMove:
					if(mCurrentNode instanceof ParticleNode){
						setContext(item, INDEX_START_VALUE, ""+mCurrentNode.getPosition().x+","+mCurrentNode.getPosition().y);break;
					} 
					break;
				case Animate:
				case Empty:
				case Parallel:
				case Sequence:
				}
			}
		}
	}

	private final void asEndValue() {
		final TreeItem item = getFocusControlItem();
		if(item != null){
			final String text = getContext(item, INDEX_TYPE);
			final Type type = Type.fromString(text);
			if(type != null){
				switch (type) {
				case Alpha:
					setContext(item, INDEX_END_VALUE, ""+mCurrentNode.getAlpha());break;
				case Move:
					final String old = getContext(item, INDEX_END_VALUE);
					setContext(item, INDEX_END_VALUE, old + "("+mCurrentNode.getPosition().x+","+mCurrentNode.getPosition().y+")");break;
				case Rotate:
					setContext(item, INDEX_END_VALUE, ""+mCurrentNode.getRotate());break;
				case Scale:
					setContext(item, INDEX_END_VALUE, ""+mCurrentNode.getScale().x+","+mCurrentNode.getScale().y);break;
				case EmitterMove:
					if(mCurrentNode instanceof ParticleNode){
						final String old2 = getContext(item, INDEX_END_VALUE);
						setContext(item, INDEX_END_VALUE, old2 + "("+mCurrentNode.getPosition().x+","+mCurrentNode.getPosition().y+")");
					} 
					break;
				case Animate:
				case Empty:
				case Parallel:
				case Sequence:
				}
			}
		}
	}
	
	
	TreeItem getNextIndex(final TreeItem father, Point index){
		if(index.y == 0){
			return father;
		} else {
			final TreeItem [] childs = father.getItems(); 
			for(TreeItem item : childs){ 
				index.y--; 
				final TreeItem ret = getNextIndex(item, index); 
				if(ret != null){ 
					return ret; 
				} 
			} 
		} 
		
		return null;
	} 
	
	//
	private DropTarget setTextDropSource(final Text text) {
		DropTarget dropTarget = new DropTarget(text, DND.DROP_MOVE | DND.DROP_COPY);
		dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dropTarget.addDropListener(new DropTargetAdapter() {
			public void dragOver(DropTargetEvent event) {
				event.feedback |= DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL;
			}

			public void drop(DropTargetEvent event) {
				if (TextTransfer.getInstance().isSupportedType(event.currentDataType)) {
					final String drops = (String) event.data;
					if (drops == null) {
						event.detail = DND.DROP_NONE;
						return;
					}

					final Animate animate = PowerMan.getSingleton(DataModel.class).getAnimate(drops);
					if (animate != null) {
						final TreeItem item = (TreeItem) text.getData(CONTROL_TO_FIND_ITEM_KEY);					
						setContext(item, INDEX_START_VALUE, drops);
						setContext(item, INDEX_PERIOD, ""+animate.getAnimateTime());
					}
				}
			}
		});
		return dropTarget;
	}
	
	
	private Color mTextInitColor;
	private final void addModifierTextListener(final Control control) {
		final TreeItem item = (TreeItem)control.getData(CONTROL_TO_FIND_ITEM_KEY);

		if (control instanceof CCombo) {
			final CCombo composite = (CCombo) control;
			final Color color = composite.getBackground();
			composite.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					composite.setBackground(color);
					afterModifierText(composite, item);
				}
			});
		} else if (control instanceof Text) {
			final Text composite = (Text) control;
			mTextInitColor = composite.getBackground();
			composite.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					composite.setBackground(mTextInitColor);
					afterModifierText(composite, item);
				}
			});
		} else if (control instanceof Spinner) {
			final Spinner composite = (Spinner) control;
			final Color color = composite.getBackground();
			composite.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					composite.setBackground(color);
					afterModifierText(composite, item);
				}
			});
		} else {
			Redirect.errPrintln("error addModifierTextListener!");
		}  
	}
	
	void afterModifierText(final Control control, final TreeItem item){
		//
		if(control == item.getData(sTableLabels[INDEX_TYPE])){
			final CCombo cCombo = (CCombo)control;
			final String text = cCombo.getText();
			int mask = 0;
			if(text.equalsIgnoreCase(Type.Parallel.toString())){
				mask |= 1 << INDEX_PERIOD;
				mask |= 1 << INDEX_INTERPOLOR;
				mask |= 1 << INDEX_START_VALUE;
				mask |= 1 << INDEX_END_VALUE;
				mask |= 1 << INDEX_MODE;
			} else if(text.equalsIgnoreCase(Type.Sequence.toString())){
				mask |= 1 << INDEX_PERIOD;
				mask |= 1 << INDEX_INTERPOLOR;
				mask |= 1 << INDEX_START_VALUE;
				mask |= 1 << INDEX_END_VALUE;
				mask |= 1 << INDEX_MODE;
			} else if(text.equalsIgnoreCase(Type.Empty.toString())){
				mask |= 1 << INDEX_PERIOD;
				mask |= 1 << INDEX_INTERPOLOR;
				mask |= 1 << INDEX_START_VALUE;
				mask |= 1 << INDEX_MODE;
				mask |= 1 << INDEX_END_VALUE;
			} else if(text.equalsIgnoreCase(Type.Animate.toString())){
				mask |= 1 << INDEX_PERIOD;
				mask |= 1 << INDEX_END_VALUE;
			} 
			
			for(int i=0; i<sTableLabels.length; i++){
				if((mask & (1<<i)) != 0){
					setContextEnable(item, i, false);
				} else {
					setContextEnable(item, i, true);
				}
			}
			
			setContextColor(item, INDEX_START_VALUE, mTextInitColor);
			setContextColor(item, INDEX_END_VALUE, mTextInitColor);
		}
		
		getItemTexts(item);
		//check name
		final TreeItem parent = item.getParentItem();
		final TreeItem [] items;
		if(parent == null){
			items = mActionTree.getItems();
		} else {
			items = parent.getItems();
		} 
		
		for(TreeItem item0 : items){
			final Text text = ((Text)item0.getData(sTableLabels[INDEX_NAME]));
			text.setBackground(mTextInitColor);
			for(TreeItem item1 : items){
				if(item0 != item1){
					final String name0 = getContext(item0, INDEX_NAME);
					final String name1 = getContext(item1, INDEX_NAME);
					if(name0.equalsIgnoreCase(name1)){
						text.setBackground(ColorFactory.YELLOW);
						break;
					}
				}
			}
		}
	}

	public Composite createTab(CTabFolder parent) {
		final Composite composite = new Composite(parent, SWT.NONE | SWT.BORDER | SWT.SEPARATOR);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		composite.setLayout(gridLayout);

		mIconCache = PowerMan.getSingleton(MainDesigner.class).mIconCache;

		createActionBar(composite);
		createActionTable(composite);

		run();
		return composite;
	}
	
	TreeItem getFocusControlItem() {
		final TreeItem[] items = mActionTree.getItems();
		if (items != null && items.length > 0) {
			for (TreeItem item : items) {
				TreeItem ret = getFocusControlItem(item);
				if (ret != null) {
					return ret;
				}
			}
		}
		return null;
	}

	TreeItem getFocusControlItem(TreeItem father) {
		for (int i = 0; i < sTableLabels.length; i++) {
			final Control control = (Control) father.getData(sTableLabels[i]);
			if (!control.isDisposed() && control.isFocusControl()) {
				return father;
			}
		}
		final TreeItem[] items = father.getItems();
		if (items != null && items.length > 0) {
			for (TreeItem item : items) {
				TreeItem ret = getFocusControlItem(item);
				if (ret != null) {
					return ret;
				}
			}
		}
		return null;
	}
	
	void updateTreeChecks(){
		final TreeItem [] items = mActionTree.getItems();
		for(TreeItem i : items){
			updateChecks(i);
		}
	}
	
	void updateChecks(TreeItem item){
		final ActionData data = (ActionData)item.getData(ACTION_DATA_KEY);
		data.mChecked = item.getChecked();
		final TreeItem [] items = item.getItems();
		for(TreeItem i : items){
			updateChecks(i);
		}
	}

	private final void deleteTreeItems(boolean clean) {
		TreeItem[] items = mActionTree.getItems();
		for (TreeItem item : items) {
			deleteRecursion(item, clean);
		}
		mActionTree.pack(true);
		mActionTree.layout();
	}

	void deleteRecursion(final TreeItem item, final boolean clean) {
		if (item.getChecked()) { 
			if(clean) {
				final ActionData data = (ActionData)item.getData(ACTION_DATA_KEY);
				data.removeFromFather();
			}
				
			item.dispose();
		} else {
			TreeItem[] items = item.getItems();
			for (TreeItem i : items) {
				deleteRecursion(i, clean);
			}
		}
	}
	
	String getContext(TreeItem item, int index) {
		final Object object = item.getData(sTableLabels[index]);
		if (object instanceof Text) {
			return ((Text) object).getText();
		} else if (object instanceof CCombo) {
			return ((CCombo) object).getText();
		} else if (object instanceof Spinner) {
			return ((Spinner) object).getText();
		}
		return "";
	}

	void setContext(TreeItem item, int index, String text) {
		final Object object = item.getData(sTableLabels[index]);
		if (object instanceof Text) {
			try {
				((Text) object).setText(text);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (object instanceof CCombo) {
			try {
				((CCombo) object).setText(text);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (object instanceof Spinner) {
			try {
				((Spinner) object).setSelection(Math.round(Float.valueOf(text) * 100));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	void setContextColor(TreeItem item, int index, Color color){
		final Control object = (Control)item.getData(sTableLabels[index]);
		object.setBackground(color);
	}
	
	void setContextEnable(TreeItem item, int index, boolean enable){
		final Control object = (Control)item.getData(sTableLabels[index]);
		object.setEnabled(enable);
	}

	void setSelectAll() {
		TreeItem[] items = mActionTree.getItems();
		for (TreeItem item : items) {
			item.setChecked(true);
		}
	}

	void setSelectNone() {
		TreeItem[] items = mActionTree.getItems();
		for (TreeItem item : items) {
			item.setChecked(false);
		} 
	} 
}
