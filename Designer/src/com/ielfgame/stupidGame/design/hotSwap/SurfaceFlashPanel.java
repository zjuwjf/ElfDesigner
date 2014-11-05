package com.ielfgame.stupidGame.design.hotSwap;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Menu;

import com.ielfgame.stupidGame.NodeView.NodeViewWorkSpaceTab;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.design.Surface.Surface;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashConfig;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashControlNode;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashControlNode.DragData;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashManager;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashStruct.IFlashKey;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashStruct.IFlashKeyArray;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.node.ClipNode;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.graph.ColorButtonNode;
import elfEngine.basic.node.graph.ColorTabNode;
import elfEngine.basic.node.graph.RectEdgeNode;
import elfEngine.basic.node.nodeList.ListNode;
import elfEngine.basic.node.nodeList.ListNode.ListContainerNode;
import elfEngine.basic.node.nodeShape.GradientNode;
import elfEngine.basic.node.nodeText.richLabel.LabelNode;
import elfEngine.basic.node.nodeText.richLabel.SharedLabelNode;
import elfEngine.basic.node.nodeTouch.CheckBoxNode;
import elfEngine.basic.node.nodeTouch.ShieldNode;
import elfEngine.basic.node.nodeTouch.TabNode;
import elfEngine.basic.node.nodeTouch.slider.SliderNode;
import elfEngine.basic.node.nodeTouch.slider.SliderNode.IPercentageListener;
import elfEngine.basic.touch.ElfEvent;
import elfEngine.extend.ElfOnClickListener;

public class SurfaceFlashPanel extends Surface {
	private final static FlashManager sFlashManager = PowerMan.getSingleton(FlashManager.class);
	private ElfNode mKeyDrawNode;
	private FlashControlNode mFlashControlNode;
	private final SharedLabelNode mSharedLabelNode;
	private boolean mInited = false;
	
	private final static float sMaxSpeedRate = 8;
	private final static int sMaxFCapacity = 75;

	public SurfaceFlashPanel() {
		// D:\\transfer\\xml\\
		super("FlashPanel.xml");

		mKeyDrawNode = this.createDynamicNode("@key");
		mSharedLabelNode = (SharedLabelNode) this.createDynamicNode("@label");
	}

	public boolean isInListArea(final float x, final float y) {
		return this._listLayer.isInSize(new ElfPointf(x, y));
	}

	public void onMenuShow(final Menu menu, final float x, final float y) {
		/***
		 * interType, loopMode, translate, copy-paste, delete <-> add
		 */
		if (mFlashControlNode != null) {
			if (this._listLayer.isInSize(new ElfPointf(x, this.getRootNode().getSize().y - y))) {
				mFlashControlNode.onRightTouch(menu, x, this.getRootNode().getSize().y - y);
			}
		}
	}
	
	public boolean mouseDoubleClick(MouseEvent event) {
		if (mFlashControlNode != null) {
			return mFlashControlNode.mouseDoubleClick(event, event.x, this.getRootNode().getSize().y - event.y);
		}
		return false;
	}
	
	public void keyPressed(KeyEvent e) {
		if (mFlashControlNode != null) {
			mFlashControlNode.keyPressed(e);
		}
	}

	public void keyReleased(KeyEvent e) {
		if (mFlashControlNode != null) {
			mFlashControlNode.keyReleased(e);
		}
	}

	private void setTimeLine(final int time) {
		final float fp = (float) time / (1000.0f / sFlashManager.getFPS());
		/***
		 * just view
		 */
		setTimeLineByFrame(fp);
	}

	private void setTimeLineByFrame(final float frame) {
		final float x = FlashConfig.getXByFrame(frame);
		this._controlLayer_time.setPositionX(x + 100);
	}

