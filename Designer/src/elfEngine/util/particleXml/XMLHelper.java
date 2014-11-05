package elfEngine.util.particleXml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLHelper {
	
	public static void main(String [] ars) throws SAXException, IOException, ParserConfigurationException{
		final XMLNode root = getRootFromFile(new File("D://ball1.plist"));
		
		final ArrayList<XMLNode> list = root.findByKey("key");
		
		for(XMLNode node : list){
			System.out.print("public String "+node.value+" = "+"\""+node.value+"\""+";\n");
		}		
		
		System.out.print("\n\n\n");
		
		for(XMLNode node : list){
			System.out.print("    else if("+node.value+".equals(value)){\n");
			System.out.print("    \tfinal String context = root.findNextByNode(next).value;\n");
			final XMLNode next = root.findNextByNode(node);
			if(next.name.equals("string")){
				System.out.print("    \t"+node.value+"Value = context;\n");
			} else if(next.name.equals("real")){
				System.out.print("    \t"+node.value+"Value = Float.valueOf(context);\n");
			} else if(next.name.equals("integer")){
				System.out.print("    \t"+node.value+"Value = Integer.valueOf(context);\n");
			}
			
			
			System.out.print("    }\n");
		}
		
		for(XMLNode node : list){
			final XMLNode next = root.findNextByNode(node);
			if(next.name.equals("string")){
				System.out.print("public String "+node.value+"Value;\n");
			} else if(next.name.equals("real")){
				System.out.print("public float "+node.value+"Value;\n");
			} else if(next.name.equals("integer")){
				System.out.print("public int "+node.value+"Value;\n");
			}
		}
	}
	
//	public static final XMLNode getRootFromAsset(final String pathName) throws SAXException, IOException, ParserConfigurationException{
//		final ElfBasicGameActivity activity = ActivityHelper.getActivity();
//		final InputStream inputStream = activity.getAssets().open(pathName);
//		final InputSource inputScore = new InputSource(inputStream);
//		return getRoot( inputScore );
//	}
	
	public static final XMLNode getRootFromFile(final File file) throws SAXException, IOException, ParserConfigurationException{
		final InputStream inputStream = new FileInputStream(file);
	    InputSource inputSource = new InputSource(inputStream);
	    
		return getRoot(inputSource);
	}
	
	public static XMLNode getRoot(final InputSource inputSource) throws SAXException, IOException, ParserConfigurationException{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	    documentBuilderFactory.setNamespaceAware(true);
	    
	    final Document document =  documentBuilderFactory.newDocumentBuilder().parse(inputSource); 
	    final Element rootElement = document.getDocumentElement(); 
	    
	    try{
	    	final Reader reader = inputSource.getCharacterStream();
	    	if(reader!=null){
	    		reader.close();
	    	}
	    } catch(IOException ioe){
	    }
	    
	    try{
	    	final InputStream input = inputSource.getByteStream();
	    	if(input!=null){
	    		input.close();
	    	}
	    }catch(IOException ioe){
	    }
	    
		return toXMLNode(rootElement, 0);
	}
	
	private final static boolean hasChilds(final Element element){
		
		final NodeList list = element.getChildNodes(); 
		final int length = list.getLength();
		for(int i=0; i<length; i++){
			final Node node = list.item(i);
			if(node instanceof Element){
				return true;
			}
		}
		
		return false;
	}
	
	private final static XMLNode toXMLNode(final Element element, final int depth){
		final String name = element.getTagName();
		final XMLNode rootNode = new XMLNode(name, depth);
		
		if(hasChilds(element)){
			final NodeList list = element.getChildNodes(); 
			final int length = list.getLength();
			for(int i=0; i<length; i++){
				final Node node = list.item(i);
				if(node instanceof Element){
					final Element e = (Element)node;
					rootNode.childs.add( toXMLNode(e, depth+1) );
				}
			}
		} else {
			rootNode.value = element.getTextContent();
		}

		return rootNode;
	}
}
