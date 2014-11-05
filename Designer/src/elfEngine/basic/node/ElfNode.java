package elfEngine.basic.node;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.cocos2d.actions.ActionManager;
import org.cocos2d.actions.base.CCAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

import com.ielfgame.stupidGame.ActionData;
import com.ielfgame.stupidGame.ActionDataConfig;
import com.ielfgame.stupidGame.ResJudge;
import com.ielfgame.stupidGame.NodeView.NodeViewWorkSpaceTab;
import com.ielfgame.stupidGame.bitmapTool.ImageHelper;
import com.ielfgame.stupidGame.config.ProjectSetting;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.data.SpellHelper;
import com.ielfgame.stupidGame.data.Stringified;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashManager;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashStruct.IFlashMain;
import com.ielfgame.stupidGame.platform.PlatformHelper;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.reflect.ReflectConstants;
import com.ielfgame.stupidGame.res.ResManager;
import com.ielfgame.stupidGame.trans.TransferRes;
import com.ielfgame.stupidGame.undo.UndoRedoManager;
import com.ielfgame.stupidGame.utils.FileHelper;
import com.ielfgame.stupidGame.xml.IXMLChilds;

import elfEngine.basic.list.ElfList;
import elfEngine.basic.list.ElfOrderedList;
import elfEngine.basic.math.MathCons;
import elfEngine.basic.modifier.INodeModifier;
import elfEngine.basic.node.extend.ElfScreenNode;
import elfEngine.basic.node.helper.NodeUtils;
import elfEngine.basic.node.nodeAnimate.timeLine.NodePropertyType;
import elfEngine.basic.node.nodeList.IClipable;
import elfEngine.basic.ordinal.IElfOrdinal;
import elfEngine.basic.pool.ElfPool;
import elfEngine.basic.pool.ElfPoolItem;
import elfEngine.basic.pool.IElfPool;
import elfEngine.basic.pool.IElfPoolItem;
import elfEngine.basic.pool.INewInstance;
import elfEngine.basic.touch.ElfEvent;
import elfEngine.basic.touch.IElfOnTouch;
import elfEngine.basic.touch.MotionEvent;
import elfEngine.extend.ElfConfig;
import elfEngine.graphics.PointF;
import elfEngine.graphics.Rectangle;
import elfEngine.opengl.BlendMode;
import elfEngine.opengl.BufferHelper;
import elfEngine.opengl.DrawHelper;
import elfEngine.opengl.GLHelper;
import elfEngine.opengl.GLManage;
import elfEngine.opengl.Texture;
import elfEngine.opengl.TextureRegion;

/**
 * @author zju_wjf
 */
public class ElfNode implements IElfOrdinal, IElfPoolItem, ElfConfig, IElfOnTouch, IXMLChilds, ReflectConstants {

	protected final static String INNER_NODE_KEY = "inner";

	protected final static String getDefaultInnerName(final ElfNode node) {
		return "#" + INNER_NODE_KEY + "_" + node.getClass().getSimpleName();
	}

	protected final static boolean isInnerNode(final ElfNode node) {
		final String name = node.getName();
		return name != null && name.contains(INNER_NODE_KEY);
	}

	/**
	 * for Identity a node as
	 */
	private int mIdentityID;

	public void setIdentityID(final int id) {
		mIdentityID = id;
	}

	public int getIdentityID() {
		// System.err.println(""+this.getName()+"->"+mIdentityID);
		return mIdentityID;
	}

	private final static IdentityMaker sIdentityMaker = new IdentityMaker();

	public ElfNode(ElfNode father, int ordinal) {
		mFatherNode = father;
		setOrdinal(ordinal);
		setName("#" + this.getClass().getSimpleName().replace("Node", "").toLowerCase());

		this.setIdentityID(sIdentityMaker.createIdentityId());
	}

	public ElfNode copySelf(ElfNode father) {
		return copySelf(father, false);
	}
	