	public void onUpdate(float dt) {
		if (!mInited) {
			return;
		}

		updateView();

		this.setTimeLine(sFlashManager.getProgressTime());

		// just for frame show
		this._controlLayer_cf.setText(String.format("f:%d", 1 + sFlashManager.getCurrentFrame()));
		this._listLayer_clip_shield.setPositionX((sFlashManager.getMaxF() + 1) * 10);
		
		this._controlLayer_speedSlider_touch.setPercentage(sFlashManager.getSpeedRate()*100f/sMaxSpeedRate, false);
		this._controlLayer_speedSlider_label.setText(String.format("速率:%.1f", sFlashManager.getSpeedRate()));
		
		this._controlLayer_maxFSlider_touch.setPercentage(sFlashManager.getMaxF()*100f/sMaxFCapacity, false);
		this._controlLayer_maxFSlider_touch_label.setText("MaxF:"+(sFlashManager.getMaxF() + 1));
		
		final float listY = this._listLayer_list.getSize().y;
		final float containerY = this._listLayer_list_container.getSize().y;
		if(listY >= containerY) {
			final float oldY = this._controlLayer_scrollSlider_touch.getSize().y;
			if(oldY != listY) {
				this._controlLayer_scrollSlider_touch.setSize(15, listY);
				_controlLayer_scrollSlider_touch_rectedge.setSize(_controlLayer_scrollSlider_touch.getSize());
				this._controlLayer_scrollSlider_touch.setPercentage(0, true);
			}
		} else {
			final float oldY = this._controlLayer_scrollSlider_touch.getSize().y;
			final float newY = listY * listY / containerY;
			if(oldY != newY) {
				this._controlLayer_scrollSlider_touch.setSize(15, newY);
				_controlLayer_scrollSlider_touch_rectedge.setSize(_controlLayer_scrollSlider_touch.getSize());
				this._controlLayer_scrollSlider_touch.setPercentage(0, true);
			}
		}
	}

	private void updateView() {

		final IFlashKeyArray[] allFlashKeyArrays = sFlashManager.getFlashKeyArrays();
		ElfNode[] nodes = this._listLayer_list_container.getChilds();
		final int diff = allFlashKeyArrays.length - nodes.length;

		// make sure they are the same number
		for (int i = 0; i < diff; i++) {
			final ElfNode newItem = this.createDynamicNode("@item");
			newItem.addToParent();

			final ElfNode right = newItem.findBySimpleName("right");
			final float x = right.getPositionInScreen().x;
			FlashConfig.setFrameStartXInScreen(x);
			FlashConfig.setFrameBarLength(right.getSize().x);

			final DrawKeyNode dkn = new DrawKeyNode(this, right, Integer.MAX_VALUE);
			dkn.setName("DrawKeyNode");
			dkn.addToParent();
		}
		for (int i = 0; i < -diff; i++) {
			nodes[nodes.length - 1 - i].removeFromParent();
		}

		nodes = this._listLayer_list_container.getChilds();
		
		final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode();

		for (int i = 0; i < nodes.length; i++) {
			final IFlashKeyArray fka = allFlashKeyArrays[i];
			final ElfNode target = fka.getTarget();
			final ElfNode item = nodes[i];

			((LabelNode) (item.findBySimpleName("left").findBySimpleName("name"))).setText("" + fka.getTargetName());
			((DrawKeyNode) (item.findBySimpleName("right")).findBySimpleName("DrawKeyNode")).setFlashKeyArray(fka);

			final TabNode tab = (TabNode) item.findBySimpleName("tab");
			tab.setOnClickListener(new ElfOnClickListener() {
				public void onClick(int id, ElfEvent event) {
					// PowerMan.getSingleton(NodeViewWorkSpaceTab.class).addSelectNodes(fka.getTarget());
					
					if(target != null) {
//						if (event != null && ((event.stateMask & PlatformHelper.CTRL) != 0 || (event.stateMask & PlatformHelper.SHIFT) != 0)) {
//							PowerMan.getSingleton(NodeViewWorkSpaceTab.class).addSelectNodes(target);
//						} else {
							PowerMan.getSingleton(NodeViewWorkSpaceTab.class).setSelectNodes(target);
//						}
						
						if (!target.isSelected()) {
							tab.onReleased();
						}
					} else {
						tab.onReleased();
					}
				}
			});
			
			if(selectNode == target && target != null) {
				tab.setTabSelected(true);
			} else {
				tab.setTabSelected(false);
			}

			{
				final CheckBoxNode cbnVisible = (CheckBoxNode) item.findBySimpleName(new String[] { "left", "checkboxVisible" });
				cbnVisible.setOnClickListener(new ElfOnClickListener() {
					public void onClick(int id, ElfEvent event) {
						if (fka != null) {
							fka.setVisibleMask(cbnVisible.getStateSelected());
						}
					}
				});
				cbnVisible.setStateSelected(fka.getVisibleMask());
			}

			{
				final CheckBoxNode cbnLocked = (CheckBoxNode) item.findBySimpleName(new String[] { "left", "checkboxLock" });
				cbnLocked.setOnClickListener(new ElfOnClickListener() {
					public void onClick(int id, ElfEvent event) {
						if (fka != null) {
							fka.setLocked(cbnLocked.getStateSelected());
						}
					}
				});
				cbnLocked.setStateSelected(fka.getLocked());
			}
			// lock and visible
		}

		// multiply animate
		ElfNode[] animateItemNodes = this._select_list.getContainer().getChilds();
		final String[] animateNames = sFlashManager.arrayAnimateName();
		final int diffAnimate = animateNames.length - animateItemNodes.length;

		// make sure they are the same number
		for (int i = 0; i < diffAnimate; i++) {
			final ElfNode newItem = this.createDynamicNode("@tabkey");
			newItem.addToParent();
		}
		for (int i = 0; i < -diffAnimate; i++) {
			animateItemNodes[animateItemNodes.length - 1 - i].removeFromParent();
		}

		animateItemNodes = this._select_list.getContainer().getChilds();
		for (int i = 0; i < animateItemNodes.length; i++) {
			final ElfNode item = animateItemNodes[i];
			final ColorTabNode tab = (ColorTabNode) item.findBySimpleName("mytab");
			final String name = animateNames[i];

			tab.setOnClickListener(new ElfOnClickListener() {
				public void onClick(int id, ElfEvent event) {
					sFlashManager.setAnimateName(name);
				}
			});

			if (name != null && name.equals(sFlashManager.getAnimateName())) {
				if (!tab.getTabSelected()) {
					tab.onPressed();
				}
			}

			final SharedLabelNode label = (SharedLabelNode) item.findBySimpleName("mylabel");
			label.setText(name);
		}
	}
	
