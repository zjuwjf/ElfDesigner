package elfEngine.basic.node.particle.modifier;
//
//import elfEngine.basic.node.ElfBasicNode;
//import elfEngine.basic.node.counter.BasicCounter;
//import elfEngine.basic.node.particle.IValue;
//import elfEngine.extend.ElfConfig;
//
//public class SpiralValue2Modifier2  extends BasicCounter implements IParticleModifier, ElfConfig{
//	
//	
//	protected final float [] mTransTmp = new float[2];
//	private IValue mGR, mGT, mSLife;
//	
//	private float mGRValue, mGTValue, mSlifeValue;
//	
//	public SpiralValue2Modifier2(final IValue gr, final IValue gt, final IValue slife){
//		mGR = gr;
//		mGT = gt;
//		mSLife = slife;
//		
//		set(mGR.getValue(), mGT.getValue(), mSLife.getValue());
//	}
//	
//	protected void set(final float gr, final float gt, final float sl){
//		mGRValue = gr;
//		mGTValue = gt;
//		mSlifeValue = sl;
//	}
//	
//	@Override
//	public IParticleModifier newInstance() {
//		return new SpiralValue2Modifier2(mGR, mGT, mSLife);
//	}
//
//	@Override
//	public void reDress() {	
//		reset();
//		mTransTmp[0] = mTransTmp[1] = 0;
//		mIsFinished = false;
//		set(mGR.getValue(), mGT.getValue(), mSLife.getValue());
//	}
//
//
//	@Override
//	public void modifier(ElfBasicNode node) {
//		final float progress = mProgress*MS_TO_SEC;
//		final float radius = 0.5f*mGRValue*progress*progress;
//		final float tang = 0.5f*mGTValue*progress*progress;
////		final float rad = tang/radius
//		
//	}
//
//
//	private ModifierListener mModifierListener = null;
//	
//	public void setModifierListener(final ModifierListener modifierListener){
//		mModifierListener = modifierListener;
//	}
//
//	@Override
//	@Deprecated
//	public float getValue() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//	
//	protected boolean mIsFinished = false;
//	
//	public void onModifierFinished(ElfBasicNode node){
//		if(mModifierListener != null){
//			mModifierListener.onFinished(node);
//		}
//	}
//	
//	public boolean isFinished(){
//		return mIsFinished;
//	}
//	
//	public void setFinished(){
//		mIsFinished = true;
//	}
//
//	@Override
//	public void setOriginPosition(float x, float y) {
//		// TODO Auto-generated method stub
//		
//	}
//	
//}
