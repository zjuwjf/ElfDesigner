package elfEngine.basic.node.particle;

import com.ielfgame.stupidGame.data.ElfDataDisplay;

import elfEngine.basic.utils.ElfRandom;

public class Value2f extends ElfDataDisplay{
	public float value;
	public float variance;
	public float randomValue() {
		return ElfRandom.nextFloat(value-variance, value+variance);
	} 
	public void set(float value, float variance) {
		this.value = value;
		this.variance = variance;
	}
} 