	private final void drawKey(final float x, boolean select, float alpha) {
		mKeyDrawNode.setPosition(x, 0);
		mKeyDrawNode.setVisible(true);

		final ElfNode selectNode = mKeyDrawNode.findBySimpleName("select");
		selectNode.setVisible(select);
		selectNode.setAlpha(0.5f * alpha);

		final ElfNode pointNode = mKeyDrawNode.findBySimpleName("point");
		pointNode.setAlpha(alpha);

		mKeyDrawNode.drawSprite();
	}

	private final void drawText(final float x, String text, float alpha) {
		mSharedLabelNode.setPosition(x, 0);
		mSharedLabelNode.setPositionY(0);

		mSharedLabelNode.setVisible(true);
		mSharedLabelNode.setText(text);
		mSharedLabelNode.setAlpha(alpha);

		mSharedLabelNode.calcSprite(0);
		mSharedLabelNode.drawSprite();
	}

	public final int getIndexByScreenY(final float y) {
		final ElfNode[] nodes = this._listLayer_list_container.getChilds();
		if (nodes != null && nodes.length > 0) {
			final float startY = nodes[0].getPositionInScreen().y;
			final float step = nodes[0].getSize().y;
			return Math.round((startY - y) / step);
		}
		return -1;
	}

	/***
	 * screen xy
	 */
	public IFlashKey getFlashKeyByScreenXY(final float x, final float y) {
		final int index = getIndexByScreenY(y);
		IFlashKeyArray[] branches = sFlashManager.getFlashKeyArrays();
		if (index >= 0 && index < branches.length) {
			final IFlashKeyArray branch = branches[index];
			final float startX = FlashConfig.getFrameStartXInScreen();
			final int frame = FlashConfig.getFrameByX(x - startX);
			final IFlashKey key = branch.findFlashKeyByFrame(frame);
			return key;
		}
		return null;
	}

