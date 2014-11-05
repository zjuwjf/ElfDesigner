package com.jcraft.jzlib.test;
/* -*-mode:java; c-basic-offset:2; -*- */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.ZInputStream;
import com.jcraft.jzlib.ZOutputStream;

public class test_stream_deflate_inflate {
	public static void main(String[] args) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ZOutputStream zOut = new ZOutputStream(out,
					JZlib.Z_BEST_COMPRESSION);

			byte[] hello = "Hello World!".getBytes();
			zOut.write(hello);
			zOut.close();

			ByteArrayInputStream in = new ByteArrayInputStream(out
					.toByteArray());
			ZInputStream zIn = new ZInputStream(in);
			byte[] hello2 = new byte[1024];
			int count = 0;
			int c;
			while ((c = zIn.read()) != -1) {
				hello2[count] = (byte) c;
				count++;
			}

			System.out.println(new String(hello2, 0, count));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
