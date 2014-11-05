package elfEngine.basic.debug;

import com.ielfgame.stupidGame.Redirect;


/**
 * @author zju_wjf
 *
 */
public final class Debug {
	
	private static final String mTag = "";
	
	public static int LEVEL_V = 0;
	public static int LEVEL_D = 1;
	public static int LEVEL_I = 2;
	public static int LEVEL_W = 3;
	public static int LEVEL_E = 4;
	
	private static int mLevel = LEVEL_V;
	public static void setLevel(int level){
		mLevel = level;
	}
	
	public static void i(String msg){
		if(mLevel<=LEVEL_I){
			Redirect.outPrintln(mTag + msg);
		}
	}
	
	public static void w(String msg){
		if(mLevel<=LEVEL_W){
			Redirect.outPrintln(mTag + msg);
		}
	}
	
	public static void v(String msg){
		if(mLevel<=LEVEL_V){
			Redirect.outPrintln(mTag + msg);
		}
	}
	
	public static void d(String msg){
		if(mLevel<=LEVEL_D){
			Redirect.outPrintln(mTag + msg);
		}
	}
	
	public static void e(String msg){
		if(mLevel<=LEVEL_E){
			System.out.println(mTag + msg);
		}
	}
	
	public static void exit(String msg){
		e(msg);
		System.exit(1);
	}
}
