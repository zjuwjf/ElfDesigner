package com.ielfgame.stupidGame.design.hotSwap.flash;

import java.util.ArrayList;

import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashStruct.IFlashKey;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashStruct.IFlashKeyArray;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.INodePropertyChange;
import elfEngine.basic.node.nodeAnimate.timeLine.NodePropertyType;

public class FlashUpdateManager {

	private final static FlashManager sFlashManager = PowerMan.getSingleton(FlashManager.class);
	private final static ArrayList<ElfNode> sFlashSelectList = new ArrayList<ElfNode>();

	public synchronized static ArrayList<ElfNode> updateSelects() {
		final ArrayList<ElfNode> selectList = PowerMan.getSingleton(DataModel.class).getSelectNodeList();

		/***
		 * clean old
		 */
		for (ElfNode node : sFlashSelectList) {
			node.setNodePropertyChange(null);
		}
		sFlashSelectList.clear();

		for (ElfNode node : selectList) {
			final IFlashKeyArray fka = sFlashManager.findFlashKeyArrayByTarget(node);
			if (fka != null) {
				sFlashSelectList.add(node);
			}
		}

		final int frame = sFlashManager.getCurrentFrame();

		for (ElfNode node : sFlashSelectList) {
			node.setNodePropertyChange(new INodePropertyChange() {
				// before
				private ElfPointf mLastPosition;
				private float mLastRotate;
				private ElfPointf mLastScale;

				public boolean onNodePropertyChangeBefore(ElfNode node, NodePropertyType type) {

					if (type == NodePropertyType.PositionType) {
						mLastPosition = node.getPosition();
					} else {
						mLastPosition = null;
					}

					if (type == NodePropertyType.RotateType) {
						mLastRotate = node.getRotate();
					} 
					
					if (type == NodePropertyType.ScaleType) {
						mLastScale = node.getScale();
					} else {
						mLastScale = null;
					}

					final IFlashKeyArray fka = sFlashManager.findFlashKeyArrayByTarget(node);
					if (fka == null) {
						return true;
					}

					if (fka.getLocked()) {
						return false;
					}

					// final IFlashKey [] keyarray = fka.getFlashKeys();
					// if(keyarray == null || keyarray.length ==0) {
					// return true;
					// }

					final IFlashKey key = fka.findFlashKeyByFrame(frame);
					if (key == null) {
						// final MessageDialog md = new MessageDialog();
						// boolean ret = md.open("", "要插入关键帧么 ? ");
						if (sFlashManager.getAutoKey()) {
							sFlashManager.addKeyFrameByKeyArray(fka, frame);
						}

						return true;
					}

					return true;
				}

				// after
				public void onNodePropertyChange(ElfNode node, NodePropertyType type) {
					final IFlashKeyArray fka = sFlashManager.findFlashKeyArrayByTarget(node);
					final IFlashKey key = fka.findFlashKeyByFrame(frame);

					if (key != null) {
						key.setPropertiesFromElfNode(node);
					}

					if (type == NodePropertyType.PositionType && mLastPosition != null) {
						final IFlashKey[] array = fka.getFlashKeys();

						int selectCount = 0;
						for (final IFlashKey k : array) {
							if (k.getFrameSelect()) {
								selectCount++;
							}
						}

						if (selectCount >= 0) {
							final ElfPointf newPosition = node.getPosition();
							for (final IFlashKey k : array) {
								if (k != key && k.getFrameSelect()) {
									k.setPosition(k.getPosition().translate(newPosition.x - mLastPosition.x, newPosition.y - mLastPosition.y));
								}
							}
						}
					} else if (type == NodePropertyType.RotateType) {
						// mLastRotate
						final IFlashKey[] array = fka.getFlashKeys();

						int selectCount = 0;
						for (final IFlashKey k : array) {
							if (k.getFrameSelect()) {
								selectCount++;
							}
						}

						if (selectCount >= 0) {
							final float newRotate = node.getRotate();
							for (final IFlashKey k : array) {
								if (k != key && k.getFrameSelect()) {
									k.setRotate(k.getRotate() + (newRotate-mLastRotate));
								}
							}
						}
					} else if(type == NodePropertyType.ScaleType && mLastScale != null) {
						final IFlashKey[] array = fka.getFlashKeys();

						int selectCount = 0;
						for (final IFlashKey k : array) {
							if (k.getFrameSelect()) {
								selectCount++;
							}
						}

						if (selectCount >= 0) {
							final ElfPointf newScale = node.getScale();
							for (final IFlashKey k : array) {
								if (k != key && k.getFrameSelect()) {
									k.setScale(k.getScale().mult(newScale.x/mLastScale.x, newScale.y/mLastScale.y));
								}
							}
						}
					}
				}
			});
		}

		return new ArrayList<ElfNode>(sFlashSelectList);
	}

}
