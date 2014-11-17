package com.ielfgame.stupidGame.crypto;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/* XXTEA encryption arithmetic library.
 *
 * Copyright: Ma Bingyao <andot@ujn.edu.cn>
 * Version: 3.0.2
 * LastModified: Apr 12, 2010
 * This library is free.  You can redistribute it and/or modify it under GPL.
 */

public final class XXTEA {

	private static final int delta = 0x9E3779B9;

	private XXTEA() {
	}

	private static final int MX(int sum, int y, int z, int p, int e, int[] k) {
		return (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
	}
	
	/**
	 * Encrypt data with key.
	 * 
	 * @param data
	 * @param key
	 * @return
	 */
	public static final byte[] encrypt(byte[] data, byte[] key) {
		if (data.length == 0) {
			return data;
		}
		return toByteArray(encrypt(toIntArray(data, true), toIntArray(key, false)), false);
	}

	/**
	 * Decrypt data with key.
	 * 
	 * @param data
	 * @param key
	 * @return
	 */
	public static final byte[] decrypt(byte[] data, byte[] key) {
		if (data.length == 0) {
			return data;
		}
		return toByteArray(decrypt(toIntArray(data, false), toIntArray(key, false)), true);
	}

	/**
	 * Encrypt data with key.
	 * 
	 * @param v
	 * @param k
	 * @return
	 */
	private static final int[] encrypt(int[] v, int[] k) {
		int n = v.length - 1;

		if (n < 1) {
			return v;
		}
		if (k.length < 4) {
			int[] key = new int[4];

			System.arraycopy(k, 0, key, 0, k.length);
			k = key;
		}
		int z = v[n], y = v[0], sum = 0, e;
		int p, q = 6 + 52 / (n + 1);

		while (q-- > 0) {
			sum = sum + delta;
			e = sum >>> 2 & 3;
			for (p = 0; p < n; p++) {
				y = v[p + 1];
				z = v[p] += MX(sum, y, z, p, e, k);
			}
			y = v[0];
			z = v[n] += MX(sum, y, z, p, e, k);
		}
		return v;
	}

	/**
	 * Decrypt data with key.
	 * 
	 * @param v
	 * @param k
	 * @return
	 */
	private static final int[] decrypt(int[] v, int[] k) {
		int n = v.length - 1;

		if (n < 1) {
			return v;
		}
		if (k.length < 4) {
			int[] key = new int[4];

			System.arraycopy(k, 0, key, 0, k.length);
			k = key;
		}
		int z = v[n], y = v[0], sum, e;
		int p, q = 6 + 52 / (n + 1);

		sum = q * delta;
		while (sum != 0) {
			e = sum >>> 2 & 3;
			for (p = n; p > 0; p--) {
				z = v[p - 1];
				y = v[p] -= MX(sum, y, z, p, e, k);
			}
			z = v[n];
			y = v[0] -= MX(sum, y, z, p, e, k);
			sum = sum - delta;
		}
		return v;
	}

	/**
	 * Convert byte array to int array.
	 * 
	 * @param data
	 * @param includeLength
	 * @return
	 */
	private static final int[] toIntArray(byte[] data, boolean includeLength) {
		int n = (((data.length & 3) == 0) ? (data.length >>> 2) : ((data.length >>> 2) + 1));
		int[] result;

		if (includeLength) {
			result = new int[n + 1];
			result[n] = data.length;
		} else {
			result = new int[n];
		}
		n = data.length;
		for (int i = 0; i < n; i++) {
			result[i >>> 2] |= (0x000000ff & data[i]) << ((i & 3) << 3);
		}
		return result;
	}

	/**
	 * Convert int array to byte array.
	 * 
	 * @param data
	 * @param includeLength
	 * @return
	 */
	private static final byte[] toByteArray(int[] data, boolean includeLength) {
		int n = data.length << 2;

		if (includeLength) {
			int m = data[data.length - 1];

			if (m > n) {
				return null;
			} else {
				n = m;
			}
		}
		byte[] result = new byte[n];

		for (int i = 0; i < n; i++) {
			result[i] = (byte) ((data[i >>> 2] >>> ((i & 3) << 3)) & 0xff);
		}
		return result;
	}
	
	public static void encrypt(final String source, final String destination, final String key) {
		try {
			final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(source)));

			final int size = bis.available();

			final byte[] bytes = new byte[size];

			final FileOutputStream fos = new FileOutputStream(destination);

			while (true) {
				final int n = bis.read(bytes);
				if (n > 0) {
					if (n == bytes.length) {
						final byte[] bytesout = XXTEA.encrypt(bytes, key.getBytes("utf-8"));
						fos.write(bytesout);
					} else {
						System.err.println("Not Excepted");

						final byte[] lastblock = new byte[n];
						for (int i = 0; i < n; i++) {
							lastblock[i] = bytes[i];
						}

						final byte[] bytesout = XXTEA.encrypt(lastblock, key.getBytes("utf-8"));
						fos.write(bytesout);
					}
				} else {
					break;
				}
			}
			try {
				fos.flush();
				fos.close();
			} catch (Exception e) {
			}

			try {
				bis.close();
			} catch (Exception e) {
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void decrypt(final String source, final String destination, final String key) {
		try {
			final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(source)));

			final int size = bis.available();

			final byte[] bytes = new byte[size];

			final FileOutputStream fos = new FileOutputStream(destination);

			assert (fos != null);

			while (true) {
				final int n = bis.read(bytes);
				if (n > 0) {
					if (n == bytes.length) {
						final byte[] bytesout = XXTEA.decrypt(bytes, key.getBytes("utf-8"));
						fos.write(bytesout);
					} else {

						System.err.println("Not Excepted");

						final byte[] lastblock = new byte[n];
						for (int i = 0; i < n; i++) {
							lastblock[i] = bytes[i];
						}

						final byte[] bytesout = XXTEA.decrypt(lastblock, key.getBytes("utf-8"));
						fos.write(bytesout);
					}

				} else {
					break;
				}
			}

			try {
				fos.flush();
				fos.close();
			} catch (Exception e) {
			}

			try {
				bis.close();
			} catch (Exception e) {
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
