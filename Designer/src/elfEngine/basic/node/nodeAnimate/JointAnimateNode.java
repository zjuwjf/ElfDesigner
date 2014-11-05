package elfEngine.basic.node.nodeAnimate;

import java.io.File;
import java.util.LinkedList;

import com.ielfgame.stupidGame.ResJudge;
import com.ielfgame.stupidGame.data.StringHelper;

import elfEngine.basic.node.ElfNode;

public class JointAnimateNode extends ElfNode {
	private final static int STEP_LEN = 3;

	private final String[][] mResidArrays = new String[STEP_LEN][];
	private final String[] mFolderArray = new String[STEP_LEN];
	private final int[] mStepLoopArray = new int[STEP_LEN];

	private float mProgressTime = 0;
	private float mFrameDelay = 0.1f;

	public JointAnimateNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		init();
	}

	private void init() {
		for (int i = 0; i < STEP_LEN; i++) {
			mResidArrays[i] = new String[0];
			mStepLoopArray[i] = 1;
		}
	}

	public void calc(float pMsElapsed) {
		super.calc(pMsElapsed);

		this.mProgressTime += pMsElapsed * 0.001f;

		final String resid = this.getResidByProgress(this.mProgressTime);
		this.setResid(resid);
	}

	public void setFrameDelay(float delay) {
		if (delay > 0) {
			mFrameDelay = delay;
		}
	}

	public float getFrameDelay() {
		return mFrameDelay;
	}

	protected final static int REF_FrameDelay = DEFAULT_SHIFT;

	public void setProgressTime(float p) {
		this.mProgressTime = p;
	}

	public float getProgressTime() {
		return this.mProgressTime;
	}
	protected final static int REF_ProgressTime = DEFAULT_FACE_SHIFT;

	public void setFolder0(String folder) {
		this.mFolderArray[0] = folder;
		this.setResidArray(getResidArrayByFolder(folder), 0);
	}

	public String getFolder0() {
		return this.mFolderArray[0];
	}

	protected final static int REF_Folder0 = DEFAULT_SHIFT | DROP_RESID_SHIFT;

	public void setFolder1(String folder) {
		this.mFolderArray[1] = folder;
		this.setResidArray(getResidArrayByFolder(folder), 1);
	}

	public String getFolder1() {
		return this.mFolderArray[1];
	}

	protected final static int REF_Folder1 = DEFAULT_SHIFT | DROP_RESID_SHIFT;

	public void setFolder2(String folder) {
		this.mFolderArray[2] = folder;
		this.setResidArray(getResidArrayByFolder(folder), 2);
	}

	public String getFolder2() {
		return this.mFolderArray[2];
	}

	protected final static int REF_Folder2 = DEFAULT_SHIFT | DROP_RESID_SHIFT;

	public void setResidArray0(String[] array) {
		this.setResidArray(array, 0);
	}

	public String[] getResidArray0() {
		return this.mResidArrays[0];
	}

	protected final static int REF_ResidArray0 = DEFAULT_SHIFT;

	public void setResidArray1(String[] array) {
		this.setResidArray(array, 1);
	}

	public String[] getResidArray1() {
		return this.mResidArrays[1];
	}

	protected final static int REF_ResidArray1 = DEFAULT_SHIFT;

	public void setResidArray2(String[] array) {
		this.setResidArray(array, 2);
	}

	public String[] getResidArray2() {
		return this.mResidArrays[2];
	}

	protected final static int REF_ResidArray2 = DEFAULT_SHIFT;

	public void setLoop0(int loop) {
		this.setLoop(loop, 0);
	}

	public int getLoop0() {
		return mStepLoopArray[0];
	}

	protected final static int REF_Loop0 = DEFAULT_SHIFT;

	public void setLoop1(int loop) {
		this.setLoop(loop, 1);
	}

	public int getLoop1() {
		return mStepLoopArray[1];
	}

	protected final static int REF_Loop1 = DEFAULT_SHIFT;

	public void setLoop2(int loop) {
		this.setLoop(loop, 2);
	}

	public int getLoop2() {
		return mStepLoopArray[2];
	}

	protected final static int REF_Loop2 = DEFAULT_SHIFT;
	
	public void setRestart() {
		this.mProgressTime = 0;
	}
	protected final static int REF_Restart = FACE_SET_SHIFT;

	private void setLoop(int loop, int index) {
		if (loop >= 0 && index < STEP_LEN) {
			mStepLoopArray[index] = loop;
		}
	}

	private void setResidArray(String[] array, int index) {
		if (array != null && index < STEP_LEN) {
			mResidArrays[index] = array;
		}
		this.mProgressTime = 0;
	}

	protected String getResidByProgress(float time) {
		int index = (int) (time / mFrameDelay);
		int totalF = 0;
		for (int i = 0; i < STEP_LEN; i++) {
			totalF += this.mStepLoopArray[i] * this.mResidArrays[i].length;
		}

		if (totalF <= 0) {
			return null;
		} else {
			index = index % totalF;
			for (int i = 0; i < STEP_LEN; i++) {
				final int stepLen = this.mStepLoopArray[i] * this.mResidArrays[i].length;
				if (stepLen <= 0) {
					continue;
				} else if (index < stepLen) {
					index = index % this.mResidArrays[i].length;
					return this.mResidArrays[i][index];
				} else {
					index -= stepLen;
				}
			}
		}
		return null;
	}

	private final static String[] getResidArrayByFolder(final String folder) {
		String[] ret = null;

		final File file = new File(folder);
		if (file.exists() && file.isDirectory()) {
			final String[] list = file.list();

			final LinkedList<String> idslist = new LinkedList<String>();
			for (int i = 0; i < list.length; i++) {
				final String id = list[i];
				if (ResJudge.isRes(id)) {
					idslist.add(id);
				}
			}
			ret = new String[idslist.size()];
			idslist.toArray(ret);
			
			StringHelper.sortByLastInt(ret);
		} else {
			ret = new String[0];
		}

		return ret;
	}

}