	public IFlashKeyArray getFlashKeyByScreenY(final float y) {
		final int index = getIndexByScreenY(y);
		IFlashKeyArray[] branches = sFlashManager.getFlashKeyArrays();
		if (index >= 0 && index < branches.length) {
			final IFlashKeyArray branch = branches[index];
			return branch;
		}
		return null;
	}

	public int getFlashFrameByScreenX(final float x) {
		final float startX = FlashConfig.getFrameStartXInScreen();
		final int frame = FlashConfig.getFrameByX(x - startX);
		return frame;
	}

	// public
	private final void initControlNode() {
		mFlashControlNode = new FlashControlNode(this, this._controlLayer, Integer.MAX_VALUE);
		mFlashControlNode.addToParent();

		this._button_copy.setOnClickListener(new ElfOnClickListener() {
			public void onClick(int id, ElfEvent event) {
				mFlashControlNode.onClickCopy();
			}
		});

		this._button_delete.setOnClickListener(new ElfOnClickListener() {
			public void onClick(int id, ElfEvent event) {
				mFlashControlNode.onClickDelete();
			}
		});

		this._button_new.setOnClickListener(new ElfOnClickListener() {
			public void onClick(int id, ElfEvent event) {
				mFlashControlNode.onClickNew();
			}
		});
	}

	private void initButtons() {
		this._controlLayer_play.setOnClickListener(new ElfOnClickListener() {
			public void onClick(int id, ElfEvent event) {
				if (_controlLayer_play.getStateSelected()) {
					sFlashManager.play();
				} else {
					sFlashManager.stop();
				}
			}
		});

		this._controlLayer_autoKey.setOnClickListener(new ElfOnClickListener() {
			public void onClick(int id, ElfEvent event) {
				System.err.println("auto key "+_controlLayer_autoKey.getStateSelected());
				sFlashManager.setAutoKey(!_controlLayer_autoKey.getStateSelected());
			}
		});
		this._controlLayer_autoKey.setStateSelected(!sFlashManager.getAutoKey());

		this._controlLayer_showAll.setOnClickListener(new ElfOnClickListener() {
			public void onClick(int id, ElfEvent event) {
				sFlashManager.setShowAll(!_controlLayer_showAll.getStateSelected());
			}
		});
		this._controlLayer_showAll.setStateSelected(!sFlashManager.getShowAll());
	}

	private void initBg() {
		this._bg.setScale(this.getPhysicWidth() / _bg.getSize().x, this.getPhysicHeight() / _bg.getSize().y);
	}
	
	private void initSliders() {
		this._controlLayer_maxFSlider_touch.setPercentageListener(new IPercentageListener() {
			public void onPercentageChange(float percentage, boolean innerCall) {
				if(innerCall) {
					sFlashManager.setMaxF(Math.round(sMaxFCapacity*percentage/100f));
				}
			}
		});
		
		this._controlLayer_speedSlider_touch.setPercentageListener(new IPercentageListener() {
			public void onPercentageChange(float percentage, boolean innerCall) {
				if(innerCall) {
					//0.1 ?
					sFlashManager.setSpeedRate(Math.round((sMaxSpeedRate*percentage/100f)*10) * 0.1f);
				}
			}
		});
		
		this._controlLayer_scrollSlider_touch.setPercentageListener(new IPercentageListener() {
			public void onPercentageChange(float percentage, boolean innerCall) {
				if(innerCall) {
					final float listY = _listLayer_list.getSize().y;
					final float containerY = _listLayer_list_container.getSize().y;
					
					if(containerY > listY) {
						final float newY = listY/2 + (containerY-listY)*percentage/100f;
						_listLayer_list_container.setPositionY(newY);
					}
				}
			}
		});
	}
	
	public void scrollList(int count) {
//		this._listLayer_list.onMove(0, 32*count);
	}
	
