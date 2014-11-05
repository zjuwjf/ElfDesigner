package com.ielfgame.stupidGame.data;

import java.util.ArrayList;

import com.ielfgame.stupidGame.animation.Animate;
import com.ielfgame.stupidGame.face.action.CCActionData;
import com.ielfgame.stupidGame.power.APowerManSingleton;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.trans.TransferRes;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.extend.ElfScreenNode;
import elfEngine.opengl.GLManage;

public class DataModel extends APowerManSingleton {
	
	public void setAutoSaveTime(float saveTime) {
//		if(saveTime > 0.1f) { 
//			PowerMan.getSingleton(OtherConfig.class).AutoSaveMinute = saveTime;
//		} 
	}
	public float getAutoSaveTime() {
		return 1000000;
	}
	
	public boolean getErrToLog() {
		return false;
	}
	public void setErrToLog(boolean errToLog) {
//		PowerMan.getSingleton(OtherConfig.class).ErrToLog = errToLog;
	} 
	
	private final CCActionData mRootCCActionData = new CCActionData();
	private final CCActionData mRecycleCCActionData = new CCActionData();
	
	public void clearCCActionDataList() {
		mRootCCActionData.mChildsList.clear();
	} 
	public void addCCActionData(CCActionData data) {
		mRootCCActionData.mChildsList.add(data);
	} 
	public CCActionData getRootCCActionData() {
		return mRootCCActionData;
	}
	
	public void clearRecycleCCActionDataList() {
		mRecycleCCActionData.mChildsList.clear();
	} 
	public void addRecycleCCActionData(CCActionData data) {
		mRecycleCCActionData.mChildsList.add(data);
	} 
	public CCActionData getRecycleCCActionData() {
		return mRecycleCCActionData;
	}
	
	private String mLastPath;
	public void setLastImportPath(final String path) {
		mLastPath = path;
	} 
	public String getLastImportPath() { 
		return mLastPath;
	} 
	
	private String mVersion;
	public void setVersion(String version) {
		mVersion = version;
	}
	
	public String getVersion() {
		return mVersion;
	}
	
	// node module
	private final ElfScreenNode mScreenNode = new ElfScreenNode();
	
	public static class RootScreenNode extends ElfNode {
		public RootScreenNode(ElfNode father, int ordinal) {
			super(father, ordinal);
		}
		
		public void setSelected(boolean isSelected) {
		}
		
		public void myRefresh(){ 
			super.myRefresh();
			resetStatic();
			TransferRes.clearPathMap();
			GLManage.clearSizeMap();
			GLManage.clearUnusedTextureRegion();
			GLManage.clearAllUnFoundResid();
			System.err.println("refresh static");
		} 
	}
	
	private final ElfNode mRootScreen = new RootScreenNode(null, -1);
	
	private final ElfNode mRootRecycle = new ElfNode(null, -1) {
		public void setSelected(boolean isSelected) {
		}
	};
	
	private final ArrayList<String> mPicPathList = new ArrayList<String>();
	public ArrayList<String> getPicPathList() {
		return mPicPathList;
	}

	public static class GLColor {
		public float r = 0, g = 0, b = 0, a = 1;
		public void set(float r, float g, float b, float a) {
			this.r = r;
			this.g = g;
			this.b = b;
			this.a = a;
		} 
	}

	
	private final GLColor mPreviewScreenColor = new GLColor();

	public DataModel() {
		mRootScreen.setName("#screen");
		mRootRecycle.setName("#recycle");
	}

//	public GLColor getScreenColor() {
//		return mScreenColor;
//	}

	public GLColor getPreviewScreenColor() {
		return mPreviewScreenColor;
	}

	public ElfScreenNode getScreenNode() {
		return mScreenNode;
	}
	
	public ElfNode getRootScreen() {
		return mRootScreen;
	}

	public ElfNode getRootRecycle() {
		return mRootRecycle;
	}

	public ArrayList<ElfNode> getSelectNodeList() {
		return mScreenNode.getSelectNodeList();
	}
	
	public ElfNode getSelectNode() {
		return mScreenNode.getSelectNode();
	}
	
	public void setSelectNode(ElfNode elfBasicNode) {
		mScreenNode.setSelectNode(elfBasicNode);
	}

	// Animate module
	private final ArrayList<Animate> mAnimateList = new ArrayList<Animate>();

	public ArrayList<Animate> getAnimateList() {
		return mAnimateList;
	}

	public Animate getAnimate(String key) {
		final ArrayList<Animate> list = PowerMan.getSingleton(DataModel.class)
				.getAnimateList();
		for (Animate animate : list) {
			if (animate.mName.equals(key)) {
				return animate;
			} 
		} 
		return null;
	} 

	// Modifier module
	private final ArrayList<String[]> mModifierList = new ArrayList<String[]>();
	
	public ArrayList<String[]> getModifierList() {
		return mModifierList;
	}
}
