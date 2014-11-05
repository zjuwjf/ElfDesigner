package elfEngine.basic.node.particle;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.LinkedList;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.glu.GLU;

import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.test.TexturedVertex;
import com.ielfgame.stupidGame.utils.FileHelper;

import elfEngine.basic.math.MathCons;
import elfEngine.basic.modifier.INodeModifier.ModifierListener;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.particle.emitter.IParticleEmitter;
import elfEngine.basic.node.particle.emitter.RectangleInEmitter;
import elfEngine.basic.node.particle.modifier.BasicParticleModifier;
import elfEngine.basic.node.particle.modifier.MathHelper;
import elfEngine.basic.node.particle.modifier.ParticlePathModeAModifier;
import elfEngine.basic.node.particle.modifier.ParticlePathModeBModifier;
import elfEngine.basic.pool.ArrayPool;
import elfEngine.basic.pool.ElfPool;
import elfEngine.basic.pool.INewInstance;
import elfEngine.basic.touch.ElfEvent;
import elfEngine.basic.utils.ElfRandom;
import elfEngine.opengl.BlendMode;
import elfEngine.opengl.BufferHelper;
import elfEngine.opengl.GLHelper;
import elfEngine.opengl.GLManage;
import elfEngine.opengl.TextureRegion;

public class ParticleNode2 extends ElfNode {
	private IParticleEmitter mEmitter; 
	private final ElfPool mPool = new ElfPool();
	
	private String mPList;
	
	private int mExistedParticles = 0; 
	private float mDurationCount = 0;
	private boolean mPauseEmit = false;
	private float mEmissionRate = 0; 
	private float mEmitCounter = 0; 
	
	//blend
	protected BlendMode particlBlendMode = BlendMode.ACTIVLE;
	//color
	protected ElfColor finishColor = new ElfColor();
	protected ElfColor finishColorVariance = new ElfColor();
	protected ElfColor startColor = new ElfColor();
	protected ElfColor startColorVariance = new ElfColor();
	//size
	protected Value2i finishParticleSize = new Value2i();
	protected Value2i startParticleSize = new Value2i();
	//rotate
	
	protected Value2f rotationStart = new Value2f();
	protected Value2f rotationEnd = new Value2f();
	//position
	protected ElfPointf sourcePosition = new ElfPointf();
	protected ElfPointf sourcePositionVariance = new ElfPointf();
	//path
	protected int emitterTypeValue;
	//mode a
	//radialAccel, tangentialAccel, gravityX, gravityY, angle, velocity
	protected Value2f radialAcceleration = new Value2f();
	protected Value2f tangentialAcceleration = new Value2f();
	protected ElfPointf gravity = new ElfPointf();
	protected Value2f angle = new Value2f();
	protected Value2f speedValue = new Value2f();
	//mode b
	//radiusMin, deltaRadius, degPerSecond
	protected Value2f maxRadius = new Value2f();
	protected float minRadiusValue;
	protected Value2f rotatePerSecond = new Value2f();
	
	//life
	protected int maxParticlesValue = 0;
	protected float durationValue = -1;
	protected Value2f particleLifespan = new Value2f();
	
	protected String textureFileNameValue; 
	
	protected float textureSizeWidth;
	protected float textureSizeHeight;
	
	protected final static int REF_ParticlePlist = DEFAULT_SHIFT | DROP_RESID_SHIFT;
	
	public String getTextureFileNameValue() {
		return textureFileNameValue;
	}
	public void setTextureFileNameValue(String textureFileNameValue) {
		this.textureFileNameValue = textureFileNameValue;
		textureSizeWidth = GLManage.getWidth(textureFileNameValue);
		textureSizeHeight = GLManage.getHeight(textureFileNameValue);
	}
	protected final static int REF_TextureFileNameValue = DEFAULT_SHIFT | DROP_RESID_SHIFT;
	
	public BlendMode getParticlBlendMode() {
		return particlBlendMode;
	}
	public void setParticlBlendMode(BlendMode particlBlendMode) {
		this.particlBlendMode = particlBlendMode;
	}
	protected final static int REF_ParticlBlendMode = DEFAULT_SHIFT;

	public ElfColor getFinishColor() {
		return finishColor;
	}
	public void setFinishColor(ElfColor finishColor) {
		this.finishColor = finishColor;
	}
	protected final static int REF_FinishColor = DEFAULT_SHIFT;
	
