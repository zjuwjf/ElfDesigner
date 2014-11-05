package elfEngine.basic.node.nodeMap;

import java.util.LinkedList;

import com.ielfgame.stupidGame.ResJudge;
import com.ielfgame.stupidGame.NodeView.NodeViewWorkSpaceTab;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.data.ElfPointi;
import com.ielfgame.stupidGame.platform.PlatformHelper;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.utils.FileHelper;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeAnimate.AnimateNode;
import elfEngine.basic.node.nodeAnimate.AnimateNode.AnimateFrameNode;
import elfEngine.basic.touch.ElfEvent;
import elfEngine.basic.touch.MotionEvent;
import elfEngine.opengl.GLManage;
import elfEngine.opengl.TextureRegion;

public class MapTileNode extends ElfNode{
	
	public static class Int4 { 
		public int [] value = new int[4];
		public int sum() { 
			return value[0] + value[1] + value[2] + value[3];
		} 
		public void set(final Int4 value) {
			this.value[0] = value.value[0];
			this.value[1] = value.value[1];
			this.value[2] = value.value[2];
			this.value[3] = value.value[3];
		}
	} 
	
	public MapTileNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		
		setGridSize(new ElfPointi(10, 10));
		this.setUseSettedSize(true);
	}
	
	public String [] getSelfResids() {
		return getTileResid16();
	}
	
	public void setSelfResids(final String[] resids) {
		this.setTileResid16(resids);
	}
	
	protected String mTileFolder;
	public String getTileFolder() {
		return mTileFolder;
	}
	public void setTileFolder(String mTileFolder) {
		this.mTileFolder = mTileFolder;
		final String [] paths = FileHelper.getFullPaths(mTileFolder);
		setTileResid16(paths);
	} 
	protected final static int REF_TileFolder = DEFAULT_SHIFT | DROP_RESID_SHIFT;
	//16 zhang
	//
	//
	protected final String [] mTileResids = new String[16];
	public void setTileResid16(String [] tiles) {
		if(tiles != null && tiles.length >= mTileResids.length) {
			ElfPointf size = null;
			for(int i=0; i<mTileResids.length; i++) {
				mTileResids[i] = PlatformHelper.toCurrentPath( tiles[i] );
				if(size == null) {
					size = GLManage.getSize(mTileResids[i]);
				}
			}
			if(size != null) { 
				this.setTileSize(new ElfPointf(Math.round(size.x), Math.round(size.y))); 
			} 
		}
	}
	public String [] getTileResid16() {
		return mTileResids;
	}
	protected final static int REF_TileResid16 = DEFAULT_SHIFT;
	
	protected final ElfPointi mGridSize = new ElfPointi(10, 10);
	protected final ElfPointf mTileSize = new ElfPointf(10, 10);
	protected final ElfPointf mTileScale = new ElfPointf(1,1);
	
	public void setTileScale(ElfPointf scale) {
		mTileScale.set(scale);
		setTileResid16(this.mTileResids);
	}
	public ElfPointf getTileScale() {
		return mTileScale;
	}
	protected final static int REF_TileScale = DEFAULT_SHIFT;
	
	//4
	protected Int4 [] mGridValue = new Int4[100];
	
	public void setGridSize(final ElfPointi size) {
		if(size != null && size.x > 0 && size.y > 0) {
			final Int4 [] newGridValue = new Int4[size.x*size.y];
			for(int i=0; i<newGridValue.length; i++) {
				newGridValue[i] = new Int4();
			} 
			
			mGridSize.set(size); 
			mGridValue = newGridValue; 
			
			this.setSize(mGridSize.x*mTileSize.x, mGridSize.y*mTileSize.y);
		} 
	}
	public ElfPointi getGridSize() {
		return mGridSize;
	}
	protected final static int REF_GridSize = DEFAULT_SHIFT;
	
	public void setTileSize(ElfPointf size) {
		mTileSize.setPoint(size.x*mTileScale.x, size.y*mTileScale.y);
		this.setSize(mGridSize.x*mTileSize.x, mGridSize.y*mTileSize.y);
	}
	public ElfPointf getTileSize() {
		return mTileSize;
	}
