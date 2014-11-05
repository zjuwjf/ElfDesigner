package elfEngine.graphics;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import elfEngine.basic.list.ElfCircle;
import elfEngine.basic.math.MathCons;
import elfEngine.basic.pool.ArrayPool;
import elfEngine.basic.pool.ElfPool;
import elfEngine.basic.pool.IElfPoolItem;
import elfEngine.basic.pool.INewInstance;
import elfEngine.graphics.GraphHelper.Site;

public class Polygon extends BasicShape {
	
	private final static float sRayMaxRight = 10000;

	protected final float[] mXs;
	protected final float[] mYs;
	protected final int mVertexs;
	protected final Rectangle mBounds = new Rectangle();
	
	private float mAreaTmp = Float.MAX_VALUE;
	
	public Polygon(List<Vertex> list){
		mVertexs = list.size();
		if (mVertexs <= 2) {
			throw new IllegalArgumentException("vertexs must more than 2!");
		}
		
		mXs = new float[mVertexs];
		mYs = new float[mVertexs];
		int i=0;
		for(Vertex v:list){
			mXs[i] = v.x;
			mYs[i] = v.y;
			i++;
		}
		
		updateBounds();
	}

	public Polygon(Vertex... vs) {
		if (vs.length <= 2) {
			throw new IllegalArgumentException("vertexs must more than 2!");
		}
		
		mVertexs = vs.length;
		mXs = new float[mVertexs];
		mYs = new float[mVertexs];
		
		for (int i = 0; i < mVertexs; i++) {
			mXs[i] = vs[i].x;
			mYs[i] = vs[i].y;
		}
		
		updateBounds();
	}
	
	public Polygon(PointF... vs) {
		if (vs.length <= 2) {
			throw new IllegalArgumentException("vertexs must more than 2!");
		}

		mVertexs = vs.length;
		mXs = new float[mVertexs];
		mYs = new float[mVertexs];

		for (int i = 0; i < mVertexs; i++) {
			mXs[i] = vs[i].x;
			mYs[i] = vs[i].y;
		}
		
		updateBounds();
	}

	public Polygon(final float xs[], final float ys[], final int ns) {
		this(xs, ys, ns, true);
	}
	
	public Polygon(final float xs[], final float ys[], final int ns, final boolean isCopy){
		if (ns > xs.length || ns > ys.length) {
			throw new IndexOutOfBoundsException("ns > xs.length || " + "ns > ys.length");
		}

		if (ns < 0) {
			throw new NegativeArraySizeException("ns < 0");
		}
		
		if(ns <= 2){
			throw new IllegalArgumentException("ns must more than 2!");
		}
		
		if(isCopy){
			this.mVertexs = ns;
			mXs = new float[mVertexs];
			mYs = new float[mVertexs];
			
			System.arraycopy(xs, 0, mXs, 0, ns);
			System.arraycopy(ys, 0, mYs, 0, ns);
		} else {
			this.mVertexs = ns;
			mXs = xs;
			mYs = ys;
		}
		
		updateBounds();
	}

	public Polygon(final Polygon copy) {
		this(copy.mXs, copy.mYs, copy.mVertexs);
	}
	
//	public 

	public int getVertexs() {
		return mVertexs;
	}

	public Rectangle bounds() {
		return mBounds;
	}

	public void translate(final float x, final float y) {
		for (int i = 0; i < mVertexs; i++) {
			mXs[i] += x;
			mYs[i] += y;
		}
		mBounds.translate(x, y);
	}

	 public float area(){
		 if(mAreaTmp!=Float.MAX_VALUE){
			 return Math.abs( mAreaTmp );
		 }
		 
		 float area = 0f;
		 area += mXs[mVertexs-1]*mYs[0] - mXs[0]*mYs[mVertexs-1];
		 
		 for(int i=1; i<mVertexs; i++){
			 area += mXs[i-1] * mYs[i] - mXs[i] * mYs[i-1];
		 }
		 mAreaTmp = area;
		 /**
		  * if area < 0, CW, elf CCW
		  */
		 return Math.abs( area );
	 }
	 