	public ElfColor getFinishColorVariance() {
		return finishColorVariance;
	}
	public void setFinishColorVariance(ElfColor finishColorVariance) {
		this.finishColorVariance = finishColorVariance;
	}
	protected final static int REF_FinishColorVariance = DEFAULT_SHIFT;
	
	public ElfColor getStartColor() {
		return startColor;
	}
	public void setStartColor(ElfColor startColor) {
		this.startColor = startColor;
	}
	protected final static int REF_StartColor = DEFAULT_SHIFT;
	
	public ElfColor getStartColorVariance() {
		return startColorVariance;
	}
	public void setStartColorVariance(ElfColor startColorVariance) {
		this.startColorVariance = startColorVariance;
	}
	protected final static int REF_StartColorVariance = DEFAULT_SHIFT;
	
	public Value2i getFinishParticleSize() {
		return finishParticleSize;
	}
	public void setFinishParticleSize(Value2i finishParticleSize) {
		this.finishParticleSize = finishParticleSize;
	}
	protected final static int REF_FinishParticleSize = DEFAULT_SHIFT;
	
	public Value2i getStartParticleSize() {
		return startParticleSize;
	}
	public void setStartParticleSize(Value2i startParticleSize) {
		this.startParticleSize = startParticleSize;
	}
	protected final static int REF_StartParticleSize = DEFAULT_SHIFT;
	
	public Value2f getRotationStart() {
		return rotationStart;
	}
	public void setRotationStart(Value2f rotationStart) {
		this.rotationStart = rotationStart;
	}
	protected final static int REF_RotationStart = DEFAULT_SHIFT;
	
	public Value2f getRotationEnd() {
		return rotationEnd;
	}
	public void setRotationEnd(Value2f rotationEnd) {
		this.rotationEnd = rotationEnd;
	}
	protected final static int REF_RotationEnd = DEFAULT_SHIFT;
	
	public ElfPointf getSourcePosition() {
		return sourcePosition;
	}
	public void setSourcePosition(ElfPointf sourcePositionx) {
		this.sourcePosition = sourcePositionx;
	}
	protected final static int REF_SourcePosition = DEFAULT_SHIFT;
	
	public ElfPointf getSourcePositionVariance() {
		return sourcePositionVariance;
	}
	public void setSourcePositionVariance(ElfPointf sourcePositiony) {
		this.sourcePositionVariance = sourcePositiony;
	}
	protected final static int REF_SourcePositionVariance = DEFAULT_SHIFT;
	
	public int getEmitterTypeValue() {
		return emitterTypeValue;
	}
	public void setEmitterTypeValue(int emitterTypeValue) {
		this.emitterTypeValue = emitterTypeValue;
	}
	protected final static int REF_EmitterTypeValue = DEFAULT_SHIFT;
	
	public Value2f getRadialAcceleration() {
		return radialAcceleration;
	}
	public void setRadialAcceleration(Value2f radialAcceleration) {
		this.radialAcceleration = radialAcceleration;
	}
	protected final static int REF_RadialAcceleration = DEFAULT_SHIFT;
	
	public Value2f getTangentialAcceleration() {
		return tangentialAcceleration;
	}
	public void setTangentialAcceleration(Value2f tangentialAcceleration) {
		this.tangentialAcceleration = tangentialAcceleration;
	}
	protected final static int REF_TangentialAcceleration = DEFAULT_SHIFT;
	
	public ElfPointf getGravity() {
		return gravity;
	}
	public void setGravity(ElfPointf gravity) {
		this.gravity = gravity;
	}
	protected final static int REF_Gravity = DEFAULT_SHIFT;
	
	public Value2f getAngle() {
		return angle;
	}
	public void setAngle(Value2f angle) {
		this.angle = angle;
	}
	protected final static int REF_Angle = DEFAULT_SHIFT;
	
	public Value2f getSpeedValue() {
		return speedValue;
	}
	public void setSpeedValue(Value2f speedValue) {
		this.speedValue = speedValue;
	}
	protected final static int REF_SpeedValue = DEFAULT_SHIFT;
	
	public Value2f getMaxRadius() {
		return maxRadius;
	}
	public void setMaxRadius(Value2f maxRadius) {
		this.maxRadius = maxRadius;
	}
	protected final static int REF_MaxRadius = DEFAULT_SHIFT;
	
