package com.ielfgame.stupidGame.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ielfgame.stupidGame.trans.TransferRes;

public class FileHelper {

	public static final String DECOLLATOR;
	public static final boolean IS_WINDOWS;

	public enum FileType {
		FILE, DIR, NO
	}
	
	public static String getUserName() {
		final Map<String, String> map = System.getenv();
	    final String userName = map.get("USERNAME");// 获取用户名
	    return userName;
	}
	
	public static boolean isDir(String dir) {
		if (dir == null) {
			return false;
		}

		final File f = new File(dir);
		return f.exists() && f.isDirectory();
	}

	public static boolean isFile(String file) {
		if (file == null) {
			return false;
		}

		final File f = new File(file);
		return f.exists() && f.isFile();
	}

	public static FileType getFileType(final String path) {
		if (path != null) {
			final File f = new File(path);
			if (f.exists()) {
				if (f.isFile()) {
					return FileType.FILE;
				} else if (f.isDirectory()) {
					return FileType.DIR;
				}
			}
		}
		return FileType.NO;
	}

	static {
		final String sys = System.getProperty("os.name");
		IS_WINDOWS = sys.contains("Windows");
		if (IS_WINDOWS) {
			DECOLLATOR = "\\";
		} else {
			DECOLLATOR = "/";
		}
	}

	public static String replaceDecollators(String path) {
		if (IS_WINDOWS) {
			return path.replace("/", "\\");
		} else {
			return path.replace("\\", "/");
		}
	}
	
	public static boolean removeFile(final File file) {
		if(file.isDirectory()) {
			final File [] fs = file.listFiles();
			for(File f : fs) {
				removeFile(f);
			}
		}
		return file.delete();
	}
	
	public static void removeFile(final String file) {
		final boolean ret = removeFile(new File(file));
		if (!ret) { 
			System.err.println("delete " + file + " failed!");
		}
	}

	// no suffix
	public static String getSimpleNameNoSuffix(final String path) {
		if (path != null) {
			String ret = getSimpleName(path);

			final int index = ret.indexOf(".");

			if (index >= 0) {
				return ret.substring(0, index);
			}

			return ret;
		}
		return null;
	}

	public static String getSimpleNameWithSuffix(final String path) {
		if (path != null) {
			String ret = getSimpleName(path);
			return ret;
		}
		return null;
	}
	
	public static LinkedList<String> getFullPahIdsWithIgnore(final File file, final String subs[], final boolean deep) {
		final LinkedList<String> ret = new LinkedList<String>();
		if (file.exists()) {
			if (file.isDirectory()) {
				if(file.getName().startsWith(".")) {
					return ret;
				}
				
				final File[] fs = file.listFiles();
				if (deep) {
					for (File f : fs) {
						ret.addAll(getFullPahIdsWithIgnore(f, subs, deep));
					}
				} else {
					for (File f : fs) {
						if (!f.isDirectory()) {
							final String name = f.getAbsolutePath();
							if (subs != null) {
								for (String sub : subs) {
									if (name.endsWith(sub)) {
										ret.add(name);
										break;
									}
								}
							} else {
								ret.add(name);
							}
						}
					}
				}
			} else if (file.isFile()) {
				final String name = file.getAbsolutePath();
				if (subs != null) {
					for (String sub : subs) {
						if (name.endsWith(sub)) {
							ret.add(name);
							break;
						}
					}
				} else {
					ret.add(name);
				}
			}
		}

		return ret;
	}

	public static LinkedList<String> getFullPahIds(final File file, final String subs[], final boolean deep) {
		final LinkedList<String> ret = new LinkedList<String>();
		if (file.exists()) {
			if (file.isDirectory()) {
				final File[] fs = file.listFiles();
				if (deep) {
					for (File f : fs) {
						ret.addAll(getFullPahIds(f, subs, deep));
					}
				} else {
					for (File f : fs) {
						if (!f.isDirectory()) {
							final String name = f.getAbsolutePath();
							if (subs != null) {
								for (String sub : subs) {
									if (name.endsWith(sub)) {
										ret.add(name);
										break;
									}
								}
							} else {
								ret.add(name);
							}
						}
					}
				}
			} else if (file.isFile()) {
				final String name = file.getAbsolutePath();
				if (subs != null) {
					for (String sub : subs) {
						if (name.endsWith(sub)) {
							ret.add(name);
							break;
						}
					}
				} else {
					ret.add(name);
				}
			}
		}

		return ret;
	}

