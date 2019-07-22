package com.l024.smilegirl.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

import com.l024.smilegirl.utils.CommonUtils;

/**
 * 自定义SurfaceView
 */
public class MySurfaceView2 extends SurfaceView {
    //画笔
    private Paint paint;
    //宽度
    private int widthSize;
    //圆的半径
    private int height;

    public MySurfaceView2(Context context) {
        super(context);
        initView();
    }

    public MySurfaceView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MySurfaceView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    //自定义View尺寸的规则
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthSize = MeasureSpec.getSize(widthMeasureSpec);//父类的宽度
        int screenWidth = CommonUtils.getScreenWidth(getContext());//屏幕的宽度
        int screenHeight = CommonUtils.getScreenHeight(getContext());//屏幕的高度
        height = screenHeight/2+20;//适配HUAWEIP20的高度
        //缩小屏幕的0.55倍
        double screenWidth1= 0.55*screenWidth;//即屏幕的宽度*0.55，绘制的surfaceview的宽度
        double screenHeight1= 0.55*screenHeight;//即屏幕的高度*0.55，绘制的surfaceView的高度
        Log.e("onMeasure", "widthSize="+widthSize);
        Log.e("onMeasure", "draw: widthMeasureSpec = " +screenWidth + "  heightMeasureSpec = " + screenHeight);
        //绘制的输入参数必须是整数型，做浮点型运算后为float型数据，故需要做取整操作
        setMeasuredDimension((int) screenWidth1, (int) screenHeight1);
    }

    @Override
    //绘制一个圆形的框，并设置圆框的坐标和半径大小
    public void draw(Canvas canvas) {
        Log.e("onDraw", "draw: test");
        Path path = new Path();
        path.addCircle(widthSize / 2, height/ 2, widthSize / 2, Path.Direction.CCW);
        canvas.clipPath(path, Region.Op.REPLACE);
        super.draw(canvas);
    }

    //绘制图形
    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("onDraw", "onDraw");
        super.onDraw(canvas);
    }

    //在控件大小发生改变时调用。所以这里初始化会被调用一次
    //作用：获取控件的宽和高度
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int screenWidth = CommonUtils.getScreenWidth(getContext());
        int screenHeight = CommonUtils.getScreenHeight(getContext());
        Log.d("screenWidth",Integer.toString(screenWidth));
        Log.d("screenHeight",Integer.toString(screenHeight));
        w = screenWidth;
        h = screenHeight;
        super.onSizeChanged(w, h, oldw, oldh);
    }
    //初始化视图
    private void initView() {
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
    }
}
