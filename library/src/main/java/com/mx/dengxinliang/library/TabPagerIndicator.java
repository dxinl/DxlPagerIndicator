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

    private int textColor;
    private float textSize;

    private float underRectNormalHeight;
    private float underRectSelectedHeight;

    private int bkgColor;
    private int clickedBkgColor;
    private int normalColor;
    private int selectedColor;

    private int indicatorCount;
    private float itemWidth;

    private int prePosition;
    private int currentPosition;
    private int currentPositionOffset;

    private int[] location;
    private int currentTouchedPosition;
    private boolean isOutOfView;

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
        clickedBkgColor = array.getColor(R.styleable.TabPagerIndicator_clicked_bkg_color,
                getResources().getColor(android.R.color.holo_orange_light));
        normalColor = array.getColor(R.styleable.TabPagerIndicator_under_rect_normal_color,
                getResources().getColor(android.R.color.darker_gray));
        selectedColor = array.getColor(R.styleable.TabPagerIndicator_under_rect_selected_color,
                getResources().getColor(android.R.color.holo_red_light));
        underRectNormalHeight = array.getDimension(R.styleable.TabPagerIndicator_under_rect_normal_height,
                getResources().getDimension(R.dimen.default_under_rect_normal_height));
        underRectSelectedHeight = array.getDimension(R.styleable.TabPagerIndicator_under_rect_selected_height,
                getResources().getDimension(R.dimen.default_under_rect_selected_height));

        array.recycle();

        initData();
    }

    private void initData() {
        location = new int[2];
        getLocationOnScreen(location);
        isOutOfView = false;
        currentTouchedPosition = -1;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rect = new Rect();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        getLocationOnScreen(location);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            getLocationOnScreen(location);
        }
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

        // draw view background
        paint.setColor(bkgColor);
        rect.set(0, 0, getWidth(), getHeight());
        canvas.drawRect(rect, paint);

        // calculate the count of pages
        if (mViewPager != null) {
            indicatorCount = mViewPager.getAdapter().getCount();
        }

        if (indicatorCount > 0) {
            itemWidth = getWidth() / indicatorCount;

            // draw background of the item that you touched
            if (currentTouchedPosition != -1) {
                paint.setColor(clickedBkgColor);

                rect.set((int) (currentTouchedPosition * itemWidth), 0,
                        (int) ((currentTouchedPosition + 1) * itemWidth), getHeight());
                canvas.drawRect(rect, paint);
            }

            // calculate the edges of the item
            int totalHeight = (int) (textSize * 2 + underRectSelectedHeight);
            int textBottom;
            if (totalHeight > getHeight()) {
                Log.d(TAG, "Height that you had set is smaller than actual height. This view will be show incompletely");
                textBottom = (int) textSize;
            } else {
                textBottom = (int) (getHeight() - underRectSelectedHeight);
            }

            // draw title and default under_rect
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

            // draw under_rect for the item that you selected
            int position;
            paint.setColor(selectedColor);
            float offset = itemWidth / getWidth() * currentPositionOffset;
            if (prePosition != currentPosition) {
                position = prePosition;
                prePosition = currentPosition;
            } else {
                position = currentPosition;
            }
            int left = (int) (itemWidth * position + offset);
            int right = (int) (itemWidth * (position + 1) + offset);
            int top = (int) (getHeight() - underRectSelectedHeight);
            int bottom = getHeight();
            rect.set(left, top, right, bottom);
            canvas.drawRect(rect, paint);
            System.out.println(left);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentTouchedPosition = getItemTouched(x);
                isOutOfView = false;
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                if (!isOutOfView) {
                    mViewPager.setCurrentItem(currentTouchedPosition);
                    currentTouchedPosition = -1;
                    isOutOfView = false;
                    invalidate();
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (currentTouchedPosition != getItemTouched(x)) {
                    Log.e(TAG, "out");
                    isOutOfView = true;
                    currentTouchedPosition = -1;
                }
                invalidate();
                return true;
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
        prePosition = currentPosition = position;
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
     * Draw text on the center of rect
     *
     * @param canvas
     * @param paint
     * @param text
     * @param destRect
     */
    public static void drawTextCenter(Canvas canvas, Paint paint, String text, Rect destRect) {
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();

        int baseLine = destRect.top + (destRect.bottom - destRect.top - fontMetricsInt.bottom + fontMetricsInt.top) / 2 - fontMetricsInt.top;
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, destRect.centerX(), baseLine, paint);
    }

    /**
     * which item you toching
     *
     * @param x
     * @return
     */
    private int getItemTouched(float x) {
        for (int i = 0; i < indicatorCount; i++) {
            if (x > location[0] + itemWidth * i && x < location[0] + itemWidth * (i + 1)) {
                return i;
            }
        }

        return -1;
    }

    public void setUnderRectNormalHeight(int height) {
        underRectNormalHeight = height;
        invalidate();
    }

    public void setUnderRectSelectedHeight(int height) {
        underRectSelectedHeight = height;
        invalidate();
    }

    public void setTitleSize(int size) {
        textSize = size;
        invalidate();
    }

    public void setTitleColor(int color) {
        textColor = color;
        invalidate();
    }

    public void setBkgColor(int color) {
        bkgColor = color;
        invalidate();
    }

    public void setNormalColor(int color) {
        normalColor = color;
        invalidate();
    }

    public void setSelectedColor(int color) {
        selectedColor = color;
        invalidate();
    }

    public void setClickedBkgColor(int color) {
        clickedBkgColor = color;
        invalidate();
    }
}
