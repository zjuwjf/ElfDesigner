package elfEngine.basic.node.graph;

import elfEngine.basic.node.ElfNode;
import elfEngine.opengl.DrawHelper;
import elfEngine.opengl.GLHelper;

public class FlagNodes {
	
	public static class ColorFlagOKNode extends ElfNode {
		
		public ColorFlagOKNode(ElfNode father, int ordinal) {
			super(father, ordinal);
			
			this.setUseSettedSize(true);
		}
		
		public void drawSelf() {
			final float width = this.getWidth();
			final float height = this.getHeight();
			
			final float min = Math.min(width, height);
			final float thin = min*0.2f;
			
			GLHelper.glPushMatrix();
			
			GLHelper.glTranslatef(min*0.05f, -min*0.1f, 0);
			
			GLHelper.glScalef(-1, -1);
			
			GLHelper.glRotatef(-45);
			
			DrawHelper.fillRect(min*0.9f, thin, -min*0.1f, min*0);
			
			GLHelper.glRotatef(90);
			
			DrawHelper.fillRect(min*0.5f, thin, min*0.25f, min*0.25f);
			
			GLHelper.glPopMatrix();
		}
	}
	
	public static class ColorFlagCancelNode extends ElfNode {
		
		public ColorFlagCancelNode(ElfNode father, int ordinal) {
			super(father, ordinal);
			
			this.setUseSettedSize(true);
		}
		
		public void drawSelf() {
			final float width = this.getWidth();
			final float height = this.getHeight();
			
			final float min = Math.min(width, height);
			final float thin = min*0.2f;
			
			GLHelper.glPushMatrix();
			
			GLHelper.glRotatef(-45);
			
			DrawHelper.fillRect(min, thin, 0, 0);
			
			GLHelper.glRotatef(90);
			
			DrawHelper.fillRect(min, thin, 0, 0);
			
			GLHelper.glPopMatrix();
		}
	}
	
}
