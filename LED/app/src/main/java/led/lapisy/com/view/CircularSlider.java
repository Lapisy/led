
package led.lapisy.com.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;

import led.lapisy.com.led.R;
import led.lapisy.com.util.ReflectUtil;

public class CircularSlider extends View {
    private int mThumbX;
    private int mThumbY;
    private RectF mRectF;
    private int mCircleCenterX;
    private int mCircleCenterY;
    private int mCircleRadius;

    private Drawable mThumbImage;
    private int mPadding;
    private int mThumbSize;
    private int mThumbColor;
    private int mBorderColor;
    private int[] mBorderGradientColors;
    private int mBorderThickness;
    private double mStartAngle;
    private double mAngle = mStartAngle;
    private boolean mIsThumbSelected = false;

    private Paint mPaint = new Paint();
    private SweepGradient mGradientShader;
    private OnSliderMovedListener mListener;

    public CircularSlider(Context context) {
        this(context, null);
    }

    public CircularSlider(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircularSlider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularSlider, defStyleAttr, 0);

        float startAngle = a.getFloat(R.styleable.CircularSlider_start_angle, -(float) Math.PI / 2);
        float angle = a.getFloat(R.styleable.CircularSlider_angle, 0);
        int thumbSize = a.getDimensionPixelSize(R.styleable.CircularSlider_thumb_size, 50);
        int thumbColor = a.getColor(R.styleable.CircularSlider_thumb_color, Color.GRAY);
        int borderThickness = a.getDimensionPixelSize(R.styleable.CircularSlider_border_thickness, 20);
        int borderColor = a.getColor(R.styleable.CircularSlider_border_color, Color.RED);
        String borderGradientColors = a.getString(R.styleable.CircularSlider_border_gradient_colors);
        Drawable thumbImage = a.getDrawable(R.styleable.CircularSlider_thumb_image);

        // save those to fields (really, do we need setters here..?)
        setStartAngle(startAngle);
        setAngle(angle);
        setBorderThickness(borderThickness);
        setBorderColor(borderColor);
        if (borderGradientColors != null) {
            setBorderGradientColors(borderGradientColors.split(";"));
        }
        setThumbSize(thumbSize);
        setThumbImage(thumbImage);
        setThumbColor(thumbColor);

