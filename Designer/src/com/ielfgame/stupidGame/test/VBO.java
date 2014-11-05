package com.ielfgame.stupidGame.test;

import java.nio.FloatBuffer;
import org.lwjgl.opengl.ARBVertexArrayObject;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class VBO {

    private final static int intSize = Integer.SIZE / 8;
    private final static int floatSize = Float.SIZE / 8;
    private final static int doubleSize = Double.SIZE / 8;
    private final static int verticeSize = 3 * floatSize;
    private final static int normalSize = 3 * floatSize;
    private final static int texSize = 2 * floatSize;
    private final static int colorSize = 3 * floatSize;
    private final static int elementSizeFull = verticeSize + normalSize + texSize + colorSize;
    private final static int elementSizeNoNomCol = verticeSize + texSize;
    private ByteBuffer buffer;
    private int vao, vboData, vboIndices;
    private int numVertices, numTexCoords;

    public VBO() {
        vao = createVAO();
        vboData = createVBO();
        vboIndices = createVBO();
        unbindVAO();
        numVertices = 0;
        numTexCoords = 0;
        allocate(0);
    }

    public void allocate(int size) {
        buffer = ByteBuffer.allocateDirect(size);
        buffer.order(ByteOrder.nativeOrder());
    }

    public void clear() {
        buffer.clear();
        numVertices = 0;
    }

    public void compile() {
        bindVAO(vao);
        {
            /**
             * Buffer data
             */
            bindVBO(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vboData);
            bufferVBO(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB,
                    ARBVertexBufferObject.GL_DYNAMIC_DRAW_ARB,
                    ((ByteBuffer) buffer.flip()).asFloatBuffer());

            /**
             * Bind Atrributes
             */
            glVertexPointer(3, GL_FLOAT, elementSizeNoNomCol, 0);
            glTexCoordPointer(2, GL_FLOAT, elementSizeNoNomCol, verticeSize);
            //glColorPointer(3, GL_FLOAT, elementSize, texSize + verticeSize);
            //glNormalPointer(GL_FLOAT, elementSize, verticeSize + texSize + colorSize);

            /**
             * Buffer indices
             */
            ByteBuffer indiceBuffer = ByteBuffer.allocateDirect(numVertices * 4);
            indiceBuffer.order(ByteOrder.nativeOrder());
            for (int i = 0; i < numVertices; i++) {
                indiceBuffer.putInt(i);
            }
            bindVBO(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, vboIndices);
            bufferVBO(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB,
                    ARBVertexBufferObject.GL_DYNAMIC_READ_ARB,
                    ((ByteBuffer)indiceBuffer.flip()).asFloatBuffer());
        }
        unbindVAO();
        /** Cleanup **/
    }

    public void draw() {
        bindVAO(vao);
        {
            glEnableClientState(GL_VERTEX_ARRAY);
            //glEnableClientState(GL_NORMAL_ARRAY);
            //glEnableClientState(GL_COLOR_ARRAY);
            glEnableClientState(GL_TEXTURE_COORD_ARRAY);
            glDrawElements(GL_QUADS, numVertices, GL_UNSIGNED_INT, 0);
            glDisableClientState(GL_VERTEX_ARRAY);
            //glDisableClientState(GL_COLOR_ARRAY);
            //glDisableClientState(GL_NORMAL_ARRAY);
            glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        }
        unbindVAO();
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    /**
     * 
     */
    public void addFloat(float x, float y, float z) {
        buffer.putFloat(x);
        buffer.putFloat(y);
        buffer.putFloat(z);
    }

    public void addFloat(float x, float y) {
        buffer.putFloat(x);
        buffer.putFloat(y);
    }

    public void addVertex(Vector3f vert) {
        addVertex(vert.x, vert.y, vert.z);
    }

    public void addVertex(float x, float y, float z) {
        addFloat(x, y, z);
        numVertices++;
    }

    public void addTexCoord(Vector2f texCoord) {
        addTexCoord(texCoord.x, texCoord.y);
    }

    public void addTexCoord(float x, float y) {
        addFloat(x, y);
        numTexCoords++;
    }

    public void addNormal(Vector3f normal) {
        addNormal(normal.x, normal.y, normal.z);
    }

    public void addNormal(float x, float y, float z) {
        addFloat(x, y, z);
    }

    public void bufferVBO(int target, int type, FloatBuffer buffer) {
        ARBVertexBufferObject.glBufferDataARB(target, buffer, type);
    }

    public void bindVAO(int vao) {
        ARBVertexArrayObject.glBindVertexArray(vao);
    }

    public void bindVBO(int target, int vbo) {
        ARBVertexBufferObject.glBindBufferARB(target, vbo);
    }

    public void unbindVAO() {
        ARBVertexArrayObject.glBindVertexArray(0);
    }

    public int createVAO() {
        return ARBVertexArrayObject.glGenVertexArrays();
    }

    public int createVBO() {
        return ARBVertexBufferObject.glGenBuffersARB();
    }
}
