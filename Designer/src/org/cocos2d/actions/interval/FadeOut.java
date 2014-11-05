package org.cocos2d.actions.interval;


//
// FadeOut
//

public class FadeOut extends IntervalAction {

    public static FadeOut action(float t) {
        return new FadeOut(t);
    }

    protected FadeOut(float t) {
        super(t);
    }

    @Override
    public void update(float t) {
        (target).setAlpha(1 - t);
    }

	@Override
	public FadeOut copy() {
		return new FadeOut(duration);
	}

    @Override
    public IntervalAction reverse() {
        return new FadeIn(duration);
    }
}