	public boolean mouseScrolled(MouseEvent event) {
		if(mFlashControlNode != null) {
			mFlashControlNode.mouseScrolled(event);
		}
		return false;
	}

	public void onStart() {
		initNodesActions();
		initControlNode();
		initButtons();
		initSliders();
		initBg();
		mInited = true;
	}

	public void onFinished() {
	}
	
	public void onResume() {
	}

	public void onPause() {
	}

	final void initNodesActions() {
		// @@auto-code-start initialize
		_bg = (GradientNode)findNodeByName("bg");
		_listLayer = (ElfNode)findNodeByName("listLayer");
		_listLayer_list = (ListNode)findNodeByName("listLayer_list");
		_listLayer_list_container = (ListContainerNode)findNodeByName("listLayer_list_container");
		_tab = (ColorTabNode)findNodeByName("tab");
		_left = (ElfNode)findNodeByName("left");
		_left_checkboxVisible = (CheckBoxNode)findNodeByName("left_checkboxVisible");
		_left_checkboxLock = (CheckBoxNode)findNodeByName("left_checkboxLock");
		_left_name = (SharedLabelNode)findNodeByName("left_name");
		_right = (ClipNode)findNodeByName("right");
		_right_grid = (ElfNode)findNodeByName("right_grid");
		_select = (RectEdgeNode)findNodeByName("select");
		_point = (RectEdgeNode)findNodeByName("point");
		_listLayer_clip_shield = (ShieldNode)findNodeByName("listLayer_clip_shield");
		_bottom = (ElfNode)findNodeByName("bottom");
		_bottom_timeline = (ClipNode)findNodeByName("bottom_timeline");
		_controlLayer = (ElfNode)findNodeByName("controlLayer");
		_controlLayer_time = (RectEdgeNode)findNodeByName("controlLayer_time");
		_controlLayer_play = (CheckBoxNode)findNodeByName("controlLayer_play");
		_controlLayer_showAll = (CheckBoxNode)findNodeByName("controlLayer_showAll");
		_controlLayer_autoKey = (CheckBoxNode)findNodeByName("controlLayer_autoKey");
		_controlLayer_cf = (SharedLabelNode)findNodeByName("controlLayer_cf");
		_controlLayer_speedSlider = (ElfNode)findNodeByName("controlLayer_speedSlider");
		_controlLayer_speedSlider_touch = (SliderNode)findNodeByName("controlLayer_speedSlider_touch");
		_controlLayer_speedSlider_label = (SharedLabelNode)findNodeByName("controlLayer_speedSlider_label");
		_controlLayer_maxFSlider = (ElfNode)findNodeByName("controlLayer_maxFSlider");
		_controlLayer_maxFSlider_touch = (SliderNode)findNodeByName("controlLayer_maxFSlider_touch");
		_controlLayer_maxFSlider_touch_label = (SharedLabelNode)findNodeByName("controlLayer_maxFSlider_touch_label");
		_controlLayer_scrollSlider = (ElfNode)findNodeByName("controlLayer_scrollSlider");
		_controlLayer_scrollSlider_touch = (SliderNode)findNodeByName("controlLayer_scrollSlider_touch");
		_controlLayer_scrollSlider_touch_rectedge = (RectEdgeNode)findNodeByName("controlLayer_scrollSlider_touch_rectedge");
		_select_list = (ListNode)findNodeByName("select_list");
		_mytab = (ColorTabNode)findNodeByName("mytab");
		_mylabel = (SharedLabelNode)findNodeByName("mylabel");
		_button_new = (ColorButtonNode)findNodeByName("button_new");
		_button_new_label = (SharedLabelNode)findNodeByName("button_new_label");
		_button_delete = (ColorButtonNode)findNodeByName("button_delete");
		_button_delete_label = (SharedLabelNode)findNodeByName("button_delete_label");
		_button_copy = (ColorButtonNode)findNodeByName("button_copy");
		_button_copy_label = (SharedLabelNode)findNodeByName("button_copy_label");
		// @@auto-code-end
	}

