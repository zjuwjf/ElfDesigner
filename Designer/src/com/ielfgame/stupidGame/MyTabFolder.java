package com.ielfgame.stupidGame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class MyTabFolder extends CTabFolder{

	public MyTabFolder(final Composite parent) {
		super(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		
		this.setBorderVisible(false);
		this.setMinimizeVisible(true);
		this.setMaximizeVisible(true);
		this.setMinimizeVisible(true);
		Color[] color=new Color[4]; 
		color[0]=new Color(Display.getCurrent(), 220, 230, 239);
        color[1]=new Color(Display.getCurrent(), 153, 180, 209);   
        color[2]=Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);    
        color[3]=Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);      
        
        int[] intArray=new int[]{50,70,100};
        this.setSelectionBackground(color, intArray);            
        this.setSimple(false);
		
//		Button button=new Button(this, SWT.ARROW|SWT.RIGHT);
//		this.setTopRight(button);
		
		this.addCTabFolder2Listener(new CTabFolder2Adapter() {    
            public void minimize(CTabFolderEvent event) {    
            	setMinimized(true);    
            	setMinimizeVisible(true);
            	setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false)); 
            	parent.layout(true);//Ë¢ÐÂ²¼¾Ö    
//            	if ( event.widget == PowerMan.getSingleton(ResViewWorkSpace.class).mTabFolder ){
//            		
//            	} else if( event.widget == PowerMan.getSingleton(NodeViewWorkSpace.class).mTabFolder ){
//            		
//            	}
            }    
            
            public void maximize(CTabFolderEvent event) {    
            	setMaximized(true);    
            	setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));    
            	parent.layout(true);    
//            	if ( event.widget == PowerMan.getSingleton(ResViewWorkSpace.class).mTabFolder ){
//            		
//            	} else if( event.widget == PowerMan.getSingleton(NodeViewWorkSpace.class).mTabFolder ){
//            		
//            	}
            }    
            
            public void restore(CTabFolderEvent event) {    
            	setMinimized(false);    
            	setMaximized(false);    
            	setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false));    
            	parent.layout(true);    
//            	if ( event.widget == PowerMan.getSingleton(ResViewWorkSpace.class).mTabFolder ){
//            		
//            	} else if( event.widget == PowerMan.getSingleton(NodeViewWorkSpace.class).mTabFolder ){
//            		
//            	}
            }    
        });    
		
	}

}
