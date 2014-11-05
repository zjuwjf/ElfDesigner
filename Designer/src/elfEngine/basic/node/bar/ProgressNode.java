package elfEngine.basic.node.bar;

import org.lwjgl.opengl.GL11;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ElfNode;
import elfEngine.opengl.GLHelper;
import elfEngine.opengl.GLManage;
import elfEngine.opengl.TextureRegion;

public class ProgressNode extends ElfNode {

	public enum ProgressTimerType {
		// / Radial Counter-Clockwise
		Radial,
		// / Bar
		Bar,
	};

	public ProgressNode(ElfNode father, int ordinal) {
		super(father, ordinal);

		setName("#progress");
	}
	
	// setPercentage (0-100)
	private float mPercentage = 50;
	
	public float getPercentage() {
		return mPercentage;
	}

	public void setPercentage(float mPercentage) {
		mPercentage = Math.max(mPercentage, 0);
		mPercentage = Math.min(mPercentage, 100);
		this.mPercentage = mPercentage;
		updateUV();
	}

	protected final static int REF_Percentage = DEFAULT_SHIFT;

	// setBarChangeRate
	private final ElfPointf mBarChangeRate = new ElfPointf(0.5f, 0.5f);

	public void setBarChangeRate(ElfPointf rate) {
		if (rate != null) {
			mBarChangeRate.set(rate);
			updateUV();
		}
	}

	public ElfPointf getBarChangeRate() {
		return mBarChangeRate;
	}

	protected final static int REF_BarChangeRate = DEFAULT_SHIFT;

	// setMidpoint
	private final ElfPointf mMidPoint = new ElfPointf(0.5f, 0.5f);

	public ElfPointf getMidPoint() {
		return mMidPoint;
	}

	public void setMidPoint(ElfPointf mid) {
		if (mid != null) {
			mMidPoint.set(mid);
			updateUV();
		}
	}

	protected final static int REF_MidPoint = DEFAULT_SHIFT;

	// setType
	private ProgressTimerType mProgressTimerType = ProgressTimerType.Radial;

	public ProgressTimerType getProgressTimerType() {
		return mProgressTimerType;
	}

	public void setProgressTimerType(ProgressTimerType mProgressTimerType) {
		if (mProgressTimerType != null) {
			this.mProgressTimerType = mProgressTimerType;
			updateUV();
		}
	}

	protected final static int REF_ProgressTimerType = DEFAULT_SHIFT;

	private boolean mReverseDirection = false;

	public void setReverseDirection(boolean reverseDirection) {
		mReverseDirection = reverseDirection;
		updateUV();
	}

	public boolean getReverseDirection() {
		return mReverseDirection;
	}

	protected final static int REF_ReverseDirection = DEFAULT_SHIFT;

	private final ElfPointf[] mSrcPos = new ElfPointf[10];
	private final ElfPointf[] mDstPos = new ElfPointf[10];
	private int mVertexCount = 0;
	
	public void setResid(String resid) {
		super.setResid(resid);
		this.updateUV();
	}
	
	public void drawSelf() {
		final TextureRegion tr = GLManage.loadTextureRegion(this.getResid(), this.getGLId());
		if (tr != null) { 
			GLHelper.glBindTexture(tr.texture.getGLBindId());
			if (mProgressTimerType == ProgressTimerType.Bar) {
				if (mVertexCount == 8) {
					GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
					for (int i = 0; i < 4; i++) { 
						GL11.glTexCoord2f(mSrcPos[i].x, mSrcPos[i].y);
						GL11.glVertex3f(mDstPos[i].x, mDstPos[i].y, 0);
					}
					GL11.glEnd();

					GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
					for (int i = 4; i < 8; i++) {
						GL11.glTexCoord2f(mSrcPos[i].x, mSrcPos[i].y);
						GL11.glVertex3f(mDstPos[i].x, mDstPos[i].y, 0);
					}
					GL11.glEnd();
				} else {
					GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
					for (int i = 0; i < 4; i++) {
						GL11.glTexCoord2f(mSrcPos[i].x, mSrcPos[i].y);
						GL11.glVertex3f(mDstPos[i].x, mDstPos[i].y, 0);
					}
					GL11.glEnd();
				}
			} else if (mProgressTimerType == ProgressTimerType.Radial) {
				GL11.glBegin(GL11.GL_TRIANGLE_FAN);
				for (int i = 0; i < mVertexCount; i++) {
					GL11.glTexCoord2f(mSrcPos[i].x, mSrcPos[i].y);
					GL11.glVertex3f(mDstPos[i].x, mDstPos[i].y, 0);
				}
				GL11.glEnd();
			} 
		}
	}

