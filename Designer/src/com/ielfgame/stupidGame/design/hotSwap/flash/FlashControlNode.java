package com.ielfgame.stupidGame.design.hotSwap.flash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfDataDisplay;
import com.ielfgame.stupidGame.data.ElfDataXML;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.data.StringHelper;
import com.ielfgame.stupidGame.design.hotSwap.SurfaceFlashPanel;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashControlNode.KeySetting.ImportImageSetting;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashStruct.IFlashKey;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashStruct.IFlashKeyArray;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashStruct.IFlashMain;
import com.ielfgame.stupidGame.dialog.MessageDialog;
import com.ielfgame.stupidGame.dialog.MultiLineDialog;
import com.ielfgame.stupidGame.platform.PlatformHelper;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.res.ResManager;
import com.ielfgame.stupidGame.undo.UndoRedoManager;
import com.ielfgame.stupidGame.utils.FileHelper;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeTouch.TouchNode;
import elfEngine.basic.touch.BasicEventDecoder;
import elfEngine.basic.touch.ElfEvent;
import elfEngine.graphics.Rectangle;

public class FlashControlNode extends TouchNode {
	/***
	 * right button listener
	 */
	private final static FlashManager sFlashManager = PowerMan.getSingleton(FlashManager.class);
	private final static ImportImageSetting sImportImageSetting = new ImportImageSetting();

	private final static CopyPasteFrameData sCopyPasteFrameData = new CopyPasteFrameData();

	private final SelectRectNode mSelectRectNode = new SelectRectNode(this, 0);
	private final SurfaceFlashPanel mPanel;

	public FlashControlNode(SurfaceFlashPanel panel, ElfNode father, int ordinal) {
		super(father, ordinal);

		mPanel = panel;

		this.setTouchDecoder(new CustomDecode(panel));
		this.setTouchEnable(true);

		this.setUseSettedSize(true);
		this.setSize(1200, 640);

		mSelectRectNode.setUseSettedSize(true);
		mSelectRectNode.setSize(1200, 640);
		this.addChild(mSelectRectNode, 1000);

		this.setSelectRectListener(new Runnable() {
			public void run() {
				final Rectangle rect = mSelectRectNode.getRectangle();
				if (rect != null) {
					IFlashKeyArray[] branches = sFlashManager.getFlashKeyArrays();

					final int startX = mPanel.getFlashFrameByScreenX(rect.left);
					final int endX = mPanel.getFlashFrameByScreenX(rect.right);

					final int startY = mPanel.getIndexByScreenY(rect.top);
					final int endY = mPanel.getIndexByScreenY(rect.bottom);

					final IFlashKey[] all = sFlashManager.getAllFlashKeys();
					for (IFlashKey f : all) {
						f.setFrameSelect(false);
					}
					
//					System.err.println("startX="+startX);
//					System.err.println("endX="+endX);
//					System.err.println("startY="+startY);
//					System.err.println("endY="+endY);
//					System.err.println(" branches.length="+ branches.length);
					
					for (int y = startY; y <= endY; y++) {
						if (y >= 0 && y < branches.length) {
							final IFlashKeyArray branch = branches[y];
							for (int x = startX; x <= endX; x++) {
								final IFlashKey key = branch.findFlashKeyByFrame(x);
								if(key != null) {
									key.setFrameSelect(true);
								}
							}
						}
					}
				}
			}
		});
	}

	public void setSelectRectListener(final Runnable run) {
		mSelectRectNode.setListener(run);
	}

	public DragData getDragData() {
		return ((CustomDecode) this.getTouchDecoder()).getDragData();
	}

	public boolean onRightTouch(final Menu menu, float x, float y) {
		return ((CustomDecode) this.getTouchDecoder()).onRightTouch(menu, x, y);
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (e.keyCode == PlatformHelper.DEL) {
			final IFlashKey[] allSelects = sFlashManager.getAllSelectedFlashKeys();
			for (IFlashKey k : allSelects) {
				final IFlashKeyArray array = k.getKeyFrameArrayParent();
				if (array != null) {
					array.removeFlashKey(k);
				}
			}
		}

		if ((e.stateMask & PlatformHelper.CTRL) != 0) {
			if (e.keyCode == 'c' || e.keyCode == 'C') {
				((CustomDecode) this.getTouchDecoder()).copyFlashFrameArray();
			} else if (e.keyCode == 'v' || e.keyCode == 'V') {
				((CustomDecode) this.getTouchDecoder()).pasteFlashFrameArray();
			}
		}

	}

