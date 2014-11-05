package com.ielfgame.stupidGame.face.action;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.FiniteTimeAction;
import org.cocos2d.actions.interval.Sequence;
import org.cocos2d.actions.interval.Spawn;

import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.data.ElfVertex3;
import com.ielfgame.stupidGame.data.IDataDisplay;
import com.ielfgame.stupidGame.data.Stringified;

import elfEngine.basic.counter.InterHelper.InterType;

public class CCActionHelper {
	private final static LinkedList<Class<?>> sActionClass = new LinkedList<Class<?>>();
	private final static void addCCActionClass(Class<?> _class) {
		if(_class != null) {
//			System.err.println("addCCActionClass "+_class.getSimpleName());
			sActionClass.add(_class);
		} else {
//			System.err.println("addCCActionClass null");
		}
	}
	static {
		try {
			addCCActionClass(Class.forName("org.cocos2d.actions.base.CCAction"));
			addCCActionClass(Class.forName("org.cocos2d.actions.base.FiniteTimeAction"));
			addCCActionClass(Class.forName("org.cocos2d.actions.base.RepeatForever"));
			addCCActionClass(Class.forName("org.cocos2d.actions.base.Speed"));
			addCCActionClass(Class.forName("org.cocos2d.actions.camera.CameraAction"));
			addCCActionClass(Class.forName("org.cocos2d.actions.camera.OrbitCamera"));
			addCCActionClass(Class.forName("org.cocos2d.actions.ease.EaseAction"));
			addCCActionClass(Class.forName("org.cocos2d.actions.ease.EaseBackIn"));
			addCCActionClass(Class.forName("org.cocos2d.actions.ease.EaseBackInOut"));
			addCCActionClass(Class.forName("org.cocos2d.actions.ease.EaseBackOut"));
			addCCActionClass(Class.forName("org.cocos2d.actions.ease.EaseBounce"));
			addCCActionClass(Class.forName("org.cocos2d.actions.ease.EaseBounceIn"));
			addCCActionClass(Class.forName("org.cocos2d.actions.ease.EaseBounceInOut"));
			addCCActionClass(Class.forName("org.cocos2d.actions.ease.EaseBounceOut"));
			addCCActionClass(Class.forName("org.cocos2d.actions.ease.EaseElastic"));
			addCCActionClass(Class.forName("org.cocos2d.actions.ease.EaseElasticIn"));
			addCCActionClass(Class.forName("org.cocos2d.actions.ease.EaseElasticInOut"));
			addCCActionClass(Class.forName("org.cocos2d.actions.ease.EaseElasticOut"));
			addCCActionClass(Class.forName("org.cocos2d.actions.ease.EaseExponentialIn"));
			addCCActionClass(Class.forName("org.cocos2d.actions.ease.EaseExponentialInOut"));
			addCCActionClass(Class.forName("org.cocos2d.actions.ease.EaseExponentialOut"));
			addCCActionClass(Class.forName("org.cocos2d.actions.ease.EaseIn"));
			addCCActionClass(Class.forName("org.cocos2d.actions.ease.EaseInOut"));
			addCCActionClass(Class.forName("org.cocos2d.actions.ease.EaseOut"));
			addCCActionClass(Class.forName("org.cocos2d.actions.ease.EaseRateAction"));
			addCCActionClass(Class.forName("org.cocos2d.actions.ease.EaseSineIn"));
			addCCActionClass(Class.forName("org.cocos2d.actions.ease.EaseSineInOut"));
			addCCActionClass(Class.forName("org.cocos2d.actions.ease.EaseSineOut"));
			addCCActionClass(Class.forName("org.cocos2d.actions.instant.CallFunc"));
			addCCActionClass(Class.forName("org.cocos2d.actions.instant.CallFuncN"));
			addCCActionClass(Class.forName("org.cocos2d.actions.instant.CallFuncND"));
			addCCActionClass(Class.forName("org.cocos2d.actions.instant.Hide"));
			addCCActionClass(Class.forName("org.cocos2d.actions.instant.InstantAction"));
			addCCActionClass(Class.forName("org.cocos2d.actions.instant.Place"));
			addCCActionClass(Class.forName("org.cocos2d.actions.instant.Show"));
			addCCActionClass(Class.forName("org.cocos2d.actions.instant.ToggleVisibility"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interpolator.ElfInterAction"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interval.BezierBy"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interval.Blink"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interval.DelayTime"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interval.FadeIn"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interval.FadeOut"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interval.FadeTo"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interval.IntervalAction"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interval.JumpBy"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interval.JumpTo"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interval.MoveBy"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interval.MoveTo"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interval.Repeat"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interval.ReverseTime"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interval.RotateBy"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interval.RotateTo"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interval.ScaleBy"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interval.ScaleTo"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interval.Sequence"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interval.Spawn"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interval.TintBy"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interval.TintTo"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interval.Action3DBy"));
			addCCActionClass(Class.forName("org.cocos2d.actions.interval.Action3DTo"));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public interface INewAction extends IDataDisplay {
		public CCAction newAction(final CCAction[] actions);
	}

	public static abstract class AbstractNewAction implements INewAction, IDataDisplay, Cloneable{
		public String name;
		public Class<? extends CCAction> mActoinClass;
		
		public AbstractNewAction copy() { 
			try {
				return (AbstractNewAction) this.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			} 
			return null;
		} 

		@SuppressWarnings("unchecked")
		public AbstractNewAction() { 
			this.name = this.getClass().getSimpleName().substring(1); 
			final Class<?>[] _classRet = new Class<?>[1]; 
			
			for(Class<?> _class : sActionClass) {
				if (_class.getSimpleName().equals(name)) { 
					_classRet[0] = _class; 
				} 
			}
//			ReflectFactory.initClasses("org.cocos2d.actions", true, new IDealClass() {
//				public void dealClass(Class<?> _class) { 
//					if (_class.getSimpleName().equals(name)) { 
//						_classRet[0] = _class; 
//					} 
//				} 
//			}); 
			
			mActoinClass = null;
			try {
				mActoinClass = (Class<? extends CCAction>) _classRet[0];
			} catch (Exception e) {
				e.printStackTrace();
			}

			assert (mActoinClass != null);
		}

		public CCAction newAction(final CCAction[] actions) {
			
			try {
				final Field[] fs = this.getClass().getFields();

				final LinkedList<Object> args = new LinkedList<Object>();
				for (int i = 0; i < fs.length; i++) {
					fs[i].setAccessible(true);
					final Object obj = fs[i].get(this);
					if (!(obj instanceof Class<?>)) {
						if (!fs[i].getName().contains("ame")) {
							final Object o = fs[i].get(this);
							args.add(o);
						}
					}
				}

				final Object[] _args = new Object[args.size()];
				args.toArray(_args);

				final Class<?>[] _argTypes = new Class<?>[_args.length];
				for (int i = 0; i < _args.length; i++) {
					_argTypes[i] = _args[i].getClass();
				}

//				int count = 0;
				@SuppressWarnings("unchecked")
				final Constructor<? extends CCAction> constructor = (Constructor<? extends CCAction>) mActoinClass.getDeclaredConstructors()[0];
				final Class<?>[] parTypes = constructor.getParameterTypes();
				if (parTypes.length >0 && CCAction.class.isAssignableFrom(parTypes[0])) {
					final Object[] _args2 = new Object[_args.length + 1];
					for (int i = 0; i < _args.length; i++) { 
						_args2[i + 1] = _args[i];
					}
					_args2[0] = actions[0];
					constructor.setAccessible(true);
					return constructor.newInstance(_args2);
				} else {
					constructor.setAccessible(true);
					if(_args.length > 0) {
						return constructor.newInstance(_args);
					} else {
						return constructor.newInstance();
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
//				System.exit(1);
			}
			return null; 
		}

		public String toTitle() {
			return mActoinClass.getSimpleName();
		} 
		
		public String[] toLabels() {
			final LinkedList<String> fieldList = new LinkedList<String>();
			try {
				final Field[] fs = this.getClass().getFields();
				for (int i = 0; i < fs.length; i++) {
					fs[i].setAccessible(true);
					final Object obj = fs[i].get(this);
					if (!(obj instanceof Class<?>)) {
						fieldList.add(fs[i].getName());
					}
				}
				final String[] ret = new String[fieldList.size()];
				fieldList.toArray(ret);
				return ret;

			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
			return null;
		}

		public String[] toValues() {
			final LinkedList<String> fieldList = new LinkedList<String>();
			try {
				final Field[] fs = this.getClass().getFields();
				for (int i = 0; i < fs.length; i++) {
					fs[i].setAccessible(true);
					final Object obj = fs[i].get(this);
					if (!(obj instanceof Class<?>)) {
						fieldList.add(Stringified.toText(fs[i].get(this), false));
					} 
				}
				final String[] ret = new String[fieldList.size()];
				fieldList.toArray(ret);
				return ret;

			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
			return null;
		} 
		
		public Object [] getValues() {
			final LinkedList<Object> fieldList = new LinkedList<Object>();
			try {
				final Field[] fs = this.getClass().getFields();
				for (int i = 0; i < fs.length; i++) {
					fs[i].setAccessible(true);
					final Object obj = fs[i].get(this);
					if (!(obj instanceof Class<?>)) {
						fieldList.add(fs[i].get(this));
					} 
				}
				final Object[] ret = new Object[fieldList.size()];
				fieldList.toArray(ret);
				return ret; 
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			} 
			return null;
		}
		
		public Class<?> [] getTypes() {
			final LinkedList<Class<?>> fieldList = new LinkedList<Class<?>>();
			try {
				final Field[] fs = this.getClass().getFields();
				for (int i = 0; i < fs.length; i++) {
					fs[i].setAccessible(true);
					final Object obj = fs[i].get(this);
					if (!(obj instanceof Class<?>)) {
						try {
							fieldList.add( fs[i].getType() );
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} 
				}
				final Class<?>[] ret = new Class<?>[fieldList.size()];
				fieldList.toArray(ret);
				return ret; 
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
			return null;
		}
		
		public int setValues(Object [] values) {
			int i = 0;
			try {
				final Field[] fs = this.getClass().getFields();
				for (; i < fs.length; i++) {
					fs[i].setAccessible(true); 
					final Object obj = fs[i].get(this); 
					if (!(obj instanceof Class<?>)) { 
						if (values[i] != null) { 
							fs[i].set(this, values[i]); 
						} else { 
							return i; 
						} 
					} 
				}
			} catch (Exception e) {
				e.printStackTrace(); 
				return i; 
			}

			return -1;
		}

		public int fromValues(String[] values) {
			int i = 0;
			try {
				final Field[] fs = this.getClass().getFields();
				for (; i < fs.length; i++) {
					fs[i].setAccessible(true); 
					final Object obj = fs[i].get(this); 
					if (!(obj instanceof Class<?>)) { 
						final Object ret = Stringified.fromText(fs[i].getType(), values[i])[0];
						if (ret != null) { 
							fs[i].set(this, ret); 
						} else { 
							return i; 
						} 
					} 
				}
			} catch (Exception e) {
				e.printStackTrace(); 
				return i; 
			}

			return -1;
		}
	}

	// public static class _CCAction extends AbstractNewAction {
	// }
	// public static class _FiniteTimeAction extends AbstractNewAction {
	// public float _float0;
	// }
	public static class _RepeatForever extends AbstractNewAction implements Cloneable{
		// public IntervalAction _IntervalAction0;
	}

	public static class _Speed extends AbstractNewAction implements Cloneable {
		// public CCAction _CCAction0;
		public float speed = 1.0f;
	}

	// public static class _EaseAction extends AbstractNewAction {
	// //public IntervalAction _IntervalAction0;
	// }
	public static class _EaseBackIn extends AbstractNewAction implements Cloneable {
		// public IntervalAction _IntervalAction0;
	}

	public static class _EaseBackInOut extends AbstractNewAction implements Cloneable {
		// public IntervalAction _IntervalAction0;
	}

	public static class _EaseBackOut extends AbstractNewAction implements Cloneable {
		// public IntervalAction _IntervalAction0;
	}

	public static class _EaseBounce extends AbstractNewAction implements Cloneable {
		// public IntervalAction _IntervalAction0;
	}

	public static class _EaseBounceIn extends AbstractNewAction implements Cloneable {
		// public IntervalAction _IntervalAction0;
	}

	public static class _EaseBounceInOut extends AbstractNewAction implements Cloneable {
		// public IntervalAction _IntervalAction0;
	}

	public static class _EaseBounceOut extends AbstractNewAction implements Cloneable {
		// public IntervalAction _IntervalAction0;
	}

//	public static class _EaseElastic extends AbstractNewAction {
//		// public IntervalAction _IntervalAction0;
//		public float period = 1.0f;
//	}

	public static class _EaseElasticIn extends AbstractNewAction implements Cloneable {
		// public IntervalAction _IntervalAction0;
		public float period = 1.0f;
	}

	public static class _EaseElasticInOut extends AbstractNewAction implements Cloneable {
		// public IntervalAction _IntervalAction0;
		public float period = 1.0f;
	}

	public static class _EaseElasticOut extends AbstractNewAction implements Cloneable {
		// public IntervalAction _IntervalAction0;
		public float period = 1.0f;
	}

	public static class _EaseExponentialIn extends AbstractNewAction implements Cloneable {
		// public IntervalAction _IntervalAction0;
	}

	public static class _EaseExponentialInOut extends AbstractNewAction implements Cloneable {
		// public IntervalAction _IntervalAction0;
	}

	public static class _EaseExponentialOut extends AbstractNewAction implements Cloneable {
		// public IntervalAction _IntervalAction0;
	}

	public static class _EaseIn extends AbstractNewAction implements Cloneable {
		// public IntervalAction _IntervalAction0;
		public float rate = 1;
	}

	public static class _EaseInOut extends AbstractNewAction implements Cloneable {
		// public IntervalAction _IntervalAction0;
		public float rate = 1;
	}

	public static class _EaseOut extends AbstractNewAction implements Cloneable {
		// public IntervalAction _IntervalAction0;
		public float rate = 1;
	}

	// public static class _EaseRateAction extends AbstractNewAction {
	// //public IntervalAction _IntervalAction0;
	// public float rate = 1;
	// }
	public static class _EaseSineIn extends AbstractNewAction implements Cloneable {
		// public IntervalAction _IntervalAction0;
	}

	public static class _EaseSineInOut extends AbstractNewAction implements Cloneable {
		// public IntervalAction _IntervalAction0;
	}

	public static class _EaseSineOut extends AbstractNewAction implements Cloneable {
		// public IntervalAction _IntervalAction0;
	}

	// public static class _CallFunc extends AbstractNewAction {
	// }
	// public static class _CallFuncN extends AbstractNewAction {
	// }
	// public static class _CallFuncND extends AbstractNewAction {
	// public Object _Object0;
	// public String _String1;
	// public Object _Object2;
	// }
	public static class _Hide extends AbstractNewAction implements Cloneable {
	}

	// public static class _InstantAction extends AbstractNewAction {
	// }
	public static class _Place extends AbstractNewAction implements Cloneable {
		public float x = 0;
		public float y = 0;
	}

	public static class _Show extends AbstractNewAction implements Cloneable {
	}

	public static class _ToggleVisibility extends AbstractNewAction implements Cloneable {
	}

	public static class _BezierBy extends AbstractNewAction implements Cloneable {
		public float duration = 1.0f;
		public ElfPointf startPos;
		public ElfPointf endPos;
		public ElfPointf control1;
		public ElfPointf control2;
	}

	public static class _Blink extends AbstractNewAction implements Cloneable {
		public float duration = 1.0f;
		public int times;
	}

	public static class _DelayTime extends AbstractNewAction implements Cloneable {
		public float duration = 1.0f;
	}

	public static class _FadeIn extends AbstractNewAction implements Cloneable {
		public float duration = 1.0f;
	}

	public static class _FadeOut extends AbstractNewAction implements Cloneable {
		public float duration = 1.0f;
	}

	public static class _FadeTo extends AbstractNewAction implements Cloneable {
		public float duration = 1.0f;
		public float alpha = 1.0f;
	}

	// public static class _IntervalAction extends AbstractNewAction {
	// public float _float0;
	// }
	public static class _JumpBy extends AbstractNewAction implements Cloneable {
		public float duration = 1.0f;
		public float x;
		public float y;
		public float height;
		public int jumps;
	}

	public static class _JumpTo extends AbstractNewAction implements Cloneable {
		public float duration;
		public float x;
		public float y;
		public float height;
		public int jumps;
	}

	public static class _MoveBy extends AbstractNewAction implements Cloneable {
		public float duration = 1.0f;
		public float x;
		public float y;
	}

	public static class _MoveTo extends AbstractNewAction implements Cloneable {
		public float duration = 1.0f;
		public float x;
		public float y;
	}

	public static class _Repeat extends AbstractNewAction implements Cloneable {
		// public FiniteTimeAction _FiniteTimeAction0;
		public int times;
	}

	public static class _ReverseTime extends AbstractNewAction implements Cloneable {
		// public FiniteTimeAction _FiniteTimeAction0;
	}

	public static class _RotateBy extends AbstractNewAction implements Cloneable {
		public float duration = 1.0f;
		public float angle;
	}

	public static class _RotateTo extends AbstractNewAction implements Cloneable {
		public float duration = 1.0f;
		public float angle;
	}

	public static class _ScaleBy extends AbstractNewAction implements Cloneable {
		public float duration = 1.0f;
		public float x;
		public float y;
	}

	public static class _ScaleTo extends AbstractNewAction implements Cloneable {
		public float duration = 1.0f;
		public float x;
		public float y;
	}

	// public static class _Sequence extends AbstractNewAction {
	// //public FiniteTimeAction _FiniteTimeAction0;
	// //public FiniteTimeAction _FiniteTimeAction1;
	// }
	public static class _Spawn extends AbstractNewAction implements Cloneable {
		// public FiniteTimeAction _FiniteTimeAction0;
		// public FiniteTimeAction _FiniteTimeAction1;
		public CCAction newAction(final CCAction[] actions) {
			try {
				final FiniteTimeAction _one = (FiniteTimeAction) actions[0];
				final FiniteTimeAction[] _two = new FiniteTimeAction[actions.length - 1];
				for (int i = 0; i < _two.length; i++) { 
					_two[i] = (FiniteTimeAction) actions[i+1];
				} 
				return Spawn.actions(_one, _two);
			} catch (Exception e) { 
				e.printStackTrace();
			}
			return null;
		}
	}

	public static class _TintBy extends AbstractNewAction implements Cloneable {
		public float duration = 1.0f;
		public int red;
		public int green;
		public int blue;
	}

	public static class _TintTo extends AbstractNewAction implements Cloneable {
		public float duration = 1.0f;
		public int red;
		public int green;
		public int blue;
	}

	public static class _Sequence extends AbstractNewAction implements Cloneable { 
		public CCAction newAction(final CCAction[] actions) { 
			try {
				final FiniteTimeAction _one = (FiniteTimeAction) actions[0];
				final FiniteTimeAction[] _two = new FiniteTimeAction[actions.length - 1];
				for (int i = 0; i < _two.length; i++) { 
					_two[i] = (FiniteTimeAction) actions[i+1];
				} 
				return Sequence.actions(_one, _two);
			} catch (Exception e) {
				e.printStackTrace(); 
			} 
			return null;
		} 
	}
	
	public static class _ElfInterAction extends AbstractNewAction implements Cloneable {
		// public IntervalAction _IntervalAction0;
		public InterType interpolator = InterType.Linear;
	}
	
	public static class _Action3DBy extends AbstractNewAction implements Cloneable {
		public float duration = 1.0f;
		public ElfVertex3 position;
		public ElfVertex3 rotate;
		public ElfVertex3 scale;
	}
	
	public static class _Action3DTo extends AbstractNewAction implements Cloneable {
		public float duration = 1.0f;
		public ElfVertex3 position;
		public ElfVertex3 rotate;
		public ElfVertex3 scale;
	}
	
	public final static HashMap<String, LinkedList<Class<?>>> CategoryMap = new HashMap<String, LinkedList<Class<?>>>();
//	public stat
	static { 
		CategoryMap.put("base", new LinkedList<Class<?>>());
		CategoryMap.put("ease", new LinkedList<Class<?>>());
		CategoryMap.put("instant", new LinkedList<Class<?>>());
		CategoryMap.put("interval", new LinkedList<Class<?>>());
		CategoryMap.put("interpolator", new LinkedList<Class<?>>());
		
		final Set<String> set = CategoryMap.keySet(); 
		
		final Class<?>[] _classes = CCActionHelper.class.getDeclaredClasses();
		for (int i = 0; i < _classes.length; i++) {
			if (AbstractNewAction.class.isAssignableFrom(_classes[i])) {
				final int ii = i;
				final String name = _classes[i].getSimpleName().substring(1);
				
				for(Class<?> _class : sActionClass) {
					final String className = _class.getCanonicalName();
					if (className != null && className.endsWith(name)) { 
						for(String key : set) {
							if(className.contains(key)) { 
								final LinkedList<Class<?>> list = CategoryMap.get(key); 
								list.add(_classes[ii]); 
								break; 
							}
						}
					}
				}
			} 
		} 
	} 
	
	static void printForC() {
		try {
			//auto
			println("//auto");
			
			final Set<String> set = CategoryMap.keySet(); 
			for(String key : set) {
				println("//" + key);
				final LinkedList<Class<?>> list = CategoryMap.get(key);
				for(Class<?> _class : list) {
					final String name = _class.getSimpleName().substring(1);
					println( "if( strcmp(actionClassName, \""+name+"\") == 0) {" ); 
					
					final AbstractNewAction abstractNewAction = (AbstractNewAction)_class.newInstance();
					
					final Constructor<?> constructor = abstractNewAction.mActoinClass.getDeclaredConstructors()[0]; 
					final Class<?> [] parameters = constructor.getParameterTypes(); 
					
					int count = 0;
					String actionType = "";
					for(int i=0;i<parameters.length; i++) { 
						if(CCAction.class.isAssignableFrom(parameters[i])) {
							count ++;
							actionType = "CCAction"+parameters[i].getSimpleName().replace("CCAction", "").replace("Action", "");
							if(actionType.equals("CCActionFiniteTime")) {
								actionType = "CCFiniteTimeAction";
							}
						} else {
//							System.out.println(parameters[i].getSimpleName());
						} 
					}
					
					String fieldParameters = "";
					
					final Field [] fs = _class.getFields();
					for(int i=0; i<fs.length; i++) {
						final String fieldName = fs[i].getName();
						if(!fieldName.contains("name") && !fieldName.contains("Class")) { 
							fieldParameters += fieldName +", ";
							//QueryFloatAttribute
							final String simpleName = fs[i].getType().getSimpleName();
							final String tmp = Character.toUpperCase( simpleName.charAt(0) ) + simpleName.substring(1);
							println("\t"+simpleName+" "+fieldName + ";");
							println("\t"+"element->Query"+tmp+"Attribute(\""+fieldName+"\", &"+fieldName+");");
						} 
					} 
					
					if(count == 0) { 
						
					} else if(count == 1) { 
						fieldParameters = "("+actionType+"*)(actionArray->objectAtIndex(0)), " + fieldParameters;
					} else { 
						fieldParameters = "actionArray";
					} 
					
					if(fieldParameters.endsWith(", ")) { 
						fieldParameters = fieldParameters.substring(0, fieldParameters.length()-2);
					}
					
					if(!name.contains("cale")) {
						fieldParameters = fieldParameters.replace("x, y", "ccp(x, y)");
					}
					
					println("\treturn CC"+name+"::create( "+fieldParameters+" );");
					println( "}" ); 
				} 
			} 
		} catch (Exception e) { 
			e.printStackTrace();
		} 
	} 
	
	static void println(String out) { 
		System.err.println("\t" + out); 
	} 
	
	
	
	public static void main(final String [] args) {
//		final String pack = "org.cocos2d.actions";
//		final String path = FileHelper.getUserDir()+FileHelper.DECOLLATOR+"bin"+FileHelper.DECOLLATOR+pack.replace(".",FileHelper.DECOLLATOR);
//		FileHelper.travelFiles(path, new IteratorFile() {
//			public boolean iterator(File file) {
//				final String name = file.getName();
//				final String fullPath = file.getAbsolutePath();
//				if(name.endsWith(".class")) { 
//					final String className = fullPath.substring( fullPath.indexOf("bin") + 4 ).replace(FileHelper.DECOLLATOR, ".");
//					if(className.contains("Scheduler") || className.contains("$") || className.contains("ActionManager")) {
//					} else {
//						System.err.printf("\t\taddCCActionClass(Class.forName(\"%s\"));\n", className.replace(".class", "")); 
//					}
//				} 
//				return false;
//			}
//		});
	}
	
	
} 
