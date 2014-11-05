package com.ielfgame.stupidGame.face.basic.item;

import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.ielfgame.stupidGame.Redirect;
import com.ielfgame.stupidGame.data.ElfDataDisplay;
import com.ielfgame.stupidGame.data.IDataDisplay;
import com.ielfgame.stupidGame.data.Stringified;
import com.ielfgame.stupidGame.data.Stringified.ErrorStruct;
import com.ielfgame.stupidGame.data.TypeFactory;
import com.ielfgame.stupidGame.data.TypeFactory.ClassType;
import com.ielfgame.stupidGame.dialog.AnalysisDialog;
import com.ielfgame.stupidGame.dialog.MultiLineDialog;
import com.ielfgame.stupidGame.dialog.PopDialog;
import com.ielfgame.stupidGame.language.LanguageManager;
import com.ielfgame.stupidGame.undo.UndoRedoManager;

import elfEngine.basic.utils.EnumHelper;

public class AllFuncItem extends ATreeItem{
	
	protected TreeItem mMyItem;
	protected Tree mTree;
	protected final boolean mIsGlobal;
	protected final Class<?> mClassSelect;
	protected final Class<?> mClassType;
	protected final String mDialogTitle;
	
	protected ClassType mValueClassType;
	
	protected Method mSetFunc;
	protected Method mGetFunc;
	
	protected final String[] mDialogValues;
	
	protected Control mControl;
	
	protected long mLastModifierTime = System.currentTimeMillis();
	
	protected final int MAX_MODIFIER_TIME = 2000;
	protected final int NORMAL_MODIFIER_TIME = 500;
	
	private Object [] mStaticValueCandidates = null; 
	private Object [] mDynamicValueCandidates = null;  
	private Method mDynamicCandidateMethod = null; 
	
	protected final boolean mUseUndo;
	
	boolean isDirectModifier() { 
		return (mStaticValueCandidates != null || mDynamicCandidateMethod != null || TypeFactory.isBasicType(mClassType) );
	} 
	
	void setStaticValueCandidates(Object [] valueCandidates) { 
		mStaticValueCandidates = valueCandidates; 
		if(mStaticValueCandidates != null) { 
			assert( mStaticValueCandidates.getClass().getComponentType() == this.mClassType ); 
		} 
	} 
	
	void setDynamicValueCandidates(Object [] valueCandidates) { 
		mDynamicValueCandidates = valueCandidates; 
		if(mDynamicValueCandidates != null) { 
			assert( mDynamicValueCandidates.getClass().getComponentType() == this.mClassType ); 
		} 
		CCombo combo = (CCombo)mControl; 
		if(combo != null) { 
			final String [] oldItems = combo.getItems(); 
			
			final String [] shows = new String[mDynamicValueCandidates.length];
			for(int i=0; i<shows.length; i++) { 
				shows[i] = mDynamicValueCandidates[i].toString(); 
			} 
			
			boolean same = oldItems.length == shows.length;
			for(int i=0; same && i<shows.length; i++) { 
				if(!shows[i].equals(oldItems[i])) { 
					same = false; 
				} 
			} 
			
			if(!same) { 
				combo.setItems(shows); 
			} 
		} 
	} 
	
	public AllFuncItem(boolean global, Class<?> classSelect, Class<?> classType,
			String getFunc, String setFunc, String arrayFunc,
			String title, String[] values, boolean undo) {
		this.mIsGlobal = global;
		this.mClassSelect = classSelect;
		this.mClassType = classType;
		this.mUseUndo = undo;
		
		assert (mClassSelect != null && mClassType != null) ;
		
		try {
			this.mGetFunc = mClassSelect.getMethod(getFunc);
			this.mSetFunc = mClassSelect.getMethod(setFunc, mClassType);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		} 
		
		this.mDialogTitle = LanguageManager.get(title); 
		
		this.mDialogValues = values; 
		
		try { 
			mDynamicCandidateMethod = mClassSelect.getMethod(arrayFunc); 
			if(mDynamicCandidateMethod != null) {
				//STATIC: 8
				if((mDynamicCandidateMethod.getModifiers() & 8) == 0) {
					// not static 
				} else { 
					final Object ret = mDynamicCandidateMethod.invoke(null); 
					setStaticValueCandidates((Object[])ret); 
					mDynamicCandidateMethod = null; 
				} 
			}
		} catch (Exception e) { 
		} 
	} 

	public AllFuncItem(boolean global, Class<?> classSelect, Class<?> classType, String funcName, String[] values, boolean undo) {
		this(global, classSelect, classType, "get" + funcName, "set" + funcName, "array"+funcName, funcName, values, undo);
	}
	