	 public boolean isCW(){
		 if(mAreaTmp!=Float.MAX_VALUE){
			 return mAreaTmp < 0;
		 }
		 
		 float area = 0f;
		 area += mXs[mVertexs-1]*mYs[0] - mXs[0]*mYs[mVertexs-1];
		 
		 for(int i=1; i<mVertexs; i++){
			 area += mXs[i-1] * mYs[i] - mXs[i] * mYs[i-1];
		 }
		 mAreaTmp = area;
		 return area < 0;
	 }

	public boolean contains(final float x, final float y) {
		
		final Rectangle bounds = bounds(); 
		if(!bounds.contains(x, y)){ 
			return false; 
		} 
		
		int hits = 0; 
		final Segment s = new Segment();
		for(int i=0; i<mVertexs; i++){
			getLine(i, s);
			if(GraphHelper.isCrossed(x, y, sRayMaxRight, y, s.p1.x, s.p1.y, s.p2.x, s.p2.y)){
				hits++;
			} 
		} 

		return ((hits & 1) != 0);
	}

	
	public float [] getXs(){
		return mXs;
	}
	
	public float [] getYs(){
		return mYs;
	}
	
	public short [] triangles(){
		final int triangleIndexArryLen = (mVertexs-2)*3;
		final short [] triangleIndexs = new short[triangleIndexArryLen];
		/**
		 * in circle ,make sure CW
		 */
		final ElfCircle<Inner> circle = new ElfCircle<Inner>();
		final boolean isCW = isCW();
		for(short i=0; i<mVertexs; i++){
			final Inner inner = (Inner)sPool.getFreshItem();
			inner.x = mXs[i];
			inner.y = mYs[i];
			inner.index = i;
			
			if(isCW){
				circle.insertLast(inner);
			} else {
				circle.insertFirst(inner);
			}
		}
		
		int triangleIndex = 0;
		final ElfCircle<Inner>.Iterator it = circle.iterator();
		it.move(1);
		int fail = 0;
		while(triangleIndex< triangleIndexArryLen){
			final Inner p1 = it.getPrev();
			final Inner p2 = it.getCurrent();
			final Inner p3 = it.getNext();
			
			final Site type = GraphHelper.side(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
			if(type==Site.ON_LINE || type==Site.P1_EXTEND || type==Site.P2_EXTEND){
				it.remove();
				p2.recycle();
				
//				final String s = "p"+p1.index+", p"+p2.index+", p"+p3.index+" succ";
//				Debug.e(s);
//				System.out.println(s);
				
				triangleIndexs[triangleIndex++] = p1.index;
				triangleIndexs[triangleIndex++] = p2.index;
				triangleIndexs[triangleIndex++] = p3.index;
			}else if(type == Site.LEFT_SIDE){//CONVEX
				final int remains = (triangleIndexArryLen-triangleIndex)/3 - 1;
				it.move(1);//except 3 vertex(p1, p2, p3)
				boolean isAnyInTriangle = false;
				
				int move = 0;
				for(int i=0; i<remains; i++){
					final Inner test = it.next();
					move++;
					if(GraphHelper.isInsideTriangle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, test.x, test.y)){
						isAnyInTriangle = true;
//						final String s = "p"+test.index+" in:"+p1.index+","+p2.index+","+p3.index;
//						Debug.e(s);
//						System.out.println(s);
						break;
					}
				}
				
				it.move(remains+2-move);//move to p2
				if(!isAnyInTriangle){
					
					//remove p2
					it.remove();
					p2.recycle();
					
//					final String s = "p"+p1.index+", p"+p2.index+", p"+p3.index+" succ";
//					Debug.e(s);
//					System.out.println(s);
					triangleIndexs[triangleIndex++] = p1.index;
					triangleIndexs[triangleIndex++] = p2.index;
					triangleIndexs[triangleIndex++] = p3.index;
				} else{
					fail++;
				}
			} else {
//				final String s = "p"+p1.index+", p"+p2.index+", p"+p3.index+" fail";
//				Debug.e(s);
//				System.out.println(s);
				
				fail++;
			}
			
			if(fail>400){
				break;
			}
			
			it.move(1);
		}
		
		return triangleIndexs;
	}
	