        // assign padding - check for version because of RTL layout compatibility
        int padding;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            int all = getPaddingLeft() + getPaddingRight() + getPaddingBottom() + getPaddingTop() + getPaddingEnd() + getPaddingStart();
            padding = all / 6;
        } else {
            padding = (getPaddingLeft() + getPaddingRight() + getPaddingBottom() + getPaddingTop()) / 4;
        }
        setPadding(padding);

        a.recycle();
    }

    /* ***** Setters ***** */
    public void setStartAngle(double startAngle) {
        mStartAngle = startAngle;
    }

    public void setAngle(double angle) {
        mAngle = angle;
    }

    public void setThumbSize(int thumbSize) {
        mThumbSize = thumbSize;
    }

    public void setBorderThickness(int circleBorderThickness) {
        mBorderThickness = circleBorderThickness;
    }

    public void setBorderColor(int color) {
        mBorderColor = color;
    }

    public void setBorderGradientColors(String[] colors) {
        mBorderGradientColors = new int[colors.length];
        for (int i = 0; i < colors.length; i++) {
            mBorderGradientColors[i] = Color.parseColor(colors[i]);
        }
    }

    @SuppressWarnings("unused")
    public void setBorderGradientColors(int[] colors) {
        if (colors == null) {
            mBorderGradientColors = null;
            mGradientShader = null;
        } else {
            mBorderGradientColors = Arrays.copyOf(colors, colors.length);
            mGradientShader = new SweepGradient(mCircleRadius, mCircleRadius, mBorderGradientColors, null);
        }
    }

    public void setThumbImage(Drawable drawable) {
        mThumbImage = drawable;
    }

    public void setThumbColor(int color) {
        mThumbColor = color;
    }

    public void setPadding(int padding) {
        mPadding = padding;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        int smallerDim = w > h ? h : w;

        mCircleCenterX = w / 2;
        mCircleCenterY = h / 2;
        mCircleRadius = smallerDim / 2 - mBorderThickness / 2 - mPadding;
        mRectF = new RectF(mCircleCenterX - mCircleRadius, mCircleCenterY - mCircleRadius, mCircleRadius + mCircleCenterX, mCircleCenterY + mCircleRadius);

        if (mBorderGradientColors != null) {
            mGradientShader = new SweepGradient(mCircleRadius, mCircleRadius, new int[]{Color.WHITE, Color.BLACK}, new float[]{0, 0.5f});
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //draw arc
        mPaint.setColor(mBorderColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderThickness);
        mPaint.setAntiAlias(true);
        if (mGradientShader != null) {
            mPaint.setShader(mGradientShader);
        }
        canvas.drawArc(mRectF, 0, 180, false, mPaint);

        // find thumb position
        mThumbX = (int) (mCircleCenterX + mCircleRadius * Math.cos(mAngle));
        mThumbY = (int) (mCircleCenterY - mCircleRadius * Math.sin(mAngle));

        if (mThumbImage != null) {
            // draw png
            mThumbImage.setBounds(mThumbX - mThumbSize / 2, mThumbY - mThumbSize / 2, mThumbX + mThumbSize / 2, mThumbY + mThumbSize / 2);
            mThumbImage.draw(canvas);
        } else {
            // draw colored circle
            mPaint.setColor(mThumbColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(mThumbX, mThumbY, mThumbSize, mPaint);
        }
    }

    /**
     * Invoked when slider starts moving or is currently moving. This method calculates and sets position and angle of the thumb.
     *
     * @param touchX Where is the touch identifier now on X axis
     * @param touchY Where is the touch identifier now on Y axis
     */
    private void updateSliderState(int touchX, int touchY, boolean isComplete) {
        int distanceX = touchX - mCircleCenterX;
        int distanceY = mCircleCenterY - touchY;
        //noinspection SuspiciousNameCombination
        double c = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
        mAngle = Math.acos(distanceX / c);
        if (distanceY < 0) {
            mAngle = -mAngle;
        }

        if (mListener != null) {
            // notify slider moved listener of the new position which should be in [0..1] range
            if (isComplete) {
                mListener.onSlideCompete(Math.abs(mAngle) / (Math.PI));
            } else {
                mListener.onSliderMoved(Math.abs(mAngle) / (Math.PI));
            }
        }
    }

    /**
     * Position setter. This method should be used to manually position the slider thumb.<br>
     * Note that counterclockwise {@link #mStartAngle} is used to determine the initial thumb position.
     *
     * @param pos Value between 0 and 1 used to calculate the angle. {@code Angle = StartingAngle + pos * 2 * Pi}<br>
     *            Note that angle will not be updated if the position parameter is not in the valid range [0..1]
     */
    @SuppressWarnings("unused")
    public void setPosition(double pos) {
        if (pos >= 0 && pos <= 1) {
            mAngle = mStartAngle + pos * 2 * Math.PI;
        }
    }

    /**
     * Saves a new slider moved listener. Set {@link OnSliderMovedListener} to {@code null} to remove it.
     *
     * @param listener Instance of the slider moved listener, or null when removing it
     */
    @SuppressWarnings("unused")
    public void setOnSliderMovedListener(OnSliderMovedListener listener) {
        mListener = listener;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                if (x < mThumbX + mThumbSize && x > mThumbX - mThumbSize && y < mThumbY + mThumbSize && y > mThumbY - mThumbSize) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    mIsThumbSelected = true;
                    updateSliderState(x, y, false);
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                if (mIsThumbSelected) {
                    int x = (int) ev.getX();
                    int y = (int) ev.getY();
                    //just can drag on the arc
                    if (y < mCircleCenterY)
                        y = mCircleCenterY;
                    updateSliderState(x, y, false);
                }
                break;
            }

            case MotionEvent.ACTION_UP: {
                getParent().requestDisallowInterceptTouchEvent(false);
                mIsThumbSelected = false;
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                if (y < mCircleCenterY)
                    y = mCircleCenterY;
                updateSliderState(x, y, true);
                break;
            }
        }

        // redraw the whole component
        invalidate();
        return true;
    }


    /**
     * Listener interface used to detect when slider moves around.
     */
    @SuppressWarnings("WeakerAccess")
    public interface OnSliderMovedListener {

        /**
         * This method is invoked when slider moves, providing position of the slider thumb.
         *
         * @param pos Value between 0 and 1 representing the current angle.<br>
         *            {@code pos = (Angle - StartingAngle) / (2 * Pi)}
         */
        void onSliderMoved(double pos);

        void onSlideCompete(double pos);
    }

}