	public AllFuncItem(boolean global, Class<?> classSelect, Class<?> classType, String funcName, boolean undo) {
		this(global, classSelect, classType, funcName, new String[] { funcName }, undo);
	} 
	
	private final ModifyListener mModifyListener = new ModifyListener() {
		public void modifyText(ModifyEvent e) { 
			mLastModifierTime = System.currentTimeMillis();
			final String text = getContext();
			final Object objRet;
			final ErrorStruct errorRet;
			
			if(mStaticValueCandidates != null) { 
				int index = -1;
				for(int i=0; i<mStaticValueCandidates.length; i++) {
					if(text.equals(mStaticValueCandidates[i].toString())) {
						index = i;
						break;
					} 
				} 
				//modifier
				if(index >= 0) { 
					errorRet = null; 
					objRet = mStaticValueCandidates[index];
				} else {
					errorRet = new ErrorStruct();
					errorRet.errorInfo = "Unfound " + text + "In ValueCandidates";
					objRet = null;
				}
			} else { 
				final Object [] ret = Stringified.fromText(mClassType, text);
				
				objRet = ret[0];
				errorRet = (ErrorStruct)ret[1];
			}
			
			if(errorRet == null) { 
				
				final Object [] objs = getSelectObjs(mIsGlobal);
				for(Object select : objs) { 
					if(select != null && mClassSelect.isAssignableFrom(select.getClass())) {
						try{ 
							mSetFunc.invoke(select, objRet);
						} catch (Exception exception) {
							exception.printStackTrace();
						} 
					} 
				} 
				
//				if(mIsGlobal) {
//					final ElfNode [] selects= getCurrentNodes();
//					for(Object select : selects) { 
//						if(select != null && mClassSelect.isAssignableFrom(select.getClass())) {
//							try{ 
//								mSetFunc.invoke(select, objRet);
//							} catch (Exception exception) {
//								exception.printStackTrace();
//							} 
//						} 
//					} 
//				} else { 
//					final ElfNode [] selects= getCurrentNodes(); 
//					if(selects.length > 0) {
//						final Object select = selects[0]; 
//						if(select != null && mClassSelect.isAssignableFrom(select.getClass())) {
//							try{
//								mSetFunc.invoke(select, objRet);
//							} catch (Exception exception) {
//								exception.printStackTrace();
//							}
//						} 
//					}
//				}
			} else {
				System.err.println("error info:"+errorRet.errorInfo+"\n");
				System.err.println("error type:"+errorRet.errorType+"\n");
				System.err.println("error pos:["+errorRet.beginPos+","+errorRet.endPos+")\n");
			}
		} 
	}; 
	
//	private final Listener mFloatListener = new Listener() {
//		public void handleEvent(Event event) {
//			 if(event.widget instanceof Text) {
//				 Text t = (Text) event.widget;
//				 String s = t.getText() + event.text; 
//				 if (s.matches("\\d*(\\.{0,1})\\d*$")) { 
//					 event.doit = true; 
//				 } else {
//					 event.doit = false; 
//				 } 
//			 } 
//		}
//	}; 