	public float getMinRadiusValue() {
		return minRadiusValue;
	}
	public void setMinRadiusValue(float minRadiusValue) {
		this.minRadiusValue = minRadiusValue;
	}
	protected final static int REF_MinRadiusValue = DEFAULT_SHIFT;
	
	public Value2f getRotatePerSecond() {
		return rotatePerSecond;
	}
	public void setRotatePerSecond(Value2f rotatePerSecond) {
		this.rotatePerSecond = rotatePerSecond;
	}
	protected final static int REF_RotatePerSecond = DEFAULT_SHIFT;
	
	public int getMaxParticlesValue() {
		return maxParticlesValue;
	}
	public void setMaxParticlesValue(int maxParticlesValue) {
		this.maxParticlesValue = maxParticlesValue;
	}
	protected final static int REF_MaxParticlesValue = DEFAULT_SHIFT;
	
	public float getDurationValue() {
		return durationValue;
	}
	public void setDurationValue(float durationValue) {
		this.durationValue = durationValue;
	}
	protected final static int REF_DurationValue = DEFAULT_SHIFT;
	
	public Value2f getParticleLifespan() {
		return particleLifespan;
	}
	public void setParticleLifespan(Value2f particleLifespan) {
		this.particleLifespan = particleLifespan;
	}
	protected final static int REF_ParticleLifespan = DEFAULT_SHIFT;
	
	
	public void initAParticle(final Particle particle) {
		particle.clearModifiers();
		
		particle.setResid(textureFileNameValue);
		particle.setBlendMode(particlBlendMode);
		
		final float life = Math.max(0, particleLifespan.randomValue());
		final float redBeg = Math.max(0, ElfRandom.nextFloat(startColor.red-startColorVariance.red, startColor.red+startColorVariance.red));
		final float redEnd = Math.max(0, ElfRandom.nextFloat(finishColor.red-finishColorVariance.red, finishColor.red+finishColorVariance.red));
		final float greenBeg = Math.max(0, ElfRandom.nextFloat(startColor.green-startColorVariance.green, startColor.red+startColorVariance.green));
		final float greenEnd = Math.max(0, ElfRandom.nextFloat(finishColor.green-finishColorVariance.green, finishColor.red+finishColorVariance.green));
		final float blueBeg = Math.max(0, ElfRandom.nextFloat(startColor.blue-startColorVariance.blue, startColor.red+startColorVariance.blue));
		final float blueEnd = Math.max(0, ElfRandom.nextFloat(finishColor.blue-finishColorVariance.blue, finishColor.red+finishColorVariance.blue));
		final float alphaBeg = Math.max(0, ElfRandom.nextFloat(startColor.alpha-startColorVariance.alpha, startColor.alpha+startColorVariance.alpha));
		final float alphaEnd = Math.max(0, ElfRandom.nextFloat(finishColor.alpha-finishColorVariance.alpha, finishColor.alpha+finishColorVariance.alpha));
		
		final float width = textureSizeWidth;
		if(width <= 0) {
			System.err.println("textureFileNameValue ? " + textureFileNameValue );
			return ;
		} 
		final float scaleRate = 1.0f/width;
		final float scaleBeg = Math.max(0, scaleRate*startParticleSize.randomValue());
		final float scaleEnd = Math.max(0, scaleRate*finishParticleSize.randomValue());
		final float rotateBeg = rotationStart.randomValue();
		final float rotateSpan = rotationEnd.randomValue();
		//basic
		final BasicParticleModifier basicParticleModifier = particle.mBasicParticleModifier;
		basicParticleModifier.reset();
		basicParticleModifier.set(
				redBeg, redEnd, 
				greenBeg, greenEnd, 
				blueBeg, blueEnd, 
				alphaBeg, alphaEnd, 
				scaleBeg, scaleEnd, 
				rotateBeg, rotateSpan, 
				life);
		particle.addModifier(basicParticleModifier);
		
		//a
		if(emitterTypeValue == 0) {
			final float radialAccel = this.radialAcceleration.randomValue();
			final float tangentialAccel = this.tangentialAcceleration.randomValue();
			final float gravityX = this.gravity.x;
			final float gravityY = this.gravity.y;
			final float angle = this.angle.randomValue()*MathCons.RAD_TO_DEG;
			final float velocity = this.speedValue.randomValue();
			
			final ParticlePathModeAModifier AModifier = particle.mParticlePathModeAModifier;
			AModifier.reset();
			AModifier.set(radialAccel, tangentialAccel, gravityX, gravityY, angle, velocity);
			particle.addModifier(AModifier);
		} else {
			final float minRadius = this.minRadiusValue;
			final float r = this.maxRadius.randomValue();
			final float deltaRadius = r/life;
			final float degPerSecond = this.rotatePerSecond.randomValue()*MathCons.RAD_TO_DEG;
			
			final ParticlePathModeBModifier BModifier = particle.mParticlePathModeBModifier;
			BModifier.reset();
			BModifier.set(minRadius, deltaRadius, degPerSecond);
			particle.addModifier(BModifier);
		} 
	}
	
