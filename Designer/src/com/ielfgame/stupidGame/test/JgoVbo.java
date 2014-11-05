package com.ielfgame.stupidGame.test;

import static org.lwjgl.opengl.ARBBufferObject.GL_STATIC_DRAW_ARB;
import static org.lwjgl.opengl.ARBBufferObject.glBindBufferARB;
import static org.lwjgl.opengl.ARBBufferObject.glBufferDataARB;
import static org.lwjgl.opengl.ARBBufferObject.glDeleteBuffersARB;
import static org.lwjgl.opengl.ARBBufferObject.glGenBuffersARB;
import static org.lwjgl.opengl.ARBBufferObject.glMapBufferARB;
import static org.lwjgl.opengl.ARBBufferObject.glUnmapBufferARB;
import static org.lwjgl.opengl.ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB;
import static org.lwjgl.opengl.ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB;
import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_SHORT;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColorPointer;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL11.glViewport;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ARBBufferObject;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class JgoVbo {
	static void initContext() throws LWJGLException {
		int w = 640;
		int h = 480;

		Display.setDisplayMode(new DisplayMode(w, h));
		Display.setFullscreen(false);
		Display.create();
		glViewport(0, 0, w, h);
	}

	static void renderLoop() {
		while (!Display.isCloseRequested()) {
			preRender();

			render();

			Display.update();

			Display.sync(10 /* desired fps */);
		}

		Display.destroy();
	}

	static void preRender() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}

	static void drawImmediateMode() {
		glBegin(GL_TRIANGLES);

		glColor3f(1, 0, 0);
		glVertex3f(-0.5f, -0.5f, 0.0f);

		glColor3f(0, 1, 0);
		glVertex3f(+0.5f, -0.5f, 0.0f);

		glColor3f(0, 0, 1);
		glVertex3f(+0.5f, +0.5f, 0.0f);

		glEnd();
	}

	static void drawVertexArray() {
		// create geometry buffers
		FloatBuffer cBuffer = BufferUtils.createFloatBuffer(9);
		cBuffer.put(1).put(0).put(0);
		cBuffer.put(0).put(1).put(0);
		cBuffer.put(0).put(0).put(1);
		cBuffer.flip();

		FloatBuffer vBuffer = BufferUtils.createFloatBuffer(9);
		vBuffer.put(-0.5f).put(-0.5f).put(0.0f);
		vBuffer.put(+0.5f).put(-0.5f).put(0.0f);
		vBuffer.put(+0.5f).put(+0.5f).put(0.0f);
		vBuffer.flip();

		//

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);

		glColorPointer(3, /* stride */3 << 2, cBuffer);
		glVertexPointer(3, /* stride */3 << 2, vBuffer);
		glDrawArrays(GL_TRIANGLES, 0, /* elements */3);

		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);
	}

	static void drawVertexBufferObject() {
		// create geometry buffers
		FloatBuffer cBuffer = BufferUtils.createFloatBuffer(9);
		cBuffer.put(1).put(0).put(0);
		cBuffer.put(0).put(1).put(0);
		cBuffer.put(0).put(0).put(1);
		cBuffer.flip();

		FloatBuffer vBuffer = BufferUtils.createFloatBuffer(9);
		vBuffer.put(-0.5f).put(-0.5f).put(0.0f);
		vBuffer.put(+0.5f).put(-0.5f).put(0.0f);
		vBuffer.put(+0.5f).put(+0.5f).put(0.0f);
		vBuffer.flip();

		//

		IntBuffer ib = BufferUtils.createIntBuffer(2);

		glGenBuffersARB(ib);
		int vHandle = ib.get(0);
		int cHandle = ib.get(1);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);

		glBindBufferARB(GL_ARRAY_BUFFER_ARB, vHandle);
		glBufferDataARB(GL_ARRAY_BUFFER_ARB, vBuffer, GL_STATIC_DRAW_ARB);
		glVertexPointer(3, GL_FLOAT, /* stride */3 << 2, 0L);

		glBindBufferARB(GL_ARRAY_BUFFER_ARB, cHandle);
		glBufferDataARB(GL_ARRAY_BUFFER_ARB, cBuffer, GL_STATIC_DRAW_ARB);
		glColorPointer(3, GL_FLOAT, /* stride */3 << 2, 0L);

		glDrawArrays(GL_TRIANGLES, 0, 3 /* elements */);

		glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);

		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);

		// cleanup VBO handles
		ib.put(0, vHandle);
		ib.put(1, cHandle);
		glDeleteBuffersARB(ib);
	}

	static void drawVertexBufferObjectIndexed() {
		// create geometry buffers
		FloatBuffer cBuffer = BufferUtils.createFloatBuffer(9);
		cBuffer.put(1).put(0).put(0);
		cBuffer.put(0).put(1).put(0);
		cBuffer.put(0).put(0).put(1);
		cBuffer.flip();

		FloatBuffer vBuffer = BufferUtils.createFloatBuffer(9);
		vBuffer.put(-0.5f).put(-0.5f).put(0.0f);
		vBuffer.put(+0.5f).put(-0.5f).put(0.0f);
		vBuffer.put(+0.5f).put(+0.5f).put(0.0f);
		vBuffer.flip();

		// create index buffer
		ShortBuffer iBuffer = BufferUtils.createShortBuffer(3);
		iBuffer.put((short) 0);
		iBuffer.put((short) 1);
		iBuffer.put((short) 2);
		iBuffer.flip();

		//

		IntBuffer ib = BufferUtils.createIntBuffer(3);

		glGenBuffersARB(ib);
		int vHandle = ib.get(0);
		int cHandle = ib.get(1);
		int iHandle = ib.get(2);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);

		glBindBufferARB(GL_ARRAY_BUFFER_ARB, vHandle);
		glBufferDataARB(GL_ARRAY_BUFFER_ARB, vBuffer, GL_STATIC_DRAW_ARB);
		glVertexPointer(3, GL_FLOAT, /* stride */3 << 2, 0L);

		glBindBufferARB(GL_ARRAY_BUFFER_ARB, cHandle);
		glBufferDataARB(GL_ARRAY_BUFFER_ARB, cBuffer, GL_STATIC_DRAW_ARB);
		glColorPointer(3, GL_FLOAT, /* stride */3 << 2, 0L);

		glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, iHandle);
		glBufferDataARB(GL_ELEMENT_ARRAY_BUFFER_ARB, iBuffer, GL_STATIC_DRAW_ARB);

		glDrawElements(GL_TRIANGLES, /* elements */3, GL_UNSIGNED_SHORT, 0L);

		glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);
		glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, 0);

		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);

		// cleanup VBO handles
		ib.put(0, vHandle);
		ib.put(1, cHandle);
		ib.put(2, cHandle);
		glDeleteBuffersARB(ib);
	}

	static void drawVertexBufferObjectInterleaved1() {
		// create geometry buffer (both vertex and color)
		FloatBuffer vcBuffer = BufferUtils.createFloatBuffer(9 + 9);

		vcBuffer.put(-0.5f).put(-0.5f).put(0.0f); // v
		vcBuffer.put(1).put(0).put(0); // c

		vcBuffer.put(+0.5f).put(-0.5f).put(0.0f); // v
		vcBuffer.put(0).put(1).put(0); // c

		vcBuffer.put(+0.5f).put(+0.5f).put(0.0f); // v
		vcBuffer.put(0).put(0).put(1); // c

		vcBuffer.flip();

		//

		IntBuffer ib = BufferUtils.createIntBuffer(1);

		glGenBuffersARB(ib);
		int vcHandle = ib.get(0);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);

		glBindBufferARB(GL_ARRAY_BUFFER_ARB, vcHandle);
		glBufferDataARB(GL_ARRAY_BUFFER_ARB, vcBuffer, GL_STATIC_DRAW_ARB);
		glVertexPointer(3, GL_FLOAT, /* stride * */(3 * 2) << 2, /* offset * */0 << 2); // float
																						// at
																						// index
																						// 0
		glColorPointer(3, GL_FLOAT, /* stride * */(3 * 2) << 2, /* offset * */(3 * 1) << 2); // float
																								// at
																								// index
																								// 3

		glDrawArrays(GL_TRIANGLES, 0, 3 /* elements */);

		glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);

		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);

		// cleanup VBO handles
		ib.put(0, vcHandle);
		glDeleteBuffersARB(ib);
	}

	static void drawVertexBufferObjectInterleaved2() {
		// create geometry buffer (both vertex and color)
		FloatBuffer vcBuffer = BufferUtils.createFloatBuffer(9 + 9);

		vcBuffer.put(-0.5f).put(-0.5f).put(0.0f); // v
		vcBuffer.put(+0.5f).put(-0.5f).put(0.0f); // v
		vcBuffer.put(+0.5f).put(+0.5f).put(0.0f); // v
		vcBuffer.put(1).put(0).put(0); // c
		vcBuffer.put(0).put(1).put(0); // c
		vcBuffer.put(0).put(0).put(1); // c

		vcBuffer.flip();

		//

		IntBuffer ib = BufferUtils.createIntBuffer(1);

		glGenBuffersARB(ib);
		int vcHandle = ib.get(0);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);

		glBindBufferARB(GL_ARRAY_BUFFER_ARB, vcHandle);
		glBufferDataARB(GL_ARRAY_BUFFER_ARB, vcBuffer, GL_STATIC_DRAW_ARB);
		glVertexPointer(3, GL_FLOAT, /* stride * */(3 * 1) << 2, /* offset */0 << 2); // float
																						// at
																						// index
																						// 0
		glColorPointer(3, GL_FLOAT, /* stride * */(3 * 1) << 2, /* offset */(3 * 3) << 2); // float
																							// at
																							// index
																							// 9

		glDrawArrays(GL_TRIANGLES, 0, 3 /* elements */);

		glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);

		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);

		// cleanup VBO handles
		ib.put(0, vcHandle);
		glDeleteBuffersARB(ib);
	}

	static void drawVertexBufferObjectInterleavedMapped() {
		IntBuffer ib = BufferUtils.createIntBuffer(1);

		glGenBuffersARB(ib);
		int vcHandle = ib.get(0);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);

		glBindBufferARB(GL_ARRAY_BUFFER_ARB, vcHandle);
		glBufferDataARB(GL_ARRAY_BUFFER_ARB, (9 + 9) << 2, GL_STATIC_DRAW_ARB);

		{
			ByteBuffer dataBuffer = glMapBufferARB(GL_ARRAY_BUFFER_ARB, ARBBufferObject.GL_WRITE_ONLY_ARB, (9 + 9) << 2, null);
			
			// create geometry buffer (both vertex and color)
			FloatBuffer vcBuffer = dataBuffer.order(ByteOrder.nativeOrder()).asFloatBuffer();

			vcBuffer.put(-0.5f).put(-0.5f).put(0.0f); // v
			vcBuffer.put(1).put(0).put(0); // c

			vcBuffer.put(+0.5f).put(-0.5f).put(0.0f); // v
			vcBuffer.put(0).put(1).put(0); // c

			vcBuffer.put(+0.5f).put(+0.5f).put(0.0f); // v
			vcBuffer.put(0).put(0).put(1); // c

			vcBuffer.flip();

			glUnmapBufferARB(GL_ARRAY_BUFFER_ARB);
		}

		glVertexPointer(3, GL_FLOAT, /* stride */(3 * 2) << 2, /* offset */0L << 2); // float
																					// at
																					// index
																					// 0
		glColorPointer(3, GL_FLOAT, /* stride */(3 * 2) << 2, /* offset */(3 * 1) << 2); // float
																						// at
																						// index
																						// 3

		glDrawArrays(GL_TRIANGLES, 0, 3 /* elements */);

		glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);

		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);

		// cleanup VBO handles
		ib.put(0, vcHandle);
		glDeleteBuffersARB(ib);
	}

	static void render() {
		glClearColor(1, 1, 1, 1);
		// start drawing a triangle with the different strategies
		drawImmediateMode();

		glTranslatef(+0.05f, -0.05f, 0);
		drawVertexArray();

		glTranslatef(+0.05f, -0.05f, 0);
		drawVertexBufferObject();

		glTranslatef(+0.05f, -0.05f, 0);
		drawVertexBufferObjectIndexed();

		glTranslatef(+0.05f, -0.05f, 0);
		drawVertexBufferObjectInterleaved1();

		glTranslatef(+0.05f, -0.05f, 0);
		drawVertexBufferObjectInterleaved2();

		glTranslatef(+0.05f, -0.05f, 0);
		drawVertexBufferObjectInterleavedMapped();
	}

	public static void main(String[] args) throws LWJGLException {
		System.out.println("Hello world from OpenGL!");

		initContext();
		renderLoop();
	}
}