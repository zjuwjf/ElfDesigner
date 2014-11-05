//package com.ielfgame.stupidGame.face.basic;
//
//import org.eclipse.swt.custom.CCombo;
//import org.eclipse.swt.widgets.Control;
//import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.Spinner;
//import org.eclipse.swt.widgets.Text;
//import org.eclipse.swt.widgets.Tree;
//import org.eclipse.swt.widgets.TreeItem;
//
//import com.ielfgame.stupidGame.data.TypeFactory.ClassType;
//
//@Deprecated
//public abstract class BasicFuncItem implements ITreeItem{
//	protected final Class<?> mClassSelect;
//	protected final Class<?> mClassType;
//	protected ClassType mValueClassType;
//	
//	protected final String mDialogTitle;
////	protected final String[] mDialogValues;
//	
//	protected TreeItem mMyItem;
//	protected Tree mTree;
//	protected Control mControl;
//	
//	protected final boolean mIsGlobal;
//	
//	protected long mLastModifierTime = System.currentTimeMillis();
//	
//	protected final int MAX_MODIFIER_TIME = 2000;
//	protected final int NORMAL_MODIFIER_TIME = 500;
//	
//	public BasicFuncItem(boolean global, Class<?> classSelect, Class<?> classType,
//			String getFunc, String setFunc, String arrayFunc,
//			String title) {
//		this.mIsGlobal = global;
//		this.mClassSelect = classSelect;
//		this.mClassType = classType;
//		
//		assert (mClassSelect != null && mClassType != null) ;
//		
//		this.mDialogTitle = title;
//	} 
//	
//	String getContext() {
//		final TreeItem item = mMyItem;
//		final Control control = mControl;
//		if (control == null) {
//			final String ret = item.getText(1);
//			return ret;
//		} else {
//			if (control instanceof Text) {
//				return ((Text) control).getText();
//			} else if (control instanceof CCombo) {
//				return ((CCombo) control).getText();
//			} else if (control instanceof Spinner) {
//				return "" + ((Spinner) control).getSelection();
//			} else if(control instanceof Label) { 
//				return ((Label) control).getText();
//			}
//		} 
//		return null;
//	}
//
//	void setContext(String text) {
//		final String oldText = getContext();
//		if (!oldText.equals(text)) {
//			final TreeItem item = mMyItem;
//			final Control control = mControl;
//			if (control == null) {
//				item.setText(1, text );
//			} else {
//				if(control instanceof Label) {
//					((Label) control).setText(""+text);
//				}
//			}
//		}
//	}
//
//	public TreeItem getMyTreeItem() {
//		return mMyItem;
//	} 
//
//	public Class<?> getValueType() {
//		return this.mClassType;
//	}	
//	
//}
