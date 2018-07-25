package cf.android666.applibrary;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ToggleButton;

/**
 * Created by jixiaoyong on 2018/2/18.
 */

public class ToggleButtonPlus extends ToggleButton {

    private boolean isChecked = false;

    private int mBackgroundColor;
    private int mForwardColor;
    private int mStrokeColor;

    private int mHeight;
    private int mWidth;

    //小球圆心坐标
    private float cx;
    private float cy;

    public ToggleButtonPlus(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.ToggleButtonPlus, defStyleAttr, defStyleRes);

        mBackgroundColor = array.getColor(R.styleable.ToggleButtonPlus_background_color, 0xff09bb07);
        mForwardColor = array.getColor(R.styleable.ToggleButtonPlus_forward_color, 0xfff5f5f5);
        mStrokeColor = array.getColor(R.styleable.ToggleButtonPlus_stroke_color, 0xddaaaaab);

    }

    public ToggleButtonPlus(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ToggleButtonPlus(Context context, AttributeSet attrs) {
        this(context, attrs, 0, 0);
    }

    public ToggleButtonPlus(Context context) {
        this(context, null, 0, 0);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        int circleR = mHeight / 2;

        Paint backgroundPaint = new Paint();
        RectF rectF = new RectF(0, 0, mWidth, mHeight);

        Paint forwardPaint = new Paint();
        forwardPaint.setColor(mForwardColor);

        if (isChecked) {
            backgroundPaint.setColor(mBackgroundColor);
        } else {
            backgroundPaint.setColor(mForwardColor);
        }

        canvas.drawRoundRect(rectF, circleR, circleR, backgroundPaint);
        canvas.drawCircle(cx, cy, circleR, forwardPaint);

        forwardPaint.setStyle(Paint.Style.STROKE);
        forwardPaint.setColor(mStrokeColor);
        forwardPaint.setStrokeWidth(3);
        canvas.drawCircle(cx, cy, circleR, forwardPaint);

        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setColor(mStrokeColor);
        backgroundPaint.setStrokeWidth(3);
        canvas.drawRoundRect(rectF, circleR, circleR, backgroundPaint);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = 100;
            if (widthMode == MeasureSpec.AT_MOST) {
                mWidth = Math.min(mWidth, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = 50;
            if (heightMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(mHeight, heightSize);
            }
        }

        int widthSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
        //使父布局为button分配对应大小控件
        super.onMeasure(widthSpec, heightSpec);


        cx = mHeight / 2;
        cy = mHeight / 2;
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
    }

    private void refresh() {

        isChecked = !isChecked;

        ValueAnimator animator;

        if (isChecked) {
            cx = mWidth - cy;
            animator = ValueAnimator.ofFloat(cy, cx);
        } else {
            cx = cy;
            animator = ValueAnimator.ofFloat(mWidth - cy, cx);
        }

        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                cx = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                refresh();
                break;
        }

        return super.onTouchEvent(event);
    }
}
