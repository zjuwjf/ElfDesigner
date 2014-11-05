package elfEngine.basic.counter;

public class InterHelper { 
	
	public enum InterType implements Interpolator { 
		AcceDece { 
			public float getInterpolation(float input) {
				return (float) (Math.cos((input + 1) * Math.PI) / 2.0f) + 0.5f;
			}

			public float getInterpolation(float input, float rate) {
				return 0;
			}
		},
		Acce {
			private float mFactor = 1;
			private double mDoubleFactor = mFactor * 2;

			public float getInterpolation(float input) {
				if (mFactor == 1.0f) {
					return input * input;
				} else {
					return (float) Math.pow(input, mDoubleFactor);
				}
			}

			public float getInterpolation(float input, float rate) {
				return 0;
			}
		},
		Anticipate {
			private final float mTension = 2.0f;
			public float getInterpolation(float t) {
				return t * t * ((mTension + 1) * t - mTension);
			}
			@Override
			public float getInterpolation(float input, float rate) {
				// TODO Auto-generated method stub
				return 0;
			}
		},
		AnticipateOvershoot {
			private final float mTension = 2.0f * 1.5f;

			public float getInterpolation(float t) {
				if (t < 0.5f)
					return 0.5f * a(t * 2.0f, mTension);
				else
					return 0.5f * (o(t * 2.0f - 2.0f, mTension) + 2.0f);
			}

			private float a(float t, float s) {
				return t * t * ((s + 1) * t - s);
			}

			private float o(float t, float s) {
				return t * t * ((s + 1) * t + s);
			}

			@Override
			public float getInterpolation(float input, float rate) {
				// TODO Auto-generated method stub
				return 0;
			}

		},
		Bounce {
			public float getInterpolation(float t) {
				t *= 1.1226f;
				if (t < 0.3535f)
					return bounce(t);
				else if (t < 0.7408f)
					return bounce(t - 0.54719f) + 0.7f;
				else if (t < 0.9644f)
					return bounce(t - 0.8526f) + 0.9f;
				else
					return bounce(t - 1.0435f) + 0.95f;
			}

			private float bounce(float t) {
				return t * t * 8.0f;
			}

			@Override
			public float getInterpolation(float input, float rate) {
				// TODO Auto-generated method stub
				return 0;
			}

		},
		Cycle {
			private float mCycles = 1.0f;

			public float getInterpolation(float input) {
				return (float) (Math.sin(2 * mCycles * Math.PI * input));
			}

			@Override
			public float getInterpolation(float input, float rate) {
				// TODO Auto-generated method stub
				return 0;
			}
		},
		Dece {
			private float mFactor = 1.0f;

			public float getInterpolation(float input) {
				if (mFactor == 1.0f) {
					return (float) (1.0f - (1.0f - input) * (1.0f - input));
				} else {
					return (float) (1.0f - Math.pow((1.0f - input), 2 * mFactor));
				}
			}

			@Override
			public float getInterpolation(float input, float rate) {
				// TODO Auto-generated method stub
				return 0;
			}
		},
		Linear {
			public float getInterpolation(float input) {
				return input;
			}

			@Override
			public float getInterpolation(float input, float rate) {
				// TODO Auto-generated method stub
				return 0;
			}
		},
		Overshoot {
			private final float mTension = 2.0f;

			public float getInterpolation(float t) {
				t -= 1.0f;
				return t * t * ((mTension + 1) * t + mTension) + 1.0f;
			}

			@Override
			public float getInterpolation(float input, float rate) {
				// TODO Auto-generated method stub
				return 0;
			}
		},
		Viscous {
			private static final float mViscousFluidScale = 8.0f;
			private float mViscousFluidNormalize = 1.0005767f;

			public float getInterpolation(float x) {
				x *= mViscousFluidScale;
				if (x < 1.0f) {
					x -= (1.0f - (float) Math.exp(-x));
				} else {
					float start = 0.36787944117f; // 1/e == exp(-1)
					x = 1.0f - (float) Math.exp(1.0f - x);
					x = start + x * (1.0f - start);
				}
				x *= mViscousFluidNormalize;
				return x;
			}

			@Override
			public float getInterpolation(float input, float rate) {
				// TODO Auto-generated method stub
				return 0;
			}
		},

		EaseBackIn {
			public float getInterpolation(float t) {
				float overshoot = 1.70158f;
				return t * t * ((overshoot + 1) * t - overshoot);
			}

			@Override
			public float getInterpolation(float input, float rate) {
				// TODO Auto-generated method stub
				return 0;
			}
		},

