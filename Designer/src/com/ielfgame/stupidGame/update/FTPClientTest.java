package com.ielfgame.stupidGame.update;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.ielfgame.stupidGame.data.StringHelper;

public class FTPClientTest {
	
//	private final static String REMOTE_FILE_URL = "ftp://share:glee@192.168.1.158/Soft/MainDesigner/MainDesigner-win32.jar";
//	private final static int BUFFER = 1024;
	
	/**
	 * Description: 向FTP服务器上传文件
	 * @param url FTP服务器hostname
	 * @param port FTP服务器端口
	 * @param username FTP登录账号
	 * @param password FTP登录密码
	 * @param path FTP服务器保存目录
	 * @param filename 上传到FTP服务器上的文件名
	 * @param input 输入流
	 * @return 成功返回true，否则返回false
	 */
	public static boolean uploadFile(String url,int port,String username, String password, String path, String filename, InputStream input) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(url, port);//连接FTP服务器
			//如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);//登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			ftp.changeWorkingDirectory(path);
			ftp.storeFile(filename, input);			
			
			input.close();
			ftp.logout();
			success = true;
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
		return success;
	}
	
	/**
	 * Description: 从FTP服务器下载文件
	 * @param url FTP服务器hostname
	 * @param port FTP服务器端口
	 * @param username FTP登录账号
	 * @param password FTP登录密码
	 * @param remotePath FTP服务器上的相对路径
	 * @param fileName 要下载的文件名
	 * @param localPath 下载后保存到本地的路径
	 * @return
	 */
	public static boolean downFile(String url, int port,String username, String password, String remotePath,String fileName,String localPath) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(url, port);
			//如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);//登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			} 
			ftp.changeWorkingDirectory(remotePath);//转移到FTP服务器目录
			FTPFile[] fs = ftp.listFiles();
			
//			final FTPFile newFTPFile;
			if(fs != null && fs.length > 0) { 
				Arrays.sort(fs, new Comparator<FTPFile>() {
					public int compare(FTPFile o1, FTPFile o2) {
						final String s1 = o1.getName();
						final String s2 = o2.getName();
						return StringHelper.compareStringByLastInt(s1, s2);
					}
				});
//				newFTPFile = fs[fs.length-1];
			} else {
//				newFTPFile = null;
			} 
			
			for(FTPFile ff:fs){
				if(ff.getName().equals(fileName)){ 
					System.err.println("start:"+System.currentTimeMillis()); 
					System.err.println("totalsize="+(ff.getSize()/1024));
					final File localFile = new File(localPath+"/"+ff.getName()); 
					
					final Thread thread = new Thread(new Runnable() {
						public void run() { 
							while(true) { 
								try { 
									Thread.sleep(500); 
									System.err.println("size="+( new FileInputStream(localFile).available()/1024 ));
								} catch (Exception e) { 
									e.printStackTrace();
								} 
							} 
						} 
					});
					thread.start();
					
					OutputStream is = new FileOutputStream(localFile); 
					ftp.retrieveFile(ff.getName(), is);
					is.close();
					System.err.println("end:"+System.currentTimeMillis());
				}
			}
			
			ftp.logout();
			success = true;
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
		return success;
	}

	public static void main(String[] args) { 
		downFile("192.168.1.158", 21, "share", "glee", "/Soft/MainDesigner/", "MainDesigner-win32.jar", "D:\\ftp\\");
	}
}