	public ParticleNode2(ElfNode father, int ordinal) {
		super(father, ordinal);
		
		setDefaultWidth(128);
		setDefaultHeight(128);
		
		setUseSettedSize(true);
		setSize(128, 128);
	}
	
	private ParticleEmitter mParticleEmitter;
	
	public void onRecognizeRequiredNodes() {
		super.onRecognizeRequiredNodes();
		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				if(node instanceof ParticleEmitter) {
					mParticleEmitter = (ParticleEmitter)node;
					return true;
				}
				return false;
			}
		});
		
		if(mParticleEmitter == null) {
			mParticleEmitter = new ParticleEmitter(this, 0);
			mParticleEmitter.addToParent(); 
		} 
	}
	
	public void onCreateRequiredNodes() {
		super.onCreateRequiredNodes(); 
		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				if(node instanceof ParticleEmitter) {
					mParticleEmitter = (ParticleEmitter)node;
					return true;
				}
				return false;
			} 
		});
		
		if(mParticleEmitter == null) { 
			mParticleEmitter = new ParticleEmitter(this, 0);
			mParticleEmitter.addToParent(); 
		} 
	} 
	
	private final void fillPool(){ 
		mPool.fillPool(new INewInstance(){
			public ElfNode newInstance() {
				return new Particle();
			} 
		}, maxParticlesValue); 
	} 
	
	public void reset(){ 
		super.reset(); 
		mDurationCount = 0; 
	} 
	
	static boolean sHasSetupQuad = false;
	static int vaoId;
	static int vboId;
	static int vboiId;
	static ByteBuffer vertexByteBuffer;
	static ArrayList<TexturedVertex[]> texturedVertexList = new ArrayList<TexturedVertex[]>();
	
	static final int MAX_PARTICLES = 2048;
	
	static void setupQuad() {
		vertexByteBuffer = BufferHelper.vertexByteBuffer;
		
		// Create a new Vertex Array Object in memory and select it (bind)
		vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);
		
		// Create a new Vertex Buffer Object in memory and select it (bind)
		vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		
		GL11.glVertexPointer(TexturedVertex.positionElementCount, GL11.GL_FLOAT,TexturedVertex.stride, TexturedVertex.positionByteOffset);
		GL11.glColorPointer(TexturedVertex.colorElementCount, GL11.GL_FLOAT,TexturedVertex.stride, TexturedVertex.colorByteOffset);
		GL11.glTexCoordPointer(TexturedVertex.textureElementCount, GL11.GL_FLOAT,TexturedVertex.stride, TexturedVertex.textureByteOffset);
		
