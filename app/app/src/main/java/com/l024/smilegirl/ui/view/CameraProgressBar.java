package com.l024.smilegirl.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;

import com.l024.smilegirl.R;

/**
 * 自定义圆形进度条
 */
public class CameraProgressBar extends View {

    /**
     * 默认缩小值
     */
    public static final float DEF_SCALE = 0.75F;
    /**
     * 默认缩小值
     */
    private float scale = DEF_SCALE;

    /**
     * 内圆颜色
     */
    private int innerColor = Color.BLACK;
    /**
     * 背景颜色
     */
    private int backgroundColor = Color.WHITE;
    /**
     * 外圆颜色
     */
    private int outerColor = Color.parseColor("#e8e8e8");
    /**
     * 进度颜色
     */
    private int progressColor = Color.parseColor("#0ebffa");
    /**
     * 进度宽
     */
    private int progressWidth = 40;
    /**
     * 内圆宽度
     */
    private int innerRadio = 10;
    /**
     * 进度
     */
    private int progress;
    /**
     * 最大进度
     */
    private int maxProgress = 100;
    /**
     * paint
     */
    private Paint backgroundPaint, progressPaint, innerPaint;
    /**
     * 圆的中心坐标点, 进度百分比
     */
    private float sweepAngle;
    /**
     * 手识识别
     */
    private GestureDetectorCompat mDetector;
    /**
     * 是否为长按录制
     */
    private boolean isLongClick;
    /**
     * 是否产生滑动
     */
    private boolean isBeingDrag;
    /**
     * 滑动单位
     */
    private int mTouchSlop;
    /**
     * 记录上一次Y轴坐标点
     */
    private float mLastY;
    /**
     * 是否长按放大
     */
    private boolean isLongScale;


    public CameraProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    public CameraProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public CameraProgressBar(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    //初始化
    private void init(Context context, AttributeSet attrs){
        //它获得的是触发移动事件的最短距离，如果小于这个距离就不触发移动控件
        // 如viewpager就是用这个距离来判断用户是否翻页
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CameraProgressBar);
            innerColor = a.getColor(R.styleable.CameraProgressBar_innerColor, innerColor);
            outerColor = a.getColor(R.styleable.CameraProgressBar_outerColor, outerColor);
            progressColor = a.getColor(R.styleable.CameraProgressBar_progressColor, progressColor);
            innerRadio = a.getDimensionPixelOffset(R.styleable.CameraProgressBar_innerRadio, innerRadio);
            progressWidth = a.getDimensionPixelOffset(R.styleable.CameraProgressBar_progressWidth, progressWidth);
            progress = a.getInt(R.styleable.CameraProgressBar_progress, progress);
            scale = a.getFloat(R.styleable.CameraProgressBar_scale, scale);
            isLongScale = a.getBoolean(R.styleable.CameraProgressBar_isLongScale, isLongScale);
            maxProgress = a.getInt(R.styleable.CameraProgressBar_maxProgress, maxProgress);
            a.recycle();
        }
    }
}