	public void myRefresh() {
		super.myRefresh();
		updateUV();
	}

	void updateUV() {
		final TextureRegion tr = GLManage.loadTextureRegion(getResid(), getGLId());
		if (tr != null) {
			if (mProgressTimerType == ProgressTimerType.Radial) {
				updateRadial(tr);
			} else {
				updateBar(tr);
			}
		}
	}

	void updateBar(final TextureRegion tr) {
		float alpha = mPercentage / 100.0f;
		final ElfPointf alphaOffset = ccpMult(ccp(1.0f * (1.0f - mBarChangeRate.x) + alpha * mBarChangeRate.x, 1.0f * (1.0f - mBarChangeRate.y) + alpha * mBarChangeRate.y), 0.5f);
		final ElfPointf min = ccpSub(mMidPoint, alphaOffset);
		final ElfPointf max = ccpAdd(mMidPoint, alphaOffset);

		if (min.x < 0.f) {
			max.x += -min.x;
			min.x = 0.f;
		}

		if (max.x > 1.f) {
			min.x -= max.x - 1.f;
			max.x = 1.f;
		}

		if (min.y < 0.f) {
			max.y += -min.y;
			min.y = 0.f;
		}

		if (max.y > 1.f) {
			min.y -= max.y - 1.f;
			max.y = 1.f;
		}

		if (!mReverseDirection) {
			mVertexCount = 4;
			// TOPLEFT
			mSrcPos[0] = textureCoordFromAlphaPoint(ccp(min.x, max.y), tr);
			mDstPos[0] = vertexFromAlphaPoint(ccp(min.x, max.y), tr);

			// BOTLEFT
			mSrcPos[1] = textureCoordFromAlphaPoint(ccp(min.x, min.y), tr);
			mDstPos[1] = vertexFromAlphaPoint(ccp(min.x, min.y), tr);

			// TOPRIGHT
			mSrcPos[2] = textureCoordFromAlphaPoint(ccp(max.x, max.y), tr);
			mDstPos[2] = vertexFromAlphaPoint(ccp(max.x, max.y), tr);

			// BOTRIGHT
			mSrcPos[3] = textureCoordFromAlphaPoint(ccp(max.x, min.y), tr);
			mDstPos[3] = vertexFromAlphaPoint(ccp(max.x, min.y), tr);
		} else {
			mVertexCount = 8;
			// TOPLEFT 1
			mSrcPos[0] = textureCoordFromAlphaPoint(ccp(0, 1), tr);
			mDstPos[0] = vertexFromAlphaPoint(ccp(0, 1), tr);

			// BOTLEFT 1
			mSrcPos[1] = textureCoordFromAlphaPoint(ccp(0, 0), tr);
			mDstPos[1] = vertexFromAlphaPoint(ccp(0, 0), tr);

			// TOPRIGHT 2
			mSrcPos[6] = textureCoordFromAlphaPoint(ccp(1, 1), tr);
			mDstPos[6] = vertexFromAlphaPoint(ccp(1, 1), tr);

			// BOTRIGHT 2
			mSrcPos[7] = textureCoordFromAlphaPoint(ccp(1, 0), tr);
			mDstPos[7] = vertexFromAlphaPoint(ccp(1, 0), tr);

			// TOPRIGHT 1
			mSrcPos[2] = textureCoordFromAlphaPoint(ccp(min.x, max.y), tr);
			mDstPos[2] = vertexFromAlphaPoint(ccp(min.x, max.y), tr);

			// BOTRIGHT 1
			mSrcPos[3] = textureCoordFromAlphaPoint(ccp(min.x, min.y), tr);
			mDstPos[3] = vertexFromAlphaPoint(ccp(min.x, min.y), tr);

			// TOPLEFT 2
			mSrcPos[4] = textureCoordFromAlphaPoint(ccp(max.x, max.y), tr);
			mDstPos[4] = vertexFromAlphaPoint(ccp(max.x, max.y), tr);

			// BOTLEFT 2
			mSrcPos[5] = textureCoordFromAlphaPoint(ccp(max.x, min.y), tr);
			mDstPos[5] = vertexFromAlphaPoint(ccp(max.x, min.y), tr);
		}
	}

