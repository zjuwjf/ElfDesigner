//package com.ielfgame.stupidGame.test;
//
//import java.nio.ByteBuffer;
//import java.nio.FloatBuffer;
//import java.util.HashMap;
//
//import lwjgl.Texture;
//
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL15;
//
//import assets.graphics.SpriteAsset;
//
//public final class SpritesBatchRenderer
//{
//	private static final int   	V_PER_SPRITE = 4;
//	private static final int   	ELEMENT_SIZE = 4;
//	
//	private static final int   	SZ_TEX   	= 2;                        	// U,V
//	private static final int   	SZ_VER   	= 3;                        	// X,Y,Z
//	private static final int   	SZ_UNIT  	= SZ_TEX + SZ_VER;
//	
//	private final Texture      	texture;
//	
//	private HashMap<Long, Integer> idToIdx  	= new HashMap<Long, Integer> ();
//	private HashMap<Integer, Long> idxToId  	= new HashMap<Integer, Long> ();
//	private long       			nextID;
//	
//	private FloatBuffer        	vertexBuffer;
//	private boolean            	bDirty;
//	
//	private int                	capacity 	= 64;
//	private int                	size 		= 0;
//	private int                	vertexBO;
//	
//	public SpritesBatchRenderer (Texture aTexture)
//	{
//		texture = aTexture;
//		vertexBO = GL15.glGenBuffers ();
//		
//		resize (capacity);
//	}
//	
//	public long addSprite (final float aX, final float aY, final float aScale, final boolean flipX,
//    		final boolean flipY, SpriteAsset anAsset)
//	{
//		int theIndex = size++;
//		if( size == capacity )
//			resize ((int) (capacity * 1.5));
//		
//		long theID = ++nextID;
//		idToIdx.put (theID, theIndex);
//		idxToId.put (theIndex, theID);
//		
//		updateSprite (theID, aX, aY, aScale, flipX, flipY, anAsset);
//		return theID;
//	}
//	
//	private void updateSprite (long theID, float aX, float aY, float aScale, boolean flipX,
//    		boolean flipY, SpriteAsset anAsset)
//	{
//		int theIndex = idToIdx.get (theID);
//		bDirty = true;
//		
//		anAsset.fillBuffer (vertexBuffer, aX, aY, aScale, flipX, flipY, theIndex * SZ_UNIT
//    			* V_PER_SPRITE);
//	}
//	
//	public void rmSprite (long aID)
//	{
//		size--;
//		
//		// Fetch and remove the binding for the current item
//		int theIndex = idToIdx.get (aID);
//		idToIdx.remove (aID);
//		
//		// If we removed an item from anywhere but the end of the list...
//		if( theIndex != size )
//		{
//			// Replace the removed item with the last item in the list.
//			for( int i = 0; i < V_PER_SPRITE * SZ_UNIT; ++i )
//				vertexBuffer.put (theIndex + i, vertexBuffer.get (size * i));
//			
//			// Update the binding for the moved item
//			final long theMovedId = idxToId.get (size);
//			idToIdx.put (theMovedId, theIndex);
//			idxToId.put (theIndex, theMovedId);
//			
//			bDirty = true;
//		}
//	}
//	
//	private void resize (int aCap)
//	{
//		final FloatBuffer oldVB = vertexBuffer;
//		
//		vertexBuffer = ByteBuffer.allocateDirect (aCap * V_PER_SPRITE * SZ_UNIT * ELEMENT_SIZE)
//    			.asFloatBuffer ();
//		
//		if( oldVB != null )
//		{
//			oldVB.position (0);
//			vertexBuffer.put (oldVB);
//		}
//		
//		capacity = aCap;
//		bDirty = true;
//	}
//	
//	public void render ()
//	{
//		if( bDirty )
//		{
////			for( int i = 0; i < size * V_PER_SPRITE; ++i )
////			{
////				for( int j = 0; j < SZ_UNIT; ++j )
////				{
////					System.out.print (" ");
////					System.out.print (vertexBuffer.get (i * SZ_UNIT + j));
////				}
////				System.out.println ();
////			}
//			
//			vertexBuffer.position (0);
//			GL15.glBindBuffer (GL15.GL_ARRAY_BUFFER, vertexBO);
//			GL15.glBufferData (GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_DYNAMIC_DRAW);
//			bDirty = false;
//		}
//		
//		GL11.glEnableClientState (GL11.GL_VERTEX_ARRAY);
//		GL11.glEnableClientState (GL11.GL_TEXTURE_COORD_ARRAY);
//		
//		GL15.glBindBuffer (GL15.GL_ARRAY_BUFFER, vertexBO);
//		GL11.glVertexPointer (SZ_VER, GL11.GL_FLOAT, SZ_UNIT * ELEMENT_SIZE, 0);
//		GL11.glTexCoordPointer (SZ_TEX, GL11.GL_FLOAT, SZ_UNIT * ELEMENT_SIZE, SZ_VER * ELEMENT_SIZE);
//		
//		// TODO For testing purposes; change to 1,1,1,1 and bind() later
//		GL11.glColor4f (0, 1, 1, 0.4f);
////		texture.bind ();
//		GL11.glDrawArrays (GL11.GL_TRIANGLES, 0, size * V_PER_SPRITE);
////		texture.unbind ();
//		
//		GL15.glBindBuffer (GL15.GL_ARRAY_BUFFER, 0);
//		GL11.glDisableClientState (GL11.GL_VERTEX_ARRAY);
//		GL11.glDisableClientState (GL11.GL_TEXTURE_COORD_ARRAY);
//	}
//	
//	protected void finalize () throws Throwable
//	{
//		dispose ();
//	}
//	
//	public void dispose ()
//	{
//		if( vertexBO != 0 ) {
//			GL15.glDeleteBuffers (vertexBO);
//			vertexBO = 0;
//		}
//	}
//}