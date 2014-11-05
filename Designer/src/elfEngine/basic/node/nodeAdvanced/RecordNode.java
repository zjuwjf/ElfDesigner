package elfEngine.basic.node.nodeAdvanced;

import java.io.File;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.lwjgl.opengl.GL11;

import com.ielfgame.stupidGame.bitmapTool.ImageHelper;
import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.utils.FileHelper;

import elfEngine.basic.node.ElfNode;
import elfEngine.opengl.BufferHelper;
import elfEngine.opengl.Texture;
import elfEngine.opengl.TextureRegion;

public class RecordNode extends ElfNode { 
	
	private TextureRegion mTextureRegion = null;
	private boolean mNeedRefresh = true;
	private final ElfColor mClearColor = new ElfColor(0,0,0,0);
	
	public RecordNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		this.setUseSettedSize(true); 
	} 
	
	public void setSize(final ElfPointf size) {
		super.setSize(size);
		mNeedRefresh = true; 
	}
	
	public void calcSprite(float pMsElapsed) {
		if(mIsRecording) { 
			final float dt = 15; 
			super.calcSprite(dt); 
		} else { 
			super.calcSprite(pMsElapsed);
		} 
	}
	
	public void drawSprite() { 
		if (mVisible) { 
			drawBefore(); 
			drawSelf(); 
			//no children
			drawAfter();
		} 
	} 
	
	public void drawSelf() { 
		if(mTextureRegion != null) { 
			mTextureRegion.draw();
		} 
	} 
	
	public void calc(float t) { 
		super.calc(t); 
		
		final int width = Math.round(this.getWidth()); 
		final int height = Math.round(this.getHeight()); 
		
		if(mNeedRefresh || mTextureRegion == null) { 
			System.err.println("NeedFresh");
			mNeedRefresh = false;
			if(mTextureRegion != null) { 
				mTextureRegion.texture.invalid();
			} 
			
			final ImageData data = ImageHelper.create(width, height);
			final Texture texture = new Texture(data);
			texture.load();
			mTextureRegion = new TextureRegion(texture); 
		} 
		
		final TextureRegion tr = mTextureRegion; 
		if(tr != null) { 
			GL11.glClearColor(mClearColor.red, mClearColor.green, mClearColor.blue, mClearColor.alpha); 
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT); 
			
			GL11.glPushMatrix(); 
			GL11.glTranslatef(width/2.0f, height/2.0f, 0); 
			GL11.glScalef(1, -1, 1); 
			drawChilds(); 
			GL11.glPopMatrix(); 
			
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tr.texture.getGLBindId()); 
			GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, width, height); 
			
			if(mIsRecording) { 
				final float dt = t/1000f; 
				if(mRecorderFrameTmp <= 0) { 
					mIsRecording = false; 
					System.err.println("Record completed!"); 
					return; 
				} 
				
				mRecordIntervalTmp -= dt; 
				
				if(mRecordIntervalTmp < 0) { 
					mRecordIntervalTmp += mRecordInterval; 
					mRecorderFrameTmp--; 
					save(width, height, mRecorderFrame-mRecorderFrameTmp-1); 
				} 
			} 
			
			GL11.glClearColor(0, 0, 0, 0); 
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT); 
		} 
	} 
	
	public void setClearColor(final ElfColor color) {
		mClearColor.set(color);
	}
	public ElfColor getClearColor() { 
		return mClearColor;
	}
	protected final static int REF_ClearColor = DEFAULT_SHIFT; 
	
	String toPng(final int i) { 
		if(i < 10) {
			return "00" + i + ".png";
		} else if(i < 100) {
			return "0" + i + ".png";
		} else {
			return i + ".png";
		} 
	}
	
	@Deprecated
	final void save(final int width, final int height, final int iPng) { 
		final int [] bytes = mRecordBuffer; 
		BufferHelper.sByteBuffer.position(0);
		GL11.glReadPixels(0,0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, BufferHelper.sByteBuffer); 
		BufferHelper.sByteBuffer.position(0);
		for(int i=0; i<bytes.length; i++) {
			bytes[i] = BufferHelper.sByteBuffer.get(i);
		}
		
		final ImageData data = ImageHelper.create(width, height); 
		for(int i=0; i<width; i++) {
			for(int j=0; j<height; j++) {
				final int index = (j*width + i); 
				final int piexl = bytes[index]; 
				
				int red = (piexl & 0xFF); 
				int green = ((piexl>> 8) & 0xFF) ;
				int blue = ((piexl>> 16) & 0xFF) ;
				int alpha = ((piexl>>24) & 0xFF) ;
				
				final RGB rgb = new RGB(red, green, blue); 
				data.setAlpha(i, j, alpha); 
				final int pixelValue = data.palette.getPixel(rgb); 
				data.setPixel(i, j, pixelValue); 
			} 
		} 
		ImageHelper.save(data, mPath+this.getName()+FileHelper.DECOLLATOR+toPng(iPng+1));
	} 
	
	private int [] mRecordBuffer = null; 
	private int mRecorderFrame = 1; 
	private int mRecorderFrameTmp = mRecorderFrame;
	private float mStartRecordTime = 0.0f;
	private float mRecordInterval = 0.1f;
	private float mRecordIntervalTmp;
	private boolean mIsRecording = false;
	
	public int getRecorderFrame() {
		return mRecorderFrame;
	}
	public void setRecorderFrame(int mRecorderFrame) {
		this.mRecorderFrame = mRecorderFrame;
	}
	protected final static int REF_RecorderFrame = DEFAULT_SHIFT;

	public float getStartRecordTime() {
		return mStartRecordTime;
	}
	public void setStartRecordTime(float mStartRecordTime) {
		this.mStartRecordTime = mStartRecordTime;
	}
	protected final static int REF_StartRecordTime = DEFAULT_SHIFT;
	
	public float getRecordInterval() {
		return mRecordInterval;
	}
	public void setRecordInterval(float mRecordInterval) {
		this.mRecordInterval = mRecordInterval;
	}
	protected final static int REF_RecordInterval = DEFAULT_SHIFT;
	
	public void setPrepareRecorder() { 
		mIsRecording = false; 
		mNeedRefresh = true; 
		this.setPaused(true); 
		final int width = Math.round(this.getWidth()); 
		final int height = Math.round(this.getHeight()); 
		mRecordBuffer = new int[width*height]; 
	} 
	protected final static int REF_PrepareRecorder = FACE_SET_SHIFT; 
	
	public void setRecorderEnable() { 
		if(!mIsRecording) {
			mIsRecording = true; 
			this.setPaused(false); 
			
			mRecorderFrameTmp = mRecorderFrame;
			mRecordIntervalTmp = mRecordInterval;
			
			final File f = new File(mPath+this.getName()); 
			if(!f.exists()) { 
				f.mkdir(); 
			} 
		} 
	} 
	protected final static int REF_RecorderEnable = FACE_SET_SHIFT; 
	
	protected static final String mPath = FileHelper.getUserDir()+FileHelper.DECOLLATOR+"screenshot"+FileHelper.DECOLLATOR; 
	static  { 
		final File f = new File(mPath); 
		if(!f.exists()) { 
			f.mkdir(); 
		} 
	} 
} 
