package com.ielfgame.stupidGame.helper;

public class LearnNode {
	
	/*
	 * basic ElfNode
	 * --多用于父节点, 和 图片设置
	 * ---setResid
	 * 
	 * 
	 * ClipNode
	 * 负责视图的切割, 矩形
	 * 
	 * MapNode
	 * MapContainerNode
	 * 地图拖动类, 可以进行拖动阻力等相关设置
	 * 
	 * ListNode
	 * ListContainerNode -- 
	 * ListScrollNode -- 列表滚条
	 * 列表节点,
	 * 设置方向,
	 * 对齐方式,
	 * 拖动参数
	 * 
	 * PullDownNode
	 * 下拉框节点
	 * 多种方向设置
	 * 
	 * SwipNode
	 * 直线滑动切换节点
	 * 
	 * CircleNode
	 * 弧形滑动选择节点
	 * 
	 * Layout
	 * 	--LayoutNode
	 * 	单行对齐节点, 设置节点之间的间距
	 * 	--Layout2DNode
	 *  多行对齐节点, 设置节点之间的水平和垂直间距
	 *  --LinearLayoutNode
	 *  以填充方式对齐子节点
	 * 	--LinearLayout2DNode
	 * 	--c++目前不支持
	 * 
	 * TouchNode
	 * 		ButtonNode --最普通的按钮节点
	 * 		--设置3张图片, 对应Button的三个状态
	 * 		ClickNode
	 * 		--可以直接设置按钮的3个状态对应的节点, 如点击放大
	 * 		TabNode 
	 * 		--会自动取消同组TabNode下其他节点的按下状态, 常用于选择
	 * 		HoldNode 
	 * 		--长按一段时间后响应
	 * 		ShieldNode 
	 * 		--屏蔽响应节点, 按照节点大小屏蔽
	 * 
	 * Text
	 * 	--TextNode
	 * 		--
	 * 	--Dimensions
	 * 	--RichTextNode
	 *  --TimeNode 时间计时 节点
	 * 	
	 * 
	 * ShapeNode
	 * 		--RectangleNode 纯色的矩形节点, 设置大小, 设置颜色, 多做蒙层
	 * 
	 * 
	 * HFNode - STNode
	 * 	--HFNode 后 拖动  
	 *  --STNode : HFNode施放后目标节点 
	 * 
	 * AnimateNode
	 *  --SimpleAnimateNode
	 *  	--满足大部分帧动画的需求
	 *  	--
	 *  --AnimateNode
	 *  --可以对任意帧独立操作, 帧延迟, 颜色, 透明, 缩放等
	 *  
	 *  --ElfMotionNode
	 *  	--复杂的动画编辑
	 *  
	 *  --MultiAnimateNode
	 *  	--多动画状态节点
	 *  
	 *  --PuppyNode
	 *  	--小人节点
	 *  
	 *  3D
	 * 	--Elf3DLayerNode
	 * 	--Elf3DNode
	 * 	--CoverFlowNode
	 *   --可以做出Mac 上的CoverFlow 视图效果
	 *  
	 *  
	 *  BarNode
	 *  --(左,中,右) (上,中,下) 拼接成一个节点, 
	 *  --setLength(), 设置节点长度
	 *  --多用于血条, 拼接的铭牌
	 *  
	 *  ProgressNode
	 *  --和cocos类同
	 *  
	 *  ParticleNode
	 *  --和cocos类同
	 *  
	 */
	
}
