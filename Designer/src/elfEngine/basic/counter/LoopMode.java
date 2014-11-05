package elfEngine.basic.counter;

public enum LoopMode {
	LOOP, RELOOP, STAY, 
	ENDLESS
	;

	/**
	 * @param source >= 0
	 * @return [0,1]
	 */
	public final float convert(float source){
		switch(this){
		case LOOP:
			return source - (int)source;
		case RELOOP:
			final int integer = (int)source;
			if((integer & 1) == 0){
				return source - integer;
			} else {
				return 1 + integer - source;
			}
		case STAY:
			return source>1? 1: source;
		case ENDLESS:
			return source;
		}
		return 0;
	}
}
