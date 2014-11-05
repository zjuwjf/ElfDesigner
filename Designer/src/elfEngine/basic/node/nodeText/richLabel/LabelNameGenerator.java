package elfEngine.basic.node.nodeText.richLabel;

import com.ielfgame.stupidGame.utils.FileHelper;

public class LabelNameGenerator {
	
	private static int sLabelGenerator = 0;
	private final static String sHead = FileHelper.getUserDir()+FileHelper.DECOLLATOR+"labels"+FileHelper.DECOLLATOR;
	
	public static synchronized String getLabelName() {
		sLabelGenerator++;
		return new StringBuilder().append(sHead).append("tmp_").append(sLabelGenerator).append(".png").toString();
	}
	
	public static String getLabelName(String name) {
		return new StringBuilder().append(sHead).append(name).toString();
	}
	
	
}
