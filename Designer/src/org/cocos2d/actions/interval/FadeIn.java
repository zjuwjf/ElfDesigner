package org.cocos2d.actions.interval;


//
// FadeIn
//

public class FadeIn extends IntervalAction {

    public static FadeIn action(float t) {
        return new FadeIn(t);
    }

    protected FadeIn(float t) {
        super(t);
    }

    @Override
    public void update(float t) {
        (target).setAlpha(t);
    }

	@Override
	public FadeIn copy() {
		return new FadeIn(duration);
	}

    @Override
    public IntervalAction reverse() {
        return new FadeOut(duration);
    }
}
