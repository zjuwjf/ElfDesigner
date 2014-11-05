//package elfEngine.basic.node.nodeAnimate.timeLine;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.util.HashMap;
//
//import elfEngine.basic.node.ElfNode;
//
//public class JsonNode extends ElfMotionNode {
//
//	public JsonNode(ElfNode father, int ordinal) {
//		super(father, ordinal);
//	} 
//	
//	public void onCreateRequiredNodes() {
//		super.onCreateRequiredNodes();
//		
//		readFromJson();
//	} 
//	
////	public void se
//	
//	void readFromJson() { 
//		final String path = "C:\\Users\\zju_wjf\\workspace\\Designer\\src\\elfEngine\\basic\\node\\nodeAnimate\\timeLine\\goblins.json";
//		
//		JsonValue root;
//		try {
//			root = new JsonReader().parse(new FileInputStream(new File(path)));
//			
//			// Bones.
//			final HashMap<String, ElfNode> nodeMap = new HashMap<String, ElfNode>();
//			
//			for (JsonValue boneMap = root.getChild("bones"); boneMap != null; boneMap = boneMap.next()) {
//				ElfNode parent = null;
//				String parentName = boneMap.getString("parent", null);
//				if (parentName != null) {
//					parent = nodeMap.get(parentName);
//					if (parent == null) 
//						throw new SerializationException("Parent bone not found: " + parentName);
//				} 
//				
//				if(parent == null) {
//					parent = this;
//				} 
//				
//				BoneNode bone = new BoneNode(parent, 0);
//				bone.setName( boneMap.getString("name") );
//				
//				bone.setLength(boneMap.getFloat("length", 0));
//				bone.setPosition(boneMap.getFloat("x", 0), boneMap.getFloat("y", 0));
//				bone.setRotate(-boneMap.getFloat("rotation", 0));
//				bone.setScale(boneMap.getFloat("scaleX", 1), boneMap.getFloat("scaleY", 1));
//				
//				bone.addToParent();
//				
//				nodeMap.put(boneMap.getString("name"), bone); 
//			} 
//			
//			//animates
//			for(JsonValue animations = root.getChild("animations"); animations != null; animations = animations.next()) {
//				for (JsonValue boneMap = animations.getChild("bones"); boneMap != null; boneMap = boneMap.next()) {
//					final String name = boneMap.name();
//					final ElfNode node = nodeMap.get(name);
//					
//					for (JsonValue timelineMap = boneMap.child(); timelineMap != null; timelineMap = timelineMap.next()) {
//						String timelineName = timelineMap.name(); 
//						if (timelineName.equals("rotate")) {
//							for (JsonValue valueMap = timelineMap.child(); valueMap != null; valueMap = valueMap.next()) {
//								float time = valueMap.getFloat("time");
//								float angle = valueMap.getFloat("angle");
//								final ElfMotionKeyNode key = this.addKey(node, NodePropertyType.RotateType, Math.round(time*1000));
//								key.setRotate(node.getRotate()-angle); 
//							}
//						} else if(timelineName.equals("translate")) {
//							for (JsonValue valueMap = timelineMap.child(); valueMap != null; valueMap = valueMap.next()) {
//								float time = valueMap.getFloat("time");
//								float x = valueMap.getFloat("x");
//								float y = valueMap.getFloat("y");
//								
//								final ElfMotionKeyNode key = this.addKey(node, NodePropertyType.PositionType, Math.round(time*1000));
//								key.setPosition(node.getPosition().x+x, node.getPosition().y+y);
//							} 
//						}
//					}
//				} 
//			}
//			
//			//skins
//			for (JsonValue skinMap = root.getChild("skins"); skinMap != null; skinMap = skinMap.next()) {
//				System.err.println("skip:"+skinMap.name());
//				if(skinMap.name().equals("goblin")) {
//					for (JsonValue slotEntry = skinMap.child(); slotEntry != null; slotEntry = slotEntry.next()) {
//						for (JsonValue entry = slotEntry.child(); entry != null; entry = entry.next()) {
//							final String name = entry.name();
//							final ElfNode parent = nodeMap.get(name);
//							
//							if(parent != null) {
//								final ElfNode node = new ElfNode(parent, 0);
//								final String resid = "C:\\Users\\zju_wjf\\Desktop\\龙\\新建文件夹\\" + entry.getString("name", "empty")+".png";
//								node.setResid(resid);
//								
//								final float x = entry.getFloat("x", 0);
//								final float y = entry.getFloat("y", 0);
//								final float rotation = entry.getFloat("rotation", 0);
//								final float sx = entry.getFloat("scaleX", 1);
//								final float sy = entry.getFloat("scaleY", 1);
//								
//								node.setRotate(-rotation);
//								node.setPosition(x, y);
//								node.setScale(sx, sy);
//								
//								node.addToParent();
//							}
//						} 
//					} 
//				} 
//			} 
//			
//			
//		} catch (FileNotFoundException e) { 
//			e.printStackTrace(); 
//		} 
//	} 
//	
//	public static void main(final String [] args) {
//		final String path = "C:\\Users\\zju_wjf\\workspace\\Designer\\src\\elfEngine\\basic\\node\\nodeAnimate\\timeLine\\goblins.json";
//		
//		JsonValue root;
//		try {
//			root = new JsonReader().parse(new FileInputStream(new File(path)));
//			
//			// Bones.
//			HashMap<String, ElfNode> map = new HashMap<String, ElfNode>();
//			for (JsonValue boneMap = root.getChild("bones"); boneMap != null; boneMap = boneMap.next()) {
//				ElfNode parent = null;
//				String parentName = boneMap.getString("parent", null);
//				if (parentName != null) {
//					parent = map.get(parentName);
//					if (parent == null) throw new SerializationException("Parent bone not found: " + parentName);
//				}
//				ElfNode boneData = new ElfNode(parent, 0);
//				boneData.setName( boneMap.getString("name") );
//				
////				boneData.length = boneMap.getFloat("length", 0) * scale;
////				boneData.x = boneMap.getFloat("x", 0) * scale;
////				boneData.y = boneMap.getFloat("y", 0) * scale;
////				boneData.rotation = boneMap.getFloat("rotation", 0);
////				boneData.scaleX = boneMap.getFloat("scaleX", 1);
////				boneData.scaleY = boneMap.getFloat("scaleY", 1);
//				
//				map.put(boneMap.getString("name"), boneData);
//			}
//			
//			
//			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		
//	}
//} 