	void updateRadial(final TextureRegion tr) {
		float alpha = mPercentage / 100.f;
		float angle = 2.f * ((float) Math.PI) * (mReverseDirection ? alpha : 1.0f - alpha);

		// We find the vector to do a hit detection based on the percentage
		// We know the first vector is the one @ 12 o'clock (top,mid) so we
		// rotate
		// from that by the progress angle around the m_tMidpoint pivot
		final ElfPointf topMid = new ElfPointf(mMidPoint.x, 1.f);

		final ElfPointf percentagePt = rotateByAngle(topMid, mMidPoint, angle);

		int index = 0;
		final ElfPointf hit = new ElfPointf();

		if (alpha == 0.f) {
			// More efficient since we don't always need to check intersection
			// If the alpha is zero then the hit point is top mid and the index
			// is 0.
			hit.set(topMid);
			index = 0;
		} else if (alpha == 1.f) {
			// More efficient since we don't always need to check intersection
			// If the alpha is one then the hit point is top mid and the index
			// is 4.
			hit.set(topMid);
			index = 4;
		} else {
			// We run a for loop checking the edges of the texture to find the
			// intersection point
			// We loop through five points since the top is split in half

			float min_t = Float.MAX_VALUE;
			final int kProgressTextureCoordsCount = 4;
			for (int i = 0; i <= kProgressTextureCoordsCount; ++i) {
				int pIndex = (i + (kProgressTextureCoordsCount - 1)) % kProgressTextureCoordsCount;

				ElfPointf edgePtA = boundaryTexCoord((i % kProgressTextureCoordsCount));
				ElfPointf edgePtB = boundaryTexCoord((pIndex));

				// Remember that the top edge is split in half for the 12
				// o'clock position
				// Let's deal with that here by finding the correct endpoints
				if (i == 0) {
					edgePtB = ccpLerp(edgePtA, edgePtB, 1 - mMidPoint.x);
				} else if (i == 4) {
					edgePtA = ccpLerp(edgePtA, edgePtB, 1 - mMidPoint.x);
				}

				// s and t are returned by ccpLineIntersect
				final ElfPointf st = new ElfPointf();
				if (ccpLineIntersect(edgePtA, edgePtB, mMidPoint, percentagePt, st)) {
					// Since our hit test is on rays we have to deal with the
					// top edge
					// being in split in half so we have to test as a segment
					if ((i == 0 || i == 4)) {
						// s represents the point between edgePtA--edgePtB
						if (!(0.f <= st.x && st.x <= 1.f)) {
							continue;
						}
					}
					// As long as our t isn't negative we are at least finding a
					// correct hitpoint from m_tMidpoint to percentagePt.
					if (st.y >= 0.f) {
						// Because the percentage line and all the texture edges
						// are
						// rays we should only account for the shortest
						// intersection
						if (st.y < min_t) {
							min_t = st.y;
							index = i;
						}
					}
				}
			}

			// Now that we have the minimum magnitude we can use that to find
			// our intersection
			hit.set(ccpAdd(mMidPoint, ccpMult(ccpSub(percentagePt, mMidPoint), min_t)));

		}

		// The size of the vertex data is the index from the hitpoint
		// the 3 is for the m_tMidpoint, 12 o'clock point and hitpoint position.

		mVertexCount = index + 3;
		// First we populate the array with the m_tMidpoint, then all
		// vertices/texcoords/colors of the 12 'o clock start and edges and the
		// hitpoint
		mSrcPos[0] = textureCoordFromAlphaPoint(mMidPoint, tr);
		mDstPos[0] = vertexFromAlphaPoint(mMidPoint, tr);

		mSrcPos[1] = textureCoordFromAlphaPoint(topMid, tr);
		mDstPos[1] = vertexFromAlphaPoint(topMid, tr);

		for (int i = 0; i < index; ++i) {
			ElfPointf alphaPoint = boundaryTexCoord((int) i);
			mSrcPos[i + 2] = textureCoordFromAlphaPoint(alphaPoint, tr);
			mDstPos[i + 2] = vertexFromAlphaPoint(alphaPoint, tr);
		}

		// hitpoint will go last
		mSrcPos[mVertexCount - 1] = textureCoordFromAlphaPoint(hit, tr);
		mDstPos[mVertexCount - 1] = vertexFromAlphaPoint(hit, tr);

	}

	private ElfPointf ccp(float f, float g) {
		return new ElfPointf(f, g);
	}

	private ElfPointf ccpAdd(ElfPointf mMidPoint2, ElfPointf ccpMult) {
		return new ElfPointf(mMidPoint2.x + ccpMult.x, mMidPoint2.y + ccpMult.y);
	}

	private ElfPointf ccpMult(ElfPointf ccpSub, float min_t) {
		return new ElfPointf(ccpSub.x * min_t, ccpSub.y * min_t);
	}