	public boolean mouseDoubleClick(MouseEvent event, float x, float y) {
		return ((CustomDecode) this.getTouchDecoder()).mouseDoubleClick(event, x, y);
	}

	private final String openNameDialog(final String name) {
		final MultiLineDialog mld = new MultiLineDialog();
		mld.setLabels(new String[] { "New Name" });
		mld.setValues(new String[] { name });
		mld.setTitle("");
		mld.setValueTypes(new Class<?>[] { String.class });

		final Object[] objs = mld.open();
		if (objs != null) {
			return (String) objs[0];
		}

		return null;
	}

	public void onClickCopy() {
		final String name = openNameDialog("animate");
		if (name != null) {
			UndoRedoManager.checkInUndo();
			sFlashManager.copyCurrentAnimate(name);
		}
	}

	public void onClickNew() {
		UndoRedoManager.checkInUndo();
		final String name = openNameDialog("animate");
		if (name != null) {
			UndoRedoManager.checkInUndo();
			sFlashManager.addAnimate(name);
		}
	}

	public void onClickDelete() {
		UndoRedoManager.checkInUndo();
		sFlashManager.deleteAnimate(sFlashManager.getAnimateName());
	}

	/***
	 * shift - multiply-select move delete copy - paste
	 */
	public static class CustomDecode extends BasicEventDecoder {
		private final SurfaceFlashPanel mSurfaceFlashPanel;
		private final DragData mDragData = new DragData();

		public CustomDecode(SurfaceFlashPanel panel) {
			mSurfaceFlashPanel = panel;
		}

		public boolean mouseDoubleClick(MouseEvent event, float x, float y) {
			if ((event.stateMask & PlatformHelper.SHIFT) != 0) {
				final IFlashKeyArray kArray = mSurfaceFlashPanel.getFlashKeyByScreenY(y);
				if (kArray != null) {
					final IFlashKey[] keys = kArray.getFlashKeys();
					for (IFlashKey k : keys) {
						k.setFrameSelect(true);
					}
					return true;
				}
			}
			return false;
		}

		public DragData getDragData() {
			return mDragData;
		}

		protected boolean onTouchMove(ElfEvent event, float moveX, float moveY) {
			if ((event.stateMask & PlatformHelper.CTRL) == 0) {
				mDragData.isDraging = false;
			}

			if (!mSurfaceFlashPanel.isInListArea(event.x, event.y)) {
				return false;
			}

			final int frame = Math.min(mSurfaceFlashPanel.getFlashFrameByScreenX(event.x), sFlashManager.getMaxF());
			sFlashManager.setFrame(frame);

			final IFlashKey[] fks = sFlashManager.getAllSelectedFlashKeys();

			if (fks.length > 0 && mDragData.isDraging) {
				mDragData.dragDx = getTotalMoveX(event);
				return true;
			}

			mDragData.isDraging = false;
			mDragData.dragDx = 0;
			return false;
		}

		protected boolean onTouchDwon(ElfEvent event) {
			mDragData.isDraging = false;
			mDragData.dragDx = 0;

			if (!mSurfaceFlashPanel.isInListArea(event.x, event.y)) {
				return false;
			}

			// check shift mask
			final int frame = Math.min(mSurfaceFlashPanel.getFlashFrameByScreenX(event.x), sFlashManager.getMaxF());
			sFlashManager.setFrame(frame);

			// search key data
			final IFlashKey kData = mSurfaceFlashPanel.getFlashKeyByScreenXY(event.x, event.y);
			if (kData != null && ((event.stateMask & PlatformHelper.CTRL) != 0)) {
				mDragData.isDraging = true;
				return true;
			}

			return false;
		}