	public static LinkedList<String> getSimplePahIds(final String dir, final String[] subs, final boolean deep) {
		final File file = new File(dir);
		return getSimplePahIds(file, subs, deep);
	}

	public static LinkedList<String> getSimplePahIds(final File file, final String[] subs, final boolean deep) {
		final LinkedList<String> ret = new LinkedList<String>();
		if (file.exists()) {
			if (file.isDirectory()) {
				final File[] fs = file.listFiles();
				if (deep) {
					for (File f : fs) {
						ret.addAll(getSimplePahIds(f, subs, deep));
					}
				} else {
					for (File f : fs) {
						if (!f.isDirectory()) {
							ret.addAll(getSimplePahIds(f, subs, deep));
						}
					}
				}
			} else if (file.isFile()) {
				final String name = file.getName();
				if (subs != null) {
					for (String sub : subs) {
						if (name.endsWith(sub)) {
							ret.add(name);
							break;
						}
					}
				} else {
					ret.add(name);
				}
			}
		}

		return ret;
	}

	public static LinkedList<String> getFullPahIds(final String dir, final String[] subs, final boolean deep) {
		final File file = new File(dir);
		return getFullPahIds(file, subs, deep);
	}

	public static void main(String[] args) {
		final String dir = "D:\\LordRiseRes";
		final LinkedList<String> ret = getFullPathsDeep(dir, new String[] { "release", ".nvcd", "text-base" });
		for (String f : ret) {
			System.err.println(f);
		}
	}

	public static String getUserDir() {
		return System.getProperty("user.dir");
	}

	// @Deprecated
	public static String[] getFullPaths(final String dir) {
		boolean end = dir.endsWith(DECOLLATOR);
		final File f = new File(dir);
		if (f.exists() && f.isDirectory()) {
			final String[] ret = f.list();
			for (int i = 0; i < ret.length; i++) {
				if (end) {
					ret[i] = dir + ret[i];
				} else {
					ret[i] = dir + DECOLLATOR + ret[i];
				}
			}

			return ret;
		}
		return null;
	}

	public static String[] getSimplePaths(final String dir) {
		final File f = new File(dir);
		if (f.exists() && f.isDirectory()) {
			final String[] ret = f.list();
			return ret;
		}
		return new String[0];
	}

	public static LinkedList<String> getFullPathsDeep(final String dir) {
		return getFullPathsDeep(dir, null);
	}

	public static LinkedList<String> getFullPathsDeep(final String dir, final String[] except) {
		final File f = new File(dir);

		final String simpleName = f.getName();

		final LinkedList<String> ret = new LinkedList<String>();
		if (except != null && simpleName != null) {
			for (String exc : except) {
				if (simpleName.equals(exc)) {
					return ret;
				}
			}
		}

		if (f.exists()) {
			if (f.isDirectory()) {
				final String[] paths = getFullPaths(dir);
				for (String path : paths) {
					ret.addAll(getFullPathsDeep(path, except));
				}
			} else {
				ret.add(f.getAbsolutePath());
			}
		}
		return ret;
	}

	public static String getSimpleName(final String path) {
		if (path == null) {
			return null;
		}
		return new File(path).getName();
	}

	public static BufferedReader getReader(final String path) {
		return getReader(new File(path));
	}

