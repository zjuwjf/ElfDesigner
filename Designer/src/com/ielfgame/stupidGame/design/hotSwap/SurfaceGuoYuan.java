package com.ielfgame.stupidGame.design.hotSwap;

import com.ielfgame.stupidGame.design.Surface.Surface;
import com.ielfgame.stupidGame.face.action.CCActionData;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeAnimate.SimpleAnimateNode;
import elfEngine.basic.node.nodeList.ListNode;
import elfEngine.basic.node.nodeShape.RectangleNode;
import elfEngine.basic.node.nodeText.TextNode;
import elfEngine.basic.node.nodeText.TimeNode;
import elfEngine.basic.node.nodeTouch.ButtonNode;
import elfEngine.basic.node.nodeTouch.CircleNode;
import elfEngine.basic.node.nodeTouch.ClickNode;
import elfEngine.basic.node.nodeTouch.TabNode;

public class SurfaceGuoYuan extends Surface{ 
	
	public SurfaceGuoYuan() { 
		super("D:\\pic\\elf-xml\\果园.xml"); 
	} 
	
	//Owner->id, name 
	static class Owner { 
		int id; 
		String name; 
	} 
	//GuoShu->id, name, lastRefreshTime, refreshTime
	static class GuoShu { 
		int id; 
		String name; 
		int lastRefreshTime, refreshTime; 
	} 
	//GuoShuNode
	static class GuoShuNode { 
		ElfNode xiaoGuo; 
		ButtonNode wuZhu, youZhu; 
		ElfNode bar; 
		TimeNode timer; 
	} 
	
	Owner getOnwerByIndex(final int index) { 
		return null; 
	} 
	
	void setOwnerByIndex(final int index, final Owner owner) { 
		
	} 
	
	GuoShu getGuoShuByIndex(final int index) { 
		return null; 
	} 
	
	GuoShuNode getGuoShuNodeByIndex(final int index) { 
		return null; 
	} 
	
	
	
	public void onStart() { 
		initNodesActions(); 
		
	} 
	
	public void onFinished() {
	} 
	
	public void onResume() {
	} 
	
	public void onPause() { 
	} 
	
