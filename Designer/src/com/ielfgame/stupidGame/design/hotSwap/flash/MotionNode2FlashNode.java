package com.ielfgame.stupidGame.design.hotSwap.flash;

import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.data.ElfDataDisplay;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.dialog.MessageDialog;
import com.ielfgame.stupidGame.dialog.MultiLineDialog;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.res.ResManager;
import com.ielfgame.stupidGame.utils.FileHelper;
import com.ielfgame.stupidGame.utils.FileHelper.FileType;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.ElfNode.IIterateChilds;
import elfEngine.basic.node.nodeAnimate.timeLine.ElfMotionKeyNode;
import elfEngine.basic.node.nodeAnimate.timeLine.ElfMotionNode;
import elfEngine.basic.node.nodeAnimate.timeLine.NodePropertyType;

public class MotionNode2FlashNode {
	private final static String sKeyStorageName = "KeyStorage";
	/***
	 * 
	 */
	public static void reduceElfMotionNode(final ElfMotionNode motionNode) {
		//
	}
	
	public static FlashMainNode convert(final ElfMotionNode motionNode) {
		reduceElfMotionNode(motionNode);
		
		final float speed = motionNode.getMotionSpeed();
		final int end = motionNode.getLoopEnd();
//		final LoopMode lm = motionNode.getLoopMode();
		final FlashMainNode flashMainNode = new FlashMainNode(motionNode.getParent(), 0);
		
		ElfNode.copyFrom(flashMainNode, motionNode);
		
		final KeyStorageNode ksnode = new KeyStorageNode(flashMainNode, -1);
		ksnode.addToParent();
		ksnode.setName("KeyStorage");
		
		flashMainNode.check();
		
		flashMainNode.setFPS(20);
		flashMainNode.setMaxF(Math.round(20*end/1000.0f/speed));
		
		final ElfNode [] roots = new ElfNode[2];
		
		final ElfNode [] childs = motionNode.getChilds();
		for(final ElfNode child : childs) {
			if(child.getName().contains(sKeyStorageName)) {
				continue;
			}
			//copy normal nodes
			final ElfNode copy = child.copyDeep(flashMainNode);
			copy.addToParent();
			copy.setPosition(0,0);
			
			final ElfPointf pos = child.getPosition();
			
			child.iterateChildsDeepWithSelf(new IIterateChilds() {
				public boolean iterate(final ElfNode node) {
					
					final ElfNode parent = node.getParent();
					if( parent != null && node.getName().equals("根节点") && parent.getName().equals("root")) {
						if(node.getClass() == ElfNode.class && parent.getClass() == ElfNode.class && parent.getChilds().length == 1) {
							final ElfNode grand = parent.getParent();
							if(grand == motionNode) {
								roots[0] = copy;
								roots[1] = copy.findBySimpleName("根节点");
								
								System.err.println("find grand....");
							}
						}
					}
					
					final KeyFrameArrayNode kfaNode = new KeyFrameArrayNode(ksnode, -1);
					kfaNode.setName(node.getName());
					
					boolean used = false;
					
					final NodePropertyType [] types = NodePropertyType.values();
					for(final NodePropertyType type : types) {
						final ElfMotionKeyNode[] keys = motionNode.findKeysByNode(node, type.toString());
						if(keys != null && keys.length >= 2) {
							
							for(ElfMotionKeyNode key : keys) {
								final int time = key.getTime();
								final int frame = Math.round(20*time/1000.0f/speed);
								
								KeyFrameNode keyframe = kfaNode.findFlashKeyByFrame(frame);
								
								if(keyframe == null) {
									motionNode.setProgress(time);
									
									keyframe = kfaNode.createFlashKeyByFrame(frame);
									kfaNode.addFlashKey(keyframe);
									
									keyframe.setPosition( node.getPosition() );
									keyframe.setScale( node.getScale() );
									keyframe.setRotate( node.getRotate() );
									keyframe.setColor( node.getColor() );
									keyframe.setVisible( node.getVisible() );
									keyframe.setResid( node.getResid() );
									
									keyframe.setInterType(key.getInterType());
									keyframe.setLoopMode(key.getLoopMode());
									
									if(node.getParent() == child) {
										keyframe.translate(pos.x, pos.y);
									}
									
									used = true;
								}
							}
						}
					}
					
					if(used) {
						kfaNode.addToParent();
					} else {
						
						motionNode.setProgress(0);
						
						KeyFrameNode keyframe = kfaNode.createFlashKeyByFrame(0);
						kfaNode.addFlashKey(keyframe);
						
						keyframe.setPosition( node.getPosition() );
						keyframe.setScale( node.getScale() );
						keyframe.setRotate( node.getRotate() );
						keyframe.setColor( node.getColor() );
						keyframe.setVisible( node.getVisible() );
						keyframe.setResid( node.getResid() );
						
						if(node.getParent() == child) {
							keyframe.translate(pos.x, pos.y);
						}
						
						keyframe = kfaNode.createFlashKeyByFrame(flashMainNode.getMaxF());
						kfaNode.addFlashKey(keyframe);
						
						keyframe.setPosition( node.getPosition() );
						keyframe.setScale( node.getScale() );
						keyframe.setRotate( node.getRotate() );
						keyframe.setColor( node.getColor() );
						keyframe.setVisible( node.getVisible() );
						keyframe.setResid( node.getResid() );
						
						if(node.getParent() == child) {
							keyframe.translate(pos.x, pos.y);
						}
						
						kfaNode.addToParent();
					}
					
					return false;
				}
			});
		}
		
		flashMainNode.setAnimateName(ksnode.getName());
		ksnode.recheckTargets();
		
		if(roots[0] != null && roots[1] != null) {
			System.err.println("roots...");
			
//			ksnode.
			final KeyFrameArrayNode kfaRoot = (KeyFrameArrayNode)ksnode.findBySimpleName(roots[0].getName());
			final KeyFrameArrayNode kfaGen = (KeyFrameArrayNode)ksnode.findBySimpleName(roots[1].getName());
			
			assert(kfaRoot != null);
			assert(kfaGen != null);
			
			if(kfaRoot != null && kfaGen != null) {
				System.err.println("roots in ...");
				
				final KeyFrameNode k = kfaRoot.findFlashKeyByFrame(0);
				final ElfPointf pos = k.getPosition();
				
				final KeyFrameNode [] ks = kfaGen.getFlashKeys();
				for(KeyFrameNode key : ks) {
					key.getPosition().translate(pos.x, pos.y);
				}
				
				kfaRoot.removeFromParent();
				kfaGen.setName(kfaRoot.getName());
				
				roots[0].removeFromParentView(false);
				roots[1].removeFromParentView(false);
				
				roots[1].setName(roots[0].getName());
				roots[1].setParent(flashMainNode);
				roots[1].addToParent();
				
				ksnode.recheckTargets();
			}
		}
		
		ksnode.setName(flashMainNode.getName().replace("@", ""));
		
		return flashMainNode;
	}
	
