package com.foxmail.aroundme.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by gzl on 1/16/17.
 */

public class RoundRectLabelView extends View {

    private Paint mLabelTextPaint;
    private int mLabelTextColor = Color.YELLOW;
    private float mLabelTextSize = sp2px(8);
    private String mLabelText = "Hot";
    private int mLabelBgColor = Color.parseColor("#66000000");
    private Paint mTrianglePaint;

    private Paint mRoundRectPaint;

    //控件总宽高
    private int width;
    private int height;

    //控件需要实现的宽高
    private double setWidth;
    private double setHeight;

    //宽度的权重
    private int weightW = 2;
    //高度的权重
    private int weightH = 2;

    public RoundRectLabelView(Context context) {
        this(context, null);
    }

    public RoundRectLabelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundRectLabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LabelTextView);

        mLabelBgColor = typedArray.getColor(R.styleable.LabelTextView_labelBgColor, mLabelBgColor);
        mLabelTextColor = typedArray.getColor(R.styleable.LabelTextView_labelTextColor, mLabelTextColor);
        mLabelTextSize = typedArray.getDimension(R.styleable.LabelTextView_labelTextSize, mLabelTextSize);
        mLabelText = typedArray.getString(R.styleable.LabelTextView_labelText);
        weightW = typedArray.getInteger(R.styleable.LabelTextView_labelWidthWeight, weightW);
        weightH = typedArray.getInteger(R.styleable.LabelTextView_labelHeightWeight, weightH);

        typedArray.recycle();

        if(weightW <= 0 || weightH <= 0) {
            throw new RuntimeException("labelWidthWeight or labelHeightWeight must be greater than 0");
        }

        initTextPaint();
        initTrianglePaint();
        initRoundRectPaint();
        resetTextStatus();
    }

    private void initTextPaint() {
        //初始化绘制修饰文本的画笔
        mLabelTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLabelTextPaint.setColor(mLabelTextColor);
        mLabelTextPaint.setTextAlign(Paint.Align.CENTER);
        mLabelTextPaint.setTextSize(mLabelTextSize);
        mLabelTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mLabelTextPaint.setAntiAlias(true);

    }

    private void initTrianglePaint(){
        //初始化绘制三角形背景的画笔
        mTrianglePaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        mTrianglePaint.setColor(mLabelBgColor);
        mTrianglePaint.setAntiAlias(true);
    }

    private void initRoundRectPaint() {
        //绘制圆角矩形画笔
        mRoundRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRoundRectPaint.setColor(mLabelBgColor);
        mRoundRectPaint.setStyle(Paint.Style.STROKE);
        mRoundRectPaint.setAntiAlias(true);
    }

    private void resetTextStatus() {
        // 测量文字高度
        Rect rectText = new Rect();
        mLabelTextPaint.getTextBounds(mLabelText, 0, mLabelText.length(), rectText);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        setWidth = width / weightW;
        setHeight = height / weightH;

    }

    @TargetApi(19)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制三角形背景
        Path pathTriangle = new Path();
        pathTriangle.moveTo(0, double2Float(setHeight));
        pathTriangle.lineTo(0, 0);
        pathTriangle.lineTo(double2Float(setWidth), 0);
        pathTriangle.close();

        Path pathRountRect = new Path();
        //绘制圆角矩形
        RectF r = new RectF();
        r.left = 0;
        r.top = 0;
        r.right = width;
        r.bottom = height;
        pathRountRect.addRoundRect(r, 32, 32, Path.Direction.CCW);
        pathTriangle.op(pathRountRect,  Path.Op.INTERSECT);
        canvas.drawPath(pathTriangle, mTrianglePaint);
        canvas.drawPath(pathRountRect, mRoundRectPaint);

        //画文字
        Path pathLine = new Path();
        pathLine.moveTo(0, double2Float(setHeight));
        pathLine.lineTo(double2Float(setWidth), 0);
        canvas.drawTextOnPath(mLabelText, pathLine, -10, -10, mLabelTextPaint);
    }

    public float double2Float(double dou) {
        return Float.parseFloat(String.valueOf(dou));
    }

    public int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public float sp2px(float spValue) {
        final float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return spValue * scale;
    }
}
