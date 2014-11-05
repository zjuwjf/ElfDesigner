package com.ielfgame.stupidGame.newNodeMenu;

import org.eclipse.swt.events.SelectionEvent;

import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashMainNode;
import com.ielfgame.stupidGame.design.hotSwap.flash.KeyFrameArrayNode;
import com.ielfgame.stupidGame.design.hotSwap.flash.KeyFrameNode;
import com.ielfgame.stupidGame.design.hotSwap.flash.KeyStorageNode;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.utils.FileHelper;

import elfEngine.basic.node.AddColorNode;
import elfEngine.basic.node.BatchNode;
import elfEngine.basic.node.ClipNode;
import elfEngine.basic.node.ElfGrayNode;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.GenieTest;
import elfEngine.basic.node.GenieTest2;
import elfEngine.basic.node.Scale9Node;
import elfEngine.basic.node.bar.BarNode;
import elfEngine.basic.node.bar.Joint9Node;
import elfEngine.basic.node.bar.ProgressNode;
import elfEngine.basic.node.bar.TrainNode;
import elfEngine.basic.node.fit.FitAllNode;
import elfEngine.basic.node.fit.FitPositionNode;
import elfEngine.basic.node.fit.FitScaleNode;
import elfEngine.basic.node.fit.FitSizeNode;
import elfEngine.basic.node.gltest.StencilBeginNode;
import elfEngine.basic.node.gltest.StencilEndNode;
import elfEngine.basic.node.graph.ColorBarNode;
import elfEngine.basic.node.graph.ColorButtonNode;
import elfEngine.basic.node.graph.ColorTabNode;
import elfEngine.basic.node.graph.FlagNodes.ColorFlagCancelNode;
import elfEngine.basic.node.graph.FlagNodes.ColorFlagOKNode;
import elfEngine.basic.node.graph.RectEdgeNode;
import elfEngine.basic.node.graph.RectNode;
import elfEngine.basic.node.hf.HFST.HFNode;
import elfEngine.basic.node.hf.HFST.STNode;
import elfEngine.basic.node.node3d.CoverFlowNode;
import elfEngine.basic.node.node3d.Elf3DLayer;
import elfEngine.basic.node.node3d.Elf3DNode;
import elfEngine.basic.node.nodeAdvanced.AccumNode;
import elfEngine.basic.node.nodeAdvanced.BlinkNode;
import elfEngine.basic.node.nodeAdvanced.FreeNode;
import elfEngine.basic.node.nodeAdvanced.MotionBlurNode;
import elfEngine.basic.node.nodeAdvanced.RecordNode;
import elfEngine.basic.node.nodeAdvanced.RenderNode;
import elfEngine.basic.node.nodeAdvanced.ShakeNode;
import elfEngine.basic.node.nodeAdvanced.WaveNode;
import elfEngine.basic.node.nodeAnimate.AnimateNode;
import elfEngine.basic.node.nodeAnimate.AnimateNode.AnimateFrameNode;
import elfEngine.basic.node.nodeAnimate.JointAnimateNode;
import elfEngine.basic.node.nodeAnimate.MultiAnimateNode;
import elfEngine.basic.node.nodeAnimate.PuppyNode;
import elfEngine.basic.node.nodeAnimate.SimpleAnimateNode;
import elfEngine.basic.node.nodeAnimate.timeLine.BoneNode;
import elfEngine.basic.node.nodeAnimate.timeLine.ElfMotionKeyNode;
import elfEngine.basic.node.nodeAnimate.timeLine.ElfMotionNode;
import elfEngine.basic.node.nodeFollow.ChairNode;
import elfEngine.basic.node.nodeFollow.FollowNode;
import elfEngine.basic.node.nodeFollow.HoldAndFollowNode;
import elfEngine.basic.node.nodeFollow.WalkNode;
import elfEngine.basic.node.nodeLayout.Layout2DNode;
import elfEngine.basic.node.nodeLayout.LayoutNode;
import elfEngine.basic.node.nodeLayout.LinearLayout2DNode;
import elfEngine.basic.node.nodeLayout.LinearLayoutNode;
import elfEngine.basic.node.nodeList.ListNode;
import elfEngine.basic.node.nodeList.ListNode.ListContainerNode;
import elfEngine.basic.node.nodeList.ListNode.ListScrollNode;
import elfEngine.basic.node.nodeList.NoticeLabelNode;
import elfEngine.basic.node.nodeList.PullDownNode;
import elfEngine.basic.node.nodeMap.MapNode;
import elfEngine.basic.node.nodeMap.MapNode.MapContainerNode;
import elfEngine.basic.node.nodeMap.MapTileNode;
import elfEngine.basic.node.nodeShape.BezierNode;
import elfEngine.basic.node.nodeShape.BezierNode.BezierAssistNode;
import elfEngine.basic.node.nodeShape.EditShapeNode;
import elfEngine.basic.node.nodeShape.GradientNode;
import elfEngine.basic.node.nodeShape.RectangleNode;
import elfEngine.basic.node.nodeText.BMFontNode;
import elfEngine.basic.node.nodeText.InputTextNode;
import elfEngine.basic.node.nodeText.RichTextNode;
import elfEngine.basic.node.nodeText.TextNode;
import elfEngine.basic.node.nodeText.TimeNode;
import elfEngine.basic.node.nodeText.richLabel.LabelNode;
import elfEngine.basic.node.nodeText.richLabel.RichLabelNode;
import elfEngine.basic.node.nodeText.richLabel.SharedLabelNode;
import elfEngine.basic.node.nodeTouch.ButtonNode;
import elfEngine.basic.node.nodeTouch.CheckBoxNode;
import elfEngine.basic.node.nodeTouch.CircleNode;
import elfEngine.basic.node.nodeTouch.ClickNode;
import elfEngine.basic.node.nodeTouch.ColorClickNode;
import elfEngine.basic.node.nodeTouch.HoldNode;
import elfEngine.basic.node.nodeTouch.LuaTouchNode;
import elfEngine.basic.node.nodeTouch.ShieldNode;
import elfEngine.basic.node.nodeTouch.SwipNode;
import elfEngine.basic.node.nodeTouch.TabNode;
import elfEngine.basic.node.nodeTouch.slider.SliderNode;
import elfEngine.basic.node.particle.ParticleNode;
import elfEngine.basic.node.particle.ParticleNode.ParticleEmitter;

