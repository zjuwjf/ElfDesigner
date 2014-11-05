package com.ielfgame.stupidGame.design.hotSwap;

import com.ielfgame.stupidGame.design.Surface.Surface;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.bar.BarNode;
import elfEngine.basic.node.nodeLayout.LayoutNode;
import elfEngine.basic.node.nodeText.TextNode;
import elfEngine.basic.node.nodeText.TimeNode;
import elfEngine.basic.node.nodeTouch.ButtonNode;
import elfEngine.basic.node.nodeTouch.TabNode;

public class SurfaceHeroBar extends Surface{ 
	
	public SurfaceHeroBar() { 
		super("D:\\pic\\test\\xml\\HeroBar.xml"); 
	} 
		
	@Override
	public void onStart() { 
		initNodesActions();
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
		_AllPane = (ElfNode)findNodeByName("AllPane");
		_AllPane_mainpane = (ElfNode)findNodeByName("AllPane_mainpane");
		_AllPane_mainpane_hero1 = (ElfNode)findNodeByName("AllPane_mainpane_hero1");
		_AllPane_mainpane_hero1_icon = (ElfNode)findNodeByName("AllPane_mainpane_hero1_icon");
		_AllPane_mainpane_hero1_heropic_pos = (ElfNode)findNodeByName("AllPane_mainpane_hero1_heropic_pos");
		_AllPane_mainpane_hero1_siwei_tili = (TextNode)findNodeByName("AllPane_mainpane_hero1_siwei_tili");
		_AllPane_mainpane_hero1_siwei_liliang = (TextNode)findNodeByName("AllPane_mainpane_hero1_siwei_liliang");
		_AllPane_mainpane_hero1_siwei_zhili = (TextNode)findNodeByName("AllPane_mainpane_hero1_siwei_zhili");
		_AllPane_mainpane_hero1_siwei_mingjie = (TextNode)findNodeByName("AllPane_mainpane_hero1_siwei_mingjie");
		_AllPane_mainpane_hero1_siwei_tilibar = (BarNode)findNodeByName("AllPane_mainpane_hero1_siwei_tilibar");
		_AllPane_mainpane_hero1_siwei_liliangbar = (BarNode)findNodeByName("AllPane_mainpane_hero1_siwei_liliangbar");
		_AllPane_mainpane_hero1_siwei_zhilibar = (BarNode)findNodeByName("AllPane_mainpane_hero1_siwei_zhilibar");
		_AllPane_mainpane_hero1_siwei_mingjiebar = (BarNode)findNodeByName("AllPane_mainpane_hero1_siwei_mingjiebar");
		_AllPane_mainpane_hero1_name = (TextNode)findNodeByName("AllPane_mainpane_hero1_name");
		_AllPane_mainpane_hero1_zhaomubutton = (ButtonNode)findNodeByName("AllPane_mainpane_hero1_zhaomubutton");
		_AllPane_mainpane_hero1_checkbutton = (ButtonNode)findNodeByName("AllPane_mainpane_hero1_checkbutton");
		_AllPane_mainpane_hero1_starlayout = (LayoutNode)findNodeByName("AllPane_mainpane_hero1_starlayout");
		_AllPane_mainpane_hero1_unstarlayout = (LayoutNode)findNodeByName("AllPane_mainpane_hero1_unstarlayout");
		_AllPane_mainpane_hero1_jinbiprice = (ElfNode)findNodeByName("AllPane_mainpane_hero1_jinbiprice");
		_AllPane_mainpane_hero1_jinbiprice_text = (TextNode)findNodeByName("AllPane_mainpane_hero1_jinbiprice_text");
		_AllPane_mainpane_hero1_baoshiprice = (ElfNode)findNodeByName("AllPane_mainpane_hero1_baoshiprice");
		_AllPane_mainpane_hero1_baoshiprice_text = (TextNode)findNodeByName("AllPane_mainpane_hero1_baoshiprice_text");
		_AllPane_mainpane_hero2 = (ElfNode)findNodeByName("AllPane_mainpane_hero2");
		_AllPane_mainpane_hero2_icon = (ElfNode)findNodeByName("AllPane_mainpane_hero2_icon");
		_AllPane_mainpane_hero2_heropic_pos = (ElfNode)findNodeByName("AllPane_mainpane_hero2_heropic_pos");
		_AllPane_mainpane_hero2_siwei_tili = (TextNode)findNodeByName("AllPane_mainpane_hero2_siwei_tili");
		_AllPane_mainpane_hero2_siwei_liliang = (TextNode)findNodeByName("AllPane_mainpane_hero2_siwei_liliang");
		_AllPane_mainpane_hero2_siwei_zhili = (TextNode)findNodeByName("AllPane_mainpane_hero2_siwei_zhili");
		_AllPane_mainpane_hero2_siwei_mingjie = (TextNode)findNodeByName("AllPane_mainpane_hero2_siwei_mingjie");
		_AllPane_mainpane_hero2_siwei_tilibar = (BarNode)findNodeByName("AllPane_mainpane_hero2_siwei_tilibar");
		_AllPane_mainpane_hero2_siwei_liliangbar = (BarNode)findNodeByName("AllPane_mainpane_hero2_siwei_liliangbar");
		_AllPane_mainpane_hero2_siwei_zhilibar = (BarNode)findNodeByName("AllPane_mainpane_hero2_siwei_zhilibar");
		_AllPane_mainpane_hero2_siwei_mingjiebar = (BarNode)findNodeByName("AllPane_mainpane_hero2_siwei_mingjiebar");
		_AllPane_mainpane_hero2_name = (TextNode)findNodeByName("AllPane_mainpane_hero2_name");
		_AllPane_mainpane_hero2_zhaomubutton = (ButtonNode)findNodeByName("AllPane_mainpane_hero2_zhaomubutton");
		_AllPane_mainpane_hero2_checkbutton = (ButtonNode)findNodeByName("AllPane_mainpane_hero2_checkbutton");
		_AllPane_mainpane_hero2_starlayout = (LayoutNode)findNodeByName("AllPane_mainpane_hero2_starlayout");
		_AllPane_mainpane_hero2_unstarlayout = (LayoutNode)findNodeByName("AllPane_mainpane_hero2_unstarlayout");
		_AllPane_mainpane_hero2_jinbiprice = (ElfNode)findNodeByName("AllPane_mainpane_hero2_jinbiprice");
		_AllPane_mainpane_hero2_jinbiprice_text = (TextNode)findNodeByName("AllPane_mainpane_hero2_jinbiprice_text");
		_AllPane_mainpane_hero2_baoshiprice = (ElfNode)findNodeByName("AllPane_mainpane_hero2_baoshiprice");
		_AllPane_mainpane_hero2_baoshiprice_text = (TextNode)findNodeByName("AllPane_mainpane_hero2_baoshiprice_text");
		_AllPane_mainpane_hero3 = (ElfNode)findNodeByName("AllPane_mainpane_hero3");
		_AllPane_mainpane_hero3_icon = (ElfNode)findNodeByName("AllPane_mainpane_hero3_icon");
		_AllPane_mainpane_hero3_heropic_pos = (ElfNode)findNodeByName("AllPane_mainpane_hero3_heropic_pos");
		_AllPane_mainpane_hero3_siwei_tili = (TextNode)findNodeByName("AllPane_mainpane_hero3_siwei_tili");
		_AllPane_mainpane_hero3_siwei_liliang = (TextNode)findNodeByName("AllPane_mainpane_hero3_siwei_liliang");
		_AllPane_mainpane_hero3_siwei_zhili = (TextNode)findNodeByName("AllPane_mainpane_hero3_siwei_zhili");
		_AllPane_mainpane_hero3_siwei_mingjie = (TextNode)findNodeByName("AllPane_mainpane_hero3_siwei_mingjie");
		_AllPane_mainpane_hero3_siwei_tilibar = (BarNode)findNodeByName("AllPane_mainpane_hero3_siwei_tilibar");
		_AllPane_mainpane_hero3_siwei_liliangbar = (BarNode)findNodeByName("AllPane_mainpane_hero3_siwei_liliangbar");
		_AllPane_mainpane_hero3_siwei_zhilibar = (BarNode)findNodeByName("AllPane_mainpane_hero3_siwei_zhilibar");
		_AllPane_mainpane_hero3_siwei_mingjiebar = (BarNode)findNodeByName("AllPane_mainpane_hero3_siwei_mingjiebar");
		_AllPane_mainpane_hero3_name = (TextNode)findNodeByName("AllPane_mainpane_hero3_name");
		_AllPane_mainpane_hero3_zhaomubutton = (ButtonNode)findNodeByName("AllPane_mainpane_hero3_zhaomubutton");
		_AllPane_mainpane_hero3_checkbutton = (ButtonNode)findNodeByName("AllPane_mainpane_hero3_checkbutton");
		_AllPane_mainpane_hero3_starlayout = (LayoutNode)findNodeByName("AllPane_mainpane_hero3_starlayout");
		_AllPane_mainpane_hero3_unstarlayout = (LayoutNode)findNodeByName("AllPane_mainpane_hero3_unstarlayout");
		_AllPane_mainpane_hero3_jinbiprice = (ElfNode)findNodeByName("AllPane_mainpane_hero3_jinbiprice");
		_AllPane_mainpane_hero3_jinbiprice_text = (TextNode)findNodeByName("AllPane_mainpane_hero3_jinbiprice_text");
		_AllPane_mainpane_hero3_baoshiprice = (ElfNode)findNodeByName("AllPane_mainpane_hero3_baoshiprice");
		_AllPane_mainpane_hero3_baoshiprice_text = (TextNode)findNodeByName("AllPane_mainpane_hero3_baoshiprice_text");
		_AllPane_mainpane_emphero1 = (ElfNode)findNodeByName("AllPane_mainpane_emphero1");
		_AllPane_mainpane_emphero2 = (ElfNode)findNodeByName("AllPane_mainpane_emphero2");
		_AllPane_mainpane_emphero3 = (ElfNode)findNodeByName("AllPane_mainpane_emphero3");
		_AllPane_mainpane_shuaxinbutton = (ButtonNode)findNodeByName("AllPane_mainpane_shuaxinbutton");
		_AllPane_mainpane_shuaxincount_time = (TimeNode)findNodeByName("AllPane_mainpane_shuaxincount_time");
		_AllPane_bg_Exit = (ButtonNode)findNodeByName("AllPane_bg_Exit");
		_AllPane_darkbg = (ElfNode)findNodeByName("AllPane_darkbg");
		_AllPane_herodetailpane = (ElfNode)findNodeByName("AllPane_herodetailpane");
		_AllPane_herodetailpane_Exit = (ButtonNode)findNodeByName("AllPane_herodetailpane_Exit");
		_AllPane_herodetailpane_pane_shuxing = (ElfNode)findNodeByName("AllPane_herodetailpane_pane_shuxing");
		_AllPane_herodetailpane_pane_shuxing_show_ti = (BarNode)findNodeByName("AllPane_herodetailpane_pane_shuxing_show_ti");
		_AllPane_herodetailpane_pane_shuxing_show_li = (BarNode)findNodeByName("AllPane_herodetailpane_pane_shuxing_show_li");
		_AllPane_herodetailpane_pane_shuxing_show_zhi = (BarNode)findNodeByName("AllPane_herodetailpane_pane_shuxing_show_zhi");
		_AllPane_herodetailpane_pane_shuxing_show_ming = (BarNode)findNodeByName("AllPane_herodetailpane_pane_shuxing_show_ming");
		_AllPane_herodetailpane_pane_shuxing_num = (LayoutNode)findNodeByName("AllPane_herodetailpane_pane_shuxing_num");
		_AllPane_herodetailpane_pane_shuxing_incnum = (LayoutNode)findNodeByName("AllPane_herodetailpane_pane_shuxing_incnum");
		_AllPane_herodetailpane_pane_dshuxing = (LayoutNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing");
		_AllPane_herodetailpane_pane_dshuxing_s1_hp = (ElfNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s1_hp");
		_AllPane_herodetailpane_pane_dshuxing_s1_hp_num = (TextNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s1_hp_num");
		_AllPane_herodetailpane_pane_dshuxing_s1_phyattack = (ElfNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s1_phyattack");
		_AllPane_herodetailpane_pane_dshuxing_s1_phyattack_num = (TextNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s1_phyattack_num");
		_AllPane_herodetailpane_pane_dshuxing_s1_magicattack = (ElfNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s1_magicattack");
		_AllPane_herodetailpane_pane_dshuxing_s1_magicattack_num = (TextNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s1_magicattack_num");
		_AllPane_herodetailpane_pane_dshuxing_s1_criticalratio = (ElfNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s1_criticalratio");
		_AllPane_herodetailpane_pane_dshuxing_s1_criticalratio_num = (TextNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s1_criticalratio_num");
		_AllPane_herodetailpane_pane_dshuxing_s1_criticalnum = (ElfNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s1_criticalnum");
		_AllPane_herodetailpane_pane_dshuxing_s1_criticalnum_num = (TextNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s1_criticalnum_num");
		_AllPane_herodetailpane_pane_dshuxing_s2_speed = (ElfNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s2_speed");
		_AllPane_herodetailpane_pane_dshuxing_s2_speed_num = (TextNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s2_speed_num");
		_AllPane_herodetailpane_pane_dshuxing_s2_phydef = (ElfNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s2_phydef");
		_AllPane_herodetailpane_pane_dshuxing_s2_phydef_num = (TextNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s2_phydef_num");
		_AllPane_herodetailpane_pane_dshuxing_s2_magicdef = (ElfNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s2_magicdef");
		_AllPane_herodetailpane_pane_dshuxing_s2_magicdef_num = (TextNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s2_magicdef_num");
		_AllPane_herodetailpane_pane_dshuxing_s2_missratio = (ElfNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s2_missratio");
		_AllPane_herodetailpane_pane_dshuxing_s2_missratio_num = (TextNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s2_missratio_num");
		_AllPane_herodetailpane_pane_dshuxing_s2_tough = (ElfNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s2_tough");
		_AllPane_herodetailpane_pane_dshuxing_s2_tough_num = (TextNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s2_tough_num");
		_AllPane_herodetailpane_pane_dshuxing_s3_hitadd = (ElfNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s3_hitadd");
		_AllPane_herodetailpane_pane_dshuxing_s3_hitadd_num = (TextNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s3_hitadd_num");
		_AllPane_herodetailpane_pane_dshuxing_s3_phybre = (ElfNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s3_phybre");
		_AllPane_herodetailpane_pane_dshuxing_s3_phybre_num = (TextNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s3_phybre_num");
		_AllPane_herodetailpane_pane_dshuxing_s3_magicbre = (ElfNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s3_magicbre");
		_AllPane_herodetailpane_pane_dshuxing_s3_magicbre_num = (TextNode)findNodeByName("AllPane_herodetailpane_pane_dshuxing_s3_magicbre_num");
		_AllPane_herodetailpane_pane_best_bestpos = (ElfNode)findNodeByName("AllPane_herodetailpane_pane_best_bestpos");
		_AllPane_herodetailpane_pane_intro_heroname_bar = (BarNode)findNodeByName("AllPane_herodetailpane_pane_intro_heroname_bar");
		_AllPane_herodetailpane_pane_intro_heroname_text = (TextNode)findNodeByName("AllPane_herodetailpane_pane_intro_heroname_text");
		_AllPane_herodetailpane_pane_intro_heropic = (ElfNode)findNodeByName("AllPane_herodetailpane_pane_intro_heropic");
		_AllPane_choosepane = (ElfNode)findNodeByName("AllPane_choosepane");
		_AllPane_choosepane_back = (ButtonNode)findNodeByName("AllPane_choosepane_back");
		_AllPane_choosepane_enter = (ButtonNode)findNodeByName("AllPane_choosepane_enter");
		_AllPane_choosepane_choosetab = (ElfNode)findNodeByName("AllPane_choosepane_choosetab");
		_AllPane_choosepane_choosetab_choose1 = (TabNode)findNodeByName("AllPane_choosepane_choosetab_choose1");
		_AllPane_choosepane_choosetab_choose2 = (TabNode)findNodeByName("AllPane_choosepane_choosetab_choose2");
		_AllPane_choosepane_choosetab_choose3 = (TabNode)findNodeByName("AllPane_choosepane_choosetab_choose3");
		_AllPane_ensurepane = (ElfNode)findNodeByName("AllPane_ensurepane");
		_AllPane_ensurepane_back = (ButtonNode)findNodeByName("AllPane_ensurepane_back");
		_AllPane_ensurepane_ensure = (ButtonNode)findNodeByName("AllPane_ensurepane_ensure");
		_AllPane_ensurepane_text = (TextNode)findNodeByName("AllPane_ensurepane_text");
		_AllPane_corepane = (ElfNode)findNodeByName("AllPane_corepane");
//		_&task1 = (ElfNode)findNodeByName("&task1");
//		_&task1_tipnode1 = (ElfNode)findNodeByName("&task1_tipnode1");
//		_&task1_tipnode1_ZheZhao = (ElfNode)findNodeByName("&task1_tipnode1_ZheZhao");
//		_&task1_tipnode1_ZheZhao_Quan = (ElfNode)findNodeByName("&task1_tipnode1_ZheZhao_Quan");
//		_&task1_tipnode1_JianTou = (ElfNode)findNodeByName("&task1_tipnode1_JianTou");
//		_&task1_tipnode1_JianTou_JianTou = (ElfNode)findNodeByName("&task1_tipnode1_JianTou_JianTou");
//		_&task1_tipnode2 = (ElfNode)findNodeByName("&task1_tipnode2");
//		_&task1_tipnode2_ZheZhao = (ElfNode)findNodeByName("&task1_tipnode2_ZheZhao");
//		_&task1_tipnode2_ZheZhao_Quan = (ElfNode)findNodeByName("&task1_tipnode2_ZheZhao_Quan");
//		_&task1_tipnode2_JianTou = (ElfNode)findNodeByName("&task1_tipnode2_JianTou");
//		_&task1_tipnode2_JianTou_JianTou = (ElfNode)findNodeByName("&task1_tipnode2_JianTou_JianTou");
//		_&task1_tipnode3 = (ElfNode)findNodeByName("&task1_tipnode3");
//		_&task1_tipnode3_ZheZhao = (ElfNode)findNodeByName("&task1_tipnode3_ZheZhao");
//		_&task1_tipnode3_ZheZhao_Quan = (ElfNode)findNodeByName("&task1_tipnode3_ZheZhao_Quan");
//		_&task1_tipnode3_JianTou = (ElfNode)findNodeByName("&task1_tipnode3_JianTou");
//		_&task1_tipnode3_JianTou_JianTou = (ElfNode)findNodeByName("&task1_tipnode3_JianTou_JianTou");
//		_&task1_notepane = (ElfNode)findNodeByName("&task1_notepane");
//		_&task1_notepane_LiLiSi = (ElfNode)findNodeByName("&task1_notepane_LiLiSi");
//		_&task1_notepane_textpane = (ElfNode)findNodeByName("&task1_notepane_textpane");
//		_&task1_notepane_textpane_nextanimate = (SimpleAnimateNode)findNodeByName("&task1_notepane_textpane_nextanimate");
//		_&task1_notepane_textpane_text = (TextNode)findNodeByName("&task1_notepane_textpane_text");
//		_&task1_notepane_touch = (ButtonNode)findNodeByName("&task1_notepane_touch");
		// @@auto-code-end
	}
	
	// @@auto-code-start defines
	protected ElfNode _AllPane;
	protected ElfNode _AllPane_mainpane;
	protected ElfNode _AllPane_mainpane_hero1;
	protected ElfNode _AllPane_mainpane_hero1_icon;
	protected ElfNode _AllPane_mainpane_hero1_heropic_pos;
	protected TextNode _AllPane_mainpane_hero1_siwei_tili;
	protected TextNode _AllPane_mainpane_hero1_siwei_liliang;
	protected TextNode _AllPane_mainpane_hero1_siwei_zhili;
	protected TextNode _AllPane_mainpane_hero1_siwei_mingjie;
	protected BarNode _AllPane_mainpane_hero1_siwei_tilibar;
	protected BarNode _AllPane_mainpane_hero1_siwei_liliangbar;
	protected BarNode _AllPane_mainpane_hero1_siwei_zhilibar;
	protected BarNode _AllPane_mainpane_hero1_siwei_mingjiebar;
	protected TextNode _AllPane_mainpane_hero1_name;
	protected ButtonNode _AllPane_mainpane_hero1_zhaomubutton;
	protected ButtonNode _AllPane_mainpane_hero1_checkbutton;
	protected LayoutNode _AllPane_mainpane_hero1_starlayout;
	protected LayoutNode _AllPane_mainpane_hero1_unstarlayout;
	protected ElfNode _AllPane_mainpane_hero1_jinbiprice;
	protected TextNode _AllPane_mainpane_hero1_jinbiprice_text;
	protected ElfNode _AllPane_mainpane_hero1_baoshiprice;
	protected TextNode _AllPane_mainpane_hero1_baoshiprice_text;
	protected ElfNode _AllPane_mainpane_hero2;
	protected ElfNode _AllPane_mainpane_hero2_icon;
	protected ElfNode _AllPane_mainpane_hero2_heropic_pos;
	protected TextNode _AllPane_mainpane_hero2_siwei_tili;
	protected TextNode _AllPane_mainpane_hero2_siwei_liliang;
	protected TextNode _AllPane_mainpane_hero2_siwei_zhili;
	protected TextNode _AllPane_mainpane_hero2_siwei_mingjie;
	protected BarNode _AllPane_mainpane_hero2_siwei_tilibar;
	protected BarNode _AllPane_mainpane_hero2_siwei_liliangbar;
	protected BarNode _AllPane_mainpane_hero2_siwei_zhilibar;
	protected BarNode _AllPane_mainpane_hero2_siwei_mingjiebar;
	protected TextNode _AllPane_mainpane_hero2_name;
	protected ButtonNode _AllPane_mainpane_hero2_zhaomubutton;
	protected ButtonNode _AllPane_mainpane_hero2_checkbutton;
	protected LayoutNode _AllPane_mainpane_hero2_starlayout;
	protected LayoutNode _AllPane_mainpane_hero2_unstarlayout;
	protected ElfNode _AllPane_mainpane_hero2_jinbiprice;
	protected TextNode _AllPane_mainpane_hero2_jinbiprice_text;
	protected ElfNode _AllPane_mainpane_hero2_baoshiprice;
	protected TextNode _AllPane_mainpane_hero2_baoshiprice_text;
	protected ElfNode _AllPane_mainpane_hero3;
	protected ElfNode _AllPane_mainpane_hero3_icon;
	protected ElfNode _AllPane_mainpane_hero3_heropic_pos;
	protected TextNode _AllPane_mainpane_hero3_siwei_tili;
	protected TextNode _AllPane_mainpane_hero3_siwei_liliang;
	protected TextNode _AllPane_mainpane_hero3_siwei_zhili;
	protected TextNode _AllPane_mainpane_hero3_siwei_mingjie;
	protected BarNode _AllPane_mainpane_hero3_siwei_tilibar;
	protected BarNode _AllPane_mainpane_hero3_siwei_liliangbar;
	protected BarNode _AllPane_mainpane_hero3_siwei_zhilibar;
	protected BarNode _AllPane_mainpane_hero3_siwei_mingjiebar;
	protected TextNode _AllPane_mainpane_hero3_name;
	protected ButtonNode _AllPane_mainpane_hero3_zhaomubutton;
	protected ButtonNode _AllPane_mainpane_hero3_checkbutton;
	protected LayoutNode _AllPane_mainpane_hero3_starlayout;
	protected LayoutNode _AllPane_mainpane_hero3_unstarlayout;
	protected ElfNode _AllPane_mainpane_hero3_jinbiprice;
	protected TextNode _AllPane_mainpane_hero3_jinbiprice_text;
	protected ElfNode _AllPane_mainpane_hero3_baoshiprice;
	protected TextNode _AllPane_mainpane_hero3_baoshiprice_text;
	protected ElfNode _AllPane_mainpane_emphero1;
	protected ElfNode _AllPane_mainpane_emphero2;
	protected ElfNode _AllPane_mainpane_emphero3;
	protected ButtonNode _AllPane_mainpane_shuaxinbutton;
	protected TimeNode _AllPane_mainpane_shuaxincount_time;
	protected ButtonNode _AllPane_bg_Exit;
	protected ElfNode _AllPane_darkbg;
	protected ElfNode _AllPane_herodetailpane;
	protected ButtonNode _AllPane_herodetailpane_Exit;
	protected ElfNode _AllPane_herodetailpane_pane_shuxing;
	protected BarNode _AllPane_herodetailpane_pane_shuxing_show_ti;
	protected BarNode _AllPane_herodetailpane_pane_shuxing_show_li;
	protected BarNode _AllPane_herodetailpane_pane_shuxing_show_zhi;
	protected BarNode _AllPane_herodetailpane_pane_shuxing_show_ming;
	protected LayoutNode _AllPane_herodetailpane_pane_shuxing_num;
	protected LayoutNode _AllPane_herodetailpane_pane_shuxing_incnum;
	protected LayoutNode _AllPane_herodetailpane_pane_dshuxing;
	protected ElfNode _AllPane_herodetailpane_pane_dshuxing_s1_hp;
	protected TextNode _AllPane_herodetailpane_pane_dshuxing_s1_hp_num;
	protected ElfNode _AllPane_herodetailpane_pane_dshuxing_s1_phyattack;
	protected TextNode _AllPane_herodetailpane_pane_dshuxing_s1_phyattack_num;
	protected ElfNode _AllPane_herodetailpane_pane_dshuxing_s1_magicattack;
	protected TextNode _AllPane_herodetailpane_pane_dshuxing_s1_magicattack_num;
	protected ElfNode _AllPane_herodetailpane_pane_dshuxing_s1_criticalratio;
	protected TextNode _AllPane_herodetailpane_pane_dshuxing_s1_criticalratio_num;
	protected ElfNode _AllPane_herodetailpane_pane_dshuxing_s1_criticalnum;
	protected TextNode _AllPane_herodetailpane_pane_dshuxing_s1_criticalnum_num;
	protected ElfNode _AllPane_herodetailpane_pane_dshuxing_s2_speed;
	protected TextNode _AllPane_herodetailpane_pane_dshuxing_s2_speed_num;
	protected ElfNode _AllPane_herodetailpane_pane_dshuxing_s2_phydef;
	protected TextNode _AllPane_herodetailpane_pane_dshuxing_s2_phydef_num;
	protected ElfNode _AllPane_herodetailpane_pane_dshuxing_s2_magicdef;
	protected TextNode _AllPane_herodetailpane_pane_dshuxing_s2_magicdef_num;
	protected ElfNode _AllPane_herodetailpane_pane_dshuxing_s2_missratio;
	protected TextNode _AllPane_herodetailpane_pane_dshuxing_s2_missratio_num;
	protected ElfNode _AllPane_herodetailpane_pane_dshuxing_s2_tough;
	protected TextNode _AllPane_herodetailpane_pane_dshuxing_s2_tough_num;
	protected ElfNode _AllPane_herodetailpane_pane_dshuxing_s3_hitadd;
	protected TextNode _AllPane_herodetailpane_pane_dshuxing_s3_hitadd_num;
	protected ElfNode _AllPane_herodetailpane_pane_dshuxing_s3_phybre;
	protected TextNode _AllPane_herodetailpane_pane_dshuxing_s3_phybre_num;
	protected ElfNode _AllPane_herodetailpane_pane_dshuxing_s3_magicbre;
	protected TextNode _AllPane_herodetailpane_pane_dshuxing_s3_magicbre_num;
	protected ElfNode _AllPane_herodetailpane_pane_best_bestpos;
	protected BarNode _AllPane_herodetailpane_pane_intro_heroname_bar;
	protected TextNode _AllPane_herodetailpane_pane_intro_heroname_text;
	protected ElfNode _AllPane_herodetailpane_pane_intro_heropic;
	protected ElfNode _AllPane_choosepane;
	protected ButtonNode _AllPane_choosepane_back;
	protected ButtonNode _AllPane_choosepane_enter;
	protected ElfNode _AllPane_choosepane_choosetab;
	protected TabNode _AllPane_choosepane_choosetab_choose1;
	protected TabNode _AllPane_choosepane_choosetab_choose2;
	protected TabNode _AllPane_choosepane_choosetab_choose3;
	protected ElfNode _AllPane_ensurepane;
	protected ButtonNode _AllPane_ensurepane_back;
	protected ButtonNode _AllPane_ensurepane_ensure;
	protected TextNode _AllPane_ensurepane_text;
	protected ElfNode _AllPane_corepane;
//	protected ElfNode _&task1;
//	protected ElfNode _&task1_tipnode1;
//	protected ElfNode _&task1_tipnode1_ZheZhao;
//	protected ElfNode _&task1_tipnode1_ZheZhao_Quan;
//	protected ElfNode _&task1_tipnode1_JianTou;
//	protected ElfNode _&task1_tipnode1_JianTou_JianTou;
//	protected ElfNode _&task1_tipnode2;
//	protected ElfNode _&task1_tipnode2_ZheZhao;
//	protected ElfNode _&task1_tipnode2_ZheZhao_Quan;
//	protected ElfNode _&task1_tipnode2_JianTou;
//	protected ElfNode _&task1_tipnode2_JianTou_JianTou;
//	protected ElfNode _&task1_tipnode3;
//	protected ElfNode _&task1_tipnode3_ZheZhao;
//	protected ElfNode _&task1_tipnode3_ZheZhao_Quan;
//	protected ElfNode _&task1_tipnode3_JianTou;
//	protected ElfNode _&task1_tipnode3_JianTou_JianTou;
//	protected ElfNode _&task1_notepane;
//	protected ElfNode _&task1_notepane_LiLiSi;
//	protected ElfNode _&task1_notepane_textpane;
//	protected SimpleAnimateNode _&task1_notepane_textpane_nextanimate;
//	protected TextNode _&task1_notepane_textpane_text;
//	protected ButtonNode _&task1_notepane_touch;
	// @@auto-code-end
	
} 
