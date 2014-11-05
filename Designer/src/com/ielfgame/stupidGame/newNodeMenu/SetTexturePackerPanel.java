package com.ielfgame.stupidGame.newNodeMenu;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import com.ielfgame.stupidGame.ColorFactory;
import com.ielfgame.stupidGame.Constants;
import com.ielfgame.stupidGame.data.Stringified;

import elfEngine.basic.utils.EnumHelper;

public class SetTexturePackerPanel {
	/***
	 * 
	  --opt <pixelformat>         Optimized output for given pixel formats. Supported formats are:
                                        RGBA8888 - 32bit, 8bit/channel, 8bit transparency
                                        BGRA8888 - 32bit, 8bit/channel, 8bit transparency
                                        RGBA4444 - 16bit, 4bit/channel, 4bit transparency
                                          RGB888 - 24bit, 8bit/channel, no transparency
                                          RGB565 - 16bit, 5bit red, 6bit green, 5bit blue, no transparancy
                                        RGBA5551 - 16bit, 5bit/channel, 1bit transparancy
                                        RGBA5555 - 20bit, 5bit/channel, 5bit transparancy
                                          PVRTC2 - PVRTC compression, 2bit per pixel, PVR files only
                                          PVRTC4 - PVRTC compression, 4bit per pixel, PVR files only
                                  PVRTC2_NOALPHA - PVRTC compression, 2bit per pixel, PVR files only
                                  PVRTC4_NOALPHA - PVRTC compression, 4bit per pixel, PVR files only
                                           ALPHA - 8bit transparency
                                 ALPHA_INTENSITY - 8bit intensity, 8bit transparency
                                            ETC1 - ETC1 compression (PKM file only)
	 */
	
	public enum DitherFormat{
		NoneLinear("--dither-none-linear"),
		Fs("--dither-fs"),
		FsAlpha("--dither-fs-alpha"),
		Atkinson("--dither-atkinson"),
		AtkinsonAlpha("--dither-atkinson-alpha"),
		;
		DitherFormat(String value) {
			this.value = value;
		} 
		private final String value;
		public String toString() { 
			return value; 
		} 
	}
	
	public enum ImageFormat{
		RGBA4444(DitherFormat.FsAlpha,"pvr2ccz","pvr.ccz","RGBA4444", "NPOT", 0),
		RGBA8888(DitherFormat.FsAlpha,"pvr2ccz","pvr.ccz","RGBA8888","NPOT", 0), 
		RGB565(DitherFormat.Fs,"pvr2ccz","pvr.ccz","RGB565", "NPOT", 0), 
		RGB5551(DitherFormat.FsAlpha,"pvr2ccz","pvr.ccz","RGB5551","", 0), 
		
		PVRTC4(DitherFormat.FsAlpha,"pvr2ccz","pvr.ccz","PVRTC4", "POT", 0), 
		PVRTC4_NOALPHA(DitherFormat.Fs,"pvr2ccz","pvr.ccz","PVRTC4_NOALPHA", "POT", 0), 
		PVRTC2(DitherFormat.FsAlpha,"pvr2ccz","pvr.ccz","PVRTC2", "POT", 0),
		PVRTC2_NOALPHA(DitherFormat.Fs,"pvr2ccz","pvr.ccz","PVRTC2_NOALPHA", "POT", 0),
		PNG8888(DitherFormat.FsAlpha,"png","PNG","RGBA8888", "NPOT", 0),
		
//		PVRTC4_RAW(DitherFormat.FsAlpha,"pvr2","pvr","PVRTC4", "POT", 0), 
		
		GLEE75(DitherFormat.FsAlpha,"png","PNG","RGBA8888", "NPOT", 75),
		GLEE35(DitherFormat.FsAlpha,"png","PNG","RGBA8888", "NPOT", 35),
//		RGB888(DitherFormat.Fs), 
		;
		
		private final DitherFormat mDitherFormat;
		private final String mFormat;
		private final String mSubfix;
		private final String mString;
		private final int mCompress;
		
		private final String mSizeFormat;
		
//		final String imageformat = "pvr2ccz";
//		final String subfix = "pvr.ccz";
		
//		final String imageformat = "png";
//		final String subfix = "PNG";
		
		ImageFormat(DitherFormat ditherFormat, String imageformat, String subfix, String str, String sizeFormat, int compress) {
			mDitherFormat = ditherFormat;
			mFormat = imageformat;
			mSubfix = subfix;
			mString = str;
			mCompress = compress;
			mSizeFormat = sizeFormat;
		}
		
