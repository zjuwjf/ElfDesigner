package com.ielfgame.stupidGame.newNodeMenu;

import org.eclipse.swt.events.SelectionEvent;

import com.ielfgame.stupidGame.NodeView.NodeViewWorkSpaceTab;
import com.ielfgame.stupidGame.batch.TpPlistScaner;
import com.ielfgame.stupidGame.config.CurrentPlist;
import com.ielfgame.stupidGame.data.SearchData;
import com.ielfgame.stupidGame.dialog.MessageDialog;
import com.ielfgame.stupidGame.dialog.MultiLineDialog;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.res.ResManager;
import com.ielfgame.stupidGame.trans.TransferRes;
import com.ielfgame.stupidGame.utils.FileHelper;
import com.ielfgame.stupidGame.utils.FileHelper.FileType;

import elfEngine.basic.node.ElfNode;

public class SearchMenu extends AbstractMenu {

	public SearchMenu() {
		super("Search");

		this.checkInMenuItem(new AbstractMenuItem("Search Nodes") {
			public void onClick(SelectionEvent e) {
				searchNodes();
			}
		});

		this.checkInMenuItem(new AbstractMenuItem("Search In Plist") {
			public void onClick(SelectionEvent e) {
				searchPlists();
			}
		});
	}

	@Override
	public void onClick(SelectionEvent e) {

	}

	private static String sSearchResidPathInPlist = "";

	public final static void searchPlists() {
		final String path = ResManager.getSingleton().getDesignerImageAsset();

		if (FileHelper.getFileType(path) == FileType.DIR) {
			final MultiLineDialog dialog = new MultiLineDialog();
			dialog.setTitle("Search Resid In Plists");
			final Object[] ret = dialog.open("" + sSearchResidPathInPlist);

			if (ret != null && ret.length > 0) {
				try {
					sSearchResidPathInPlist = "" + (String) ret[0];
					System.err.println("Search " + sSearchResidPathInPlist);
					final TpPlistScaner scaner = new TpPlistScaner(path);
					scaner.scanPlist(CurrentPlist.plistToIdMap, CurrentPlist.idToPlistMap, null, false);
					final String plist = CurrentPlist.idToPlistMap.get(TransferRes.exportCompressPath(sSearchResidPathInPlist, true));
					if (plist != null) {
						MessageDialog message = new MessageDialog();
						message.open("", String.format("Found %s In\n %s!", sSearchResidPathInPlist, path + FileHelper.DECOLLATOR + plist + ".plist"));
					} else {
						MessageDialog message = new MessageDialog();
						message.open("", String.format("Not Found %s In Any Plist!", sSearchResidPathInPlist, path + FileHelper.DECOLLATOR + plist + ".plist"));
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		} else {
			MessageDialog message = new MessageDialog();
			message.open("", String.format("Check Your Plist Location!"));
		}
	}

	private final static SearchData sSearchData = new SearchData();

	public final static void searchNodes() {
		final SearchData data = sSearchData;
		final MultiLineDialog dialog = new MultiLineDialog();
		final Object[] ret = dialog.open(data);
		if (ret != null) {
			try {
				data.setValues(ret);
				final ElfNode[] nodes = data.search();
				final NodeViewWorkSpaceTab view = PowerMan.getSingleton(NodeViewWorkSpaceTab.class);
				if (view != null) {
					view.setSelectNodes(nodes);
					for (ElfNode node : nodes) {
						NodeViewWorkSpaceTab.setExpand(node, true);
					}
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