		protected boolean onTouchUp(ElfEvent event) {

			if (mDragData.isDraging) {
				final float diffX = mDragData.dragDx;
				final int diffFrame = FlashConfig.getFrameDiffByXDiff(diffX);

				final IFlashKey[] fks = sFlashManager.getAllSelectedFlashKeys();
				final ArrayList<Integer> recoverList = new ArrayList<Integer>();
				for (int i = 0; i < fks.length; i++) {
					recoverList.add(fks[i].getFrame());
				}

				if (diffFrame != 0) {
					boolean suc = true;

					UndoRedoManager.checkInUndo();

					if (diffFrame > 0) {
						for (int i = fks.length - 1; i >= 0; i--) {
							final IFlashKey key = fks[i];
							suc = key.getKeyFrameArrayParent().translateKey(key, diffFrame, true);
							if (!suc) {
								break;
							}
						}

					} else if (diffFrame < 0) {
						for (int i = 0; i < fks.length; i++) {
							final IFlashKey key = fks[i];
							suc = key.getKeyFrameArrayParent().translateKey(key, diffFrame, true);
							if (!suc) {
								break;
							}
						}
					}

					if (!suc) {
						// recover
						for (int i = 0; i < fks.length; i++) {
							fks[i].setFrame(recoverList.get(i));
						}
					}
				}

			}

			if (Math.abs(getTotalMoveX(event)) < 5) {
				if ((event.stateMask & PlatformHelper.SHIFT) == 0) {
					// cancel all selects
					sFlashManager.setAllFlashKeySelected(false);
				}

				// search key data
				final IFlashKey kData = mSurfaceFlashPanel.getFlashKeyByScreenXY(event.x, event.y);
				if (kData != null) {
					// add
					kData.getKeyFrameArrayParent().shiftAdd(kData);
				}
			}

			mDragData.isDraging = false;
			mDragData.dragDx = 0;

			return false;
		}

