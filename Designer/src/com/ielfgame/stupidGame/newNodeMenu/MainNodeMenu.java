package com.ielfgame.stupidGame.newNodeMenu;

import java.util.ArrayList;

import org.eclipse.swt.events.SelectionEvent;

import com.ielfgame.stupidGame.NodeView.NodeViewWorkSpaceTab;
import com.ielfgame.stupidGame.config.MenuConfig;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.data.ElfDataXML;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.data.ElfPointi;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashStruct.IFlashMain;
import com.ielfgame.stupidGame.dialog.MultiLineDialog;
import com.ielfgame.stupidGame.dialog.PopDialog.PopType;
import com.ielfgame.stupidGame.platform.PlatformHelper;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.undo.UndoRedoManager;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeAnimate.timeLine.ElfMotionNode;

public class MainNodeMenu extends AbstractMenu{ 
	
//	private final class StringRepace extends ElfDataXML{
//		public String Old_Folder_REF_DIR;
//		public String New_Folder_REF_DIR;
//		public boolean ConvertDecollator;
//	}
	
	private final NodeViewWorkSpaceTab mNodeViewWorkSpaceTab; 
//	private final StringRepace mStringRepace = new StringRepace();
	
	public MainNodeMenu() { 
		super(null); 
		
		mNodeViewWorkSpaceTab = PowerMan.getSingleton(NodeViewWorkSpaceTab.class); 
		
		this.checkInMenuItem(new AbstractMenuItem("Set As Scene") { 
			public void onClick(SelectionEvent e) { 
				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode(); 
				mNodeViewWorkSpaceTab.bindScreen(selectNode); 
			} 
			public boolean isShow() { 
				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode(); 
				final ElfNode bindNode = PowerMan.getSingleton(DataModel.class).getScreenNode().getBindNode(); 
				return selectNode != null && selectNode != bindNode && false; 
			} 
		}); 
		
		this.checkInMenuItem(new AbstractMenuItem("绑定 Flash") { 
			public void onClick(SelectionEvent e) { 
				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode(); 
				if(selectNode instanceof IFlashMain) { 
					mNodeViewWorkSpaceTab.bindFlash((IFlashMain)selectNode);
				} 
			} 
			public boolean isShow() { 
				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode(); 
				if(selectNode instanceof IFlashMain) { 
					return true;
				}
				return false;
			} 
		}); 
		
		this.checkInMenuItem(new AbstractMenuItem("Bind Motion") { 
			public void onClick(SelectionEvent e) { 
				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode(); 
				if(selectNode instanceof ElfMotionNode) {
					mNodeViewWorkSpaceTab.bindMotion((ElfMotionNode)selectNode); 
				}
			} 
			public boolean isShow() { 
				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode(); 
				if(selectNode != null) { 
					if(selectNode instanceof ElfMotionNode) {
						return true;
					}
				}
				return false;
			} 
		}); 
		
		
		this.checkInMenuItem(null);
		this.checkInMenuItem(new NewNodeMenu()); 
		this.checkInMenuItem(null); 
		this.checkInMenuItem(new ConvertNodeMenu()); 
		this.checkInMenuItem(null); 
		
//		this.checkInMenuItem(new AbstractMenuItem("Replace Res Folder") {
//			public void onClick(SelectionEvent e) { 
//				final StringRepace sr = mStringRepace;
//				final MultiLineDialog dialog = new MultiLineDialog();
//				final Object [] objs = dialog.open(sr);
//				if(objs != null) {
//					sr.setValues(objs);
//					if(sr.Old_Folder_REF_DIR != null && sr.New_Folder_REF_DIR != null) {
//						final List<ElfNode> selectlist = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
//						for(final ElfNode selectnode : selectlist) {
//							selectnode.iterateChildsDeepWithSelf(new IIterateChilds() {
//								public boolean iterate(ElfNode node) {
//									final String [] ids = node.getSelfResids();
//									if(ids != null) {
//										for(int i=0; i<ids.length; i++) {
//											final String id = ids[i];
//											if(id != null) {
//												if(sr.ConvertDecollator) {
//													ids[i] = FileHelper.replaceDecollators(ids[i]);
//													final String oldStr = FileHelper.replaceDecollators(sr.Old_Folder_REF_DIR);
//													final String newStr = FileHelper.replaceDecollators(sr.New_Folder_REF_DIR);
//													ids[i] = ids[i].replace(oldStr, newStr);
//												} else {
//													ids[i] = id.replace(sr.Old_Folder_REF_DIR, sr.New_Folder_REF_DIR);
//												}
//											}
//										}
//										
//										node.setSelfResids(ids);
//									}
//									return false;
//								} 
//							});
//						} 
//					} 
//				} 
//			} 
//			public boolean isShow() { 
//				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode();
//				return selectNode != null && !mNodeViewWorkSpaceTab.isScreenOrRecycleNode(selectNode);
//			} 
//		}); 
		
		this.checkInMenuItem(new AbstractMenuItem("Rename\tF2") {
			public void onClick(SelectionEvent e) { 
				mNodeViewWorkSpaceTab.entry(PopType.RENAME);
			} 
			public boolean isShow() { 
				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode();
				return selectNode != null && !mNodeViewWorkSpaceTab.isScreenOrRecycleNode(selectNode);
			} 
		}); 
		
		this.checkInMenuItem(new AbstractMenuItem("Delete") {
			public void onClick(SelectionEvent e) {
				UndoRedoManager.checkInUndo();
				
				final ArrayList<ElfNode> nodes = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
				for(ElfNode node: nodes) {
					if(!mNodeViewWorkSpaceTab.isScreenOrRecycleNode(node)) {
						mNodeViewWorkSpaceTab.removeNode(node); 
						if((e.stateMask & PlatformHelper.CTRL) == 0) {
							final ElfNode recycleNode = mNodeViewWorkSpaceTab.getRecycleNode();
							if(!recycleNode.isRecurFatherOf(node)) {
								mNodeViewWorkSpaceTab.addNode(recycleNode, node, Integer.MAX_VALUE, false);
							} 
						}
					} 
				} 
			} 
			public boolean isShow() { 
				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode();
				return selectNode != null && !mNodeViewWorkSpaceTab.isScreenOrRecycleNode(selectNode);
			} 
		});
		
		this.checkInMenuItem(null);
		
		this.checkInMenuItem(new AbstractMenuItem("Auto Name") {
			public void onClick(SelectionEvent e) {
				final MenuConfig menuConfig = PowerMan.getSingleton(MenuConfig.class);
				menuConfig.AutoNameWhenCopy = getMenuItem().getSelection();
			}
			public boolean isCheckItem() {
				return true;
			}
			public boolean initCheck() {
				final MenuConfig menuConfig = PowerMan.getSingleton(MenuConfig.class);
				return menuConfig.AutoNameWhenCopy;
			}
		});
		
		this.checkInMenuItem(new AbstractMenuItem("Copy\tF3") {
			public void onClick(SelectionEvent e) {
				mNodeViewWorkSpaceTab.entry(PopType.COPY);
			} 
			public boolean isShow() { 
				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode();
				return selectNode != null && !mNodeViewWorkSpaceTab.isScreenOrRecycleNode(selectNode);
			} 
		});
		
		this.checkInMenuItem(new AbstractMenuItem("Deep Copy\tF4") {
			public void onClick(SelectionEvent e) {
				mNodeViewWorkSpaceTab.entry(PopType.COPY_DEEP);
			} 
			public boolean isShow() { 
				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode();
				return selectNode != null && !mNodeViewWorkSpaceTab.isScreenOrRecycleNode(selectNode);
			} 
		});
		
		this.checkInMenuItem(null);
		
//		this.checkInMenuItem(new AbstractMenuItem("Refresh Node\tF5") {
//			public void onClick(SelectionEvent e) {
//				final ArrayList<ElfNode> nodes = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
//				for(final ElfNode node: nodes) {
//					PowerMan.getSingleton(DataModel.class).getScreenNode().runWithDelay(new Runnable() {
//						public void run() {
//							node.myRefresh(); 
//						} 
//					}, 0); 
//				} 
//			} 
//			public boolean isShow() { 
//				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode();
//				return selectNode != null ;
//			} 
//		}); 
		
		this.checkInMenuItem(new AbstractMenuItem("Refresh Node\tF5") {
			public void onClick(SelectionEvent e) {
				final ArrayList<ElfNode> nodes = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
				for(final ElfNode node: nodes) {
					PowerMan.getSingleton(DataModel.class).getScreenNode().runWithDelay(new Runnable() {
						public void run() {
							node.refresh();
						} 
					}, 0); 
				} 
			} 
			public boolean isShow() { 
				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode();
				return selectNode != null ;
			} 
		});
		
		this.checkInMenuItem(null);
		
		this.checkInMenuItem(new AbstractMenuItem("平移") {
			public void onClick(SelectionEvent e) {
				final ElfPointf pos = new ElfPointf();
				
				final MultiLineDialog mld = new MultiLineDialog();
				final Object[] ret = mld.open(pos);
				if (ret != null) {
					pos.setValues(ret);
					
					UndoRedoManager.checkInUndo();
					
					final ArrayList<ElfNode> nodes = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
					for(final ElfNode node: nodes) {
						node.translate(pos.x, pos.y);
					} 
				}
				
				
			} 
			public boolean isShow() { 
				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode();
				return selectNode != null ;
			} 
		});
		
		this.checkInMenuItem(null);
		
//		this.checkInMenuItem(new AbstractMenuItem("Create Lua Res File") { 
//			public void onClick(final SelectionEvent e) {
//				//1.getResids 
//				final ElfNode node = PowerMan.getSingleton(DataModel.class).getScreenNode();
//				final Set<String> resids = new HashSet<String>(); 
//				resids.addAll(node.getLegalResids(true)); 
//				node.iterateChildsDeep(new IIterateChilds() {
//					public boolean iterate(final ElfNode child) {
//						resids.addAll(child.getLegalResids(true)); 
//						return false;
//					} 
//				});
//				
//				String [] array = new String[resids.size()];
//				resids.toArray(array); 
//				
//				final HashMap<String, Long> map = new HashMap<String, Long>();
//				
//				//2.read plist 
//				final String source = PowerMan.getSingleton(DataModel.class).getTransferDir();
//				final ResLocationScaner plistScaner = new ResLocationScaner(source);
//				final HashMap<String, Set<String>> plistToIdMap = new HashMap<String, Set<String>>();
//				final HashMap<String, String> idToPlistMap = new HashMap<String, String>();
//				final HashMap<String, String> plistToPvr = new HashMap<String, String>();
//				
//				plistScaner.scanPlist(plistToIdMap, idToPlistMap, plistToPvr, false); 
//				
//				final HashSet<String> resSet = new HashSet<String>();
//				//3.update
//				for(int i=0; i<array.length; i++) {
//					final String p = idToPlistMap.get(array[i]);
//					if(p != null) {
//						array[i] = plistToPvr.get(p); 
//					} 
//					resSet.add(array[i]);
//				} 
//				//4.sort
//				array = new String[resSet.size()];
//				resSet.toArray(array); 
//				
//				final String dir = PowerMan.getSingleton(DataModel.class).getTransferDir();
//				Arrays.sort(array, new  Comparator<String>(){
//					public int compare(String arg0, String arg1) {
//						
//						long size0 = 0;
//						if(map.containsKey(arg0)) {
//							size0 = map.get(arg0);
//						} else {
//							if(plistToPvr.containsValue( arg0 )) {
//								final String path = dir + FileHelper.DECOLLATOR + FileHelper.replaceDecollators(arg0);
//								final File f = new File(path);
//								if(f.exists()) {
//									size0 = f.length();
//								} 
//							} else {
//								final String realPath = PlatformHelper.toCurrentPath(arg0);
//								size0 = new File(realPath).length();
//							}
//							
//							map.put(arg0, size0);
//						}
//						
//						long size1 = 0;
//						if(map.containsKey(arg1)) {
//							size1 = map.get(arg1);
//						} else {
//							if(plistToPvr.containsValue( arg1 )) {
//								final String path = dir + FileHelper.DECOLLATOR + FileHelper.replaceDecollators(arg1);
//								final File f = new File(path);
//								if(f.exists()) {
//									size1 = f.length();
//								} 
//							} else {
//								final String realPath = PlatformHelper.toCurrentPath(arg1);
//								size1 = new File(realPath).length();
//							}
//							
//							map.put(arg1, size1);
//						}
//						
//						return size0<size1 ? 1:-1;
//					}
//				});
//				
//				//convert
//				final String path = PowerMan.getSingleton(DataModel.class).getLastImportPath();
//				final String simpleName = FileHelper.getSimpleNameNoSuffix(path);
//				final String luaPath = PowerMan.getSingleton(DataModel.class).getTransferDir()+FileHelper.DECOLLATOR+ simpleName+"_res.lua";
//				final String [] comments = new String[array.length];
//				
//				for(int i=0; i<array.length; i++) {
//					comments[i] = "-- size="+map.get(array[i]);
//				}
//				
//				final boolean ret = ConvertXMLToRes.convertToLuaRes(luaPath, array, comments);
//				
//				new MessageDialog().open(ResJudge.getResourceString("dialog.About.title"),
//						String.format("Create %s %s!", luaPath, ret?"succeeded":"failed"));
//			} 
//		});
//		
//		this.checkInMenuItem(new AbstractMenuItem("Create Res T.lua File") { 
//			public void onClick(final SelectionEvent e) {
//				final String luaPath = PowerMan.getSingleton(DataModel.class).getTransferDir()+FileHelper.DECOLLATOR+ "T.lua";
//				final boolean ret = ConvertXMLToRes.convertToAllLuaRes(luaPath);
//				new MessageDialog().open(ResJudge.getResourceString("dialog.About.title"),
//						String.format("Create %s %s!", luaPath, ret?"succeeded":"failed"));
//			} 
//		});
//		
//		this.checkInMenuItem(new AbstractMenuItem("Create Res T.h File") { 
//			public void onClick(final SelectionEvent e) { 
//				final String hPath = PowerMan.getSingleton(ProjectSetting.class).XCode_Resource_REF_DIR+FileHelper.DECOLLATOR+ "T.h";
//				
//				
//				final boolean ret = ConvertXMLToRes.convertToHRes(hPath);
//				
//				
//				new MessageDialog().open(ResJudge.getResourceString("dialog.About.title"),
//						String.format("Create %s %s!", hPath, ret?"succeeded":"failed"));
//			} 
//		});
//		
//		this.checkInMenuItem(null);
		this.checkInMenuItem(new ViewMenu());
//		this.checkInMenuItem(null);
//		
//		this.checkInMenuItem(new AbstractMenuItem("Transfer Res") {
//			public void onClick(SelectionEvent e) {
//				final ArrayList<ElfNode> nodes = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
//				final Set<String> resids = new HashSet<String>(); 
//				for(final ElfNode node: nodes) { 
//					resids.addAll( node.getLegalResids(false) );
//					node.iterateChildsDeep(new IIterateChilds() { 
//						public boolean iterate(final ElfNode child) { 
//							resids.addAll( child.getLegalResids(false) );
//							return false;
//						} 
//					});
//				} 
//				
//				final CopyPanel cp = new CopyPanel();
//				cp.open("Nodes",resids);
//			} 
//			public boolean isShow() { 
//				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode();
//				return selectNode != null ;
//			} 
//		}); 
		
//		this.checkInMenuItem(new AbstractMenuItem("Pack Texture Deep") {
//			public void onClick(SelectionEvent e) {
//				texturePacker();
//			} 
//			
//			public boolean isShow() { 
//				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode();
//				return selectNode != null;
//			} 
//		}); 
		
		this.checkInMenuItem(null); 
		
		this.checkInMenuItem(new CommentMenu());
		
		this.checkInMenuItem(null);
		
		this.checkInMenuItem(new SelectMenu());
		
		this.checkInMenuItem(null); 
		
		this.checkInMenuItem(new AbstractMenuItem("Print Node") {
			public void onClick(SelectionEvent e) {
				final ArrayList<ElfNode> list = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
				class Dir extends ElfDataXML{
					public ElfPointi grid = new ElfPointi(1,1);
					public float scale = 1;
					public String path_REF_DIR;
				}; 
				class File extends ElfDataXML{
					public ElfPointi grid = new ElfPointi(1,1);
					public float scale = 1;
					public String path_REF_FILE_png_jpg;
				}; 
				
				final String path;
				final int xNums;
				final int yNums;
				final float scale;
				if(list.size() == 1) {
					final File f = new File();
					MultiLineDialog dialog = new MultiLineDialog();
					final Object[] ret = dialog.open(f);
					if(ret != null) {
						f.setValues(ret);
					}
					path = f.path_REF_FILE_png_jpg;
					xNums = f.grid.x; 
					yNums = f.grid.y; 
					scale = f.scale;
					
				} else {
					final Dir dir = new Dir();
					MultiLineDialog dialog = new MultiLineDialog();
					final Object[] ret = dialog.open(dir);
					if(ret != null) {
						dir.setValues(ret);
					}
					path = dir.path_REF_DIR; 
					xNums = dir.grid.x; 
					yNums = dir.grid.y; 
					scale = dir.scale;
				} 
				
				PowerMan.getSingleton(DataModel.class).getScreenNode().runWithDelay(new Runnable() {
					public void run() { 
						if(path != null && path.length() > 0 && !path.equals("null")) {
							for(final ElfNode node : list) {
								if(list.size() == 1) {
									node.writeToPNG(path,xNums,yNums,scale); 
								} else {
									node.writeToPNG(path+"\\"+node.getName(), xNums,yNums,scale); 
								}
							} 
						} 
					} 
				}, 0); 
			}
		}); 
		
		this.checkInMenuItem(null);
		
		this.checkInMenuItem(new SearchMenu());
		
		this.checkInMenuItem(null);
	} 
	
	
//	static SetTexturePackerPanelRet sDefaultTexturePacker = new SetTexturePackerPanelRet();
//	
//	public static boolean texturepacker(SetTexturePackerPanelRet ret, final Set<String> resids, final boolean showOut, final boolean showErr,
//			final String outputDir, final String plistDir) {
//		if(ret == null) {
//			final SetTexturePackerPanel setTexturePackerPanel = new SetTexturePackerPanel();
//			ret = setTexturePackerPanel.open(sDefaultTexturePacker, PowerMan.getSingleton(MainDesigner.class).mShell);
//			sDefaultTexturePacker.copyFrom(ret);
//		} 
//		System.err.println(ret.toString()); 
//		
//		final String [] array = new String[resids.size()];
//		resids.toArray(array); 
//		
//		Arrays.sort(array, new Comparator<String>() { 
//			public int compare(String arg0, String arg1) { 
//				return arg0.compareTo(arg1);
//			} 
//		}); 
//		
//		final ArrayList<String> residArray = new ArrayList<String>();
//		for(int i=0; i<array.length; i++) { 
//			residArray.add( array[i] ); 
//		} 
//		
//		if(ret.name != null) { 
//			ret.name = SpellHelper.getUpEname(ret.name); 
//			
//			final int num = ret.single? array.length:ret.num; 
//			final TPHandle handle = TexturePackerHelper.getTPHandle(); 
//			handle.clear(); 
//			TexturePackerHelper.refreshCmd(); 
//			
//			final ArrayList<String> residArrayCopy = new ArrayList<String>(residArray); 
//			final String strRet = TexturePackerHelper.pack(outputDir, plistDir, ret.name, residArray, num, ret.imageFormat.toString(), ret.ditherFormat.toString(), ret.scale);
//			
//			if(strRet == null) { 
//				if(ret.autoRemove) {
//					String outputDir2 = outputDir;
//					if(outputDir2 == null) {
//						outputDir2 = PowerMan.getSingleton(DataModel.class).getTransferDir();
//					} 
//					
//					for(String s : residArrayCopy) { 
//						final String f_str = outputDir2 + FileHelper.DECOLLATOR + s;
//						new File(f_str).delete();
//					} 
//				} 
//				if(showOut) {
//					PowerMan.runOnUI(new Runnable() {
//						public void run() {
//							final AnalysisDialog<String> dialog = new AnalysisDialog<String>("TexturePacker Out",false);
//							String output = "";
//						
//							final ArrayList<String> list = handle.getOut();
//							for(String line : list) { 
//								output += line + "\n";
//							} 
//							dialog.open(output, String.class); 
//						}
//					});
//				}
//				
//			} else if(showErr) { 
//				PowerMan.runOnUI(new Runnable() {
//					public void run() {
//						final AnalysisDialog<String> dialog = new AnalysisDialog<String>("TexturePacker Err",false);
//						String output = strRet+"\n";
//						
//						final ArrayList<String> list = handle.getErr();
//						for(String line : list) { 
//							output += line + "\n"; 
//						} 
//						dialog.open(output, String.class); 
//					}
//				});
//				
//				return true;
//			} 
//		} 
//		
//		return false; 
//	} 
//	
//	public static void texturepacker(final Set<String> resids) { 
//		texturepacker(null, resids, true, true, null, null); 
//	} 

	@Override
	public void onClick(SelectionEvent e) {
	}
	
//	
//	public final static void texturePacker() { 
//		final ArrayList<ElfNode> nodes = PowerMan.getSingleton(DataModel.class).getSelectNodeList(); 
//		for(final ElfNode node: nodes) { 
//			final Set<String> resids = new HashSet<String>(); 
//			resids.addAll(node.getLegalResids(true)); 
//			
//			node.iterateChildsDeep(new IIterateChilds() { 
//				public boolean iterate(final ElfNode child) { 
//					resids.addAll(child.getLegalResids(true)); 
//					return false;
//				} 
//			}); 
//			sDefaultTexturePacker.name = node.getName();
//			texturepacker(resids); 
//		} 
//	} 
} 