	public ElfNode copySelf(ElfNode father, boolean copId) {
		Class<?> _class = this.getClass();
		try {
			final ElfNode node = NodeUtils.newInstacne(_class, father, this.ordinal());
			
			copyFrom(node, this);
			if (copId) {
				node.setIdentityID(this.getIdentityID());
			}
			return node;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// for new create
	public void onCreateRequiredNodes() {
	}

	// for xml...after
	public void onRecognizeRequiredNodes() {
	}

	public static boolean isSameNodeDeep(ElfNode dst, ElfNode src) {
		boolean ret = isSameNode(dst, src);
		if (!ret) {
			return false;
		}
		final ElfNode[] c1 = dst.getChilds();
		final ElfNode[] c2 = src.getChilds();

		if (c1.length != c2.length) {
			return false;
		}

		for (int i = 0; i < c1.length; i++) {
			final ElfNode cc1 = c1[i];
			final ElfNode cc2 = c2[i];
			if (!isSameNodeDeep(cc1, cc2)) {
				return false;
			}
		}

		return true;
	}

	public static boolean isSameNode(ElfNode dst, ElfNode src) {
		if (dst != null && src != null) {
			if (dst == src) {
				return true;
			}

			if (dst.getClass() != src.getClass()) {
				return false;
			}

			Class<?> _class = src.getClass();

			try {
				final Stack<Class<?>> stack = new Stack<Class<?>>();
				while (_class != null && ElfNode.class.isAssignableFrom(_class)) {
					stack.add(_class);
					_class = _class.getSuperclass();
				}

				while (!stack.isEmpty()) {
					final Class<?> myClass = stack.pop();
					final Field[] fs = myClass.getDeclaredFields();

					for (int i = 0; i < fs.length; i++) {
						try {
							final Field f = fs[i];
							final String name = f.getName();

							if (name.startsWith("REF_")) {
								final String funcName = name.substring(4);
								try {
									f.setAccessible(true);
									final int mask = f.getInt(null);
									if ((mask & ElfNode.XML_GET_SHIFT) != 0 || (mask & ElfNode.XML_SET_SHIFT) != 0 || (mask & ElfNode.XML_ALL_SHIFT) != 0) {
										final Method[] methods = myClass.getMethods();
										for (int j = 0; j < methods.length; j++) {
											try {
												final Method method = methods[j];
												final String methodName = method.getName();
												if (methodName.equals("get" + funcName)) {

													final Object valueSrc = method.invoke(src);
													String outSrc = Stringified.toText(valueSrc, false);

													final Object valueDst = method.invoke(dst);
													String outDst = Stringified.toText(valueDst, false);

													if (!outSrc.equals(outDst)) {
														return false;
													}
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	private static class MethodPair {
		public Method get;
		public Method set;
	}

	private static HashMap<Class<?>, ArrayList<Class<?>>> sClassArrayMap = new HashMap<Class<?>, ArrayList<Class<?>>>();
	private static HashMap<Class<?>, ArrayList<MethodPair>> sMethodArrayMap = new HashMap<Class<?>, ArrayList<MethodPair>>();

	private static ArrayList<Class<?>> getClassArrayList(Class<?> _class) {
		final ArrayList<Class<?>> ret = sClassArrayMap.get(_class);
		if (ret != null) {
			return ret;
		}

		try {
			final ArrayList<Class<?>> array = new ArrayList<Class<?>>();
			sClassArrayMap.put(_class, array);
			
			while (_class != null && ElfNode.class.isAssignableFrom(_class)) {
				array.add(_class);
				_class = _class.getSuperclass();
			}
			Collections.reverse(array);

			return array;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static ArrayList<MethodPair> getMethodArrayList(final Class<?> _class) {
		final ArrayList<MethodPair> ret = sMethodArrayMap.get(_class);
		if (ret != null) {
			return ret;
		}

		final ArrayList<MethodPair> array = new ArrayList<MethodPair>();

		final Class<?> myClass = _class;
		final Field[] fs = myClass.getDeclaredFields();
		
		for (int i = 0; i < fs.length; i++) {
			try {
				final Field f = fs[i];
				final String name = f.getName();

				if (name.startsWith("REF_")) {
					final String funcName = name.substring(4);
					try {
						f.setAccessible(true);
						final int mask = f.getInt(null);
						if ((mask & ElfNode.XML_ALL_SHIFT) != 0 || (mask & ElfNode.BACKGROUND_SHIFT) != 0) {
							final Method[] methods = myClass.getMethods();
							for (int j = 0; j < methods.length; j++) {
								try {
									final Method method = methods[j];
									final String methodName = method.getName();
									if (methodName.equals("get" + funcName)) {
										try {
											final Class<?> returnType = method.getReturnType();
											final Method setMethod = myClass.getMethod("set" + funcName, returnType);
											
											if (setMethod != null) {
												
												final MethodPair mp = new MethodPair();
												mp.set = setMethod;
												mp.get = method;
												
												array.add(mp);
											}

										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		sMethodArrayMap.put(_class, array);
		
		return array;
	}

	public final static void copyFrom(ElfNode dst, ElfNode src) {
		// if(dst == null || src == null) {
		// return;
		// }
		final ArrayList<Class<?>> dstClassArray = getClassArrayList(dst.getClass());
//		final ArrayList<Class<?>> srcClassArray = getClassArrayList(src.getClass());
		
		for (int i = 0; i < dstClassArray.size() ; i++) {
			final Class<?> dstClass = dstClassArray.get(i);
//			final Class<?> srcClass = srcClassArray.get(i);
//			if (dstClass == srcClass) {
				final ArrayList<MethodPair> methodPairArray = getMethodArrayList(dstClass);
				
				if (methodPairArray != null) {
					for (MethodPair mp : methodPairArray) {
//						final Class<?> returnType = mp.get.getReturnType();
						
						final String getName = mp.get.getName();
						
						try {
							final Method getM = src.getClass().getMethod(getName);
							if(getM != null) {
								final Object value = getM.invoke(src);
								mp.set.invoke(dst, value);
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
//				}
			} else {
				return;
			}
		}

//		Class<?> _class = src.getClass();
//		try {
//			final Stack<Class<?>> stack = new Stack<Class<?>>();
//			while (_class != null && ElfNode.class.isAssignableFrom(_class)) {
//				stack.add(_class);
//				_class = _class.getSuperclass();
//			}
//
//			final Class<?> yourClass = dst.getClass();
//
//			while (!stack.isEmpty()) {
//				final Class<?> myClass = stack.pop();
//				final Field[] fs = myClass.getDeclaredFields();
//
//				for (int i = 0; i < fs.length; i++) {
//					try {
//						final Field f = fs[i];
//						final String name = f.getName();
//
//						if (name.startsWith("REF_")) {
//							final String funcName = name.substring(4);
//							try {
//								f.setAccessible(true);
//								final int mask = f.getInt(null);
//								if ((mask & ElfNode.XML_GET_SHIFT) != 0 || (mask & ElfNode.XML_SET_SHIFT) != 0 || (mask & ElfNode.XML_ALL_SHIFT) != 0) {
//									final Method[] methods = myClass.getMethods();
//									for (int j = 0; j < methods.length; j++) {
//										try {
//											final Method method = methods[j];
//											final String methodName = method.getName();
//											if (methodName.equals("get" + funcName)) {
//												final Class<?> returnType = method.getReturnType();
//
//												final Object value = method.invoke(src);
//												String out = Stringified.toText(value, false);
//												final Object copyValue = Stringified.fromText(returnType, out)[0];
//												// final Object copyValue =
//												// value;
//
//												try {
//													final Method setMethod = yourClass.getMethod("set" + funcName, returnType);
//													setMethod.invoke(dst, copyValue);
//												} catch (Exception e) {
//													e.printStackTrace();
//												}
//											}
//										} catch (Exception e) {
//											e.printStackTrace();
//										}
//									}
//								}
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public ElfNode copyDeep() {
		return copyDeep(this.getParent());
	}

	public final ElfNode copyDeep(ElfNode newParent) {
		return copyDeep(newParent, false);
	}

	/*
	 * for undo-redo
	 */
	public final ElfNode copyDeep(ElfNode newParent, final boolean copyId) {
		final ElfNode ret = copySelf(newParent, copyId);

		final ElfList<ElfNode> list = getChildList();
		final ElfList<ElfNode>.Iterator it = list.iterator(true);
		int index = 0;
		while (it.hasNext()) {
			final ElfNode child = it.next();
			final ElfNode newChild = child.copyDeep(ret, copyId);
			newChild.setParent(ret);
			newChild.addToParent(index);
			index++;
		}

		ret.iterateChildsDeepWithSelf(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				node.onRecognizeRequiredNodes();
				return false;
			}
		});
		return ret;
	}

	public void setParent(ElfNode father) {
		mFatherNode = father;
	}

	public boolean isRecurFatherOf(ElfNode child) {
		while (child.mFatherNode != null) {
			if (child.mFatherNode == this) {
				return true;
			}
			child = child.mFatherNode;
		}
		return false;
	}

	/**
	 * ordinal ordinal >= 0
	 */
	private int mOrdinal = 0;

	public int ordinal() {
		return mOrdinal;
	}

	public void setOrdinal(final int ordinal) {
		mOrdinal = ordinal;
	}

	public void setOrdinal(final IElfOrdinal ordinal) {
		if (ordinal != null) {
			mOrdinal = ordinal.ordinal();
		} else {
			mOrdinal = -1;
		}
	}

	/**
	 * color assist
	 */
	public static final class ColorAssist {
		public float red = -1f;
		public float green = -1f;
		public float blue = -1f;
		public float alpha = -1f;

		public final void copy(final ColorAssist copy) {
			red = copy.red;
			green = copy.green;
			blue = copy.blue;
			alpha = copy.alpha;
		}

		public final void copy(final float alpha, final float red, final float green, final float blue) {
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.alpha = alpha;
		}

		public final void reset() {
			this.red = -1;
			this.green = -1;
			this.blue = -1;
			this.alpha = -1;
		}
	}

	protected ColorAssist mColorAssist = new ColorAssist();

	public ColorAssist getColorAssist() {
		return mColorAssist;
	}

	/**
	 * data map
	 */
	private final HashMap<String, Object> mDataMap = new HashMap<String, Object>();

	public final void setData(String key, Object value) {
		mDataMap.put(key, value);
	}

	public final Object getData(String key) {
		return mDataMap.get(key);
	}

	/**
	 * Delay
	 */
	private static ElfPool sPpool = new ElfPool();
	private static final int sInitPoolSize = 32;

	private static final class DelayUnion extends ElfPoolItem implements INewInstance {
		private float mDelay;
		private Runnable mRun;

		private DelayUnion() {
			super(sPpool);
		}

		public IElfPoolItem newInstance() {
			return new DelayUnion();
		}
	}

	static {
		sPpool.fillPool(new DelayUnion(), sInitPoolSize);
	}
	private boolean mIsUseUpdateHandler = false;

	public final void setUseDelayHandler(final boolean isUseUpdateHandler) {
		mIsUseUpdateHandler = isUseUpdateHandler;
	}

	public final boolean isUseUpdateHandler() {
		return mIsUseUpdateHandler;
	}

	private ElfList<DelayUnion> mDelayList = new ElfList<DelayUnion>();

	public final void runWithDelay(final Runnable run, final float delay) {
		final DelayUnion delayUnion = (DelayUnion) sPpool.getFreshItem();
		delayUnion.mRun = run;
		delayUnion.mDelay = delay;
		mDelayList.insertLast(delayUnion);
	}

	protected final void updateDelayList(final float pMsElapsed) {
		final ElfList<DelayUnion>.Iterator it = mDelayList.iterator(true);
		while (it.hasNext()) {
			if (mIsClearUpdateListFlag) {
				mIsClearUpdateListFlag = false;
			}
			final DelayUnion delayUnion = it.next();
			if (delayUnion.mDelay <= 0) {
				delayUnion.mRun.run();

				if (mIsClearUpdateListFlag) {// just do clear in the run
					mIsClearUpdateListFlag = false;
					break;
				} else {
					delayUnion.mRun = null;
					delayUnion.recycle();
					it.remove();
				}

			} else {
				delayUnion.mDelay -= pMsElapsed;
			}
		}
	}

	private boolean mIsClearUpdateListFlag = false;

	public final void clearUpdateList() {
		final ElfList<DelayUnion>.Iterator it = mDelayList.iterator(true);
		while (it.hasNext()) {
			final DelayUnion delayUnion = it.next();
			delayUnion.mRun = null;
			delayUnion.recycle();
			// delayUnion = null;
			it.remove();
		}
		mIsClearUpdateListFlag = true;
	}

	public String toString() {
		return getName();
	}

	/**
	 * modifiers
	 */

	private ElfList<INodeModifier> mModifierList = new ElfList<INodeModifier>();

	public final ElfList<INodeModifier> getModifiers() {
		return mModifierList;
	}

	public void clearModifiers() {
		mModifierList.clear();
	}

	public final void addModifier(final INodeModifier modifier) {
		// if(modifier!=null){
		mModifierList.insertLast(modifier);
		// }
	}

	public final void removeModifier(final INodeModifier modifier) {
		mModifierList.remove(modifier);
	}

	public final void resetModifiers() {
		final ElfList<INodeModifier>.Iterator it = mModifierList.iterator(true);
		while (it.hasNext()) {
			final INodeModifier modifier = it.next();
			modifier.reset();
		}
	}

	private final void modifierCalc(final float pMsElapsed) {
		final ElfList<INodeModifier>.Iterator it = mModifierList.iterator(true);
		while (it.hasNext()) {
			final INodeModifier modifier = it.next();
			if (!modifier.isFinished()) {

				modifier.count(pMsElapsed);

				if (modifier.isDead()) {
					modifier.setFinished();

					if (modifier.isRemovable()) {
						it.remove();
					}

					modifier.setProgress(modifier.getLife());
					modifier.modifier(this);
					modifier.onModifierFinished(this);

				} else {
					modifier.modifier(this);
				}
			}
		}
	}

	/**
	 * culling
	 */
	// private boolean mCullingEnabled = false;
	// public boolean isCullingEnabled() {
	// return mCullingEnabled;
	// }
	//
	// public void setCullingEnabled(final boolean pCullingEnabled) {
	// mCullingEnabled = pCullingEnabled;
	// }

	/*************************************************************************
	 * ***********************************************************************
	 * ***********************************************************************
	 */
	private ElfNode mFatherNode;
	private final ElfOrderedList<ElfNode> mChildList = new ElfOrderedList<ElfNode>();

	/**
	 * @param father
	 *            has not added to new father
	 */
	// public void changeFather(final ElfBasicNode father){
	// if(mFatherNode!=null)
	// mFatherNode.removeChild(this);
	// mFatherNode = father;
	//
	// }
	public ElfNode getParent() {
		return mFatherNode;
	}

	public ElfNode getTopNode() {
		ElfNode ret = this, tmp = mFatherNode;
		while (tmp != null) {
			ret = tmp;
			tmp = tmp.mFatherNode;
		}
		return ret;
	}

	public void onAddChild(final ElfNode child) {

	}

	public void onRemoveChild(final ElfNode child) {

	}

	/**
	 * 
	 */
	public void addToParent() {
		if (mFatherNode != null) {
			if (mFatherNode.mIsInRunningNode) {
				mGLid = mFatherNode.mGLid;
				mIsInRunningNode = true;
				onBorned();
				iterateChildsDeep(new IIterateChilds() {
					public boolean iterate(ElfNode node) {
						node.mGLid = mGLid;
						node.mIsInRunningNode = true;
						node.onBorned();
						return false;
					}
				});
			}
			// mFatherNode.removeChild(this);
			mFatherNode.addChild(this);
		}
	}

	public final void addToParent(int newIndex) {
		if (mFatherNode != null) {
			if (mFatherNode.mIsInRunningNode) {
				mGLid = mFatherNode.mGLid;
				mIsInRunningNode = mFatherNode.mIsInRunningNode;
				onBorned();
				iterateChildsDeep(new IIterateChilds() {
					public boolean iterate(ElfNode node) {
						node.mGLid = mGLid;
						node.mIsInRunningNode = mFatherNode.mIsInRunningNode;
						node.onBorned();
						return false;
					}
				});
			}

			// mFatherNode.removeChild(this);
			mFatherNode.addChild(this, newIndex);
		}
	}

	public void addToParentView() {
		addToParentView(Integer.MAX_VALUE);
	}

	public final void addToParentView(int newIndex) {
		final NodeViewWorkSpaceTab nodeViewWorkSpaceTab = PowerMan.getSingleton(NodeViewWorkSpaceTab.class);
		if (nodeViewWorkSpaceTab != null) {
			nodeViewWorkSpaceTab.addNode(mFatherNode, this, newIndex, true);
		} else {
			addToParent(newIndex);
		}
	}

	public int indexOfFather() {
		if (mFatherNode == null) {
			return 0;
		}
		final ElfNode father = mFatherNode;
		final ElfList<ElfNode> list = father.getChildList();
		int index = 0;
		final ElfList<ElfNode>.Iterator it = list.iterator(true);
		while (it.hasNext()) {
			if (it.next() == this) {
				return index;
			}
			index++;
		}
		return 0;
	}

	public void onBorned() {
		if (mIsUseBornListener) {
			final ElfList<IBornListener>.Iterator it = mBornListeners.iterator(true);
			while (it.hasNext()) {
				it.next().onBorn(this);
			}
		}
	}

	// public void addToFatherWithCheck(){
	// if(!mFatherNode.containsChild(this)){
	// mFatherNode.addChild(this);
	// }
	// }

	/**
	 * @ remove self from father
	 */
	public void removeFromParent() {
		if (mIsInRunningNode) {
			mIsInRunningNode = false;
			onDead();

			iterateChildsDeep(new IIterateChilds() {
				public boolean iterate(ElfNode node) {
					node.mIsInRunningNode = false;
					node.onDead();
					return false;
				}
			});
		}

		if (mFatherNode != null) {
			mFatherNode.removeChild(this);
		}
		clearBufferMemory();
	}

	public void removeFromParentView(boolean recycle) {
		final NodeViewWorkSpaceTab nodeViewWorkSpaceTab = PowerMan.getSingleton(NodeViewWorkSpaceTab.class);
		if (nodeViewWorkSpaceTab != null) {
			if (recycle) {
				nodeViewWorkSpaceTab.recycleNode(this);
			} else {
				nodeViewWorkSpaceTab.removeNode(this);
			}
		} else {
			removeFromParent();
		}
	}

	public void onDead() {
		if (mIsUseDeadListener) {
			final ElfList<IDeadListener>.Iterator it = mDeadListeners.iterator(true);
			while (it.hasNext()) {
				it.next().onDead(this);
			}
		}
	}

	private boolean mIsUseDeadListener = true;

	private ElfList<IDeadListener> mDeadListeners = new ElfList<IDeadListener>();

	public void addDeadListener(final IDeadListener deadListener) {
		mDeadListeners.insertLast(deadListener);
	}

	public void removeDeadListener(final IDeadListener deadListener) {
		mDeadListeners.remove(deadListener);
	}

	public void clearDeadListeners() {
		mDeadListeners.clear();
	}

	public static interface IDeadListener {
		public void onDead(final ElfNode node);
	};

	private ElfList<IBornListener> mBornListeners = new ElfList<IBornListener>();
	private boolean mIsUseBornListener = true;

	// public void setUseBornListener(final boolean isUseBornListener){
	// mIsUseBornListener = isUseBornListener;
	// }
	public void addBornListener(final IBornListener bornListener) {
		mBornListeners.insertLast(bornListener);
	}

	public void removeBornListener(final IBornListener bornListener) {
		mBornListeners.remove(bornListener);
	}

	public void clearBornListeners() {
		mBornListeners.clear();
	}

	public static interface IBornListener {
		public void onBorn(final ElfNode node);
		// public
	};

	/**
	 * @param ordinal
	 *            set order and refresh the position in father list
	 */
	public void reorder(final IElfOrdinal ordinal) {
		this.reorder(ordinal.ordinal());
	}

	/**
	 * @param ordinal
	 *            set order and refresh the position in father list
	 */
	public final void reorder(final Enum<?> ordinal) {
		this.reorder(ordinal.ordinal());
	}

	/**
	 * @param ordinal
	 *            set order and refresh the position in father list
	 */
	public void reorder(final int ordinal) {
		if (ordinal != ordinal()) {
			this.setOrdinal(ordinal);
			resortInFather();
		}
	}

	private void resortInFather() {
		if (mFatherNode != null) {
			final ElfList<ElfNode>.Iterator it = mFatherNode.childIterator(this);
			if (it != null) {
				it.remove();
				// mFatherNode.addChild(this);
				if (this.mOrdinal < 0) {
					mFatherNode.mChildList.insertFirst(this);
				} else {
					mFatherNode.mChildList.insertAfter(this);
				}
			}
		}
	}

	protected void addChild(final ElfNode child) {
		
		if (child.mOrdinal < 0) {
			mChildList.insertFirst(child);
		} else {
			mChildList.insertAfter(child);
		}
		
		onAddChild(child);
	}

	protected void addChild(final ElfNode child, int newIndex) {

		final ElfList<ElfNode> list = mChildList;
		int index = 0;
		final ElfList<ElfNode>.Iterator it = list.iterator(true);
		while (it.hasNext()) {
			if (newIndex <= index) {
				it.insertAfter(child);
				it.next();
				int ordinal = it.next().ordinal();
				child.setOrdinal(ordinal);
				return;
			} else {
				it.next();
				index++;
			}
		}
		if (list.size() > 0) {
			child.setOrdinal(list.getLast().ordinal());
		} else {
			child.setOrdinal(0);
		}

		list.insertLast(child);
		
		onAddChild(child);
	}

	protected boolean removeChild(final ElfNode child) {
		onRemoveChild(child);
		return mChildList.remove(child);
	}

	public boolean containsChild(final ElfNode child) {
		return mChildList.contains(child);
	}

	private final ElfList<ElfNode>.Iterator childIterator(final ElfNode child) {
		return mChildList.iterator(child);
	}

	public int getChildsSize() {
		return mChildList.size();
	}

	public ElfList<ElfNode> getChildList() {
		return mChildList;
	}

	public boolean hasChild() {
		return !mChildList.isEmpty();
	}

	public void removeAllChilds() {
		final ElfList<ElfNode>.Iterator it = mChildList.iterator(true);
		while (it.hasNext()) {
			it.next().removeFromParent();
		}
	}

	/**
	 * @param ret
	 *            as return
	 * 
	 */
	public void selfXYtoScreen(final ElfPointf ret) {
		ElfNode node = mFatherNode;
		ret.x = this.mPosition.x;
		ret.y = this.mPosition.y;

		while (node != null) {
			node.childXYToSelf(ret.x, ret.y, ret);
			node = node.mFatherNode;
		}
		;
	}

	public void selfXYtoScreen(float x, float y, final ElfPointf ret) {
		ElfNode node = mFatherNode;
		ret.x = x;
		ret.y = y;
		while (node != null) {
			node.childXYToSelf(ret.x, ret.y, ret);
			node = node.mFatherNode;
		}
	}

	/**
	 * @param ret
	 *            as return
	 */
	public final void selfXYToFatherXY(final ElfPointf ret) {
		if (mFatherNode != null) {
			mFatherNode.childXYToSelf(this.mPosition.x, this.mPosition.y, ret);
		}
	}

	/**
	 * @param (x,y) child point,ret as return with this's XY(in father)
	 * @this.x-->child.x, this.y-->childY, ret.x-->father.x, ret.y-->father.y
	 */
	public final void childXYToSelf(float childX, float childY, final ElfPointf ret) {
		final ElfNode node = this;
		childX -= node.getWidth() * (node.mAnchorPosition.x - 0.5f);
		childY -= node.getHeight() * (node.mAnchorPosition.y - 0.5f);

		final float dx = childX * node.mScale.x;
		final float dy = childY * node.mScale.y;

		final float angle = node.mRotate * MathCons.RAD_TO_DEG;
		final float cos = (float) Math.cos(angle);
		final float sin = (float) Math.sin(angle);

		ret.x = node.mPosition.x + dx * cos + dy * sin;
		ret.y = node.mPosition.y + dy * cos - dx * sin;
	}

	public final void childXYToScreen(float childX, float childY, final ElfPointf ret) {
		// childXYToSelf(childX, childY, ret);
		// selfXYtoScreen(ret.x, ret.y, ret);
		ret.x = childX;
		ret.y = childY;
		ElfNode node = this;
		while (node != null) {
			node.childXYToSelf(ret.x, ret.y, ret);
			node = node.mFatherNode;
		}
	}

	/**
	 * @param fatherX
	 * @param fatherY
	 * @param ret
	 * @param (x,y) child point,ret as return with this's XY(in father) fatherX
	 *        -->this.x, father.y-->this.y ret.x-->child.x, ret.y-->child.y
	 */
	public final void selfXYToChild(final float fatherX, final float fatherY, ElfPointf ret) {
		if (this == null) {
			ret.x = fatherX;
			ret.y = fatherY;
		} else {
			final float angle = this.mRotate * MathCons.RAD_TO_DEG;
			final float cos = (float) Math.cos(angle);
			final float sin = (float) Math.sin(angle);
			/*
			 * ret.x = mFatherNode.getCentreX()+dx*cos+dy*sin;*cos, *sin ret.y =
			 * mFatherNode.getCentreY()+dy*cos-dx*sin;*sin, *cos
			 */
			final float tempDx = fatherX - this.mPosition.x;
			final float tempDy = fatherY - this.mPosition.y;
			final float dx = ((tempDx) * cos - (tempDy) * sin);
			final float dy = ((tempDx) * sin + (tempDy) * cos);

			if (this.mScale.x == 0) {
				ret.x = Float.NaN;
			} else {
				ret.x = dx / this.mScale.x;
			}

			if (this.mScale.y == 0) {
				ret.y = Float.NaN;
			} else {
				ret.y = dy / this.mScale.y;
			}

			ret.x += this.getWidth() * (this.mAnchorPosition.x - 0.5f);
			ret.y += this.getHeight() * (this.mAnchorPosition.y - 0.5f);
		}
	}

	public final void screenXYToChild(final float screenX, final float screenY, ElfPointf ret) {
		ret.x = screenX;
		ret.y = screenY;

		final ElfList<ElfNode> fatherList = new ElfList<ElfNode>();

		ElfNode tmp = this;
		while (tmp != null) {
			fatherList.insertFirst(tmp);
			tmp = tmp.mFatherNode;
		}

		final ElfList<ElfNode>.Iterator itHead = fatherList.iterator(true);
		while (itHead.hasNext()) {
			final ElfNode node = itHead.next();
			node.selfXYToChild(ret.x, ret.y, ret);
		}
	}

	public final void screenXYToSelf(final float screenX, final float screenY, ElfPointf ret) {
		ret.x = screenX;
		ret.y = screenY;
		if (mFatherNode != null) {
			mFatherNode.screenXYToChild(screenX, screenY, ret);
		}
	}

	public void calcSprite(float pMsElapsed) {
		// TODO Auto-generated method stub
		if (!mPaused) {
			pMsElapsed *= mElapsedRate;

			// children first
			if (!mChildList.isEmpty()) {
				final ElfList<ElfNode>.Iterator it = mChildList.iterator(true);
				pMsElapsed *= mChildTimeRate;
				while (it.hasNext()) {
					it.next().calcSprite(pMsElapsed);
				}
			}

			/**
			 * update handles
			 */
			if (mIsUseUpdateHandler) {
				updateDelayList(pMsElapsed);
			}

			/**
			 * update modifiers
			 */
			if (mIsUseModifiers) {
				modifierCalc(pMsElapsed);
			}

			/**
			 * to be override
			 */
			calc(pMsElapsed);
		}
	}

	/**
	 * @param pMsElapse
	 *            for override
	 */
	protected void calc(final float pMsElapsed) {
	}

	/**
	 * draw assist
	 */
	// private static BlendMode sBlendMode = null;
	// private static ColorAssist sColorAssist = new ColorAssist();

	public void drawSprite() {
		if (mVisible) {
			drawBefore();
			drawSelf();
			drawChilds();
			drawAfter();
		}
	}

	public final void drawSpriteSelected() {
		if (mVisible) {
			drawSelectBefore();
			drawSelected();
			drawChildsSelected();
			drawSelectAfter();
		}
	}

	// private static float sStaticInSideZOffset = 0;
	// protected static final float sStaticInsideOffsetMinStep = 0.00001f;

	public static void resetStaticInSideZOffset() {
		// sStaticInSideZOffset = 0;
	}

	/**
	 * @scale, roate
	 */
	protected void transformToAnchor() {
		GLHelper.glTranslatef(mPosition.x, mPosition.y, mZ);
		// sStaticInSideZOffset += sStaticInsideOffsetMinStep;
		if (mRotate != 0) {
			GLHelper.glRotatef(mRotate);
		}
		if (mScale.x != 1 || mScale.y != 1) {
			GLHelper.glScalef(mScale.x, mScale.y);
		}
	}

	protected final void transformToCenter() {
		final float w = getWidth();
		final float h = getHeight();

		GLHelper.glTranslatef(-w * (mAnchorPosition.x - 0.5f), -h * (mAnchorPosition.y - 0.5f), 0);
	}

	protected void drawSelectBefore() {
		GLHelper.glPushMatrix();
		this.transformToAnchor();
		this.transformToCenter();
	}

	protected void drawSelectAfter() {
		GLHelper.glPopMatrix();
	}

	private static TextureRegion sTextureRegionRotate;
	private static TextureRegion sTextureRegionScale;

	private static TextureRegion sTextureRegionCenter;
	private static TextureRegion sTextureRegionAlpha;

	private static ElfNode sStaticSelectNode;

	// public static void setStaticSelectNode(ElfNode node ) {
	// sStaticSelectNode = node;
	// }
	public static ElfNode getStaticSelectNode() {
		return sStaticSelectNode;
	}

	protected void drawSelected() {
		if (isSelected()) {
			final float width = getWidth();
			final float height = getHeight();

			GLHelper.glBlendFunc(BlendMode.BLEND.sourceBlendFunction, BlendMode.BLEND.destinationBlendFunction);

			if (PowerMan.getSingleton(DataModel.class).getSelectNode() == this) {
				GLHelper.glColor4f(1, 0, 0, 1);
			} else if (!getCouldMove()) {
				GLHelper.glColor4f(0, 0, 1, 1);
			} else if (this != sStaticSelectNode) {
				GLHelper.glColor4f(0, 1, 0, 1);
			} else {
				GLHelper.glColor4f(0.7f, 0.7f, 0.7f, 0.7f);
			}

			float scaleX = 1, scaleY = 1;
			ElfNode node = this;
			while (node != null) {
				scaleX *= node.mScale.x;
				scaleY *= node.mScale.y;
				node = node.mFatherNode;
			}

			final float thickX = 1 / scaleY;
			final float thickY = 1 / scaleX;

			DrawHelper.fillRect(width + thickY * 2, thickX, 0, height / 2 + thickX);
			DrawHelper.fillRect(width + thickY * 2, thickX, 0, -(height / 2 + thickX));

			DrawHelper.fillRect(thickY, height + thickX * 2, width / 2 + thickY, 0);
			DrawHelper.fillRect(thickY, height + thickX * 2, -(width / 2 + thickY), 0);

			GLHelper.glColor4f(1, 1, 1, 1);

			if (mIsUseControl) {
				sTextureRegionAlpha.draw(getCurrentAlphaMoveX(), height / 2, 1 / scaleX, 1 / scaleY, 0);
				sTextureRegionCenter.draw((mAnchorPosition.x - 0.5f) * width, (mAnchorPosition.y - 0.5f) * height, 1 / scaleX, 1 / scaleY, 0);

				final ElfPointf rotateControlPoint = getRotateControlPoint();
				sTextureRegionRotate.draw(width * rotateControlPoint.x, height * rotateControlPoint.y, 1 / scaleX, 1 / scaleY, 0);
				// sTextureRegionRotate2.draw(width / 2, height / 2, 1 / scaleX,
				// 1 / scaleY, 0);

				final ElfPointf scaleControlPoint = getScaleControlPoint();
				sTextureRegionScale.draw(width * scaleControlPoint.x, height * scaleControlPoint.y, 1 / scaleX, 1 / scaleY, 0);
				// sTextureRegionScale2.draw(-width / 2, height / 2, 1 / scaleX,
				// 1 / scaleY, 0);
			}
		}
	}

	protected void drawBefore() {
		prepareBlendAndColor();
		GLHelper.glPushMatrix();
		this.transformToAnchor();
		this.transformToCenter();
	}

	protected void prepareBlendAndColor() {
		// xiuzhen
		GLHelper.glBlendFunc(mBlendMode.sourceBlendFunction, mBlendMode.destinationBlendFunction);

		if (mFatherNode == null) {
			final ColorAssist ca = mColorAssist;
			ca.red = mColor.red;
			ca.green = mColor.green;
			ca.blue = mColor.blue;
			ca.alpha = mColor.alpha;
			GLHelper.glColor4f(ca.red, ca.green, ca.blue, ca.alpha);
		} else {
			final ColorAssist fca = mFatherNode.mColorAssist;
			final ColorAssist ca = mColorAssist;
			if (fca.red != 1) {
				ca.red = mColor.red * fca.red;
			} else {
				ca.red = mColor.red;
			}

			if (fca.green != 1) {
				ca.green = mColor.green * fca.green;
			} else {
				ca.green = mColor.green;
			}

			if (fca.blue != 1) {
				ca.blue = mColor.blue * fca.blue;
			} else {
				ca.blue = mColor.blue;
			}

			if (fca.alpha != 1) {
				ca.alpha = mColor.alpha * fca.alpha;
			} else {
				ca.alpha = mColor.alpha;
			}
			GLHelper.glColor4f(ca.red, ca.green, ca.blue, ca.alpha);
		}
	}

	protected void drawAfter() {
		GLHelper.glPopMatrix();
	}

	//
	private boolean mDrawReverse = false;

	public void setDrawReverse(final boolean reverse) {
		mDrawReverse = reverse;
	}

	public boolean getDrawReverse() {
		return mDrawReverse;
	}

	protected void drawChilds() {
		if (!mChildList.isEmpty()) {
			if (mDrawReverse) {
				final ElfList<ElfNode>.Iterator it = mChildList.iterator(false);
				while (it.hasPrev()) {
					it.prev().drawSprite();
				}
			} else {
				final ElfList<ElfNode>.Iterator it = mChildList.iterator(true);
				while (it.hasNext()) {
					it.next().drawSprite();
				}
			}
		}
	}

	private final void drawChildsSelected() {
		if (!mChildList.isEmpty()) {
			final ElfList<ElfNode>.Iterator it = mChildList.iterator(true);
			while (it.hasNext()) {
				it.next().drawSpriteSelected();
			}
		}
	}

	/**
	 * @param gl
	 *            for override
	 */
	private boolean mSelected = false;

	public final boolean isSelected() {
		return mSelected || sStaticSelectNode == this;
	}

	public void setSelected(boolean isSelected) {
		mSelected = isSelected;
	}

	protected void drawPic() {
		GLManage.draw(mResid, mGLid);
	}

	// ElfColor display, ElfColor rb, ElfColor lb, ElfColor lt, ElfColor rt
	protected void drawPic(ElfColor rb, ElfColor lb, ElfColor lt, ElfColor rt) {
		GLManage.draw(mResid, mGLid, mColorAssist, rb, lb, lt, rt);
	}

	protected final void drawPic(String pic) {
		GLManage.draw(pic, mGLid);
	}

	protected void drawSelf() {
		drawPic();
	}

	protected int mGLid = 0;

	public int getGLId() {
		return mGLid;
	}

	public void setGLid(final int id) {
		if (id != mGLid) {
			mGLid = id;
			this.iterateChilds(new IIterateChilds() {
				public boolean iterate(ElfNode node) {
					node.setGLid(id);
					return false;
				}
			});
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see elfEngine.basic.pool.IElfPoolItem#recycle()
	 */
	private IElfPool mPool;

	public void setPool(IElfPool pool) {
		mPool = pool;
	}

	public IElfPool getPool() {
		return mPool;
	}

	@Override
	public void recycle() {
		if (mPool != null) {
			mPool.recycle(this);
		}
	}

	/**
	 * deep
	 */
	public int getNodeDepth() {
		if (mFatherNode == null) {
			return 0;
		} else {
			return 1 + mFatherNode.getNodeDepth();
		}
	}

	/**
	 * 
	 */
	// @Deprecated
	public void reset() {
		// resetModifiers();
	}

	public boolean isInFather() {
		if (mFatherNode == null) {
			return false;
		} else {
			return mFatherNode.containsChild(this);
		}
	}

	/**
	 * touch area scale
	 */
	private float mTouchAreaScaleX = 1, mTouchAreaScaleY = 1;

	public void setTouchAreaScale(final float sx, final float sy) {
		mTouchAreaScaleX = sx;
		mTouchAreaScaleY = sy;
	}

	public void setTouchAreaScaleX(final float sx) {
		mTouchAreaScaleX = sx;
	}

	public void setTouchAreaScaleY(final float sy) {
		mTouchAreaScaleY = sy;
	}

	public float getTouchAreaScaleX() {
		return mTouchAreaScaleX;
	}

	public float getTouchAreaScaleY() {
		return mTouchAreaScaleY;
	}

	public static interface ICollideListener {
		public boolean collide(final ElfNode node1, final ElfNode node2);
	}

	public static final void resetStatic() {
		if (sTextureRegionRotate != null) {
			sTextureRegionRotate.texture.invalid();
			GLManage.checkOut(sTextureRegionRotate.texture);
		}
		Texture texture = new Texture(ElfNode.class.getResourceAsStream("rotate1.png"));
		texture.load();
		sTextureRegionRotate = new TextureRegion(texture);

		// if (sTextureRegionRotate2 != null) {
		// sTextureRegionRotate2.mTexture.invalid();
		// GLManage.checkOut(sTextureRegionRotate2.mTexture);
		// }
		// texture = new
		// Texture(ElfNode.class.getResourceAsStream("rotate2.png"));
		// texture.load();
		// sTextureRegionRotate2 = new TextureRegion(texture);

		if (sTextureRegionScale != null) {
			sTextureRegionScale.texture.invalid();
			GLManage.checkOut(sTextureRegionScale.texture);
		}
		texture = new Texture(ElfNode.class.getResourceAsStream("scale1.png"));
		texture.load();
		sTextureRegionScale = new TextureRegion(texture);

		// if (sTextureRegionScale2 != null) {
		// sTextureRegionScale2.mTexture.invalid();
		// GLManage.checkOut(sTextureRegionScale2.mTexture);
		// }
		// texture = new
		// Texture(ElfNode.class.getResourceAsStream("scale2.png"));
		// texture.load();
		// sTextureRegionScale2 = new TextureRegion(texture);

		if (sTextureRegionCenter != null) {
			sTextureRegionCenter.texture.invalid();
			GLManage.checkOut(sTextureRegionCenter.texture);
		}
		texture = new Texture(ElfNode.class.getResourceAsStream("walkable.png"));
		texture.load();
		sTextureRegionCenter = new TextureRegion(texture);

		if (sTextureRegionAlpha != null) {
			sTextureRegionAlpha.texture.invalid();
			GLManage.checkOut(sTextureRegionAlpha.texture);
		}
		texture = new Texture(ElfNode.class.getResourceAsStream("color.png"));
		texture.load();
		sTextureRegionAlpha = new TextureRegion(texture);
	}

	/**
	 * when you did not need it any more!
	 */
	public void clearMemory() {
		clearBufferMemory();

		clearDeadListeners();
		clearModifiers();
		clearUpdateList();

		final ElfList<ElfNode>.Iterator it = mChildList.iterator(true);
		while (it.hasNext()) {
			it.next().clearMemory();
		}

		mChildList.clear();
		// mTotalChildList.clear();

		mTag = null;

	}

	private final void clearBufferMemory() {
	}

	private int mTouchOperate = 0;
	public static final int OPERATE_MOVE = 1;
	public static final int OPERATE_ROTATE = 2;
	public static final int OPERATE_SCALE = 4;
	public static final int OPERATE_SIZE = 8;
	public static final int OPERATE_ALPHA = 16;
	public static final int OPERATE_CENTER = 32;

	private final ElfPointf mLastTouchPoint = new ElfPointf();
	private final ElfPointf mLastTouchPoint2 = new ElfPointf();// for anchor
	private boolean mAlphaMoveLeft = false;

	public boolean isInSelectSize(ElfEvent event) {
		return isInSize(event);
	}

	public boolean isInSize(final PointF screenPos) {
		final ElfPointf ret = new ElfPointf();
		this.screenXYToChild(screenPos.x, screenPos.y, ret);
		return isInSelectRect(ret.x, ret.y);
	}
	
	public boolean isInRectangle(final Rectangle screenRect) {
		if(screenRect != null) {
			final ElfPointf ret1 = new ElfPointf();
			this.screenXYToChild(screenRect.left, screenRect.bottom, ret1);
			
			final ElfPointf ret2 = new ElfPointf();
			this.screenXYToChild(screenRect.right, screenRect.top, ret2);
			
			if(ret1.x * ret2.x <= 0 && ret1.y * ret2.y <= 0) {
				return true;
			}
		}
		return false;
	}

	public boolean onNextSelectTouch(ElfEvent event) {
		if (getTouchShielded()) {
			return false;
		}

		final ElfList<ElfNode>.Iterator it = mChildList.iterator(false);
		while (it.hasPrev()) {
			final ElfNode node = it.prev();
			if (node.onNextSelectTouch(event)) {
				return true;
			}
		}

		return this.onNextSelectTouchSelf(event);
	}

	public void onArrowKey(float dx, float dy) {
		if (this.getCouldMove()) {
			final ElfPointf pos = this.getPositionInScreen();
			pos.translate(dx, dy);
			this.setPositionInScreen(pos);
		}
	}

	protected final boolean onNextSelectTouchSelf(ElfEvent event) {
		if (event.action == MotionEvent.RIGHT_DOUBLE_CLICK) {
			final ElfPointf ret = convertTouchToSelf(event);
			final boolean isInSelectSize = isInSelectRect(ret.x, ret.y);

			if (isInSelectSize && isRealVisible(event)) {
				if ((event.stateMask & PlatformHelper.CTRL) != 0) {
					PowerMan.getSingleton(NodeViewWorkSpaceTab.class).addSelectNodes(this);
				} else {
					if (mSelected) {
						PowerMan.getSingleton(NodeViewWorkSpaceTab.class).setSelectNodes();
					} else {
						PowerMan.getSingleton(NodeViewWorkSpaceTab.class).setSelectNodes(this);
					}
				}
				return true;
			}
		}

		if (mTouchable && isRealVisible(event)) {
			return onTouch(event);
		} else {
			return false;
		}
	}

	protected final ElfPointf convertTouchToSelf(ElfEvent event) {
		final ElfPointf ret = new ElfPointf();
		this.screenXYToChild(event.x, event.y, ret);
		return ret;
	}

	private enum TouchArea {
		Anchor, Scale, Rotate, Alpha, Select, Size, OutSide
	}

	public boolean onPreSelectTouch(ElfEvent event) {
		final ElfList<ElfNode>.Iterator it = mChildList.iterator(false);
		while (it.hasPrev()) {
			final ElfNode node = it.prev();
			if (node.onPreSelectTouch(event)) {
				return true;
			}
		}

		return this.onPreSelectTouchSelf(event);
	}

	public boolean onPreSelectTouchSelf(ElfEvent event) {
		return false;
	}

	public boolean onSelectTouch(ElfEvent event) {
		if (event.action == MotionEvent.RIGHT_DOUBLE_CLICK || event.action == MotionEvent.RIGHT_CLICK || event.action == MotionEvent.RIGHT_DOWN || event.action == MotionEvent.RIGHT_MOVE || event.action == MotionEvent.RIGHT_UP) {
			return false;
		}

		final ElfList<ElfNode>.Iterator it = mChildList.iterator(false);
		while (it.hasPrev()) {
			final ElfNode node = it.prev();
			if (node.onSelectTouch(event)) {
				return true;
			}
		}

		return this.onSelectTouchSelf(event);
	}

	public boolean onSelectTouchSelf(ElfEvent event) {
		if (isSelected()) {
			final ElfPointf inFather = mFatherNode.convertTouchToSelf(event);
			final ElfPointf inSelf = convertTouchToSelf(event);

			TouchArea mode = TouchArea.OutSide;
			if (mIsUseControl && isInAnchorRect(inSelf.x, inSelf.y)) {
				mode = TouchArea.Anchor;// anchor
			} else if (mIsUseControl && isInAlphaRect(inSelf.x, inSelf.y)) {
				mode = TouchArea.Alpha;// alpha
			} else if (mIsUseControl && isInSizeRect(inSelf.x, inSelf.y)) {
				mode = TouchArea.Size;// size
			} else if (mIsUseControl && isInRotateRect(inSelf.x, inSelf.y)) {
				mode = TouchArea.Rotate;// rotate
			} else if (mIsUseControl && isInScaleRect(inSelf.x, inSelf.y)) {
				mode = TouchArea.Scale;// scale
			} else if (isInSelectRect(inSelf.x, inSelf.y)) {
				mode = TouchArea.Select;// move
			} else if (mTouchOperate == 0) {
				return false;
			}

			if (event.action == MotionEvent.LEFT_DOUBLE_CLICK) {
				UndoRedoManager.checkInUndo();

				switch (mode) {
				case Alpha:
					setAlpha(1);
					return true;
				case Anchor:
					final ElfPointf newFatherPos = new ElfPointf();
					this.childXYToSelf(0, 0, newFatherPos);

					setPosition(newFatherPos);
					setAnchorPosition(new ElfPointf(0.5f, 0.5f));
					return true;
				case Rotate:
					setRotate(0);
					return true;
				case Scale:
					setScale(new ElfPointf(1, 1));
					return true;
				case Size:
					final boolean useSize = getUseSettedSize();
					setUseSettedSize(!useSize);
					return true;
				case Select:
					final boolean control = getUseControl();
					setUseControl(!control);
					return true;
				case OutSide:
					return false;
				}
			} else if (event.action == MotionEvent.LEFT_DOWN) {
				if (mode != TouchArea.OutSide) {
					UndoRedoManager.checkInUndo();

					PowerMan.getSingleton(DataModel.class).setSelectNode(this);
					if (mode == TouchArea.Scale || mode == TouchArea.Rotate || mode == TouchArea.Size) {
						mLastTouchPoint.setPoint(inSelf.x, inSelf.y);
					} else if (mode == TouchArea.Anchor || mode == TouchArea.Alpha || mode == TouchArea.Select) {
						ArrayList<ElfNode> selects = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
						for (ElfNode _node : selects) {
							final ElfPointf _inFather = _node.mFatherNode.convertTouchToSelf(event);
							final ElfPointf _inSelf = _node.convertTouchToSelf(event);

							_node.mLastTouchPoint.setPoint(_inFather.x, _inFather.y);
							_node.mLastTouchPoint2.setPoint(_inSelf.x, _inSelf.y);
						}
					}
				}

				switch (mode) {
				case Alpha:
					mTouchOperate = OPERATE_ALPHA;
					return true;
				case Anchor:
					mTouchOperate = OPERATE_CENTER;
					return true;
				case Rotate:
					mTouchOperate = OPERATE_ROTATE;
					return true;
				case Scale:
					mTouchOperate = OPERATE_SCALE;
					return true;
				case Size:
					mTouchOperate = OPERATE_SIZE;
					return true;
				case Select:
					mTouchOperate = OPERATE_MOVE;
					return this.getCouldMove();
				case OutSide:
					return false;
				}
			} else if (event.action == MotionEvent.LEFT_MOVE) {
				if (mTouchOperate == OPERATE_MOVE) {
					ArrayList<ElfNode> selects = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
					for (ElfNode _node : selects) {
						final ElfPointf _inFather = _node.mFatherNode.convertTouchToSelf(event);
						if (_node.getCouldMove()) {
							if ((event.stateMask & PlatformHelper.CTRL) != 0) {
								final ElfNode[] nodes = _node.getChilds();
								final ElfPointf[] posScreens = new ElfPointf[nodes.length];
								for (int i = 0; i < posScreens.length; i++) {
									posScreens[i] = nodes[i].getPositionInScreen();
								}
								_node.translate(_inFather.x - _node.mLastTouchPoint.x, _inFather.y - _node.mLastTouchPoint.y);
								for (int i = 0; i < posScreens.length; i++) {
									nodes[i].setPositionInScreen(posScreens[i]);
								}
							} else {
								_node.translate(_inFather.x - _node.mLastTouchPoint.x, _inFather.y - _node.mLastTouchPoint.y);
							}
							_node.mLastTouchPoint.setPoint(_inFather.x, _inFather.y);
						} else {
							return false;
						}
					}
				} else if (mTouchOperate == OPERATE_ROTATE) {
					final float centerx = getWidth() * (mAnchorPosition.x - 0.5f);
					final float centery = getHeight() * (mAnchorPosition.y - 0.5f);

					final float oldAngle = (float) Math.atan2(mLastTouchPoint.y - centery, mLastTouchPoint.x - centerx);
					final float newAngle = (float) Math.atan2(inSelf.y - centery, inSelf.x - centerx);
					float newRotae = -(newAngle - oldAngle) * 180f / (float) Math.PI;
					if (this.getScale().x * this.getScale().y > 0) {
						transRotate(newRotae);
					} else {
						transRotate(-newRotae);
					}
					// chage
					this.screenXYToChild(event.x, event.y, mLastTouchPoint);
				} else if (mTouchOperate == OPERATE_SCALE) {
					final float centerx = getWidth() * (mAnchorPosition.x - 0.5f);
					final float centery = getHeight() * (mAnchorPosition.y - 0.5f);
					final float scaleX = (inSelf.x - centerx) / (mLastTouchPoint.x - centerx);
					final float scaleY = (inSelf.y - centery) / (mLastTouchPoint.y - centery);

					if ((event.stateMask & SWT.SHIFT) != 0) {
						final float scale = Math.min(scaleX, scaleY);
						if (mScale.x * scale != 0 && mScale.y * scale != 0) {
							setScale(mScale.x * scale, mScale.y * scale);
						}
					} else if (mScale.x * scaleX != 0 && mScale.y * scaleY != 0) {
						setScale(mScale.x * scaleX, mScale.y * scaleY);
					}
				} else if (mTouchOperate == OPERATE_SIZE) {
					// 1. if has resid? setusedsize
					// 2. if has text?
					// 3. change size?
					// 4.

					this.setSize(this.getScaleSize());

				} else if (mTouchOperate == OPERATE_ALPHA) {
					float rate = 2 * inSelf.x / getWidth();
					rate = Math.min(1, rate);
					rate = Math.max(-1, rate);
					setAlpha(1 - Math.abs(rate));
					mAlphaMoveLeft = rate < 0;
				} else if (mTouchOperate == OPERATE_CENTER) {
					final float w = getWidth();
					final float h = getHeight();

					transAnchorPosition((inSelf.x - mLastTouchPoint2.x) / w, (inSelf.y - mLastTouchPoint2.y) / h);
					translate(inFather.x - mLastTouchPoint.x, inFather.y - mLastTouchPoint.y);

					mLastTouchPoint.setPoint(inFather.x, inFather.y);
					mLastTouchPoint2.setPoint(inSelf.x, inSelf.y);
				} else {
					return false;
				}
			} else if (event.action == MotionEvent.LEFT_UP) {
				mTouchOperate = 0;
			}

			if (mode == TouchArea.Select) {
				return this.getCouldMove();
			}
		}

		return mSelected;
	}

	float getDiameterX() {
		float diameter = 8;
		ElfNode node = this;
		while (node != null) {
			diameter /= Math.abs(node.getScale().x);
			node = node.getParent();
		}
		return diameter;
	}

	float getDiameterY() {
		float diameter = 8;
		ElfNode node = this;
		while (node != null) {
			diameter /= Math.abs(node.getScale().y);
			node = node.getParent();
		}
		return diameter;
	}

	private final ElfPointf getSizeControlPoint() {
		final ElfPointf[] four = new ElfPointf[] { new ElfPointf(-0.5f, -0.5f), new ElfPointf(-0.5f, 0.5f), new ElfPointf(0.5f, -0.5f), new ElfPointf(0.5f, 0.5f), };

		final ElfPointf anchor = this.getAnchorPosition();
		anchor.translate(-0.5f, -0.5f);

		Arrays.sort(four, new Comparator<ElfPointf>() {
			public int compare(ElfPointf arg0, ElfPointf arg1) {
				final boolean bigger = (arg0.x - anchor.x) * (arg0.x - anchor.x) + (arg0.y - anchor.y) * (arg0.y - anchor.y) > ((arg1.x - anchor.x) * (arg1.x - anchor.x) + (arg1.y - anchor.y) * (arg1.y - anchor.y));
				return bigger ? -1 : 1;
			}
		});

		return four[2];
	}

	private final ElfPointf getRotateControlPoint() {
		final ElfPointf[] four = new ElfPointf[] { new ElfPointf(-0.5f, -0.5f), new ElfPointf(-0.5f, 0.5f), new ElfPointf(0.5f, -0.5f), new ElfPointf(0.5f, 0.5f), };

		final ElfPointf anchor = this.getAnchorPosition();
		anchor.translate(-0.5f, -0.5f);

		Arrays.sort(four, new Comparator<ElfPointf>() {
			public int compare(ElfPointf arg0, ElfPointf arg1) {
				final boolean bigger = (arg0.x - anchor.x) * (arg0.x - anchor.x) + (arg0.y - anchor.y) * (arg0.y - anchor.y) > ((arg1.x - anchor.x) * (arg1.x - anchor.x) + (arg1.y - anchor.y) * (arg1.y - anchor.y));
				return bigger ? -1 : 1;
			}
		});

		return four[1];
	}

	private final ElfPointf getScaleControlPoint() {
		final ElfPointf[] four = new ElfPointf[] { new ElfPointf(-0.5f, -0.5f), new ElfPointf(-0.5f, 0.5f), new ElfPointf(0.5f, -0.5f), new ElfPointf(0.5f, 0.5f), };

		final ElfPointf anchor = this.getAnchorPosition();
		anchor.translate(-0.5f, -0.5f);

		Arrays.sort(four, new Comparator<ElfPointf>() {
			public int compare(ElfPointf arg0, ElfPointf arg1) {
				final boolean bigger = (arg0.x - anchor.x) * (arg0.x - anchor.x) + (arg0.y - anchor.y) * (arg0.y - anchor.y) > ((arg1.x - anchor.x) * (arg1.x - anchor.x) + (arg1.y - anchor.y) * (arg1.y - anchor.y));
				return bigger ? -1 : 1;
			}
		});

		return four[0];
	}

	private final boolean isInControlRect(final float x, final float y, ElfPointf... controlPoints) {
		final float w = getWidth();
		final float h = getHeight();
		for (ElfPointf controlPoint : controlPoints) {
			if (isInRect(x - w * controlPoint.x, y - h * controlPoint.y, getDiameterX() * 2, getDiameterY() * 2)) {
				return true;
			}
		}
		return false;
	}

	// lt, rb
	private boolean isInScaleRect(final float x, final float y) {
		return isInControlRect(x, y, getScaleControlPoint());
	}

	private boolean isInRotateRect(final float x, final float y) {
		return isInControlRect(x, y, getRotateControlPoint());
	}

	private boolean isInSizeRect(final float x, final float y) {
		return isInControlRect(x, y, getSizeControlPoint());
	}

	private boolean isInAlphaRect(final float x, final float y) {
		final float h = getHeight();
		if (isInRect(x - getCurrentAlphaMoveX(), y - h / 2, getDiameterX() * 2, getDiameterY() * 2)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isInAnchorRect(final float x, final float y) {
		final float w = getWidth();
		final float h = getHeight();

		final float offX = (this.mAnchorPosition.x - 0.5f) * w;
		final float offy = (this.mAnchorPosition.y - 0.5f) * h;

		if (isInRect(x - offX, y - offy, getDiameterX() * 2, getDiameterY() * 2)) {
			return true;
		} else {
			return false;
		}
	}

	protected boolean isInSelectRect(final float x, final float y) {
		return isInRect(x, y, getWidth(), getHeight());
	}

	private boolean isInRect(float x, float y, float width, float height) {
		return (Math.abs(x) <= (width / 2)) && (Math.abs(y) <= (height / 2));
	}

	protected float mDefaultWidth = 50;
	protected float mDefaultHeight = 50;

	public void setDefaultWidth(float width) {
		mDefaultWidth = width;
	}

	public float getDefaultWidth() {
		return mDefaultWidth;
	}

	public void setDefaultHeight(float height) {
		mDefaultHeight = height;
	}

	public float getDefaultHeight() {
		return mDefaultHeight;
	}

	protected final static int REF_DefaultWidth = XML_ALL_SHIFT;
	protected final static int REF_DefaultHeight = XML_ALL_SHIFT;

	public final float getWidth() {
		return getSize().x;
	}

	public final float getHeight() {
		return getSize().y;
	}

	private float getCurrentAlphaMoveX() {
		final float width = getWidth();
		return mAlphaMoveLeft ? -(1 - mColor.alpha) / 2 * width : (1 - mColor.alpha) / 2 * width;
	}

	public void clearModifierDeep() {
		clearModifiers();
		ElfList<ElfNode>.Iterator it = mChildList.iterator(true);
		while (it.hasNext()) {
			it.next().clearModifierDeep();
		}
	}

	public boolean iterateChilds(IIterateChilds iterator) {
		final ElfList<ElfNode>.Iterator it = mChildList.iterator(true);
		while (it.hasNext()) {
			if (iterator.iterate(it.next())) {
				return true;
			}
		}
		return false;
	}

	public boolean iterateChildsForFace(IIterateChilds iterator) {
		final ElfList<ElfNode>.Iterator it = mChildList.iterator(true);
		while (it.hasNext()) {
			if (iterator.iterate(it.next())) {
				return true;
			}
		}
		return false;
	}

	public ElfNode[] getChilds() {
		final ElfNode[] childs = new ElfNode[getChildsSize()];
		final int[] index = new int[1];
		index[0] = 0;
		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				childs[index[0]] = node;
				index[0]++;
				return false;
			}
		});

		return childs;
	}

	public ElfNode[] getChildsForXML() {
		return getChilds();
	}

	public ElfNode[] getChildsForFace() {
		return getChilds();
	}

	public boolean iterateChildsReversed(IIterateChilds iterator) {
		final ElfList<ElfNode>.Iterator it = mChildList.iterator(false);
		while (it.hasPrev()) {
			if (iterator.iterate(it.prev())) {
				return true;
			}
		}
		return false;
	}

	public boolean touchIterate(IIterateChilds iterator) {
		final ElfList<ElfNode>.Iterator it = mChildList.iterator(false);
		while (it.hasPrev()) {
			final ElfNode node = it.prev();

			if (node.touchIterate(iterator)) {
				return true;
			}

			if (iterator.iterate(node)) {
				return true;
			}
		}

		return false;
	}

	public boolean iterateChildsDeep(IIterateChilds iterator) {
		final ElfList<ElfNode>.Iterator it = mChildList.iterator(true);
		while (it.hasNext()) {
			final ElfNode node = it.next();

			if (iterator.iterate(node)) {
				return true;
			}

			if (node.iterateChildsDeep(iterator)) {
				return true;
			}
		}
		return false;
	}

	public boolean iterateChildsDeepForFace(IIterateChilds iterator) {
		return iterateChildsDeep(iterator);
	}

	public boolean iterateChildsDeepWithSelf(IIterateChilds iterator) {
		if (iterator.iterate(this)) {
			return true;
		}

		final ElfList<ElfNode>.Iterator it = mChildList.iterator(true);
		while (it.hasNext()) {
			final ElfNode node = it.next();

			if (iterator.iterate(node)) {
				return true;
			}

			if (node.iterateChildsDeep(iterator)) {
				return true;
			}
		}
		return false;
	}

	public interface IIterateChilds {
		public boolean iterate(ElfNode node);
	}

	// static int mTotal
	public ElfPointf getNodeIndex(final ElfNode child) {
		final ElfPointf ret = new ElfPointf(1, -1);
		if (this == child) {
			ret.y = 0;
		}
		if (this.hasChild()) {
			this.iterateChildsDeep(new IIterateChilds() {
				public boolean iterate(ElfNode node) {
					if (node == child) {
						ret.y = ret.x;
					}
					ret.x++;
					return false;
				}
			});
		}
		return ret;
	}

	public boolean onTouch(ElfEvent event) {
		return false;
	}

	public ElfNode findByName(final String[] names) {
		ElfNode ret = this;
		for (int i = 0; i < names.length && ret != null; i++) {
			ret = ret.findByName(names[i]);
		}
		return ret;
	}

	public final ElfNode findChildBySimpleName(final String name) {
		final ElfNode[] ret = { null };
		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(final ElfNode node) {
				if (node.getName().equals(name)) {
					ret[0] = node;
					return true;
				}
				return false;
			}
		});
		return ret[0];
	}

	public final ElfNode findBySimpleName(final String[] names) {
		ElfNode searcher = this;
		for (String name : names) {
			searcher = searcher.findBySimpleName(name);
			if (searcher == null) {
				break;
			}
		}
		return searcher;
	}

	public final ElfNode findBySimpleName(final String name) {
		if (this.getName().equals(name)) {
			return this;
		}

		final ElfNode[] ret = { null };
		this.iterateChildsDeep(new IIterateChilds() {
			public boolean iterate(final ElfNode node) {
				if (node.getName().equals(name)) {
					ret[0] = node;
					return true;
				}
				return false;
			}
		});
		return ret[0];
	}

	public ElfNode findByName(final String name) {
		final ElfNode[] ret = new ElfNode[1];
		final String fullName = getFullName();
		if (fullName != null && fullName.equals(name)) {
			ret[0] = this;
		} else {
			iterateChildsDeep(new IIterateChilds() {
				public boolean iterate(ElfNode node) {
					final String childFullName = node.getFullName();
					if (childFullName != null && childFullName.equals(name)) {
						ret[0] = node;
						return true;
					}
					return false;
				}
			});
		}
		return ret[0];
	}

	public void runModifiers() {
		this.clearModifierDeep();
		this.iterateChildsDeep(mIIterateChilds);
		mIIterateChilds.iterate(this);
	}

	public final void refresh() {
		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				node.refresh();
				return false;
			}
		});
		this.myRefresh();
	}

	public void myRefresh() {
		myRefreshResource();
		this.onRecognizeRequiredNodes();
		this.reset();
	}

	protected void myRefreshResource() {
		final String[] ids = this.getSelfResids();
		for (String id : ids) {
			refreshTexture(id, mGLid);
		}
	}

	public final void refreshResource() {
		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				node.refreshResource();
				return false;
			}
		});
		this.myRefreshResource();
	}

	final IIterateChilds mIIterateChilds = new IIterateChilds() {
		public boolean iterate(ElfNode node) {
			final ActionData actions = (ActionData) node.getData(ActionDataConfig.ACTIONDATA_KEY);
			if (actions != null) {
				for (ActionData action : actions.mChildsList) {
					if (action.mChecked) {
						final org.eclipse.swt.graphics.Point error = new org.eclipse.swt.graphics.Point(0, 0);
						final INodeModifier modifier = action.createModifier(error);
						if (modifier != null) {
							node.setUseModifier(true);
							node.addModifier(modifier);
						}
					}
				}
			}
			return false;
		}
	};

	public CCAction runAction(CCAction action) {
		// assert action != null : "Argument must be non-null";
		if (action != null) {
			ActionManager actionManager = getMyActionManager();
			if (actionManager != null) {
				actionManager.addAction(action, this, !mIsInRunningNode);
			} else {
				System.err.println("no actionManager");
			}
		} else {
			System.err.println("null action");
		}

		return action;
	}

	public CCAction runElfAction(CCAction action) {
		stopActions();
		return runAction(action);
	}

	private final ActionManager getMyActionManager() {
		final ElfNode node = this.getTopNode();
		if (node != null && node instanceof ElfScreenNode) {
			return ((ElfScreenNode) node).getActionManager();
		}
		return null;
	}

	public void stopActions() {
		ActionManager actionManager = getMyActionManager();
		if (actionManager != null) {
			actionManager.removeAllActions(this);
		}
	}

	public void stopAction(CCAction action) {
		ActionManager actionManager = getMyActionManager();
		if (actionManager != null) {
			actionManager.removeAction(action);
		}
	}

	public final String getCopyName(final ElfNode newFather) {
		final String name = getName();

		int len = name.length();
		int lastIndex = len;
		for (int i = len - 1; i >= 0; i--) {
			if (!Character.isDigit(name.charAt(i))) {
				lastIndex = i;
				break;
			}
		}

		String subName;
		int num;
		boolean hasNum;
		if (lastIndex == len - 1) {
			subName = name;
			num = 0;
			hasNum = false;
		} else if (lastIndex == len) {
			hasNum = true;
			subName = "";
			String numStr = name;
			num = Integer.valueOf(numStr);
			hasNum = true;
		} else {
			subName = name.substring(0, lastIndex + 1);
			String numStr = name.substring(lastIndex + 1);
			num = Integer.valueOf(numStr);
			hasNum = true;
		}

		if (newFather != null) {
			int size = newFather.mChildList.size();
			final String[] names = new String[size];
			final int[] index = new int[1];
			index[0] = 0;

			newFather.iterateChilds(new IIterateChilds() {
				public boolean iterate(ElfNode node) {
					names[index[0]] = node.getName();
					index[0]++;
					return false;
				}
			});

			String newName = subName;
			if (hasNum) {
				newName = subName + (num);
				num++;
			}

			while (true) {
				boolean find = false;
				for (int i = 0; i < names.length && !find; i++) {
					if (newName.equals(names[i])) {
						find = true;
					}
				}
				if (!find) {
					return newName;
				}
				newName = subName + (num);
				num++;
			}
		} else {
			return subName;
		}
	}

	public void commentName() {
		if (mName != null) {
			if (mName.startsWith("@")) {
				// mName = "#" + mName.substring(1);
			} else if (!mName.startsWith("#")) {
				mName = "#" + mName;
			}
		}
	}

	public void commentNameDeep() {
		this.commentName();
		this.iterateChildsDeep(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				node.commentName();
				return false;
			}
		});
	}

	public void uncommentName() {
		if (mName != null) {
			if (mName.startsWith("#")) {
				mName = mName.substring(1, mName.length());
			}
		}
	}

	public void uncommentNameDeep() {
		this.uncommentName();
		this.iterateChildsDeep(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				node.uncommentName();
				return false;
			}
		});
	}

	public ArrayList<ElfNode> searchSameNameNodes() {
		final ArrayList<ElfNode> ret = new ArrayList<ElfNode>();
		if (mName == null) {
			ret.add(this);
		} else {
			final ElfNode topNode = getTopNode();
			if (topNode != null) {
				topNode.iterateChildsDeep(new IIterateChilds() {
					public boolean iterate(final ElfNode node) {
						if (ElfNode.this.isSameNameAndType(node)) {
							ret.add(node);
						}
						return false;
					}
				});
			} else {
				ret.add(this);
			}
		}

		return ret;
	}

	public boolean isSameNameAndType(ElfNode node) {
		if (node != null) {
			if ((ElfNode.this.getClass() == node.getClass())) {
				if ((mName != null && mName.equals(node.getName())) || mName == node.getName()) {
					return true;
				}
			}
		}
		return false;
	}

	public ArrayList<ElfNode> searchSameNameAndTypeParentSonNodes() {
		final ArrayList<ElfNode> ret = new ArrayList<ElfNode>();
		final ArrayList<ElfNode> sames = this.searchSameNameAndTypeNodes();

		final ElfNode parent = this.getParent();

		for (final ElfNode node : sames) {
			if (parent != null) {
				if (parent.isSameNameAndType(node.getParent())) {
					ret.add(node);
				}
			} else if (node.getParent() == null) {
				ret.add(node);
			}
		}
		return ret;
	}

	public ArrayList<ElfNode> searchSameNameAndTypeNodes() {
		final ArrayList<ElfNode> ret = new ArrayList<ElfNode>();
		if (mName == null) {
			ret.add(this);
		} else {
			final ElfNode topNode = getTopNode();
			if (topNode != null) {
				topNode.iterateChildsDeep(new IIterateChilds() {
					public boolean iterate(ElfNode node) {
						if (mName.equals(node.getName()) && (ElfNode.this.getClass() == node.getClass())) {
							ret.add(node);
						}
						return false;
					}
				});
			} else {
				ret.add(this);
			}
		}

		return ret;
	}

	public void bindToLua() {
	}

	public static void refreshTexture(final String resid, final int glId) {
		GLManage.unloadTextureRegions(resid, glId);
		GLManage.loadTextureRegion(resid, glId);
	}

	public final boolean isRealVisible(ElfEvent event) {
		ElfNode node = this;
		if (!node.getVisible()) {
			return false;
		}

		node = node.mFatherNode;
		while (node != null) {
			if (!node.getVisible()) {
				return false;
			}

			if (event.action != MotionEvent.LEFT_UP) {
				// clipable
				if (node instanceof IClipable) {
					boolean ret = ((IClipable) node).isInClipSize(event.x, event.y);
					if (ret == false) {
						return false;
					}
				}
			}

			node = node.mFatherNode;
		}

		return true;
	}

	protected String mResid = "null";

	public void setResid(final String resid) {
		mResid = PlatformHelper.toCurrentPath(resid);
		// if(resid != null) {
		// int index = resid.lastIndexOf("_");
		// if(index>=0) {
		// mResid = resid.substring(index+1);
		// } else {
		// mResid = resid;
		// }
		// } else {
		// mResid = resid;
		// }
	}

	public String getResid() {
		return mResid;
	}

	// reflect
	protected static final int REF_Resid = DEFAULT_SHIFT | DROP_RESID_SHIFT | UNDO_SHIFT;

	private final ElfPointf mPosition = new ElfPointf();

	public void setPosition(final ElfPointf pos) {
		if (onNodePropertyChangeBefore(NodePropertyType.PositionType)) {
			mPosition.x = pos.x;
			mPosition.y = pos.y;

			doOnNodePropertyChange(NodePropertyType.PositionType);
		}
	}

	public final void setPosition(float x, float y) {
		this.setPosition(new ElfPointf(x, y));
	}

	public final void setPositionX(float x) {
		setPosition(new ElfPointf(x, getPosition().y));
	}

	public final void setPositionY(float y) {
		setPosition(new ElfPointf(getPosition().x, y));
	}

	public ElfPointf getPosition() {
		return new ElfPointf(mPosition);
	}

	public final void translate(float dx, float dy) {
		ElfPointf p = getPosition();
		p.translate(dx, dy);
		setPosition(p);
	}

	// reflect
	protected final static int REF_Position = DEFAULT_SHIFT | UNDO_SHIFT;

	public void setPositionInScreen(final ElfPointf pos) {
		final ElfPointf ret = new ElfPointf();
		this.screenXYToSelf(pos.x, pos.y, ret);
		this.setPosition(ret);
	}

	public ElfPointf getPositionInScreen() {
		final ElfPointf ret = new ElfPointf();
		this.selfXYtoScreen(ret);
		return new ElfPointf(ret.x, ret.y);
	}

	// reflect
	protected static final int REF_PositionInScreen = FACE_ALL_SHIFT | COPY_SHIFT | PASTE_SHIFT;

	private final ElfPointf mAnchorPosition = new ElfPointf(0.5f, 0.5f);

	public void setAnchorPosition(final ElfPointf pos) {
		if (onNodePropertyChangeBefore(NodePropertyType.PositionType)) {

			mAnchorPosition.x = pos.x;
			mAnchorPosition.y = pos.y;

			doOnNodePropertyChange(NodePropertyType.PositionType);
		}
	}

	public void setAnchorPosition(float x, float y) {
		mAnchorPosition.x = x;
		mAnchorPosition.y = y;
	}

	public void setAnchorPositionX(float x) {
		setAnchorPosition(new ElfPointf(x, getPosition().y));
	}

	public void setAnchorPositionY(float y) {
		setAnchorPosition(new ElfPointf(getAnchorPosition().x, y));
	}

	public ElfPointf getAnchorPosition() {
		return new ElfPointf(mAnchorPosition);
	}

	public void transAnchorPosition(float dx, float dy) {
		ElfPointf p = getAnchorPosition();
		p.translate(dx, dy);
		setAnchorPosition(p);
	}

	// reflect
	protected static final int REF_AnchorPosition = DEFAULT_SHIFT | UNDO_SHIFT;

	private final ElfPointf mSize = new ElfPointf(20, 20);

	public void setSize(final ElfPointf size) {
		mSize.x = size.x;
		mSize.y = size.y;
	}

	public ElfPointf getSize() {
		if (!getUseSettedSize()) {
			final ElfPointf size = GLManage.getSize(getSizeResid());
			if (size == null) {
				return new ElfPointf(mDefaultWidth, mDefaultHeight);
			} else {
				return new ElfPointf(size);
			}
		}
		return new ElfPointf(mSize);
	}

	protected String getSizeResid() {
		return getResid();
	}

	// reflect
	protected static final int REF_Size = DEFAULT_SHIFT | UNDO_SHIFT;

	public void setSize(final float width, final float height) {
		mSize.x = width;
		mSize.y = height;
	}

	private boolean mUseSettedSize = false;

	public void setUseSettedSize(boolean use) {
		if (ResJudge.isRes(getResid())) {
			mUseSettedSize = false;
		} else {
			mUseSettedSize = use;
		}
	}

	public boolean getUseSettedSize() {
		return mUseSettedSize && !ResJudge.isRes(getResid());
	}

	// reflect
	protected static final int REF_UseSettedSize = FACE_ALL_SHIFT | XML_ALL_SHIFT | UNDO_SHIFT;

	protected boolean mVisible = true;

	public void toggleVisible() {
		setVisible(!getVisible());
	}

	public void setVisible(final boolean visible) {
		if (onNodePropertyChangeBefore(NodePropertyType.VisibleType)) {
			mVisible = visible;
			doOnNodePropertyChange(NodePropertyType.VisibleType);
		}
	}

	public boolean getVisible() {
		return mVisible;
	}

	// reflect
	protected static final int REF_Visible = FACE_ALL_SHIFT | XML_ALL_SHIFT | UNDO_SHIFT;

	private final ElfPointf mScale = new ElfPointf(1, 1);

	public void setScale(final ElfPointf scale) {
		if (onNodePropertyChangeBefore(NodePropertyType.ScaleType)) {
			mScale.x = scale.x;
			mScale.y = scale.y;
			doOnNodePropertyChange(NodePropertyType.ScaleType);
		}
	}

	public void setScale(float x, float y) {
		this.setScale(new ElfPointf(x, y));
	}

	public ElfPointf getScale() {
		return new ElfPointf(mScale);
	}

	// reflect
	protected static final int REF_Scale = DEFAULT_SHIFT | UNDO_SHIFT;

	private INodePropertyChange mINodePropertyChange;

	public void setNodePropertyChange(INodePropertyChange change) {
		mINodePropertyChange = change;
	}

	public INodePropertyChange getNodePropertyChange() {
		return mINodePropertyChange;
	}

	void doOnNodePropertyChange(NodePropertyType type) {
		if (mINodePropertyChange != null) {
			mINodePropertyChange.onNodePropertyChange(this, type);
		}
	}

	boolean onNodePropertyChangeBefore(NodePropertyType type) {
		if (mINodePropertyChange != null) {
			return mINodePropertyChange.onNodePropertyChangeBefore(this, type);
		}
		return true;
	}

	private final ElfColor mColor = new ElfColor();

	public void setColor(final ElfColor color) {
		if (onNodePropertyChangeBefore(NodePropertyType.ColorType)) {
			mColor.red = color.red;
			mColor.green = color.green;
			mColor.blue = color.blue;
			mColor.alpha = color.alpha;

			doOnNodePropertyChange(NodePropertyType.ColorType);
		}
	}
	
	public void setColorNoAplha(final ElfColor color) {
		if (onNodePropertyChangeBefore(NodePropertyType.ColorType)) {
			mColor.red = color.red;
			mColor.green = color.green;
			mColor.blue = color.blue;

			doOnNodePropertyChange(NodePropertyType.ColorType);
		}
	}

	public ElfColor getColor() {
		return new ElfColor(mColor);
	}

	public void setAlpha(float alpha) {
		if (onNodePropertyChangeBefore(NodePropertyType.ColorType)) {
			mColor.alpha = alpha;
			doOnNodePropertyChange(NodePropertyType.ColorType);
		}
	}

	public float getAlpha() {
		return mColor.alpha;
	}

	// reflect
	protected static final int REF_Color = DEFAULT_SHIFT | UNDO_SHIFT;

	private float mRotate = 0;

	public void setRotate(final float r) {
		if (onNodePropertyChangeBefore(NodePropertyType.RotateType)) {
			mRotate = r;
			doOnNodePropertyChange(NodePropertyType.RotateType);
		}
	}

	public float getRotate() {
		return mRotate;
	}

	public void transRotate(float dRotate) {
		setRotate(mRotate + dRotate);
	}

	// reflect
	protected static final int REF_Rotate = FACE_ALL_SHIFT | XML_ALL_SHIFT | UNDO_SHIFT;

	private BlendMode mBlendMode = BlendMode.BLEND;

	public void setBlendMode(final BlendMode blendMode) {
		mBlendMode = blendMode;
	}

	public BlendMode getBlendMode() {
		return mBlendMode;
	}

	// reflect
	protected static final int REF_BlendMode = FACE_ALL_SHIFT | XML_ALL_SHIFT;

	public String getBlendModeHelper() {
		String ret = "";
		ret += "All Blend Modes \n";
		for (BlendMode mode : BlendMode.values()) {

			ret += "\t" + mode.toString() + ":\t" + mode.sourceBlendFunction + "," + mode.destinationBlendFunction + "\n";
			if (mode == BlendMode.BLEND) {
				ret += "\n";
			}
		}

		return ret;
	}

	// reflect
	protected static final int REF_BlendModeHelper = FACE_GET_SHIFT;

	public int getBlendSrc() {
		return mBlendMode.sourceBlendFunction;
	}

	public void setBlendSrc(int src) {
	}

	protected static final int REF_BlendSrc = XML_SET_SHIFT;

	public int getBlendDst() {
		return mBlendMode.destinationBlendFunction;
	}

	public void setBlendDst(int dst) {
	}

	protected static final int REF_BlendDst = XML_SET_SHIFT;

	private boolean mTouchable = false;

	public void setTouchEnable(boolean touchable) {
		mTouchable = touchable;
	}

	public boolean getTouchEnable() {
		return mTouchable;
	}

	// reflect
	protected static final int REF_TouchEnable = FACE_ALL_SHIFT | XML_ALL_SHIFT | UNDO_SHIFT;

	private boolean mTouchShielded = false;

	public final void setTouchShielded(boolean touchable) {
		mTouchShielded = touchable;
	}

	public final boolean getTouchShielded() {
		return mTouchShielded;
	}

	// reflect
	protected static final int REF_TouchShielded = FACE_ALL_SHIFT | XML_ALL_SHIFT | UNDO_SHIFT;

	private boolean mPaused = false;

	public void setPaused(final boolean pause) {
		mPaused = pause;
	}

	public boolean getPaused() {
		return mPaused;
	}

	// reflect
	protected static final int REF_Paused = DEFAULT_SHIFT | UNDO_SHIFT;

	private boolean mIsCouldMove = true;

	public void setCouldMove(boolean could) {
		mIsCouldMove = could;
	}

	public boolean getCouldMove() {
		return mIsCouldMove;
	}

	// reflect
	protected static final int REF_CouldMove = FACE_ALL_SHIFT | XML_ALL_SHIFT | UNDO_SHIFT;

	private String mName = null;

	public void setName(final String name) {
		mName = name;
		if (mName != null) {
			mName = mName.replace(" ", "");
		}
	}

	public String getName() {
		return mName == null ? "" : mName;
	}

	// reflect
	protected static final int REF_Name = XML_ALL_SHIFT | UNDO_SHIFT;

	private String mFullName = null;

	public final void setFullName(final String name) {
		mFullName = name;
	}

	private static String getNameDeeply(final ElfNode node, final String subname) {
		if (node instanceof ElfScreenNode) {
			return subname;
		} else if (node == null) {
			return subname;
		} else {
			final ElfNode grandparent = node.getParent();
			if (grandparent != null && grandparent instanceof ElfScreenNode) {
				return subname;
			}

			if (subname.startsWith("@")) {
				return subname;
			}

			final String nodename = node.getName();
			if (nodename.startsWith("@")) {
				final ProjectSetting ps = PowerMan.getSingleton(ProjectSetting.class);
				if (ps.Full_Name_Reduced) {
					return subname;
				} else {
					return nodename.replace("@", "##") + "_" + subname;
				}
			}

			if (nodename.equals("#")) {
				// do nothing with subname
				return getNameDeeply(grandparent, subname);
			}

			if (nodename.startsWith("#")) {
				return getNameDeeply(grandparent, nodename.replace("#", "") + "_" + subname);
			}

			final String finalname = getNameDeeply(grandparent, node.getName() + "_" + subname);
			return finalname;
		}
	}

	public final String getFullName() {
		// final ProjectSetting ps =
		// PowerMan.getSingleton(ProjectSetting.class);
		//
		if (getGLId() != 0 && mFullName != null) {
			return mFullName;
		}
		//
		// final String myName = getName();
		// if (myName.startsWith("@")) {
		// return myName;
		// }
		//
		// final StringBuffer sb = new StringBuffer();
		//
		// String preName = "";
		// ElfNode node = this.mFatherNode;
		//
		// final ElfNode bindNode =
		// PowerMan.getSingleton(DataModel.class).getRootScreen();
		//
		// while (node != null && node != bindNode) {
		// final String name = node.getName();
		//
		// if(ps.Full_Name_Reduced && name.startsWith("@")) {
		// break;
		// }
		//
		// if (name != null && name.equals("#")) {
		// // final String tmp = name.replace("#", "");
		// // preName = tmp + "_" + preName;
		// // do nothing
		// } else {
		// final String tmp = name.replace("#", "");
		// preName = tmp + "_" + preName;
		// }
		//
		// node = node.getParent();
		// }
		//
		// preName = preName.replace("elf_screen_", "");
		//
		// if (preName.contains("@")) {
		// int index = preName.lastIndexOf("@");
		// preName = preName.substring(index);
		// preName = preName.replace("@", "##");
		// }

		return SpellHelper.getUpEname(getNameDeeply(this.getParent(), this.getName()));
	}

	// reflect
	protected static final int REF_FullName = FACE_GET_SHIFT | COPY_SHIFT | XML_ALL_SHIFT;

	private float mZ = 0;

	public void setZ(final float z) {
		mZ = z;
	}

	public float getZ() {
		return mZ;
	}

	// reflect
	protected static final int REF_Z = FACE_ALL_SHIFT + XML_ALL_SHIFT;

	private Object mTag = null;

	public void setTag(final Object tag) {
		mTag = tag;
	}

	public Object getTag() {
		return mTag;
	}

	private int mID = -1;

	public final void setId(final int id) {
		mID = id;
	}

	public final int getId() {
		return mID;
	}

	// reflect
	// protected static final int REF_Id = FACE_ALL_SHIFT + XML_ALL_SHIFT;

	private boolean mIsInRunningNode = false;

	public boolean getIsInRunningNode() {
		return mIsInRunningNode;
	}

	public void setIsInRunningNode(boolean isInRunningNode) {
		mIsInRunningNode = isInRunningNode;
	}

	private float mElapsedRate = 1;

	public void setElaspedRate(final float elapsed) {
		mElapsedRate = elapsed;
	}

	public float getElapsedRate() {
		return mElapsedRate;
	}

	private float mChildTimeRate = 1.0f;

	public void setChilcTimeRate(final float childTimeRate) {
		mChildTimeRate = childTimeRate;
	}

	public float getChildTimeRate() {
		return mChildTimeRate;
	}

	private boolean mIsUseModifiers = false;

	public final void setUseModifier(final boolean isUse) {
		mIsUseModifiers = isUse;
	}

	public final boolean getUseModifier() {
		return mIsUseModifiers;
	}

	// protected static final int REF_UseModifier = FACE_ALL_SHIFT |
	// XML_ALL_SHIFT | UNDO_SHIFT;

	protected boolean mIsUseControl = false;

	public void setUseControl(boolean useControl) {
		mIsUseControl = useControl;
	}

	public boolean getUseControl() {
		return mIsUseControl;
	}

	// reflect
	// protected static final int REF_UseControl = FACE_ALL_SHIFT |
	// XML_ALL_SHIFT | UNDO_SHIFT;

	private final Stack<ElfNode> saveStack = new Stack<ElfNode>();

	// save tmp
	public void pushActionTmp() {
		final ElfNode tmp = new ElfNode(null, 0);
		// copySelf(mFatherNode);
		//
		tmp.setPosition(this.getPosition());
		tmp.setColor(this.getColor());
		tmp.setVisible(this.getVisible());
		tmp.setScale(this.getScale());
		tmp.setRotate(this.getRotate());

		saveStack.clear();
		saveStack.push(tmp);
	}

	public void popActionTmp() {
		if (!saveStack.isEmpty()) {
			final ElfNode tmp = saveStack.pop();
			// copyFrom(this, tmp);
			this.setPosition(tmp.getPosition());
			this.setColor(tmp.getColor());
			this.setVisible(tmp.getVisible());
			this.setScale(tmp.getScale());
			this.setRotate(tmp.getRotate());
		}
	}

	public void writeToPNG(final String path, int xNums, int yNums, final float scale) {
		final int width = Math.round(this.getSize().x);
		final int height = Math.round(this.getSize().y);

		GL11.glEnable(EXTFramebufferObject.GL_RENDERBUFFER_EXT);
		GL11.glEnable(EXTFramebufferObject.GL_FRAMEBUFFER_EXT);

		final int newFBO = EXTFramebufferObject.glGenFramebuffersEXT();
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, newFBO);

		final int newRBO = EXTFramebufferObject.glGenRenderbuffersEXT();
		EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, newRBO);
		EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, GL11.GL_RGBA, width, height);
		EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, newRBO);

		// assert(EXTFramebufferObject.glCheckFramebufferStatusEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT)==EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT);
		// begin
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, newFBO);

		GL11.glViewport(0, 0, width, height);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, 0, height, -1000, 1000);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glClearColor(0, 0, 0, 0);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		prepareBlendAndColor();
		GLHelper.glPushMatrix();
		// move to center
		GL11.glTranslatef(width / 2, height / 2, 0);
		GL11.glScalef(1, -1, 1);
		drawSelf();
		drawChilds();
		drawAfter();

		save(width, height, path, xNums, yNums, scale);

		final ProjectSetting mScreenConfig = PowerMan.getSingleton(ProjectSetting.class);
		final int PhysicWidth = mScreenConfig.getPhysicWidth();
		final int PhysicHeight = mScreenConfig.getPhysicHeight();

		GL11.glViewport(0, 0, PhysicWidth, PhysicHeight);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, PhysicWidth, 0, PhysicHeight, -1000, 1000);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
		EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, 0);

		EXTFramebufferObject.glDeleteFramebuffersEXT(newFBO);
		EXTFramebufferObject.glDeleteRenderbuffersEXT(newRBO);
	}

	final void save(final int width, final int height, final String path, int xNums, int yNums, final float scale) {
		try {
			BufferHelper.sIntBuffer.position(0);
			GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, BufferHelper.sIntBuffer);
			BufferHelper.sIntBuffer.position(0);

			xNums = Math.max(xNums, 1);
			yNums = Math.max(yNums, 1);

			final ImageData[] datas = new ImageData[xNums * yNums];
			final int stepX = width / xNums;
			final int stepY = height / yNums;
			for (int i = 0; i < xNums; i++) {
				for (int j = 0; j < yNums; j++) {
					final int originX = stepX * i;
					final int originY = stepY * j;
					final ImageData data = ImageHelper.create(stepX, stepY);
					datas[j * xNums + i] = data;
					for (int x = 0; x < stepX; x++) {
						for (int y = 0; y < stepY; y++) {
							final int index = ((originY + y) * width + originX + x);
							final int piexl = BufferHelper.sIntBuffer.get(index);
							final int red = (piexl & 0xFF);
							final int green = ((piexl >> 8) & 0xFF);
							final int blue = ((piexl >> 16) & 0xFF);
							final int alpha = ((piexl >> 24) & 0xFF);
							final RGB rgb = new RGB(red, green, blue);
							data.setAlpha(x, y, alpha);
							final int pixelValue = data.palette.getPixel(rgb);
							data.setPixel(x, y, pixelValue);
						}
					}
				}
			}

			if (xNums == 1 && yNums == 1) {
				ImageHelper.save(datas[0].scaledTo(Math.round(width * scale), Math.round(height * scale)), path);
			} else {
				final String head = path.replace(".png", "");
				for (int i = 0; i < xNums; i++) {
					for (int j = 0; j < yNums; j++) {
						ImageHelper.save(datas[j * xNums + i].scaledTo(Math.round(stepX * scale), Math.round(stepY * scale)), head + "-" + FileHelper.num2Str(i, FileHelper.num2Bits(xNums)) + "-" + FileHelper.num2Str(j, FileHelper.num2Bits(yNums)));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String[] getSelfResids() {
		return new String[] { mResid };
	}

	public void setSelfResids(final String[] resids) {
		if (resids != null) {
			if (resids.length > 0) {
				this.setResid(resids[0]);
			}
		}
	}

	public final Set<String> getResidsSet() {
		final HashSet<String> ret = new HashSet<String>();
		{
			final String id = this.getResid();
			if (id != null && id.length() > 0 && !id.equals("null") && !id.equals("ul")) {
				ret.add(id);
			}
		}

		final String[] resids = getSelfResids();
		for (String id : resids) {
			if (id != null && id.length() > 0 && !id.equals("null") && !id.equals("ul")) {
				ret.add(id);
			}
		}
		return ret;
	}

	public final Set<String> getResidsSetDeep() {
		final HashSet<String> ret = new HashSet<String>();
		this.iterateChildsDeepWithSelf(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				ret.addAll(node.getResidsSet());
				return false;
			}
		});
		return ret;
	}

	// public final Set<String> getResidsSet() {
	// final HashSet<String> ret = new HashSet<String>();
	// final String [] resids = getSelfResids();
	// for(String id : resids) {
	// final String real = id;
	// ret.add( real );
	// }
	// return ret;
	// }

	public final Set<String> getLegalResids(boolean transPath) {
		final HashSet<String> ret = new HashSet<String>();
		final String[] resids = getSelfResids();
		for (String id : resids) {
			if (ResJudge.isRes(id)) {
				final String real = transPath ? TransferRes.exportCompressPath(id, true) : id;
				ret.add(real);
			}
		}
		return ret;
	}

	public final Set<String> getNotExistResids() {
		final HashSet<String> ret = new HashSet<String>();
		final String[] resids = getSelfResids();
		for (String id : resids) {
			final HashMap<String, String> map = ResManager.getName2PathMap();

			final String full = map.get(id);
			if (full == null || ResJudge.isLegalResNotExisted(full)) {
				ret.add(id);
			}
		}
		return ret;
	}

	public void setAutoScaleImages(final ElfPointf scale) {
		System.err.println("setAutoScaleImages " + scale.x + ", " + scale.y);
		final String[] resids = this.getSelfResids();

		System.err.println("length " + resids.length);
		if (resids != null) {
			for (String resid : resids) {
				System.err.println("--" + resid);
				try {
					final ImageData data = ImageHelper.read(resid);
					// if(data != null) {
					final ImageData newData = data.scaledTo(Math.round(scale.x * data.width), Math.round(scale.y * data.height));
					ImageHelper.save(newData, resid);
					// }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			this.runWithDelay(new Runnable() {
				public void run() {
					for (String id : resids) {
						refreshTexture(id, mGLid);
					}
				}
			}, 1);
		}
	}

	// protected final static int REF_AutoScaleImages = FACE_SET_SHIFT;

	private int mCurrentNextChildIndex = 0;

	public void setGoToNextChild() {
		final ElfNode[] nodes = this.getChilds();
		if (nodes != null && nodes.length > 0) {
			if (mCurrentNextChildIndex >= nodes.length) {
				mCurrentNextChildIndex = 0;
			}

			for (int i = 0; i < nodes.length; i++) {
				nodes[i].setVisible(false);
			}

			nodes[mCurrentNextChildIndex].setVisible(true);
			// PowerMan.getSingleton(NodeViewWorkSpaceTab.class).setSelectNodes(nodes[mCurrentNextChildIndex]);

			mCurrentNextChildIndex++;
		}
	}

	// protected final static int REF_GoToNextChild = FACE_SET_SHIFT;

	// private final Stack<ElfNode> mUndoStack = new Stack<ElfNode>();
	// private final Stack<ElfNode> mRedoStack = new Stack<ElfNode>();
	//
	// public void unDo() {
	// if(!mUndoStack.isEmpty()) {
	// final ElfNode node = mUndoStack.pop();
	// // final Runnable run = sUndoList.getLast();
	// // run.run();
	// // sUndoList.removeLast();
	// // if(sCurrentRun != null) {
	// // sRedoList.addLast(sCurrentRun);
	// // }
	// // sCurrentRun = run;
	// }
	// }
	//
	// public void reDo() {
	//
	// }

	private static class IdentityMaker {
		private int mIdentityGenerator = 0;

		public int createIdentityId() {
			return ++mIdentityGenerator;
		}
	}
	
	public void autoBindFlash() {
		this.iterateChildsDeep(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				if(node instanceof IFlashMain) {
					FlashManager fm = PowerMan.getSingleton(FlashManager.class);
					fm.bindFlashMain((IFlashMain)node);
					return true;
				}
				return false;
			}
		});
	}
	
	public ElfPointf getScaleSize() {
		return new ElfPointf(getSize().x * mScale.x, getSize().y * mScale.y);
	}

	// reflect
	 protected static final int REF_ScaleSize = FACE_GET_SHIFT | COPY_SHIFT;

}
