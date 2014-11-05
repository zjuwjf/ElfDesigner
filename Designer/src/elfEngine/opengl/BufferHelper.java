package elfEngine.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.ielfgame.stupidGame.test.TexturedVertex;


public class BufferHelper {
	private final static ByteOrder sByteOrder = ByteOrder.nativeOrder();
	
	public static final ByteBuffer sByteBuffer = ByteBuffer.allocateDirect(2048*2048*4).order(sByteOrder);
	public static final ByteBuffer sByteBuffer_1 = ByteBuffer.allocateDirect(2048*2048*4).order(sByteOrder);
	public static final ByteBuffer sByteBuffer_2 = ByteBuffer.allocateDirect(2048*2048*4).order(sByteOrder);
	
//	public static final ByteBuffer sByteBuffer_2 = ByteBuffer.allocateDirect(2048*2048*4).order(sByteOrder);
	public static final ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(TexturedVertex.stride).order(sByteOrder);
//	public static final ByteBuffer sByteBuffer_3 = ByteBuffer.allocateDirect(2048*2048*4).order(sByteOrder);
	
	public static final IntBuffer sIntBuffer = ByteBuffer.allocateDirect(2048*2048).order(sByteOrder).asIntBuffer();
	public static final FloatBuffer sFloatBuffer = ByteBuffer.allocateDirect(64*4).order(sByteOrder).asFloatBuffer();
	public static final DoubleBuffer sDoubleBuffer = ByteBuffer.allocateDirect(64*8).order(sByteOrder).asDoubleBuffer();
	
	public static final FloatBuffer sFloatBuffer_0 = ByteBuffer.allocateDirect(64*4).order(sByteOrder).asFloatBuffer();
	public static final FloatBuffer sFloatBuffer_1 = ByteBuffer.allocateDirect(64*4).order(sByteOrder).asFloatBuffer();
	public static final FloatBuffer sFloatBuffer_2 = ByteBuffer.allocateDirect(64*4).order(sByteOrder).asFloatBuffer();
	public static final FloatBuffer sFloatBuffer_3 = ByteBuffer.allocateDirect(64*4).order(sByteOrder).asFloatBuffer();
	
	static {
		sByteBuffer.clear();
		sIntBuffer.clear();
		sFloatBuffer.clear();
		sDoubleBuffer.clear();
		
		sFloatBuffer_0.clear();
		sFloatBuffer_1.clear();
		sFloatBuffer_2.clear();
		sFloatBuffer_3.clear(); 
	}
	
	public static ByteBuffer getByteBuffer() {
		sByteBuffer.clear();
		return sByteBuffer;
	}
	
	public static ByteBuffer getByteBuffer(final int size, byte fill) {
		sByteBuffer.clear();
		for(int i=0; i<size; i++) { 
			sByteBuffer.put(fill);
		} 
		sByteBuffer.flip();
		return sByteBuffer;
	}
}