	// @@auto-code-start defines
	protected GradientNode _bg;
	protected ElfNode _listLayer;
	protected ListNode _listLayer_list;
	protected ListContainerNode _listLayer_list_container;
	protected ColorTabNode _tab;
	protected ElfNode _left;
	protected CheckBoxNode _left_checkboxVisible;
	protected CheckBoxNode _left_checkboxLock;
	protected SharedLabelNode _left_name;
	protected ClipNode _right;
	protected ElfNode _right_grid;
	protected RectEdgeNode _select;
	protected RectEdgeNode _point;
	protected ShieldNode _listLayer_clip_shield;
	protected ElfNode _bottom;
	protected ClipNode _bottom_timeline;
	protected ElfNode _controlLayer;
	protected RectEdgeNode _controlLayer_time;
	protected CheckBoxNode _controlLayer_play;
	protected CheckBoxNode _controlLayer_showAll;
	protected CheckBoxNode _controlLayer_autoKey;
	protected SharedLabelNode _controlLayer_cf;
	protected ElfNode _controlLayer_speedSlider;
	protected SliderNode _controlLayer_speedSlider_touch;
	protected SharedLabelNode _controlLayer_speedSlider_label;
	protected ElfNode _controlLayer_maxFSlider;
	protected SliderNode _controlLayer_maxFSlider_touch;
	protected SharedLabelNode _controlLayer_maxFSlider_touch_label;
	protected ElfNode _controlLayer_scrollSlider;
	protected SliderNode _controlLayer_scrollSlider_touch;
	protected RectEdgeNode _controlLayer_scrollSlider_touch_rectedge;
	protected ListNode _select_list;
	protected ColorTabNode _mytab;
	protected SharedLabelNode _mylabel;
	protected ColorButtonNode _button_new;
	protected SharedLabelNode _button_new_label;
	protected ColorButtonNode _button_delete;
	protected SharedLabelNode _button_delete_label;
	protected ColorButtonNode _button_copy;
	protected SharedLabelNode _button_copy_label;
	// @@auto-code-end

	private static class DrawKeyNode extends ElfNode {
		private final SurfaceFlashPanel mSurfaceFlashPanel;
		private IFlashKeyArray mKeyArray;

		public DrawKeyNode(SurfaceFlashPanel panel, ElfNode father, int ordinal) {
			super(father, ordinal);
			mSurfaceFlashPanel = panel;
		}

		public void setFlashKeyArray(IFlashKeyArray kda) {
			mKeyArray = kda;
		}

		public void drawSelf() {
			super.drawSelf();

			if (mKeyArray != null) {
				final IFlashKey[] fks = mKeyArray.getFlashKeys();
				for (int i = 0; i < fks.length; i++) {
					final IFlashKey data = fks[i];
					float x = FlashConfig.getXByFrame(data.getFrame());

					if (i == fks.length - 1) {
						if (data.getLoopMode() != null) {
							mSurfaceFlashPanel.drawText(x + 5, data.getLoopMode().toString(), 0.8f);
						}
					} else {
						if (data.getInterType() != null && data.getInterType() != InterType.Linear) {
							mSurfaceFlashPanel.drawText(x + 5, data.getInterType().toString(), 0.8f);
						}
					}

					mSurfaceFlashPanel.drawKey(x, data.getFrameSelect(), 1);

					final DragData dd = mSurfaceFlashPanel.mFlashControlNode.getDragData();
					// draw drag?
					if (data.getFrameSelect() && dd.isDraging) {
						x = x + dd.dragDx;
						final float alpha = 0.4f;
						mSurfaceFlashPanel.drawKey(x, data.getFrameSelect(), alpha);
						if (i == fks.length - 1) {
							if (data.getLoopMode() != null) {
								mSurfaceFlashPanel.drawText(x + 5, data.getLoopMode().toString(), alpha);
							}
						} else {
							if (data.getInterType() != null && data.getInterType() != InterType.Linear) {
								mSurfaceFlashPanel.drawText(x + 5, data.getInterType().toString(), alpha);
							}
						}
					}
				}
			}
		}
	}
}
