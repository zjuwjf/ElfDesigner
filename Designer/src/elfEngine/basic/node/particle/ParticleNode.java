package elfEngine.basic.node.particle;

import java.util.LinkedList;

import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.utils.FileHelper;

import elfEngine.basic.math.MathCons;
import elfEngine.basic.node.BatchNode;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.particle.emitter.IParticleEmitter;
import elfEngine.basic.node.particle.emitter.RectangleInEmitter;
import elfEngine.basic.node.particle.modifier.BasicParticleModifier;
import elfEngine.basic.node.particle.modifier.ParticlePathModeAModifier;
import elfEngine.basic.node.particle.modifier.ParticlePathModeBModifier;
import elfEngine.basic.pool.ArrayPool;
import elfEngine.basic.pool.ElfPool;
import elfEngine.basic.pool.INewInstance;
import elfEngine.basic.touch.ElfEvent;
import elfEngine.basic.utils.ElfRandom;
import elfEngine.opengl.BlendMode;
import elfEngine.opengl.GLManage;

public class ParticleNode extends BatchNode {
	private IParticleEmitter mEmitter; 
	final ElfPool mPool = new ElfPool();
	
	private String mPList;
	
	int mExistedParticles = 0; 
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
		
		this.setBathResid(textureFileNameValue);
	}
	protected final static int REF_TextureFileNameValue = DEFAULT_SHIFT | DROP_RESID_SHIFT;
	
	public BlendMode getParticlBlendMode() {
		return particlBlendMode;
	}
	public void setParticlBlendMode(BlendMode particlBlendMode) {
		this.particlBlendMode = particlBlendMode;
		this.setBathBlendMode(particlBlendMode);
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
		if(maxParticlesValue > 0 && maxParticlesValue <= MAX_PARTICLES) {
			this.maxParticlesValue = maxParticlesValue;
			mEmissionRate = maxParticlesValue/particleLifespan.value; 
		}
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
		if(particleLifespan.value > 0) {
			this.particleLifespan = particleLifespan;
			mEmissionRate = maxParticlesValue/particleLifespan.value; 
		}
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
	
	public ParticleNode(ElfNode father, int ordinal) {
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
				final Particle particle = new Particle(ParticleNode.this, -1);
				particle.setParticleSys(ParticleNode.this);
				return particle;
			} 
		}, maxParticlesValue); 
	} 
	
	public void reset(){ 
		super.reset(); 
		mDurationCount = 0; 
	} 
	
	static final int MAX_PARTICLES = 2048;
	
	final float [] sTexCoords = new float[]{ 
			0,0,//0,
			1,0,//1,
			1,1,//2,
			0,0,//0,
			1,1,//2,
			0,1,//3,
			};
	
	private boolean mNormalRender = false;
	public void setNormalRender(boolean normal) {
		mNormalRender = normal;
	}
	public boolean getNormalRender() {
		return mNormalRender;
	}
	protected final static int REF_NormalRender = FACE_ALL_SHIFT;
	
	
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
	
	IOnNoExistedParticle mOnNoExistedParticle = null;
	public void setOnNoExistedParticleListener(final IOnNoExistedParticle onNoExistedParticle){
		mOnNoExistedParticle = onNoExistedParticle;
	}
	
	public static interface IOnNoExistedParticle{
		public void onNoExistedParticle(final ParticleNode sys);
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
	
	public Particle [] getParticles() {
		final LinkedList<Particle> nodes = new LinkedList<Particle>();
		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) { 
				if((node instanceof Particle)) {
					nodes.add((Particle)node);
				} 
				return false;
			}
		});
		final Particle [] childs = new Particle[nodes.size()];
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
	
	public ElfNode [] getBatchChilds() {
		return this.getParticles();
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
				if(node instanceof ParticleNode) {
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
	
	public final ElfNode copyDeep() {
		final ElfNode ret = copySelf(this.getParent());
		final ElfNode [] nodes = this.getChilds();
		if(nodes != null) {
			for(int i=0; i<nodes.length; i++) {
				final ElfNode child = nodes[i];
				final ElfNode newChild = child.copyDeep(ret);
				newChild.setParent(ret);
				newChild.addToParent(i);
			} 
		} 
		
		return ret;
	} 
}  
