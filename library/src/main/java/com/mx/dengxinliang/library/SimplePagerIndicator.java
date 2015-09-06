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
import android.view.View;

/**
 * Created by dxinliang on 9/5/15.
 */
public class SimplePagerIndicator extends View implements PagerIndicator, ViewPager.OnPageChangeListener {
    private final String TAG = "SimplePagerIndicator";
    private final String CIRCLE_STYLE = "circle";
    private final String RECT_STYLE = "rect";

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener onPageChangeListener;

    private Paint paint;
    private Rect rect;

    private int normalColor;
    private int selectedColor;
    private float circleRadius;
    private float rectWidth;
    private float rectHeight;
    private float dividerSize;
    private String preferencesStyle;

    private float currentPositionOffset;
    private int currentPosition = 0;

    public SimplePagerIndicator(Context context) {
        this(context, null);
    }

    public SimplePagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimplePagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SimplePagerIndicator);
        getAttrs(array);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SimplePagerIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SimplePagerIndicator);
        getAttrs(array);
    }

    private void getAttrs(TypedArray array) {
        normalColor = array.getColor(R.styleable.SimplePagerIndicator_normal_color,
                getResources().getColor(android.R.color.darker_gray));
        selectedColor = array.getColor(R.styleable.SimplePagerIndicator_selected_color,
                getResources().getColor(android.R.color.holo_red_light));
        circleRadius = array.getDimension(R.styleable.SimplePagerIndicator_circle_radius,
                getResources().getDimension(R.dimen.default_circle_radius));
        rectWidth = array.getDimension(R.styleable.SimplePagerIndicator_rect_width,
                getResources().getDimension(R.dimen.default_rect_width));
        rectHeight = array.getDimension(R.styleable.SimplePagerIndicator_rect_height,
                getResources().getDimension(R.dimen.default_rect_height));
        preferencesStyle = array.getString(R.styleable.SimplePagerIndicator_preferences_style);
        dividerSize = array.getDimension(R.styleable.SimplePagerIndicator_indicator_divider_size,
                preferencesStyle.equals(RECT_STYLE) ?
                        getResources().getDimension(R.dimen.default_rect_divider) : getResources().getDimension(R.dimen.default_circle_divider));
        array.recycle();

        if (!preferencesStyle.equals(CIRCLE_STYLE) && !preferencesStyle.equals(RECT_STYLE)) {
            preferencesStyle = CIRCLE_STYLE;
            Log.e(TAG, "This setting will be not working. Preferences Style can be either rect or circle. It will be default(circle)");
        }

        if (circleRadius >= 0 && preferencesStyle.equals(RECT_STYLE)) {
            Log.e(TAG, "This setting will be not working, \"circle_radius\" can only work when the preferences style is \"circle\".");
        }

        if ((rectHeight >= 0 || rectWidth >= 0) && preferencesStyle.equals(CIRCLE_STYLE)) {
            Log.e(TAG, "This setting will be not working, \"rect_height\" or \"rect_width\" can only be working when the preferences style is \"rect\".");
        }

        initData();
    }

    private void initData() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (preferencesStyle.equals(RECT_STYLE)) {
            rect = new Rect();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize;

        if (heightMode == MeasureSpec.EXACTLY) {
            Log.w(TAG, "This setting of Height will be not working, \"wrap_content\" is recommended.");
        }

        if (preferencesStyle.equals(RECT_STYLE)) {
            if (rectWidth < 0) {
                rectWidth = -rectWidth;
            }
            if (rectHeight < 0) {
                rectHeight = -rectHeight;
            }

            heightSize = (int) (2 * rectHeight);
        } else {
            if (circleRadius < 0) {
                circleRadius = -circleRadius;
            }

            heightSize = (int) (2 * circleRadius);
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int indicatorCount = 0;
        if (mViewPager != null) {
            indicatorCount = 3;
        }

        paint.setColor(normalColor);

        if (preferencesStyle.equals(RECT_STYLE)) {
            float totalWidth = rectWidth * indicatorCount + dividerSize * (indicatorCount - 1);
            float paddingLeft;
            float top = rectHeight / 2;
            float bottom = top + rectHeight;

            if (totalWidth > getWidth()) {
                Log.e(TAG, "Width that you had set is smaller than actual width. This view will be show incompletely");
                paddingLeft = 0f;
            } else {
                paddingLeft = (getWidth() - totalWidth) / 2;
            }

            if (rect == null) {
                rect = new Rect();
            }

			for (int i = 0; i < indicatorCount; i++) {
				float left = paddingLeft + dividerSize * i + rectWidth * i;
				float right = left + rectWidth;
				rect.set((int) left, (int) top, (int) right, (int) bottom);
				canvas.drawRect(rect, paint);
			}

			paint.setColor(selectedColor);
			float offset = (rectWidth + dividerSize) / ((View)getParent()).getWidth() * currentPositionOffset;
			float left = paddingLeft + dividerSize * currentPosition + rectWidth * currentPosition + offset;
			float right = left + rectWidth;
			rect.set((int) left, (int) top, (int) right, (int) bottom);
			canvas.drawRect(rect, paint);
        } else {

        }
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
}