	public static BufferedReader getReader(final File file) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reader;
	}
	
	public static BufferedReader getUTF8Reader(final String path) {
		return getUTF8Reader(new File(path));
	}
	
	public static BufferedReader getUTF8Reader(final File file) {
		BufferedReader reader = null;
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
			reader = new BufferedReader(isr);
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return reader;
	}

	public static BufferedReader getReader(final Class<?> _class, final String resName) {
		final InputStream stream = _class.getResourceAsStream(resName);
		final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

		return reader;
	}
	
	//final Reader reader = new BufferedReader(new InputStreamReader(pInputStream, "UTF-8"));
	public static BufferedReader getUTF8Reader(final Class<?> _class, final String resName) {
		final InputStream stream = _class.getResourceAsStream(resName);
		try {
			final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			return reader;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static BufferedWriter getWriter(final String path) {
		return getWriter(new File(path));
	}
	
	public static BufferedWriter getUTF8Writer(final String dir, String file) {
		file = FileHelper.getSimpleNameWithSuffix(file);
		
		final String full = searchFullPath(dir, file);
		if(full != null) {
			return getUTF8Writer(new File(full));
		} else {
			return getUTF8Writer(new File(new File(dir), file));
		}
	}
	
	public static String searchFullPath(final String dir, final String file) {
		final String [] path = new String[1];
		travelFiles(dir, new IteratorFile() {
			public boolean iterator(File file) {
				if(file.getName().equals(file)) {
					path[0] = file.getAbsolutePath();
					return true;
				}
				return false;
			}
		});
		return path[0];
	}

	public static BufferedWriter getWriter(final File file) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer;
	}
	
	public static BufferedWriter getUTF8Writer(String path) {
		return getUTF8Writer(new File(path));
	}
	
	public static BufferedWriter getUTF8Writer(final File file) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer;
	}

	public static String readBufferedReader(BufferedReader br) throws IOException {
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		
		try {
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	public static LinkedList<String> utf8Read(final File file) {
		final LinkedList<String> ret = new LinkedList<String>();
		final BufferedReader reader = getUTF8Reader(file);
		if (reader != null) {
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					ret.add(line);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return ret;
	}

	public static LinkedList<String> read(final File file) {
		final LinkedList<String> ret = new LinkedList<String>();
		final BufferedReader reader = getReader(file);
		if (reader != null) {
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					ret.add(line);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return ret;
	}
	
	//utf8Read
	public static LinkedList<String> utf8read(final InputStream is) {
		final LinkedList<String> ret = new LinkedList<String>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			if (reader != null) {
				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						ret.add(line);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return ret;
	}

	public static LinkedList<String> read(final InputStream is) {
		final LinkedList<String> ret = new LinkedList<String>();
		final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		if (reader != null) {
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					ret.add(line);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return ret;
	}

	public static void write(final List<String> lines, final File file) {
		final BufferedWriter write = getWriter(file);
		if (write != null) {
			try {
				for (String line : lines) {
					write.write(line + "\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					write.flush();
					write.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void writeUTF8(final List<String> lines, final File file) {
		final BufferedWriter write = getUTF8Writer(file);
		if (write != null) {
			try {
				for (String line : lines) {
					write.write(line + "\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					write.flush();
					write.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String readInputStream(InputStream in) throws IOException {
		return readBufferedReader(new BufferedReader(new InputStreamReader(in)));
	}

	public static String removeFileSubfix(String path) {
		if (path.lastIndexOf('.') >= 0) {
			return path.substring(0, path.lastIndexOf('.'));
		} else {
			return path;
		}
	}

	public static String fileSubfix(String path) {
		if (path.lastIndexOf('.') > 0) {
			return path.substring(path.lastIndexOf('.') + 1);
		} else {
			return "";
		}
	}

	public static String removeSubfix(String path) {
		final int index = path.lastIndexOf('.');
		if (index > 0) {
			return path.substring(0, index);
		} else {
			return path;
		}
	}

	public static String removeAllSubfix(String path) {
		final int index = path.indexOf('.');
		if (index > 0) {
			return path.substring(0, index);
		} else {
			return path;
		}
	}

	public static String getDirPath(final String path) {
		if(path == null) {
			return null;
		} else {
			final File f = new File(path).getParentFile();
			if(f!=null) {
				return f.getAbsolutePath();
			} 
		} 
		
		final int index1 = path.lastIndexOf("\\");
		final int index2 = path.lastIndexOf("/");
		int index = Math.max(index1, index2);
		return index < 0 ? path : path.substring(0, index);
	}

	public static String replaceFileSubfix(String path, String replaceSubfix) {
		if (path.lastIndexOf('.') >= 0) {
			return path.substring(0, path.lastIndexOf('.') + 1) + replaceSubfix;
		} else {
			return path;
		}
	}

	// public static interface IStringMatch {
	// public boolean match(final String str1, final String str2);
	// }

	public static void removeFile(final String name, final IFileMatch match) {
		final File file = new File(name);
		if (match.isFileMatch(file)) {
			boolean ret = file.delete();
			System.err.println("remove : " + file.getAbsolutePath() + " " + ret);
		} else if (file.isDirectory()) {
			final String[] list = file.list();
			for (String l : list) {
				removeFile(name + DECOLLATOR + l, match);
			}
		}
	}

	public static interface IteratorFile {
		public boolean iterator(final File file);
	}

	public static void travelFiles(final String path, final IteratorFile it) {
		final File file = new File(path);
		travelFiles(file, it);
	}
	
	public static void travelFiles(final File file, final IteratorFile it) {
		if (file.exists()) {
			if (!it.iterator(file)) {
				final File[] list = file.listFiles();
				if (list != null) {
					for (File f : list) {
						travelFiles(f, it);
					}
				}
			}
		}
	}

	public static void replaceFileKey(final String path, final ArrayList<String> keys, final ArrayList<String> replaces) {
		final BufferedReader br = getReader(path);
		final LinkedList<String> allLines = new LinkedList<String>();
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				int size = keys.size();
				for (int i = 0; i < size; i++) {
					line = line.replace(keys.get(i), replaces.get(i));
				}
				allLines.add(line);
			}

			br.close();

			try {
				final BufferedWriter bw = getWriter(path);
				for (String text : allLines) {
					bw.write(text + "\n");
				}
				bw.flush();
				bw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String num2Str(final int num, final int bits) {
		return String.format("%s%d", chars(bits - num2Bits(num), '0'), num);
	}

	public static int num2Bits(final int num) {
		return String.format("%d", num).length();
	}

	public static String chars(final int num, final char c) {
		final char[] cs = new char[num];
		Arrays.fill(cs, c);
		return String.valueOf(cs);
	}

	static boolean isSubfix(String path, String[] subfix) {
		if (subfix != null && path != null) {
			final String sub = fileSubfix(path);
			for (final String s : subfix) {
				if (sub.endsWith(s)) {
					return true;
				}
			}
		}

		if (subfix == null) {
			return true;
		}

		return false;
	}

	public static void copyDeep(final String source, final String dest, final String[] sourceSubfix) {
		copyDeep(source, dest, sourceSubfix, null);
	}

	public static interface IFileMatch {
		public boolean isFileMatch(File file);
	}

	public static void copyDeep(final String source, final String dest, final String[] sourceSubfix, final IFileMatch match) {
		final File sourceFile = new File(source);

		if (match != null) {
			if (!match.isFileMatch(sourceFile)) {
				return;
			}
		}

		if (sourceFile.isFile()) {
			if (isSubfix(source, sourceSubfix)) {
				final File destFile = new File(dest);
				TransferRes.copyFileQuickly(sourceFile, destFile.getParentFile(), destFile.getName());
			}
		} else if (sourceFile.isDirectory()) {
			final String[] list = sourceFile.list();
			for (final String child : list) {
				copyDeep(source + DECOLLATOR + child, dest + DECOLLATOR + child, sourceSubfix, match);
			}
		}
	}

	public static void copyDeepBut(final String source, final String dest, final String[] sourceSubfix) {
		copyDeepBut(source, dest, sourceSubfix, null);
	}

	public static void copyDeepBut(final String source, final String dest, final String[] sourceSubfix, final IFileMatch match) {
		final File sourceFile = new File(source);

		if (match != null) {
			if (!match.isFileMatch(sourceFile)) {
				return;
			}
		}

		if (sourceFile.isFile()) {
			if (!isSubfix(source, sourceSubfix)) {
				final File destFile = new File(dest);
				TransferRes.copyFileQuickly(sourceFile, destFile.getParentFile(), destFile.getName());
			}
		} else if (sourceFile.isDirectory()) {
			final String[] list = sourceFile.list();
			for (final String child : list) {
				copyDeepBut(source + DECOLLATOR + child, dest + DECOLLATOR + child, sourceSubfix, match);
			}
		}
	}

	/***
	 * @param pathA
	 * @param pathB
	 * @return true if same
	 */
			
	public static boolean compare2files(final String pathA, final String pathB) {
		if (pathA == null || pathB == null) {
			return false;
		}

		if (pathA.equals(pathB)) {
			return true;
		}

		final File fileA = new File(pathA);
		final File fileB = new File(pathB);
		
		if(!fileA.exists() || !fileB.exists() || fileA.isDirectory() || fileB.isDirectory()) {
			return false;
		}
		
		FileInputStream i1 = null, i2 = null;
		try {
			i1 = new FileInputStream(fileA);
			i2 = new FileInputStream(fileB);
		} catch (Exception e) {
		}
		
		ReadableByteChannel ch1 = Channels.newChannel(i1);
		ReadableByteChannel ch2 = Channels.newChannel(i2);

		ByteBuffer buf1 = ByteBuffer.allocateDirect(1024);
		ByteBuffer buf2 = ByteBuffer.allocateDirect(1024);

		try {
			while (true) {
				int n1 = ch1.read(buf1);
				int n2 = ch2.read(buf2);

				if (n1 == -1 || n2 == -1)
					return n1 == n2;

				buf1.flip();
				buf2.flip();

				for (int i = 0; i < Math.min(n1, n2); i++)
					if (buf1.get() != buf2.get())
						return false;
				
				buf1.compact();
				buf2.compact();
			}

		} catch (Exception e) {
		} finally {
			if (i1 != null) {
				try {
					i1.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if (i2 != null) {
				try {
					i2.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return true;
	}
	
	public static void copyQuickly(final File source, final File dest) {
		if(source.isFile()) { 
			copyFileQuickly(source, dest.getParentFile(), dest.getName());
		} else if(source.isDirectory()) { 
			dest.mkdirs();
			final File [] fs = source.listFiles();
			for(final File f : fs) { 
				copyQuickly(f, new File(dest, f.getName()));
			} 
		} 
	}

	public static long copyFileQuickly(File srcFile, File destDir, String newFileName) {
		long copySizes = 0;
		if (!srcFile.exists()) {
			System.err.println("copyFileQuickly src file did not existed!");
			copySizes = -1;
		} else if (newFileName == null) {
			System.err.println("new file name null");
			copySizes = -1;
		} else {
			if (!destDir.exists()) {
				destDir.mkdirs();
			}

			try {
				final String src = srcFile.getAbsolutePath();
				final String dst = destDir.getAbsolutePath() + FileHelper.DECOLLATOR + newFileName;

				if (src.equals(dst)) {
					return 1;
				}

				final File destFile = new File(destDir, newFileName);

				final FileChannel fcin = new FileInputStream(srcFile).getChannel();
				final FileChannel fcout = new FileOutputStream(destFile).getChannel();
				final long size = fcin.size();
				fcin.transferTo(0, fcin.size(), fcout);
				fcin.close();
				fcout.close();
				copySizes = size;
				// System.err.println(String.format("Src=%s,Dst=%s", src, dst));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return copySizes;
	}
	
	public static LinkedList<String> readUTF8(final File file) {
		final LinkedList<String> ret = new LinkedList<String>();
		final BufferedReader reader = getUTF8Reader(file);
		if (reader != null) {
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					ret.add(line);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return ret;
	}

	public static void contentReplace(final File file, final String r1, final String v1) {
		final LinkedList<String> list = readUTF8(file);
		final LinkedList<String> outList = new LinkedList<String>();
		
		for(String line : list) {
			outList.add( line.replace( r1, v1) );
		}
		
		writeUTF8(outList, file);
	}
}
