//package com.ielfgame.stupidGame.design.hotSwap.flash;
//
//import elfEngine.basic.node.ElfNode;
//
//public class FlashComNode extends ElfNode {
//	
//	private String mFlashName;
//	private FlashMainNode mFlashMainNode;
//
//	public FlashComNode(ElfNode father, int ordinal) {
//		super(father, ordinal);
//	}
//	
//	public String getFlashName() {
//		return mFlashName;
//	}
//
//	public void setFlashName(String flashName) {
//		this.mFlashName = flashName;
//		mFlashMainNode = null;
//	}
//	
//	public void check() {
//		if(mFlashMainNode == null) {
//			final ElfNode topnode = this.getTopNode();
//			
//			topnode.iterateChildsDeep(new IIterateChilds() {
//				public boolean iterate(final ElfNode node) {
//					final String childFullName = node.getFullName();
//					if (childFullName != null && childFullName.equals(mFlashName)) {
//						if(node instanceof FlashMainNode) {
//							mFlashMainNode = (FlashMainNode)node;
//							return true;
//						}
//					}
//					return false;
//				}
//			});
//		}
//		
//		if(mFlashMainNode != null) {
//			mFlashName = mFlashMainNode.getFullName();
//		}
//	}
//	
//	
//}
