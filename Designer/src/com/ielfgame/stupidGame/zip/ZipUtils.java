package com.ielfgame.stupidGame.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public class ZipUtils {

	/**
	 * 使用GBK编码可以避免压缩中文文件名乱码
	 */
	private static final String CHINESE_CHARSET = "GBK";

	/**
	 * 文件读取缓冲区大小
	 */
	private static final int CACHE_SIZE = 1024;

	private final static ZipEntry initZipEntry(final ZipEntry ze, boolean isFile) {
		ze.setTime(0);
		ze.setCrc(0xff);
		ze.setComment("");
		ze.setExternalAttributes(0);
		ze.setInternalAttributes(0);
		if (isFile) {
			ze.setUnixMode(644);
		} else {
			ze.setUnixMode(755);
		}
		return ze;
	}

	private final static ZipOutputStream initZipOutputStream(ZipOutputStream zos) {
		zos.setComment("code");
		zos.setEncoding(CHINESE_CHARSET);
		zos.setMethod(ZipOutputStream.DEFLATED);
		zos.setLevel(ZipOutputStream.DEFAULT_COMPRESSION);
		// zos.setLevel(level);
		// zos.STORED
		// 解决中文文件名乱码
		return zos;
	}

	/**
	 * <p>
	 * 压缩文件
	 * </p>
	 * 
	 * @param sourceFolder
	 *            压缩文件夹
	 * @param zipFilePath
	 *            压缩文件输出路径
	 * @throws Exception
	 */
	public static void zip(String sourceFolder, final String zipFilePath) throws Exception {
		OutputStream out = new FileOutputStream(zipFilePath);
		BufferedOutputStream bos = new BufferedOutputStream(out);
		ZipOutputStream zos = new ZipOutputStream(bos);
		initZipOutputStream(zos);

		File file = new File(sourceFolder);
		String basePath = null;
		if (file.isDirectory()) {
			basePath = file.getPath();
		} else {
			basePath = file.getParent();
		}
		zipFile(file, basePath, zos);
		zos.closeEntry();
		zos.close();
		bos.close();
		out.close();
	}

	private static void zipFile(File singleFile, String basePath, final ZipOutputStream zos) throws Exception {
		File[] files = new File[0];
		if (singleFile.isDirectory()) {
			files = singleFile.listFiles();
		} else {
			files = new File[1];
			files[0] = singleFile;
		}
		String pathName;
		InputStream is;
		BufferedInputStream bis;
		byte[] cache = new byte[CACHE_SIZE];
		for (File file : files) {
			if (file.isDirectory()) {
				pathName = file.getPath().substring(basePath.length() + 1) + "/";
				final ZipEntry ze = new ZipEntry(pathName);
				initZipEntry(ze, false);
				zos.putNextEntry(ze);
				zipDir(file, basePath, zos);
			} else {
				pathName = file.getPath().substring(basePath.length() + 1);
				is = new FileInputStream(file);
				bis = new BufferedInputStream(is);
				final ZipEntry ze = new ZipEntry(pathName);
				initZipEntry(ze, true);
				zos.putNextEntry(ze);
				int nRead = 0;
				while ((nRead = bis.read(cache, 0, CACHE_SIZE)) != -1) {
					zos.write(cache, 0, nRead);
				}
				bis.close();
				is.close();
			}
		}
	}

	/**
	 * <p>
	 * 递归压缩文件
	 * </p>
	 * 
	 * @param parentFile
	 * @param basePath
	 * @param zos
	 * @throws Exception
	 */
	private static void zipDir(File parentFile, String basePath, ZipOutputStream zos) throws Exception {
		File[] files = new File[0];
		if (parentFile.isDirectory()) {
			files = parentFile.listFiles();
		} else {
			files = new File[1];
			files[0] = parentFile;
		}
		String pathName;
		InputStream is;
		BufferedInputStream bis;
		byte[] cache = new byte[CACHE_SIZE];
		for (File file : files) {
			if (file.isDirectory()) {
				pathName = file.getPath().substring(basePath.length() + 1) + "/";
				final ZipEntry ze = new ZipEntry(pathName);
				initZipEntry(ze, false);
				zos.putNextEntry(ze);
				zipFile(file, basePath, zos);
			} else {
				pathName = file.getPath().substring(basePath.length() + 1);
				is = new FileInputStream(file);
				bis = new BufferedInputStream(is);
				final ZipEntry ze = new ZipEntry(pathName);
				initZipEntry(ze, true);
				zos.putNextEntry(ze);
				int nRead = 0;
				while ((nRead = bis.read(cache, 0, CACHE_SIZE)) != -1) {
					zos.write(cache, 0, nRead);
				}
				bis.close();
				is.close();
			}
		}
	}

	/**
	 * <p>
	 * 解压压缩包
	 * </p>
	 * 
	 * @param zipFilePath
	 *            压缩文件路径
	 * @param destDir
	 *            压缩包释放目录
	 * @throws Exception
	 */
	public static void unZip(String zipFilePath, String destDir) throws Exception {
		ZipFile zipFile = new ZipFile(zipFilePath, CHINESE_CHARSET);
		Enumeration<?> emu = zipFile.getEntries();
		BufferedInputStream bis;
		FileOutputStream fos;
		BufferedOutputStream bos;
		File file, parentFile;
		ZipEntry entry;
		byte[] cache = new byte[CACHE_SIZE];
		while (emu.hasMoreElements()) {
			entry = (ZipEntry) emu.nextElement();
			if (entry.isDirectory()) {
				new File(destDir + entry.getName()).mkdirs();
				continue;
			}
			bis = new BufferedInputStream(zipFile.getInputStream(entry));

			file = new File(destDir + entry.getName());
			parentFile = file.getParentFile();
			if (parentFile != null && (!parentFile.exists())) {
				parentFile.mkdirs();
			}
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos, CACHE_SIZE);
			int nRead = 0;
			while ((nRead = bis.read(cache, 0, CACHE_SIZE)) != -1) {
				fos.write(cache, 0, nRead);
			}
			bos.flush();
			bos.close();
			fos.close();
			bis.close();
		}
		zipFile.close();
	}
}
