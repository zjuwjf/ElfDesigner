package elfEngine.basic.modifier;

//import elfEngine.basic.node.ElfBasicNode;
//import elfEngine.basic.node.counter.Interpolator;
//import elfEngine.basic.node.counter.LoopMode;
//import elfEngine.graphics.Segment;

//public class LineModifier extends BasicNodeModifier{
//	private Align mAlign;
//	private Segment mLine;
//	private final static Segment sTmp = new Segment();
//	
//	public LineModifier(Align align, float beg, float end, float period, float life,
//			LoopMode mode, Interpolator inter) {
//		this(null, align, beg, end, period, life, mode, inter);
//	}
//	
//	public LineModifier(Segment seg, Align align, float beg, float end, float period, float life,
//			LoopMode mode, Interpolator inter) {
//		super(beg, end, period, life, mode, inter);
//		mAlign = align;
//		mLine = seg;
//	}
//	
//	public void setAlign(Align align){
//		mAlign = align;
//	}
//	
//	public Align getAlign(){
//		return mAlign;
//	}
//
//	@Override
//	public void modifier(ElfBasicNode node) {
//		final ElfLine lineNode = (ElfLine)node;
//		final float value = getValue();
//		
//		if(mLine == null){
//			final Segment l = lineNode.getLine();
//			mLine = new Segment(l);
//		}
//		
//		final Segment l = sTmp;
//		if(mAlign == Align.LEFT){
//			l.p1.x = mLine.p1.x;
//			l.p1.y = mLine.p1.y;
//			
//			l.p2.x = (mLine.p2.x-mLine.p1.x)*value+l.p1.x;
//			l.p2.y = (mLine.p2.y-mLine.p1.y)*value+l.p1.y;
//		} else if(mAlign == Align.CENTRE){
//			final float halfW = (mLine.p2.x-mLine.p1.x)*0.5f;
//			final float halfH = (mLine.p2.y-mLine.p1.y)*0.5f;
//			final float centreX = (mLine.p1.x+mLine.p2.x)*0.5f;
//			final float centreY = (mLine.p1.y+mLine.p2.y)*0.5f;
//			l.p1.x = centreX-halfW;
//			l.p1.y = centreY-halfH;
//			l.p2.x = centreX+halfW;
//			l.p2.y = centreY+halfH;
//		} else if(mAlign == Align.RIGHT){
//			l.p2.x = mLine.p2.x;
//			l.p2.y = mLine.p2.y;
//			
//			l.p1.x = (-mLine.p2.x+mLine.p1.x)*value+l.p2.x;
//			l.p1.y = (-mLine.p2.y+mLine.p1.y)*value+l.p2.y;
//		} 
//		
//		lineNode.setLine(l);
//	}
//	
//	public enum Align{
//		LEFT,CENTRE,RIGHT;
//	}
//}
