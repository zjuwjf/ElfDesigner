package com.ielfgame.stupidGame.design.hotSwap;

import java.util.HashMap;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.FiniteTimeAction;
import org.cocos2d.actions.interval.DelayTime;
import org.cocos2d.actions.interval.FadeIn;
import org.cocos2d.actions.interval.FadeOut;
import org.cocos2d.actions.interval.Sequence;

import com.ielfgame.stupidGame.design.Surface.Surface;
import com.ielfgame.stupidGame.face.action.CCActionData;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeShape.RectangleNode;
import elfEngine.basic.node.nodeText.TextNode;

public class Surfaceopen extends Surface{ 
	
	public Surfaceopen() { 
		super("C:\\Users\\zju_wjf\\Desktop\\open.xml"); 
	} 
	
	static String [] mTalks = {
			"亿年之前,诸神大战.万世寂灭.散落世间众多神器.",
			
			"古老的族书记载,塞尔比家族一度辉煌无比.", 
			"却在千年前的\"灭世之乱\"中逐渐没落, 至今...", 
			"沦落为尤加大陆上的三流贵族.", 
			
			"我手上的这串碎片,我们家族最深的秘密.", 
			"其实是一枚神器的残片.她的作用是...", 
			"通过特殊的仪式,将其它的部分拼合在一起,使神器复原!将其唤醒!", 
			"重现家族千年前的荣耀!", 
			
			"不用吃惊为何我会出现在你的领地",
			"把挂饰交给我,然后你们可以消失了",
			
			"虽然不知道你们到底是谁.如何进入我的私人领地的",
			"但是从你话语中知道你是充满敌意的...所以",
			
			"家族侍卫们. 击杀他们....!",
			
			"从今以后,就有你来继承塞尔比家族了.",
			"今后的路要靠你自己走下去了...我的孩子..."
	}; 
	
	@Override
	public void onStart() { 
		initNodesActions(); 
		
		mIndexMap.clear(); 
		float [] delays = {0.5f, 5, 12, 13,
				2, 8, 7, 2, 3, 5}; 
		final FiniteTimeAction [] actions = new FiniteTimeAction[delays.length];
		for(int i=0; i<delays.length; i++) {
			final int index = i;
			final FiniteTimeAction d = DelayTime.action(delays[i]);
			d.setListener(new Runnable() { 
				public void run() { 
					step(index); 
				} 
			});
			actions[i] = d;
		} 
		
		setChildVisible(_BeiJing, false); 
		setChildVisible(_DiaoZhui, false); 
		_BeiJing_FuZi_DiRen.setVisible(false); 
		setChildVisible(_BeiJing_FuZi_DiRen, false); 
		
		_DiaoZhui.setVisible(true);
		_BeiJing.runElfAction( Sequence.actions(DelayTime.action(0), actions) );
	} 
	
	final HashMap<ElfNode, Integer> mIndexMap = new HashMap<ElfNode, Integer>();
	
