package com.ielfgame.stupidGame.design.hotSwap;

import com.ielfgame.stupidGame.design.Surface.Surface;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeList.ListNode;
import elfEngine.basic.node.nodeText.TextNode;
import elfEngine.basic.node.nodeTouch.ButtonNode;

public class SurfaceYouJian extends Surface{ 
	
	public SurfaceYouJian() { 
		super("D:\\pic\\elf-xml\\邮件.xml"); 
	} 
	
	//邮件 ->title, 
	
	//
	
	//
	
	
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
		_AllPane_bg_bg_tablist = (ListNode)findNodeByName("AllPane_bg_bg_tablist");
		_AllPane_bg_bg_itemlist = (ListNode)findNodeByName("AllPane_bg_bg_itemlist");
		_AllPane_bg_bg_itemlist_container_MailBar = (ElfNode)findNodeByName("AllPane_bg_bg_itemlist_container_MailBar");
		_AllPane_bg_bg_itemlist_container_MailBar_mailbutton = (ButtonNode)findNodeByName("AllPane_bg_bg_itemlist_container_MailBar_mailbutton");
		_AllPane_bg_bg_itemlist_container_MailBar_mailicon = (ElfNode)findNodeByName("AllPane_bg_bg_itemlist_container_MailBar_mailicon");
		_AllPane_bg_bg_itemlist_container_MailBar_mailname = (TextNode)findNodeByName("AllPane_bg_bg_itemlist_container_MailBar_mailname");
		_AllPane_bg_bg_itemlist_container_MailBar_mailtime = (TextNode)findNodeByName("AllPane_bg_bg_itemlist_container_MailBar_mailtime");
		_AllPane_bg_bg_itemlist_container_MailBar_mailstate = (TextNode)findNodeByName("AllPane_bg_bg_itemlist_container_MailBar_mailstate");
		_AllPane_bg_bg_itemlist_container_MailBar_maildeletebutton = (ButtonNode)findNodeByName("AllPane_bg_bg_itemlist_container_MailBar_maildeletebutton");
		_AllPane_bg_bg_itemlist_container_MailBar_maildeletebutton_Shan = (TextNode)findNodeByName("AllPane_bg_bg_itemlist_container_MailBar_maildeletebutton_Shan");
		_AllPane_bg_bg_itemlist_container_MailBarDark = (ElfNode)findNodeByName("AllPane_bg_bg_itemlist_container_MailBarDark");
		_AllPane_bg_bg_itemlist_container_MailBarDark_mailbutton = (ButtonNode)findNodeByName("AllPane_bg_bg_itemlist_container_MailBarDark_mailbutton");
		_AllPane_bg_bg_itemlist_container_MailBarDark_mailicon = (ElfNode)findNodeByName("AllPane_bg_bg_itemlist_container_MailBarDark_mailicon");
		_AllPane_bg_bg_itemlist_container_MailBarDark_mailname = (TextNode)findNodeByName("AllPane_bg_bg_itemlist_container_MailBarDark_mailname");
		_AllPane_bg_bg_itemlist_container_MailBarDark_mailtime = (TextNode)findNodeByName("AllPane_bg_bg_itemlist_container_MailBarDark_mailtime");
		_AllPane_bg_bg_itemlist_container_MailBarDark_mailstate = (TextNode)findNodeByName("AllPane_bg_bg_itemlist_container_MailBarDark_mailstate");
		_AllPane_bg_bg_itemlist_container_MailBarDark_maildeletebutton = (ButtonNode)findNodeByName("AllPane_bg_bg_itemlist_container_MailBarDark_maildeletebutton");
		_AllPane_bg_bg_itemlist_container_MailBarDark_maildeletebutton_Shan = (TextNode)findNodeByName("AllPane_bg_bg_itemlist_container_MailBarDark_maildeletebutton_Shan");
		_AllPane_bg_bg_sendmailbutton = (ButtonNode)findNodeByName("AllPane_bg_bg_sendmailbutton");
		_AllPane_bg_bg_clearmail = (ButtonNode)findNodeByName("AllPane_bg_bg_clearmail");
		_AllPane_readmailpane = (ElfNode)findNodeByName("AllPane_readmailpane");
		_AllPane_readmailpane_resendbutton = (ButtonNode)findNodeByName("AllPane_readmailpane_resendbutton");
		_AllPane_readmailpane_back = (ButtonNode)findNodeByName("AllPane_readmailpane_back");
		_AllPane_readmailpane_title = (TextNode)findNodeByName("AllPane_readmailpane_title");
		_AllPane_sendmailpane = (ElfNode)findNodeByName("AllPane_sendmailpane");
		_AllPane_bg_Exit = (ButtonNode)findNodeByName("AllPane_bg_Exit");
		// @@auto-code-end
	}
	
	// @@auto-code-start defines
	protected ElfNode _AllPane;
	protected ListNode _AllPane_bg_bg_tablist;
	protected ListNode _AllPane_bg_bg_itemlist;
	protected ElfNode _AllPane_bg_bg_itemlist_container_MailBar;
	protected ButtonNode _AllPane_bg_bg_itemlist_container_MailBar_mailbutton;
	protected ElfNode _AllPane_bg_bg_itemlist_container_MailBar_mailicon;
	protected TextNode _AllPane_bg_bg_itemlist_container_MailBar_mailname;
	protected TextNode _AllPane_bg_bg_itemlist_container_MailBar_mailtime;
	protected TextNode _AllPane_bg_bg_itemlist_container_MailBar_mailstate;
	protected ButtonNode _AllPane_bg_bg_itemlist_container_MailBar_maildeletebutton;
	protected TextNode _AllPane_bg_bg_itemlist_container_MailBar_maildeletebutton_Shan;
	protected ElfNode _AllPane_bg_bg_itemlist_container_MailBarDark;
	protected ButtonNode _AllPane_bg_bg_itemlist_container_MailBarDark_mailbutton;
	protected ElfNode _AllPane_bg_bg_itemlist_container_MailBarDark_mailicon;
	protected TextNode _AllPane_bg_bg_itemlist_container_MailBarDark_mailname;
	protected TextNode _AllPane_bg_bg_itemlist_container_MailBarDark_mailtime;
	protected TextNode _AllPane_bg_bg_itemlist_container_MailBarDark_mailstate;
	protected ButtonNode _AllPane_bg_bg_itemlist_container_MailBarDark_maildeletebutton;
	protected TextNode _AllPane_bg_bg_itemlist_container_MailBarDark_maildeletebutton_Shan;
	protected ButtonNode _AllPane_bg_bg_sendmailbutton;
	protected ButtonNode _AllPane_bg_bg_clearmail;
	protected ElfNode _AllPane_readmailpane;
	protected ButtonNode _AllPane_readmailpane_resendbutton;
	protected ButtonNode _AllPane_readmailpane_back;
	protected TextNode _AllPane_readmailpane_title;
	protected ElfNode _AllPane_sendmailpane;
	protected ButtonNode _AllPane_bg_Exit;
	// @@auto-code-end
	
} 
