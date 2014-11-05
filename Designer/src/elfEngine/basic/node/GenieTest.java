package elfEngine.basic.node;

import org.lwjgl.opengl.GL11;

import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.data.ElfPointi;

import elfEngine.basic.node.particle.modifier.MathHelper;
import elfEngine.opengl.GLHelper;

public class GenieTest extends ElfNode {

	static final int MAX = 1000;
	static float[] sDistanceTable = new float[MAX + 1];
	static {
		for (int i = 0; i < sDistanceTable.length; i++) {
			final float r = ((float) i) / (sDistanceTable.length - 1f);
			sDistanceTable[i] = (float) (Math.sqrt(r - r * r) + Math.asin(Math.sqrt(1 - r)));
		}
	}

	static float distanceToTime(float distance) {
		for (int i = 0; i < sDistanceTable.length; i++) {
			final float r = ((float) i) / (sDistanceTable.length - 1f);
			if (r > distance) {
				return sDistanceTable[i];
			}
		}
		return 1;
	}

	static float timeToDistane(float time) {
		for (int i = 0; i < sDistanceTable.length; i++) {
			if (sDistanceTable[i] < time) {
				return ((float) i) / MAX;
			}
		}
		return 1;
	}

	public GenieTest(ElfNode father, int ordinal) {
		super(father, ordinal);

		calc(0);
	}

	protected ElfPointi mGridSize = new ElfPointi(50, 50);
	protected float mBlockSize = 10;
	protected ElfPointf[][] mGridPoints = new ElfPointf[51][51];

	protected float mProgress = 0;
	protected float mLife = 1;

	public void setProgress(float progress) {
		mProgress = progress;
	}

	public float getProgress() {
		return mProgress;
	}

	protected final static int REF_Progress = DEFAULT_SHIFT;

	public void setBlockSize(float size) {
		mBlockSize = size;
	}

	public float getBlockSize() {
		return mBlockSize;
	}

	protected final static int REF_BlockSize = DEFAULT_SHIFT;

	protected ElfPointf mTarget = new ElfPointf();

	public void setTarget(ElfPointf size) {
		mTarget = size;
	}

	public ElfPointf getTarget() {
		return mTarget;
	}

	protected final static int REF_Target = DEFAULT_SHIFT;

	protected ElfPointf mControlPoint = new ElfPointf();

	/***
	 * 重力吸引
	 * http://hiphotos.baidu.com/shaoyx/pic/item/e59a3e34edf59e54241f14fb.jpg
	 */

	float calcMaxDistance() {
		float ret = 0;
		for (int i = 0; i <= mGridSize.x; i++) {
			for (int j = 0; j <= mGridSize.y; j++) {
				final ElfPointf init = getPosInit(i, j);
				final float dx = init.x - mTarget.x;
				final float dy = init.y - mTarget.y;
				final float d = MathHelper.sqrt(dx * dx + dy * dy);
				ret = Math.max(d, ret);
			}
		}
		return ret;
	}

	final static float SK = (float) Math.PI / 2;

	public void calc(float dt) {
		super.calc(dt);
		final float maxDis = calcMaxDistance();
		final float rate = mProgress / mLife;

		for (int i = 0; i <= mGridSize.x; i++) {
			for (int j = 0; j <= mGridSize.y; j++) {
				final ElfPointf init = getPosInit(i, j);
				final float dx = init.x - mTarget.x;
				final float dy = init.y - mTarget.y;
				final float dxy = MathHelper.sqrt(dx * dx + dy * dy);

				float drs3 = (float) Math.sqrt(dxy / maxDis);
				drs3 = 1 / (drs3 * drs3 * drs3);

				final float k = SK * drs3;
				final float t = k * rate;

				final float dr = timeToDistane(t);

				setPos(i, j, mTarget.x + dx * dr, mTarget.y + dy * dr);
			}
		}
	}

	//

	public void drawSelf() {
		GLHelper.glBindTexture(0);

		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glLineWidth(1);
		GL11.glDisable(GL11.GL_LINE_STIPPLE);

		GL11.glBegin(GL11.GL_LINES);
		for (int i = 0; i <= mGridSize.x; i++) {
			for (int j = 0; j <= mGridSize.y; j++) {
				if (i < mGridSize.x) {
					final ElfPointf p1 = getPos(i, j);
					final ElfPointf p2 = getPos(i + 1, j);
					GL11.glVertex3f(p1.x, p1.y, 0);
					GL11.glVertex3f(p2.x, p2.y, 0);
				}

				if (j < mGridSize.y) {
					final ElfPointf p1 = getPos(i, j);
					final ElfPointf p2 = getPos(i, j + 1);
					GL11.glVertex3f(p1.x, p1.y, 0);
					GL11.glVertex3f(p2.x, p2.y, 0);
				}
			}
		}
		GL11.glEnd();
	}

	ElfPointf getPos(int i, int j) {
		return mGridPoints[i][j];
	}

	void setPos(int i, int j, float x, float y) {
		mGridPoints[i][j] = new ElfPointf(x, y);
	}

	ElfPointf getPosInit(int i, int j) {
		return new ElfPointf(mBlockSize * (-mGridSize.x / 2f + i), mBlockSize * (mGridSize.y / 2f - j));
	}
}