		public DitherFormat getDitherFormat() {
			return mDitherFormat;
		}
		
		public String getFormat() {
			return mFormat;
		}
		
		public String getSizeFromat() {
			return mSizeFormat;
		}
		
		public String getSubfix() {
			return mSubfix;
		}
		
		public String toRGBString() {
			return mString;
		}
		public int getCompress() {
			return mCompress;
		}
	}
	
	/***
	 *  
	 *  --dither-none-nn
 --dither-none-linear
 --dither-fs
 --dither-fs-alpha
 --dither-atkinson
 --dither-atkinson-alpha
 --background-color <rrggbb>
	 */
	
	public static class SetTexturePackerPanelRet {
		public String name;
		public int num;
		
		public float scale = 1;
		public ImageFormat imageFormat = ImageFormat.RGBA4444;
		public DitherFormat ditherFormat = DitherFormat.FsAlpha;
		
		public boolean single;
		public boolean autoRemove;
		
		public String toString() { 
			return String.format("name %s\nscale %f\nimageFormat %s\nditherFormat %s\n", name,scale,imageFormat.toString(),ditherFormat.toString());
		}
		
		public void copyFrom(SetTexturePackerPanelRet ret) {
			this.name = ret.name;
			this.num = ret.num;
			this.scale = ret.scale;
			this.imageFormat = ret.imageFormat;
			this.ditherFormat = ret.ditherFormat;
			this.single = ret.single;
			this.autoRemove = ret.autoRemove;
		}
	} 

