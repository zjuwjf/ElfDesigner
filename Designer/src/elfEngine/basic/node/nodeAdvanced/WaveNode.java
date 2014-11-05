package elfEngine.basic.node.nodeAdvanced;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.lwjgl.opengl.GL11;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.touch.ElfEvent;
import elfEngine.basic.touch.MotionEvent;
import elfEngine.basic.utils.ElfRandom;
import elfEngine.opengl.BufferHelper;
import elfEngine.opengl.GLHelper;
import elfEngine.opengl.GLManage;
import elfEngine.opengl.TextureOptions;

public class WaveNode extends ElfNode {
	
	public WaveNode(ElfNode father, int ordinal) {
		super(father, ordinal); 
		this.setName("#wave"); 
		this.setTouchEnable(true); 
		this.mNeedUpdate = true;
	} 

	private boolean mNeedUpdate;
	public void setResid(final String resid) { 
		super.setResid(resid);
		mNeedUpdate = true;
		System.err.println("WaveNode Need Update");
	} 

	private byte[] mBuffer;
	private short[] mOffset1, mOffset2;
//	private int[] mImage; 
	
	private byte [] mImage;
	
	private final int[] mBindId = new int[1];
	private int mPower2Width, mPower2Height;
	private int mImageWidth, mImageHeight;

	void recycleArrays() {
		mBuffer = null;
		mOffset1 = null;
		mOffset2 = null;
		mImage = null;
	}

	public void calc(final float pMsElapsed) {
		super.calc(pMsElapsed);
		if (mNeedUpdate) {
			mNeedUpdate = false;

			final ElfPointf size = GLManage.getSize(getResid());
			if (size != null) {
				
				this.mImageWidth = Math.round(size.x);
				this.mImageHeight = Math.round(size.y);
				
				final int power2Width = GLHelper.minPow2(mImageWidth);
				final int power2Height = GLHelper.minPow2(mImageHeight);
				
				this.mPower2Width = power2Width;
				this.mPower2Height = power2Height;

				mBuffer = new byte[mPower2Width * mPower2Height * 4];
				mImage = new byte[mImageWidth * mImageHeight * 4];
				mOffset1 = new short[mImageWidth * mImageHeight];
				mOffset2 = new short[mImageWidth * mImageHeight];

				try {
					final ImageData source = new ImageData(new FileInputStream(new File(getResid())));
					for (int y = 0; y < mPower2Height; y++) {
						for (int x = 0; x < mPower2Width; x++) {
							if (x < mImageWidth && y < mImageHeight) { 
								int pixel = source.getPixel(x, y); 
								RGB rgb = source.palette.getRGB(pixel); 
								boolean hasAlpha = false; 
								int alphaValue = 0; 
								if (source.alphaData != null && source.alphaData.length > 0) {
									hasAlpha = true;
									alphaValue = source.getAlpha(x, y);
								} 

								final byte r = (byte) (rgb.red);
								final byte g = (byte) (rgb.green);
								final byte b = (byte) (rgb.blue);
								final byte a = hasAlpha ? (byte) (alphaValue) : (byte) (255);
								
								final int index = (y*mImageWidth + x) * 4;
								
								this.mImage[index] = r;
								this.mImage[index+1] = g;
								this.mImage[index+2] = b;
								this.mImage[index+3] = a;
							} else {
								final int index = (y*mPower2Width + x) * 4;
								mBuffer[index] = 0;
								mBuffer[index+1] = 0;
								mBuffer[index+2] = (byte)0;
								mBuffer[index+3] = (byte)0;
							} 
						}
					}
				} catch (FileNotFoundException e) {
					recycleArrays();
				}
			} else {
				recycleArrays();
			}
		}
		//
		rippleSpread();
		rippleRender();
	} 

	// ĳ����һʱ�̵Ĳ����㷨Ϊ�����������ĵ�Ĳ���͵�һ���ȥ��ǰ����
	// X0' =��X1 + X2 + X3 + X4��/ 2 - X0
	// 

	void rippleSpread() {
		final int m_width = this.mImageWidth;
		final int m_height = this.mImageHeight;

		final short[] m_buf1 = this.mOffset1;
		final short[] m_buf2 = this.mOffset2;

		int pixels = m_width * (m_height - 1);
		for (int i = m_width; i < pixels; ++i) {
			// ������ɢ:���������ĵ�Ĳ���͵�һ���ȥ��ǰ����
			// X0' =��X1 + X2 + X3 + X4��/ 2 - X0
			m_buf2[i] = (short) (((m_buf1[i - 1] + m_buf1[i + 1] + m_buf1[i - m_width] + m_buf1[i + m_width]) >> 1) - m_buf2[i]);
			// ����˥�� 1/32
			m_buf2[i] -= m_buf2[i] >> 5;
		}
		// ����������ݻ�����
//		final short[] temp = m_buf1;
//		m_buf1 = m_buf2;
//		m_buf2 = temp; 
		this.mOffset1 = m_buf2;
		this.mOffset2 = m_buf1;
	} 