		EaseBackOut {
			public float getInterpolation(float t) {
				float overshoot = 1.70158f;
				t = t - 1;
				return (t * t * ((overshoot + 1) * t + overshoot) + 1);
			}

			@Override
			public float getInterpolation(float input, float rate) {
				// TODO Auto-generated method stub
				return 0;
			}
		},

		EaseBackInOut {
			public float getInterpolation(float t) {
				float overshoot = 1.70158f * 1.525f;
				t = t * 2;
				if (t < 1) {
					return ((t * t * ((overshoot + 1) * t - overshoot)) / 2);
				} else {
					t = t - 2;
					return ((t * t * ((overshoot + 1) * t + overshoot)) / 2 + 1);
				}
			}

			@Override
			public float getInterpolation(float input, float rate) {
				// TODO Auto-generated method stub
				return 0;
			}
		},

		EaseBounceIn {
			public float getInterpolation(float t) {
				float newT = 1 - bounceTime(1 - t);
				return newT;
			}

			@Override
			public float getInterpolation(float input, float rate) {
				// TODO Auto-generated method stub
				return 0;
			}
		},

		EaseBounceOut {
			public float getInterpolation(float t) {
				float newT = bounceTime(t);
				return newT;
			}

			@Override
			public float getInterpolation(float input, float rate) {
				// TODO Auto-generated method stub
				return 0;
			}
		},

		EaseBounceInOut {
			public float getInterpolation(float t) {
				float newT = 0;
				if (t < 0.5) {
					t = t * 2;
					newT = (1 - bounceTime(1 - t)) * 0.5f;
				} else
					newT = bounceTime(t * 2 - 1) * 0.5f + 0.5f;
				return newT;
			}

			@Override
			public float getInterpolation(float input, float rate) {
				// TODO Auto-generated method stub
				return 0;
			}
		},
		
		EaseExponentialIn {
			public float getInterpolation(float t) {
				return ((t == 0) ? 0 : (float) Math.pow(2, 10 * (t / 1 - 1)) - 1 * 0.001f);
			}

			@Override
			public float getInterpolation(float input, float rate) {
				// TODO Auto-generated method stub
				return 0;
			}
			
		},
		
		EaseExponentialOut {
			public float getInterpolation(float t) {
				return ((t == 1) ? 1 : ((float) (-Math.pow(2, -10 * t / 1) + 1)));
			}

			@Override
			public float getInterpolation(float input, float rate) {
				// TODO Auto-generated method stub
				return 0;
			}
		},
		
		EaseExponentialInOut {
			public float getInterpolation(float t) {
				if ((t /= 0.5f) < 1)
		            t = 0.5f * (float) Math.pow(2, 10 * (t - 1));
		        else
		            t = 0.5f * (-(float) Math.pow(2, -10 * --t) + 2);
				return t;
			}

			@Override
			public float getInterpolation(float input, float rate) {
				// TODO Auto-generated method stub
				return 0;
			}
		},
		
		EaseSineIn {
			public float getInterpolation(float t) {
				return (-1 * (float)Math.cos(t * (float) Math.PI / 2) + 1);
			}

			@Override
			public float getInterpolation(float input, float rate) {
				// TODO Auto-generated method stub
				return 0;
			}
		},
		
		EaseSineOut {
			public float getInterpolation(float t) {
				return ((float)Math.sin(t * (float) Math.PI / 2));
			}

			@Override
			public float getInterpolation(float input, float rate) {
				// TODO Auto-generated method stub
				return 0;
			}
		},
		
		EaseSineInOut {
			public float getInterpolation(float t) {
				return (-0.5f * ((float)Math.cos(Math.PI * t) - 1));
			}

			@Override
			public float getInterpolation(float input, float rate) {
				// TODO Auto-generated method stub
				return 0;
			}
		},
		
		FlashEase {
			/***
			 * rate = [-1,1]
			 */
			public float getInterpolation(float t, float rate) {
				return (1-rate) * t + rate * t * t;
			}
			
			public float getInterpolation(float t) {
				return 0;
			}
		}
		;

		private final static float bounceTime(float t) {
			if (t < 1 / 2.75) {
				return 7.5625f * t * t;
			} else if (t < 2 / 2.75) {
				t -= 1.5f / 2.75f;
				return 7.5625f * t * t + 0.75f;
			} else if (t < 2.5 / 2.75) {
				t -= 2.25f / 2.75f;
				return 7.5625f * t * t + 0.9375f;
			}

			t -= 2.625f / 2.75f;
			return 7.5625f * t * t + 0.984375f;
		}
	} 
} 