	private static final ElfPool sPool = new ElfPool();
	static {
		sPool.fillPool(new Inner(), 32);
	}
	
	private static class Inner implements IElfPoolItem, INewInstance{
		public float x,y;
		public short index;

		public void recycle() {
			sPool.recycle(this);
		}

		public IElfPoolItem newInstance() {
			final Inner ret = new Inner();
			sPool.recycle(ret);
			
			return ret;
		}
	}
	
	private final void updateBounds() {
		mBounds.left = mXs[0];
		mBounds.right = mXs[0];

		mBounds.bottom = mYs[0];
		mBounds.top = mYs[0];

		for (int i = 1; i < mVertexs; i++) {
			final float x = mXs[i];
			final float y = mYs[i];
			if (mBounds.left > x) {
				mBounds.left = x;
			}
			if (mBounds.right < x) {
				mBounds.right = x;
			}
			if (mBounds.bottom > y) {
				mBounds.bottom = y;
			}
			if (mBounds.top < y) {
				mBounds.top = y;
			}
		}
	}

	public boolean collideWith(IShape shape) {
		final ShapeType type = shape.getType();
		if(type == ShapeType.POLYGON){
			final Polygon polygon = (Polygon)shape;
			for(int i=0; i<polygon.mVertexs; i++){
				if(contains(polygon.mXs[i], polygon.mYs[i])){
					return true;
				}
			}
		} else if(type == ShapeType.RECTANGLE){
			final Rectangle rectangle = (Rectangle)shape;
			if(contains(rectangle.left, rectangle.bottom)){
				return true;
			} else if(contains(rectangle.left, rectangle.top)){
				return true;
			} else if(contains(rectangle.right, rectangle.bottom)){
				return true;
			} else if(contains(rectangle.right, rectangle.top)){
				return true;
			}
			
		} else if(type == ShapeType.CIRCLE){
			for(int i=0; i<mVertexs; i++){
				final Circle circle = (Circle)shape;
				if(circle.contains(mXs[i], mYs[i])){
					return true;
				}
			}
		}
		return false;
	}

	public Vertex getCentre() {
		float x=0, y=0;
		for(int i=0; i<mVertexs; i++){
			x+=mXs[i];
			y+=mYs[i];
		}
		
		final Vertex centre = new Vertex(x/mVertexs, y/mVertexs); 
		return centre;
	}

	public ShapeType getType() {
		return ShapeType.POLYGON;
	}
	
	/**
	 * @param scalex
	 * @param scaley
	 * @param centrex
	 * @param centrey
	 */
	public void scale(final float scalex,final float scaley,final float centrex,final float centrey){
		if(mAreaTmp!=Float.MAX_VALUE){
			mAreaTmp *= scalex*scaley;
		}
		
		for(int i=0; i<mVertexs; i++){
			mXs[i] = (mXs[i]-centrex)*scalex+centrex;
			mYs[i] = (mYs[i]-centrey)*scaley+centrey;
		}
	}
	
	/**
	 * @param scale
	 * @param centrex
	 * @param centrey
	 */
	public void scale(final float scale,final float centrex,final float centrey){
		scale(scale,scale,centrex,centrey);
	}
	
	/**
	 * @param rotate
	 * @param centrex
	 * @param centrey
	 * CW
	 */
	public void rotate(final float rotate,final float centrex,final float centrey){
		final float z = rotate*MathCons.RAD_TO_DEG;
		final float cos = (float)Math.cos(z);
		final float sin = (float)Math.sin(z);
		
		for(int i=0; i<mVertexs; i++){
			final float dx = mXs[i]-centrex;
			final float dy = mYs[i]-centrey;
			
			mXs[i] = dx*cos+dy*sin+centrex;
			mYs[i] = dy*cos-dx*sin+centrey;
		}
	}
	
