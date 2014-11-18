package com.uve.android.tools;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.uve.android.service.UveLogger;

public class ByteCascader {
	public static int cascade4Bytes(int b1, int b2, int b3, int b4) {

		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4);
		// by choosing big endian, high order bytes must be put
		// to the buffer before low order bytes
		byteBuffer.order(ByteOrder.BIG_ENDIAN);
		// since ints are 4 bytes (32 bit), you need to put all 4, so put 0
		// for the high order bytes
		byteBuffer.put((byte) b1);
		byteBuffer.put((byte) b2);
		byteBuffer.put((byte) b3);
		byteBuffer.put((byte) b4);
		byteBuffer.flip();
		int result = byteBuffer.getInt();
		UveLogger.Info("cascading 4 bytes to int: " + b1 + " " + b2 + " " + b3
				+ " " + b4 + " -> into:" + result);
		return result;
	}
	public static int cascade2Bytes(int b1, int b2) {

		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(2);
		// by choosing big endian, high order bytes must be put
		// to the buffer before low order bytes
		byteBuffer.order(ByteOrder.BIG_ENDIAN);
		// since ints are 4 bytes (32 bit), you need to put all 4, so put 0
		// for the high order bytes
		byteBuffer.put((byte) b1);
		byteBuffer.put((byte) b2);
		byteBuffer.flip();
		int result = byteBuffer.getInt();
		UveLogger.Info("cascading 2 bytes to int: " + b1 + " " + b2 + " -> into:" + result);
		return result;
	}
}