//		BufferHelper.sByteBuffer.position(0);
//		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, BufferHelper.sByteBuffer_3.asFloatBuffer(), GL15.GL_DYNAMIC_DRAW);
		//40 float
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, MAX_PARTICLES*40, GL15.GL_DYNAMIC_DRAW);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		// Deselect (bind to 0) the VAO
		GL30.glBindVertexArray(0);
		
		// Create a new VBO for the indices and select it (bind) - INDICES
		vboiId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
		// OpenGL expects to draw vertices in counter clockwise order by default
		final int [] indices = {
				0, 1, 2,
				2, 3, 0 
//				3, 2, 1
		};
		
		final ShortBuffer indicesBuffer = BufferHelper.sByteBuffer.asShortBuffer(); 
		indicesBuffer.position(0); 
		for(int i=0; i<MAX_PARTICLES; i++) { 
			for(int j=0; j<6; j++) { 
				indicesBuffer.put(i*6+j, (short)(i*4+indices[j])); 
			} 
		} 
		indicesBuffer.position(0); 
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		
		System.err.println("setupQuad");
		exitOnGLError("setupQuad");
		
		GL11.glEnableClientState (GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState (GL11.GL_COLOR_ARRAY);
		GL11.glEnableClientState (GL11.GL_TEXTURE_COORD_ARRAY);
		
		// Put the position coordinates in attribute list 0
//		GL20.glVertexAttribPointer(0, TexturedVertex.positionElementCount, GL11.GL_FLOAT, false, TexturedVertex.stride, TexturedVertex.positionByteOffset);
		// Put the color components in attribute list 1
//		GL20.glVertexAttribPointer(1, TexturedVertex.colorElementCount, GL11.GL_FLOAT, false, TexturedVertex.stride, TexturedVertex.colorByteOffset);
		// Put the texture coordinates in attribute list 2
//		GL20.glVertexAttribPointer(2, TexturedVertex.textureElementCount, GL11.GL_FLOAT, false, TexturedVertex.stride, TexturedVertex.textureByteOffset);
	} 
	
	public void drawSprite() { 
		if(!sHasSetupQuad) { 
			sHasSetupQuad = true;
			setupQuad(); 
		} 
		
		final TextureRegion tr = GLManage.loadTextureRegion(this.textureFileNameValue, this.getGLId());
		
		if (mVisible && tr!= null) {
			drawBefore();
			// draw once
			final ArrayList<TexturedVertex[]> list = texturedVertexList;
			list.clear();
			this.iterateChilds(new IIterateChilds() {
				public boolean iterate(ElfNode node) {
					if(node instanceof Particle) {
						final Particle particle = (Particle)(node);
						//calc
						particle.updateTexturedVertex();
						list.add(particle.vertices);
					} 
					return false;
				}
			});
			
			// Update vertices in the VBO, first bind the VBO
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
			
//			GL20.glEnableVertexAttribArray(0);
//			GL20.glEnableVertexAttribArray(1);
//			GL20.glEnableVertexAttribArray(2);
			
			GL11.glEnableClientState (GL11.GL_VERTEX_ARRAY);
			GL11.glEnableClientState (GL11.GL_COLOR_ARRAY);
			GL11.glEnableClientState (GL11.GL_TEXTURE_COORD_ARRAY);
			
			// Apply and update vertex data
			final int quads = list.size();
			final FloatBuffer vertexFloatBuffer = vertexByteBuffer.asFloatBuffer();
			for(int i=0; i<quads; i++) {
				final TexturedVertex[] texturedVertexs = list.get(i);
				for(int j=0; j<4; j++) { 
					final TexturedVertex vertex = texturedVertexs[j];
					vertexFloatBuffer.position(0);
					vertexFloatBuffer.put(vertex.getElements());
					vertexFloatBuffer.flip();
					GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, (i*4+j) * TexturedVertex.stride, vertexByteBuffer);
				} 
			} 
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			
			GLHelper.glBlendFunc(this.particlBlendMode.sourceBlendFunction, this.particlBlendMode.destinationBlendFunction);
			GLHelper.glBindTexture(tr.texture.getGLBindId());
			
			GL30.glBindVertexArray(vaoId);
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
			// Draw the vertices
			
			GL11.glDrawElements(GL11.GL_TRIANGLES, 6*quads, GL11.GL_SHORT, 0);
			// Put everything back to default (deselect) 
			
			GL11.glDisableClientState (GL11.GL_VERTEX_ARRAY);
			GL11.glDisableClientState (GL11.GL_COLOR_ARRAY);
			GL11.glDisableClientState (GL11.GL_TEXTURE_COORD_ARRAY);
			
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
			GL30.glBindVertexArray(0);
			
			drawAfter();
		}
	}
	
	public void calcSprite(float pMsElapsed){	
		pMsElapsed = pMsElapsed/1000f;
		super.calcSprite(pMsElapsed);
		
		if(mEmitter != null) { 
			final float x = mEmitter.getCentreX();
			final float y = mEmitter.getCentreY();
			
			if( !mPauseEmit ){
				if(durationValue>0){ 
					if(durationValue>mDurationCount){ 
						mDurationCount += pMsElapsed; 
						float rate = 1.0f / mEmissionRate;
				        if (mExistedParticles < maxParticlesValue) { 
				            mEmitCounter += pMsElapsed;
				        } 
				        
				        while (mExistedParticles < maxParticlesValue && mEmitCounter > rate) {
				        	addAParticle(x, y); 
				            mEmitCounter -= rate; 
				        }
					} 
				} else {
					float rate = 1.0f / mEmissionRate;
			        if (mExistedParticles < maxParticlesValue) { 
			            mEmitCounter += pMsElapsed; 
			        } 
			        
			        while (mExistedParticles < maxParticlesValue && mEmitCounter > rate) {
			        	addAParticle(x, y); 
			            mEmitCounter -= rate;
			        } 
				} 
			}
		} 
	} 
	
	private final void addAParticle(final float x, final float y){
		final Particle particle = (Particle) mPool.getFreshItem();
		initAParticle(particle);
		final float [] tmp = ArrayPool.float2;
		mEmitter.getPosition(tmp);
		
		if(mParticleEmitter != null) {
			final ElfPointf pos = mParticleEmitter.getPosition(); 
			particle.setPosition(pos.x+tmp[0], pos.y+tmp[1]); 
		} else { 
			particle.setPosition(tmp[0], tmp[1]);
		}
		
		particle.addToParent();
		particle.mParticlePathModeAModifier.setOriginPosition(x, y); 
		mExistedParticles++;
	}
	
	private IOnNoExistedParticle mOnNoExistedParticle = null;
	public void setOnNoExistedParticleListener(final IOnNoExistedParticle onNoExistedParticle){
		mOnNoExistedParticle = onNoExistedParticle;
	}
	
	public static interface IOnNoExistedParticle{
		public void onNoExistedParticle(final ParticleNode2 sys);
	}
	
	public int getExistParticles(){
		return mExistedParticles;
	}
	protected final static int REF_ExistParticles = FACE_GET_SHIFT;
	
	public IParticleEmitter getEmitter() {
		return mEmitter;
	}
	public void setEmitter(IParticleEmitter mEmitter) {
		this.mEmitter = mEmitter;
	} 
	
	public void pauseEmit() {
		 mPauseEmit = true;
	}
	public void resumeEmit() {
		mDurationCount = 0;
		mPauseEmit = false;
	}
	
	public boolean getEmitPaused(){
		return mPauseEmit;
	}
	
	//
	private static void exitOnGLError(String errorMessage) {
		int errorValue = GL11.glGetError();
		
		if (errorValue != GL11.GL_NO_ERROR) {
			String errorString = GLU.gluErrorString(errorValue);
			System.err.println("ERROR - " + errorMessage + ": " + errorString);
			
			if (Display.isCreated()) Display.destroy();
			System.exit(-1);
		}
	}
	
	public class Particle extends ElfNode{
		//life
		public final BasicParticleModifier mBasicParticleModifier = new BasicParticleModifier(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		//position
		public final ParticlePathModeAModifier mParticlePathModeAModifier = new ParticlePathModeAModifier(0,0,0,0,0,0);
		public final ParticlePathModeBModifier mParticlePathModeBModifier = new ParticlePathModeBModifier(0,0,0);
		
		public final TexturedVertex[] vertices = new TexturedVertex[]{new TexturedVertex(),new TexturedVertex(),new TexturedVertex(),new TexturedVertex()};
		
		public Particle() {
			super(ParticleNode2.this, -1);
			mBasicParticleModifier.setModifierListener(new ModifierListener(){
				public void onFinished(ElfNode node) {
					node.removeFromParent();
				}
			});
			
			this.addDeadListener(new IDeadListener(){
				public void onDead(ElfNode node) {
					node.recycle();
					mExistedParticles--;
					if(mExistedParticles<=0&&mOnNoExistedParticle!=null){
						mOnNoExistedParticle.onNoExistedParticle(ParticleNode2.this);
					}
				}
			});
						
			this.setUseModifier(true);
			this.setPool(mPool);
			mPool.recycle(this);
		} 
		
		final void convert(float x, float y, float sin, float cos, float [] ret) {
			ret[0] = x*cos - y*sin;
			ret[1] = x*sin + y*cos;
		}
		
		public void updateTexturedVertex() {
			final float scale = this.getScale().x;
			final float rotate = this.getRotate() * MathCons.RAD_TO_DEG;
			final ElfPointf pos = this.getPosition();
			final float x = pos.x;
			final float y = pos.y;
			
			final float w = textureSizeWidth*scale/2;
			final float h = textureSizeHeight*scale/2;
			
			final float sin = MathHelper.sin(rotate);
			final float cos = MathHelper.cos(rotate);
			
			convert(-w,h,sin,cos,ArrayPool.float2);
			vertices[0].setST(0, 0);
			vertices[0].setXYZ(x+ArrayPool.float2[0], y+ArrayPool.float2[1], 0);
			vertices[2].setST(1, 1);
			vertices[2].setXYZ(x-ArrayPool.float2[0], y-ArrayPool.float2[1], 0);
			
			convert(w,h,scale,rotate,ArrayPool.float2);
			vertices[1].setST(0, 1);
			vertices[1].setXYZ(x-ArrayPool.float2[0], y-ArrayPool.float2[1], 0);
			vertices[3].setST(1, 0);
			vertices[3].setXYZ(x+ArrayPool.float2[0], y+ArrayPool.float2[1], 0);
			
			final ElfNode fatherNode = this.getParent();
			final ElfColor color = this.getColor();
			if (fatherNode != null) {
				final ColorAssist fca = fatherNode.getColorAssist();
				final ColorAssist ca = mColorAssist;
				if (fca.red != 1) {
					ca.red = color.red * fca.red;
				} else {
					ca.red = color.red;
				}
				if (fca.green != 1) {
					ca.green = color.green * fca.green;
				} else {
					ca.green = color.green;
				}
				if (fca.blue != 1) {
					ca.blue = color.blue * fca.blue;
				} else {
					ca.blue = color.blue;
				}
				if (fca.alpha != 1) {
					ca.alpha = color.alpha * fca.alpha;
				} else {
					ca.alpha = color.alpha;
				}
				vertices[0].setRGBA(ca.red, ca.green, ca.blue, ca.alpha);
				vertices[1].setRGBA(ca.red, ca.green, ca.blue, ca.alpha);
				vertices[2].setRGBA(ca.red, ca.green, ca.blue, ca.alpha);
				vertices[3].setRGBA(ca.red, ca.green, ca.blue, ca.alpha);
				vertices[0].setRGBA(1, 1, 1, 1);
				vertices[1].setRGBA(1, 1, 1, 1);
				vertices[2].setRGBA(1, 1, 1, 1);
				vertices[3].setRGBA(1, 1, 1, 1);
			} 
		} 
	} 
	
	public ElfNode [] getChilds() { 
		final LinkedList<ElfNode> nodes = new LinkedList<ElfNode>();
		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) { 
				if(!(node instanceof Particle)) {
					nodes.add(node);
				}
				return false;
			}
		});
		final ElfNode [] childs = new ElfNode[nodes.size()];
		nodes.toArray(childs);
		
		return childs;
	} 

	public String getParticlePlist() { 
		return mPList;
	} 
	public void setParticlePlist(final String plistPath) {
		mPList = plistPath;
		final ParticlePlistHelper helper = new ParticlePlistHelper(plistPath);
		
		//color
		finishColor.set(helper.finishColorRedValue,helper.finishColorGreenValue,helper.finishColorBlueValue,helper.finishColorAlphaValue);
		finishColorVariance.set(helper.finishColorVarianceRedValue,helper.finishColorVarianceGreenValue,helper.finishColorVarianceBlueValue,helper.finishColorVarianceAlphaValue);
		startColor.set(helper.startColorRedValue,helper.startColorGreenValue,helper.startColorBlueValue,helper.startColorAlphaValue);
		startColorVariance.set(helper.startColorVarianceRedValue,helper.startColorVarianceGreenValue,helper.startColorVarianceBlueValue,helper.startColorVarianceAlphaValue);
		//size
		finishParticleSize.set(Math.round(helper.finishParticleSizeValue), Math.round(helper.finishParticleSizeVarianceValue));
		startParticleSize.set(Math.round(helper.startParticleSizeValue), Math.round(helper.startParticleSizeVarianceValue));
		//rotate
		rotationStart.set(helper.rotationStartValue, helper.rotationStartVarianceValue);
		rotationEnd.set(helper.rotationEndValue, helper.rotationEndVarianceValue);
		//position
		sourcePosition.setPoint(helper.sourcePositionxValue, helper.sourcePositionyValue);
		sourcePositionVariance.setPoint(helper.sourcePositionVariancexValue, helper.sourcePositionVarianceyValue);
		//path
		emitterTypeValue = Math.round(helper.emitterTypeValue);
		//mode a
		//radialAccel, tangentialAccel, gravityX, gravityY, angle, velocity
		radialAcceleration.set(helper.radialAccelerationValue, helper.radialAccelVarianceValue);
		tangentialAcceleration.set(helper.tangentialAccelerationValue, helper.tangentialAccelVarianceValue);
		gravity.setPoint(helper.gravityxValue, helper.gravityyValue);
		angle.set(helper.angleValue, helper.angleVarianceValue);
		speedValue.set(helper.speedValue, helper.speedVarianceValue);
		//mode b
		maxRadius.set(helper.maxRadiusValue, helper.maxRadiusVarianceValue);
		minRadiusValue = helper.minRadiusValue;
		rotatePerSecond.set(helper.rotatePerSecondValue, helper.rotatePerSecondVarianceValue);
		//life
		maxParticlesValue = Math.round(helper.maxParticlesValue);
		durationValue = helper.durationValue;
		particleLifespan.set(helper.particleLifespanValue, helper.particleLifespanVarianceValue);
		
		//textureFileNameValue = FileHelper.getDirPath(plistPath) +FileHelper.DECOLLATOR+ helper.textureFileNameValue; 
		setTextureFileNameValue(FileHelper.getDirPath(plistPath) +FileHelper.DECOLLATOR+ helper.textureFileNameValue);
		
		final RectangleInEmitter emitter = new RectangleInEmitter(0, 0, sourcePositionVariance.x, sourcePositionVariance.y);
		mEmitter = emitter;
		
		if(mParticleEmitter != null) {
			mParticleEmitter.setPosition(sourcePosition.x, sourcePosition.y);
		}
		
		removeParticles();
		mExistedParticles = 0;
		mDurationCount = 0;
		mEmissionRate = maxParticlesValue/particleLifespan.value; 
		mEmitCounter = 0; 
		
		fillPool();
	} 
	
	
	public boolean onNextSelectTouch(ElfEvent event){ 
		return this.onNextSelectTouchSelf(event);
	} 
	
	public boolean onPreSelectTouch(ElfEvent event){ 
		return this.onPreSelectTouchSelf(event);
	} 
	
	void removeParticles() {
		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				if(node instanceof ParticleNode2) {
					node.removeFromParent();
				}
				return false;
			}
		});
	}
	
	public static class ParticleEmitter extends ElfNode{ 
		public enum ParticleEmitterType { 
			CircleIn, CircleOutLine, 
			RectangleIn, RectangleOutLine, 
			PolygonIn, PolygonOutLine, 
		} 
		private ParticleEmitterType mParticleEmitterType = ParticleEmitterType.CircleOutLine;
		public ParticleEmitterType getParticleEmitterType() {
			return mParticleEmitterType;
		}
		public void setParticleEmitterType(ParticleEmitterType particleEmitterType) { 
			if(particleEmitterType != null) {
				this.mParticleEmitterType = particleEmitterType;
			}
		} 
		protected final static int REF_ParticleEmitterType = DEFAULT_SHIFT; 
		
		private final ElfPointf mCircle = new ElfPointf(100, 100); 
		public void setCircle(ElfPointf circle) { 
			mCircle.set(circle); 
		} 
		public ElfPointf getCircle() { 
			return mCircle; 
		} 
		protected final static int REF_Circle = DEFAULT_SHIFT; 
		
		private final ElfPointf mRectangle = new ElfPointf(100, 100); 
		public void setRectangle(ElfPointf rectangle) { 
			mRectangle.set(rectangle); 
		} 
		public ElfPointf getRectangle() { 
			return mRectangle; 
		} 
		protected final static int REF_Rectangle = DEFAULT_SHIFT; 
		
		private float mOutLineThickness = 1f;
		public void setOutLineThickness(float outLineThickness) {
			mOutLineThickness = outLineThickness; 
		} 
		public float getOutLineThickness() { 
			return mOutLineThickness;
		} 
		protected final static int REF_OutLineThickness = DEFAULT_SHIFT; 
		
		//circle value 
		//rectangle value 
		//polygon value 
		public ParticleEmitter(ElfNode father, int ordinal) {
			super(father, ordinal); 
			setName("#emitter"); 
		} 
		
		public ElfPointf getEmitterPosition() {
			final ElfPointf pos = this.getPosition();
			
			return pos;
		} 
	} 
}  