	public float getGirth(){
		float sum = 0.0f;
		for(int i=0; i<mVertexs; i++){
			sum += getSideLength(i);
		}
		return sum;
	}
	
	public float getSideLength(final int index){
		final float [] tmp = ArrayPool.float4;
		getLine(index, tmp);
		return GraphHelper.distance(tmp[0], tmp[1], tmp[2], tmp[3]);
	}
	
	
	private final static LinkedList<Vertex> read(final InputSource pInputStream){
		final LinkedList<Vertex> list = new LinkedList<Vertex>();
		try{
			final SAXParserFactory spf = SAXParserFactory.newInstance();
			final SAXParser sp = spf.newSAXParser();
			
			final XMLReader xr = sp.getXMLReader();
			
			final DefaultHandler handler = new DefaultHandler(){
				public void startElement(final String pUri, final String pLocalName, final String pQualifiedName, final Attributes pAttributes) throws SAXException {
					super.startElement(pUri,pLocalName,pQualifiedName,pAttributes);
					if(pQualifiedName.equals(XML_PONIT)){
						final Vertex p = new Vertex();
						p.x = Integer.parseInt(getAttributeOrThrow(pAttributes, XML_X));
						p.y = -Integer.parseInt(getAttributeOrThrow(pAttributes, XML_Y));
						list.add(p);
						
					} else if(pQualifiedName.equals(XML_LINE)){
					} else if(pQualifiedName.equals(XML_DART)){
					} 
				}
				
			};
			xr.setContentHandler(handler);
			
			try {
				xr.parse(pInputStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (final SAXException se) {
			se.printStackTrace();
		} catch (final ParserConfigurationException pe) {
			pe.printStackTrace();
		} 
		
		return list;
	}
	
	private final static String getAttributeOrThrow(final Attributes pAttributes, final String pAttributeName) {
		final String value = pAttributes.getValue("", pAttributeName);
		if(value != null) {
			return value;
		} else {
			throw new IllegalArgumentException("No value found for attribute: " + pAttributeName);
		}
	}
	
	public static final String XML_LEFT = "<";
	public static  final String XML_RIGHT = "/>";
	public static  final String XML_COMILA = "\"";
	public static  final String XML_TABLE = "    ";
	
	public static  final String XML_PONIT = "point";
	public static  final String XML_INDEX = "index";
	
	public static  final String XML_SPACE = " ";
	public static  final String XML_EQUAL = "=";
	
	public static  final String XML_POINT_BEG = "<points>\n";
	public static  final String XML_PONIT_END = "</points>\n";
	
	public static  final String XML_X = "x";
	public static  final String XML_Y = "y";
	
	public static  final String XML_DART = "dart";
	public static  final String XML_DART_BEG = "<darts>\n";
	public static  final String XML_DART_END = "</darts>\n";
	public static  final String XML_DART_TYPE = "type";
	
	public static  final String XML_LINE = "line";
	public static  final String XML_LINE_BEG = "<lines>\n";
	public static  final String XML_LINE_END = "</lines>\n";
	public static  final String XML_LINE_TYPE = "type";
	
	public static  final String XML_INFO_BEG = "<infos>\n";
	public static  final String XML_INFO_END = "</infos>\n";
	
	public static  final String XML_COMMENT_LEFT = "<!-- ";
	public static  final String XML_COMMENT_RIGHT = " -->\n";
	
	public static void main(String [] args) throws FileNotFoundException{
		final String path = "C:\\Users\\zju_wjf\\workspace\\slash tool\\slash tool\\map\\level_";
		final List<Vertex> list = read(new InputSource(new FileInputStream(path+"1.xml")));
		
		final Polygon p = new Polygon(list);
		System.out.println("cw:"+p.isCW());
		
		System.out.println(p.contains(127, -63));
	}
}