	@SuppressWarnings("unchecked")
	public final TreeItem create(final TreeItem root) {
		mTree = root.getParent();
		mMyItem = new TreeItem(root, SWT.NONE);
		mMyItem.setText(0, mDialogTitle);
		
		mControl = null;
		mValueClassType = TypeFactory.getType(mClassType);
		
		if (isDirectModifier()) {
			final TreeEditor editor = new TreeEditor(mTree);
			editor.grabHorizontal = true;
			
			final int style = SWT.NONE | SWT.BORDER;
			
			if(mStaticValueCandidates != null || (mDynamicCandidateMethod != null) ) {
				final CCombo candidates = new CCombo(mTree, style);
				editor.setEditor(candidates, mMyItem, 1);
				candidates.addModifyListener(mModifyListener);
				if(mStaticValueCandidates != null) {
					final String [] shows = new String[mStaticValueCandidates.length];
					for(int i=0; i<shows.length; i++) {
						shows[i] = mStaticValueCandidates[i].toString();
					} 
					candidates.setItems(shows); 
				} 
				
				mControl = candidates;
			} else {
				switch (mValueClassType) { 
				
				case INT_TYPE:
					//spinner -max, +max
					final Spinner intSpinner = new Spinner(mTree, style);
					editor.setEditor(intSpinner, mMyItem, 1);
					intSpinner.setMaximum(Integer.MAX_VALUE);
					intSpinner.setMinimum(Integer.MIN_VALUE);
					intSpinner.addModifyListener(mModifyListener);
					
					mControl = intSpinner;
					break;
				case FLOAT_TYPE:
					//text
					final Text floatText = new Text(mTree, style);
					editor.setEditor(floatText, mMyItem, 1);
					floatText.addModifyListener(mModifyListener);
					
					mControl = floatText;
					break;
				case BOOL_TYPE:
					//ccomb
					final CCombo boolCombo = new CCombo(mTree, style);
					editor.setEditor(boolCombo, mMyItem, 1);
					boolCombo.addModifyListener(mModifyListener);
					boolCombo.setItems(new String[]{"true",  "false"});
					
					mControl = boolCombo;
					break;
				case STRING_TYPE:
					//text
					final Text strText = new Text(mTree, style);
					editor.setEditor(strText, mMyItem, 1);
					strText.addModifyListener(mModifyListener);
					
					mControl = strText;
					break;
				case ENUM_TYPE:
					//ccomb
					final CCombo enumCombo = new CCombo(mTree, style);
					editor.setEditor(enumCombo, mMyItem, 1);
					enumCombo.addModifyListener(mModifyListener);
					enumCombo.setItems(EnumHelper.toStringNoT((Class<? extends Enum<?>>)mClassType));
					
					mControl = enumCombo;
					break;
				case DOUBLE_TYPE:
					//text
					final Text doubleText = new Text(mTree, style); 
					editor.setEditor(doubleText, mMyItem, 1); 
					doubleText.addModifyListener(mModifyListener); 
					
					mControl = doubleText;
					break; 
				case CHAR_TYPE:
					//text
					final Text charText = new Text(mTree, style); 
					editor.setEditor(charText, mMyItem, 1); 
					charText.addModifyListener(mModifyListener); 
					
					mControl = charText;
					break; 
				case ELFENUM_TYPE:
					throw new IllegalArgumentException("ELFENUM_TYPE Cound not be modifiered directly!");
//					break;
				case ELSE_TYPE:
					throw new IllegalArgumentException("STRANGE_TYPE Cound not be modifiered directly!");
				case ARRAY_TYPE:
					throw new IllegalArgumentException("ARRAY_TYPE Cound not be modifiered directly!");
				} 
			}
		} 

		final Tree tree = mMyItem.getParent();
		tree.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent e) {
				final TreeItem[] items = tree.getSelection();
				if (items != null && items.length > 0 && items[0] == mMyItem) {
					onDoubleClick();
				} 
			}
		}); 
		
		return mMyItem;
	} 

	public void onDoubleClick() {
		try {
			Object value = getValue(); 
			
			if(value instanceof ElfDataDisplay) { 
				final MultiLineDialog dialog = new MultiLineDialog();
				
				dialog.setTitle(((ElfDataDisplay)value).toTitle());
				dialog.setLabels(((ElfDataDisplay)value).toLabels());
				dialog.setValues(((ElfDataDisplay)value).toValues());
				dialog.setValueTypes(((ElfDataDisplay)value).toTypes());
				
				final Object [] ret = dialog.open();
				if(ret != null) {
					int flag = ((ElfDataDisplay)value).setValues(ret); 
					if(flag != -1) { 
						System.err.println(this.getClass().getSimpleName()+":onDoubleClick:setValue"+value.getClass().getSimpleName()+" faild " + flag);
					} 
				}
			} else if(value instanceof IDataDisplay) {
				final PopDialog popDialog = new PopDialog();
				popDialog.setTitle("Modifier "+mDialogTitle+" "+((IDataDisplay) value).toTitle());
				popDialog.setLabels(((IDataDisplay) value).toLabels());
				popDialog.setValues(((IDataDisplay) value).toValues());
				
				((IDataDisplay) value).fromValues( popDialog.open() );
				
			} else {
				final AnalysisDialog<Object> dialog = new AnalysisDialog<Object>(mDialogTitle, true);
				value = dialog.open(value, mClassType); 
			} 
			
			if(value != null) {
				setValue( value );
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Object getValue() {
		try {
			final Object [] objs = this.getSelectObjs(mIsGlobal);
			if(objs != null && objs.length > 0 && mClassSelect.isAssignableFrom(objs[0].getClass())) {
				final Object value = this.mGetFunc.invoke(objs[0]); 
				return value;
			} 
		} catch (Exception e) {
		}
		
		final String text = getContext();
		final Object [] ret = Stringified.fromText(mClassType, text);
		final Object objRet = ret[0];
		return objRet;
	} 
	
	public boolean pasteContext(String text) {
		final Object [] ret = Stringified.fromText(mClassType, text);
		final Object value = ret[0];
		setValue( value );
		return value != null;
	} 
	
	public String copyContext() {
		return getContext();
	} 
	
	public void setValue(final Object value) {
		if(value != null) { 
			final Object [] objs = this.getSelectObjs(mIsGlobal);
			if(objs != null) {
//				final Object oldValue = getValue();
//				
//				if(mUseUndo) {
//					UndoManager.checkInUndo(new Runnable() {
//						public void run() {
//							for(final Object o : objs) {
//								if(o != null && mClassSelect.isAssignableFrom(o.getClass())) {
//									try {
//										mSetFunc.invoke(o, oldValue); 
//									} catch (Exception e) { 
//									} 
//								}
//							} 
//						}
//					}, new Runnable() {
//						public void run() {
//							for(final Object o : objs) {
//								if(o != null && mClassSelect.isAssignableFrom(o.getClass())) {
//									try {
//										mSetFunc.invoke(o, value); 
//									} catch (Exception e) { 
//									} 
//								}
//							} 
//						}
//					}); 
//				} 
				UndoRedoManager.checkInUndo(); 
				for(final Object o : objs) {
					if(o != null && mClassSelect.isAssignableFrom(o.getClass())) {
						try { 
							mSetFunc.invoke(o, value); 
						} catch (Exception exception) { 
							exception.printStackTrace(); 
						} 
					}
				} 
			} 
		} 
	} 

	public final void run(boolean instance) {
		// setContext(mTreeItem, mValue.toString())
		final long now = System.currentTimeMillis();
		final long time;
		
		if(isDirectModifier() && (this.mValueClassType == ClassType.INT_TYPE || this.mValueClassType == ClassType.FLOAT_TYPE)) {
			time = MAX_MODIFIER_TIME;
		} else { 
			time = NORMAL_MODIFIER_TIME;
		} 
		
		if(instance || time + mLastModifierTime < now) {
			//need update 
			try { 
				final Object [] objs = this.getSelectObjs(mIsGlobal);
				if(objs != null && objs.length > 0 && mClassSelect.isAssignableFrom(objs[0].getClass())) {
					final Object value = this.mGetFunc.invoke(objs[0]); 
					setContext(""+Stringified.toText(value, false)); 
				} 
				
				if(mDynamicCandidateMethod != null) {
					final Object ret = mDynamicCandidateMethod.invoke(objs[0]); 
					setDynamicValueCandidates((Object[])ret); 
				} 
				
			} catch (Exception e) { 
//				e.printStackTrace(); 
			} 
			mLastModifierTime = now; 
		} 
	} 

	String getContext() {
		final TreeItem item = mMyItem;
		final Control control = mControl;
		if (control == null) {
			final String ret = item.getText(1);
			return ret;
		} else {
			if (control instanceof Text) {
				return ((Text) control).getText();
			} else if (control instanceof CCombo) {
				return ((CCombo) control).getText();
			} else if (control instanceof Spinner) {
				return "" + ((Spinner) control).getSelection();
			} else if(control instanceof Label) { 
				return ((Label) control).getText(); 
			}
		} 
		return null;
	}

	void setContext(String text) {
		final String oldText = getContext();
		if (!oldText.equals(text)) {
			final TreeItem item = mMyItem;
			final Control control = mControl;
			if (control == null) {
				item.setText(1, text );
			} else {
				if (control instanceof Text) { 
					((Text) control).removeModifyListener(mModifyListener);
					((Text) control).setText(text); 
					((Text) control).addModifyListener(mModifyListener);
				} else if (control instanceof CCombo) {
					((CCombo) control).removeModifyListener(mModifyListener);
					((CCombo) control).setText(text);
					((CCombo) control).addModifyListener(mModifyListener);
				} else if (control instanceof Spinner) {
					try {
						((Spinner) control).removeModifyListener(mModifyListener);
						((Spinner) control).setSelection(Integer.valueOf(text));
						((Spinner) control).addModifyListener(mModifyListener);
					} catch (Exception e) { 
						Redirect.errPrintln(e.toString());
					}
				} else if(control instanceof Label) {
					((Label) control).setText(""+text);
				}
			}
		} 
	} 

	public TreeItem getMyTreeItem() { 
		return mMyItem;
	} 

	public Class<?> getValueType() { 
		return this.mClassType;
	}
} 
