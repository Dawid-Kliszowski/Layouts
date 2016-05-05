package pl.droidsonroids.crazylayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.List;

public class CardsLayout extends RelativeLayout {
    private final String TAG = this.getClass().getSimpleName();

    private int mChildHeaderHeight;
    private int mChildMinimumWidth;

    public CardsLayout(Context context) {
        super(context);
    }

    public CardsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CardsLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public CardsLayout(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CardsLayout,
                0, 0);

        try {
            mChildHeaderHeight = a.getDimensionPixelSize(R.styleable.CardsLayout_childHeaderHeight, 0);
            mChildMinimumWidth = a.getDimensionPixelSize(R.styleable.CardsLayout_minChildWidth, 0);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int availableWidth;

        if (widthMode == MeasureSpec.EXACTLY) {
            availableWidth = widthSize;
        } else if (getLayoutParams().width > 0) {
            availableWidth = Math.min(widthSize, getLayoutParams().width);
        } else {
            availableWidth = widthSize;
        }

        List<View> visibleChildViews = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                visibleChildViews.add(childView);
            }
        }

        int maxWidthRequestedByChild = 0;

        for (View visibleChild : visibleChildViews) {
            visibleChild.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));

            if (visibleChild.getMeasuredWidth() > maxWidthRequestedByChild) {
                maxWidthRequestedByChild = visibleChild.getMeasuredWidth();
            }
        }

        boolean isEnoughSpace = maxWidthRequestedByChild * getChildCount() < availableWidth;

        int childWidth;
        if (isEnoughSpace) {
            childWidth = maxWidthRequestedByChild;
        } else if (availableWidth / getChildCount() > mChildMinimumWidth) {
            childWidth = availableWidth / getChildCount();
        } else {
            childWidth = mChildMinimumWidth;
        }

        for (View visibleChild : visibleChildViews) {
            visibleChild.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
        }

        if (isEnoughSpace && getLayoutParams().width == LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(childWidth * getChildCount(), heightSize);
        } else {
            setMeasuredDimension(availableWidth, heightSize);
        }
    }

    private Rect computeChildFrame(View childView, int index) {
        int offset = (getMeasuredWidth() - childView.getMeasuredWidth()) / (getChildCount() - 1);
        int left = index * offset;
        int top = computeTopOffset(((LayoutParams) childView.getLayoutParams()).mIsExpanded);
        return new Rect(left, top, left + childView.getMeasuredWidth(), top + childView.getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout");
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            Rect frame = computeChildFrame(child, i);
            child.layout(frame.left, frame.top, frame.right, frame.bottom);
        }
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
        boolean expanded = ((LayoutParams) child.getLayoutParams()).mIsExpanded;
        ((LayoutParams) child.getLayoutParams()).mIsExpanded = !expanded;
        boolean currentExpanded = !expanded;

        Interpolator interpolator;
        if (currentExpanded) {
           interpolator = new AccelerateInterpolator();
        } else {
            interpolator = new DecelerateInterpolator();
        }
        final long topOffset = computeTopOffset(expanded);

        final Animation animation = currentExpanded ? createAnimation(-topOffset, duration, interpolator) : createAnimation(topOffset, duration, interpolator);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(final Animation animation) {

            }

            @Override
            public void onAnimationEnd(final Animation anim) {
                animation.setAnimationListener(null);
                anim.setAnimationListener(null);
                anim.cancel();
                requestLayout();
            }

            @Override
            public void onAnimationRepeat(final Animation animation) {

            }
        });
        child.setAnimation(animation);
        child.animate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int x = Math.round(ev.getX());
        int y = Math.round(ev.getY());

        boolean result = false;

        for (int i=getChildCount() - 1; i >= 0; i--){
            View child = getChildAt(i);
            if(x > child.getLeft() && x < child.getRight() && y > child.getTop() && y < child.getBottom()){
                if(ev.getAction() == MotionEvent.ACTION_UP){
                    switchState(i);
                    break;
                }
            }
        }

        Log.d(TAG, "onTouchEvent: " + result);
        return true;
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
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        Log.d(TAG, "generateLayoutParams");
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    public void addView(final View child) {
        Log.d(TAG, "addView");
        super.addView(child);
    }

    public static class LayoutParams extends RelativeLayout.LayoutParams {
        private boolean mIsExpanded;

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a =
                    context.obtainStyledAttributes(attrs, R.styleable.CardsLayout_Layout);

            mIsExpanded = a.getInt(R.styleable.CardsLayout_Layout_state, -1) == 0;

            a.recycle();
        }
    }
}
