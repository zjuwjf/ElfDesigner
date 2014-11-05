package elfEngine.basic.node.nodeAnimate.flash;

public class CopyRegion {
	private int mBranchTop;
	private int mBranchBottom;
	private int mKeyTimeLeft;
	private int mKeyTimeRight;
	private boolean mCopyEnabled;

	public boolean getCopyEnabled() {
		return mCopyEnabled;
	}

	public void setCopyEnabled(boolean copyEnabled) {
		this.mCopyEnabled = copyEnabled;
	}

	public int getBranchTop() {
		return mBranchTop;
	}

	public void setBranchTop(int branchTop) {
		this.mBranchTop = branchTop;
	}

	public int getBranchBottom() {
		return mBranchBottom;
	}

	public void setBranchBottom(int branchBottom) {
		this.mBranchBottom = branchBottom;
	}

	public int getKeyTimeLeft() {
		return mKeyTimeLeft;
	}

	public void setKeyTimeLeft(int keyTimeLeft) {
		this.mKeyTimeLeft = keyTimeLeft;
	}

	public int getKeyTimeRight() {
		return mKeyTimeRight;
	}

	public void setKeyTimeRight(int keyTimeRight) {
		this.mKeyTimeRight = keyTimeRight;
	}
}
