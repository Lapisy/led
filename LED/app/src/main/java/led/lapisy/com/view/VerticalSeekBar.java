package led.lapisy.com.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.icu.util.Measure;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import led.lapisy.com.led.R;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2018/4/21
 * Desc  :
 */

public class VerticalSeekBar extends View {

    private Drawable mBackgroudDrawable;
    private Drawable mThumbDrawable;
    private int mDistance;
    private int mViewWidth;
    private int mViewHeight;
    private int mMiddleX;
    private int mMiddleY;
    private int startY;
    private int mThumbWidth;
    private int mThumbHeight;
    private boolean mIsDrag;
    private OnSlideListener mOnSlideListener;
    private float mPersent;

    public VerticalSeekBar(Context context) {
        this(context, null);
    }

    public VerticalSeekBar(Context context, @Nullable AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public VerticalSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VerticalSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mBackgroudDrawable = context.getResources().getDrawable(R.drawable.ic_color_slide_background);
        mThumbDrawable = context.getResources().getDrawable(R.drawable.ic_color_slide_thumb);

        //drawable
        if (mThumbDrawable == null)
            return;
        mThumbWidth = mThumbDrawable.getIntrinsicWidth();
        mThumbHeight = mThumbDrawable.getIntrinsicHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureValue(widthMeasureSpec, true);
        int height = measureValue(heightMeasureSpec, false);
        setMeasuredDimension(width, height);
    }

    private int measureValue(int value, boolean isWidth) {
        int size = MeasureSpec.getSize(value);
        int mode = MeasureSpec.getMode(value);
        if (mode == MeasureSpec.EXACTLY) {
            return size;
        } else {
            if (mBackgroudDrawable == null)
                throw new RuntimeException("Thumb Drawable is null");
            return isWidth ? Math.max(mBackgroudDrawable.getIntrinsicWidth(), mThumbDrawable.getIntrinsicWidth())
                    : mBackgroudDrawable.getIntrinsicHeight();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewHeight = h;
        mViewWidth = w;
        mMiddleX = mViewWidth / 2;
        mMiddleY = mViewHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBackgroudDrawable != null) {
            int w = mBackgroudDrawable.getIntrinsicWidth();
            Rect bgBound = new Rect(mMiddleX - w / 2, 0, mMiddleX + w / 2, mViewHeight);
            mBackgroudDrawable.setBounds(bgBound);
            mBackgroudDrawable.draw(canvas);
        }
//        //drawable
//        if (mThumbDrawable == null)
//            return;
//        mThumbWidth = mThumbDrawable.getIntrinsicWidth();
//        mThumbHeight = mThumbDrawable.getIntrinsicHeight();
        Rect bound = new Rect(mMiddleX - mThumbWidth / 2, mDistance, mMiddleX + mThumbWidth / 2, mDistance + mThumbHeight);
        mThumbDrawable.setBounds(bound);
        mThumbDrawable.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) (event.getY() + 0.5f);
                int startX = (int) event.getX();
                if (startX >= mMiddleX - mThumbWidth / 2 && startX <= mMiddleX + mThumbWidth / 2
                        && startY >= mDistance && startY <= mDistance + mThumbHeight) {
                    mIsDrag = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsDrag) {
                    mDistance += ((int) (event.getY() - startY));
                    if (mDistance < 0)
                        mDistance = 0;
                    if (mDistance > mViewHeight - mThumbHeight)
                        mDistance = mViewHeight - mThumbHeight;
                    startY = (int) (event.getY() + 0.5f);

                    if (mOnSlideListener != null) {
                        mOnSlideListener.onSlide(mDistance * 1.0f / (mViewHeight - mThumbHeight));
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mIsDrag = false;
            case MotionEvent.ACTION_UP:
                mIsDrag = false;
                if (mOnSlideListener != null) {
                    float percent = mDistance * 1.0f / (mViewHeight - mThumbHeight);
                    percent = Math.min(percent, 1.0f);
                    mOnSlideListener.onSlideComplete(percent);
                }
                break;
        }
        invalidate();
        return true;
    }

    public void setProgress(final float persent) {
        this.mPersent = persent;
        this.post(new Runnable() {
            @Override
            public void run() {
                mDistance = (int) ((mViewHeight - mThumbHeight) * persent);
                invalidate();
            }
        });
    }

    public void setOnSlideListener(OnSlideListener listener) {
        if (listener != null)
            mOnSlideListener = listener;
    }

    public static interface OnSlideListener {
        void onSlide(float percent);

        void onSlideComplete(float percent);
    }

}
