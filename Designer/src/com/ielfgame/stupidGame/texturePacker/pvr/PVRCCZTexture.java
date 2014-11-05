package com.ielfgame.stupidGame.texturePacker.pvr;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;


/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 14:17:23 - 27.07.2011
 */
public abstract class PVRCCZTexture extends PVRTexture {

	public PVRCCZTexture() throws IllegalArgumentException, IOException {
	} 

	private CCZHeader mCCZHeader;

	@Override
	public final InflaterInputStream getInputStream() throws IOException {
		final InputStream inputStream = this.onGetInputStream();

		this.mCCZHeader = new CCZHeader(StreamUtils.streamToBytes(inputStream, CCZHeader.SIZE));

		return this.mCCZHeader.getCCZCompressionFormat().wrap(inputStream);
	}

	@Override
	public ByteBuffer getPVRTextureBuffer() throws IOException {
		final InputStream inputStream = this.getInputStream();
		try {
			final byte[] data = new byte[this.mCCZHeader.getUncompressedSize()];
			StreamUtils.copy(inputStream, data);
			return ByteBuffer.wrap(data);
		} finally {
			StreamUtils.close(inputStream);
		}
	}

	public static class CCZHeader {
		static final byte[] MAGIC_IDENTIFIER = {
			(byte)'C',
			(byte)'C',
			(byte)'Z',
			(byte)'!'
		};

		public static final int SIZE = 16;

		private final ByteBuffer mDataByteBuffer;
		private final CCZCompressionFormat mCCZCompressionFormat;

		public CCZHeader(final byte[] pData) {
			this.mDataByteBuffer = ByteBuffer.wrap(pData);
			this.mDataByteBuffer.rewind();
			this.mDataByteBuffer.order(ByteOrder.BIG_ENDIAN);

			/* Check magic bytes. */
//			if(!ArrayUtils.equals(pData, 0, CCZHeader.MAGIC_IDENTIFIER, 0, CCZHeader.MAGIC_IDENTIFIER.length)) {
//				throw new IllegalArgumentException("Invalid " + this.getClass().getSimpleName() + "!");
//			}

			// TODO Check the version?

			this.mCCZCompressionFormat = CCZCompressionFormat.fromID(this.getCCZCompressionFormatID());
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		private short getCCZCompressionFormatID() {
			return this.mDataByteBuffer.getShort(4);
		}

		public CCZCompressionFormat getCCZCompressionFormat() {
			return this.mCCZCompressionFormat;
		}

		public short getVersion() {
			return this.mDataByteBuffer.getShort(6);
		}

		public int getUserdata() {
			return this.mDataByteBuffer.getInt(8);
		}

		public int getUncompressedSize() {
			return this.mDataByteBuffer.getInt(12);
		}
	}

	public static enum CCZCompressionFormat {
		ZLIB((short)0),
		BZIP2((short)1),
		GZIP((short)2),
		NONE((short)3);

		private final short mID;

		private CCZCompressionFormat(final short pID) {
			this.mID = pID;
		}

		public InflaterInputStream wrap(final InputStream pInputStream) throws IOException {
			switch(this) {
				case GZIP:
					return new GZIPInputStream(pInputStream);
				case ZLIB:
					return new InflaterInputStream(pInputStream, new Inflater());
				case NONE:
				case BZIP2:
				default:
					throw new IllegalArgumentException("Unexpected " + CCZCompressionFormat.class.getSimpleName() + ": '" + this + "'.");
			}
		}

		public static CCZCompressionFormat fromID(final short pID) {
			final CCZCompressionFormat[] cczCompressionFormats = CCZCompressionFormat.values();
			final int cczCompressionFormatCount = cczCompressionFormats.length;
			for(int i = 0; i < cczCompressionFormatCount; i++) {
				final CCZCompressionFormat cczCompressionFormat = cczCompressionFormats[i];
				if(cczCompressionFormat.mID == pID) {
					return cczCompressionFormat;
				}
			}
			throw new IllegalArgumentException("Unexpected " + CCZCompressionFormat.class.getSimpleName() + "-ID: '" + pID + "'.");
		}
	}
}
