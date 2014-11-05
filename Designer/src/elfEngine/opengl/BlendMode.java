package elfEngine.opengl;

import org.lwjgl.opengl.GL11;

public enum BlendMode implements BlendConstants{
	/**
	1.GL_ONE, GL_ZERO
	2.GL_ONE, GL_ONE
	3.GL_ONE, GL_ONE_MINUS_DST_ALPHA
	4.GL_SRC_ALPHA, GL_ONE
	5.GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA
	 */
	//src
	/***
	 *  GL_ONE_MINUS_DST_ALPHA��GL_ZERO, GL_ONE��GL_DST_COLOR GL_ONE_MINUS_DST_COLOR��GL_SRC_ALPHA GL_ONE_MINUS_SRC_ALPHA��GL_DST_ALPHA �� GL_SRC_ALPHA_SATURATE��
	 */
	//dst
	/***
	 * GL_ZERO GL_ONE��GL_SRC_COLOR GL_ONE_MINUS_SRC_COLOR��GL_SRC_ALPHA GL_ONE_MINUS_SRC_ALPHA��GL_DST_ALPHA���� GL_ONE_MINUS_DST_ALPHA��
	 */
	//770,1
	//770,771
	ACTIVLE(GL11.GL_SRC_ALPHA, GL11.GL_ONE), 
//	HIGHT_LIGHT(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA), // = 25
	BLEND(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA), 
	
	BLEND00, 
	BLEND01, 
	BLEND02, 
	BLEND03, 
	BLEND04, 
	BLEND05, 
	BLEND06, 
	BLEND07, 
	
	BLEND10, 
	BLEND11, 
	BLEND12, 
	BLEND13, 
	BLEND14, 
	BLEND15, 
	BLEND16, 
	BLEND17, 
	
	BLEND20, 
	BLEND21, 
	BLEND22, 
	BLEND23, 
	BLEND24, 
	BLEND25, 
	BLEND26, 
	BLEND27, 
	
	BLEND30, 
	BLEND31, 
	BLEND32, 
	BLEND33,
	BLEND34,
	BLEND35, 
	BLEND36,
	BLEND37,
	
	BLEND40,
	BLEND41,
	BLEND42,
	BLEND43,
	BLEND44,
	BLEND45,
	BLEND46,
	BLEND47,
	
	BLEND50,
	BLEND51,
	BLEND52,
	BLEND53,
	BLEND54,
	BLEND55,
	BLEND56,
	BLEND57,
	
	BLEND60,
	BLEND61,
	BLEND62,
	BLEND63,
	BLEND64,
	BLEND65,
	BLEND66,
	BLEND67,
	
	BLEND70,
	BLEND71,
	BLEND72,
	BLEND73,
	BLEND74,
	BLEND75,
	BLEND76,
	BLEND77,
	
	BLEND80,
	BLEND81,
	BLEND82,
	BLEND83,
	BLEND84,
	BLEND85,
	BLEND86,
	BLEND87,
	; 
	public final int sourceBlendFunction, destinationBlendFunction;
	BlendMode(final int source, final int dest){
		this.sourceBlendFunction = source;
		this.destinationBlendFunction = dest;
	}
	
	BlendMode(){
		int ordinal = ordinal() - 2;
		final int mod = GL_BLEND_DST.length;
		
		int x = (ordinal)/mod;
		int y = (ordinal)%mod;
		
		sourceBlendFunction = GL_BLEND_SRC[x];
		destinationBlendFunction = GL_BLEND_DST[y];
	}
	
	public static String [] toStrings(){
		final String [] ret = new String[BlendMode.values().length];
		for(int i=0; i<ret.length; i++){
			ret[i] = BlendMode.values()[i].toString();
		} 
		return ret;
	}
	
	public static BlendMode fromString(String string){
		final BlendMode [] items = BlendMode.values();
		for(int i=0; i<items.length; i++){
			if(items[i].toString().equalsIgnoreCase(string)){
				return items[i];
			}
		}
		return null;
	}
}

interface BlendConstants{
	public final static int [] GL_BLEND_SRC = {
		GL11.GL_ONE_MINUS_DST_ALPHA,//773		0
		GL11.GL_ZERO,//0						1
		GL11.GL_ONE,//1						2       --- 
		GL11.GL_DST_COLOR,//774				3
		GL11.GL_ONE_MINUS_DST_COLOR,//775		4
		GL11.GL_SRC_ALPHA,//770				5
		GL11.GL_ONE_MINUS_SRC_ALPHA,//771		6
		GL11.GL_DST_ALPHA,//772				7
		GL11.GL_SRC_ALPHA_SATURATE//776		8
	};
	
	public final static int [] GL_BLEND_DST = {
		GL11.GL_ZERO,//0 						0
		GL11.GL_ONE,//1 						1
		GL11.GL_SRC_COLOR,//768 				2
		GL11.GL_ONE_MINUS_SRC_COLOR,//769 	3
		GL11.GL_SRC_ALPHA,//770 				4
		GL11.GL_ONE_MINUS_SRC_ALPHA,//771 	5       ----
		GL11.GL_DST_ALPHA,//772 				6 
		GL11.GL_ONE_MINUS_DST_ALPHA,//773 	7
	}; 
	//ACTIVLE(GL11.GL_SRC_ALPHA, GL11.GL_ONE) = Blend51 
	//BLEND(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA) = Blend55 
} 