	public SetTexturePackerPanelRet open(final SetTexturePackerPanelRet ret, final Shell parent) { 
		ret.name = (""+ret.name).replace("#", "").replace("@", "");
		
		final Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
//		RunState.initChildShell(shell);
		shell.setLayout(new GridLayout());
		shell.setText("Set TexturePacker Config");

		final Composite composite0 = new Composite(shell, SWT.NONE); 
		composite0.setLayoutData(new GridData(GridData.FILL_HORIZONTAL)); 
		GridLayout layout = new GridLayout(); 
		layout.numColumns = 2; 
		composite0.setLayout(layout); 
		
		final Composite composite01 = new Composite(composite0, SWT.NONE);
		GridData gridData = new GridData();
		gridData.widthHint = 200;
		composite01.setLayoutData(gridData);
		layout = new GridLayout();
		layout.numColumns = 2;
		composite01.setLayout(layout);
		
		final Label label = new Label(composite01, SWT.RIGHT);
		label.setText("Output");
		
		final Text text = new Text(composite01, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.setText(""+ret.name);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				text.setBackground(ColorFactory.WHITE);
			}
		});
		
		final Composite composite02 = new Composite(composite0, SWT.NONE);
		composite02.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout2 = new GridLayout();
		layout2.numColumns = 2;
		composite02.setLayout(layout2);
		gridData = new GridData();
		gridData.widthHint = 200;
		composite02.setLayoutData(gridData);
		
		final Label label02 = new Label(composite02, SWT.RIGHT);
		label02.setText("Dither");
		final CCombo combo02 = new CCombo(composite02, SWT.BORDER);
		combo02.setItems(EnumHelper.toString(DitherFormat.class));
		combo02.setText(ret.ditherFormat.toString());
		combo02.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		combo02.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				combo02.setBackground(ColorFactory.WHITE);
			} 
		}); 
		
		final Composite composite1 = new Composite(shell, SWT.NONE);
		composite1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		layout = new GridLayout();
		layout.numColumns = 2;
		composite1.setLayout(layout);
		
		final Composite composite11 = new Composite(composite1, SWT.NONE);
		gridData = new GridData();
		gridData.widthHint = 200;
		composite11.setLayoutData(gridData);
		layout = new GridLayout();
		layout.numColumns = 2;
		composite11.setLayout(layout);
		final Label label11 = new Label(composite11, SWT.LEFT);
		label11.setText("Scale");
		final Text text11 = new Text(composite11, SWT.BORDER);
		text11.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text11.setText(""+ret.scale);
		text11.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) { 
				text11.setBackground(ColorFactory.WHITE);
			}
		});
		
		final Composite composite12 = new Composite(composite1, SWT.NONE);
		gridData = new GridData();
		gridData.widthHint = 200;
		composite12.setLayoutData(gridData);
		layout = new GridLayout();
		layout.numColumns = 2;
		composite12.setLayout(layout);
		final Label label12 = new Label(composite12, SWT.RIGHT);
		label12.setText("Format");
		final CCombo combo = new CCombo(composite12, SWT.BORDER);
		combo.setItems(EnumHelper.toString(ImageFormat.class));
		combo.setText(ret.imageFormat.toString());
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		combo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				combo.setBackground(ColorFactory.WHITE);
			}
		});
		
		final Composite composite2 = new Composite(shell, SWT.NONE);
		composite2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		layout = new GridLayout();
		layout.numColumns = 3;
		composite2.setLayout(layout);

		final Button singleTexture = new Button(composite2, SWT.CHECK);
		singleTexture.setText("Pack in single texture");
		singleTexture.setSelection(ret.single);
		
		final Spinner nums = new Spinner(composite2, SWT.NONE);
		nums.setSelection(ret.num);
		nums.setMaximum(Integer.MAX_VALUE);
		nums.setMinimum(1);
		nums.setEnabled(!ret.single);
		nums.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				nums.setBackground(ColorFactory.WHITE);
			}
		});
		
		singleTexture.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				if(singleTexture.getSelection()) {
					nums.setEnabled(false);
				} else { 
					nums.setEnabled(true);
				} 
			} 
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		
		final Button autoRemove = new Button(composite2, SWT.CHECK);
		autoRemove.setText("Auto Remove");
		autoRemove.setSelection(ret.autoRemove);
		
		createControlButtons(shell, text, text11, combo, combo02, singleTexture, nums, autoRemove, ret);

		shell.pack();
		shell.setLocation(shell.getDisplay().getClientArea().width / 2 - shell.getSize().x / 2, shell.getDisplay().getClientArea().height / 2 - shell.getSize().y / 2);
		
		shell.open();
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		
		return ret;
	} 

	private void createControlButtons(final Shell shell, final Text text, 
			final Text scale, final CCombo format, final CCombo dither,
			final Button isConvertJpg2Png, final Spinner nums, final Button autoRemove, final SetTexturePackerPanelRet ret) {
		final Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		
		Button okButton = new Button(composite, SWT.PUSH);
		okButton.setText(Constants.POP_DIALOG_OK);
		okButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ret.name = text.getText();
				if(ret.name == null || ret.name.length()==0) {
					text.setBackground(ColorFactory.RED);
					return;
				}
				
				ret.single = isConvertJpg2Png.getSelection();
				ret.num = nums.getSelection(); 
				if(!ret.single && ret.num <= 0) { 
					nums.setBackground(ColorFactory.RED);
					return;
				} 
				
				ret.autoRemove = autoRemove.getSelection();
				
				final String text = format.getText();
				final Object [] rets = Stringified.fromText(ImageFormat.class, text);
				try {
					final ImageFormat objRet = (ImageFormat)rets[0];
					ret.imageFormat = objRet; 
				} catch (Exception e2) { 
					e2.printStackTrace();
					format.setBackground(ColorFactory.RED);
					return;
				} 
				
				if(ret.imageFormat == null) {
					format.setBackground(ColorFactory.RED);
					return;
				} 
				
				final String text2 = dither.getText();
				final Object [] rets2 = Stringified.fromText(DitherFormat.class, text2);
				try {
					final DitherFormat objRet = (DitherFormat)rets2[0];
					ret.ditherFormat = objRet; 
				} catch (Exception e2) { 
					e2.printStackTrace();
					dither.setBackground(ColorFactory.RED);
					return;
				} 
				
				if(ret.imageFormat == null) {
					format.setBackground(ColorFactory.RED);
					return;
				} 
				
				try {
					final String str_scale = scale.getText();
					final float value = Float.valueOf(str_scale);
					ret.scale = value;
				} catch (Exception e2) {
					e2.printStackTrace();
					scale.setBackground(ColorFactory.RED);
					return;
				} 
				
				if(ret.scale <= 0) { 
					scale.setBackground(ColorFactory.RED);
					return;
				} 
				
				shell.close();
			}
		});

		Button cancelButton = new Button(composite, SWT.PUSH);
		cancelButton.setText(Constants.POP_DIALOG_CANCEL);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ret.name = null;
				ret.single = true;
				ret.num = 1;
				
				shell.close();
			}
		});

		shell.setDefaultButton(okButton);
	}
}
