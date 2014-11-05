package elfEngine.basic.counter;

@Deprecated
public class SequenceResidCounter extends SequenceCounter<IResidCounter> implements IResidCounter{
	public SequenceResidCounter(float life, IResidCounter...cs){
		super(life,cs);
	}
	
	public SequenceResidCounter(IResidCounter...cs){
		super(cs);
	}
	
	public int getResid() {
		final IResidCounter counter = getCurrentCounter();
		return counter.getResid();
	}
}