public class NewNodeMenu extends AbstractMenu {
	public NewNodeMenu() {
		super("New Node");

		init();
	}

	public void init() {
		// ElfNode
		this.checkInMenuItem(new NewNodeMenuItem(ElfNode.class));
		this.checkInMenuItem(null);
		this.checkInMenuItem(new NewNodeMenuItem(AddColorNode.class));
		this.checkInMenuItem(null);

		// Map, MapContainer,
		this.checkInMenuItem(new NewNodeMenu() {
			public void init() {
				setLable("Flash");
				this.checkInMenuItem(new NewNodeMenuItem(FlashMainNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(KeyStorageNode.class, false));
				this.checkInMenuItem(new NewNodeMenuItem(KeyFrameArrayNode.class, false));
				this.checkInMenuItem(new NewNodeMenuItem(KeyFrameNode.class, false));
			}
		});

		// Map, MapContainer,
		this.checkInMenuItem(new NewNodeMenu() {
			public void init() {
				setLable("Map");
				this.checkInMenuItem(new NewNodeMenuItem(MapNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(MapContainerNode.class));

				// TileNode
				this.checkInMenuItem(new NewNodeMenuItem(MapTileNode.class));
			}
		});

		// List, ListContainer, ListScroll
		this.checkInMenuItem(new NewNodeMenu() {
			public void init() {
				setLable("List");
				this.checkInMenuItem(new NewNodeMenuItem(ListNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(ListContainerNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(ListScrollNode.class));
				// this.checkInMenuItem(new NewNodeMenuItem(StretchNode.class));
			}
		});

		// Clip
		this.checkInMenuItem(new NewNodeMenu() {
			public void init() {
				setLable("Clip");
				this.checkInMenuItem(new NewNodeMenuItem(ClipNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(PullDownNode.class));

				this.checkInMenuItem(new NewNodeMenuItem(StencilBeginNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(StencilEndNode.class));
			}
		});
		
		//Joint9Node
		this.checkInMenuItem(new NewNodeMenu() {
			public void init() {
				setLable("拼接");
				this.checkInMenuItem(new NewNodeMenuItem(Joint9Node.class));
				this.checkInMenuItem(new NewNodeMenuItem(TrainNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(Scale9Node.class));
			}
		});
		
		this.checkInMenuItem(null);

		// Circle, Swip
		this.checkInMenuItem(new NewNodeMenu() {
			public void init() {
				setLable("Rotate&Swip");
				this.checkInMenuItem(new NewNodeMenuItem(CircleNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(SwipNode.class));
			}
		});
		this.checkInMenuItem(null);

		// Touch
		// button, tab, click, hold
		// shield,
		this.checkInMenuItem(new NewNodeMenu() {
			public void init() {
				setLable("Touch");
				this.checkInMenuItem(new NewNodeMenuItem(ButtonNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(ColorClickNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(ClickNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(TabNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(HoldNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(CheckBoxNode.class));
				this.checkInMenuItem(null);
				this.checkInMenuItem(new NewNodeMenuItem(ShieldNode.class));
				this.checkInMenuItem(null);
				this.checkInMenuItem(new NewNodeMenuItem(LuaTouchNode.class));
				// SliderNode
				this.checkInMenuItem(null);
				this.checkInMenuItem(new NewNodeMenuItem(SliderNode.class, FileHelper.IS_WINDOWS));
			}
		});
		this.checkInMenuItem(null);

		// Text, Time, input
		this.checkInMenuItem(new NewNodeMenu() {
			public void init() {
				setLable("Text");
				
				this.checkInMenuItem(new NewNodeMenuItem(BMFontNode.class));
				
				this.checkInMenuItem(new NewNodeMenuItem(LabelNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(RichLabelNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(TimeNode.class));
				this.checkInMenuItem(null);
				this.checkInMenuItem(new NewNodeMenuItem(SharedLabelNode.class));
				this.checkInMenuItem(null);

				this.checkInMenuItem(new NewNodeMenuItem(InputTextNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(NoticeLabelNode.class));
				this.checkInMenuItem(null);

				this.checkInMenuItem(new NewNodeMenuItem(TextNode.class, false));
				// StrokeTextNode
				this.checkInMenuItem(new NewNodeMenuItem(RichTextNode.class, false));
				
			}
		});
		this.checkInMenuItem(null);

		// Shap...rectangle
		this.checkInMenuItem(new NewNodeMenu() {
			public void init() {
				setLable("Shape");
				this.checkInMenuItem(new NewNodeMenuItem(RectangleNode.class));

				this.checkInMenuItem(new NewNodeMenuItem(BezierNode.class, false));
				this.checkInMenuItem(new NewNodeMenuItem(BezierAssistNode.class, false));
				// EditShapeNode
				this.checkInMenuItem(new NewNodeMenuItem(EditShapeNode.class, false));
			}
		});
		this.checkInMenuItem(null);

		// follow, holdAndFolloe, walk, s
		this.checkInMenuItem(new NewNodeMenu() {
			public void init() {
				setLable("Follow");
				this.checkInMenuItem(new NewNodeMenuItem(FollowNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(HoldAndFollowNode.class));
				this.checkInMenuItem(null);

				this.checkInMenuItem(new NewNodeMenuItem(HFNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(STNode.class));

				this.checkInMenuItem(null);
				this.checkInMenuItem(new NewNodeMenuItem(SliderNode.class));

				this.checkInMenuItem(null);
				this.checkInMenuItem(new NewNodeMenuItem(WalkNode.class, false));
				this.checkInMenuItem(new NewNodeMenuItem(ChairNode.class, false));
			}
		});
		this.checkInMenuItem(null);

		// Layout, Linear
		this.checkInMenuItem(new NewNodeMenu() {
			public void init() {
				setLable("Layout");
				this.checkInMenuItem(new NewNodeMenuItem(LayoutNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(LinearLayoutNode.class));
				// Layout2DNode
				this.checkInMenuItem(new NewNodeMenuItem(Layout2DNode.class));
				// LinearLayout2DNode
				this.checkInMenuItem(new NewNodeMenuItem(LinearLayout2DNode.class, false));
			}
		});
		this.checkInMenuItem(null);
		// Layout, Linear
		this.checkInMenuItem(new NewNodeMenu() {
			public void init() {
				setLable("Fit");
				this.checkInMenuItem(new NewNodeMenuItem(FitPositionNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(FitScaleNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(FitSizeNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(FitAllNode.class));
			}
		});
		this.checkInMenuItem(null);

		// Animate, AnimateFrame
		this.checkInMenuItem(new NewNodeMenu() {
			public void init() {
				setLable("Animate");
				this.checkInMenuItem(new NewNodeMenuItem(AnimateNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(AnimateFrameNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(MultiAnimateNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(SimpleAnimateNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(JointAnimateNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(PuppyNode.class));
				this.checkInMenuItem(null);
				this.checkInMenuItem(new NewNodeMenuItem(ElfMotionNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(ElfMotionKeyNode.class));

				// this.checkInMenuItem(new NewNodeMenuItem(JsonNode.class,
				// false));
				this.checkInMenuItem(new NewNodeMenuItem(BoneNode.class, false));
				// PuppyNode
				// AutoFadeAnimate
				// MultiAnimateNode
				// SampleAnimateNode
			}
		});
		this.checkInMenuItem(null);

		this.checkInMenuItem(new NewNodeMenu() {
			public void init() {
				setLable("Advanced");
				// ElfGrayNode
				this.checkInMenuItem(new NewNodeMenuItem(ElfGrayNode.class, true));

				this.checkInMenuItem(new NewNodeMenuItem(WaveNode.class, false));
				this.checkInMenuItem(new NewNodeMenuItem(BlinkNode.class, false));
				this.checkInMenuItem(new NewNodeMenuItem(MotionBlurNode.class, false));
				this.checkInMenuItem(new NewNodeMenuItem(RenderNode.class, false));
				this.checkInMenuItem(new NewNodeMenuItem(AccumNode.class, false));
				// ShakeNode
				// FreeNode
				this.checkInMenuItem(new NewNodeMenuItem(ShakeNode.class, false));
				this.checkInMenuItem(new NewNodeMenuItem(FreeNode.class, false));
				// RecordNode
				this.checkInMenuItem(new NewNodeMenuItem(RecordNode.class, false));
				this.checkInMenuItem(new NewNodeMenuItem(GenieTest.class, false));
				this.checkInMenuItem(new NewNodeMenuItem(GenieTest2.class, false));
				// Elf3DNode

			}
		});
		this.checkInMenuItem(null);

		// this.checkInMenuItem(new NewNodeMenuItem(Elf3DNode.class));
		this.checkInMenuItem(new NewNodeMenu() {
			public void init() {
				setLable("3D");
				this.checkInMenuItem(new NewNodeMenuItem(Elf3DLayer.class));
				this.checkInMenuItem(new NewNodeMenuItem(Elf3DNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(CoverFlowNode.class));
			}
		});
		this.checkInMenuItem(null);

		// Particle
		this.checkInMenuItem(new NewNodeMenu() {
			public void init() {
				setLable("Particle");
				this.checkInMenuItem(new NewNodeMenuItem(ParticleNode.class));
				this.checkInMenuItem(new NewNodeMenuItem(ParticleEmitter.class, true));
				this.checkInMenuItem(new NewNodeMenuItem(BatchNode.class, true));
			}
		});

		this.checkInMenuItem(null);

		// Progress
		this.checkInMenuItem(new NewNodeMenuItem(ProgressNode.class));
		this.checkInMenuItem(null);

		// Bar
		this.checkInMenuItem(new NewNodeMenuItem(BarNode.class));
		this.checkInMenuItem(null);

		//
		this.checkInMenuItem(null);
		//
		this.checkInMenuItem(new NewNodeMenu() {
			public void init() {
				setLable("Graph");
				this.checkInMenuItem(new NewNodeMenuItem(RectNode.class, true));
				this.checkInMenuItem(new NewNodeMenuItem(RectEdgeNode.class, true));
				this.checkInMenuItem(new NewNodeMenuItem(ColorBarNode.class, true));
				this.checkInMenuItem(new NewNodeMenuItem(ColorButtonNode.class, true));
				this.checkInMenuItem(new NewNodeMenuItem(ColorTabNode.class, true));
				this.checkInMenuItem(new NewNodeMenuItem(ColorFlagOKNode.class, true));
				this.checkInMenuItem(new NewNodeMenuItem(ColorFlagCancelNode.class, true));
				this.checkInMenuItem(new NewNodeMenuItem(GradientNode.class, true));
			}

			public boolean isShow() {
				return true;
			}
		});
	}

	public boolean isShow() {
		final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode();
		return selectNode != null;
	}

	@Override
	public void onClick(SelectionEvent e) {
	}
}
