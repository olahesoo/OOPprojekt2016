package raycaster;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Color {
	byte r, g, b;
	byte a = 0;
	
	Color(int r, int g, int b) {
		this.r = (byte)r;
		this.g = (byte)g;
		this.b = (byte)b;
	}
	
	Color(byte r, byte g, byte b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public static int toInt(int r, int g, int b) {
		return r * 65536 + g * 256 + b - 16777216;
	}
	
	public static byte toPixelFormat(byte b) {
		return (byte) ((b & 0x80) == 0 ? b : 0xff - (b & ~0x80));
	}
	
	public int shade(double dist) {
		return ByteBuffer.wrap(this.multiply(dist)).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}
	private byte[] multiply(double dist) {
		double mult = 42 / (42 + dist);
		return new byte[]{
				a,
				toPixelFormat((byte) ((r & 0xff) * mult)),
				toPixelFormat((byte) ((g & 0xff) * mult)),
				toPixelFormat((byte) ((b & 0xff) * mult))
		};
	}
	
}