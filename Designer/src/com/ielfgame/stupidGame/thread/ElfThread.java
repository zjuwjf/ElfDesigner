package com.ielfgame.stupidGame.thread;

import java.util.LinkedList;

public class ElfThread extends Thread {

	private final LinkedList<Runnable> mRunList = new LinkedList<Runnable>();
	private boolean mEnded;
	private final Object mLock = new Object();

	public void checkRun(final Runnable run) {
		synchronized (mLock) {
			mRunList.add(run);
		}
	}

	public void run() {
		while (true) {
			Runnable nextRun = null;
			synchronized (mLock) {
				if (!mRunList.isEmpty()) {
					nextRun = mRunList.removeFirst();
				}
			}

			if (nextRun != null) {
				nextRun.run();
			} else {
				mEnded = true;
			} 

			if (mEnded)
				break;
		}
	}

	public void end() {
		mEnded = true;
	}

}
