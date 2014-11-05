package elfEngine.basic.node.particle;

import elfEngine.extend.ElfRandom;

public class ElfValue2 extends BasicValue2{
	
	public ElfValue2(float valueA, float valueB) {
		super(valueA, valueB, 0);
	}
	
	public ElfValue2 (float valueA, float valueB, int mode) {
		super(valueA, valueB, mode);
	}
	
	@Override
	public float getValue() {
		if(mValueA == mValueB){
			return mValueA;
		}
		return mValueA+(mValueB-mValueA)*ElfRandom.nextFloat();
	}
	
}
