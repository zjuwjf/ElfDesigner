package elfEngine.basic.node.particle;

public abstract class BasicValue2 implements IValue{
	
	protected float mValueA, mValueB;
	protected int mMode = 0;
	
	public BasicValue2(float valueA, float valueB, int mode){
		if(mode == 0){
			mValueA = valueA;
			mValueB = valueB;
		} else if(mode == 1){
			mValueA = valueA-valueB;
			mValueB = valueA+valueB;
		}
		
	}
	public float getValueA() {
		return mValueA;
	}
	public void setValueA(float mValueA) {
		this.mValueA = mValueA;
	}
	public float getValueB() {
		return mValueB;
	}
	public void setValueB(float mValueB) {
		this.mValueB = mValueB;
	}
	
}
