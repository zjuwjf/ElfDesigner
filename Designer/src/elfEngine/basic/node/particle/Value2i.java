package elfEngine.basic.node.particle;

import com.ielfgame.stupidGame.data.ElfDataDisplay;

import elfEngine.basic.utils.ElfRandom;

public class Value2i extends ElfDataDisplay{ 
	public int value;
	public int variance;
	public int randomValue() {
		return ElfRandom.nextInt(value-variance, value+variance+1);
	}
	public void set(int value, int variance) {
		this.value = value;
		this.variance = variance;
	}
} 