	final void initNodesActions() {
		// @@auto-code-start  initialize
		_AllPane = (ElfNode)findNodeByName("AllPane");
		_AllPane_map_container_DiMian = (ElfNode)findNodeByName("AllPane_map_container_DiMian");
		_AllPane_map_container_DiMian_BeiJing = (ElfNode)findNodeByName("AllPane_map_container_DiMian_BeiJing");
		_AllPane_map_container_DiMian_GuoShuJi_A1 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_A1");
		_AllPane_map_container_DiMian_GuoShuJi_A1_WuZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_A1_WuZhu");
		_AllPane_map_container_DiMian_GuoShuJi_A1_YouZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_A1_YouZhu");
		_AllPane_map_container_DiMian_GuoShuJi_B1_0 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_B1_0");
		_AllPane_map_container_DiMian_GuoShuJi_B1_0_WuZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_B1_0_WuZhu");
		_AllPane_map_container_DiMian_GuoShuJi_B1_0_YouZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_B1_0_YouZhu");
		_AllPane_map_container_DiMian_GuoShuJi_B1_1 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_B1_1");
		_AllPane_map_container_DiMian_GuoShuJi_B1_1_WuZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_B1_1_WuZhu");
		_AllPane_map_container_DiMian_GuoShuJi_B1_1_YouZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_B1_1_YouZhu");
		_AllPane_map_container_DiMian_GuoShuJi_B2_2 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_B2_2");
		_AllPane_map_container_DiMian_GuoShuJi_B2_2_WuZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_B2_2_WuZhu");
		_AllPane_map_container_DiMian_GuoShuJi_B2_2_YouZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_B2_2_YouZhu");
		_AllPane_map_container_DiMian_GuoShuJi_C2_0 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_C2_0");
		_AllPane_map_container_DiMian_GuoShuJi_C2_0_WuZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_C2_0_WuZhu");
		_AllPane_map_container_DiMian_GuoShuJi_C2_0_YouZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_C2_0_YouZhu");
		_AllPane_map_container_DiMian_GuoShuJi_C2_1 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_C2_1");
		_AllPane_map_container_DiMian_GuoShuJi_C2_1_WuZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_C2_1_WuZhu");
		_AllPane_map_container_DiMian_GuoShuJi_C2_1_YouZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_C2_1_YouZhu");
		_AllPane_map_container_DiMian_GuoShuJi_C2_2 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_C2_2");
		_AllPane_map_container_DiMian_GuoShuJi_C2_2_WuZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_C2_2_WuZhu");
		_AllPane_map_container_DiMian_GuoShuJi_C2_2_YouZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_C2_2_YouZhu");
		_AllPane_map_container_DiMian_GuoShuJi_C1_3 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_C1_3");
		_AllPane_map_container_DiMian_GuoShuJi_C1_3_WuZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_C1_3_WuZhu");
		_AllPane_map_container_DiMian_GuoShuJi_C1_3_YouZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_C1_3_YouZhu");
		_AllPane_map_container_DiMian_GuoShuJi_C1_4 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_C1_4");
		_AllPane_map_container_DiMian_GuoShuJi_C1_4_WuZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_C1_4_WuZhu");
		_AllPane_map_container_DiMian_GuoShuJi_C1_4_YouZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_C1_4_YouZhu");
		_AllPane_map_container_DiMian_GuoShuJi_D1_0 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_D1_0");
		_AllPane_map_container_DiMian_GuoShuJi_D1_0_WuZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_D1_0_WuZhu");
		_AllPane_map_container_DiMian_GuoShuJi_D1_0_YouZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_D1_0_YouZhu");
		_AllPane_map_container_DiMian_GuoShuJi_D1_1 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_D1_1");
		_AllPane_map_container_DiMian_GuoShuJi_D1_1_WuZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_D1_1_WuZhu");
		_AllPane_map_container_DiMian_GuoShuJi_D1_1_YouZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_D1_1_YouZhu");
		_AllPane_map_container_DiMian_GuoShuJi_D2_2 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_D2_2");
		_AllPane_map_container_DiMian_GuoShuJi_D2_2_WuZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_D2_2_WuZhu");
		_AllPane_map_container_DiMian_GuoShuJi_D2_2_YouZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_D2_2_YouZhu");
		_AllPane_map_container_DiMian_GuoShuJi_D2_3 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_D2_3");
		_AllPane_map_container_DiMian_GuoShuJi_D2_3_WuZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_D2_3_WuZhu");
		_AllPane_map_container_DiMian_GuoShuJi_D2_3_YouZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_D2_3_YouZhu");
		_AllPane_map_container_DiMian_GuoShuJi_D2_4 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_D2_4");
		_AllPane_map_container_DiMian_GuoShuJi_D2_4_WuZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_D2_4_WuZhu");
		_AllPane_map_container_DiMian_GuoShuJi_D2_4_YouZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_D2_4_YouZhu");
		_AllPane_map_container_DiMian_GuoShuJi_D2_5 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_D2_5");
		_AllPane_map_container_DiMian_GuoShuJi_D2_5_WuZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_D2_5_WuZhu");
		_AllPane_map_container_DiMian_GuoShuJi_D2_5_YouZhu = (ButtonNode)findNodeByName("AllPane_map_container_DiMian_GuoShuJi_D2_5_YouZhu");
		_AllPane_map_container_DiMian_BeiJingShang = (ElfNode)findNodeByName("AllPane_map_container_DiMian_BeiJingShang");
		_AllPane_map_container_DiMian_ShengGuang = (ElfNode)findNodeByName("AllPane_map_container_DiMian_ShengGuang");
		_AllPane_map_container_DiMian_ZiJiZhanLing = (ElfNode)findNodeByName("AllPane_map_container_DiMian_ZiJiZhanLing");
		_AllPane_map_container_DiMian_ZiJiZhanLing_11 = (SimpleAnimateNode)findNodeByName("AllPane_map_container_DiMian_ZiJiZhanLing_11");
		_AllPane_map_container_DiMian_ZiJiZhanLing_12 = (SimpleAnimateNode)findNodeByName("AllPane_map_container_DiMian_ZiJiZhanLing_12");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao = (ElfNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao_DaoJiShi = (TimeNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao_DaoJiShi");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao0 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao0");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao0_DaoJiShi = (TimeNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao0_DaoJiShi");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao1 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao1");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao1_DaoJiShi = (TimeNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao1_DaoJiShi");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao2 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao2");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao2_DaoJiShi = (TimeNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao2_DaoJiShi");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao3 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao3");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao3_DaoJiShi = (TimeNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao3_DaoJiShi");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao4 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao4");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao4_DaoJiShi = (TimeNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao4_DaoJiShi");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao5 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao5");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao5_DaoJiShi = (TimeNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao5_DaoJiShi");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao6 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao6");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao6_DaoJiShi = (TimeNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao6_DaoJiShi");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao7 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao7");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao7_DaoJiShi = (TimeNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao7_DaoJiShi");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao8 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao8");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao8_DaoJiShi = (TimeNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao8_DaoJiShi");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao9 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao9");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao9_DaoJiShi = (TimeNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao9_DaoJiShi");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao10 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao10");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao10_DaoJiShi = (TimeNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao10_DaoJiShi");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao11 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao11");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao11_DaoJiShi = (TimeNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao11_DaoJiShi");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao12 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao12");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao12_DaoJiShi = (TimeNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao12_DaoJiShi");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao13 = (ElfNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao13");
		_AllPane_map_container_DiMian_ShiJianJi_Tiao13_DaoJiShi = (TimeNode)findNodeByName("AllPane_map_container_DiMian_ShiJianJi_Tiao13_DaoJiShi");
		_top = (ElfNode)findNodeByName("top");
		_top_circlel = (CircleNode)findNodeByName("top_circlel");
		_top_circlel_player = (ButtonNode)findNodeByName("top_circlel_player");
		_top_circlel_player_intro = (ElfNode)findNodeByName("top_circlel_player_intro");
		_top_circlel_task = (ButtonNode)findNodeByName("top_circlel_task");
		_top_circlel_task_intro = (ElfNode)findNodeByName("top_circlel_task_intro");
		_top_circlel_hero = (ButtonNode)findNodeByName("top_circlel_hero");
		_top_circlel_hero_intro = (ElfNode)findNodeByName("top_circlel_hero_intro");
		_top_circlel_package = (ButtonNode)findNodeByName("top_circlel_package");
		_top_circlel_package_intro = (ElfNode)findNodeByName("top_circlel_package_intro");
		_top_circlel_rank = (ButtonNode)findNodeByName("top_circlel_rank");
		_top_circlel_rank_intro = (ElfNode)findNodeByName("top_circlel_rank_intro");
		_top_circler = (CircleNode)findNodeByName("top_circler");
		_top_circler_social = (ButtonNode)findNodeByName("top_circler_social");
		_top_circler_social_intro = (ElfNode)findNodeByName("top_circler_social_intro");
		_top_circler_mail = (ButtonNode)findNodeByName("top_circler_mail");
		_top_circler_mail_intro = (ElfNode)findNodeByName("top_circler_mail_intro");
		_top_circler_shop = (ButtonNode)findNodeByName("top_circler_shop");
		_top_circler_shop_intro = (ElfNode)findNodeByName("top_circler_shop_intro");
		_top_circler_gonghui = (ButtonNode)findNodeByName("top_circler_gonghui");
		_top_circler_gonghui_intro = (ElfNode)findNodeByName("top_circler_gonghui_intro");
		_top_circler_shezhi = (ButtonNode)findNodeByName("top_circler_shezhi");
		_top_circler_shezhi_intro = (ElfNode)findNodeByName("top_circler_shezhi_intro");
		_top_lordpic = (ClickNode)findNodeByName("top_lordpic");
		_top_lordpic_normal_pic = (ElfNode)findNodeByName("top_lordpic_normal_pic");
		_top_lordpic_pressed_pic = (ElfNode)findNodeByName("top_lordpic_pressed_pic");
		_top_bg_zhanhun_num = (TextNode)findNodeByName("top_bg_zhanhun_num");
		_top_bg_jinbi_num = (TextNode)findNodeByName("top_bg_jinbi_num");
		_top_bg_baoshi_num = (TextNode)findNodeByName("top_bg_baoshi_num");
		_top_talkbar = (ElfNode)findNodeByName("top_talkbar");
		_top_talkbar_hidebutton = (ButtonNode)findNodeByName("top_talkbar_hidebutton");
		_top_talkbar_textbutton = (ButtonNode)findNodeByName("top_talkbar_textbutton");
		_top_talkbar_test = (TextNode)findNodeByName("top_talkbar_test");
		_top_jinqi = (ElfNode)findNodeByName("top_jinqi");
		_top_jinyan = (ElfNode)findNodeByName("top_jinyan");
		_top_circlebutton = (ClickNode)findNodeByName("top_circlebutton");
		_top_lordlevel = (TextNode)findNodeByName("top_lordlevel");
		_top_leftopbutton = (ClickNode)findNodeByName("top_leftopbutton");
		_top_leftopbutton_normal_pic = (ElfNode)findNodeByName("top_leftopbutton_normal_pic");
		_top_leftopbutton_pressed_pic = (ElfNode)findNodeByName("top_leftopbutton_pressed_pic");
		_top_friendbar = (ElfNode)findNodeByName("top_friendbar");
		_top_friendbar_searchbutton = (ButtonNode)findNodeByName("top_friendbar_searchbutton");
		_top_friendbar_backbutton = (ButtonNode)findNodeByName("top_friendbar_backbutton");
		_top_friendbar_refbutton = (ButtonNode)findNodeByName("top_friendbar_refbutton");
		_top_friendbar_tab = (ElfNode)findNodeByName("top_friendbar_tab");
		_top_friendbar_tab_tab1 = (TabNode)findNodeByName("top_friendbar_tab_tab1");
		_top_friendbar_tab_tab2 = (TabNode)findNodeByName("top_friendbar_tab_tab2");
		_top_friendbar_tab_tab3 = (TabNode)findNodeByName("top_friendbar_tab_tab3");
		_top_friendbar_tab_tab4 = (TabNode)findNodeByName("top_friendbar_tab_tab4");
		_top_friendbar_tab_tab5 = (TabNode)findNodeByName("top_friendbar_tab_tab5");
		_top_friendbar_flist = (ListNode)findNodeByName("top_friendbar_flist");
		_top_posname = (TextNode)findNodeByName("top_posname");
		_CaiDan = (ElfNode)findNodeByName("CaiDan");
		_CaiDan_HeiMu = (RectangleNode)findNodeByName("CaiDan_HeiMu");
		_CaiDan_GouMaiGuoShi = (ElfNode)findNodeByName("CaiDan_GouMaiGuoShi");
		_CaiDan_GouMaiGuoShi_ShuaXin = (ButtonNode)findNodeByName("CaiDan_GouMaiGuoShi_ShuaXin");
		_CaiDan_GouMaiGuoShi_GuoShi1 = (ClickNode)findNodeByName("CaiDan_GouMaiGuoShi_GuoShi1");
		_CaiDan_GouMaiGuoShi_GuoShi1_TuBiao = (ElfNode)findNodeByName("CaiDan_GouMaiGuoShi_GuoShi1_TuBiao");
		_CaiDan_GouMaiGuoShi_GuoShi1_JinBi_ShuZhi = (TextNode)findNodeByName("CaiDan_GouMaiGuoShi_GuoShi1_JinBi_ShuZhi");
		_CaiDan_GouMaiGuoShi_GuoShi1_GouMai = (ButtonNode)findNodeByName("CaiDan_GouMaiGuoShi_GuoShi1_GouMai");
		_CaiDan_GouMaiGuoShi_GuoShi1_MingCheng = (TextNode)findNodeByName("CaiDan_GouMaiGuoShi_GuoShi1_MingCheng");
		_CaiDan_GouMaiGuoShi_GuoShi2 = (ClickNode)findNodeByName("CaiDan_GouMaiGuoShi_GuoShi2");
		_CaiDan_GouMaiGuoShi_GuoShi2_TuBiao = (ElfNode)findNodeByName("CaiDan_GouMaiGuoShi_GuoShi2_TuBiao");
		_CaiDan_GouMaiGuoShi_GuoShi2_JinBi_ShuZhi = (TextNode)findNodeByName("CaiDan_GouMaiGuoShi_GuoShi2_JinBi_ShuZhi");
		_CaiDan_GouMaiGuoShi_GuoShi2_GouMai = (ButtonNode)findNodeByName("CaiDan_GouMaiGuoShi_GuoShi2_GouMai");
		_CaiDan_GouMaiGuoShi_GuoShi2_MingCheng = (TextNode)findNodeByName("CaiDan_GouMaiGuoShi_GuoShi2_MingCheng");
		_CaiDan_GouMaiGuoShi_GuoShi3 = (ClickNode)findNodeByName("CaiDan_GouMaiGuoShi_GuoShi3");
		_CaiDan_GouMaiGuoShi_GuoShi3_TuBiao = (ElfNode)findNodeByName("CaiDan_GouMaiGuoShi_GuoShi3_TuBiao");
		_CaiDan_GouMaiGuoShi_GuoShi3_JinBi_ShuZhi = (TextNode)findNodeByName("CaiDan_GouMaiGuoShi_GuoShi3_JinBi_ShuZhi");
		_CaiDan_GouMaiGuoShi_GuoShi3_GouMai = (ButtonNode)findNodeByName("CaiDan_GouMaiGuoShi_GuoShi3_GouMai");
		_CaiDan_GouMaiGuoShi_GuoShi3_MingCheng = (TextNode)findNodeByName("CaiDan_GouMaiGuoShi_GuoShi3_MingCheng");
		_CaiDan_GouMaiGuoShi_HouTui = (ButtonNode)findNodeByName("CaiDan_GouMaiGuoShi_HouTui");
		_CaiDan_GouMaiGuoShi_DaoJiShi = (TimeNode)findNodeByName("CaiDan_GouMaiGuoShi_DaoJiShi");
		_CaiDan_QiangZhanGuoShu = (ElfNode)findNodeByName("CaiDan_QiangZhanGuoShu");
		_CaiDan_QiangZhanGuoShu_QiangZhan = (ButtonNode)findNodeByName("CaiDan_QiangZhanGuoShu_QiangZhan");
		_CaiDan_QiangZhanGuoShu_HouTui = (ButtonNode)findNodeByName("CaiDan_QiangZhanGuoShu_HouTui");
		_CaiDan_QiangZhanGuoShu_ZhanLingZhe = (TextNode)findNodeByName("CaiDan_QiangZhanGuoShu_ZhanLingZhe");
		_CaiDan_QiangZhanGuoShu_GuoShiXinXi = (ButtonNode)findNodeByName("CaiDan_QiangZhanGuoShu_GuoShiXinXi");
		_CaiDan_ZhanLingGuoShu = (ElfNode)findNodeByName("CaiDan_ZhanLingGuoShu");
		_CaiDan_ZhanLingGuoShu_QiangZhan = (ButtonNode)findNodeByName("CaiDan_ZhanLingGuoShu_QiangZhan");
		_CaiDan_ZhanLingGuoShu_HouTui = (ButtonNode)findNodeByName("CaiDan_ZhanLingGuoShu_HouTui");
		_CaiDan_ZhanLingGuoShu_MingCheng = (TextNode)findNodeByName("CaiDan_ZhanLingGuoShu_MingCheng");
		_CaiDan_ZhanLingGuoShu_GuoShiXinXi = (ButtonNode)findNodeByName("CaiDan_ZhanLingGuoShu_GuoShiXinXi");
		_move1 = findActionByName("move1");
		_move2 = findActionByName("move2");
		_show = findActionByName("show");
		_hide = findActionByName("hide");
		_textShow = findActionByName("textShow");
		// @@auto-code-end
	}
	
	// @@auto-code-start defines
	protected ElfNode _AllPane;
	protected ElfNode _AllPane_map_container_DiMian;
	protected ElfNode _AllPane_map_container_DiMian_BeiJing;
	protected ElfNode _AllPane_map_container_DiMian_GuoShuJi_A1;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_A1_WuZhu;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_A1_YouZhu;
	protected ElfNode _AllPane_map_container_DiMian_GuoShuJi_B1_0;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_B1_0_WuZhu;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_B1_0_YouZhu;
	protected ElfNode _AllPane_map_container_DiMian_GuoShuJi_B1_1;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_B1_1_WuZhu;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_B1_1_YouZhu;
	protected ElfNode _AllPane_map_container_DiMian_GuoShuJi_B2_2;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_B2_2_WuZhu;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_B2_2_YouZhu;
	protected ElfNode _AllPane_map_container_DiMian_GuoShuJi_C2_0;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_C2_0_WuZhu;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_C2_0_YouZhu;
	protected ElfNode _AllPane_map_container_DiMian_GuoShuJi_C2_1;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_C2_1_WuZhu;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_C2_1_YouZhu;
	protected ElfNode _AllPane_map_container_DiMian_GuoShuJi_C2_2;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_C2_2_WuZhu;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_C2_2_YouZhu;
	protected ElfNode _AllPane_map_container_DiMian_GuoShuJi_C1_3;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_C1_3_WuZhu;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_C1_3_YouZhu;
	protected ElfNode _AllPane_map_container_DiMian_GuoShuJi_C1_4;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_C1_4_WuZhu;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_C1_4_YouZhu;
	protected ElfNode _AllPane_map_container_DiMian_GuoShuJi_D1_0;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_D1_0_WuZhu;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_D1_0_YouZhu;
	protected ElfNode _AllPane_map_container_DiMian_GuoShuJi_D1_1;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_D1_1_WuZhu;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_D1_1_YouZhu;
	protected ElfNode _AllPane_map_container_DiMian_GuoShuJi_D2_2;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_D2_2_WuZhu;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_D2_2_YouZhu;
	protected ElfNode _AllPane_map_container_DiMian_GuoShuJi_D2_3;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_D2_3_WuZhu;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_D2_3_YouZhu;
	protected ElfNode _AllPane_map_container_DiMian_GuoShuJi_D2_4;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_D2_4_WuZhu;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_D2_4_YouZhu;
	protected ElfNode _AllPane_map_container_DiMian_GuoShuJi_D2_5;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_D2_5_WuZhu;
	protected ButtonNode _AllPane_map_container_DiMian_GuoShuJi_D2_5_YouZhu;
	protected ElfNode _AllPane_map_container_DiMian_BeiJingShang;
	protected ElfNode _AllPane_map_container_DiMian_ShengGuang;
	protected ElfNode _AllPane_map_container_DiMian_ZiJiZhanLing;
	protected SimpleAnimateNode _AllPane_map_container_DiMian_ZiJiZhanLing_11;
	protected SimpleAnimateNode _AllPane_map_container_DiMian_ZiJiZhanLing_12;
	protected ElfNode _AllPane_map_container_DiMian_ShiJianJi_Tiao;
	protected TimeNode _AllPane_map_container_DiMian_ShiJianJi_Tiao_DaoJiShi;
	protected ElfNode _AllPane_map_container_DiMian_ShiJianJi_Tiao0;
	protected TimeNode _AllPane_map_container_DiMian_ShiJianJi_Tiao0_DaoJiShi;
	protected ElfNode _AllPane_map_container_DiMian_ShiJianJi_Tiao1;
	protected TimeNode _AllPane_map_container_DiMian_ShiJianJi_Tiao1_DaoJiShi;
	protected ElfNode _AllPane_map_container_DiMian_ShiJianJi_Tiao2;
	protected TimeNode _AllPane_map_container_DiMian_ShiJianJi_Tiao2_DaoJiShi;
	protected ElfNode _AllPane_map_container_DiMian_ShiJianJi_Tiao3;
	protected TimeNode _AllPane_map_container_DiMian_ShiJianJi_Tiao3_DaoJiShi;
	protected ElfNode _AllPane_map_container_DiMian_ShiJianJi_Tiao4;
	protected TimeNode _AllPane_map_container_DiMian_ShiJianJi_Tiao4_DaoJiShi;
	protected ElfNode _AllPane_map_container_DiMian_ShiJianJi_Tiao5;
	protected TimeNode _AllPane_map_container_DiMian_ShiJianJi_Tiao5_DaoJiShi;
	protected ElfNode _AllPane_map_container_DiMian_ShiJianJi_Tiao6;
	protected TimeNode _AllPane_map_container_DiMian_ShiJianJi_Tiao6_DaoJiShi;
	protected ElfNode _AllPane_map_container_DiMian_ShiJianJi_Tiao7;
	protected TimeNode _AllPane_map_container_DiMian_ShiJianJi_Tiao7_DaoJiShi;
	protected ElfNode _AllPane_map_container_DiMian_ShiJianJi_Tiao8;
	protected TimeNode _AllPane_map_container_DiMian_ShiJianJi_Tiao8_DaoJiShi;
	protected ElfNode _AllPane_map_container_DiMian_ShiJianJi_Tiao9;
	protected TimeNode _AllPane_map_container_DiMian_ShiJianJi_Tiao9_DaoJiShi;
	protected ElfNode _AllPane_map_container_DiMian_ShiJianJi_Tiao10;
	protected TimeNode _AllPane_map_container_DiMian_ShiJianJi_Tiao10_DaoJiShi;
	protected ElfNode _AllPane_map_container_DiMian_ShiJianJi_Tiao11;
	protected TimeNode _AllPane_map_container_DiMian_ShiJianJi_Tiao11_DaoJiShi;
	protected ElfNode _AllPane_map_container_DiMian_ShiJianJi_Tiao12;
	protected TimeNode _AllPane_map_container_DiMian_ShiJianJi_Tiao12_DaoJiShi;
	protected ElfNode _AllPane_map_container_DiMian_ShiJianJi_Tiao13;
	protected TimeNode _AllPane_map_container_DiMian_ShiJianJi_Tiao13_DaoJiShi;
	protected ElfNode _top;
	protected CircleNode _top_circlel;
	protected ButtonNode _top_circlel_player;
	protected ElfNode _top_circlel_player_intro;
	protected ButtonNode _top_circlel_task;
	protected ElfNode _top_circlel_task_intro;
	protected ButtonNode _top_circlel_hero;
	protected ElfNode _top_circlel_hero_intro;
	protected ButtonNode _top_circlel_package;
	protected ElfNode _top_circlel_package_intro;
	protected ButtonNode _top_circlel_rank;
	protected ElfNode _top_circlel_rank_intro;
	protected CircleNode _top_circler;
	protected ButtonNode _top_circler_social;
	protected ElfNode _top_circler_social_intro;
	protected ButtonNode _top_circler_mail;
	protected ElfNode _top_circler_mail_intro;
	protected ButtonNode _top_circler_shop;
	protected ElfNode _top_circler_shop_intro;
	protected ButtonNode _top_circler_gonghui;
	protected ElfNode _top_circler_gonghui_intro;
	protected ButtonNode _top_circler_shezhi;
	protected ElfNode _top_circler_shezhi_intro;
	protected ClickNode _top_lordpic;
	protected ElfNode _top_lordpic_normal_pic;
	protected ElfNode _top_lordpic_pressed_pic;
	protected TextNode _top_bg_zhanhun_num;
	protected TextNode _top_bg_jinbi_num;
	protected TextNode _top_bg_baoshi_num;
	protected ElfNode _top_talkbar;
	protected ButtonNode _top_talkbar_hidebutton;
	protected ButtonNode _top_talkbar_textbutton;
	protected TextNode _top_talkbar_test;
	protected ElfNode _top_jinqi;
	protected ElfNode _top_jinyan;
	protected ClickNode _top_circlebutton;
	protected TextNode _top_lordlevel;
	protected ClickNode _top_leftopbutton;
	protected ElfNode _top_leftopbutton_normal_pic;
	protected ElfNode _top_leftopbutton_pressed_pic;
	protected ElfNode _top_friendbar;
	protected ButtonNode _top_friendbar_searchbutton;
	protected ButtonNode _top_friendbar_backbutton;
	protected ButtonNode _top_friendbar_refbutton;
	protected ElfNode _top_friendbar_tab;
	protected TabNode _top_friendbar_tab_tab1;
	protected TabNode _top_friendbar_tab_tab2;
	protected TabNode _top_friendbar_tab_tab3;
	protected TabNode _top_friendbar_tab_tab4;
	protected TabNode _top_friendbar_tab_tab5;
	protected ListNode _top_friendbar_flist;
	protected TextNode _top_posname;
	protected ElfNode _CaiDan;
	protected RectangleNode _CaiDan_HeiMu;
	protected ElfNode _CaiDan_GouMaiGuoShi;
	protected ButtonNode _CaiDan_GouMaiGuoShi_ShuaXin;
	protected ClickNode _CaiDan_GouMaiGuoShi_GuoShi1;
	protected ElfNode _CaiDan_GouMaiGuoShi_GuoShi1_TuBiao;
	protected TextNode _CaiDan_GouMaiGuoShi_GuoShi1_JinBi_ShuZhi;
	protected ButtonNode _CaiDan_GouMaiGuoShi_GuoShi1_GouMai;
	protected TextNode _CaiDan_GouMaiGuoShi_GuoShi1_MingCheng;
	protected ClickNode _CaiDan_GouMaiGuoShi_GuoShi2;
	protected ElfNode _CaiDan_GouMaiGuoShi_GuoShi2_TuBiao;
	protected TextNode _CaiDan_GouMaiGuoShi_GuoShi2_JinBi_ShuZhi;
	protected ButtonNode _CaiDan_GouMaiGuoShi_GuoShi2_GouMai;
	protected TextNode _CaiDan_GouMaiGuoShi_GuoShi2_MingCheng;
	protected ClickNode _CaiDan_GouMaiGuoShi_GuoShi3;
	protected ElfNode _CaiDan_GouMaiGuoShi_GuoShi3_TuBiao;
	protected TextNode _CaiDan_GouMaiGuoShi_GuoShi3_JinBi_ShuZhi;
	protected ButtonNode _CaiDan_GouMaiGuoShi_GuoShi3_GouMai;
	protected TextNode _CaiDan_GouMaiGuoShi_GuoShi3_MingCheng;
	protected ButtonNode _CaiDan_GouMaiGuoShi_HouTui;
	protected TimeNode _CaiDan_GouMaiGuoShi_DaoJiShi;
	protected ElfNode _CaiDan_QiangZhanGuoShu;
	protected ButtonNode _CaiDan_QiangZhanGuoShu_QiangZhan;
	protected ButtonNode _CaiDan_QiangZhanGuoShu_HouTui;
	protected TextNode _CaiDan_QiangZhanGuoShu_ZhanLingZhe;
	protected ButtonNode _CaiDan_QiangZhanGuoShu_GuoShiXinXi;
	protected ElfNode _CaiDan_ZhanLingGuoShu;
	protected ButtonNode _CaiDan_ZhanLingGuoShu_QiangZhan;
	protected ButtonNode _CaiDan_ZhanLingGuoShu_HouTui;
	protected TextNode _CaiDan_ZhanLingGuoShu_MingCheng;
	protected ButtonNode _CaiDan_ZhanLingGuoShu_GuoShiXinXi;
	protected CCActionData _move1;
	protected CCActionData _move2;
	protected CCActionData _show;
	protected CCActionData _hide;
	protected CCActionData _textShow;
	// @@auto-code-end
	
} 