	void rippleRender() {
		final int m_width = this.mImageWidth;
		final int m_height = this.mImageHeight;

		final short[] m_buf1 = this.mOffset1;
		int offset = 0;
		int i = m_width;
		int length = m_width * m_height;
		for (int y = 1; y < m_height - 1; ++y) {
			for (int x = 0; x < m_width; ++x, ++i) {
				// �����ƫ�����غ�ԭʼ���ص��ڴ��ַƫ���� :
				// offset = width * yoffset + xoffset
				offset = (m_width * (m_buf1[i - m_width] - m_buf1[i + m_width])) + (m_buf1[i - 1] - m_buf1[i + 1]);
				// �ж�����Ƿ��ڷ�Χ��
				if (i + offset > 0 && i + offset < length && offset != 0) { 
//					offset = 0; 
				} else { 
					offset = 0; 
				} 
				final int index = (i + offset) * 4; 
				final int index2 = (y * mPower2Width + x) * 4; 
				for(int k=0; k<4; k++) { 
					this.mBuffer[index2+k] =  this.mImage[index+k];
				} 
			} 
		} 
	} 
	
	// stoneSize    : ��Դ�뾶 
	// stoneWeight : ��Դ���� 
	// 
	void dropStone(int x, int y, int stoneSize, int stoneWeight) {
		final int m_width = this.mImageWidth;
		final int m_height = this.mImageHeight;

		final short[] m_buf1 = this.mOffset1;
	    // �ж�����Ƿ��ڷ�Χ��
	    if ((x + stoneSize) > m_width || (y + stoneSize) > m_height || (x - stoneSize) < 0 || (y - stoneSize) < 0) {
	       return;
	    } 
	 
	    int value = stoneSize * stoneSize;
	    short weight = (short)-stoneWeight;
	    for (int posx = x - stoneSize; posx < x + stoneSize; ++posx) {
	    	for (int posy = y - stoneSize; posy < y + stoneSize; ++posy) {
	    		if ((posx - x) * (posx - x) + (posy - y) * (posy - y)  < value) {
	                	m_buf1[m_width * posy + posx] = weight;
	    		} 
	    	}
	    } 
	} 
	
	public boolean onTouch(ElfEvent event) { 
		if(event.action == MotionEvent.LEFT_DOWN) { 
			final ElfPointf ret = new ElfPointf(); 
			this.screenXYToChild(event.x, event.y, ret); 
			dropStone((int)ret.x + mImageWidth/2, mImageHeight/2 - (int)ret.y, ElfRandom.nextInt(2, 10), ElfRandom.nextInt(10, 200)); 
		} 
		return false;
	} 
	
	public void drawSelf() { 
		bindAndDraw();
	} 

	void bindAndDraw() { 
		GL11.glDeleteTextures(mBindId[0]); 
		mBindId[0] = GL11.GL_INVALID_VALUE; 
		
		if (mBuffer != null) { 
			
//			GL11.glGenTextures(mBindId.length, mBindId); 
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, mBindId[0]); 
			
			BufferHelper.sByteBuffer.position(0);
			BufferHelper.sByteBuffer.put(mBuffer);
			BufferHelper.sByteBuffer.position(0);
			
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, 4, mPower2Width, mPower2Height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, BufferHelper.sByteBuffer);

			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, TextureOptions.DEFAULT.mMinFilter);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, TextureOptions.DEFAULT.mMagFilter);

			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, TextureOptions.DEFAULT.mWrapS);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, TextureOptions.DEFAULT.mWrapT); 
			
			GLHelper.glBindTexture(mBindId[0]); 
			GL11.glBegin(GL11.GL_QUADS); 
			//rb
			GL11.glTexCoord2f((float)mImageWidth/mPower2Height, (float)mImageHeight/mPower2Height);
			GL11.glVertex3f(mImageWidth/2f, -mImageHeight/2f, 0);
			//lb
			GL11.glTexCoord2f(0, (float)mImageHeight/mPower2Height);
			GL11.glVertex3f(-mImageWidth/2f, -mImageHeight/2f, 0);
			//lt
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(-mImageWidth/2f, mImageHeight/2f, 0);
			//rt
			GL11.glTexCoord2f((float)mImageWidth/mPower2Height, 0);
			GL11.glVertex3f(mImageWidth/2f, mImageHeight/2f, 0);
			GL11.glEnd();
		} 
	}
}