		public boolean onRightTouch(final Menu menu, final float x, final float y) {
			/***
			 * menu show
			 */
			final IFlashKey kData = mSurfaceFlashPanel.getFlashKeyByScreenXY(x, y);
			final IFlashKeyArray kArray = mSurfaceFlashPanel.getFlashKeyByScreenY(y);

			final IFlashKey[] allSelects = sFlashManager.getAllSelectedFlashKeys();
			final IFlashKey[] all = sFlashManager.getAllFlashKeys();

			final int frame = mSurfaceFlashPanel.getFlashFrameByScreenX(x);
			sFlashManager.setFrame(frame);

			addMenu(menu, "增加关键帧", kData == null && kArray != null && frame >= 0, new Runnable() {
				public void run() {
					sFlashManager.addKeyFrameByKeyArray(kArray, frame);
				}
			});

			addMenu(menu, "增加首尾关键帧", kArray != null && frame >= 0, new Runnable() {
				public void run() {
					sFlashManager.addKeyFrameByKeyArray(kArray, 0);
					sFlashManager.addKeyFrameByKeyArray(kArray, sFlashManager.getMaxF());
				}
			});
			
			addMenu(menu, "增加'待机'关键帧", true, new Runnable() {
				public void run() {
					final FlashMainNode main = (FlashMainNode) sFlashManager.getFlashMain();
					if (main != null) {
						main.addLRKeys("待机", main.getAnimateName());
					}
				}
			});

			addMenu(menu, "增加空白帧", kData == null && kArray != null && frame >= 0, new Runnable() {
				public void run() {
					final IFlashKey key = sFlashManager.addKeyFrameByKeyArray(kArray, frame);
					if (key != null) {
						key.setResid(null);
					}
				}
			});

			addMenu(menu, "增加上一帧", kData == null && kArray != null && frame >= 0, new Runnable() {
				public void run() {
					sFlashManager.addKeyFramePrevByKeyArray(kArray, frame);
				}
			});

			addMenu(menu, "增加下一帧", kData == null && kArray != null && frame >= 0, new Runnable() {
				public void run() {
					sFlashManager.addKeyFrameNextByKeyArray(kArray, frame);
				}
			});

			addMenu(menu, "补充首尾关键帧(全)", true, new Runnable() {
				public void run() {
					/***
					 * 增加
					 */
					final FlashMainNode main = (FlashMainNode) sFlashManager.getFlashMain();
					if (main != null) {
						main.autoReplenish();
					}
				}
			});

			addMenu(menu, null, false, null);

			addMenu(menu, "导入图片组", kArray != null && frame >= 0, new Runnable() {
				public void run() {
					final MultiLineDialog mld = new MultiLineDialog();

					if (sImportImageSetting.ImageFolder_REF_DIR == null) {
						sImportImageSetting.ImageFolder_REF_DIR = ResManager.getSingleton().getDesignerImageAsset();
					}

					final Object[] ret = mld.open(sImportImageSetting);
					if (ret != null) {
						sImportImageSetting.setValues(ret);
						sImportImageSetting.StepFrames = Math.max(sImportImageSetting.StepFrames, 1);

						final LinkedList<String> images = FileHelper.getSimplePahIds(sImportImageSetting.ImageFolder_REF_DIR, new String[] { ".png", ".jpg" }, false);
						if (images != null && images.size() > 0) {
							// sort
							StringHelper.sortByLastInt(images);

							for (int i = 0; i < images.size(); i++) {
								final int myframe = frame + i * sImportImageSetting.StepFrames;
								final String image = images.get(i);

								IFlashKey key = kArray.findFlashKeyByFrame(myframe);
								if (key == null) {
									key = kArray.createFlashKeyByFrame(myframe);
									kArray.addFlashKey(key);
								}

								key.setResid(image);
							}
						}
					}
				}
			});

			addMenu(menu, null, false, null);

			addMenu(menu, "删除关键帧", allSelects.length > 0, new Runnable() {
				public void run() {
					// make sure
					for (IFlashKey k : allSelects) {
						final IFlashKeyArray array = k.getKeyFrameArrayParent();
						if (array != null) {
							array.removeFlashKey(k);
						}
					}
				}
			});

			addMenu(menu, null, false, null);

			// 层级名称 +

			// 复制-粘贴
			addMenu(menu, "复制-粘贴", allSelects.length > 0, new Runnable() {
				public void run() {
					// insert at the end
					final ArrayList<IFlashKey> copy = new ArrayList<IFlashKey>();
					final HashSet<IFlashKeyArray> parentSet = new HashSet<IFlashKeyArray>();
					int min = Integer.MAX_VALUE, max = -1;
					for (IFlashKey k : allSelects) {
						copy.add(k.copyKey());
						if (min > k.getFrame()) {
							min = k.getFrame();
						}
						if (max < k.getFrame()) {
							max = k.getFrame();
						}

						parentSet.add(k.getKeyFrameArrayParent());
					}

					if (min == Integer.MAX_VALUE || max == -1) {
						// unexpected
						return;
					}

					int range = max - min;
					int trans = 1 + range;

					for (IFlashKeyArray array : parentSet) {
						final IFlashKey[] keys = array.getFlashKeys();
						for (int i = keys.length - 1; i >= 0; i--) {
							final IFlashKey key = keys[i];
							if (key.getFrame() > max) {
								key.setFrame(key.getFrame() + trans);
							}
						}
					}

					for (IFlashKey k : copy) {
						k.setFrame(k.getFrame() + trans);
						final IFlashKeyArray fkarray = k.getKeyFrameArrayParent();
						if (fkarray != null) {
							fkarray.addFlashKey(k);
						}
					}
				}
			});

			addMenu(menu, null, false, null);

			addMenu(menu, "设置帧属性", kData != null, new Runnable() {
				public void run() {
					final KeySetting kt = KeySetting.create(kData);
					final MultiLineDialog mld = new MultiLineDialog();
					final Object[] ret = mld.open(kt);
					if (ret != null) {
						kt.setValues(ret);
						kt.copyToKeyFrame(kData);
					}

					sFlashManager.setProgressTime(sFlashManager.getProgressTime());
				}
			});

			addMenu(menu, null, false, null);

			addMenu(menu, "选中全部", all.length > 0, new Runnable() {
				public void run() {
					for (final IFlashKey key : all) {
						key.setFrameSelect(true);
					}
				}
			});

			addMenu(menu, "选中一行", kArray != null, new Runnable() {
				public void run() {
					for (final IFlashKey key : all) {
						key.setFrameSelect(false);
					}

					for (final IFlashKey key : kArray.getFlashKeys()) {
						key.setFrameSelect(true);
					}
				}
			});

			addMenu(menu, null, false, null);

			addMenu(menu, "反转", allSelects.length > 0, new Runnable() {
				public void run() {
					final IFlashKeyArray[] arrays = sFlashManager.getFlashKeyArrays();
					for (IFlashKeyArray array : arrays) {
						reverse(array);
					}
				}
			});

			addMenu(menu, null, false, null);

			addMenu(menu, "平移", allSelects.length > 0, new Runnable() {
				public void run() {
					final ElfPointf pos = new ElfPointf();

					final MultiLineDialog mld = new MultiLineDialog();
					final Object[] ret = mld.open(pos);
					if (ret != null) {
						pos.setValues(ret);

						for (IFlashKey key : allSelects) {
							key.setPosition(key.getPosition().translate(pos.x, pos.y));
						}
					}
				}
			});

			addMenu(menu, null, false, null);

			final IFlashMain iFlashMain = sFlashManager.getFlashMain();

			addMenu(menu, "导出序列帧", iFlashMain != null && iFlashMain instanceof FlashMainNode, new Runnable() {
				public void run() {
					MotionNode2FlashNode.exportImages((FlashMainNode) iFlashMain);
				}
			});

			addMenu(menu, null, false, null);

			addMenu(menu, "替换皮肤", iFlashMain != null && iFlashMain instanceof FlashMainNode, new Runnable() {
				public void run() {
					final FlashMainNode main = (FlashMainNode) iFlashMain;
					final MultiLineDialog mld = new MultiLineDialog();
					final SkinId si = new SkinId();
					final Object[] ret = mld.open(si);
					if (ret != null) {
						si.setValues(ret);
						main.replaceSkinById(si.skinId);
					}
				}
			});

			addMenu(menu, "缩放骨骼(全部)", iFlashMain != null && iFlashMain instanceof FlashMainNode, new Runnable() {
				public void run() {
					final FlashMainNode main = (FlashMainNode) iFlashMain;
					final MultiLineDialog mld = new MultiLineDialog();
					final BoneScale bs = new BoneScale();
					bs.scale = main.getBoneScale();
					final Object[] ret = mld.open(bs);
					if (ret != null) {
						bs.setValues(ret);
						main.setBoneScale(bs.scale);
					}
				}
			});

			addMenu(menu, "缩放骨骼(单一)", iFlashMain != null && iFlashMain instanceof FlashMainNode, new Runnable() {
				public void run() {
					final FlashMainNode main = (FlashMainNode) iFlashMain;
					final MultiLineDialog mld = new MultiLineDialog();
					final BoneScale bs = new BoneScale();
					bs.scale = main.getBoneScale();
					final Object[] ret = mld.open(bs);
					if (ret != null) {
						bs.setValues(ret);
						main.setBoneScaleSingle(bs.scale);
					}
				}
			});

			return false;
		}

