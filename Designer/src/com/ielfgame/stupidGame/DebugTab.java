package com.ielfgame.stupidGame;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.ielfgame.stupidGame.power.APowerManSingleton;
import com.ielfgame.stupidGame.power.PowerMan;

public class DebugTab extends AbstractWorkSpaceTab {
	protected StyledText mOutput;

	private CCombo mClassCCombo;
	private Text mPathText;
	
	private String [] mClassedText;
	private Class<? extends APowerManSingleton> [] mClasses;
	
	public DebugTab() {
		super("Debug");
	}
	
	@SuppressWarnings("unchecked")
	public void initAfterPowerInit(){
		try {
			@SuppressWarnings("rawtypes")
			final Class powerManclass =  PowerMan.class;
			final Field field = powerManclass.getDeclaredField("sMap");
			field.setAccessible(true);
			final HashMap<Class<? extends APowerManSingleton>, APowerManSingleton> sMap = (HashMap<Class<? extends APowerManSingleton>, APowerManSingleton>)field.get(null);
			Set<Class<? extends APowerManSingleton>> set = sMap.keySet();
			final int size = set.size();
			mClassedText = new String[size];
			mClasses = new Class[size];
			int i = 0;
			for(Class<? extends APowerManSingleton> c : set){
				mClassedText[i] = c.getSimpleName();
				mClasses[i] = c;
				i++;
			}
			mClassCCombo.setItems(mClassedText);
//			mClassCCombo.sets
		} catch (Exception e2) {
			e2.printStackTrace();
		}
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
		layout.numColumns = 3;
		composite2.setLayout(layout);
		
		mClassCCombo = new CCombo(composite2, SWT.NONE | SWT.BORDER);
		GridData gridData = new GridData();
		gridData.widthHint = 130;
		mClassCCombo.setLayoutData(gridData);

		final Button run = new Button(composite2, SWT.CENTER);
		run.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				try {
					int selectIndex = mClassCCombo.getSelectionIndex();
					final String path = mPathText.getText();
					
					int fromIndex = 0;
					ArrayList<Integer> indexs = new ArrayList<Integer>();
					
					while(fromIndex < path.length()){
						int newIndex = path.indexOf(".", fromIndex);
						if(newIndex > 0){
							if(newIndex < path.length() - 1 && ! (Character.isDigit( path.charAt(newIndex+1) ))){
								indexs.add(newIndex);
							}
							
							fromIndex = newIndex + 1;
						} else {
							indexs.add(path.length());
							break;
						}
					}
					
					ArrayList<Section> paths = new ArrayList<Section>();
					int begIndex = 0;
					for(Integer endIndex : indexs){
						final String blockText = path.substring(begIndex, endIndex);
						final String str = blockText;
						final Section section = new Section();
						int index = str.indexOf("(");
						if (index > 0) {
							section.isMethod = true;
							section.name = str.substring(0, index);
							
							final ArrayList<String> strArgs = new ArrayList<String>();
							final String totalArgs = str.substring(index);
							final StringTokenizer strT2 = new StringTokenizer(totalArgs, "(,) ");
							while (strT2.hasMoreElements()) {
								final String text = strT2.nextToken();
								if(text.length() > 0){
									if(strT2.hasMoreElements()){
										final String text2 = strT2.nextToken();
										if(text2.length() > 0){
											if(Character.isDigit( text.charAt(text.length()-1) ) &&
													Character.isDigit( text2.charAt(text2.length()-1) )){
												
												strArgs.add(text+"."+text2);
												continue;
											}
										} 
										
										strArgs.add(text);
										strArgs.add(text2);
									}
								}
								strArgs.add(text);
							}
							
							final Object[] args = new Object[strArgs.size()];
							
							for (int i = 0; i < args.length; i++) {
								final String text = strArgs.get(i);
								System.err.println("#"+text+"#");
								if(text.equalsIgnoreCase("true")){
									args[i] = true;
								} else if(text.equalsIgnoreCase("false")){
									args[i] = false;
								} else if (text.contains(".") || text.contains("f")) {
									args[i] = Float.valueOf(text);
								} else if (text.startsWith("\"")) {
									args[i] = text.substring(1, text.length()-1);
								} else {
									args[i] = Integer.valueOf(text);
								} 
							}
							
							section.args = args;
						} else {
							section.isMethod = false;
							section.name = str;
						} 
						paths.add(section);
						begIndex = endIndex + 1;
					} 

					@SuppressWarnings({"rawtypes" })
					Class _class =  mClasses[selectIndex];

					@SuppressWarnings("unchecked")
					Object ret = PowerMan.getSingleton(_class);
					
					for(final Section section : paths){
						System.err.println(section.toString());
						
						if(section.isMethod){
							@SuppressWarnings({"rawtypes" })
							final Class [] classes = new Class[section.args.length];
							for(int i=0; i<classes.length; i++){
								if(section.args[i] instanceof Integer){
									classes[i] = int.class;
								} else if(section.args[i] instanceof Float){
									classes[i] = float.class;
								} else if(section.args[i] instanceof Boolean){
									classes[i] = boolean.class;
								} else {
									classes[i] = String.class;
								} 
							}
							@SuppressWarnings("unchecked")
							Method method = _class.getMethod(section.name, classes);
							ret = method.invoke(ret, section.args); 
							
							System.err.println("2"+section.toString());
						} else {
							Field field = _class.getDeclaredField(section.name);
							field.setAccessible(true);
							ret = field.get(ret);
						}
						if(ret != null){
							_class = ret.getClass();
						} else {
							_class = null;
						} 
					}

					mOutput.append("return:\n");
					if (ret == null) {
						mOutput.append("null");
					} else {
						mOutput.append(ret.toString());
					}
					mOutput.append("\n");
					

				} catch (Exception e1) {
					int startLine = mOutput.getLineCount() - 1;

					mOutput.append(e1.toString() + "\n");
					int endLine = mOutput.getLineCount();
					mOutput.setLineBackground(startLine, endLine - startLine - 1, new Color(null, 255, 0, 0));
				}
				
				mOutput.setSelection(mOutput.getCharCount());
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		run.setText("run");

		final Button clear = new Button(composite2, SWT.CENTER);
		clear.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				mOutput.setText("");
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		clear.setText("clear");
		
		composite2 = new Composite(composite, SWT.NONE);
		composite2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		layout = new GridLayout();
		layout.numColumns = 1;
		composite2.setLayout(layout);
		mPathText = new Text(composite2, SWT.NONE | SWT.BORDER);
		mPathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		mPathText.setText("getSelectNode().setCentreX(10.0f)");

		mOutput = new StyledText(composite, SWT.NONE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		mOutput.setEditable(false);
		mOutput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		mOutput.setWordWrap(true);
		mOutput.setJustify(true);
		mOutput.setBottomMargin(0);

		return composite;
	}

	public void update() {

	}

	class Section {
		public String name;
		boolean isMethod;
		Object[] args;
		
		public String toString(){
			String ret = name+"#";
			if(isMethod){
				ret += "args:";
				for(Object o : args){
					ret += o + ",";
				}
			}
			return ret;
		}
	}
}
