package pl.droidsonroids.crazylayout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

public class CardsLayout extends RelativeLayout {
    private final String TAG = this.getClass().getSimpleName();

    private int mChildHeaderHeight;
    private int mChildMinimumWidth;

    public CardsLayout(Context context) {
        super(context);
    }

    public CardsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardsLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout");
        super.onLayout(changed, l, t, r, b);
    }

    private int computeTopOffset(boolean expanded) {
        if (expanded) {
            return 0;
        } else {
            return getMeasuredHeight() - mChildHeaderHeight;
        }
    }

    private static Animation createAnimation(float toYDelta, long duration, Interpolator interpolator) {
        Animation animation = new TranslateAnimation(0f, 0f, 0f, toYDelta);
        animation.setDuration(duration);
        animation.setInterpolator(interpolator);
        animation.setFillAfter(true);

        return animation;
    }

    private void animateChild(final View child, final int childIndex, final long duration) {
        //boolean expanded = ((LayoutParams) child.getLayoutParams()).mIsExpanded;
        //((LayoutParams) child.getLayoutParams()).mIsExpanded = !expanded;
        //boolean currentExpanded = !expanded;
        //
        //Interpolator interpolator;
        //if (currentExpanded) {
        //   interpolator = new AccelerateInterpolator();
        //} else {
        //    interpolator = new DecelerateInterpolator();
        //}
        //final long topOffset = computeTopOffset(expanded);
        //
        //final Animation animation = currentExpanded ? createAnimation(-topOffset, duration, interpolator) : createAnimation(topOffset, duration, interpolator);
        //
        //animation.setAnimationListener(new Animation.AnimationListener() {
        //    @Override
        //    public void onAnimationStart(final Animation animation) {
        //
        //    }
        //
        //    @Override
        //    public void onAnimationEnd(final Animation anim) {
        //        animation.setAnimationListener(null);
        //        anim.setAnimationListener(null);
        //        anim.cancel();
        //        requestLayout();
        //    }
        //
        //    @Override
        //    public void onAnimationRepeat(final Animation animation) {
        //
        //    }
        //});
        //child.setAnimation(animation);
        //child.animate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean result = super.onTouchEvent(ev);
        Log.d(TAG, "onTouchEvent: " + result);
        return result;
    }

    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        boolean result = super.dispatchTouchEvent(ev);
        Log.d(TAG, "onTouchEvent: " + result);
        return result;
    }

    @Override
    public boolean onInterceptTouchEvent(final MotionEvent ev) {
        boolean result = super.onInterceptTouchEvent(ev);
        Log.d(TAG, "onTouchEvent: " + result);
        return result;
    }

    public void switchState(int childIndex) {
        View childView = getChildAt(childIndex);
        animateChild(childView, childIndex, 300);

        invalidate();
    }

    @Override
    protected void dispatchDraw(final Canvas canvas) {
        Log.d(TAG, "dispatchDraw");
        super.dispatchDraw(canvas);
    }

    @Override
    public void draw(final Canvas canvas) {
        Log.d(TAG, "draw");
        super.draw(canvas);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        Log.d(TAG, "onDraw");
        super.onDraw(canvas);
    }

    @Override
    public void requestLayout() {
        Log.d(TAG, "requestLayout");
        super.requestLayout();
    }

    @Override
    public void addView(final View child) {
        Log.d(TAG, "addView");
        super.addView(child);
    }
}
