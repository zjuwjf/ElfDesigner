package elfEngine.extend.count;


public abstract class ElfCounters<T extends Enum<T>>{
	private ElfCounter [] counters;
	private T mEnum;
	private T [] mEnums;
	
	public ElfCounters(T mEnum, ElfCounter ... counters){
		this.counters = counters;
		this.mEnums = mEnum.getDeclaringClass().getEnumConstants();
		this.setState(mEnum);
		
		if(this.counters.length != mEnum.getDeclaringClass().getEnumConstants().length){
//			Debug.e("ElfCounters constructer error");
//			Debug.e(""+mEnum.getDeclaringClass().getName()+"'length is not equals with the counters!");
			System.exit(1);
		}
	}
	
	public void count(float add) {
		this.getCounter().count(add);
	}

	public ElfCounter getCounter() {
		return counters[index(mEnum)];
	}

	public T getState() {
		return this.mEnum;
	}

	public void setState(T e) {
		this.mEnum = e;
		this.getCounter().reset();
	}
	
	public void setNextState(){
		final int index = index(mEnum)+1;
		if(index <mEnums.length)
			setState(mEnums[index]);
		else
			setState(mEnums[0]);
	}
	
	public ElfCounter getCounter(T t){
		return counters[index(t)];
	}
	
	private int index(Enum<?> e){
		for(int i=0; i<mEnums.length; i++){
			if(e.equals(mEnums[i])){
				return i;
			}
		}
		return 0;
	}
	
	
}