	void step(final int index) { 
		switch(index) { 
		case 0:
			//镜头1   5s
			goToNextChild(_BeiJing, _XiaoShi, _ChuXian, 0, 0); 
			goToNextChild(_DiaoZhui, _XiaoShi, _ChuXian, 0, 0); 
			_DiaoZhui.runElfAction(_downRotate.newAction()); 
			nextText(1, _WenZi, mTalks, 2.2f, 3);
			break;
		case 1:
			//镜头2   12s
			goToNextChild(_BeiJing, _XiaoShi, _ChuXian, 0, 0); 
//			goToNextChild(_DiaoZhui,  _XiaoShi, _ChuXian, 0, 0); 
			_BeiJing_BiHua.runAction(_left2right.newAction()); 
			nextText(3, _WenZi, mTalks, 1, 4);
			break;
		case 2:
			//镜头3   13
			nextText(4, _WenZi, mTalks, 3, 2.5f);
			goToNextChild(_BeiJing, _XiaoShi, _ChuXian, 0, 1); 
			goToNextChild(_DiaoZhui, _XiaoShi, _ChuXian, 0, 1); 
			_BeiJing_FuZi_FuQin.runElfAction(_FuQin.newAction()); 
			_BeiJing_FuZi_HaiZi1.runElfAction(_XiaoHai.newAction()); 
			break;
		case 3:
			//镜头4   2
			_BeiJing_FuZi_DiRen.setVisible(true); 
			final ElfNode [] diRens = _BeiJing_FuZi_DiRen.getChilds();
			for(int i=0; i<3; i++) { 
				final int ii = i;
				runWithDelay(diRens[i], FadeIn.action(0.1f), i*0.1f, new Runnable() {
					public void run() {
						diRens[ii].setVisible(true);
					}
				});
			} 
			goToChildByIndex(_BeiJing_FuZi, 3, _XiaoShi, _ChuXian, 0, 0); 
			break;
		case 4:
			//镜头5   8 
			goToNextChild(_BeiJing, null, _JingTou, 0, 0); 
			nextText(2, _WenZi, mTalks, 2, 3); 
			break;
		case 5:
			//镜头6   7
			goToNextChild(_BeiJing, _XiaoShi, _ChuXian, 0, 0); 
			this.setChildVisible(_BeiJing_WeiShi, false);
			this._BeiJing_WeiShi_FuZi.setVisible(true);
			goToChildByIndex(_BeiJing_FuZi, 1, _XiaoShi, _ChuXian, 0, 0); 
			nextText(2, _WenZi, mTalks, 1, 3); 
			break;
		case 6:
			//镜头7   2
			final ElfNode [] weiSis = {_BeiJing_WeiShi_l1,_BeiJing_WeiShi_l2,_BeiJing_WeiShi_l3,
					_BeiJing_WeiShi_l4,_BeiJing_WeiShi_l5};
			final CCActionData [] datas = {_l1,_l2,_l3,_l4,_l5};
			
			runWithDelay(weiSis[0], datas[0].newAction(), 0, null);
			runWithDelay(weiSis[1], datas[1].newAction(), 0.1f, null);
			runWithDelay(weiSis[2], datas[2].newAction(), 0.05f, null);
			runWithDelay(weiSis[3], datas[3].newAction(), 0.1f, null);
			runWithDelay(weiSis[4], datas[4].newAction(), 0, null);
			
			nextText(1, _WenZi, mTalks, 0, 2);
			break;
		case 7:
			//镜头8   2
			goToNextChild(_BeiJing, _XiaoShi, _ChuXian, 0, 0); 
			break;
		case 8:
			//镜头9   5
			goToNextChild(_BeiJing, _XiaoShi, _ChuXian, 0, 0); 
			nextText(2, _WenZi, mTalks, 1, 3);
			break;
		}
	} 
	
	void nextText(final int count, final TextNode textNode, final String [] lines, final float delay,
			final float step) { 
		for(int i=0; i<count; i++) {
			final int index = getNextIndexByNode(textNode);
			runWithDelay(textNode, showTextAction(step), delay+i*step, new Runnable() {
				public void run() { 
					textNode.setText(lines[index]);
				}
			});
		} 
	} 
	
	int getNextIndexByNode(final ElfNode node) {
		final int index;
		if(mIndexMap.containsKey(node)) { 
			index = mIndexMap.get(node)+1; 
		} else { 
			index = 0; 
		} 
		mIndexMap.put(node, index); 
		return index;
	}
	
	CCAction showTextAction(float last) {
		return Sequence.actions(FadeIn.action(1), DelayTime.action(last-2), FadeOut.action(1));
	}
	
	void runWithDelay(final ElfNode node , final CCAction action, float delay, final Runnable pRun) {
		DelayTime d = DelayTime.action(delay);
		d.setListener(new Runnable() {
			public void run() {
				if(pRun != null) {
					pRun.run();
				}
				if(action != null) {
					node.runElfAction(action);
				} 
			}
		});
		_DiaoZhui.runAction(d);
	}
	