	private ElfPointf ccpSub(ElfPointf percentagePt, ElfPointf mMidPoint2) {
		return new ElfPointf(percentagePt.x - mMidPoint2.x, percentagePt.y - mMidPoint2.y);
	}

	final ElfPointf rotateByAngle(final ElfPointf v, final ElfPointf pivot, final float angle) {
		final ElfPointf r = new ElfPointf(v.x - pivot.x, v.y - pivot.y);
		final float cosa = (float) Math.cos(angle), sina = (float) Math.sin(angle);
		float t = r.x;
		r.x = t * cosa - r.y * sina + pivot.x;
		r.y = t * sina + r.y * cosa + pivot.y;
		return r;
	}

	final ElfPointf boundaryTexCoord(int index) {
		final int kProgressTextureCoordsCount = 4;
		final int kCCProgressTextureCoords = 0x4b;
		if (index < kProgressTextureCoordsCount) {
			if (mReverseDirection) {
				return ccp((kCCProgressTextureCoords >> (7 - (index << 1))) & 1, (kCCProgressTextureCoords >> (7 - ((index << 1) + 1))) & 1);
			} else {
				return ccp((kCCProgressTextureCoords >> ((index << 1) + 1)) & 1, (kCCProgressTextureCoords >> (index << 1)) & 1);
			}
		}
		return new ElfPointf(0, 0);
	}

	final ElfPointf ccpLerp(final ElfPointf a, final ElfPointf b, float alpha) {
		return ccpAdd(ccpMult(a, 1.f - alpha), ccpMult(b, alpha));
	}

	boolean ccpLineIntersect(final ElfPointf A, final ElfPointf B, final ElfPointf C, final ElfPointf D, final ElfPointf ST) {
		// FAIL: Line undefined
		if ((A.x == B.x && A.y == B.y) || (C.x == D.x && C.y == D.y)) {
			return false;
		}
		final float BAx = B.x - A.x;
		final float BAy = B.y - A.y;
		final float DCx = D.x - C.x;
		final float DCy = D.y - C.y;
		final float ACx = A.x - C.x;
		final float ACy = A.y - C.y;

		final float denom = DCy * BAx - DCx * BAy;

		ST.x = DCx * ACy - DCy * ACx;
		ST.y = BAx * ACy - BAy * ACx;

		if (denom == 0) {
			if (ST.x == 0 || ST.y == 0) {
				// Lines incident
				return true;
			}
			// Lines parallel and not incident
			return false;
		}

		ST.x = ST.x / denom;
		ST.y = ST.y / denom;
		// Point of intersection
		// CGPoint P;
		// P.x = A.x + *S * (B.x - A.x);
		// P.y = A.y + *S * (B.y - A.y);
		return true;
	}

	final ElfPointf textureCoordFromAlphaPoint(final ElfPointf alpha, final TextureRegion tr) {
		// final ElfPoint ret = new ElfPoint(0.0f, 0.0f);
		// CCPoint min = ccp(quad.bl.texCoords.u,quad.bl.texCoords.v);
		// CCPoint max = ccp(quad.tr.texCoords.u,quad.tr.texCoords.v);
		// Fix bug #1303 so that progress timer handles sprite frame texture
		// rotation
		// if (m_pSprite->isTextureRectRotated()) {
		// CC_SWAP(alpha.x, alpha.y, float);
		// }
		final ElfPointf min = new ElfPointf(tr.getU(), tr.getV2());
		final ElfPointf max = new ElfPointf(tr.getU2(), tr.getV());

		return new ElfPointf(min.x * (1.f - alpha.x) + max.x * alpha.x, min.y * (1.f - alpha.y) + max.y * alpha.y);
	}

	final ElfPointf vertexFromAlphaPoint(ElfPointf alpha, final TextureRegion tr) {
		final ElfPointf ret = new ElfPointf(0.0f, 0.0f);
		// CCPoint min = ccp(quad.bl.vertices.x,quad.bl.vertices.y);
		// CCPoint max = ccp(quad.tr.vertices.x,quad.tr.vertices.y);
		
		final float hw = tr.getWidth()/2f;
		final float hh = tr.getHeight()/2f;
		
		final ElfPointf min = new ElfPointf(-hw, -hh);
		final ElfPointf max = new ElfPointf(hw, hh);
		
		ret.x = min.x * (1.f - alpha.x) + max.x * alpha.x;
		ret.y = min.y * (1.f - alpha.y) + max.y * alpha.y;
		return ret;
	}

}
