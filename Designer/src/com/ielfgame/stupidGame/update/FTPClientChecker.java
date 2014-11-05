package com.ielfgame.stupidGame.update;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Shell;

import com.ielfgame.stupidGame.data.StringHelper;
import com.ielfgame.stupidGame.dialog.MessageDialog;
import com.ielfgame.stupidGame.dialog.ProgressDialog;
import com.ielfgame.stupidGame.shellRunner.ShellRunner;
import com.ielfgame.stupidGame.utils.FileHelper;

public class FTPClientChecker implements Runnable {

	private ProgressDialog mProgressDialog;
	private UpdateServerConfig mUpdateServerConfig;
	private File mLocalFile;
	private long mLocalFileLastSize;
	private long mFTPFileTotalSize;
	private final Shell mShell = new Shell();
	private Thread mFTPThread;

	private boolean mCheckFinished;

	private synchronized void setCheckFinished(boolean b) {
		mCheckFinished = b;
	}

	public void checkThenGo() {
		mUpdateServerConfig = new UpdateServerConfig();

		mUpdateServerConfig.url = "192.168.1.158";
		mUpdateServerConfig.port = 21;
		mUpdateServerConfig.user = "share";
		mUpdateServerConfig.password = "glee";
		mUpdateServerConfig.remotePath = "/Soft/MainDesigner/";
		mUpdateServerConfig.fileName = "MainDesigner";

		mProgressDialog = new ProgressDialog();
		
		mShell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				System.exit(1);
			}
		});
		
		mFTPThread = new Thread(new Runnable() {
			public void run() {
				check();
			}
		});
		mFTPThread.start();

		mProgressDialog.open(mShell, "Designer Updating...");
	}

	private void asyncExec(Runnable run) { 
		mShell.getDisplay().asyncExec(run);
	} 
	
	private void asyncExec(final Runnable run, final int milliseconds) {
		asyncExec(new Runnable() {
			public void run() {
				mShell.getDisplay().timerExec(milliseconds, run);
			}
		});
	}

	private String getCurrentDesignerJarName() {
		final String dir = FileHelper.getUserDir();
		final File[] fs = new File(dir).listFiles();
		Arrays.sort(fs, new Comparator<File>() {
			public int compare(File o1, File o2) {
				return StringHelper.compareStringByLastInt(o1.getName(), o2.getName());
			}
		});

		for (int i = fs.length - 1; i >= 0; i--) {
			final String name = fs[i].getName();
			if (name.startsWith(mUpdateServerConfig.fileName)) {
				return name;
			}
		}

		return null;
	}

	private String readJumpCmd() {
		final BufferedReader br = FileHelper.getReader(FileHelper.getUserDir() + FileHelper.DECOLLATOR + "jump");
		try {
			return br.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private final void check() {
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(mUpdateServerConfig.url, mUpdateServerConfig.port);
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(mUpdateServerConfig.user, mUpdateServerConfig.password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();

				asyncExec(new Runnable() {
					public void run() {
						showServerWaringThenGo("Could not connect with server!");
					}
				});
				setCheckFinished(true);
				return;
			}
			ftp.changeWorkingDirectory(mUpdateServerConfig.remotePath);// 转移到FTP服务器目录
			FTPFile[] ftpfs = ftp.listFiles();

			FTPFile newFTPFile = null;
			if (ftpfs != null && ftpfs.length > 0) {
				Arrays.sort(ftpfs, new Comparator<FTPFile>() {
					public int compare(FTPFile o1, FTPFile o2) {
						final String s1 = o1.getName();
						final String s2 = o2.getName();
						return StringHelper.compareStringByLastInt(s1, s2);
					}
				});

				for (int i = ftpfs.length - 1; i >= 0; i++) {
					final String name = ftpfs[i].getName();
					if (name.startsWith(mUpdateServerConfig.fileName)) {
						newFTPFile = ftpfs[i];
						break;
					}
				}
			}

			if (newFTPFile != null) {
				final int serverVersion = StringHelper.readLastInt(newFTPFile.getName());

				final String dir = FileHelper.getUserDir();
				mFTPFileTotalSize = newFTPFile.getSize();
				mLocalFile = new File(dir + "/" + newFTPFile.getName());

				int localFileSize = 0;
				try {
					localFileSize = new FileInputStream(mLocalFile).available();
				} catch (Exception e) {
				}

				if (serverVersion != StringHelper.readLastInt(getCurrentDesignerJarName()) || localFileSize != mFTPFileTotalSize) {
					// final Thread thread = new Thread(this);
					// thread.start();
					asyncExec(this);

					OutputStream is = new FileOutputStream(mLocalFile);
					ftp.retrieveFile(newFTPFile.getName(), is);
					is.close();
				} else {
					asyncExec(new Runnable() {
						public void run() {
							showServerWaringThenGo("No need to update!");
						}
					});
					setCheckFinished(true);
					return;
				}
			} else {
				asyncExec(new Runnable() {
					public void run() {
						showServerWaringThenGo("Server has no Main_Jar!");
					}
				});
				setCheckFinished(true);
				return;
			}

			ftp.logout();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}

		setCheckFinished(true);

		asyncExec(new Runnable() {
			public void run() {
				go2jar(mLocalFile.getName());
			}
		});
	}

	private final void showServerWaringThenGo(final String warning) {
		mProgressDialog.setProgress(warning, "Going to Main Program", 100, 100);
		go2jar(getCurrentDesignerJarName());
	}

	private final void showErrorAndExit(final String error) {
		final MessageDialog message = new MessageDialog(mShell);
		message.open("Error!", error, SWT.ICON_INFORMATION | SWT.OK);
		mProgressDialog.close();
	}

	private final void go2jar(final String jarName) {
		if (jarName == null) {
			showErrorAndExit("MainJar is null!");
		}

		final String jumpCmd = readJumpCmd();
		if (jumpCmd == null) {
			showErrorAndExit("No Jump Command!");
		}

		mProgressDialog.setProgressDirectly("download completed!", "", 100, 100);

		final Thread thread = new Thread(new Runnable() {
			public void run() {
				
				asyncExec(new Runnable() {
					public void run() {
						mProgressDialog.close();
					}
				}, 10000);
				
				final ShellRunner sr = new ShellRunner();
				final String cmd = jumpCmd.replace("MAIN_JAR", jarName);
				sr.setCmdline(cmd);
				
				final int runRet = sr.run();
				if (runRet != 0) {
					asyncExec(new Runnable() { 
						public void run() { 
							final MessageDialog message = new MessageDialog(new Shell());
							message.open("Error!", "Nonexcutable Jump Command :\n" + cmd, SWT.ICON_INFORMATION | SWT.OK);
						} 
					});
				} 
			} 
		});
		thread.start();
	}

	public static void main(final String[] args) {
		new FTPClientChecker().checkThenGo();
	}
	
	public synchronized void run() {
		try { 
			if (!mCheckFinished) { 
				try { 
					final long nowSize = new FileInputStream(mLocalFile).available();
					mProgressDialog.setProgressDirectly("downloading:" + mLocalFile.getName(), (nowSize - mLocalFileLastSize) * 10 / 1024 + "KB/s", (int) (nowSize / 1024), (int) (mFTPFileTotalSize / 1024));
					mLocalFileLastSize = nowSize;
				} catch (Exception e) {
//					e.printStackTrace();
				} 
				mShell.getDisplay().timerExec(100, this);
			} 
		} catch (Exception e) { 
			e.printStackTrace();
		} 
	}
}