	void setChildVisible(final ElfNode node, boolean visible) { 
		final ElfNode [] childs = node.getChilds(); 
		for(ElfNode n : childs) { 
			n.setVisible(visible);
		} 
	} 
	
	void goToNextChild(final ElfNode node, final CCActionData oldAction, final CCActionData nowAction, float delay0, float delay1) {
		final int index = getNextIndexByNode(node); 
		goToChildByIndex(node, index, oldAction, nowAction, delay0, delay1); 
	} 
	
	void goToChildByIndex(final ElfNode node, final int index, 
			final CCActionData oldAction, final CCActionData nowAction, float delay0, float delay1) {
		final ElfNode [] childs = node.getChilds(); 
		final ElfNode old = index>0? childs[index-1]:null; 
		final ElfNode now = index<childs.length? childs[index]:null; 
		if(old != null) { 
			DelayTime delay = DelayTime.action(delay0);
			delay.setListener(new Runnable() {
				public void run() {
					if(oldAction != null) {
						old.runAction(oldAction.newAction()); 
					} else {
						old.setVisible(false);
					}
				}
			});
			old.runAction(delay); 
		} 
		if(now != null) { 
			DelayTime delay = DelayTime.action(delay1);
			delay.setListener(new Runnable() {
				public void run() {
					if(nowAction != null) {
						now.runAction(nowAction.newAction()); 
					} else {
						now.setVisible(false);
					}
				} 
			});
			now.runAction(delay); 
		} 
	}
	
	@Override
	public void onFinished() {
	} 
	
	@Override
	public void onResume() { 
	} 
	
	@Override
	public void onPause() { 
	} 
	
	final void initNodesActions() {
		// @@auto-code-start  initialize
		_BeiJing = (ElfNode)findNodeByName("BeiJing");
		_BeiJing_KaiChang = (ElfNode)findNodeByName("BeiJing_KaiChang");
		_BeiJing_BiHua = (ElfNode)findNodeByName("BeiJing_BiHua");
		_BeiJing_BiHua_1 = (ElfNode)findNodeByName("BeiJing_BiHua_1");
		_BeiJing_BiHua_2 = (ElfNode)findNodeByName("BeiJing_BiHua_2");
		_BeiJing_FuZi = (ElfNode)findNodeByName("BeiJing_FuZi");
		_BeiJing_FuZi_FuQin = (ElfNode)findNodeByName("BeiJing_FuZi_FuQin");
		_BeiJing_FuZi_DiRen = (ElfNode)findNodeByName("BeiJing_FuZi_DiRen");
		_BeiJing_FuZi_DiRen_1 = (ElfNode)findNodeByName("BeiJing_FuZi_DiRen_1");
		_BeiJing_FuZi_DiRen_2 = (ElfNode)findNodeByName("BeiJing_FuZi_DiRen_2");
		_BeiJing_FuZi_DiRen_3 = (ElfNode)findNodeByName("BeiJing_FuZi_DiRen_3");
		_BeiJing_FuZi_HaiZi1 = (ElfNode)findNodeByName("BeiJing_FuZi_HaiZi1");
		_BeiJing_FuZi_HaiZi2 = (ElfNode)findNodeByName("BeiJing_FuZi_HaiZi2");
		_BeiJing_ShenMiRen = (ElfNode)findNodeByName("BeiJing_ShenMiRen");
		_BeiJing_WeiShi = (ElfNode)findNodeByName("BeiJing_WeiShi");
		_BeiJing_WeiShi_l3 = (ElfNode)findNodeByName("BeiJing_WeiShi_l3");
		_BeiJing_WeiShi_l2 = (ElfNode)findNodeByName("BeiJing_WeiShi_l2");
		_BeiJing_WeiShi_FuZi = (ElfNode)findNodeByName("BeiJing_WeiShi_FuZi");
		_BeiJing_WeiShi_l5 = (ElfNode)findNodeByName("BeiJing_WeiShi_l5");
		_BeiJing_WeiShi_l4 = (ElfNode)findNodeByName("BeiJing_WeiShi_l4");
		_BeiJing_WeiShi_l1 = (ElfNode)findNodeByName("BeiJing_WeiShi_l1");
		_BeiJing_HeiPing = (RectangleNode)findNodeByName("BeiJing_HeiPing");
		_BeiJing_JieWei = (ElfNode)findNodeByName("BeiJing_JieWei");
		_DiaoZhui = (ElfNode)findNodeByName("DiaoZhui");
		_DiaoZhui_1 = (ElfNode)findNodeByName("DiaoZhui_1");
		_DiaoZhui_1_2 = (ElfNode)findNodeByName("DiaoZhui_1_2");
		_ZheZhao = (ElfNode)findNodeByName("ZheZhao");
		_ZheZhao_up = (RectangleNode)findNodeByName("ZheZhao_up");
		_ZheZhao_down = (RectangleNode)findNodeByName("ZheZhao_down");
		_WenZi = (TextNode)findNodeByName("WenZi");
		_downRotate = findActionByName("downRotate");
		_left2right = findActionByName("left2right");
		_XiaoShi = findActionByName("消失");
		_ChuXian = findActionByName("出现");
		_DiYiWenZiChuXianShiJian = findActionByName("第一文字出现时间");
		_XiaoHai = findActionByName("小孩");
		_FuQin = findActionByName("父亲");
		_JingTou = findActionByName("镜头");
		_l1 = findActionByName("l1");
		_l2 = findActionByName("l2");
		_l3 = findActionByName("l3");
		_l4 = findActionByName("l4");
		_l5 = findActionByName("l5");
		// @@auto-code-end
	}
	
