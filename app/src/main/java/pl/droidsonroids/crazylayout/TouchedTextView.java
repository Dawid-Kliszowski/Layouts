package pl.droidsonroids.crazylayout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class TouchedTextView extends TextView {
    private final String TAG = this.getClass().getSimpleName();

    public TouchedTextView(final Context context) {
        super(context);
    }

    public TouchedTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchedTextView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
    protected void dispatchDraw(final Canvas canvas) {
        Log.d(TAG, "requestLayout");
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
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout");
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
