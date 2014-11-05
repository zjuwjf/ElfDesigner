package elfEngine.basic.node.node3d;

import java.util.Arrays;
import java.util.Comparator;

import org.lwjgl.opengl.GL11;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.list.ElfList;
import elfEngine.basic.node.ElfNode;

public class Elf3DNode extends ElfNode {

	public Elf3DNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	} 
	
	private float mRotateX=0, mRotateY=0;
	private float mScaleZ = 1;
	
	public float getRotateX() {
		return mRotateX;
	}
	public void setRotateX(float mRotateX) {
		this.mRotateX = mRotateX;
	}
	public float getRotateY() {
		return mRotateY;
	}
	public void setRotateY(float mRotateY) {
		this.mRotateY = mRotateY;
	}
	public float getScaleZ() {
		return mScaleZ;
	}
	public void setScaleZ(float mScaleZ) {
		this.mScaleZ = mScaleZ;
	}
	protected final static int REF_RotateX = DEFAULT_SHIFT;
	protected final static int REF_RotateY = DEFAULT_SHIFT;
	protected final static int REF_ScaleZ = DEFAULT_SHIFT;
	
	protected void transformToAnchor() { 
		final ElfPointf pos = getPosition();
		GL11.glTranslatef(pos.x, pos.y, getZ());
		
		GL11.glRotatef(getRotate(), 0, 0, -1);
		GL11.glRotatef(mRotateX, -1, 0, 0);
		GL11.glRotatef(mRotateY, 0, -1, 0);
		final ElfPointf scale = getScale();
		GL11.glScalef(scale.x, scale.y, mScaleZ);
		
		if(mUseCamera) { 
			mElfCamera.locate(); 
		} 
	} 
	
	private final ElfCamera mElfCamera = new ElfCamera();
	private boolean mUseCamera = false;
	
	public void setCamera(ElfCamera camera) {
		mElfCamera.setCamera(camera);
	}
	public ElfCamera getCamera() {
		return new ElfCamera(mElfCamera);
	}
	protected final static int REF_Camera = DEFAULT_SHIFT;
	
	public void setUseCamera(boolean use) {
		mUseCamera = use;
	}
	public boolean getUseCamera() {
		return mUseCamera;
	}
	protected final static int REF_UseCamera = DEFAULT_SHIFT;
	
	private boolean mDrawOrdinalByZ = true;
	public void setDrawOrdinalByZ(boolean is) {
		mDrawOrdinalByZ = is;
	}
	public boolean getDrawOrdinalByZ() {
		return mDrawOrdinalByZ;
	}
	protected final static int REF_DrawOrdinalByZ = DEFAULT_SHIFT;
	
	public void setDefaultChildsZ() { 
		final float min = Float.MIN_VALUE;
		final int [] count = new int[]{0};
		this.iterateChildsDeep(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				count[0]++;
				node.setZ(min*count[0]);
				return false;
			} 
		});
	}
	protected final static int REF_DefaultChildsZ = FACE_SET_SHIFT;
	
	private boolean mUseDepthTest = false;
	public void setUseDepthTest(boolean use) {
		mUseDepthTest = use;
	}
	public boolean getUseDepthTest() {
		return mUseDepthTest;
	}
	protected final static int REF_UseDepthTest = DEFAULT_SHIFT;
	private boolean mOldDepthTest; 
	
	public void drawBefore() { 
		super.drawBefore(); 
		mOldDepthTest = GL11.glGetBoolean(GL11.GL_DEPTH_TEST); 
		if(mUseDepthTest != mOldDepthTest) { 
			if(mUseDepthTest) {
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			} else {
				GL11.glDisable(GL11.GL_DEPTH_TEST);
			} 
		} 
	} 
	
	public void drawAfter() { 
		super.drawAfter(); 
		if(mUseDepthTest != mOldDepthTest) { 
			if(mUseDepthTest) { 
				GL11.glDisable(GL11.GL_DEPTH_TEST);
			} else { 
				GL11.glEnable(GL11.GL_DEPTH_TEST); 
			}
		} 
	} 
	
	protected void drawSelectBefore() {
		super.drawSelectBefore();
	} 
	
	protected void drawSelectAfter() { 
		super.drawSelectAfter();
	} 
	
	protected void drawChilds() {
		if(mDrawOrdinalByZ) {
			int i = 0;
			final ElfList<ElfNode> childList = getChildList();
			final ElfList<ElfNode>.Iterator it = childList.iterator(false);
			final int size = childList.size();
			final ElfNode [] nodes = new ElfNode[size];
			
			while (it.hasPrev()) { 
				nodes[i++] = it.prev();
			} 
			
			Arrays.sort(nodes, new Comparator<ElfNode>(){ 
				public int compare(ElfNode o1, ElfNode o2) { 
					if(o1.getZ() < o2.getZ()) {
						return mUseDepthTest ? 1 : -1;
					} else if(o1.getZ() > o2.getZ()) { 
						return mUseDepthTest ? -1 : 1;
					} else { 
						return 0;
					} 
				} 
			});
			
			for(ElfNode node : nodes) {
				node.drawSprite();
			}
		} else {
			final ElfList<ElfNode> childList = getChildList();
			if (!childList.isEmpty()) { 
				if( getDrawReverse() ) { 
					final ElfList<ElfNode>.Iterator it = childList.iterator(false); 
					while (it.hasPrev()) { 
						it.prev().drawSprite();
					} 
				} else {
					final ElfList<ElfNode>.Iterator it = childList.iterator(true);
					while (it.hasNext()) {
						it.next().drawSprite();
					} 
				} 
			} 
		} 
	} 
}
