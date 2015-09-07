package com.mx.dengxinliang.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by DengXinliang on 2015/9/6.
 */
public class TabPagerIndicator extends View implements PagerIndicator, ViewPager.OnPageChangeListener {
    private static final int[] ATTRS = new int[]{
            android.R.attr.textSize,
            android.R.attr.textColor
    };

    private final String TAG = "TabPagerIndicator";

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private Paint paint;
    private Rect rect;

    private String[] titles;

    private int textColor;
    private float textSize;

    private float underRectNormalHeight;
    private float underRectSelectedHeight;

    private int bkgColor;
    private int normalColor;
    private int selectedColor;

    private float itemWidth;

    private int currentPosition;
    private int currentPositionOffset;

    public TabPagerIndicator(Context context) {
        this(context, null);
    }

    public TabPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, ATTRS);
        getSystemAttrs(array);

        array = context.obtainStyledAttributes(attrs, R.styleable.TabPagerIndicator);
        getAttrs(array);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TabPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray array = context.obtainStyledAttributes(attrs, ATTRS);
        getSystemAttrs(array);

        array = context.obtainStyledAttributes(attrs, R.styleable.TabPagerIndicator);
        getAttrs(array);
    }

    /**
     * get android system attributes: android:textColor & android:textSize
     *
     * @param array
     */
    private void getSystemAttrs(TypedArray array) {
        textColor = array.getColor(1,
                getResources().getColor(android.R.color.darker_gray));
        textSize = array.getDimension(0,
                getResources().getDimension(R.dimen.default_text_size));
        array.recycle();
    }

    private void getAttrs(TypedArray array) {
        bkgColor = array.getColor(R.styleable.TabPagerIndicator_bkg_color,
                getResources().getColor(android.R.color.white));
        normalColor = array.getColor(R.styleable.TabPagerIndicator_under_rect_normal_color,
                getResources().getColor(android.R.color.darker_gray));
        selectedColor = array.getColor(R.styleable.TabPagerIndicator_under_rect_selected_color,
                getResources().getColor(android.R.color.holo_red_light));
        underRectNormalHeight = array.getDimension(R.styleable.TabPagerIndicator_under_rect_normal_height,
                getResources().getDimension(R.dimen.default_under_rect_normal_height));
        underRectSelectedHeight = array.getDimension(R.styleable.TabPagerIndicator_under_rect_selected_height,
                getResources().getDimension(R.dimen.default_under_rect_selected_height));
        Log.e(TAG, String.valueOf(underRectSelectedHeight));
        array.recycle();

        initData();
    }

    private void initData() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize;

        int parentWidth = ((View) getParent()).getWidth();
        if (widthSize != parentWidth) {
            Log.d(TAG, "\"layout_width\" must be \"match_parent\". Your setting will be not working.");
            widthSize = parentWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            heightSize = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            heightSize = (int) (textSize * 2 + underRectSelectedHeight);
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(bkgColor);
        rect.set(0, 0, getWidth(), getHeight());
        canvas.drawRect(rect, paint);

        int indicatorCount = 0;
        if (mViewPager != null) {
            indicatorCount = mViewPager.getAdapter().getCount();
        }

        if (indicatorCount > 0) {
            itemWidth = getWidth() / indicatorCount;

            int totalHeight = (int) (textSize * 2 + underRectSelectedHeight);
            int textBottom;
            if (totalHeight > getHeight()) {
                Log.d(TAG, "Height that you had set is smaller than actual height. This view will be show incompletely");
                textBottom = (int) textSize;
            } else {
                textBottom = (int) (getHeight() - underRectSelectedHeight);
            }

            for (int i = 0; i < indicatorCount; i++) {
                float paddingLeft = itemWidth * i;
                rect.set((int) paddingLeft, 0, (int) (itemWidth + paddingLeft), textBottom);
                paint.setColor(textColor);
                paint.setTextSize(textSize);
                drawTextCenter(canvas, paint, mViewPager.getAdapter().getPageTitle(i).toString(), rect);

                rect.set((int) paddingLeft, (int) (getHeight() - underRectNormalHeight),
                        (int) (paddingLeft + itemWidth), getHeight());
                paint.setColor(normalColor);
                canvas.drawRect(rect, paint);
            }

            paint.setColor(selectedColor);
            float offset = itemWidth / getWidth() * currentPositionOffset;
            int left = (int) (itemWidth * currentPosition + offset);
            int right = (int) (itemWidth * (currentPosition + 1) + offset);
            int top = (int) (getHeight() - underRectSelectedHeight);
            int bottom = getHeight();
            rect.set(left, top, right, bottom);
            canvas.drawRect(rect, paint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);
        invalidate();
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        currentPosition = position;
        currentPositionOffset = positionOffsetPixels;

        invalidate();

        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;

        invalidate();
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    /**
     * 绘制居中文字
     *
     * @param canvas   画布
     * @param paint    画笔
     * @param text     文本
     * @param destRect 目标方块
     */
    public static void drawTextCenter(Canvas canvas, Paint paint, String text, Rect destRect) {
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();

        int baseLine = destRect.top + (destRect.bottom - destRect.top - fontMetricsInt.bottom + fontMetricsInt.top) / 2 - fontMetricsInt.top;
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, destRect.centerX(), baseLine, paint);
    }
}