	// @@auto-code-start defines
	protected ElfNode _BeiJing;
	protected ElfNode _BeiJing_KaiChang;
	protected ElfNode _BeiJing_BiHua;
	protected ElfNode _BeiJing_BiHua_1;
	protected ElfNode _BeiJing_BiHua_2;
	protected ElfNode _BeiJing_FuZi;
	protected ElfNode _BeiJing_FuZi_FuQin;
	protected ElfNode _BeiJing_FuZi_DiRen;
	protected ElfNode _BeiJing_FuZi_DiRen_1;
	protected ElfNode _BeiJing_FuZi_DiRen_2;
	protected ElfNode _BeiJing_FuZi_DiRen_3;
	protected ElfNode _BeiJing_FuZi_HaiZi1;
	protected ElfNode _BeiJing_FuZi_HaiZi2;
	protected ElfNode _BeiJing_ShenMiRen;
	protected ElfNode _BeiJing_WeiShi;
	protected ElfNode _BeiJing_WeiShi_l3;
	protected ElfNode _BeiJing_WeiShi_l2;
	protected ElfNode _BeiJing_WeiShi_FuZi;
	protected ElfNode _BeiJing_WeiShi_l5;
	protected ElfNode _BeiJing_WeiShi_l4;
	protected ElfNode _BeiJing_WeiShi_l1;
	protected RectangleNode _BeiJing_HeiPing;
	protected ElfNode _BeiJing_JieWei;
	protected ElfNode _DiaoZhui;
	protected ElfNode _DiaoZhui_1;
	protected ElfNode _DiaoZhui_1_2;
	protected ElfNode _ZheZhao;
	protected RectangleNode _ZheZhao_up;
	protected RectangleNode _ZheZhao_down;
	protected TextNode _WenZi;
	protected CCActionData _downRotate;
	protected CCActionData _left2right;
	protected CCActionData _XiaoShi;
	protected CCActionData _ChuXian;
	protected CCActionData _DiYiWenZiChuXianShiJian;
	protected CCActionData _XiaoHai;
	protected CCActionData _FuQin;
	protected CCActionData _JingTou;
	protected CCActionData _l1;
	protected CCActionData _l2;
	protected CCActionData _l3;
	protected CCActionData _l4;
	protected CCActionData _l5;
	// @@auto-code-end
} 
