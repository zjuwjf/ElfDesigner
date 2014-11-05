package com.ielfgame.stupidGame.design.Surface;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Menu;

import _Res.code.InnerRes;

import com.ielfgame.stupidGame.GLView.GLID;
import com.ielfgame.stupidGame.GLView.IGLRender;
import com.ielfgame.stupidGame.config.ProjectSetting;
import com.ielfgame.stupidGame.face.action.CCActionData;
import com.ielfgame.stupidGame.lua.SearchCCObject.FileHelper;
import com.ielfgame.stupidGame.lua.SearchCCObject.FileHelper.FileType;
import com.ielfgame.stupidGame.power.ASuperManSingleton;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.power.SuperMan;
import com.ielfgame.stupidGame.xml.XMLVersionManage;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.ElfNode.IIterateChilds;
import elfEngine.basic.node.extend.ElfScreenNode;
import elfEngine.basic.node.extend.ElfScreenNode.IUpdateHandler;

public abstract class Surface extends ASuperManSingleton implements IUpdateHandler, KeyListener{ 
	
	private final ElfScreenNode mRootNode = new ElfScreenNode(); 
	protected final LinkedList<CCActionData> mActions = new LinkedList<CCActionData>(); 
	protected final String mXMLPath; 
	
	private final ElfNode mBindNode = new ElfNode(mRootNode, 0);
	private final LoadNode mLoadNode = new LoadNode(mRootNode, 0); 
	
	private final HashMap<String, ElfNode> mDynamicSet = new HashMap<String, ElfNode>();
	
	private SurfaceGLRender mRender;
	
	private int mPhysicWidth;
	private int mPhysicHeight;
	
	public void setPhysicWidth(int width) {
		mPhysicWidth = width;
	}
	
	public void setPhysicHeight(int height) {
		mPhysicHeight = height;
	}
	
	public int getPhysicWidth() {
		return mPhysicWidth;
	}

	public int getPhysicHeight() {
		return mPhysicHeight;
	} 
	
	public Surface(final String xmlPath) { 
		this.mXMLPath = xmlPath; 
		
		this.setPhysicWidth(PowerMan.getSingleton(ProjectSetting.class).getLogicWidth());
		this.setPhysicHeight(PowerMan.getSingleton(ProjectSetting.class).getLogicHeight());
		
		mRootNode.setGLid(GLID.GL_WINDOW_ID);
		
		mLoadNode.addToParent();
		
		mLoadNode.setOnLoadFinished(new Runnable() {
			public void run() { 
				mBindNode.setVisible(true);
				onStart();
				
				mRootNode.getUpdateHandlerList().addUpdateHandler(Surface.this);
			}
		});
		
		/**
		 * 
		 */
		final InputStream is = getXMLInputStream();
		final LinkedList<Object> objs = XMLVersionManage.readFromXML( is );
		
		try {
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(final Object obj : objs) { 
			if(obj instanceof ElfNode) { 
				final ElfNode node = (ElfNode)obj; 
				node.setParent(mBindNode); 
				node.addToParent(); 
			}  else if(obj instanceof CCActionData) { 
				final CCActionData data = (CCActionData)obj; 
				mActions.add(data); 
			} 
		} 
		
		mRootNode.setName("");
		mBindNode.setName("");
		
		mRootNode.bindNode(mBindNode);
		
		mBindNode.iterateChildsDeep(new IIterateChilds() { 
			public boolean iterate(final ElfNode node) { 
				final String name = node.getName(); 
				if(name.startsWith("@")) { 
					mDynamicSet.put(name, node); 
				} 
				return false;
			} 
		}); 
		
		final Set<String> set = mDynamicSet.keySet();
		for(String key : set) {
			final ElfNode node = mDynamicSet.get(key);
			node.removeFromParent();
		}
		
		startLoad();
	} 
	
//	public void
	
	public void setRender(final SurfaceGLRender render) {
		if(mRender != null) {
			mRender.removeKeyListener(this);
		}
		
		mRender = render;
		
		if(mRender != null) {
			mRender.addKeyListener(this);
		}
	}
	
	public IGLRender getRender() {
		return mRender;
	}
	
	public ElfNode createDynamicNode(final String key) {
		final ElfNode node = mDynamicSet.get(key);
		if(node != null) {
			return node.copyDeep();
		}
		return null;
	}
	
	public ElfScreenNode getRootNode() { 
		return mRootNode; 
	} 
	
	public abstract void onStart(); 
	public abstract void onFinished(); 
	
	public abstract void onResume(); 
	public abstract void onPause(); 
	
	public void onUpdate(float dt) {
		
	}
	
	public void startLoad() { 
		mBindNode.setVisible(false); 
		
		try {
			final InputStream is = getXMLInputStream();
			mLoadNode.load( XMLVersionManage.getAllResids(is) );
			
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	} 
	
	private final InputStream getXMLInputStream() {
		if( FileHelper.getFileType(mXMLPath) == FileType.NO) {
			final InputStream is = InnerRes.getXMLInputStream(mXMLPath);
			return is;
		} else {
			try {
				return new FileInputStream(new File(mXMLPath));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		return null;
	}
	
	public final ElfNode findNodeByName(final String name) {
		return mBindNode.findByName(name);
	}
	
	public final CCActionData findActionByName(final String name) {
		for(CCActionData data : this.mActions) {
			if(name!=null && name.equals(data.getName())) {
				return data; 
			} 
		}
		return null;
	}
	
	
	public String getXMLPath() {
		return mXMLPath;
	} 
	
	public void finish() { 
		
	} 
	
	public void goToSurface(final Surface surface) { 
		
	} 
	
	public void replaceSurface(final Surface surface) { 
		
	} 
	
	protected final static void setSurface(final Surface surface) {
		SuperMan.register(Surface.class, surface); 
	} 
	
	public void onMenuShow(final Menu menu, final float x, final float y) {
		System.err.println("Surface onMenuShow!");
	}
	
	public boolean mouseMove(MouseEvent event) {
		return false;
	}

	public boolean mouseDown(MouseEvent event) {
		return false;
	}

	public boolean mouseUp(MouseEvent event) {
		return false;
	}
	
	public void keyPressed(KeyEvent e) {
		
	}

	public void keyReleased(KeyEvent e) {
		
	}

	public boolean mouseDoubleClick(MouseEvent event) {
		return false;
	}

	public boolean mouseScrolled(MouseEvent event) {
		return false;
	}
	
//	public void clearSufaceStack() { 
//		sSurfaceStack.clear(); 
//	} 
//	
//	static Stack<Surface> sSurfaceStack = new Stack<Surface>(); 
} 