		private static void reverse(final IFlashKeyArray array) {
			if (array != null) {
				final IFlashKey[] keys = array.getFlashKeys();
				int min = Integer.MAX_VALUE, max = -1;
				for (IFlashKey key : keys) {
					if (key.getFrameSelect()) {
						if (min > key.getFrame()) {
							min = key.getFrame();
						}
						if (max < key.getFrame()) {
							max = key.getFrame();
						}
					}
				}

				if (max != -1 || min != Integer.MAX_VALUE) {
					for (IFlashKey key : keys) {
						if (key.getFrame() >= min && key.getFrame() <= max) {
							key.setFrame(min + max - key.getFrame());
						}
					}
				}
			}
		}

		private final MenuItem addMenu(final Menu menu, String label, boolean enabled, final Runnable run) {
			if (label == null) {
				return new MenuItem(menu, SWT.SEPARATOR);
			}

			MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
			;
			menuItem.setText(label);
			menuItem.setEnabled(enabled);
			menuItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					if (run != null) {
						UndoRedoManager.checkInUndo();

						run.run();
					}
				}
			});
			return menuItem;
		}

		private final boolean copyFlashFrameArray() {
			final ElfNode target = PowerMan.getSingleton(DataModel.class).getSelectNode();
			if (target != null) {
				final IFlashKeyArray fka = sFlashManager.findFlashKeyArrayByTarget(target);
				if (fka != null) {
					final IFlashKey[] myfks = fka.getFlashKeys();
					final ArrayList<KeyFrameNode> selectList = new ArrayList<KeyFrameNode>();
					for (IFlashKey key : myfks) {
						if (key.getFrameSelect() && key instanceof KeyFrameNode) {
							selectList.add((KeyFrameNode) key);
						}
					}

					if (!selectList.isEmpty()) {
						Collections.sort(selectList, new Comparator<KeyFrameNode>() {
							public int compare(KeyFrameNode arg0, KeyFrameNode arg1) {
								return arg0.getFrame() - arg1.getFrame();
							}
						});

						sCopyPasteFrameData.target = target;
						sCopyPasteFrameData.keyFrameNodeList = new ArrayList<KeyFrameNode>();
						for (KeyFrameNode k : selectList) {
							sCopyPasteFrameData.keyFrameNodeList.add(k.copyKey());
						}
						return true;
					}
				}
			}

			sCopyPasteFrameData.target = null;
			sCopyPasteFrameData.keyFrameNodeList = null;
			return false;
		}

		private final void pasteFlashFrameArray() {
			final ElfNode target = PowerMan.getSingleton(DataModel.class).getSelectNode();
			if (sCopyPasteFrameData.target != null && sCopyPasteFrameData.keyFrameNodeList != null) {
				if (sCopyPasteFrameData.target == target) {
					final int pasteIndex = sFlashManager.getCurrentFrame();

					if (pasteIndex >= 0) {

						final int copyIndex = sCopyPasteFrameData.keyFrameNodeList.get(0).getFrame();
						final int offset = pasteIndex - copyIndex;

						final IFlashKeyArray kfa = sFlashManager.findFlashKeyArrayByTarget(target);
						final ArrayList<KeyFrameNode> selectList = sCopyPasteFrameData.keyFrameNodeList;
						boolean exiested = false;
						for (KeyFrameNode k : selectList) {
							final int frame = k.getFrame() + offset;
							if (kfa.findFlashKeyByFrame(frame) != null) {
								exiested = true;
								break;
							}
						}

						if (exiested) {
							final MessageDialog ms = new MessageDialog();
							exiested = !ms.open("警告", "粘贴将覆盖一部分原有帧， 确定么？");
						}

						if (!exiested) {
							UndoRedoManager.checkInUndo();

							for (KeyFrameNode k : selectList) {
								final int frame = k.getFrame() + offset;
								final IFlashKey old = kfa.findFlashKeyByFrame(frame);
								if (old != null) {
									kfa.removeFlashKey(old);
								}
								final IFlashKey newf = k.copyKey();
								newf.setFrame(frame);

								kfa.addFlashKey(newf);
							}

							System.err.println("关键帧粘贴成功!");
						}

					}
				} else {
					// 图层不匹配
					final MessageDialog ms = new MessageDialog();
					ms.open("不匹配", "复制的关键帧所在的图层与粘贴的图层不匹配!");
				}
			} else {
				// 没有可粘贴的内容
				final MessageDialog ms = new MessageDialog();
				ms.open("空", "剪切板内容为空!");
			}
		}
	}

	private static class BoneScale extends ElfDataDisplay {
		public float scale = 1;
	}

	private static class SkinId extends ElfDataDisplay {
		public int skinId = 0;
	}

	public static class DragData {
		public boolean isDraging;
		public float dragDx;
	}

	public static class KeySetting extends ElfDataDisplay {
		public InterType interType = InterType.Linear;
		public LoopMode loopMode = LoopMode.STAY;

		public String resid;
		public ElfPointf position = new ElfPointf();
		public ElfPointf scale = new ElfPointf();
		public float rotate = 0;
		public boolean visible = true;
		public ElfColor color = new ElfColor();

		private KeySetting() {
		}

		public static KeySetting create(IFlashKey node) {
			final KeySetting ret = new KeySetting();
			ret.interType = node.getInterType();
			ret.loopMode = node.getLoopMode();

			ret.resid = node.getResid();
			ret.position.set(node.getPosition());
			ret.scale.set(node.getScale());
			ret.rotate = node.getRotate();
			ret.visible = node.getVisible();
			ret.color.set(node.getColor());

			return ret;
		}

		public void copyToKeyFrame(IFlashKey node) {
			node.setInterType(interType);
			node.setLoopMode(loopMode);

			node.setResid(resid);
			node.setPosition(position);
			node.setScale(scale);
			node.setRotate(rotate);
			node.setVisible(visible);
			node.setColor(color);
		}

		public static class ImportImageSetting extends ElfDataXML {
			public String ImageFolder_REF_DIR;
			public int StepFrames = 1;
		}

	}

	public void mouseScrolled(MouseEvent event) {
		// if(event.count) {
		//
		// }
	}

	private static class CopyPasteFrameData {
		public ElfNode target;
		public ArrayList<KeyFrameNode> keyFrameNodeList;
	}

}
