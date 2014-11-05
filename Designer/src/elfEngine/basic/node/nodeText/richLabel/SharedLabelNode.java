package elfEngine.basic.node.nodeText.richLabel;

import java.io.File;

import elfEngine.basic.node.ElfNode;

public class SharedLabelNode extends LabelNode{
	
	public SharedLabelNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		
		this.setTextFont("__fzcy.ttf");
	}
	
	private static long string2long(String string) {
		if(string != null) {
			long sum = 0;
			long mul = 1;
			final char [] cs = string.toCharArray();
			for(int i=0; i<cs.length; i++) {
				sum += cs[i] * mul;
				mul *= 256;
			}
			
			return sum;
		}
		return -1;
	}
	
	public void calcSprite(float pMsElapsed) {
		if (mNeedUpdateTexture) {
			mNeedUpdateTexture = false;
			
			final String dir = LabelNameGenerator.getLabelName(this.mLabelDefine.toString());
			final File dirFile = new File(dir);
			if(!dirFile.exists()) {
				dirFile.mkdir();
			}
			
			final File picFile = new File(dirFile, new StringBuilder().append(string2long(this.getText())).append(".png").toString());
			mResid = picFile.getAbsolutePath();
			
			if(!picFile.exists()) {
				RichLabelManager.renderPoolLabel(this.getText(), this.mLabelDefine.copy(), mResid);
			}
		}
		super.calcSprite(pMsElapsed);
	}
	
}