	public final static void exportImages(final ElfMotionNode mainNode) {
		final MultiLineDialog mld = new MultiLineDialog();
		final AnimateExport export = new AnimateExport();
		
		export.path_REF_DIR = ResManager.getSingleton().getDesignerImageAsset();
		export.frames = 10;
		export.name = mainNode.getName();
		export.width = Math.round(mainNode.getSize().x);
		export.height = Math.round(mainNode.getSize().y);
		
		final Object [] objs = mld.open(export);
		if(objs != null) {
			export.setValues(objs);
			
			if( FileHelper.getFileType(export.path_REF_DIR) != FileType.DIR ) {
				final MessageDialog md = new MessageDialog();
				md.open("", export.path_REF_DIR + " 不是一个存在的文件夹！");
			} else if(export.frames<=0||export.width<=0||export.height<=0) {
				final MessageDialog md = new MessageDialog();
				md.open("", "参数不合法！");
			} else {
				
				PowerMan.getSingleton(DataModel.class).getScreenNode().runWithDelay(new Runnable() {
					public void run() {
						exportImages(mainNode, export);
						
						final MessageDialog md = new MessageDialog();
						md.open("", "导出成功！");
					}
				}, 0);
			}
		}
	}
	
	public final static void exportImages(final FlashMainNode mainNode) {
		
		final MultiLineDialog mld = new MultiLineDialog();
		final AnimateExport export = new AnimateExport();
		
		export.path_REF_DIR = ResManager.getSingleton().getDesignerImageAsset();
		export.frames = Math.round( mainNode.getMaxF()/mainNode.getSpeedRate() );
		export.name = mainNode.getAnimateName();
		export.width = Math.round(mainNode.getSize().x);
		export.height = Math.round(mainNode.getSize().y);
		
		final Object [] objs = mld.open(export);
		if(objs != null) {
			export.setValues(objs);
			
			if( FileHelper.getFileType(export.path_REF_DIR) != FileType.DIR ) {
				final MessageDialog md = new MessageDialog();
				md.open("", export.path_REF_DIR + " 不是一个存在的文件夹！");
			} else if(export.frames<=0||export.width<=0||export.height<=0) {
				final MessageDialog md = new MessageDialog();
				md.open("", "参数不合法！");
			} else {
				
				PowerMan.getSingleton(DataModel.class).getScreenNode().runWithDelay(new Runnable() {
					public void run() {
						exportImages(mainNode, export);
						
						final MessageDialog md = new MessageDialog();
						md.open("", "导出成功！");
					}
				}, 0);
			}
		}
	}
	
	private final static void exportImages(final ElfMotionNode mainNode, final AnimateExport export) {
		
		final ElfPointf oldSize = mainNode.getSize();
		final boolean setted = mainNode.getUseSettedSize();
		
		mainNode.setUseSettedSize(true);
		mainNode.setSize(new ElfPointf(export.width, export.height));
		
		final int beg = mainNode.getLoopStart();
		final int end = mainNode.getLoopEnd();
		
		final int step = (end-beg)/export.frames;
		
		for(int i=beg; i<end; i+=step) {
			mainNode.setProgress(i);
			mainNode.writeToPNG(export.path_REF_DIR +FileHelper.DECOLLATOR+ export.name+i+".png", 1, 1, 1);
		}
		
		mainNode.setSize(oldSize);
		mainNode.setUseSettedSize(setted);
	}
	
	private final static void exportImages(final FlashMainNode mainNode, final AnimateExport export) {
		
		final int max = mainNode.getMaxF();
		final float rate = max/(float)export.frames;
		
		final ElfPointf oldSize = mainNode.getSize();
		final boolean setted = mainNode.getUseSettedSize();
		
		mainNode.setUseSettedSize(true);
		mainNode.setSize(new ElfPointf(export.width, export.height));
		
		for(int i=0; i<export.frames; i++) {
			mainNode.setProgressTime( Math.round( rate * 1000.0f/mainNode.getFPS() * i ));
			mainNode.writeToPNG(export.path_REF_DIR +FileHelper.DECOLLATOR+ export.name+i+".png", 1, 1, export.scale);
		}
		
		mainNode.setSize(oldSize);
		mainNode.setUseSettedSize(setted);
	}
	
	private static class AnimateExport extends ElfDataDisplay {
		public String path_REF_DIR;
		public String name;
		public int frames;
		public int width;
		public int height;
		public float scale = 1;
	}
}
