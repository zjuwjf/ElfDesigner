package com.ielfgame.stupidGame.newNodeMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.events.SelectionEvent;

import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.dialog.AnalysisDialog;
import com.ielfgame.stupidGame.dialog.CollectDialog;
import com.ielfgame.stupidGame.dialog.MessageDialog;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.ElfNode.IIterateChilds;

public class ViewMenu extends AbstractMenu{

	public ViewMenu() {
		super("View infos");
		
		this.checkInMenuItem(new AbstractMenuItem("View Lua Vars") { 
			public void onClick(SelectionEvent e) { 
				final ArrayList<ElfNode> nodes = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
				final ArrayList<String> values = new ArrayList<String>(); 
				
				for(final ElfNode node : nodes) { 
					final String fullName = node.getFullName();
					final String name = node.getName(); 
					
					if(name.contains("&")) { 
						values.add(fullName) ; 
					} else if(!fullName.contains("#") && !fullName.contains("@") && !fullName.contains("&")) { 
						values.add(fullName) ; 
					} 
					
					node.iterateChildsDeep(new IIterateChilds() { 
						public boolean iterate(final ElfNode node) { 
							final String fullName = node.getFullName();
							final String name = node.getName(); 
							
							if(name.contains("&")) {
								values.add(fullName) ;
							} else if(!fullName.contains("#") && !fullName.contains("@") && !fullName.contains("&")) { 
								values.add(fullName) ;
							} 
							return false; 
						} 
					}); 
				} 
				
				final String [] shows = new String[values.size()];
				values.toArray(shows);
				
				final AnalysisDialog<String[]> dialog = new AnalysisDialog<String[]>("View Lua Vars (" + values.size() + ")",false);
				dialog.open(shows, String[].class); 
			} 
		}); 
		
		this.checkInMenuItem(new AbstractMenuItem("View Nodes") {
			public void onClick(SelectionEvent e) {
				final ArrayList<ElfNode> nodes = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
				final int count[] = {nodes.size()};
				for(ElfNode node : nodes) {
					node.iterateChildsDeep(new IIterateChilds() { 
						public boolean iterate(ElfNode node) { 
							count[0]++;
							return false;
						} 
					});
				} 
				
				final AnalysisDialog<Integer> dialog = new AnalysisDialog<Integer>("View Nodes",false);
				dialog.open(count[0], Integer.class); 
			} 
		}); 
		
		this.checkInMenuItem(new AbstractMenuItem("View Resource") {
			public void onClick(SelectionEvent e) {
				final ArrayList<ElfNode> nodes = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
				final Set<String> resids = new HashSet<String>(); 
				for(final ElfNode node: nodes) { 
					resids.addAll(node.getLegalResids(false)); 
					
					node.iterateChildsDeep(new IIterateChilds() {
						public boolean iterate(final ElfNode child) {
							resids.addAll(child.getLegalResids(false)); 
							return false;
						} 
					});
				} 
				
				final String [] array = new String[resids.size()];
				resids.toArray(array); 
				
				Arrays.sort(array, new Comparator<String>() {
					public int compare(String arg0, String arg1) {
						return arg0.compareTo(arg1);
					}
				});
				
				final AnalysisDialog<String[]> dialog = new AnalysisDialog<String[]>("View Resource("+array.length+")",false);
				dialog.open(array, String[].class);
			} 
			public boolean isShow() { 
				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode();
				return selectNode != null ;
			} 
		});
		
		this.checkInMenuItem(new AbstractMenuItem("View Resource Not Existed") {
			public void onClick(SelectionEvent e) {
				final ArrayList<ElfNode> nodes = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
				final Set<String> resids = new HashSet<String>(); 
				for(final ElfNode node: nodes) { 
					resids.addAll( node.getNotExistResids() );
					node.iterateChildsDeep(new IIterateChilds() { 
						public boolean iterate(final ElfNode child) { 
							resids.addAll( child.getNotExistResids() );
							return false;
						} 
					});
				} 
				
				final String [] array = new String[resids.size()];
				resids.toArray(array); 
				
				Arrays.sort(array, new Comparator<String>() {
					public int compare(String arg0, String arg1) {
						return arg0.compareTo(arg1);
					}
				});
				
				final AnalysisDialog<String[]> dialog = new AnalysisDialog<String[]>("View Resource Not Existed("+array.length+")",false);
				dialog.open(array, String[].class);
			} 
			public boolean isShow() { 
				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode();
				return selectNode != null ; 
			} 
		});
		
		this.checkInMenuItem(new AbstractMenuItem("View All Node Types") {
			public void onClick(SelectionEvent e) { 
				final ElfNode node = PowerMan.getSingleton(DataModel.class).getScreenNode().getBindNode();
				
				if(node != null) {
					final HashSet<String> set = new HashSet<String>();
					node.iterateChildsDeep(new IIterateChilds() {
						public boolean iterate(ElfNode node) {
							set.add(node.getClass().getSimpleName());
							return false;
						}
					});
					
					final ArrayList<String> types = new ArrayList<String>(set);
					Collections.sort(types);
					
					CollectDialog dialog = new CollectDialog("View All Node Types", false);
					dialog.open(types);
				} else { 
					MessageDialog message = new MessageDialog();
					message.open("View All Node Types", "No Scene Node!");
				}
			} 
		}); 
	}

	@Override
	public void onClick(SelectionEvent e) {
		
	}
}
