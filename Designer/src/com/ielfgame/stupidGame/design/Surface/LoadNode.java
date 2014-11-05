package com.ielfgame.stupidGame.design.Surface;

import java.util.Collection;
import java.util.LinkedList;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeText.TextNode;
import elfEngine.opengl.GLManage;

public class LoadNode extends ElfNode{ 
	private final LinkedList<String> mLoadList = new LinkedList<String>();
	private int mLoadCount = 0;
	private final TextNode mLoadText = new TextNode(this, 0);
	
	public LoadNode(final ElfNode node, int ordianl) {
		super(node, ordianl);
		
		mLoadText.setText("载入中...0%"); 
		mLoadText.setAnchorPosition(0.5f, 0.5f);
		mLoadText.setPosition(0, -100);
		mLoadText.addToParent();
	} 
	
	public void load(final Collection<String> resids) { 
		mLoadList.addAll(resids); 
		mLoadCount = 0;
		
		this.setPaused(false);
		this.setVisible(true);
	} 
	
	public void calc(final float t) {
		if(mLoadList.isEmpty()) { 
			this.setPaused(true); 
			this.setVisible(false);
			
			if(mOnLoadFinished != null) {
				mOnLoadFinished.run();
			}
		} else {
			final String key = mLoadList.getFirst();
			mLoadList.removeFirst();
			GLManage.loadTextureRegion(key, this.getGLId());
			mLoadCount++;
			
			final int size = mLoadList.size() + mLoadCount;
			if(size > 0) {
				final int percentage = mLoadCount*100/size; 
				mLoadText.setText("载入中..."+percentage+"%");
			} else {
				mLoadText.setText("空载入中..."+100+"%"); 
			} 
		}
	} 
	
	public void setOnLoadFinished(final Runnable run) {
		mOnLoadFinished = run;
	}
	
	private Runnable mOnLoadFinished;
}
