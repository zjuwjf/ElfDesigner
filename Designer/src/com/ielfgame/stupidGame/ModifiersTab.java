package com.ielfgame.stupidGame;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import com.ielfgame.stupidGame.animation.Animate;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.power.PowerMan;

public class ModifiersTab extends AbstractWorkSpaceTab{

	public ModifiersTab() {
		super("Modifiers");
	}

	final ArrayList<Animate> mMap = PowerMan.getSingleton(DataModel.class).getAnimateList();
	List mList;
	private Composite mParent;
	
	public Composite createTab(CTabFolder parent) {
		mParent = new Composite(parent, SWT.NONE);
		
		final GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 1;
		gridLayout2.marginHeight = 2;
		gridLayout2.horizontalSpacing = 0;
		mParent.setLayoutData(gridLayout2);
		
		mList = new List(mParent, SWT.NONE | SWT.BORDER);
		mList.setLayoutData(GridData.FILL_HORIZONTAL|GridData.FILL_VERTICAL);
		mList.setItems(new String[] {} );
		
		mList.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		setDND();
		return mParent;
	}
	
	private DragSource setDND(){
		DragSource dragSource = new DragSource(mList, DND.DROP_MOVE| DND.DROP_COPY);
		dragSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dragSource.addDragListener(new DragSourceAdapter() {			
			public void dragSetData(DragSourceEvent event) {
				final String [] texts = mList.getSelection();
				if(texts != null && texts.length > 0){
					event.data = texts[0];
				} else {
					event.detail = DND.DROP_NONE;
				}
			}
		});
		
		return dragSource;
	}
	
	public boolean hasExited(String name){
		for(Animate animate:mMap){
			if(animate.mName.equals(name)){
				return true;
			}
		}
		return false;
	}
	
	public void save(Animate clone){
		for(Animate animate:mMap){
			if(animate.mName.equals(clone.mName)){
				mMap.remove(animate);
				break;
			}
		}
		mMap.add(clone);
		
		mList.dispose();
		mList = new List(mParent, SWT.NONE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		mList.setLayoutData(GridData.FILL_HORIZONTAL|GridData.FILL_VERTICAL);
		final String [] strs = new String[ mMap.size() ];
		for(int i=0; i<strs.length; i++){
			strs[i] = mMap.get(i).mName;
		}
		mList.setItems(strs);
		mTabItem.setControl(mList);
		mList.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		setDND();
		
		return;
	}
	
	Animate getAnimate(String key){
		for(Animate animate:mMap){
			if(animate.mName.equals(key)){
				return animate;
			}
		}
		return null;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
}