//	protected final static int REF_TileSize = DEFAULT_SHIFT;
	
	public void setGridValue(final Int4 [] value) {
		if(value != null && value.length == mGridValue.length ) {
			for(int i=0; i<value.length; i++) {
				mGridValue[i].set(value[i]);
			}
		}
	}
	
	public Int4 [] getGridValue() {
		return mGridValue;
	}
	protected final static int REF_GridValue = FACE_ALL_SHIFT | XML_ALL_SHIFT | COPY_SHIFT | PASTE_SHIFT | UNDO_SHIFT;
	
	
	public void drawSelf() {
		final float offX = -(mGridSize.x-1)/2.0f;
		final float offY = (mGridSize.y-1)/2.0f;
		
		for(int i=0; i<mGridSize.x; i++) {
			for(int j=0; j<mGridSize.y; j++) {
				final Int4 int4 = getInt4(i,j);
				final int value = Math.min(int4.sum(),15);
				final String tileKey = mTileResids[value];
				final TextureRegion tr = GLManage.loadTextureRegion(tileKey, getGLId());
				if(tr != null) { 
					tr.draw((offX+i)*mTileSize.x, (offY-j)*mTileSize.y, mTileScale.x, mTileScale.y,0);
				} 
			} 
		} 
	} 
	
	public boolean onPreSelectTouchSelf(ElfEvent event) {
		if(this.isSelected() && 
				(event.action == MotionEvent.RIGHT_DOWN || event.action == MotionEvent.RIGHT_MOVE)) { 
			final ElfPointf touchPos = this.convertTouchToSelf(event);
			
			final int x = (int)((touchPos.x+this.getSize().x/2)/mTileSize.x);
			final int y = (int)((this.getSize().y/2-touchPos.y)/mTileSize.y);
			
			boolean ret = click(mGridValue, x, y, (event.stateMask & PlatformHelper.CTRL) != 0); 
			if(ret) {
				return ret;
			} 
		} 
		
		return super.onPreSelectTouchSelf(event);
	}
	
	public ElfNode toElfNode() {
		final ElfNode node = new ElfNode(null, 1);
		ElfNode.copyFrom(node, this);
		
		node.setParent(this.getParent());
		
		final float offX = -(mGridSize.x-1)/2.0f;
		final float offY = (mGridSize.y-1)/2.0f;
		
		for(int i=0; i<mGridSize.x; i++) {
			for(int j=0; j<mGridSize.y; j++) {
				final Int4 int4 = getInt4(i,j);
				final int value = Math.min(int4.sum(),15);
				final String tileKey = mTileResids[value];
				
				if(ResJudge.isRes(tileKey)) {
					final ElfNode child = new ElfNode(node, Integer.MAX_VALUE); 
					
					child.setResid(tileKey);
					child.setPosition((offX+i)*mTileSize.x, (offY-j)*mTileSize.y);
					child.setName(String.format("%dX%d", i,j));
					
					child.setScale(mTileScale.x, mTileScale.y);
					
					child.addToParent();
				} 
			} 
		} 
		
		return node;
	}
	
	public void setToElfNode() {
		final ElfNode node = toElfNode();
		final NodeViewWorkSpaceTab view = PowerMan.getSingleton(NodeViewWorkSpaceTab.class);
		if(view != null) {
			view.addNode(node.getParent(), node, Integer.MAX_VALUE, false);
		}
	}
	protected final static int REF_ToElfNode = FACE_SET_SHIFT;
	
	public void setToAnimateFrames(final Int4 [] other) {
		if(other != null && other.length == mGridValue.length ) {
			final Int4 [] mes = new Int4[mGridValue.length];
			for(int i=0; i<mGridValue.length; i++) {
				mes[i] = new Int4();
				mes[i].set(mGridValue[i]);
			} 
			
			boolean flag = false;
			final LinkedList<ElfNode> list = new LinkedList<ElfNode>();
			
			do {
				flag = false;
				
				final LinkedList<ElfPointi> clickList = new LinkedList<ElfPointi>();
				final LinkedList<ElfPointi> easeList = new LinkedList<ElfPointi>();
				
				for(int i=0; i<mGridSize.x; i++) {
					for(int j=0; j<mGridSize.y; j++) {
						final Int4 me = mes[j*mGridSize.x + i];
						final Int4 you = other[j*mGridSize.x + i];
						//2,8,1,4
						boolean isSame = false;
						int x=i, y=j;
						//up
						if(j>0) { 
							x = i; 
							y = j-1; 
							final Int4 me2 = mes[y*mGridSize.x + x]; 
							final Int4 you2 = other[y*mGridSize.x + x]; 
							isSame = (me2.value[3] == you2.value[3]); 
						} 
						//down
						if(!isSame && j<mGridSize.y-1) {
							x = i;
							y = j+1;
							final Int4 me2 = mes[y*mGridSize.x + x];
							final Int4 you2 = other[y*mGridSize.x + x];
							isSame = (me2.value[3] == you2.value[3]);
						}
						//left
						if(!isSame && i>0) {
							x = i-1;
							y=j;
							final Int4 me2 = mes[y*mGridSize.x + x];
							final Int4 you2 = other[y*mGridSize.x + x];
							isSame = (me2.value[3] == you2.value[3]);
						}
						//right
						if(!isSame && i<mGridSize.x-1) {
							x = i+1;
							y=j;
							final Int4 me2 = mes[y*mGridSize.x + x];
							final Int4 you2 = other[y*mGridSize.x + x];
							isSame = (me2.value[3] == you2.value[3]);
						}
						
						if(isSame && me.value[3] != 4 && you.value[3] == 4) {
							flag = true;
							clickList.add(new ElfPointi(i, j));
						}
						else if(isSame && me.value[3] == 4 && you.value[3] != 4) {
							flag = true;
							easeList.add(new ElfPointi(i, j));
						}
					}
				}
				
				if(flag) {
					for(ElfPointi p : clickList) {
						click(mes, p.x, p.y, false);
					} 
					for(ElfPointi p : easeList) { 
						click(mes, p.x, p.y, true);
					} 
				}
				
				//new node
				final MapTileNode node = new MapTileNode(null,Integer.MAX_VALUE);
				ElfNode.copyFrom(node, this);
				node.setGridValue(mes);
				list.add(node);
				
			} while(flag);
			
			final AnimateNode animateNode = new AnimateNode(this.getParent(), Integer.MAX_VALUE);
			for(ElfNode node : list) {
				final AnimateFrameNode frame = new AnimateFrameNode(animateNode, Integer.MAX_VALUE);
				node.setParent(frame);
				node.addToParent();
				frame.addToParent();
			}
			
			System.err.println("frames "+list.size());
			
			final NodeViewWorkSpaceTab view = PowerMan.getSingleton(NodeViewWorkSpaceTab.class);
			if(view!=null) {
				view.addNode(this.getParent(), animateNode, Integer.MAX_VALUE, false);
			} else {
				animateNode.addToParent();
			}
		}
	}
	protected final static int REF_ToAnimateFrames = FACE_SET_SHIFT;
	
	final boolean click(final Int4[] map, int x, int y, boolean ease) {
		/*
		 * if(arr[_my][_mx][3]!=4) arr[_my][_mx][3]+=4; // 当前区块的右下
    		if(arr[_my][_mx+1][1]!=8) arr[_my][_mx+1][1]+=8; // 右边区块的左下
    		if(arr[_my+1][_mx][2]!=1)arr[_my+1][_mx][2]+=1; // 下面区块的右上
    		if(arr[_my+1][_mx+1][0]!=2)arr[_my+1][_mx+1][0]+=2; // 下面右边区块的左上
		 */
		if(map == null || map.length < mGridSize.x * mGridSize.y) {
//			return false;
		}
		
		boolean ret = false;
		if(x>=0 && x < mGridSize.x && y>=0 && y<mGridSize.y) {
			final Int4 int4 = map[y*mGridSize.x + x];
			if(!ease && int4.value[3] != 4) {
				int4.value[3] += 4; 
				ret = true;
			}
			if(ease && int4.value[3] == 4) {
				int4.value[3] = 0; 
				ret = true;
			}
		}
		
		x++;
		if(x>=0 && x < mGridSize.x && y>=0 && y<mGridSize.y) {
			final Int4 int4 = map[y*mGridSize.x + x];
			if(!ease && int4.value[1] != 8) {
				int4.value[1] += 8; 
				ret = true;
			}
			if(ease && int4.value[1] == 8) {
				int4.value[1] = 0; 
				ret = true;
			}
		}
		
		x--;
		y++;
		if(x>=0 && x < mGridSize.x && y>=0 && y<mGridSize.y) {
			final Int4 int4 = map[y*mGridSize.x + x];
			if(!ease && int4.value[2] != 1) {
				int4.value[2] += 1; 
				ret = true;
			}
			if(ease && int4.value[2] == 1) {
				int4.value[2] = 0; 
				ret = true;
			}
		}
		
		x++;
		if(x>=0 && x < mGridSize.x && y>=0 && y<mGridSize.y) {
			final Int4 int4 = map[y*mGridSize.x + x];
			if(!ease && int4.value[0] != 2) {
				int4.value[0] += 2; 
				ret = true;
			}
			if(ease && int4.value[0] == 2) {
				int4.value[0] = 0; 
				ret = true;
			}
		}
		
		return ret;
	} 
	
	final Int4 getInt4(final int x, final int y) {
		return mGridValue[y*mGridSize.x + x];
	}
}
